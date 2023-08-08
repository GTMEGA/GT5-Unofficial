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
import gregtech.common.render.GT_Renderer_Block;
import lombok.val;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class GT_Block_Ore extends GT_Generic_Block {
    private static boolean hideUnusedOresInNEI = Loader.isModLoaded("NotEnoughItems") &&
                                                 GT_Mod.gregtechproxy.mHideUnusedOres;
    private final Materials oreType;
    private final ITexture[] textures;

    protected GT_Block_Ore(Materials oreType, GT_Block_Ore_StoneType stoneType) {
        super(GT_Item_Ores.class, String.join(".", "gt.blockore", oreType.mName, stoneType.name()), Material.rock);
        this.setStepSound(soundTypeStone);
        this.setCreativeTab(GregTech_API.TAB_GREGTECH_ORES);
        this.oreType = oreType;
        this.textures = new ITexture[] { stoneType.texture, getOreTexture(this.oreType) };
    }

    public static void registerOres() {
        for (int i = 1; i < GregTech_API.sGeneratedMaterials.length; i++) {
            val material = GregTech_API.sGeneratedMaterials[i];

            if (GregTech_API.sGeneratedMaterials[i] != null &&
               (GregTech_API.sGeneratedMaterials[i].mTypes & 0x8) != 0) {
                registerOresForMaterial(material);
            }
//
//                if (GT_LanguageManager.i18nPlaceholder)
//                    GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (i + (j * 1000)) + aTextName, getLocalizedNameFormat(GregTech_API.sGeneratedMaterials[i]));
//                else
//                    GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (i + (j * 1000)) + aTextName, getLocalizedName(GregTech_API.sGeneratedMaterials[i]));
//                GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + ((i + 16000) + (j * 1000)) + aTextName, aTextSmall + (GT_LanguageManager.i18nPlaceholder ? getLocalizedNameFormat(GregTech_API.sGeneratedMaterials[i]) : getLocalizedName(GregTech_API.sGeneratedMaterials[i])));
//                    if ((GregTech_API.sGeneratedMaterials[i].mTypes & 0x8) != 0 && !aBlockedOres.contains(GregTech_API.sGeneratedMaterials[i])) {
//                        GT_OreDictUnificator.registerOre(this.getProcessingPrefix()[j] != null ? this.getProcessingPrefix()[j].get(GregTech_API.sGeneratedMaterials[i]) : "", new ItemStack(this, 1, i + (j * 1000)));
//                        if (tHideOres) {
//                            if (!(j == 0 && !aHideFirstMeta)) {
//                                codechicken.nei.api.API.hideItem(new ItemStack(this, 1, i + (j * 1000)));
//                            }
//                            codechicken.nei.api.API.hideItem(new ItemStack(this, 1, (i + 16000) + (j * 1000)));
//                        }
//                    }
        }
    }

    private static void registerOresForMaterial(Materials oreType) {
        for (val stoneType : GT_Block_Ore_StoneType.values()) {
            if (stoneType.isEnabled) {
                new GT_Block_Ore(oreType, stoneType);
            }
        }
    }

    private static ITexture getOreTexture(Materials oreType) {
        return TextureFactory.builder()
                             .addIcon(oreType.mIconSet.mTextures[OrePrefixes.ore.mTextureIndex])
                             .setRGBA(oreType.mRGBa)
                             .stdOrient()
                             .build();
    }

    public ITexture[] getTextures() {
        return this.textures;
    }

    @Override
    public int getRenderType() {
        if (GT_Renderer_Block.INSTANCE == null) {
            return super.getRenderType();
        }
        return GT_Renderer_Block.INSTANCE.mRenderID;
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
}
