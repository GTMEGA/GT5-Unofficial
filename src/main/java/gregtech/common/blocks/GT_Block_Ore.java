package gregtech.common.blocks;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.GT_Generic_Block;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_Renderer_Block;
import lombok.Getter;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public class GT_Block_Ore extends GT_Generic_Block {
    private static final boolean hideUnusedOresInNEI = Loader.isModLoaded("NotEnoughItems") &&
                                                       GT_Mod.gregtechproxy.mHideUnusedOres;
    private final Materials oreType;
    private final ITexture[] textures;

    private static final Map<Materials, GT_Block_Ore> oreMap = new HashMap<>();
    private static final ITexture stone = TextureFactory.of(Blocks.stone);

    protected GT_Block_Ore(Materials oreType) {
        super(GT_Item_Ores.class,
              String.join(".", "gt.blockore", oreType.mName),
              Material.rock);

        this.setStepSound(soundTypeStone);
        this.setCreativeTab(GregTech_API.TAB_GREGTECH_ORES);
        this.oreType = oreType;
        this.textures = new ITexture[] { stone, getOreTexture(this.oreType) };
    }

    public static void registerOres() {
        for (int i = 1; i < GregTech_API.sGeneratedMaterials.length; i++) {
            val material = GregTech_API.sGeneratedMaterials[i];

            if (material != null && (material.mTypes & 0x8) != 0) {
                val ore = new GT_Block_Ore(material);
                oreMap.put(material, ore);

                val oreNameUnlocalized = ore.getUnlocalizedName() + ".0.name";

                String oreNameLocalized;
                if (GT_LanguageManager.i18nPlaceholder)
                    oreNameLocalized = String.format(getLocalizedNameFormat(material), material.mLocalizedName);
                else
                    oreNameLocalized = getLocalizedName(material);

                GT_LanguageManager.addStringLocalization(oreNameUnlocalized, oreNameLocalized);
            }
        }
    }

    public static GT_Block_Ore getOre(Materials material) {
        return oreMap.get(material);
    }

    public static boolean setOreBlock(World world, int x, int y, int z, Materials material, boolean air) {
        if (!air) {
            y = Math.min(world.getActualHeight(), Math.max(y, 1));
        }

        Block tBlock = world.getBlock(x, y, z);
        GT_Block_Ore ore = getOre(material);

        if ((tBlock != Blocks.air) || air) {
            if (!GT_Utility.isBlockReplaceableForOreGeneration(world,
                                                               x,
                                                               y,
                                                               z,
                                                               tBlock,
                                                               Blocks.stone,
                                                               Blocks.netherrack,
                                                               Blocks.end_stone,
                                                               GregTech_API.sBlockGranites,
                                                               GregTech_API.sBlockStones)) {
                return false;
            }
            //GT_FML_LOGGER.info(tOreBlock);
            world.setBlock(x, y, z, ore, 0, 0);
            return true;
        }
        return false;
    }

    private static String getLocalizedNameFormat(Materials aMaterial) {
        switch (aMaterial.mName) {
            case "InfusedAir":
            case "InfusedDull":
            case "InfusedEarth":
            case "InfusedEntropy":
            case "InfusedFire":
            case "InfusedOrder":
            case "InfusedVis":
            case "InfusedWater":
                return "%s Infused Stone";
            case "Vermiculite":
            case "Bentonite":
            case "Kaolinite":
            case "Talc":
            case "BasalticMineralSand":
            case "GraniticMineralSand":
            case "GlauconiteSand":
            case "CassiteriteSand":
            case "GarnetSand":
            case "QuartzSand":
            case "Pitchblende":
            case "FullersEarth":
                return "%s";
            default:
                return "%s" + OrePrefixes.ore.mLocalizedMaterialPost;
        }
    }

    private static String getLocalizedName(Materials aMaterial) {
        return aMaterial.getDefaultLocalizedNameForItem(getLocalizedNameFormat(aMaterial));
    }

    private static ITexture getOreTexture(Materials oreType) {
        return TextureFactory.builder()
                             .addIcon(oreType.mIconSet.mTextures[OrePrefixes.ore.mTextureIndex])
                             .setRGBA(oreType.mRGBa)
                             .stdOrient()
                             .build();
    }

    @Override
    public int getRenderType() {
        if (GT_Renderer_Block.INSTANCE == null) {
            return super.getRenderType();
        }
        return GT_Renderer_Block.INSTANCE.mRenderID;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        val drops = new ArrayList<ItemStack>();

        val drop = GT_OreDictUnificator.get(OrePrefixes.oreChunk, this.oreType, Math.max(1L, fortune));

        drops.add(drop);

        return drops;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return true;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return 1.0F;
    }

    @Override
    public int getHarvestLevel(int metadata) {
        return 0;
    }

    @Override
    public String getHarvestTool(int metadata) {
        return "pickaxe";
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.stone.getIcon(0, 0);
    }
}
