package gregtech.common.blocks.explosives;


import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_Generic_Block;
import gregtech.common.items.explosives.GT_Item_Explosive;
import gregtech.common.items.explosives.GT_RemoteDetonator;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public abstract class GT_Block_Explosive extends GT_Generic_Block {

    public static final int primeMask = 1 << 3;

    public static final int sideMask = primeMask - 1;

    /*

    // Unused until EndlessIDs is integrated

    public static final int baseMask = sideMask | primeMask;

    public static final int otherMask = ((1 << 8) - 1) & ~baseMask;

    */

    protected final IIconContainer[] icons;

    @Getter
    @Setter
    protected GT_Item_Explosive item;

    protected GT_Block_Explosive(final Class<? extends ItemBlock> aItemClass, final String aName, final IIconContainer[] icons) {
        super(aItemClass, aName, Material.tnt);
        this.icons = icons;
    }

    public void remoteTrigger(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        if (!world.isRemote) {
            goBoom(world, x, y, z, player, -1);
        }
    }

    protected abstract void goBoom(final World world, final int x, final int y, final int z, final EntityPlayer player, final int fuse);

    /**
     * Gets the block's texture. Args: side, meta
     *
     * @param side
     * @param meta
     */
    @Override
    public IIcon getIcon(final int side, final int meta) {
        return this.icons[getIconIndex(side, meta)].getIcon();
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
            final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ
                                   ) {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof GT_RemoteDetonator || !playerActivatedMe(side, hitX, hitY, hitZ) || GT_Values.MERequiresRemote) {
            return true;
        }
        if (!world.isRemote) {
            goBoom(world, x, y, z, player, -1);
        }
        return false;
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     * @param metadata
     */
    @Override
    public int onBlockPlaced(
            final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ, final int metadata
                            ) {
        return ForgeDirection.getOrientation(side).getOpposite().ordinal();
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

    private boolean playerActivatedMe(final int side, final float hitX, final float hitY, final float hitZ) {
        float[] hits = getAppropriateHits(side, hitX, hitY, hitZ);
        // return hits[0] > 0.3f && hits[0] < 0.7f && hits[1] > 0.3f && hits[1] < 0.8f;
        return false;
    }

    private float[] getAppropriateHits(final int side, final float hitX, final float hitY, final float hitZ) {
        final float[] result = {
                0.0f,
                0.0f
        };
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

    protected int getIconIndex(final int side, final int meta) {
        final boolean activated = isPrimed(meta);
        final ForgeDirection sideRendered = ForgeDirection.getOrientation(side);
        final ForgeDirection sideFacing = getFacing(meta).getOpposite();
        int index = 0;
        if (sideRendered == sideFacing) {
            if (activated) {
                index = 3;
            } else {
                index = 2;
            }
        } else if (sideRendered == sideFacing.getOpposite()) {
            index = 1;
        }
        return index;
    }

    public boolean isPrimed(final int meta) {
        return (meta & primeMask) != 0;
    }

    public ForgeDirection getFacing(final int meta) {
        return ForgeDirection.getOrientation(meta & sideMask);
    }

    /**
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     */
    @Override
    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
        if (canConnectRedstone(world, x, y, z, 0)) {
            val powered = world.isBlockIndirectlyGettingPowered(x, y, z);
            if (powered) {
                setPrimed(world, x, y, z, true);
                val powerNextTo = world.getStrongestIndirectPower(x, y, z);
//                System.out.println("Power next to: " + powerNextTo);
                val timer = (15 - powerNextTo) * 20;
                goBoom(world, x, y, z, null, timer);
            }
        }
    }

    /**
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @return
     */
    @Override
    public boolean canConnectRedstone(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        return GT_Values.ExplosivesActivatedByRS;
    }

    public void setPrimed(final World world, final int x, final int y, final int z, final boolean primed) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (primed) {
            metadata = metadata | primeMask;
        } else {
            metadata = metadata & sideMask;
        }
        world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
    }

}
