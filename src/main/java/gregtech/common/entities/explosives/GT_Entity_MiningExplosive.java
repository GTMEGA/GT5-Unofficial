package gregtech.common.entities.explosives;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.explosives.GT_Block_MiningExplosive;
import gregtech.common.blocks.GT_Block_Ore;
import gregtech.common.misc.explosions.GT_MiningExplosion;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


@Getter
public class GT_Entity_MiningExplosive extends GT_Entity_Explosive {

    /**
     * Don't touch this, I know it's unused, but it needs to be here for the FML entity rendering
     */
    public GT_Entity_MiningExplosive(final World world) {
        super(world);
    }

    public GT_Entity_MiningExplosive(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata) {
        super(world, x, y, z, placedBy, metadata);
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

    private double rangeOffset() {
        return GT_Values.MEMaxRange * GT_Values.MEOffsetRatio;
    }

    protected void doExplode() {
        final ForgeDirection side = ((GT_Block_MiningExplosive) GregTech_API.sBlockMiningExplosive).getFacing(metadata);
        final double xOffset, yOffset, zOffset;
        xOffset = rangeOffset() * side.offsetX;
        yOffset = rangeOffset() * side.offsetY;
        zOffset = rangeOffset() * side.offsetZ;
        new GT_MiningExplosion(worldObj, this, posX + xOffset, posY + yOffset, posZ + zOffset, GT_Values.MEExplosionPower).perform();
    }

}
