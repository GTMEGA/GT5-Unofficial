package gregtech.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.items.GT_CoolantCellIC_Item;
import gregtech.api.items.GT_CoolantCell_Item;
import gregtech.api.items.GT_Tool_Item;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;
import gregtech.api.objects.GT_Cover_Default;
import gregtech.api.objects.GT_Cover_None;
import gregtech.api.objects.GT_HashSet;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.threads.GT_Runnable_Cable_Update;
import gregtech.api.threads.GT_Runnable_MachineBlockUpdate;
import gregtech.api.util.*;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.items.GT_MEGAnet;
import gregtech.common.items.GT_Item_RemoteDetonator;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static gregtech.api.enums.GT_Values.*;

/**
 * Please do not include this File in your Mod-download as it ruins compatiblity, like with the IC2-API
 * You may just copy those Functions into your Code, or better call them via reflection.
 * <p/>
 * The whole API is the basic construct of my Mod. Everything is dependent on it.
 * I change things quite often so please don't include any File inside your Mod, even if it is an Interface.
 * Since some Authors were stupid enough to break this simple Rule, I added Version checks to enforce it.
 * <p/>
 * In these Folders are many useful Functions. You can use them via reflection if you want.
 * I know not everything is compilable due to API's of other Mods, but these are easy to fix in your Setup.
 * <p/>
 * You can use this to learn about Modding, but I would recommend simpler Mods.
 * You may even copypaste Code from these API-Files into your Mod, as I have nothing against that, but you should look exactly at what you are copying.
 *
 * @author Gregorius Techneticies
 */
@SuppressWarnings("ALL")
public class GregTech_API {

    @Deprecated
    public static final long MATERIAL_UNIT = M, FLUID_MATERIAL_UNIT = L;
    /**
     * Fixes the HashMap Mappings for ItemStacks once the Server started
     */
    public static final Collection<Map<GT_ItemStack, ?>> sItemStackMappings = new ArrayList<>();
    public static final Collection<Map<Fluid, ?>> sFluidMappings = new ArrayList<>();
    /**
     * The MetaTileEntity-ID-List-Length
     */
    public static final short MAXIMUM_METATILE_IDS = Short.MAX_VALUE - 1;
    /**
     * My Creative Tab
     */
    public static final CreativeTabs
            TAB_GREGTECH = new GT_CreativeTab("Main", "Main"),
            TAB_GREGTECH_MATERIALS = new GT_CreativeTab("Materials", "Materials"),
            TAB_GREGTECH_ORES = new GT_CreativeTab("Ores", "Ores");
    /**
     * A List of all registered MetaTileEntities
     * <p/>
     * 0 	-  749  are used by GregTech.
     * 750	-  999  are reserved for Alkalus.
     * 1000 -  2047 are used by GregTech.
     * 2048 -  2559 are reserved for OvermindDL.
     * 2560 -  3071 are reserved for Immibis.
     * 3072 -  3583 are reserved for LinusPhoenix.
     * 3584 -  4095 are reserved for BloodyAsp.
     * 4096 -  5095 are used for GregTech Frames.
     * 5096 -  6099 are used for GregTech Pipes.
     * 6100 -  8191 are used for GregTech Decoration Blocks.
     * 8192 -  8703 are reserved for ZL123.
     * 8704 -  9215 are reserved for Mr10Movie.
     * 9216 -  9727 are used for GregTech Automation Machines.
     * 9728 - 10239 are reserved for 28Smiles.
     * 10240 - 10751 are reserved for VirMan.
     * 10752 - 11263 are reserved for Briareos81.
     * 11264 - 12000 are reserved for Quantum64.
     * 12001 - 12500 are reserved for RedMage17.
     * 12501 - 13000 are reserved for bartimaeusnek.
     * 13001 - 13100 are reserved for Techlone
     * 13101 - 13500 are reserved for kekzdealer
     * 13501 - 14999 are currently free.
     * 15000 - 16999 are reserved for TecTech.
     * 17000 - 29999 are currently free.
     * 30000 - 31999 are reserved for Alkalus.
     * 32001 - 32766 are currently free.
     * <p/>
     * Contact me if you need a free ID-Range, which doesn't conflict with other Addons.
     * You could make an ID-Config, but we all know, what "stupid" customers think about conflicting ID's
     */
    public static final IMetaTileEntity[] METATILEENTITIES = new IMetaTileEntity[MAXIMUM_METATILE_IDS];
    /**
     * The Icon List for Covers
     */
    public static final Map<GT_ItemStack, ITexture> sCovers = new ConcurrentHashMap<>();
    /**
     * The List of Cover Behaviors for the Covers
     */
    public static final Map<GT_ItemStack, GT_CoverBehaviorBase<?>> sCoverBehaviors = new ConcurrentHashMap<>();
    /**
     * The List of Circuit Behaviors for the Redstone Circuit Block
     */
    public static final Map<Integer, GT_CircuitryBehavior> sCircuitryBehaviors = new ConcurrentHashMap<>();
    /**
     * The List of Blocks, which can conduct Machine Block Updates
     */
    public static final Map<Block, Integer> sMachineIDs = new ConcurrentHashMap<>();
    /**
     * The Redstone Frequencies
     */
    public static final Map<Integer, Byte> sWirelessRedstone = new ConcurrentHashMap<>();
    /**
     * The IDSU Frequencies
     */
    public static final Map<Integer, Integer> sIDSUList = new ConcurrentHashMap<>();
    /**
     * A List of all Books, which were created using @GT_Utility.getWrittenBook the original Title is the Key Value
     */
    public static final Map<String, ItemStack> sBookList = new ConcurrentHashMap<>();
    /**
     * The List of all Sounds used in GT, indices are in the static Block at the bottom
     */
    public static final Map<Integer, String> sSoundList = new ConcurrentHashMap<>();
    /**
     * The List of Tools, which can be used. Accepts regular damageable Items and Electric Items
     */
    public static final GT_HashSet<GT_ItemStack>
            sToolList = new GT_HashSet<>(),
            sCrowbarList = new GT_HashSet<>(),
            sScrewdriverList = new GT_HashSet<>(),
            sWrenchList = new GT_HashSet<>(),
            sSoftHammerList = new GT_HashSet<>(),
            sHardHammerList = new GT_HashSet<>(),
            sWireCutterList = new GT_HashSet<>(),
            sSolderingToolList = new GT_HashSet<>(),
            sSolderingMetalList = new GT_HashSet<>();
    /**
     * The List of Hazmat Armors
     */
    public static final GT_HashSet<GT_ItemStack>
            sGasHazmatList = new GT_HashSet<>(),
            sBioHazmatList = new GT_HashSet<>(),
            sFrostHazmatList = new GT_HashSet<>(),
            sHeatHazmatList = new GT_HashSet<>(),
            sRadioHazmatList = new GT_HashSet<>(),
            sElectroHazmatList = new GT_HashSet<>();

