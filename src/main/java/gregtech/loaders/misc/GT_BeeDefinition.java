package gregtech.loaders.misc;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.*;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleFlowers;
import forestry.api.genetics.IMutationCustom;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.BeeVariation;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.bees.GT_AlleleBeeSpecies;
import gregtech.common.bees.GT_Bee_Mutation;
import gregtech.common.items.CombType;
import gregtech.common.items.DropType;
import gregtech.common.items.PropolisType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

import static forestry.api.apiculture.EnumBeeChromosome.*;
import static forestry.api.core.EnumHumidity.ARID;
import static forestry.api.core.EnumHumidity.DAMP;
import static forestry.api.core.EnumTemperature.*;
import static forestry.core.genetics.alleles.EnumAllele.*;
import static gregtech.api.enums.GT_Values.MOD_ID_DC;
import static gregtech.loaders.misc.GT_BeeDefinitionReference.*;

@SuppressWarnings("ALL")
/**
 * Bride Class for Lambdas
 */
class GT_BeeDefinitionReference {
    //Divider for all output amounts
    public static final int nerfAmount = 10;

    protected final static byte FORESTRY = 0;
    protected final static byte EXTRABEES = 1;
    protected final static byte GENDUSTRY = 2;
    protected final static byte MAGICBEES = 3;
    protected final static byte GREGTECH = 4;
    private GT_BeeDefinitionReference() {}
}

