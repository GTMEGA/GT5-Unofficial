package gregtech.common.blocks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
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
import lombok.var;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GT_Block_Ore_Abstract extends GT_Generic_Block {
    public static enum OreSize {
        Normal,
        Small
    }

    private static final boolean hideUnusedOresInNEI = Loader.isModLoaded("NotEnoughItems") &&
                                                       GT_Mod.gregtechproxy.mHideUnusedOres;
    private final Materials oreType;
    private final ITexture[] textures;

    protected static final Map<Materials, GT_Block_Ore_Abstract> oreMap = new HashMap<>();
    protected static final Map<Materials, GT_Block_Ore_Abstract> smallOreMap = new HashMap<>();
    private static final ITexture stone = TextureFactory.of(Blocks.stone);

    private static Block chiselLimestone;
    private static Block chiselDiorite;
    private static Block chiselAndesite;


    protected GT_Block_Ore_Abstract(Materials oreType, String unlocalizedName) {
        super(GT_Item_Ores.class,
              unlocalizedName,
              Material.rock);
        this.setStepSound(soundTypeStone);
        this.setCreativeTab(GregTech_API.TAB_GREGTECH_ORES);
        this.oreType = oreType;
        this.textures = new ITexture[]{stone, getOreTexture(this.oreType)};
    }

    public static void registerOres() {
        for (int i = 1; i < GregTech_API.sGeneratedMaterials.length; i++) {
            val material = GregTech_API.sGeneratedMaterials[i];

            if (material != null && (material.mTypes & 0x8) != 0) {
                val ore = new GT_Block_Ore(material);
                val oreSmall = new GT_Block_Ore_Small(material);

                oreMap.put(material, ore);
                smallOreMap.put(material, oreSmall);

                registerLocalization(material, ore.getUnlocalizedName() + ".0.name", OreSize.Normal);
                registerLocalization(material, oreSmall.getUnlocalizedName() + ".0.name", OreSize.Small);
            }
        }
    }

    protected static void registerLocalization(Materials material, String oreNameUnlocalized, OreSize size) {
        String oreNameLocalized;
        if (GT_LanguageManager.i18nPlaceholder)
            oreNameLocalized = String.format(getLocalizedNameFormat(material, size), material.mLocalizedName);
        else
            oreNameLocalized = getLocalizedName(material, size);

        GT_LanguageManager.addStringLocalization(oreNameUnlocalized, oreNameLocalized);
    }

    public Materials material() {
        return oreType;
    }

    public static GT_Block_Ore_Abstract getOre(Materials material, OreSize size) {
        return size == OreSize.Normal ? oreMap.get(material) : smallOreMap.get(material);
    }

    private static void populateBlockCache() {
        chiselLimestone = GameRegistry.findBlock("chisel", "limestone");
        chiselDiorite = GameRegistry.findBlock("chisel", "diorite");
        chiselAndesite = GameRegistry.findBlock("chisel", "andesite");
    }

    public static boolean setOreBlock(World world, int x, int y, int z, Materials material, boolean air, OreSize size) {
        if (!air) {
            y = Math.min(world.getActualHeight(), Math.max(y, 1));
        }

        Block tBlock = world.getBlock(x, y, z);
        GT_Block_Ore_Abstract ore = getOre(material, size);

        if (chiselLimestone == null) populateBlockCache();

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
                                                               GregTech_API.sBlockStones,
                                                               chiselDiorite,
                                                               chiselLimestone,
                                                               chiselAndesite

            )) {
                return false;
            }
            //GT_FML_LOGGER.info(tOreBlock);
            world.setBlock(x, y, z, ore, 0, 0);
            return true;
        }
        return false;
    }

    protected static String getLocalizedNameFormat(Materials aMaterial, OreSize size) {
        StringBuilder sb = new StringBuilder();

        if (size == OreSize.Small) {
            sb.append(OrePrefixes.oreSmall.mLocalizedMaterialPre);
        }

        switch (aMaterial.mName) {
            case "InfusedAir":
            case "InfusedDull":
            case "InfusedEarth":
            case "InfusedEntropy":
            case "InfusedFire":
            case "InfusedOrder":
            case "InfusedVis":
            case "InfusedWater":
                sb.append("%s Infused Stone");
                break;
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
                sb.append("%s");
                break;
            default:
                sb.append("%s").append(OrePrefixes.ore.mLocalizedMaterialPost);
        }

        return sb.toString();
    }

    protected static String getLocalizedName(Materials aMaterial, OreSize size) {
        return aMaterial.getDefaultLocalizedNameForItem(getLocalizedNameFormat(aMaterial, size));
    }

    protected abstract ITexture getOreTexture(Materials oreType);

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
        val fortuneFactor = fortune + 1;
        val numStacks = Math.max(1, fortuneFactor / 64);
        var stackAmount = fortuneFactor;
        for (int i = 0; i <= numStacks && stackAmount > 0; i++, stackAmount = fortuneFactor - i * 64) {
            val drop = GT_OreDictUnificator.get(OrePrefixes.oreChunk, this.oreType, Math.min(64L, stackAmount));
            drops.add(drop);
        }
        return drops;
    }

    public ItemStack getDropsMining(World world, int x, int y, int z, int metadata, int fortune) {
        val fortuneFactor = fortune + 1;
        val stack = GT_OreDictUnificator.get(OrePrefixes.oreChunk, this.oreType, fortuneFactor);
        stack.stackSize = fortuneFactor;
        return stack;
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
        return 3.0F;
    }

    public boolean useNeighborBrightness = false;
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

    @Override
    public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
        return 100;
    }
}