    private static final List<ItemStack> sRealConfigurationList = new ArrayList<>();
    private static final List<ItemStack> sConfigurationList = Collections.unmodifiableList(sRealConfigurationList);

    /**
     * The List of Dimensions, which are Whitelisted for the Teleporter. This list should not contain other Planets.
     * Mystcraft Dimensions and other Dimensional Things should be allowed.
     * Mystcraft and Twilight Forest are automatically considered a Dimension, without being in this List.
     */
    public static final Collection<Integer> sDimensionalList = new HashSet<>();
    /**
     * Lists of all the active World generation Features, these are getting Initialized in Postload!
     */
    public static final List<GT_Worldgen> sWorldgenList = new ArrayList<>();
    /**
     * A List containing all the Materials, which are somehow in use by GT and therefor receive a specific Set of Items.
     */
    public static final Materials[] sGeneratedMaterials = new Materials[1000];
    /**
     * This is the generic Cover behavior. Used for the default Covers, which have no Behavior.
     */
    public static final GT_CoverBehavior
            sDefaultBehavior = new GT_Cover_Default(),
            sNoBehavior = new GT_Cover_None();
    /**
     * For the API Version check
     */
    public static volatile int VERSION = 509;

    @Deprecated
    public static IGT_RecipeAdder sRecipeAdder;
    /**
     * Used to register Aspects to ThaumCraft, this Object might be null if ThaumCraft isn't installed
     */
    public static IThaumcraftCompat sThaumcraftCompat;
    /**
     * These Lists are getting executed at their respective timings. Useful if you have to do things right before/after I do them, without having to control the load order. Add your "Commands" in the Constructor or in a static Code Block of your Mods Main Class. These are not Threaded, I just use a native Java Interface for their execution. Implement just the Method run() and everything should work
     */
    public static List<Runnable>
            sBeforeGTPreload = new ArrayList<>(),
            sAfterGTPreload = new ArrayList<>(),
            sBeforeGTLoad = new ArrayList<>(),
            sAfterGTLoad = new ArrayList<>(),
            sBeforeGTPostload = new ArrayList<>(),
            sAfterGTPostload = new ArrayList<>(),
            sBeforeGTServerstart = new ArrayList<>(),
            sAfterGTServerstart = new ArrayList<>(),
            sBeforeGTServerstop = new ArrayList<>(),
            sAfterGTServerstop = new ArrayList<>(),
            sGTBlockIconload = new ArrayList<>(),
            sGTItemIconload = new ArrayList<>();
    /**
     * The Icon Registers from Blocks and Items. They will get set right before the corresponding Icon Load Phase as executed in the Runnable List above.
     */
    @SideOnly(Side.CLIENT)
    public static IIconRegister
            sBlockIcons,
            sItemIcons;
    /**
     * The Configuration Objects
     */
    public static GT_Config
            sRecipeFile = null,
            sMachineFile = null,
            sWorldgenFile = null,
            sMaterialProperties = null,
            sMaterialComponents = null,
            sUnification = null,
            sSpecialFile = null,
            sClientDataFile,
            sOPStuff = null;
    public static int
            TICKS_FOR_LAG_AVERAGING = 25,
            MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = 100;
    /**
     * Initialized by the Block creation.
     */
    public static Block sBlockMachines;

