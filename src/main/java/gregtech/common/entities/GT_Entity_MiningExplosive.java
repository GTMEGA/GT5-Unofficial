package gregtech.common.entities;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.misc.GT_MiningExplosion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


@Getter
public class GT_Entity_MiningExplosive extends EntityTNTPrimed implements IEntityAdditionalSpawnData {

    /**
     * Don't touch this, I know it's unused, but it needs to be here for the FML entity rendering
     * */
    public GT_Entity_MiningExplosive(final World world) {
        super(world);
    }

    public GT_Entity_MiningExplosive(
            final World world, final double x, final double y, final double z, final EntityLivingBase placedBy
                                    ) {
        super(world, x + 0.5, y, z + 0.5, placedBy);
        this.setSize();
        moveEntity(0.0, 0.0, 0.0);
        this.fuse = GT_Values.MEFuse;
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
        if (block instanceof BlockOre || block instanceof GT_Block_Ores_Abstract) {
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
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        this.moveEntity(0.0f, 0.0f, 0.0f);
        if (fuse-- <= 0)
        {
            this.setDead();

            if (!this.worldObj.isRemote)
            {
                this.doExplode();
            }
        }
        else
        {
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private float getNewSize() {
        return 0.5f + ((float)(fuse - GT_Values.MEFuse)) / GT_Values.MEFuse;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     *
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void moveEntity(final double x, final double y, final double z) {
        this.motionX = 0.0f;
        this.motionY = 0.0f;
        this.motionZ = 0.0f;
    }

    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     *
     * @param buffer The packet data stream
     */
    @Override
    public void writeSpawnData(final ByteBuf buffer) {
        buffer.writeByte(fuse);
    }

    /**
     * Called by the client when it receives a Entity spawn packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param additionalData
     */
    @Override
    public void readSpawnData(final ByteBuf additionalData) {
        this.fuse = additionalData.readByte();
    }

    private void doExplode() {
        GT_MiningExplosion explosion = new GT_MiningExplosion(worldObj, this, posX, posY, posZ, GT_Values.MEExplosionPower);
        explosion.perform();
    }

    private void setSize() {
        final float newSize = getNewSize();
        this.setSize(newSize, newSize);
    }

}
