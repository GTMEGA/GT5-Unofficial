package gregtech.common.blocks;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_Generic_Block;
import gregtech.common.entities.GT_Entity_MiningExplosive;
import gregtech.common.items.GT_Item_MiningExplosive;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;


public class GT_Block_MiningExplosive extends GT_Generic_Block {

    private static final IIconContainer iIconContainer = Textures.BlockIcons.MINING_EXPLOSIVE;

    public GT_Block_MiningExplosive() {
        super(GT_Item_MiningExplosive.class, "gt.mining_explosives", Material.tnt);
    }

    /**
     * Called upon block activation (right click on the block.)
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param player
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     */
    @Override
    public boolean onBlockActivated(
            final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY,
            final float hitZ
                                   ) {
        if (!playerActivatedMe(side, hitX, hitY, hitZ)) {
            return true;
        }
        goBoom(world, x, y, z, player);
        return false;
    }

    private void goBoom(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        if (!world.isRemote) {
            final GT_Entity_MiningExplosive explosive = new GT_Entity_MiningExplosive(world, x, y, z, player);
            world.spawnEntityInWorld(explosive);
            world.playSoundAtEntity(explosive, "game.tnt.primed", 1.0F, 1.0F);
            world.setBlockToAir(x, y, z);
        }
    }

    private boolean playerActivatedMe(final int side, final float hitX, final float hitY, final float hitZ) {
        float[] hits = getAppropriateHits(side, hitX, hitY, hitZ);
        return hits[0] > 0.3f && hits[0] < 0.7f && hits[1] > 0.3f && hits[1] < 0.8f;
    }

    private float[] getAppropriateHits(final int side, final float hitX, final float hitY, final float hitZ) {
        final float[] result = {0.0f, 0.0f};
        final int axis = side / 2;
        switch (axis) {
            case 0: {
                // Up/Down
                result[0] = hitX;
                result[1] = hitZ;
                break;
            }
            case 1: {
                // North/South
                result[0] = hitX;
                result[1] = hitY;
                break;
            }
            case 2: {
                // East/West
                result[0] = hitY;
                result[1] = hitZ;
                break;
            }
        }
        return result;
    }

    /**
     * Queries the class of tool required to harvest this block, if null is returned
     * we assume that anything can harvest this block.
     *
     * @param metadata
     * @return
     */
    @Override
    public String getHarvestTool(final int metadata) {
        return null;
    }

    /**
     * Gets the block's texture. Args: side, meta
     *
     * @param side
     * @param meta
     */
    @Override
    public IIcon getIcon(final int side, final int meta) {
        return iIconContainer.getIcon();
    }

}
