package gregtech.common.entities;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.GT_Explosion_Info;
import gregtech.common.misc.explosions.GT_Explosion_PreCalculation;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import static gregtech.GT_Mod.GT_FML_LOGGER;


@Getter
public class GT_Entity_Explosive extends EntityTNTPrimed implements IEntityAdditionalSpawnData {

    protected int metadata;

    protected double realX, realY, realZ;

    protected GT_Explosion<?> explosion;

    protected int initialFuse;

    protected IGT_ExplosiveTier<?> tier;

    private GT_Explosion_PreCalculation preCalc;

    public GT_Entity_Explosive(final World world) {
        super(world);
    }

    public GT_Entity_Explosive(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata, final int timer, final @NonNull IGT_ExplosiveTier<?> tier) {
        super(world, x + 0.5, y + 0.5, z + 0.5, placedBy);
        this.setSize();
        this.realX       = this.posX;
        this.realY       = this.posY;
        this.realZ       = this.posZ;
        this.motionX     = 0.0;
        this.motionY     = 0.0;
        this.motionZ     = 0.0;
        this.fuse        = timer >= 0 ? timer : GT_Values.MEFuse;
        this.initialFuse = this.fuse;
        this.metadata    = metadata;
        this.tier        = tier;
        createAndInitExplosion();
    }

    protected void setSize() {
        final float newSize = getNewSize();
        this.setSize(newSize, newSize);
    }

    private void createAndInitExplosion() {
        explosion = tier.createExplosion(this);
        preCalc   = new GT_Explosion_PreCalculation(explosion).initialize();
    }

    private float getNewSize() {
        return 0.5f + ((float) (fuse - this.initialFuse)) / (this.initialFuse + 1);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called to update the entity's position/explosion_logic.
     */
    @Override
    public void onUpdate() {
        setPosition(realX, realY, realZ);
        if (preCalc != null) {
            preCalc.tick();
        }
        if (fuse-- <= 0) {
            this.setDead();
            if (!this.worldObj.isRemote) {
                this.doExplode();
            }
        } else {
            final int n = rand.nextInt(2) + 1;
            for (int i = 0; i < n; i++) {
                final double x, y, z;
                x = rand.nextDouble() * 1.4 - 0.7;
                y = rand.nextDouble() * 1.4 - 0.7;
                z = rand.nextDouble() * 1.4 - 0.7;
                this.worldObj.spawnParticle("smoke", posX + x, posY + y, posZ + z, x / 4, y / 4, z / 4);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     *
     * @param compound
     *         The NBT data to write
     */
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("initialFuse", initialFuse);
        compound.setInteger("fuse", fuse);
        compound.setInteger("meta", metadata);
        compound.setDouble("rX", realX);
        compound.setDouble("rY", realY);
        compound.setDouble("rZ", realZ);
        compound.setInteger("tier", tier.getTier());
        compound.setInteger("typeIndex", tier.getTierTrackIndex());
        if (explosion != null) {
            compound.setTag("explosion", explosion.serializeNBT());
        }
        if (preCalc != null) {
            compound.setTag("preCalc", preCalc.serializeNBT());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     *
     * @param compound
     *         The NBT data to read
     */
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.initialFuse = compound.getInteger("initialFuse");
        this.fuse        = compound.getInteger("fuse");
        this.metadata    = compound.getInteger("meta");
        this.realX       = compound.getDouble("rX");
        this.realY       = compound.getDouble("rY");
        this.realZ       = compound.getDouble("rZ");
        this.tier        = getTier(compound);
        if (compound.hasKey("explosion")) {
            val exp = compound.getCompoundTag("explosion");
            explosion = tier.createExplosion(this);
            explosion.deserializeNBT(exp);
            if (compound.hasKey("preCalc")) {
                val pre = compound.getCompoundTag("preCalc");
                preCalc = GT_Explosion_PreCalculation.deserializeNBT(explosion, pre);
            }
        }
    }

    protected @NonNull IGT_ExplosiveTier<?> getTier(final NBTTagCompound compound) {
        val typeIndex  = compound.getInteger("typeIndex");
        val tierInType = compound.getInteger("tier");
        return getTierFromInts(typeIndex, tierInType);
    }

    private @NonNull IGT_ExplosiveTier<?> getTierFromInts(final int typeIndex, final int tierInType) {
        return GT_Explosion_Info.getTierType(typeIndex, tierInType);
    }

    protected void doExplode() {
        if (explosion != null) {
            explosion.perform();
        } else {
            GT_FML_LOGGER.error("Explosion is null! You probably reloaded the world, sorry");
        }
    }

    @Override
    public float func_145772_a(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block
                              ) {
        return tier.getBlockResistance((GT_Explosion<?>) explosion, world, x, y, z, block);
    }

    @Override
    public boolean func_145774_a(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block, final float power
                                ) {
        return canBlockBeExploded((GT_Explosion<?>) explosion, world, x, y, z, block, power);
    }

    @SuppressWarnings("unused")
    public boolean canBlockBeExploded(
            final GT_Explosion<?> explosion, final World world, final int x, final int y, final int z, final Block block, final float power
                                     ) {
        return !(block instanceof GT_Block_Explosive);
    }

    public final float defaultBlockResistance(final GT_Explosion<?> explosion, final World world, final int x, final int y, final int z, final Block block) {
        return super.func_145772_a(explosion, world, x, y, z, block);
    }

    /**
     * Called by the server when constructing the spawn packet. Data should be added to the provided stream.
     *
     * @param buffer
     *         The packet data stream
     */
    @Override
    public void writeSpawnData(final ByteBuf buffer) {
        buffer.writeInt(fuse);
        buffer.writeInt(metadata);
        buffer.writeDouble(realX);
        buffer.writeDouble(realY);
        buffer.writeDouble(realZ);
        buffer.writeInt(tier.getTierTrackIndex());
        buffer.writeInt(tier.getTier());
    }

    /**
     * Called by the client when it receives an Entity spawn packet. Data should be read out of the stream in the same way as it was written.
     *
     * @param additionalData
     *         The packet data stream
     */
    @Override
    public void readSpawnData(final ByteBuf additionalData) {
        this.fuse     = additionalData.readInt();
        this.metadata = additionalData.readInt();
        this.realX    = additionalData.readDouble();
        this.realY    = additionalData.readDouble();
        this.realZ    = additionalData.readDouble();
        val typeIndex = additionalData.readInt();
        this.tier = getTierFromInts(typeIndex, additionalData.readInt());
    }

    public Block getBlockToRenderAs() {
        return tier.getBlock();
    }

}