    public static Block
            sBlockOres1,
            sBlockOresUb1,
            sBlockOresUb2,
            sBlockOresUb3,
    /*sBlockGem,*/
    sBlockMetal1,
            sBlockMetal2,
            sBlockMetal3,
            sBlockMetal4,
            sBlockMetal5,
            sBlockMetal6,
            sBlockMetal7,
            sBlockMetal8,
            sBlockGem1,
            sBlockGem2,
            sBlockGem3,
            sBlockReinforced;
    public static Block
            sBlockGranites,
            sBlockConcretes,
            sBlockStones;
    public static Block
            sBlockCasings1,
            sBlockCasings2,
            sBlockCasings3,
            sBlockCasings4,
            sBlockCasings5,
            sBlockCasings6,
            sBlockCasings8;
    public static Block
            sBlockLongDistancePipes;

    public static GT_MEGAnet sMEGAnet;

    public static Block sPotentiometer;

    /**
     * Getting assigned by the Config
     */
    public static boolean
            sTimber = true,
            sDrinksAlwaysDrinkable = false,
            sMultiThreadedSounds = false,
            sDoShowAllItemsInCreative = false,
            sColoredGUI = true,
            sMachineMetalGUI = false,
            sConstantEnergy = true,
            sMachineExplosions = true,
            sMachineFlammable = true,
            sMachineNonWrenchExplosions = true,
            sMachineRainExplosions = true,
            sMachineThunderExplosions = true,
            sMachineFireExplosions = true,
            sMachineWireFire = true,
            mOutputRF = false,
            mInputRF = false,
            meIOLoaded = false,
            mRFExplosions = true,
            mServerStarted = false,
            mIC2Classic = false,
            mMagneticraft = false,
            mImmersiveEngineering = false,
            mGTPlusPlus = false,
            mTranslocator = false,
            mTConstruct = false,
            mGalacticraft = false,
            mAE2 = false,
            mNEI = false,
            mDWS = false;
    public static int
            mEUtoRF = 360,
            mRFtoEU = 20;


    /**
     * Option to not use MACHINE_METAL mixing into colors
     */
    public static boolean sUseMachineMetal = false;


    public static boolean mUseOnlyGoodSolderingMaterials = true;

    private static final String aTextIC2Lower = MOD_ID_IC2.toLowerCase(Locale.ENGLISH);
    /**
     * Getting assigned by the Mod loading
     */
    public static boolean
            sUnificationEntriesRegistered = false,
            sPreloadStarted = false,
            sPreloadFinished = false,
            sLoadStarted = false,
            sLoadFinished = false,
            sPostloadStarted = false,
            sPostloadFinished = false;

