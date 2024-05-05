package gregtech.common.blocks.explosives;


import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import gregtech.common.items.explosives.GT_Item_Explosive;
import gregtech.common.items.explosives.GT_RemoteDetonator;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import lombok.Getter;
import lombok.NonNull;
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

import static gregtech.api.enums.GT_Values.W;


public abstract class GT_Block_Explosive<TierType extends Enum<TierType> & IGT_ExplosiveTier<TierType>> extends Block {

    public static final int primeMask = 1 << 3;

    public static final int sideMask = primeMask - 1;

    protected final @NonNull String mUnlocalizedName;

    /*

    // Unused until EndlessIDs is integrated

    public static final int baseMask = sideMask | primeMask;

    public static final int otherMask = ((1 << 8) - 1) & ~baseMask;

    */

    protected final IIconContainer[] icons;

    @Getter
    protected final @NonNull TierType tier;

    @Getter
    @Setter
    protected GT_Item_Explosive<TierType> item;

    protected GT_Block_Explosive(final Class<? extends ItemBlock> aItemClass, final @NonNull String aName, final @NonNull TierType tier) {
        super(Material.tnt);
        this.tier = tier;
        tier.getBlockReference().set(this);
        this.icons = tier.getIcons();
        setBlockName(mUnlocalizedName = tier.getULSuffix(aName));
        GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + W + ".name", "Any Sub Block of this one");
    }

    public void remoteTrigger(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        if (!world.isRemote) {
            goBoom(world, x, y, z, player, -1);
        }
    }

    protected void goBoom(final World world, final int x, final int y, final int z, final EntityPlayer player, final int fuse) {
        val tier = getTier();
        final int metadata = world.getBlockMetadata(x, y, z);
        final GT_Entity_Explosive<TierType> explosive = createExplosive(world, x, y, z, player, metadata, fuse);
        world.spawnEntityInWorld(explosive);
        world.playSoundAtEntity(explosive, GregTech_API.sSoundList.get(tier.getFlavorInfo().getFuseSoundID()), 1.0f, 1.0f);
        world.setBlockToAir(x, y, z);
    }

    protected abstract GT_Entity_Explosive<TierType> createExplosive(final World world, final double x, final double y, final double z, final EntityPlayer placedBy, final int metadata, final int timer);

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

    public static ForgeDirection getFacing(final int meta) {
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
        return player.getHeldItem() != null && player.getHeldItem().getItem() instanceof GT_RemoteDetonator;
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