public enum GT_BeeDefinition implements IBeeDefinition {
    //organic
    CLAY(GT_BranchDefinition.ORGANIC, "Clay", true, new Color(0xC8C8DA), new Color(0x0000FF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f / nerfAmount);
                beeSpecies.addProduct(new ItemStack(Items.clay_ball, 3), 0.75f / nerfAmount);
                beeSpecies.addSpecialty(GT_ModHandler.getModItem("BiomesOPlenty", "mudball", 1, 0), 0.25f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.VANILLA);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Diligent"), 10);
                tMutation.requireResource(Blocks.clay, 0); //blockStainedHardenedClay
            }
    ),
    SLIMEBALL(GT_BranchDefinition.ORGANIC, "SlimeBall", true, new Color(0x4E9E55), new Color(0x00FF15),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 15), 0.30f / nerfAmount);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STICKY), 0.15f / nerfAmount);
                beeSpecies.addSpecialty(new ItemStack(Items.slime_ball, 1), 0.25f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.MUSHROOMS);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "water"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Marshy"), CLAY, 10);
            }
    ),
    PEAT(GT_BranchDefinition.ORGANIC, "Peat", true, new Color(0x906237), new Color(0x58300B),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f / nerfAmount);
                beeSpecies.addSpecialty(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "peat", 1, 0), 0.15f / nerfAmount);
                beeSpecies.addSpecialty(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "mulch", 1, 0), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
            },
            dis -> dis.registerMutation(getSpecies(FORESTRY, "Rural"), CLAY, 15)
    ),
    STICKYRESIN(GT_BranchDefinition.ORGANIC, "StickyResin", true, new Color(0x2E8F5B), new Color(0xDCC289),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STICKY), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(ItemList.Resin.get(1), 0.15f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(SLIMEBALL, PEAT, 10);
                tMutation.requireResource("logRubber");
            }
    ),
    COAL(GT_BranchDefinition.ORGANIC, "Coal", true, new Color(0x666666), new Color(0x525252),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.10f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.COAL), 0.5f / nerfAmount);
                beeSpecies.addSpecialty(new ItemStack(Items.coal, 1), 0.25f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.CACTI);
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Industrious"), PEAT, 10);
                tMutation.requireResource("oreCoal");
            }
    ),
    OIL(GT_BranchDefinition.ORGANIC, "Oil", true, new Color(0x4C4C4C), new Color(0x333333),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OIL), 0.30f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL);
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectAggressive);

            },

            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(COAL, STICKYRESIN, 10);
                tMutation.requireResource("oreOilsands");
            }
    ),
    SANDWICH(GT_BranchDefinition.ORGANIC, "Sandwich", true, new Color(0x32CD32), new Color(0xDAA520),
            beeSpecies -> {
//                beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f);
                beeSpecies.addProduct(ItemList.Crop_Drop_Cucumber.get(1), 0.1f / nerfAmount);
                beeSpecies.addProduct(ItemList.Crop_Drop_Onion.get(1), 0.1f / nerfAmount);
                beeSpecies.addProduct(ItemList.Crop_Drop_Tomato.get(1), 0.1f / nerfAmount);
                beeSpecies.addSpecialty(ItemList.Food_Sliced_Cheese.get(1), 0.05f / nerfAmount);
                beeSpecies.addSpecialty(new ItemStack(Items.cooked_porkchop, 1, 0), 0.05f / nerfAmount);
                beeSpecies.addSpecialty(new ItemStack(Items.cooked_beef, 1, 0), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOW);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectFertile);
                AlleleHelper.instance.set(template, TERRITORY, Territory.LARGE);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);

            },
            dis -> dis.registerMutation(getSpecies(FORESTRY, "Agrarian"), getSpecies(MAGICBEES, "TCBatty"), 10)
    ),
    ASH(GT_BranchDefinition.ORGANIC, "Ash", true, new Color(0x1e1a18), new Color(0xc6c6c6),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ASH), 0.15f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HOT);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.NORMAL);
                AlleleHelper.instance.set(template, TERRITORY, Territory.LARGE);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(COAL, CLAY, 10);
                tMutation.restrictTemperature(HELLISH);
            }
    ),
    APATITE(GT_BranchDefinition.ORGANIC, "Apatite", true, new Color(0xc1c1f6), new Color(0x676784),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.20f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.APATITE), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(WARM);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(ASH, COAL, 10);
                tMutation.requireResource("oreApatite");
            }
    ),
    FERTILIZER(GT_BranchDefinition.ORGANIC, "Fertilizer", true, new Color(0x7fcef5), new Color(0x654525),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f / nerfAmount);
                beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1), 0.3f / nerfAmount);
                beeSpecies.addSpecialty(ItemList.FR_Fertilizer.get(1), 0.3f / nerfAmount);
                beeSpecies.addSpecialty(ItemList.Fertilizer.get(1), 0.3f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(WARM);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(ASH, APATITE, 10);
                tMutation.requireResource("oreTricalciumPhosphate");
            }

    ),

    //gems
    REDSTONE(GT_BranchDefinition.GEM, "Redstone", true, new Color(0x7D0F0F), new Color(0xD11919),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDSTONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.RAREEARTH), 0.025f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
        },

            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Frugal"), 10);
                tMutation.requireResource("oreRedstone");
            }
    ),
    LAPIS(GT_BranchDefinition.GEM, "Lapis", true, new Color(0x1947D1), new Color(0x476CDA),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LAPIS), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Frugal"), getSpecies(FORESTRY, "Imperial"), 10);
                tMutation.requireResource("oreLapis");
            }
    ),
    CERTUS(GT_BranchDefinition.GEM, "CertusQuartz", true, new Color(0x57CFFB), new Color(0xBBEEFF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CERTUS), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Hermitic"), LAPIS, 10);
                if (Loader.isModLoaded("appliedenergistics2"))
                    tMutation.requireResource(GameRegistry.findBlock("appliedenergistics2", "tile.BlockQuartz"), 0);
            }
    ),
    FLUIX(GT_BranchDefinition.GEM, "FluixDust", true, new Color(0xA375FF), new Color(0xB591FF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FLUIX), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER),
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, LAPIS, 10);
                if (Loader.isModLoaded("appliedenergistics2"))
                    tMutation.requireResource(GameRegistry.findBlock("appliedenergistics2", "tile.BlockFluix"), 0);
            }
    ),
    DIAMOND(GT_BranchDefinition.GEM, "Diamond", false, new Color(0xCCFFFF), new Color(0xA3CCCC),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DIAMOND), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(HOT);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(CERTUS, COAL, 10);
                tMutation.requireResource("oreDiamond");
            }
    ),
    RUBY(GT_BranchDefinition.GEM, "Ruby", false, new Color(0xE6005C), new Color(0xCC0052),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.RUBY), 0.30f / nerfAmount);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.REDGARNET), 0.05f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(HOT);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, CLAY, 10);
                tMutation.requireResource("oreRuby");
            }
    ),
    SAPPHIRE(GT_BranchDefinition.GEM, "Sapphire", true, new Color(0x0033CC), new Color(0x00248F),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SAPPHIRE), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(CLAY, LAPIS, 10);
                tMutation.requireResource("oreSapphire");
            }
    ),
    OLIVINE(GT_BranchDefinition.GEM, "Olivine", true, new Color(0x248F24), new Color(0xCCFFCC),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OLIVINE), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> dis.registerMutation(CERTUS, CLAY, 30)
    ),
    EMERALD(GT_BranchDefinition.GEM, "Emerald", false, new Color(0x248F24), new Color(0x2EB82E),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.EMERALD), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(COLD);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(OLIVINE, DIAMOND, 10);
                tMutation.requireResource("oreEmerald");
            }
    ),
    YELLOWGARNET(GT_BranchDefinition.GEM, "YellowGarnet", false, new Color(0xA3A341), new Color(0xEDEDCE),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.YELLOWGARNET), 0.30f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(WARM);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(COAL, RUBY, 10);
                tMutation.requireResource("oreGarnetYellow");
            }
    ),
    REDGARNET(GT_BranchDefinition.GEM, "RedGarnet", false, new Color(0xBD4C4C), new Color(0xECCECE),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDGARNET), 0.30f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(WARM);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(YELLOWGARNET, RUBY, 10);
                tMutation.requireResource("oreGarnetRed");
            }
    ),
    FIRESTONE(GT_BranchDefinition.GEM, "Firestone", false, new Color(0xC00000), new Color(0xFF0000),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FIRESTONE), 0.15f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(WARM);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, RUBY, 10);
                tMutation.requireResource("oreFirestone");
            }
    ),

    //Metal Line
    COPPER(GT_BranchDefinition.METAL, "Copper", true, new Color(0xFF6600), new Color(0xE65C00),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.COPPER), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Majestic"), CLAY, 10);
                tMutation.requireResource("anyCopperOre");
            }
    ),
    TIN(GT_BranchDefinition.METAL, "Tin", true, new Color(0xD4D4D4), new Color(0xDDDDDD),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TIN), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(CLAY, getSpecies(FORESTRY, "Diligent"), 10);
                tMutation.requireResource("anyTinOre");
            }
    ),
    LEAD(GT_BranchDefinition.METAL, "Lead", true, new Color(0x666699), new Color(0xA3A3CC),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LEAD), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(WARM);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(COAL, COPPER, 10);
                tMutation.requireResource("oreGalena");
            }
    ),
    IRON(GT_BranchDefinition.METAL, "Iron", true, new Color(0xDA9147), new Color(0xDE9C59),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRON), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(TIN, COPPER, 10);
                tMutation.requireResource("anyIronOre");
            }
    ),
    STEEL(GT_BranchDefinition.METAL, "Steel", true, new Color(0x808080), new Color(0x999999),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRON), 0.35f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STEEL), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(WARM);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(IRON, COAL, 10);
                tMutation.requireResource("anyIronOre");
                tMutation.restrictTemperature(HOT);

            }
    ),
    NICKEL(GT_BranchDefinition.METAL, "Nickel", true, new Color(0x8585AD), new Color(0x8585AD),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NICKEL), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(IRON, COPPER, 10);
                tMutation.requireResource("anyNickelOre");
            }
    ),
    ZINC(GT_BranchDefinition.METAL, "Zinc", true, new Color(0xF0DEF0), new Color(0xF2E1F2),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ZINC), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GALLIUM), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(IRON, TIN, 10);
                tMutation.requireResource("anyZincOre");
            }
    ),
    SILVER(GT_BranchDefinition.METAL, "Silver", true, new Color(0xC2C2D6), new Color(0xCECEDE),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SILVER), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(COLD);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(LEAD, TIN, 10);
                tMutation.requireResource("oreSilver");
            }
    ),
    GOLD(GT_BranchDefinition.METAL, "Gold", true, new Color(0xEBC633), new Color(0xEDCC47),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GOLD), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(WARM);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(LEAD, COPPER, 10);
                tMutation.requireResource("oreGold");
                tMutation.restrictTemperature(HOT);
            }
    ),
    ARSENIC(GT_BranchDefinition.METAL, "Arsenic", true, new Color(0x736C52), new Color(0x292412),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ARSENIC), 0.15f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(WARM);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(ZINC, SILVER, 10);
                tMutation.requireResource("anyNickelOre");
            }
    ),

    //Rare Metals
    ALUMINIUM(GT_BranchDefinition.RAREMETAL, "Aluminium", true, new Color(0xB8B8FF), new Color(0xD6D6FF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BAUXITE), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ALUMINIUM), 0.30f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HOT);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectAggressive);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(NICKEL, ZINC, 10);
                tMutation.requireResource("oreBauxite");
            }
    ),
    TITANIUM(GT_BranchDefinition.RAREMETAL, "Titanium", true, new Color(0xCC99FF), new Color(0xDBB8FF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TITANIUM), 0.30f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HELLISH);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectAggressive);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, ALUMINIUM, 10);
                tMutation.requireResource("anyTitaniumOre");
            }
    ),
    CHROME(GT_BranchDefinition.RAREMETAL, "Chrome", true, new Color(0xEBA1EB), new Color(0xF2C3F2),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CHROME), 0.30f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HOT);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(ALUMINIUM, RUBY, 10);
                tMutation.requireResource("anyRedstoneOre");
            }
    ),
    MANGANESE(GT_BranchDefinition.RAREMETAL, "Manganese", true, new Color(0xD5D5D5), new Color(0xAAAAAA),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MANGANESE), 0.30f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HOT);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(TITANIUM, ALUMINIUM, 10);
                tMutation.requireResource("anyManganeseOre");
            }
    ),
    TUNGSTEN(GT_BranchDefinition.RAREMETAL, "Tungsten", false, new Color(0x5C5C8A), new Color(0x7D7DA1),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TUNGSTEN), 0.30f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HELLISH);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Heroic"), MANGANESE, 10);
                tMutation.requireResource("anyTungstenOre");
            }
    ),
    PLATINUM(GT_BranchDefinition.RAREMETAL, "Platinum", false, new Color(0xE6E6E6), new Color(0xFFFFCC),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.3f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.05f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HOT);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(DIAMOND, CHROME, 10);
                tMutation.requireResource("anyPlatinumOre");
            }
    ),
    IRIDIUM(GT_BranchDefinition.RAREMETAL, "Iridium", false, new Color(0xDADADA), new Color(0xD1D1E0),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.3f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OSMIUM), 0.025f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HELLISH);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(TUNGSTEN, PLATINUM, 10);
                tMutation.requireResource("anyPlatinumOre");
            }
    ),
    OSMIUM(GT_BranchDefinition.RAREMETAL, "Osmium", false, new Color(0x2B2BDA), new Color(0x8B8B8B),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OSMIUM), 0.20f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.05f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(COLD);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(TUNGSTEN, PLATINUM, 10);
                tMutation.requireResource("anyPlatinumOre");
            }
    ),
    SALTY(GT_BranchDefinition.RAREMETAL, "Salt", true, new Color(0xF0C8C8), new Color(0xFAFAFA),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALT), 0.30f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(WARM);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(CLAY, TIN, 10);
                tMutation.requireResource("anySaltOre");
            }
    ),
    LITHIUM(GT_BranchDefinition.RAREMETAL, "Lithium", false, new Color(0xF0328C), new Color(0xE1DCFF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.25f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LITHIUM), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALT), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(COLD);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(SALTY, ALUMINIUM, 10);
                tMutation.requireResource("anySaltOre");
            }
    ),


    //thaumic
    THAUMIUMDUST(GT_BranchDefinition.THAUMIC, "ThaumiumDust", true, new Color(0x7A007A), new Color(0x5C005C),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMIUMDUST), 0.20f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_2);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectExploration);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.JUNGLE);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "TCFire"), getSpecies(FORESTRY, "Edenic"), 10);
                tMutation.requireResource("blockThaumium");
                tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest"));//magical forest
            }
    ),
    THAUMIUMSHARD(GT_BranchDefinition.THAUMIC, "ThaumiumShard", true, new Color(0x9966FF), new Color(0xAD85FF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.THAUMIUMDUST), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMIUMSHARD), 0.20f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.SNOW);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectGlacial);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, getSpecies(MAGICBEES, "TCWater"), 10);
                tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest"));//magical forest
            }
    ),
    AMBER(GT_BranchDefinition.THAUMIC, "Amber", true, new Color(0xEE7700), new Color(0x774B15),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.AMBER), 0.20f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, STICKYRESIN, 10);
                tMutation.requireResource("oreAmber");
            }
    ),
    QUICKSILVER(GT_BranchDefinition.THAUMIC, "Quicksilver", true, new Color(0x7A007A), new Color(0x5C005C),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.QUICKSILVER), 0.20f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.JUNGLE);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectMiasmic);
            },
            dis -> dis.registerMutation(THAUMIUMDUST, SILVER, 15)
    ),
    SALISMUNDUS(GT_BranchDefinition.THAUMIC, "SalisMundus", true, new Color(0xF7ADDE), new Color(0x592582),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.20f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.JUNGLE);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectMiasmic);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, THAUMIUMSHARD, 10);
                tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest"));//magical forest
            }
    ),
    TAINTED(GT_BranchDefinition.THAUMIC, "Tainted", true, new Color(0x904BB8), new Color(0xE800FF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TAINTED), 0.20f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, NOCTURNAL, true);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, TOLERANT_FLYER, true);
                AlleleHelper.instance.set(template, FERTILITY, Fertility.LOW);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, THAUMIUMSHARD, 7);
                tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(193, "Tainted Land"));//Tainted Land
            }
    ),
    THAUMINITE(GT_BranchDefinition.THAUMIC, "Thauminite", true, new Color(0x2E2D79), new Color(0x7581E0),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem("MagicBees", "comb", 1, 19), 0.20f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMINITE), 0.125f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "TCOrder"), THAUMIUMDUST, 10);
                if (Loader.isModLoaded("thaumicbases"))
                    tMutation.requireResource(GameRegistry.findBlock("thaumicbases", "thauminiteBlock"), 0);
            }
    ),
    SHADOWMETAL(GT_BranchDefinition.THAUMIC, "ShadowMetal", true, new Color(0x100322), new Color(0x100342),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem("MagicBees", "comb", 1, 20), 0.20f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SHADOWMETAL), 0.125f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "TCChaos"), getSpecies(MAGICBEES, "TCVoid"), 10);
                if (Loader.isModLoaded("TaintedMagic"))
                    tMutation.requireResource("blockShadow");
            }
    ),
