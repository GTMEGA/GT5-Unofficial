package gregtech.common.misc.explosions;


import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;


public class GT_MiningExplosion extends GT_Explosion {

    public GT_MiningExplosion(
            final World world, final GT_Entity_Explosive entity, final double x, final double y, final double z, final float power
                             ) {
        super(world, entity, x, y, z, power);
    }

    @Override
    protected float getDropChance(final Block block) {
        if (block instanceof BlockOre || block instanceof GT_Block_Ore_Abstract) {
            return GT_Values.MEOreChance;
        } else {
            final Material material = block.getMaterial();
            if (material == Material.ground || material == Material.sand || material == Material.clay || block instanceof BlockGrass) {
                return GT_Values.MESoilChance;
            } else if (material == Material.rock) {
                return GT_Values.MERockChance;
            }
        }
        return GT_Values.MEOtherChance;
    }

    @Override
    protected double getExpRadius() {
        return GT_Values.MEMaxRange + (pubWorld.rand.nextDouble() * 2.5) - 1.5;
    }

    @Override
    protected boolean rayValid(final float power, final double rayLength, final double posX, final double posY, final double posZ, final double maxRadius) {
        return power > 0.0f && rayLength < maxRadius;
    }

}
