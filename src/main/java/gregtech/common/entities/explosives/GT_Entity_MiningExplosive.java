package gregtech.common.entities.explosives;


import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_MiningExplosion;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_MiningExplosiveTier;


@Getter
public class GT_Entity_MiningExplosive extends GT_Entity_Explosive<GT_MiningExplosiveTier> {

    @SuppressWarnings("unused")
    public GT_Entity_MiningExplosive(final World world) {
        super(world);
    }

    public GT_Entity_MiningExplosive(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata, final int fuse, final @NonNull GT_MiningExplosiveTier tier) {
        super(world, x, y, z, placedBy, metadata, fuse, tier);
    }

    /**
     * @return
     */
    @Override
    protected @NonNull GT_MiningExplosion createExplosion() {
        final ForgeDirection side = GT_Block_Explosive.getFacing(metadata);
        final double xOffset, yOffset, zOffset;
        final double offset = rangeOffset();
        xOffset = offset * side.offsetX;
        yOffset = offset * side.offsetY;
        zOffset = offset * side.offsetZ;
        return new GT_MiningExplosion(worldObj, this, posX + xOffset, posY + yOffset, posZ + zOffset, GT_Values.MEExplosionPower);
    }

    /**
     * @return GT_MiningExplosiveTier.class
     */
    @Override
    public Class<GT_MiningExplosiveTier> getTierClass() {
        return GT_MiningExplosiveTier.class;
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
    public float getBlockResistance(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block
                                   ) {
        if (block instanceof BlockOre || block instanceof GT_Block_Ore_Abstract) {
            return -GT_Values.MEOrePowerBoost;
        }
        final Material material = block.getMaterial();
        final float base = defaultBlockResistance(explosion, world, x, y, z, block);
        if (material == Material.rock) {
            return base * GT_Values.MERockResistanceDrop;
        } else if (material == Material.ground || material == Material.sand || material == Material.clay || block instanceof BlockGrass) {
            return -GT_Values.MESoilPowerBoost;
        } else {
            return base * GT_Values.MEOtherResistanceDrop;
        }
    }

    /**
     * @return
     */
    @Override
    public ResourceLocation getEntityTexture() {
        return Textures.BlockIcons.MINING_EXPLOSIVE.getTextureFile();
    }

    private double rangeOffset() {
        return tier.getRadius() * 0.75;
    }

}