//    DIVIDED(GT_BranchDefinition.THAUMIC, "Unstable", true, new Color(0xF0F0F0), new Color(0xDCDCDC),
//            beeSpecies -> {
//                beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 61), 0.20f);
//                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DIVIDED), 0.125f);
//                beeSpecies.setHumidity(EnumHumidity.NORMAL);
//                beeSpecies.setTemperature(EnumTemperature.NORMAL);
//                beeSpecies.setHasEffect();
//            },
//            template -> {
//                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
//                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
//                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
//                AlleleHelper.instance.set(template, NOCTURNAL, true);
//            },
//            dis -> {
//                IBeeMutationCustom tMutation = dis.registerMutation(DIAMOND, IRON, 3);
//                if (Loader.isModLoaded("ExtraUtilities"))
//                    tMutation.requireResource(GameRegistry.findBlock("ExtraUtilities", "decorativeBlock1"), 5);
//            }
//    ),
    SPARKELING(GT_BranchDefinition.THAUMIC, "NetherStar", true, new Color(0x7A007A), new Color(0xFFFFFF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem("MagicBees", "miscResources", 1, 3), 0.20f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SPARKELING), 0.125f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
            },
            template -> {
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
                AlleleHelper.instance.set(template, CAVE_DWELLING, true);
                AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.NETHER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectAggressive);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Withering"), getSpecies(MAGICBEES, "Draconic"), 10);
                tMutation.requireResource(GregTech_API.sBlockGem3, 3);
                tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(9, "END Biome"));//sky end biome
            }
    ),

    //radiactive
    URANIUM(GT_BranchDefinition.RADIOACTIVE, "Uranium", true, new Color(0x19AF19), new Color(0x169E16),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.URANIUM), 0.15f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(COLD);
                beeSpecies.setNocturnal();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Avenging"), PLATINUM, 10);
                tMutation.requireResource(GregTech_API.sBlockMetal7, 14);
            }
    ),
    PLUTONIUM(GT_BranchDefinition.RADIOACTIVE, "Plutonium", true, new Color(0x570000), new Color(0x240000),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f / nerfAmount);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LEAD), 0.15f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PLUTONIUM), 0.15f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setNocturnal();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(URANIUM, EMERALD, 2);
                tMutation.requireResource(GregTech_API.sBlockMetal5, 13);
            }
    ),
    NAQUADAH(GT_BranchDefinition.RADIOACTIVE, "Naquadah", false, new Color(0x003300), new Color(0x002400),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADAH), 0.15f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(PLUTONIUM, IRIDIUM, 1);
                tMutation.requireResource(GregTech_API.sBlockMetal4, 10);
            }
    ),
    NAQUADRIA(GT_BranchDefinition.RADIOACTIVE, "Naquadria", false, new Color(0x000000), new Color(0x002400),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADAH), 0.20f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADRIA), 0.15f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(PLUTONIUM, IRIDIUM, 15, 2);
                tMutation.requireResource(GregTech_API.sBlockMetal4, 10);
            }
    ),
    DOB(GT_BranchDefinition.RADIOACTIVE, "DOB", false, new Color(0x003300), new Color(0x002400),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.DOB), 0.75f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();

            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(NAQUADAH, THAUMIUMSHARD, 10);
                if (Loader.isModLoaded("AdvancedSolarPanel"))
                    tMutation.requireResource(GameRegistry.findBlock("AdvancedSolarPanel", "BlockAdvSolarPanel"), 2);
                tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(9, "END Biome"));//sky end biome
            }
    ),
    THORIUM(GT_BranchDefinition.RADIOACTIVE, "Thorium", false, new Color(0x005000), new Color(0x001E00),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.THORIUM), 0.75f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(COLD);
                beeSpecies.setNocturnal();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            },
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(COAL, URANIUM, 15).setIsSecret();
                tMutation.requireResource(GregTech_API.sBlockMetal7, 5);
            }
    ),
    LUTETIUM(GT_BranchDefinition.RADIOACTIVE, "Lutetium", false, new Color(0xE6FFE6), new Color(0xFFFFFF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LUTETIUM), 0.15f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            },
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(THORIUM, getSpecies(EXTRABEES, "rotten"), 15).setIsSecret();
                tMutation.requireResource(GregTech_API.sBlockMetal4, 3);
            }
    ),
    AMERICIUM(GT_BranchDefinition.RADIOACTIVE, "Americium", false, new Color(0xE6E6FF), new Color(0xC8C8C8),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.AMERICIUM), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            },
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(LUTETIUM, CHROME, 15, 2).setIsSecret();
                tMutation.requireResource(GregTech_API.sBlockMetal1, 2);
            }
    ),
    NEUTRONIUM(GT_BranchDefinition.RADIOACTIVE, "Neutronium", false, new Color(0xFFF0F0), new Color(0xFAFAFA),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEUTRONIUM), 0.01f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(HELLISH);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
            },
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(NAQUADRIA, AMERICIUM, 15, 2).setIsSecret();
                tMutation.requireResource(GregTech_API.sBlockMetal5, 2);
            }
    ),
    //Twilight
    NAGA(GT_BranchDefinition.TWILIGHT, "Naga", true, new Color(0x0D5A0D), new Color(0x28874B),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.02f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAGA), 0.10f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Eldritch"), getSpecies(FORESTRY, "Imperial"), 10);
                tMutation.restrictHumidity(DAMP);
            }
    ),
    LICH(GT_BranchDefinition.TWILIGHT, "Lich", true, new Color(0xC5C5C5), new Color(0x5C605E),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.04f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LICH), 0.10f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Supernatural"), NAGA, 10);
                tMutation.restrictHumidity(ARID);
            }
    ),
    HYDRA(GT_BranchDefinition.TWILIGHT, "Hydra", true, new Color(0x872836), new Color(0xB8132C),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.06f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.HYDRA), 0.10f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HELLISH);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(LICH, getSpecies(MAGICBEES, "TCFire"), 10);
                tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(138, "Undergarden"));//undergarden biome
            }
    ),
    URGHAST(GT_BranchDefinition.TWILIGHT, "UrGhast", true, new Color(0xA7041C), new Color(0x7C0618),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.08f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.URGHAST), 0.10f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(HELLISH);
                beeSpecies.setHasEffect();
                beeSpecies.setNocturnal();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.FAST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(HYDRA, THAUMIUMDUST, 10);
                if (Loader.isModLoaded("Thaumcraft"))
                    tMutation.requireResource(GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 4);
                tMutation.restrictTemperature(HELLISH);
            }
    ),
    SNOWQUEEN(GT_BranchDefinition.TWILIGHT, "SnowQueen", true, new Color(0xD02001), new Color(0x9C0018),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.15f / nerfAmount);
                beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SNOWQUEEN), 0.10f / nerfAmount);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setHasEffect();
                beeSpecies.setNocturnal();
            },
            template -> {
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(URGHAST, SALISMUNDUS, 10);
                if (Loader.isModLoaded("thaumicbases"))
                    tMutation.requireResource(GameRegistry.findBlock("thaumicbases", "blockSalisMundus"), 0);
                tMutation.restrictTemperature(ICY);
            }
    ),

    //Infinity Line
    COSMICNEUTRONIUM(GT_BranchDefinition.PLANET, "CosmicNeutronium", false, new Color(0x484848), new Color(0x323232),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.COSMICNEUTRONIUM), 0.25f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(NEUTRONIUM, DOB, 15, 10);
                /*if (Loader.isModLoaded("Avaritia"))
                    tMutation.requireResource(GameRegistry.findBlock("Avaritia", "Resource_Block"), 0);*/
            }
    ),
    INFINITYCATALYST(GT_BranchDefinition.PLANET, "InfinityCatalyst", false, new Color(0xFFFFFF), new Color(0xFFFFFF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFINITYCATALYST), 0.005f / nerfAmount);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(HELLISH);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTENED);
                AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "blindness"));
            },
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(DOB, COSMICNEUTRONIUM, 15, 10).setIsSecret();
                /*if (Loader.isModLoaded("Avaritia"))
                    tMutation.requireResource(GameRegistry.findBlock("Avaritia", "Resource_Block"), 1);*/
            }
    ),
    INFINITY(GT_BranchDefinition.PLANET, "Infinity", false, new Color(0xFFFFFF), new Color(0xFFFFFF),
            beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFINITY), 0.05f / nerfAmount);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(INFINITYCATALYST, COSMICNEUTRONIUM, 15, 10).setIsSecret();
            }
    ),
    ;
    private final GT_BranchDefinition branch;
    private final GT_AlleleBeeSpecies species;
    private final Consumer<GT_AlleleBeeSpecies> mSpeciesProperties;
    private final Consumer<IAllele[]> mAlleles;
    private final Consumer<GT_BeeDefinition> mMutations;
    private IAllele[] template;
    private IBeeGenome genome;

    GT_BeeDefinition(GT_BranchDefinition branch,
                     String binomial,
                     boolean dominant,
                     Color primary,
                     Color secondary,
                     Consumer<GT_AlleleBeeSpecies> aSpeciesProperties,
                     Consumer<IAllele[]> aAlleles,
                     Consumer<GT_BeeDefinition> aMutations
    ) {
        this.mAlleles = aAlleles;
        this.mMutations = aMutations;
        this.mSpeciesProperties = aSpeciesProperties;
        String lowercaseName = this.toString().toLowerCase(Locale.ENGLISH);
        String species = WordUtils.capitalize(lowercaseName);

        String uid = "gregtech.bee.species" + species;
        String description = "for.description." + species;
        String name = "for.bees.species." + lowercaseName;
        GT_LanguageManager.addStringLocalization("for.bees.species." + lowercaseName, species, true);

        this.branch = branch;
        this.species = new GT_AlleleBeeSpecies(uid, dominant, name, "GregTech", description, branch.getBranch(), binomial, primary, secondary);
    }

    public static void initBees() {
        for (GT_BeeDefinition bee : values()) {
            bee.init();
        }
        for (GT_BeeDefinition bee : values()) {
            bee.registerMutations();
        }
    }

    protected static IAlleleBeeEffect getEffect(byte modid, String name) {
        String s;
        switch (modid) {
            case EXTRABEES:
                s = "extrabees.effect." + name;
                break;
            case GENDUSTRY:
                s = "gendustry.effect." + name;
                break;
            case MAGICBEES:
                s = "magicbees.effect" + name;
                break;
            case GREGTECH:
                s = "gregtech.effect" + name;
                break;
            default:
                s = "forestry.effect" + name;
                break;

        }
        return (IAlleleBeeEffect) AlleleManager.alleleRegistry.getAllele(s);
    }

    protected static IAlleleFlowers getFlowers(byte modid, String name) {
        String s;
        switch (modid) {
            case EXTRABEES:
                s = "extrabees.flower." + name;
                break;
            case GENDUSTRY:
                s = "gendustry.flower." + name;
                break;
            case MAGICBEES:
                s = "magicbees.flower" + name;
                break;
            case GREGTECH:
                s = "gregtech.flower" + name;
                break;
            default:
                s = "forestry.flowers" + name;
                break;

        }
        return (IAlleleFlowers) AlleleManager.alleleRegistry.getAllele(s);
    }

    protected static IAlleleBeeSpecies getSpecies(byte modid, String name) {
        String s;
        switch (modid) {
            case EXTRABEES:
                s = "extrabees.species." + name;
                break;
            case GENDUSTRY:
                s = "gendustry.bee." + name;
                break;
            case MAGICBEES:
                s = "magicbees.species" + name;
                break;
            case GREGTECH:
                s = "gregtech.species" + name;
                break;
            default:
                s = "forestry.species" + name;
                break;

        }
        IAlleleBeeSpecies ret = (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(s);
        if (ret == null) {
            ret = NAQUADRIA.species;
        }

        return ret;
    }


    protected final void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
        this.mSpeciesProperties.accept(beeSpecies);
    }

    protected final void setAlleles(IAllele[] template) {
        this.mAlleles.accept(template);
    }

    protected final void registerMutations() {
        this.mMutations.accept(this);
    }

    private void init() {
        setSpeciesProperties(species);

        template = branch.getTemplate();
        AlleleHelper.instance.set(template, SPECIES, species);
        setAlleles(template);

        genome = BeeManager.beeRoot.templateAsGenome(template);

        BeeManager.beeRoot.registerTemplate(template);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    protected final IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, IAlleleBeeSpecies parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GT_BeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    protected final IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, GT_BeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    /**
     * Diese neue Funtion erlaubt Mutationsraten unter 1%. Setze dazu die Mutationsrate als Bruch mit chance / chancedivider
     * This new function allows Mutation percentages under 1%. Set them as a fraction with chance / chancedivider
     */
    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance, float chancedivider) {
        return new GT_Bee_Mutation(parent1, parent2, this.getTemplate(), chance, chancedivider);
    }

    protected final IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, IAlleleBeeSpecies parent2, int chance, float chancedivider) {
        return registerMutation(parent1.species, parent2, chance, chancedivider);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GT_BeeDefinition parent2, int chance, float chancedivider) {
        return registerMutation(parent1, parent2.species, chance, chancedivider);
    }

    protected final IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, GT_BeeDefinition parent2, int chance, float chancedivider) {
        return registerMutation(parent1.species, parent2, chance, chancedivider);
    }

    @Override
    public final IAllele[] getTemplate() {
        return Arrays.copyOf(template, template.length);
    }

    @Override
    public final IBeeGenome getGenome() {
        return genome;
    }

    @Override
    public final IBee getIndividual() {
        return new Bee(genome);
    }

    @Override
    public final ItemStack getMemberStack(EnumBeeType beeType) {
        return BeeManager.beeRoot.getMemberStack(getIndividual(), beeType.ordinal());
    }

    public final IBeeDefinition getRainResist() {
        return new BeeVariation.RainResist(this);
    }


}