    public static GT_Item_RemoteDetonator sItemRemoteDetonator;
    public static Item                    sBorker;

    private static Class sBaseMetaTileEntityClass = null;

    public static void addIC2SoundToList(final int index, final @NonNull String name) {
        addSoundToList(index, name, aTextIC2Lower);
    }

    public static void addSoundToList(final int index, final @NonNull String name, final String prefix) {
        sSoundList.put(index, prefix + ":" + name);
    }

    public static void addGTSoundToList(final int index, final @NonNull String name) {
        addSoundToList(index, name, MOD_ID.toLowerCase());
    }

    public static void addSoundToList(final int index, final @NonNull String name) {
        sSoundList.put(index, name);
    }

    /**
     * Adds Biomes to the Biome Lists for World Generation
     */
    static {
        sItemStackMappings.add(sCovers);
        sItemStackMappings.add(sCoverBehaviors);
        addSoundToList(0, "random.break");
        addSoundToList(1, "random.anvil_use");
        addSoundToList(2, "random.anvil_break");
        addSoundToList(3, "random.click");
        addSoundToList(4, "random.fizz");
        addSoundToList(5, "random.explode");
        addSoundToList(6, "fire.ignite");
        //
        addIC2SoundToList(100, "tools.Wrench");
        addIC2SoundToList(101, "tools.RubberTrampoline");
        addIC2SoundToList(102, "tools.Painter");
        addIC2SoundToList(103, "tools.BatteryUse");
        addIC2SoundToList(104, "tools.chainsaw.ChainsawUseOne");
        addIC2SoundToList(105, "tools.chainsaw.ChainsawUseTwo");
        addIC2SoundToList(106, "tools.drill.DrillSoft");
        addIC2SoundToList(107, "tools.drill.DrillHard");
        addIC2SoundToList(108, "tools.ODScanner");
        addIC2SoundToList(109, "tools.InsulationCutters");
        //
        addIC2SoundToList(200, "machines.ExtractorOp");
        addIC2SoundToList(201, "machines.MaceratorOp");
        addIC2SoundToList(202, "machines.InductionLoop");
        addIC2SoundToList(203, "machines.CompressorOp");
        addIC2SoundToList(204, "machines.RecyclerOp");
        addIC2SoundToList(205, "machines.MinerOp");
        addIC2SoundToList(206, "machines.PumpOp");
        addIC2SoundToList(207, "machines.ElectroFurnaceLoop");
        addIC2SoundToList(208, "machines.InductionLoop");
        addIC2SoundToList(209, "machines.MachineOverload");
        addIC2SoundToList(210, "machines.InterruptOne");
        addIC2SoundToList(211, "machines.KaChing");
        addIC2SoundToList(212, "machines.MagnetizerLoop");
        //Keep these deep-fried in dev.
        addGTSoundToList(213, "mining_explosives_explosion");
        addGTSoundToList(214, "mining_explosives_trigger");
        addGTSoundToList(215, "meganet_pickup");
        addGTSoundToList(216, "meganet_active");
        addGTSoundToList(217, "meganet_inactive");
        addGTSoundToList(218, "remote_detonator_add");
        addGTSoundToList(219, "remote_detonator_remove");
        addGTSoundToList(220, "remote_detonator_trigger");
        addGTSoundToList(221, "potentiometer_click");
        addGTSoundToList(222, "macerator");
        addGTSoundToList(223, "furnace");
        addGTSoundToList(224, "steam_furnace");
        addGTSoundToList(225, "steam_vent");
        addGTSoundToList(226, "boiler_vent");
        addGTSoundToList(227, "steam_hammer");
        addGTSoundToList(228, "seismic");
        addGTSoundToList(229, "mcdonal");
        addGTSoundToList(230, "electrolyzer");
        addGTSoundToList(231, "centrifuge");
        addGTSoundToList(232, "thermal");
        //Don't forget to put your sounds into sounds.json too!
    }

