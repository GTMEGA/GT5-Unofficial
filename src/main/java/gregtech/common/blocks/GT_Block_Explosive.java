package gregtech.common.blocks;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.items.GT_Item_Explosive;
import gregtech.common.items.GT_Item_RemoteDetonator;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import static gregtech.api.enums.GT_Values.W;


public class GT_Block_Explosive<TierType extends Enum<TierType> & IGT_ExplosiveTier<TierType>> extends Block {

    private static final double INSTABILITY_CHANCE = 0.8;

    private static final float FUZZ = 0.1f;

    public static final int primeMask = 1 << 3;

    public static final int sideMask = primeMask - 1;

    protected final @NonNull String mUnlocalizedName;

    protected final IIconContainer[] icons;

    @Getter
    protected final @NonNull IGT_ExplosiveTier<TierType> tier;

    @Getter
    @Setter
    protected GT_Item_Explosive item;

    public GT_Block_Explosive(final @NonNull TierType tier) {
        super(Material.tnt);
        this.tier = tier;
        setBlockName(mUnlocalizedName = tier.getULSuffix(tier.getBlockName()));
        GameRegistry.registerBlock(this, GT_Item_Explosive.class, getUnlocalizedName());
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + W + ".name", "Any Sub Block of this one");
        tier.getTextureInfo().loadIcons(tier);
        this.icons = tier.getIcons();
        setTickRandomly(tier.isMagic());
    }

    public void remoteTrigger(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        if (!world.isRemote) {
            goBoom(world, x, y, z, player, -1);
        }
    }

    protected void goBoom(final World world, final int x, final int y, final int z, final EntityLivingBase player, final int fuse) {
        val tier      = getTier();
        val metadata  = world.getBlockMetadata(x, y, z);
        val explosive = tier.createExplosive(world, x, y, z, player, metadata, fuse);
        world.spawnEntityInWorld(explosive);
        world.playSoundAtEntity(explosive, tier.getFuseSound(), 1.0f, 1.0f);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public IIcon getIcon(final int side, final int meta) {
        val iIconContainer = this.icons[getIconIndex(side, meta)];
        return iIconContainer.getIcon();
    }

    protected int getIconIndex(final int side, final int meta) {
        val activated    = isPrimed(meta);
        val sideRendered = ForgeDirection.getOrientation(side);
        val sideFacing   = getFacing(meta).getOpposite();
        int index        = 0;
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

    @Override
    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
        if (canConnectRedstone(world, x, y, z, 0)) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                setPrimed(world, x, y, z, true);
                val powerNextTo = world.getStrongestIndirectPower(x, y, z);
                val timer       = (15 - powerNextTo) * 20;
                goBoom(world, x, y, z, null, timer);
            }
        }
        if (tier.isMagic()) {
            if (world.rand.nextDouble() >= INSTABILITY_CHANCE) {
                return;
            }
            goBoom(world, x, y, z, null, 0);
        }
    }

    @Override
    public boolean onBlockActivated(
            final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ
                                   ) {
        return player.getHeldItem() != null && player.getHeldItem().getItem() instanceof GT_Item_RemoteDetonator;
    }

    @Override
    public int onBlockPlaced(
            final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ, final int metadata
                            ) {
        return ForgeDirection.getOrientation(side).getOpposite().ordinal();
    }

    @Override
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity instanceof EntityLivingBase && tier.isMagic()) {
            if (world.rand.nextDouble() >= INSTABILITY_CHANCE) {
                return;
            }
            val living = (EntityLivingBase) entity;
            goBoom(world, x, y, z, living, 0);
        }
    }

    @Override
    public int tickRate(final World world) {
        return super.tickRate(world);
    }

    @Override
    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        if (!world.isRemote && tier.isMagic()) {
            if (random.nextDouble() <= INSTABILITY_CHANCE) {
                val d          = FUZZ;
                val collision  = AxisAlignedBB.getBoundingBox(x - d, y - d, z - d, x + 1 + d, y + 1 + d, z + 1 + d);
                val entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, collision);
                if (entityList != null && !entityList.isEmpty()) {
                    val first = (EntityLivingBase) entityList.get(0);
                    goBoom(world, x, y, z, first, 0);
                }
            }
            world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        }

    }

    @Override
    public boolean canConnectRedstone(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        return GT_Values.ExplosivesActivatedByRS;
    }

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

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        // NO-OP as the icons are obtained elsewhere
    }

}
