package gregtech.common.entities.explosives;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.GT_Explosion_PreCalculation;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import static gregtech.GT_Mod.GT_FML_LOGGER;


@Getter
public abstract class GT_Entity_Explosive extends EntityTNTPrimed implements IEntityAdditionalSpawnData {

    private GT_Explosion_PreCalculation preCalc;

    protected int metadata;

    protected double realX, realY, realZ;

    protected GT_Explosion explosion;

    public GT_Entity_Explosive(final World world) {
        super(world);
    }

    public GT_Entity_Explosive(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata) {
        super(world, x + 0.5, y + 0.5, z + 0.5, placedBy);
        this.setSize();
        this.realX = this.posX;
        this.realY = this.posY;
        this.realZ = this.posZ;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.fuse = GT_Values.MEFuse;
        this.metadata = metadata;
        this.explosion = createExplosion();
        this.preCalc = new GT_Explosion_PreCalculation(this, this.explosion, world, explosion.getX(), explosion.getY(), explosion.getZ(), this.fuse);
        preCalc.initialize();
    }

    @NonNull
    protected abstract GT_Explosion createExplosion();

    protected void setSize() {
        final float newSize = getNewSize();
        this.setSize(newSize, newSize);
    }

    private float getNewSize() {
        return 0.5f + ((float) (fuse - GT_Values.MEFuse)) / GT_Values.MEFuse;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
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
                if (preCalc != null) {
                    this.preCalc.finalizeExplosion();
                }
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
     */
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("meta", metadata);
        compound.setDouble("rX", realX);
        compound.setDouble("rY", realY);
        compound.setDouble("rZ", realZ);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     *
     * @param compound
     */
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.metadata = compound.getInteger("meta");
        this.realX = compound.getDouble("rX");
        this.realY = compound.getDouble("rY");
        this.realZ = compound.getDouble("rZ");
    }

    protected void doExplode() {
        if (explosion != null) {
            explosion.perform();
        } else {
            GT_FML_LOGGER.error("Explosion is null! You probably reloaded the world, sorry");
        }
    }

    /**
     * @param explosion
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     * @return
     */
    @Override
    public float func_145772_a(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block
                              ) {
        return getBlockResistance(explosion, world, x, y, z, block);
    }

    /**
     * @param explosion
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     * @param power
     * @return
     */
    @Override
    public boolean func_145774_a(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block, final float power
                                ) {
        return canBlockBeExploded(explosion, world, x, y, z, block, power);
    }

    public boolean canBlockBeExploded(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block, final float power
                                     ) {
        return !(block instanceof GT_Block_Explosive);
    }

    public abstract float getBlockResistance(final Explosion explosion, final World world, final int x, final int y, final int z, final Block block);

    public float defaultBlockResistance(final Explosion explosion, final World world, final int x, final int y, final int z, final Block block) {
        return super.func_145772_a(explosion, world, x, y, z, block);
    }

    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     *
     * @param buffer The packet data stream
     */
    @Override
    public void writeSpawnData(final ByteBuf buffer) {
        buffer.writeInt(fuse);
        buffer.writeInt(metadata);
        buffer.writeDouble(realX);
        buffer.writeDouble(realY);
        buffer.writeDouble(realZ);
    }

    /**
     * Called by the client when it receives a Entity spawn packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param additionalData
     */
    @Override
    public void readSpawnData(final ByteBuf additionalData) {
        this.fuse = additionalData.readInt();
        this.metadata = additionalData.readInt();
        this.realX = additionalData.readDouble();
        this.realY = additionalData.readDouble();
        this.realZ = additionalData.readDouble();
    }

    public abstract Block getBlockToRenderAs();

    public abstract ResourceLocation getEntityTexture();

}