    /**
     * You want OreDict-Unification for YOUR Mod/Addon, when GregTech is installed? This Function is especially for YOU.
     * Call this Function after the load-Phase, as I register the the most of the Unification at that Phase (Redpowers Storageblocks are registered at postload).
     * A recommended use of this Function is inside your Recipe-System itself (if you have one), as the unification then makes 100% sure, that every added non-unificated Output gets automatically unificated.
     * <p/>
     * I will personally make sure, that only common prefixes of Ores get registered at the Unificator, as of now there are:
     * pulp, dust, dustSmall, ingot, nugget, gem, ore and block
     * If another Mod-Author messes these up, then it's not my fault and it's especially not your fault. As these are commonly used prefixes.
     * <p/>
     * This Unificator-API-Function uses the same Functions I use, for unificating Items. So if there is something messed up (very unlikely), then everything is messed up.
     * <p/>
     * You shouldn't use this to unificate the Inputs of your Recipes, this is only meant for the Outputs.
     *
     * @param aOreStack the Stack you want to get unificated. It is stackSize Sensitive.
     * @return Either an unificated Stack or the stack you toss in, but it should never be null, unless you throw a Nullpointer into it.
     */
    public static ItemStack getUnificatedOreDictStack(ItemStack aOreStack) {
        if (!GregTech_API.sPreloadFinished)
            GT_Log.err.println("GregTech_API ERROR: " + aOreStack.getItem() + "." + aOreStack.getItemDamage() + " - OreDict Unification Entries are not registered now, please call it in the postload phase.");
        return GT_OreDictUnificator.get(true, aOreStack);
    }

    /**
     * Causes a Machineblock Update
     * This update will cause surrounding MultiBlock Machines to update their Configuration.
     * You should call this Function in @Block.breakBlock and in @Block.onBlockAdded of your Machine.
     *
     * @param aWorld is being the World
     * @param aX     is the X-Coord of the update causing Block
     * @param aY     is the Y-Coord of the update causing Block
     * @param aZ     is the Z-Coord of the update causing Block
     */
    public static boolean causeMachineUpdate(World aWorld, int aX, int aY, int aZ) {
        if (aWorld != null && !aWorld.isRemote) { // World might be null during Worldgen
            GT_Runnable_MachineBlockUpdate.setMachineUpdateValues(aWorld, new ChunkCoordinates(aX, aY, aZ));
            return true;
        }
        return false;
    }

    public static boolean causePipeUpdate(World aWorld, int aX, int aY, int aZ, IMetaTileEntity meta) {
        if (aWorld != null && !aWorld.isRemote) {
            // World might be null during Worldgen
            if (meta instanceof GT_MetaPipeEntity_Cable)
                GT_Runnable_Cable_Update.setCableUpdateValues(aWorld, new ChunkCoordinates(aX, aY, aZ));
            else if (meta instanceof GT_MetaPipeEntity_Item)
                GT_Runnable_Cable_Update.setPipeUpdateValues(aWorld, new ChunkCoordinates(aX, aY, aZ));
            return true;
        }
        return false;
    }

    /**
     * Adds a Multi-Machine Block, like my Machine Casings for example.
     * You should call @causeMachineUpdate in @Block.breakBlock and in @Block.onBlockAdded of your registered Block.
     * You don't need to register TileEntities which implement @IMachineBlockUpdateable
     *
     * @param aBlock   the Block
     * @param aMeta the Metadata of the Blocks as Bitmask! -1 or ~0 for all Metavalues
     */
    public static boolean registerMachineBlock(Block aBlock, int aMeta) {
        if (aBlock == null)
            return false;
        if (GregTech_API.sThaumcraftCompat != null)
            GregTech_API.sThaumcraftCompat.registerPortholeBlacklistedBlock(aBlock);
        sMachineIDs.put(aBlock, aMeta);
        return true;
    }

    /**
     * Like above but with boolean Parameters instead of a BitMask
     */
    public static boolean registerMachineBlock(Block aBlock, boolean... aMeta) {
        if (aBlock == null || aMeta == null || aMeta.length == 0)
            return false;
        if (GregTech_API.sThaumcraftCompat != null)
            GregTech_API.sThaumcraftCompat.registerPortholeBlacklistedBlock(aBlock);
        int rMeta = 0;
        for (byte i = 0; i < aMeta.length && i < 16; i++) if (aMeta[i]) rMeta |= B[i];
        sMachineIDs.put(aBlock, rMeta);
        return true;
    }

    /**
     * if this Block is a Machine Update Conducting Block
     */
    public static boolean isMachineBlock(Block aBlock, int aMeta) {
        if (aBlock != null) {
            Integer id = sMachineIDs.get(aBlock);
            return id != null && (id & B[aMeta]) != 0;
        }
        return false;
    }

