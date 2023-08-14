package gregtech.common.entities;


import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.misc.GT_MiningExplosion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


@Getter
public class GT_Entity_MiningExplosive extends EntityTNTPrimed implements IEntityAdditionalSpawnData {

    private boolean gravityAffected;

    /**
     * Don't touch this, I know it's unused, but it needs to be here for the FML entity rendering
     */
    public GT_Entity_MiningExplosive(final World world) {
        super(world);
    }

    public GT_Entity_MiningExplosive(
            final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final boolean gravityAffected
                                    ) {
        super(world, x + 0.5, y, z + 0.5, placedBy);
        this.setSize();
        this.fuse = GT_Values.MEFuse;
        this.gravityAffected = gravityAffected;
    }

    private void setSize() {
        final float newSize = getNewSize();
        this.setSize(newSize, newSize);
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
        if (gravityAffected) {
            moveDown();
        }
        if (fuse-- <= 0) {
            this.setDead();

            if (!this.worldObj.isRemote) {
                this.doExplode();
            }
        } else {
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void moveDown() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }
    }

    private void doExplode() {
        GT_MiningExplosion explosion = new GT_MiningExplosion(worldObj, this, posX, posY, posZ, GT_Values.MEExplosionPower);
        explosion.perform();
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
    }

}
