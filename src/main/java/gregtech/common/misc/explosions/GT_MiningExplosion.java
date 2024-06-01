package gregtech.common.misc.explosions;


import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.entities.explosives.GT_Entity_MiningExplosive;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_MiningExplosiveTier;


public class GT_MiningExplosion extends GT_Explosion<GT_MiningExplosiveTier> {

    public GT_MiningExplosion(
            final World world, final GT_Entity_MiningExplosive entity, final double x, final double y, final double z, final float power
                             ) {
        super(world, entity, x, y, z, power);
    }

    @Override
    protected float getDropChance(final Block block) {
        val params   = getTier().getParameters();
        val material = block.getMaterial();
        if (material == Material.clay) {
            return params.getClayChance();
        } else if (block instanceof BlockOre || block instanceof GT_Block_Ore_Abstract) {
            return params.getOreChance();
        } else {
            if (material == Material.ground || material == Material.sand || block instanceof BlockGrass) {
                return params.getSoilChance();
            } else if (material == Material.rock) {
                return params.getRockChance();
            }
        }
        return params.getOtherChance();
    }

    @Override
    protected boolean isRayValid(GT_Explosion_PreCalculation.Ray ray) {
        return ray.power > 0.0f && ray.myLength < ray.maxLength;
    }

    @Override
    protected double preCalculateRayMaximumLength(GT_Explosion_PreCalculation.Ray ray) {
        val params = getTier().getParameters();
        return getTier().getRadius() + pubWorld.rand.nextDouble() * params.getFeatherRange() - params.getFeatherOffset();
    }

}