    /**
     * Creates a new Coolant Cell Item for your Nuclear Reactor
     */
    public static Item constructCoolantCellItem(String aUnlocalized, String aEnglish, int aMaxStore) {
        try {
            return new GT_CoolantCellIC_Item(aUnlocalized, aEnglish, aMaxStore);
//			return (Item)Class.forName("gregtech.api.items.GT_CoolantCellIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxStore);
        } catch (Throwable e) {/*Do nothing*/}
        try {
            return new GT_CoolantCell_Item(aUnlocalized, aEnglish, aMaxStore);
//			return (Item)Class.forName("gregtech.api.items.GT_CoolantCell_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxStore);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Generic_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", false);
    }

    /**
     * Creates a new Energy Armor Item
     */
    public static Item constructElectricArmorItem(String aUnlocalized, String aEnglish, int aCharge, int aTransfer, int aTier, int aDamageEnergyCost, int aSpecials, double aArmorAbsorbtionPercentage, boolean aChargeProvider, int aType, int aArmorIndex) {
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyArmorIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aDamageEnergyCost, aSpecials, aArmorAbsorbtionPercentage, aChargeProvider, aType, aArmorIndex);
        } catch (Throwable e) {/*Do nothing*/}
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyArmor_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aDamageEnergyCost, aSpecials, aArmorAbsorbtionPercentage, aChargeProvider, aType, aArmorIndex);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Generic_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", false);
    }

    /**
     * Creates a new Energy Battery Item
     */
    public static Item constructElectricEnergyStorageItem(String aUnlocalized, String aEnglish, int aCharge, int aTransfer, int aTier, int aEmptyID, int aFullID) {
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyStoreIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aEmptyID, aFullID);
        } catch (Throwable e) {/*Do nothing*/}
        try {
            return (Item) Class.forName("gregtechmod.api.items.GT_EnergyStore_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aEmptyID, aFullID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Generic_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", false);
    }

    /**
     * Creates a new Hard Hammer Item
     */
    public static GT_Tool_Item constructHardHammerItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_HardHammer_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new Crowbar Item
     */
    public static GT_Tool_Item constructCrowbarItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_CrowbarRC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
        } catch (Throwable e) {/*Do nothing*/}
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_Crowbar_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new Wrench Item
     */
    public static GT_Tool_Item constructWrenchItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_Wrench_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new electric Screwdriver Item
     */
    public static GT_Tool_Item constructElectricScrewdriverItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_ScrewdriverIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new electric Wrench Item
     */
    public static GT_Tool_Item constructElectricWrenchItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_WrenchIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new electric Saw Item
     */
    public static GT_Tool_Item constructElectricSawItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak, int aDisChargedGTID) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_SawIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aToolQuality, aToolStrength, aEnergyConsumptionPerBlockBreak, aDisChargedGTID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new electric Drill Item
     */
    public static GT_Tool_Item constructElectricDrillItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak, int aDisChargedGTID) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_DrillIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aToolQuality, aToolStrength, aEnergyConsumptionPerBlockBreak, aDisChargedGTID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new electric Soldering Tool
     */
    public static GT_Tool_Item constructElectricSolderingToolItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_SolderingToolIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
    }

    /**
     * Creates a new empty electric Tool
     */
    public static GT_Tool_Item constructEmptyElectricToolItem(String aUnlocalized, String aEnglish, int aMaxDamage, int aChargedGTID) {
        try {
            return (GT_Tool_Item) Class.forName("gregtechmod.api.items.GT_EmptyToolIC_Item").getConstructors()[0].newInstance(aUnlocalized, aEnglish, aMaxDamage, aChargedGTID);
        } catch (Throwable e) {/*Do nothing*/}
        return new gregtech.api.items.GT_Tool_Item(aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, 0, false);
    }

    /**
     * This gives you a new BaseMetaTileEntity. As some Interfaces are not always loaded (Buildcraft, Univeral Electricity) I have to use Invocation at the Constructor of the BaseMetaTileEntity
     */
    public static BaseMetaTileEntity constructBaseMetaTileEntity() {
        if (sBaseMetaTileEntityClass == null) {
            try {
                return (BaseMetaTileEntity) (sBaseMetaTileEntityClass = BaseMetaTileEntity.class).newInstance();
            } catch (Throwable e) {/*Do nothing*/}
        }

        try {
            return (BaseMetaTileEntity) (sBaseMetaTileEntityClass.newInstance());
        } catch (Throwable e) {
            GT_Log.err.println("GT_Mod: Fatal Error ocurred while initializing TileEntities, crashing Minecraft.");
            e.printStackTrace(GT_Log.err);
            throw new RuntimeException(e);
        }
    }


    /**
     * Register a new ItemStack as configuration circuits.
     * Duplicates or invalid stacks will be silently ignored.
     */
    public static void registerConfigurationCircuit(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack))
            return;
        for (ItemStack tRegistered : sRealConfigurationList)
            if (GT_Utility.areStacksEqual(tRegistered, aStack))
                return;
        sRealConfigurationList.add(GT_Utility.copyAmount(0, aStack));
    }

    /**
     * Get a list of Configuration circuits. All of these stacks will have a stack size of 0.
     * Use {@link #registerConfigurationCircuit(ItemStack)} to add to this list.
     *
     * @return An unmodifiable view of actual list.
     * It will reflect the changes to the underlying list as new circuits are registered.
     * DO NOT MODIFY THE ItemStacks!
     */
    public static List<ItemStack> getConfigurationCircuitList() {
        return sConfigurationList;
    }

    public static void registerCover(ItemStack aStack, ITexture aCover, GT_CoverBehavior aBehavior) {
        registerCover(aStack, aCover, (GT_CoverBehaviorBase<?>) aBehavior);
    }

    public static void registerCover(ItemStack aStack, ITexture aCover, GT_CoverBehaviorBase<?> aBehavior) {
        if (!sCovers.containsKey(new GT_ItemStack(aStack)))
            sCovers.put(new GT_ItemStack(aStack), aCover == null || !aCover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : aCover);
        if (aBehavior != null)
            sCoverBehaviors.put(new GT_ItemStack(aStack), aBehavior);
    }

    public static void registerCoverBehavior(ItemStack aStack, GT_CoverBehavior aBehavior) {
        registerCoverBehavior(aStack, (GT_CoverBehaviorBase<?>) aBehavior);
    }

    public static void registerCoverBehavior(ItemStack aStack, GT_CoverBehaviorBase<?> aBehavior) {
        sCoverBehaviors.put(new GT_ItemStack(aStack), aBehavior == null ? sDefaultBehavior : aBehavior);
    }

    /**
     * Registers multiple Cover Items. I use that for the OreDict Functionality.
     *
     * @param aBehavior can be null
     */
    public static void registerCover(Collection<ItemStack> aStackList, ITexture aCover, GT_CoverBehavior aBehavior) {
        registerCover(aStackList, aCover, aBehavior);
    }

    /**
     * Registers multiple Cover Items. I use that for the OreDict Functionality.
     *
     * @param aBehavior can be null
     */
    public static void registerCover(Collection<ItemStack> aStackList, ITexture aCover, GT_CoverBehaviorBase<?> aBehavior) {
        if (aCover.isValidTexture())
            aStackList.forEach(tStack -> GregTech_API.registerCover(tStack, aCover, aBehavior));
    }

    /**
     * returns a Cover behavior, guaranteed to not return null after preload
     */
    @Deprecated
    public static GT_CoverBehavior getCoverBehavior(ItemStack aStack) {
        if (aStack == null || aStack.getItem() == null)
            return sNoBehavior;
        GT_CoverBehaviorBase<?> rCover = sCoverBehaviors.get(new GT_ItemStack(aStack));
        if (!(rCover instanceof GT_CoverBehavior) || rCover == null)
            return sDefaultBehavior;
        return (GT_CoverBehavior) rCover;
    }

    /**
     * returns a Cover behavior, guaranteed to not return null after preload
     * @return
     */
    public static GT_CoverBehaviorBase<?> getCoverBehaviorNew(ItemStack aStack) {
        if (aStack == null || aStack.getItem() == null)
            return sNoBehavior;
        GT_CoverBehaviorBase<?> rCover = sCoverBehaviors.get(new GT_ItemStack(aStack));
        if (rCover == null)
            return sDefaultBehavior;
        return rCover;
    }

    /**
     * returns a Cover behavior, guaranteed to not return null
     */
    @Deprecated
    public static GT_CoverBehavior getCoverBehavior(int aStack) {
        if (aStack == 0)
            return sNoBehavior;
        return getCoverBehavior(GT_Utility.intToStack(aStack));
    }

    /**
     * returns a Cover behavior, guaranteed to not return null
     */
    public static GT_CoverBehaviorBase<?> getCoverBehaviorNew(int aStack) {
        if (aStack == 0)
            return sNoBehavior;
        return getCoverBehaviorNew(GT_Utility.intToStack(aStack));
    }

    /**
     * Register a Wrench to be usable on GregTech Machines.
     * The Wrench MUST have some kind of Durability unlike certain Buildcraft Wrenches.
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     * <p/>
     * -----
     * <p/>
     * Returning true at isDamagable was a great Idea, KingLemming. Well played.
     * Since the OmniWrench is just a Single-Item-Mod, people can choose if they want your infinite durability or not. So that's not really a Problem.
     * I even have a new Config to autodisable most infinite BC Wrenches (but that one is turned off).
     * <p/>
     * One last Bug for you to fix:
     * My Autoregistration detects Railcrafts Crowbars, Buildcrafts Wrenches and alike, due to their Interfaces.
     * Guess what now became a Crowbar by accident. Try registering the Wrench at the load phase to prevent things like that from happening.
     * Yes, I know that "You need to register Tools in the Load Phase"-Part wasn't there before this. Sorry about that.
     */
    public static boolean registerWrench(ItemStack aTool) {
        return registerTool(aTool, sWrenchList);
    }

    /**
     * Register a Crowbar to extract Covers from Machines
     * Crowbars are NOT Wrenches btw.
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerCrowbar(ItemStack aTool) {
        return registerTool(aTool, sCrowbarList);
    }

    /**
     * Register a Screwdriver to interact directly with Machines and Covers
     * Did I mention, that it is intentionally not possible to make a Multitool, which doesn't switch ItemID (like a Mode) all the time?
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerScrewdriver(ItemStack aTool) {
        return registerTool(aTool, sScrewdriverList);
    }

    /**
     * Register a Soft Hammer to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerSoftHammer(ItemStack aTool) {
        return registerTool(aTool, sSoftHammerList);
    }

    /**
     * Register a Hard Hammer to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerHardHammer(ItemStack aTool) {
        return registerTool(aTool, sHardHammerList);
    }

    /**
     * Register a Wire Cutter to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerWireCutter(ItemStack aTool) {
        return registerTool(aTool, sWireCutterList);
    }

    /**
     * Register a Soldering Tool to interact with Machines
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerSolderingTool(ItemStack aTool) {
        return registerTool(aTool, sSolderingToolList);
    }

    /**
     * Register a Soldering Tin to interact with Soldering Tools
     * <p/>
     * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
     */
    public static boolean registerSolderingMetal(ItemStack aTool) {
        return registerTool(aTool, sSolderingMetalList);
    }

    /**
     * Generic Function to add Tools to the Lists.
     * Contains all sanity Checks for Tools, like preventing one Tool from being registered for multiple purposes as controls would override each other.
     */
    public static boolean registerTool(ItemStack aTool, Collection<GT_ItemStack> aToolList) {
        if (aTool == null || GT_Utility.isStackInList(aTool, sToolList) || (!aTool.getItem().isDamageable() && !GT_ModHandler.isElectricItem(aTool) && !(aTool.getItem() instanceof IDamagableItem)))
            return false;
        aToolList.add(new GT_ItemStack(GT_Utility.copyAmount(1, aTool)));
        sToolList.add(new GT_ItemStack(GT_Utility.copyAmount(1, aTool)));
        return true;
    }

    /**
     * Sets the {@link IIconRegister} for Block Icons
     * @param aIconRegister The {@link IIconRegister} Icon Register
     */
    @SideOnly(Side.CLIENT)
    public static void setBlockIconRegister(IIconRegister aIconRegister) {
        sBlockIcons = aIconRegister;
    };

    /**
     * Sets the {@link IIconRegister} for Items Icons
     * @param aIconRegister The {@link IIconRegister} Icon Register
     */
    @SideOnly(Side.CLIENT)
    public static void setItemIconRegister(IIconRegister aIconRegister) {
        GregTech_API.sItemIcons = aIconRegister;
    }

}
