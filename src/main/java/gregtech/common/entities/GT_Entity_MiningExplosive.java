package gregtech.common.entities;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.GT_Block_MiningExplosive;
import gregtech.common.blocks.GT_Block_Ore;
import gregtech.common.misc.explosions.GT_MiningExplosion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


@Getter
public class GT_Entity_MiningExplosive extends EntityTNTPrimed implements IEntityAdditionalSpawnData {

    private int metadata;

    private double realX, realY, realZ;

    /**
     * Don't touch this, I know it's unused, but it needs to be here for the FML entity rendering
     */
    public GT_Entity_MiningExplosive(final World world) {
        super(world);
    }

    public GT_Entity_MiningExplosive(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata) {
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
    }

    private void setSize() {
        final float newSize = getNewSize();
        this.setSize(newSize, newSize);
    }

    private float getNewSize() {
        return 0.5f + ((float) (fuse - GT_Values.MEFuse)) / GT_Values.MEFuse;
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
        if (block instanceof BlockOre || block instanceof GT_Block_Ore) {
            return -GT_Values.MEOrePowerBoost;
        }
        final Material material = block.getMaterial();
        final float base = super.func_145772_a(explosion, world, x, y, z, block);
        if (material == Material.rock) {
            return base * GT_Values.MERockResistanceDrop;
        } else if (material == Material.ground || material == Material.sand || material == Material.clay || block instanceof BlockGrass) {
            return -GT_Values.MESoilPowerBoost;
        } else {
            return base * GT_Values.MEOtherResistanceDrop;
        }
    }

    /**
     * @param explosion
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     * @param explosionPower
     * @return
     */
    @Override
    public boolean func_145774_a(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block, final float explosionPower
                                ) {
        return !(block instanceof GT_Block_MiningExplosive);
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
        if (fuse-- <= 0) {
            this.setDead();

            if (!this.worldObj.isRemote) {
                this.doExplode();
            }
        } else {
            final int n = rand.nextInt(10) + 1;
            for (int i = 0; i < n; i++){
                final double x, y, z;
                x = rand.nextDouble() * 1.4 - 0.7;
                y = rand.nextDouble() * 1.4 - 0.7;
                z = rand.nextDouble() * 1.4 - 0.7;
                this.worldObj.spawnParticle("smoke", posX + x, posY + y, posZ + z, x / 4, y / 4, z / 4);
            }
        }
    }

    private void doExplode() {
        final ForgeDirection side = ((GT_Block_MiningExplosive) GregTech_API.sBlockMiningExplosive).getFacing(metadata);
        final double xOffset, yOffset, zOffset;
        xOffset = rangeOffset() * side.offsetX;
        yOffset = rangeOffset() * side.offsetY;
        zOffset = rangeOffset() * side.offsetZ;
        new GT_MiningExplosion(worldObj, this, posX + xOffset, posY + yOffset, posZ + zOffset, GT_Values.MEExplosionPower).perform();
    }

    private double rangeOffset() {
        return GT_Values.MEMaxRange * GT_Values.MEOffsetRatio;
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

}
