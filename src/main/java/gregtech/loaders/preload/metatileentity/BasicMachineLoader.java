package gregtech.loaders.preload.metatileentity;


import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.common.tileentities.machines.basic.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.api.GregTech_API.sSoundList;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OreDictNames.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.X.*;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.*;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.DISMANTLEABLE_RECIPE_MASK;


/*
TIRED OF DOOMSCROLLING LOOKING FOR THE FUCKING RECIPES IN GREGTECH? CLICK HERE TO FIND THEM
 */
public final class BasicMachineLoader {

    private static boolean LOADED = false;

    public static void load() {
        if (LOADED) {
            throw new RuntimeException("Already loaded!");
        }
        Machine_LV_AlloySmelter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(201, "basicmachine.alloysmelter.tier.01", "Basic Alloy Smelter", 1, "HighTech combination Smelter",
                                                             sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", sSoundList.get(Integer.valueOf(208)),
                                                             false, false, 0, "ALLOY_SMELTER", new Object[]{
                        "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_LV), 'W', WIRE, 'C', COIL_HEATING_DOUBLE
                }
                ).getStackForm(1L));
        Machine_MV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(202, "basicmachine.alloysmelter.tier.02", "Advanced Alloy Smelter", 2,
                                                                                 "HighTech combination Smelter", sAlloySmelterRecipes, 2, 1, 0, 0, 1,
                                                                                 "AlloySmelter.png", sSoundList.get(Integer.valueOf(208)), false, false, 0,
                                                                                 "ALLOY_SMELTER", new Object[]{
                "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_MV), 'W', WIRE, 'C', COIL_HEATING_DOUBLE
        }
        ).getStackForm(1L));
        Machine_HV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(203, "basicmachine.alloysmelter.tier.03", "Advanced Alloy Smelter II", 3,
                                                                                 "HighTech combination Smelter", sAlloySmelterRecipes, 2, 1, 0, 0, 1,
                                                                                 "AlloySmelter.png", sSoundList.get(Integer.valueOf(208)), false, false, 0,
                                                                                 "ALLOY_SMELTER", new Object[]{
                "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_HV), 'W', WIRE, 'C', COIL_HEATING_DOUBLE
        }
        ).getStackForm(1L));
        Machine_EV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(204, "basicmachine.alloysmelter.tier.04", "Advanced Alloy Smelter III", 4,
                                                                                 "HighTech combination Smelter", sAlloySmelterRecipes, 2, 1, 0, 0, 1,
                                                                                 "AlloySmelter.png", sSoundList.get(Integer.valueOf(208)), false, false, 0,
                                                                                 "ALLOY_SMELTER", new Object[]{
                "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_EV), 'W', WIRE, 'C', COIL_HEATING_DOUBLE
        }
        ).getStackForm(1L));
        Machine_IV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(205, "basicmachine.alloysmelter.tier.05", "Advanced Alloy Smelter IV", 5,
                                                                                 "HighTech combination Smelter", sAlloySmelterRecipes, 2, 1, 0, 0, 1,
                                                                                 "AlloySmelter.png", sSoundList.get(Integer.valueOf(208)), false, false, 0,
                                                                                 "ALLOY_SMELTER", new Object[]{
                "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_IV), 'W', WIRE, 'C', COIL_HEATING_DOUBLE
        }
        ).getStackForm(1L));

        Machine_LV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(211, "basicmachine.assembler.tier.01", "Basic Assembling Machine", 1, "Avengers, Assemble!",
                                                             sAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false, false, 0, "ASSEMBLER",
                                                             new Object[]{
                                                                     "ACA", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C',
                                                                     circuitLogic.get(LOGIC_LV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_MV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(212, "basicmachine.assembler.tier.02", "Advanced Assembling Machine", 2, "Avengers, Assemble!",
                                                             sAssemblerRecipes, 9, 1, 24000, 0, 1, "Assembler2.png", "", false, false, 0, "ASSEMBLER",
                                                             new Object[]{
                                                                     "ACA", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C',
                                                                     circuitLogic.get(LOGIC_MV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_HV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(213, "basicmachine.assembler.tier.03", "Advanced Assembling Machine II", 3, "Avengers, Assemble!",
                                                             sAssemblerRecipes, 9, 1, 32000, 0, 1, "Assembler2.png", "", false, false, 0, "ASSEMBLER",
                                                             new Object[]{
                                                                     "ACA", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C',
                                                                     circuitLogic.get(LOGIC_HV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_EV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(214, "basicmachine.assembler.tier.04", "Advanced Assembling Machine III", 4, "Avengers, Assemble!",
                                                             sAssemblerRecipes, 9, 1, 48000, 0, 1, "Assembler2.png", "", false, false, 0, "ASSEMBLER",
                                                             new Object[]{
                                                                     "ACA", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C',
                                                                     circuitLogic.get(LOGIC_EV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_IV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(215, "basicmachine.assembler.tier.05", "Advanced Assembling Machine IV", 5, "Avengers, Assemble!",
                                                             sAssemblerRecipes, 9, 1, 64000, 0, 1, "Assembler2.png", "", false, false, 0, "ASSEMBLER",
                                                             new Object[]{
                                                                     "ACA", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C',
                                                                     circuitLogic.get(LOGIC_IV), 'W', WIRE
                                                             }
                ).getStackForm(1L));

        Machine_LV_Bender.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(221, "basicmachine.bender.tier.01", "Basic Bending Machine", 1, "Boo, he's bad! We want BENDER!!!",
                                                             sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", sSoundList.get(Integer.valueOf(203)), false, false, 0,
                                                             "BENDER", new Object[]{
                        "PwP", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_LV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_MV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(222, "basicmachine.bender.tier.02", "Advanced Bending Machine", 2,
                                                                           "Boo, he's bad! We want BENDER!!!", sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
                                                                           sSoundList.get(Integer.valueOf(203)), false, false, 0, "BENDER", new Object[]{
                "PwP", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_MV), 'W', WIRE
        }
        ).getStackForm(1L));
        Machine_HV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(223, "basicmachine.bender.tier.03", "Advanced Bending Machine II", 3,
                                                                           "Boo, he's bad! We want BENDER!!!", sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
                                                                           sSoundList.get(Integer.valueOf(203)), false, false, 0, "BENDER", new Object[]{
                "PwP", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_HV), 'W', WIRE
        }
        ).getStackForm(1L));
        Machine_EV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(224, "basicmachine.bender.tier.04", "Advanced Bending Machine III", 4,
                                                                           "Boo, he's bad! We want BENDER!!!", sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
                                                                           sSoundList.get(Integer.valueOf(203)), false, false, 0, "BENDER", new Object[]{
                "PwP", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_EV), 'W', WIRE
        }
        ).getStackForm(1L));
        Machine_IV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(225, "basicmachine.bender.tier.05", "Advanced Bending Machine IV", 5,
                                                                           "Boo, he's bad! We want BENDER!!!", sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
                                                                           sSoundList.get(Integer.valueOf(203)), false, false, 0, "BENDER", new Object[]{
                "PwP", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_IV), 'W', WIRE
        }
        ).getStackForm(1L));

        Machine_LV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(231, "basicmachine.canner.tier.01", "Basic Canning Machine", 1,
                                                                           "Unmobile Food Canning Machine GTA4", sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png",
                                                                           sSoundList.get(Integer.valueOf(200)), false, false, 0, "CANNER", new Object[]{
                "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_MV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(232, "basicmachine.canner.tier.02", "Advanced Canning Machine", 2,
                                                                           "Unmobile Food Canning Machine GTA4", sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png",
                                                                           sSoundList.get(Integer.valueOf(200)), false, false, 0, "CANNER", new Object[]{
                "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_HV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(233, "basicmachine.canner.tier.03", "Advanced Canning Machine II", 3,
                                                                           "Unmobile Food Canning Machine GTA4", sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png",
                                                                           sSoundList.get(Integer.valueOf(200)), false, false, 0, "CANNER", new Object[]{
                "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_EV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(234, "basicmachine.canner.tier.04", "Advanced Canning Machine III", 4,
                                                                           "Unmobile Food Canning Machine GTA4", sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png",
                                                                           sSoundList.get(Integer.valueOf(200)), false, false, 0, "CANNER", new Object[]{
                "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_IV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(235, "basicmachine.canner.tier.05", "Advanced Canning Machine IV", 5,
                                                                           "Unmobile Food Canning Machine GTA4", sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png",
                                                                           sSoundList.get(Integer.valueOf(200)), false, false, 0, "CANNER", new Object[]{
                "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));

        Machine_LV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(241, "basicmachine.compressor.tier.01", "Basic Compressor", 1, "Compress-O-Matic C77",
                                                             sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 0, "COMPRESSOR",
                                                             new Object[]{"WCW", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_LV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_MV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(242, "basicmachine.compressor.tier.02", "Advanced Compressor", 2, "Compress-O-Matic C77",
                                                             sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 0, "COMPRESSOR",
                                                             new Object[]{"WCW", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_MV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_HV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(243, "basicmachine.compressor.tier.03", "Advanced Compressor II", 3, "Compress-O-Matic C77",
                                                             sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 0, "COMPRESSOR",
                                                             new Object[]{"WCW", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_HV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_EV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(244, "basicmachine.compressor.tier.04", "Advanced Compressor III", 4, "Compress-O-Matic C77",
                                                             sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 0, "COMPRESSOR",
                                                             new Object[]{"WCW", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_EV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_IV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(245, "basicmachine.compressor.tier.05", "Singularity Compressor", 5, "Compress-O-Matic C77",
                                                             sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 0, "COMPRESSOR",
                                                             new Object[]{"WCW", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_IV), 'W', WIRE}
                ).getStackForm(1L));

        Machine_LV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(251, "basicmachine.cutter.tier.01", "Basic Cutting Machine", 1, "Slice'N Dice", sCutterRecipes, 1,
                                                             2, 8000, 0, 1, "Cutter.png", "", false, false, 0, "CUTTER", new Object[]{
                        "WCG", "VMB", "CWE", 'M', HULL, 'E', MOTOR, 'V', CONVEYOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS, 'B',
                        craftingDiamondBlade
                }
                ).getStackForm(1L));
        Machine_MV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(252, "basicmachine.cutter.tier.02", "Advanced Cutting Machine", 2, "Slice'N Dice", sCutterRecipes,
                                                             2, 2, 16000, 0, 1, "Cutter2.png", "", false, false, 0, "CUTTER", new Object[]{
                        "WCG", "VMB", "CWE", 'M', HULL, 'E', MOTOR, 'V', CONVEYOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS, 'B',
                        craftingDiamondBlade
                }
                ).getStackForm(1L));
        Machine_HV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(253, "basicmachine.cutter.tier.03", "Advanced Cutting Machine II", 3, "Slice'N Dice",
                                                                           sCutterRecipes, 2, 2, 32000, 0, 1, "Cutter2.png", "", false, false, 0, "CUTTER",
                                                                           new Object[]{
                                                                                   "WCG", "VMB", "CWE", 'M', HULL, 'E', MOTOR, 'V', CONVEYOR, 'C',
                                                                                   circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS, 'B', craftingDiamondBlade
                                                                           }
        ).getStackForm(1L));
        Machine_EV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(254, "basicmachine.cutter.tier.04", "Advanced Cutting Machine III", 4, "Slice'N Dice",
                                                             sCutterRecipes, 2, 2, 64000, 0, 1, "Cutter2.png", "", false, false, 0, "CUTTER", new Object[]{
                        "WCG", "VMB", "CWE", 'M', HULL, 'E', MOTOR, 'V', CONVEYOR, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS, 'B',
                        craftingDiamondBlade
                }
                ).getStackForm(1L));
        Machine_IV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(255, "basicmachine.cutter.tier.05", "Advanced Cutting Machine IV", 5, "Slice'N Dice",
                                                                           sCutterRecipes, 2, 2, 96000, 0, 1, "Cutter2.png", "", false, false, 0, "CUTTER",
                                                                           new Object[]{
                                                                                   "WCG", "VMB", "CWE", 'M', HULL, 'E', MOTOR, 'V', CONVEYOR, 'C',
                                                                                   circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS, 'B', craftingDiamondBlade
                                                                           }
        ).getStackForm(1L));

        Machine_LV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(261, "basicmachine.e_furnace.tier.01", "Basic Electric Furnace", 1,
                                                                              "Not like using a Commodore 64", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
                                                                              sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_FURNACE",
                                                                              new Object[]{
                                                                                      "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_LV), 'W', WIRE,
                                                                                      'C', COIL_HEATING
                                                                              }
        ).getStackForm(1L));
        Machine_MV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(262, "basicmachine.e_furnace.tier.02", "Advanced Electric Furnace", 2,
                                                                              "Not like using a Commodore 64", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
                                                                              sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_FURNACE",
                                                                              new Object[]{
                                                                                      "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_MV), 'W', WIRE,
                                                                                      'C', COIL_HEATING
                                                                              }
        ).getStackForm(1L));
        Machine_HV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(263, "basicmachine.e_furnace.tier.03", "Advanced Electric Furnace II", 3,
                                                                              "Not like using a Commodore 64", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
                                                                              sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_FURNACE",
                                                                              new Object[]{
                                                                                      "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_HV), 'W', WIRE,
                                                                                      'C', COIL_HEATING
                                                                              }
        ).getStackForm(1L));
        Machine_EV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(264, "basicmachine.e_furnace.tier.04", "Advanced Electric Furnace III", 4,
                                                                              "Not like using a Commodore 64", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
                                                                              sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_FURNACE",
                                                                              new Object[]{
                                                                                      "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_EV), 'W', WIRE,
                                                                                      'C', COIL_HEATING
                                                                              }
        ).getStackForm(1L));
        Machine_IV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(265, "basicmachine.e_furnace.tier.05", "Electron Exitement Processor", 5,
                                                                              "Not like using a Commodore 64", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
                                                                              sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_FURNACE",
                                                                              new Object[]{
                                                                                      "ECE", "CMC", "WCW", 'M', HULL, 'E', circuitPower.get(PWR_IV), 'W', WIRE,
                                                                                      'C', COIL_HEATING
                                                                              }
        ).getStackForm(1L));

        Machine_LV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(271, "basicmachine.extractor.tier.01", "Basic Extractor", 1, "Dejuicer-Device of Doom - D123",
                                                             sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "EXTRACTOR", new Object[]{
                        "GCG", "EMP", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
                }
                ).getStackForm(1L));
        Machine_MV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(272, "basicmachine.extractor.tier.02", "Advanced Extractor", 2, "Dejuicer-Device of Doom - D123",
                                                             sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "EXTRACTOR", new Object[]{
                        "GCG", "EMP", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
                }
                ).getStackForm(1L));
        Machine_HV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(273, "basicmachine.extractor.tier.03", "Advanced Extractor II", 3,
                                                                              "Dejuicer-Device of Doom - D123", sExtractorRecipes, 1, 1, 0, 0, 1,
                                                                              "Extractor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                              "EXTRACTOR", new Object[]{
                "GCG", "EMP", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_EV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(274, "basicmachine.extractor.tier.04", "Advanced Extractor III", 4,
                                                                              "Dejuicer-Device of Doom - D123", sExtractorRecipes, 1, 1, 0, 0, 1,
                                                                              "Extractor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                              "EXTRACTOR", new Object[]{
                "GCG", "EMP", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_IV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(275, "basicmachine.extractor.tier.05", "Vacuum Extractor", 5, "Dejuicer-Device of Doom - D123",
                                                             sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "EXTRACTOR", new Object[]{
                        "GCG", "EMP", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS
                }
                ).getStackForm(1L));

        Machine_LV_Extruder.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(281, "basicmachine.extruder.tier.01", "Basic Extruder", 1, "Universal Machine for Metal Working",
                                                             sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", sSoundList.get(Integer.valueOf(208)), false,
                                                             false, 0, "EXTRUDER", new Object[]{
                        "CCE", "XMP", "CCE", 'M', HULL, 'X', PISTON, 'E', circuitPower.get(PWR_LV), 'P', PIPE, 'C', COIL_HEATING_DOUBLE
                }
                ).getStackForm(1L));
        Machine_MV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(282, "basicmachine.extruder.tier.02", "Advanced Extruder", 2,
                                                                             "Universal Machine for Metal Working", sExtruderRecipes, 2, 1, 0, 0, 1,
                                                                             "Extruder.png", sSoundList.get(Integer.valueOf(208)), false, false, 0, "EXTRUDER",
                                                                             new Object[]{
                                                                                     "CCE", "XMP", "CCE", 'M', HULL, 'X', PISTON, 'E', circuitPower.get(PWR_MV),
                                                                                     'P', PIPE, 'C', COIL_HEATING_DOUBLE
                                                                             }
        ).getStackForm(1L));
        Machine_HV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(283, "basicmachine.extruder.tier.03", "Advanced Extruder II", 3,
                                                                             "Universal Machine for Metal Working", sExtruderRecipes, 2, 1, 0, 0, 1,
                                                                             "Extruder.png", sSoundList.get(Integer.valueOf(208)), false, false, 0, "EXTRUDER",
                                                                             new Object[]{
                                                                                     "CCE", "XMP", "CCE", 'M', HULL, 'X', PISTON, 'E', circuitPower.get(PWR_HV),
                                                                                     'P', PIPE, 'C', COIL_HEATING_DOUBLE
                                                                             }
        ).getStackForm(1L));
        Machine_EV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(284, "basicmachine.extruder.tier.04", "Advanced Extruder III", 4,
                                                                             "Universal Machine for Metal Working", sExtruderRecipes, 2, 1, 0, 0, 1,
                                                                             "Extruder.png", sSoundList.get(Integer.valueOf(208)), false, false, 0, "EXTRUDER",
                                                                             new Object[]{
                                                                                     "CCE", "XMP", "CCE", 'M', HULL, 'X', PISTON, 'E', circuitPower.get(PWR_EV),
                                                                                     'P', PIPE, 'C', COIL_HEATING_DOUBLE
                                                                             }
        ).getStackForm(1L));
        Machine_IV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(285, "basicmachine.extruder.tier.05", "Advanced Extruder IV", 5,
                                                                             "Universal Machine for Metal Working", sExtruderRecipes, 2, 1, 0, 0, 1,
                                                                             "Extruder.png", sSoundList.get(Integer.valueOf(208)), false, false, 0, "EXTRUDER",
                                                                             new Object[]{
                                                                                     "CCE", "XMP", "CCE", 'M', HULL, 'X', PISTON, 'E', circuitPower.get(PWR_IV),
                                                                                     'P', PIPE, 'C', COIL_HEATING_DOUBLE
                                                                             }
        ).getStackForm(1L));

        Machine_LV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(291, "basicmachine.lathe.tier.01", "Basic Lathe", 1, "Produces Rods more efficiently",
                                                                          sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE", new Object[]{
                "WCW", "EMD", "CWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'D', gem.get(Diamond)
        }
        ).getStackForm(1L));
        Machine_MV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(292, "basicmachine.lathe.tier.02", "Advanced Lathe", 2, "Produces Rods more efficiently",
                                                             sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE", new Object[]{
                        "WCW", "EMD", "CWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'D', gemFlawless.get(Diamond)
                }
                ).getStackForm(1L));
        Machine_HV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(293, "basicmachine.lathe.tier.03", "Advanced Lathe II", 3, "Produces Rods more efficiently",
                                                             sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE", new Object[]{
                        "WCW", "EMD", "CWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'D', craftingIndustrialDiamond
                }
                ).getStackForm(1L));
        Machine_EV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(294, "basicmachine.lathe.tier.04", "Advanced Lathe III", 4, "Produces Rods more efficiently",
                                                             sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE", new Object[]{
                        "WCW", "EMD", "CWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'D', craftingIndustrialDiamond
                }
                ).getStackForm(1L));
        Machine_IV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(295, "basicmachine.lathe.tier.05", "Advanced Lathe IV", 5, "Produces Rods more efficiently",
                                                             sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE", new Object[]{
                        "WCW", "EMD", "CWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'D', craftingIndustrialDiamond
                }
                ).getStackForm(1L));

        Machine_LV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(301, "basicmachine.macerator.tier.01", "Basic Macerator", 1, "[LV] Macerator", sMaceratorRecipes,
                                                             1, 1, 0, 0, 1, "Macerator1.png", sSoundList.get(Integer.valueOf(201)), false, false, 1,
                                                             "MACERATOR", new Object[]{
                        "PEG", "WWM", "CCW", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', gem.get(Diamond)
                }
                ).getStackForm(1L));
        Machine_MV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(302, "basicmachine.macerator.tier.02", "Advanced Macerator", 2, "[MV] Macerator",
                                                                              sMaceratorRecipes, 1, 2, 0, 0, 1, "Macerator2.png",
                                                                              sSoundList.get(Integer.valueOf(201)), false, false, 1, "MACERATOR", new Object[]{
                "PEG", "WWM", "CCW", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', gemFlawless.get(Diamond)
        }
        ).getStackForm(1L));
        Machine_HV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(303, "basicmachine.macerator.tier.03", "Advanced Macerator II", 3, "[HV] Macerator",
                                                             sMaceratorRecipes, 1, 3, 0, 0, 1, "Macerator3.png", sSoundList.get(Integer.valueOf(201)), false,
                                                             false, 1, "PULVERIZER", new Object[]{
                        "PEG", "WWM", "CCW", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', craftingGrinder
                }
                ).getStackForm(1L));
        Machine_EV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(304, "basicmachine.macerator.tier.04", "Advanced Macerator III", 4, "[EV] Macerator",
                                                             sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png", sSoundList.get(Integer.valueOf(201)), false,
                                                             false, 1, "PULVERIZER", new Object[]{
                        "PEG", "WWM", "CCW", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', craftingGrinder
                }
                ).getStackForm(1L));
        Machine_IV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(305, "basicmachine.macerator.tier.05", "Advanced Macerator IV", 5, "[IV] Macerator",
                                                             sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png", sSoundList.get(Integer.valueOf(201)), false,
                                                             false, 1, "PULVERIZER", new Object[]{
                        "PEG", "WWM", "CCW", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', craftingGrinder
                }
                ).getStackForm(1L));

        Machine_LV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(311, "basicmachine.microwave.tier.01", "Basic Microwave", 1,
                                                                              "Did you really read the instruction Manual?", sMicrowaveRecipes, 1, 1, 0, 0, 1,
                                                                              "E_Furnace.png", sSoundList.get(Integer.valueOf(207)), false, false, 0,
                                                                              "MICROWAVE", new Object[]{
                "LWC", "LMR", "LEC", 'M', HULL, 'E', MOTOR, 'R', EMITTER, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'L', plate.get(Lead)
        }
        ).getStackForm(1L));
        Machine_MV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(312, "basicmachine.microwave.tier.02", "Advanced Microwave", 2,
                                                                              "Did you really read the instruction Manual?", sMicrowaveRecipes, 1, 1, 0, 0, 1,
                                                                              "E_Furnace.png", sSoundList.get(Integer.valueOf(207)), false, false, 0,
                                                                              "MICROWAVE", new Object[]{
                "LWC", "LMR", "LEC", 'M', HULL, 'E', MOTOR, 'R', EMITTER, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'L', plate.get(Lead)
        }
        ).getStackForm(1L));
        Machine_HV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(313, "basicmachine.microwave.tier.03", "Advanced Microwave II", 3,
                                                                              "Did you really read the instruction Manual?", sMicrowaveRecipes, 1, 1, 0, 0, 1,
                                                                              "E_Furnace.png", sSoundList.get(Integer.valueOf(207)), false, false, 0,
                                                                              "MICROWAVE", new Object[]{
                "LWC", "LMR", "LEC", 'M', HULL, 'E', MOTOR, 'R', EMITTER, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'L', plate.get(Lead)
        }
        ).getStackForm(1L));
        Machine_EV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(314, "basicmachine.microwave.tier.04", "Advanced Microwave III", 4,
                                                                              "Did you really read the instruction Manual?", sMicrowaveRecipes, 1, 1, 0, 0, 1,
                                                                              "E_Furnace.png", sSoundList.get(Integer.valueOf(207)), false, false, 0,
                                                                              "MICROWAVE", new Object[]{
                "LWC", "LMR", "LEC", 'M', HULL, 'E', MOTOR, 'R', EMITTER, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'L', plate.get(Lead)
        }
        ).getStackForm(1L));
        Machine_IV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(315, "basicmachine.microwave.tier.05", "Advanced Microwave IV", 5,
                                                                              "Did you really read the instruction Manual?", sMicrowaveRecipes, 1, 1, 0, 0, 1,
                                                                              "E_Furnace.png", sSoundList.get(Integer.valueOf(207)), false, false, 0,
                                                                              "MICROWAVE", new Object[]{
                "LWC", "LMR", "LEC", 'M', HULL, 'E', MOTOR, 'R', EMITTER, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'L', plate.get(Lead)
        }
        ).getStackForm(1L));

        Machine_LV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(321, "basicmachine.printer.tier.01", "Basic Printer", 1, "It can copy Books and paint Stuff",
                                                             sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 1, "PRINTER", new Object[]{
                        "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_LV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_MV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(322, "basicmachine.printer.tier.02", "Advanced Printer", 2, "It can copy Books and paint Stuff",
                                                             sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 1, "PRINTER", new Object[]{
                        "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_MV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_HV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(323, "basicmachine.printer.tier.03", "Advanced Printer II", 3, "It can copy Books and paint Stuff",
                                                             sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 1, "PRINTER", new Object[]{
                        "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_HV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_EV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(324, "basicmachine.printer.tier.04", "Advanced Printer III", 4,
                                                                            "It can copy Books and paint Stuff", sPrinterRecipes, 1, 1, 16000, 0, 1,
                                                                            "Printer.png", sSoundList.get(Integer.valueOf(203)), false, false, 1, "PRINTER",
                                                                            new Object[]{
                                                                                    "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_EV),
                                                                                    'W', WIRE
                                                                            }
        ).getStackForm(1L));
        Machine_IV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(325, "basicmachine.printer.tier.05", "Advanced Printer IV", 5, "It can copy Books and paint Stuff",
                                                             sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 1, "PRINTER", new Object[]{
                        "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_IV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_LuV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(326, "basicmachine.printer.tier.06", "Advanced Printer V", 6, "It can copy Books and paint Stuff",
                                                             sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 1, "PRINTER", new Object[]{
                        "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_LUV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_ZPM_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(327, "basicmachine.printer.tier.07", "Advanced Printer VI", 7, "It can copy Books and paint Stuff",
                                                             sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", sSoundList.get(Integer.valueOf(203)), false,
                                                             false, 1, "PRINTER", new Object[]{
                        "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_ZPM), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_UV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(328, "basicmachine.printer.tier.08", "Advanced Printer VII", 8,
                                                                            "It can copy Books and paint Stuff", sPrinterRecipes, 1, 1, 16000, 0, 1,
                                                                            "Printer.png", sSoundList.get(Integer.valueOf(203)), false, false, 1, "PRINTER",
                                                                            new Object[]{
                                                                                    "EWE", "CMC", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_UV),
                                                                                    'W', WIRE
                                                                            }
        ).getStackForm(1L));

        Machine_LV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(331, "basicmachine.recycler.tier.01", "Basic Recycler", 1,
                                                                             "Compress, burn, obliterate and filter EVERYTHING", sRecyclerRecipes, 1, 1, 0, 0,
                                                                             1, "Recycler.png", sSoundList.get(Integer.valueOf(204)), false, false, 0,
                                                                             "RECYCLER", new Object[]{
                "GCG", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', dust.get(Glowstone)
        }
        ).getStackForm(1L));
        Machine_MV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(332, "basicmachine.recycler.tier.02", "Advanced Recycler", 2,
                                                                             "Compress, burn, obliterate and filter EVERYTHING", sRecyclerRecipes, 1, 1, 0, 0,
                                                                             1, "Recycler.png", sSoundList.get(Integer.valueOf(204)), false, false, 0,
                                                                             "RECYCLER", new Object[]{
                "GCG", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', dust.get(Glowstone)
        }
        ).getStackForm(1L));
        Machine_HV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(333, "basicmachine.recycler.tier.03", "Advanced Recycler II", 3,
                                                                             "Compress, burn, obliterate and filter EVERYTHING", sRecyclerRecipes, 1, 1, 0, 0,
                                                                             1, "Recycler.png", sSoundList.get(Integer.valueOf(204)), false, false, 0,
                                                                             "RECYCLER", new Object[]{
                "GCG", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', dust.get(Glowstone)
        }
        ).getStackForm(1L));
        Machine_EV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(334, "basicmachine.recycler.tier.04", "Advanced Recycler III", 4,
                                                                             "Compress, burn, obliterate and filter EVERYTHING", sRecyclerRecipes, 1, 1, 0, 0,
                                                                             1, "Recycler.png", sSoundList.get(Integer.valueOf(204)), false, false, 0,
                                                                             "RECYCLER", new Object[]{
                "GCG", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', dust.get(Glowstone)
        }
        ).getStackForm(1L));
        Machine_IV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(335, "basicmachine.recycler.tier.05", "Advanced Recycler IV", 5,
                                                                             "Compress, burn, obliterate and filter EVERYTHING", sRecyclerRecipes, 1, 1, 0, 0,
                                                                             1, "Recycler.png", sSoundList.get(Integer.valueOf(204)), false, false, 0,
                                                                             "RECYCLER", new Object[]{
                "GCG", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', dust.get(Glowstone)
        }
        ).getStackForm(1L));

        Machine_LV_Scanner.set(new GT_MetaTileEntity_Scanner(341, "basicmachine.scanner.tier.01", "Basic Scanner", 1).getStackForm(1L));
        Machine_MV_Scanner.set(new GT_MetaTileEntity_Scanner(342, "basicmachine.scanner.tier.02", "Advanced Scanner", 2).getStackForm(1L));
        Machine_HV_Scanner.set(new GT_MetaTileEntity_Scanner(343, "basicmachine.scanner.tier.03", "Advanced Scanner II", 3).getStackForm(1L));
        Machine_EV_Scanner.set(new GT_MetaTileEntity_Scanner(344, "basicmachine.scanner.tier.04", "Advanced Scanner III", 4).getStackForm(1L));
        Machine_IV_Scanner.set(new GT_MetaTileEntity_Scanner(345, "basicmachine.scanner.tier.05", "Advanced Scanner IV", 5).getStackForm(1L));

        addCraftingRecipe(
                Machine_LV_Scanner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CTC", "WMW", "CRC", 'M', Hull_LV, 'T', Emitter_LV, 'R', Sensor_LV, 'C', circuitLogic.get(LOGIC_LV), 'W', cableGt01.get(Tin)
                });
        addCraftingRecipe(
                Machine_MV_Scanner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CTC", "WMW", "CRC", 'M', Hull_MV, 'T', Emitter_MV, 'R', Sensor_MV, 'C', circuitLogic.get(LOGIC_MV), 'W', cableGt01.get(AnyCopper)
                });
        addCraftingRecipe(
                Machine_HV_Scanner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CTC", "WMW", "CRC", 'M', Hull_HV, 'T', Emitter_HV, 'R', Sensor_HV, 'C', circuitLogic.get(LOGIC_HV), 'W', cableGt01.get(Gold)
                });
        addCraftingRecipe(
                Machine_EV_Scanner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CTC", "WMW", "CRC", 'M', Hull_EV, 'T', Emitter_EV, 'R', Sensor_EV, 'C', circuitLogic.get(LOGIC_EV), 'W', cableGt01.get(Aluminium)
                });
        addCraftingRecipe(
                Machine_IV_Scanner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CTC", "WMW", "CRC", 'M', Hull_IV, 'T', Emitter_IV, 'R', Sensor_IV, 'C', circuitLogic.get(LOGIC_IV), 'W', cableGt01.get(Tungsten)
                });

        Machine_LV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(351, "basicmachine.wiremill.tier.01", "Basic Wiremill", 1, "Produces Wires more efficiently",
                                                             sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", sSoundList.get(Integer.valueOf(204)), false,
                                                             false, 0, "WIREMILL",
                                                             new Object[]{"EWE", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_MV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(352, "basicmachine.wiremill.tier.02", "Advanced Wiremill", 2, "Produces Wires more efficiently",
                                                             sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", sSoundList.get(Integer.valueOf(204)), false,
                                                             false, 0, "WIREMILL",
                                                             new Object[]{"EWE", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_HV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(353, "basicmachine.wiremill.tier.03", "Advanced Wiremill II", 3, "Produces Wires more efficiently",
                                                             sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", sSoundList.get(Integer.valueOf(204)), false,
                                                             false, 0, "WIREMILL",
                                                             new Object[]{"EWE", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_HV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_EV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(354, "basicmachine.wiremill.tier.04", "Advanced Wiremill III", 4,
                                                                             "Produces Wires more efficiently", sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png",
                                                                             sSoundList.get(Integer.valueOf(204)), false, false, 0, "WIREMILL", new Object[]{
                "EWE", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_EV), 'W', WIRE
        }
        ).getStackForm(1L));
        Machine_IV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(355, "basicmachine.wiremill.tier.05", "Advanced Wiremill IV", 5, "Produces Wires more efficiently",
                                                             sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", sSoundList.get(Integer.valueOf(204)), false,
                                                             false, 0, "WIREMILL",
                                                             new Object[]{"EWE", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_IV), 'W', WIRE}
                ).getStackForm(1L));

        Machine_LV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(361, "basicmachine.centrifuge.tier.01", "Basic Centrifuge", 1, "Separating Molecules",
                                                             sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", false, false, 0, "CENTRIFUGE",
                                                             new Object[]{
                                                                     "CEC", "WMW", "CEC", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_LV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_MV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(362, "basicmachine.centrifuge.tier.02", "Advanced Centrifuge", 2, "Separating Molecules",
                                                             sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", false, false, 0, "CENTRIFUGE",
                                                             new Object[]{
                                                                     "CEC", "WMW", "CEC", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_MV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_HV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(363, "basicmachine.centrifuge.tier.03", "Advanced Centrifuge II", 3, "Separating Molecules",
                                                             sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", false, false, 0, "CENTRIFUGE",
                                                             new Object[]{
                                                                     "CEC", "WMW", "CEC", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_HV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_EV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(364, "basicmachine.centrifuge.tier.04", "Advanced Centrifuge III", 4, "Separating Molecules",
                                                             sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", false, false, 0, "CENTRIFUGE",
                                                             new Object[]{
                                                                     "CEC", "WMW", "CEC", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_EV), 'W', WIRE
                                                             }
                ).getStackForm(1L));
        Machine_IV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(365, "basicmachine.centrifuge.tier.05", "Advanced Centrifuge IV", 5, "Separating Molecules",
                                                             sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", false, false, 0, "CENTRIFUGE",
                                                             new Object[]{
                                                                     "CEC", "WMW", "CEC", 'M', HULL, 'E', MOTOR, 'C', circuitLogic.get(LOGIC_IV), 'W', WIRE
                                                             }
                ).getStackForm(1L));

        Machine_LV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(371, "basicmachine.electrolyzer.tier.01", "Basic Electrolyzer", 1, "Electrolyzing Molecules",
                                                             sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", false, false, 0, "ELECTROLYZER",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CWC", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'I',
                                                                     wireGt01.get(Gold), 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_MV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(372, "basicmachine.electrolyzer.tier.02", "Advanced Electrolyzer", 2, "Electrolyzing Molecules",
                                                             sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", false, false, 0, "ELECTROLYZER",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CWC", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'I',
                                                                     wireGt01.get(Silver), 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_HV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(373, "basicmachine.electrolyzer.tier.03", "Advanced Electrolyzer II", 3, "Electrolyzing Molecules",
                                                             sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", false, false, 0, "ELECTROLYZER",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CWC", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'I',
                                                                     wireGt01.get(Electrum), 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_EV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(374, "basicmachine.electrolyzer.tier.04", "Advanced Electrolyzer III", 4,
                                                                                 "Electrolyzing Molecules", sElectrolyzerRecipes, 2, 6, 64000, 0, 1,
                                                                                 "Electrolyzer.png", "", false, false, 0, "ELECTROLYZER", new Object[]{
                "IGI", "IMI", "CWC", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'I', wireGt01.get(Platinum), 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_IV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(375, "basicmachine.electrolyzer.tier.05", "Advanced Electrolyzer IV", 5, "Electrolyzing Molecules",
                                                             sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", false, false, 0, "ELECTROLYZER",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CWC", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'I',
                                                                     wireGt01.get(HSSG), 'G', GLASS
                                                             }
                ).getStackForm(1L));

        Machine_LV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(381, "basicmachine.thermalcentrifuge.tier.01", "Basic Thermal Centrifuge", 1,
                                                             "Separating Ores more precisely", sThermalCentrifugeRecipes, 1, 3, 0, 0, 1,
                                                             "ThermalCentrifuge.png", "", false, false, 0, "THERMAL_CENTRIFUGE", new Object[]{
                        "CEC", "OMO", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE
                }
                ).getStackForm(1L));
        Machine_MV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(382, "basicmachine.thermalcentrifuge.tier.02", "Advanced Thermal Centrifuge", 2,
                                                             "Separating Ores more precisely", sThermalCentrifugeRecipes, 1, 3, 0, 0, 1,
                                                             "ThermalCentrifuge.png", "", false, false, 0, "THERMAL_CENTRIFUGE", new Object[]{
                        "CEC", "OMO", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE
                }
                ).getStackForm(1L));
        Machine_HV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(383, "basicmachine.thermalcentrifuge.tier.03", "Advanced Thermal Centrifuge II", 3,
                                                             "Separating Ores more precisely", sThermalCentrifugeRecipes, 1, 3, 0, 0, 1,
                                                             "ThermalCentrifuge.png", "", false, false, 0, "THERMAL_CENTRIFUGE", new Object[]{
                        "CEC", "OMO", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE
                }
                ).getStackForm(1L));
        Machine_EV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(384, "basicmachine.thermalcentrifuge.tier.04", "Advanced Thermal Centrifuge III", 4,
                                                             "Separating Ores more precisely", sThermalCentrifugeRecipes, 1, 3, 0, 0, 1,
                                                             "ThermalCentrifuge.png", "", false, false, 0, "THERMAL_CENTRIFUGE", new Object[]{
                        "CEC", "OMO", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE
                }
                ).getStackForm(1L));
        Machine_IV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(385, "basicmachine.thermalcentrifuge.tier.05", "Advanced Thermal Centrifuge IV", 5,
                                                             "Separating Ores more precisely", sThermalCentrifugeRecipes, 1, 3, 0, 0, 1,
                                                             "ThermalCentrifuge.png", "", false, false, 0, "THERMAL_CENTRIFUGE", new Object[]{
                        "CEC", "OMO", "WEW", 'M', HULL, 'E', MOTOR, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE
                }
                ).getStackForm(1L));

        Machine_LV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(391, "basicmachine.orewasher.tier.01", "Basic Ore Washer", 1,
                                                                              "Getting more Byproducts from your Ores", sOreWasherRecipes, 1, 3, 16000, 0, 1,
                                                                              "OreWasher.png", "", false, false, 0, "ORE_WASHER", new Object[]{
                "RGR", "CEC", "WMW", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', PUMP
        }
        ).getStackForm(1L));
        Machine_MV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(392, "basicmachine.orewasher.tier.02", "Advanced Ore Washer", 2,
                                                                              "Getting more Byproducts from your Ores", sOreWasherRecipes, 1, 3, 24000, 0, 1,
                                                                              "OreWasher.png", "", false, false, 0, "ORE_WASHER", new Object[]{
                "RGR", "CEC", "WMW", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', PUMP
        }
        ).getStackForm(1L));
        Machine_HV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(393, "basicmachine.orewasher.tier.03", "Advanced Ore Washer II", 3,
                                                                              "Getting more Byproducts from your Ores", sOreWasherRecipes, 1, 3, 32000, 0, 1,
                                                                              "OreWasher.png", "", false, false, 0, "ORE_WASHER", new Object[]{
                "RGR", "CEC", "WMW", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', PUMP
        }
        ).getStackForm(1L));
        Machine_EV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(394, "basicmachine.orewasher.tier.04", "Advanced Ore Washer III", 4,
                                                                              "Getting more Byproducts from your Ores", sOreWasherRecipes, 1, 3, 40000, 0, 1,
                                                                              "OreWasher.png", "", false, false, 0, "ORE_WASHER", new Object[]{
                "RGR", "CEC", "WMW", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', PUMP
        }
        ).getStackForm(1L));
        Machine_IV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(395, "basicmachine.orewasher.tier.05", "Advanced Ore Washer IV", 5,
                                                                              "Getting more Byproducts from your Ores", sOreWasherRecipes, 1, 3, 48000, 0, 1,
                                                                              "OreWasher.png", "", false, false, 0, "ORE_WASHER", new Object[]{
                "RGR", "CEC", "WMW", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', PUMP
        }
        ).getStackForm(1L));

        Machine_LV_Boxinator.set(new GT_MetaTileEntity_Boxinator(401, "basicmachine.boxinator.tier.01", "Basic Packager", 1).getStackForm(1L));
        Machine_MV_Boxinator.set(new GT_MetaTileEntity_Boxinator(402, "basicmachine.boxinator.tier.02", "Advanced Packager", 2).getStackForm(1L));
        Machine_HV_Boxinator.set(new GT_MetaTileEntity_Boxinator(403, "basicmachine.boxinator.tier.03", "Advanced Packager II", 3).getStackForm(1L));
        Machine_EV_Boxinator.set(new GT_MetaTileEntity_Boxinator(404, "basicmachine.boxinator.tier.04", "Advanced Packager III", 4).getStackForm(1L));
        Machine_IV_Boxinator.set(new GT_MetaTileEntity_Boxinator(405, "basicmachine.boxinator.tier.05", "Advanced Packager IV", 5).getStackForm(1L));
        Machine_LuV_Boxinator.set(new GT_MetaTileEntity_Boxinator(406, "basicmachine.boxinator.tier.06", "Advanced Packager V", 6).getStackForm(1L));
        Machine_ZPM_Boxinator.set(new GT_MetaTileEntity_Boxinator(407, "basicmachine.boxinator.tier.07", "Advanced Packager VI", 7).getStackForm(1L));
        Machine_UV_Boxinator.set(new GT_MetaTileEntity_Boxinator(408, "basicmachine.boxinator.tier.08", "Advanced Packager VII", 8).getStackForm(1L));

        addCraftingRecipe(
                Machine_LV_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_LV, 'R', Robot_Arm_LV, 'V', Conveyor_Module_LV, 'C', circuitPower.get(PWR_LV), 'W', cableGt01.get(Tin),
                        'B', craftingChest
                });
        addCraftingRecipe(
                Machine_MV_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_MV, 'R', Robot_Arm_MV, 'V', Conveyor_Module_MV, 'C', circuitPower.get(PWR_MV), 'W',
                        cableGt01.get(AnyCopper), 'B', craftingChest
                });
        addCraftingRecipe(
                Machine_HV_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_HV, 'R', Robot_Arm_HV, 'V', Conveyor_Module_HV, 'C', circuitPower.get(PWR_HV), 'W', cableGt01.get(Gold),
                        'B', craftingChest
                });
        addCraftingRecipe(
                Machine_EV_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_EV, 'R', Robot_Arm_EV, 'V', Conveyor_Module_EV, 'C', circuitPower.get(PWR_EV), 'W',
                        cableGt01.get(Aluminium), 'B', craftingChest
                });
        addCraftingRecipe(
                Machine_IV_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_IV, 'R', Robot_Arm_IV, 'V', Conveyor_Module_IV, 'C', circuitPower.get(PWR_IV), 'W',
                        cableGt01.get(Tungsten), 'B', craftingChest
                });
        addCraftingRecipe(
                Machine_LuV_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_LuV, 'R', Robot_Arm_LuV, 'V', Conveyor_Module_LuV, 'C', circuitPower.get(PWR_LUV), 'W',
                        cableGt01.get(VanadiumGallium), 'B', craftingChest
                });
        addCraftingRecipe(
                Machine_ZPM_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_ZPM, 'R', Robot_Arm_ZPM, 'V', Conveyor_Module_ZPM, 'C', circuitPower.get(PWR_ZPM), 'W',
                        cableGt01.get(Naquadah), 'B', craftingChest
                });
        addCraftingRecipe(
                Machine_UV_Boxinator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "BCB", "RMV", "WCW", 'M', Hull_UV, 'R', Robot_Arm_UV, 'V', Conveyor_Module_UV, 'C', circuitPower.get(PWR_UV), 'W',
                        cableGt01.get(NaquadahAlloy), 'B', craftingChest
                });

        Machine_LV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(411, "basicmachine.unboxinator.tier.01", "Basic Unpackager", 1, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_LV), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));
        Machine_MV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(412, "basicmachine.unboxinator.tier.02", "Advanced Unpackager", 2, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_MV), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));
        Machine_HV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(413, "basicmachine.unboxinator.tier.03", "Advanced Unpackager II", 3, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_HV), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));
        Machine_EV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(414, "basicmachine.unboxinator.tier.04", "Advanced Unpackager III", 4, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_EV), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));
        Machine_IV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(415, "basicmachine.unboxinator.tier.05", "Advanced Unpackager IV", 5, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_IV), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));
        Machine_LuV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(416, "basicmachine.unboxinator.tier.06", "Advanced Unpackager V", 6, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_LUV), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));
        Machine_ZPM_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(417, "basicmachine.unboxinator.tier.07", "Advanced Unpackager VI", 7, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_ZPM), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));
        Machine_UV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(418, "basicmachine.unboxinator.tier.08", "Advanced Unpackager VII", 8, "Grabs things out of Boxes",
                                                             sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", false, false, 0, "UNBOXINATOR",
                                                             new Object[]{
                                                                     "BCB", "VMR", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V', CONVEYOR, 'C',
                                                                     circuitPower.get(PWR_UV), 'W', WIRE, 'B', craftingChest
                                                             }
                ).getStackForm(1L));

        Machine_LV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(421, "basicmachine.chemicalreactor.tier.01", "Basic Chemical Reactor", 1,
                                                                                    "Letting Chemicals react with each other", sChemicalRecipes, 2, 2, 16000, 0,
                                                                                    1, "ChemicalReactor.png", sSoundList.get(Integer.valueOf(200)), false,
                                                                                    false, 0, "CHEMICAL_REACTOR", new Object[]{
                "GRG", "WEW", "CMC", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_MV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(422, "basicmachine.chemicalreactor.tier.02", "Advanced Chemical Reactor", 2,
                                                                                    "Letting Chemicals react with each other", sChemicalRecipes, 2, 2, 16000, 0,
                                                                                    1, "ChemicalReactor.png", sSoundList.get(Integer.valueOf(200)), false,
                                                                                    false, 0, "CHEMICAL_REACTOR", new Object[]{
                "GRG", "WEW", "CMC", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_HV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(423, "basicmachine.chemicalreactor.tier.03", "Advanced Chemical Reactor II", 3,
                                                             "Letting Chemicals react with each other", sChemicalRecipes, 2, 2, 16000, 0, 1,
                                                             "ChemicalReactor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0, "CHEMICAL_REACTOR",
                                                             new Object[]{
                                                                     "GRG", "WEW", "CMC", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_HV), 'W',
                                                                     WIRE, 'G', pipeMedium.get(Plastic)
                                                             }
                ).getStackForm(1L));
        Machine_EV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(424, "basicmachine.chemicalreactor.tier.04", "Advanced Chemical Reactor III", 4,
                                                             "Letting Chemicals react with each other", sChemicalRecipes, 2, 2, 16000, 0, 1,
                                                             "ChemicalReactor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0, "CHEMICAL_REACTOR",
                                                             new Object[]{
                                                                     "GRG", "WEW", "CMC", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_EV), 'W',
                                                                     WIRE, 'G', pipeLarge.get(Plastic)
                                                             }
                ).getStackForm(1L));
        Machine_IV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(425, "basicmachine.chemicalreactor.tier.05", "Advanced Chemical Reactor IV", 5,
                                                             "Letting Chemicals react with each other", sChemicalRecipes, 2, 2, 16000, 0, 1,
                                                             "ChemicalReactor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0, "CHEMICAL_REACTOR",
                                                             new Object[]{
                                                                     "GRG", "WEW", "CMC", 'M', HULL, 'R', ROTOR, 'E', MOTOR, 'C', circuitPower.get(PWR_IV), 'W',
                                                                     WIRE, 'G', pipeHuge.get(Plastic)
                                                             }
                ).getStackForm(1L));

        Machine_LV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(431, "basicmachine.fluidcanner.tier.01", "Basic Fluid Canner", 1,
                                                                                "Puts Fluids into and out of Containers", sFluidCannerRecipes, 1, 1, 16000, 0,
                                                                                1, "FluidCanner.png", "", true, false, 0, "FLUID_CANNER", new Object[]{
                "GCG", "GMG", "WPW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_MV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(432, "basicmachine.fluidcanner.tier.02", "Advanced Fluid Canner", 2,
                                                                                "Puts Fluids into and out of Containers", sFluidCannerRecipes, 1, 1, 32000, 0,
                                                                                1, "FluidCanner.png", "", true, false, 0, "FLUID_CANNER", new Object[]{
                "GCG", "GMG", "WPW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_HV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(433, "basicmachine.fluidcanner.tier.03", "Advanced Fluid Canner II", 3,
                                                                                "Puts Fluids into and out of Containers", sFluidCannerRecipes, 1, 1, 48000, 0,
                                                                                1, "FluidCanner.png", "", true, false, 0, "FLUID_CANNER", new Object[]{
                "GCG", "GMG", "WPW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_EV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(434, "basicmachine.fluidcanner.tier.04", "Advanced Fluid Canner III", 4,
                                                                                "Puts Fluids into and out of Containers", sFluidCannerRecipes, 1, 1, 64000, 0,
                                                                                1, "FluidCanner.png", "", true, false, 0, "FLUID_CANNER", new Object[]{
                "GCG", "GMG", "WPW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_IV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(435, "basicmachine.fluidcanner.tier.05", "Advanced Fluid Canner IV", 5,
                                                                                "Puts Fluids into and out of Containers", sFluidCannerRecipes, 1, 1, 80000, 0,
                                                                                1, "FluidCanner.png", "", true, false, 0, "FLUID_CANNER", new Object[]{
                "GCG", "GMG", "WPW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));

        Machine_LV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(441, "basicmachine.rockbreaker.tier.01", "Basic Rock Breaker", 1).getStackForm(1L));
        Machine_MV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(442, "basicmachine.rockbreaker.tier.02", "Advanced Rock Breaker", 2).getStackForm(1L));
        Machine_HV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(443, "basicmachine.rockbreaker.tier.03", "Advanced Rock Breaker II", 3).getStackForm(1L));
        Machine_EV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(444, "basicmachine.rockbreaker.tier.04", "Advanced Rock Breaker III", 4).getStackForm(1L));
        Machine_IV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(445, "basicmachine.rockbreaker.tier.05", "Advanced Rock Breaker IV", 5).getStackForm(1L));

        addCraftingRecipe(
                Machine_LV_RockBreaker.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "PED", "WMW", "GGG", 'M', Hull_LV, 'D', craftingGrinder, 'E', Electric_Motor_LV, 'P', Electric_Piston_LV, 'C', circuitPower.get(PWR_LV),
                        'W', cableGt01.get(Tin), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_MV_RockBreaker.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "PED", "WMW", "GGG", 'M', Hull_MV, 'D', craftingGrinder, 'E', Electric_Motor_MV, 'P', Electric_Piston_MV, 'C', circuitPower.get(PWR_MV),
                        'W', cableGt01.get(AnyCopper), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_HV_RockBreaker.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "PED", "WMW", "GGG", 'M', Hull_HV, 'D', craftingGrinder, 'E', Electric_Motor_HV, 'P', Electric_Piston_HV, 'C', circuitPower.get(PWR_HV),
                        'W', cableGt01.get(Gold), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_EV_RockBreaker.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "PED", "WMW", "GGG", 'M', Hull_EV, 'D', craftingGrinder, 'E', Electric_Motor_EV, 'P', Electric_Piston_EV, 'C', circuitPower.get(PWR_EV),
                        'W', cableGt01.get(Aluminium), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_IV_RockBreaker.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "PED", "WMW", "GGG", 'M', Hull_IV, 'D', craftingGrinder, 'E', Electric_Motor_IV, 'P', Electric_Piston_IV, 'C', circuitPower.get(PWR_IV),
                        'W', cableGt01.get(Tungsten), 'G', new ItemStack(Blocks.glass, 1)
                });

        Machine_LV_Disassembler.set(new GT_MetaTileEntity_Disassembler(451, "basicmachine.disassembler.tier.01", "Basic Disassembler", 1).getStackForm(1L));
        Machine_MV_Disassembler.set(new GT_MetaTileEntity_Disassembler(452, "basicmachine.disassembler.tier.02", "Advanced Disassembler", 2).getStackForm(1L));
        Machine_HV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(453, "basicmachine.disassembler.tier.03", "Advanced Disassembler II", 3).getStackForm(1L));
        Machine_EV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(454, "basicmachine.disassembler.tier.04", "Advanced Disassembler III", 4).getStackForm(1L));
        Machine_IV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(455, "basicmachine.disassembler.tier.05", "Advanced Disassembler IV", 5).getStackForm(1L));

        addCraftingRecipe(
                Machine_LV_Disassembler.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"ACA", "WMW", "ACA", 'M', Hull_LV, 'A', Robot_Arm_LV, 'C', circuitLogic.get(LOGIC_LV), 'W', cableGt01.get(Tin)}
                         );
        addCraftingRecipe(
                Machine_MV_Disassembler.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"ACA", "WMW", "ACA", 'M', Hull_MV, 'A', Robot_Arm_MV, 'C', circuitLogic.get(LOGIC_MV), 'W', cableGt01.get(AnyCopper)}
                         );
        addCraftingRecipe(
                Machine_HV_Disassembler.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"ACA", "WMW", "ACA", 'M', Hull_HV, 'A', Robot_Arm_HV, 'C', circuitLogic.get(LOGIC_HV), 'W', cableGt01.get(Gold)}
                         );
        addCraftingRecipe(
                Machine_EV_Disassembler.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"ACA", "WMW", "ACA", 'M', Hull_EV, 'A', Robot_Arm_EV, 'C', circuitLogic.get(LOGIC_EV), 'W', cableGt01.get(Aluminium)}
                         );
        addCraftingRecipe(
                Machine_IV_Disassembler.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"ACA", "WMW", "ACA", 'M', Hull_IV, 'A', Robot_Arm_IV, 'C', circuitLogic.get(LOGIC_IV), 'W', cableGt01.get(Tungsten)}
                         );

        Machine_LV_Massfab.set(new GT_MetaTileEntity_Massfabricator(461, "basicmachine.massfab.tier.01", "Basic Mass Fabricator", 1).getStackForm(1L));
        Machine_MV_Massfab.set(new GT_MetaTileEntity_Massfabricator(462, "basicmachine.massfab.tier.02", "Advanced Mass Fabricator", 2).getStackForm(1L));
        Machine_HV_Massfab.set(new GT_MetaTileEntity_Massfabricator(463, "basicmachine.massfab.tier.03", "Advanced Mass Fabricator II", 3).getStackForm(1L));
        Machine_EV_Massfab.set(new GT_MetaTileEntity_Massfabricator(464, "basicmachine.massfab.tier.04", "Advanced Mass Fabricator III", 4).getStackForm(1L));
        Machine_IV_Massfab.set(new GT_MetaTileEntity_Massfabricator(465, "basicmachine.massfab.tier.05", "Advanced Mass Fabricator IV", 5).getStackForm(1L));

        addCraftingRecipe(
                Machine_LV_Massfab.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"CFC", "WMW", "CFC", 'M', Hull_LV, 'F', Field_Generator_LV, 'C', circuitLogic.get(LOGIC_MV), 'W', cableGt04.get(Tin)}
                         );
        addCraftingRecipe(
                Machine_MV_Massfab.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CFC", "WMW", "CFC", 'M', Hull_MV, 'F', Field_Generator_MV, 'C', circuitLogic.get(LOGIC_HV), 'W', cableGt04.get(AnyCopper)
                });
        addCraftingRecipe(
                Machine_HV_Massfab.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"CFC", "WMW", "CFC", 'M', Hull_HV, 'F', Field_Generator_HV, 'C', circuitLogic.get(LOGIC_EV), 'W', cableGt04.get(Gold)}
                         );
        addCraftingRecipe(
                Machine_EV_Massfab.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CFC", "WMW", "CFC", 'M', Hull_EV, 'F', Field_Generator_EV, 'C', circuitLogic.get(LOGIC_IV), 'W', cableGt04.get(Aluminium)
                });
        addCraftingRecipe(
                Machine_IV_Massfab.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CFC", "WMW", "CFC", 'M', Hull_IV, 'F', Field_Generator_IV, 'C', circuitLogic.get(LOGIC_LUV), 'W', cableGt04.get(Tungsten)
                });

        Machine_LV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(471, "basicmachine.amplifab.tier.01", "Basic Amplifabricator", 1, "Extracting UU Amplifier",
                                                             sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "AMPLIFAB",
                                                             new Object[]{"WPW", "PMP", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_MV), 'W', WIRE4}
                ).getStackForm(1L));
        Machine_MV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(472, "basicmachine.amplifab.tier.02", "Advanced Amplifabricator", 2, "Extracting UU Amplifier",
                                                             sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "AMPLIFAB",
                                                             new Object[]{"WPW", "PMP", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE4}
                ).getStackForm(1L));
        Machine_HV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(473, "basicmachine.amplifab.tier.03", "Advanced Amplifabricator II", 3, "Extracting UU Amplifier",
                                                             sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "AMPLIFAB",
                                                             new Object[]{"WPW", "PMP", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE4}
                ).getStackForm(1L));
        Machine_EV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(474, "basicmachine.amplifab.tier.04", "Advanced Amplifabricator III", 4, "Extracting UU Amplifier",
                                                             sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "AMPLIFAB",
                                                             new Object[]{"WPW", "PMP", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE4}
                ).getStackForm(1L));
        Machine_IV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(475, "basicmachine.amplifab.tier.05", "Advanced Amplifabricator IV", 5, "Extracting UU Amplifier",
                                                             sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", sSoundList.get(Integer.valueOf(200)), false,
                                                             false, 0, "AMPLIFAB",
                                                             new Object[]{"WPW", "PMP", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_LUV), 'W', WIRE4}
                ).getStackForm(1L));

        Machine_LV_Replicator.set(new GT_MetaTileEntity_Replicator(481, "basicmachine.replicator.tier.01", "Basic Replicator", 1).getStackForm(1L));
        Machine_MV_Replicator.set(new GT_MetaTileEntity_Replicator(482, "basicmachine.replicator.tier.02", "Advanced Replicator", 2).getStackForm(1L));
        Machine_HV_Replicator.set(new GT_MetaTileEntity_Replicator(483, "basicmachine.replicator.tier.03", "Advanced Replicator II", 3).getStackForm(1L));
        Machine_EV_Replicator.set(new GT_MetaTileEntity_Replicator(484, "basicmachine.replicator.tier.04", "Advanced Replicator III", 4).getStackForm(1L));
        Machine_IV_Replicator.set(new GT_MetaTileEntity_Replicator(485, "basicmachine.replicator.tier.05", "Advanced Replicator IV", 5).getStackForm(1L));

        addCraftingRecipe(
                Machine_LV_Replicator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "EFE", "CMC", "EWE", 'M', Hull_LV, 'F', Field_Generator_LV, 'E', Emitter_LV, 'C', circuitPower.get(PWR_MV), 'W', cableGt04.get(Tin)
                });
        addCraftingRecipe(
                Machine_MV_Replicator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "EFE", "CMC", "EWE", 'M', Hull_MV, 'F', Field_Generator_MV, 'E', Emitter_MV, 'C', circuitPower.get(PWR_HV), 'W',
                        cableGt04.get(AnyCopper)
                });
        addCraftingRecipe(
                Machine_HV_Replicator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "EFE", "CMC", "EWE", 'M', Hull_HV, 'F', Field_Generator_HV, 'E', Emitter_HV, 'C', circuitPower.get(PWR_EV), 'W', cableGt04.get(Gold)
                });
        addCraftingRecipe(
                Machine_EV_Replicator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "EFE", "CMC", "EWE", 'M', Hull_EV, 'F', Field_Generator_EV, 'E', Emitter_EV, 'C', circuitPower.get(PWR_IV), 'W',
                        cableGt04.get(Aluminium)
                });
        addCraftingRecipe(
                Machine_IV_Replicator.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "EFE", "CMC", "EWE", 'M', Hull_IV, 'F', Field_Generator_IV, 'E', Emitter_IV, 'C', circuitPower.get(PWR_LUV), 'W',
                        cableGt04.get(Tungsten)
                });

        Machine_LV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(491, "basicmachine.brewery.tier.01", "Basic Brewery", 1).getStackForm(1L));
        Machine_MV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(492, "basicmachine.brewery.tier.02", "Advanced Brewery", 2).getStackForm(1L));
        Machine_HV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(493, "basicmachine.brewery.tier.03", "Advanced Brewery II", 3).getStackForm(1L));
        Machine_EV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(494, "basicmachine.brewery.tier.04", "Advanced Brewery III", 4).getStackForm(1L));
        Machine_IV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(495, "basicmachine.brewery.tier.05", "Advanced Brewery IV", 5).getStackForm(1L));

        addCraftingRecipe(
                Machine_LV_Brewery.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "GPG", "WMW", "CBC", 'M', Hull_LV, 'P', Electric_Pump_LV, 'B', new ItemStack(Items.brewing_stand, 0), 'C', circuitPower.get(PWR_LV),
                        'W', cableGt01.get(Tin), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_MV_Brewery.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "GPG", "WMW", "CBC", 'M', Hull_MV, 'P', Electric_Pump_MV, 'B', new ItemStack(Items.brewing_stand, 0), 'C', circuitPower.get(PWR_MV),
                        'W', cableGt01.get(AnyCopper), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_HV_Brewery.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "GPG", "WMW", "CBC", 'M', Hull_HV, 'P', Electric_Pump_HV, 'B', new ItemStack(Items.brewing_stand, 0), 'C', circuitPower.get(PWR_HV),
                        'W', cableGt01.get(Gold), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_EV_Brewery.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "GPG", "WMW", "CBC", 'M', Hull_EV, 'P', Electric_Pump_EV, 'B', new ItemStack(Items.brewing_stand, 0), 'C', circuitPower.get(PWR_EV),
                        'W', cableGt01.get(Aluminium), 'G', new ItemStack(Blocks.glass, 1)
                });
        addCraftingRecipe(
                Machine_IV_Brewery.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "GPG", "WMW", "CBC", 'M', Hull_IV, 'P', Electric_Pump_IV, 'B', new ItemStack(Items.brewing_stand, 0), 'C', circuitPower.get(PWR_IV),
                        'W', cableGt01.get(Tungsten), 'G', new ItemStack(Blocks.glass, 1)
                });

        Machine_LV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(501, "basicmachine.fermenter.tier.01", "Basic Fermenter", 1, "Fermenting Fluids",
                                                                              sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", false, false, 0,
                                                                              "FERMENTER", new Object[]{
                "WPW", "GMG", "WCW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_MV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(502, "basicmachine.fermenter.tier.02", "Advanced Fermenter", 2, "Fermenting Fluids",
                                                             sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", false, false, 0, "FERMENTER",
                                                             new Object[]{
                                                                     "WPW", "GMG", "WCW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G',
                                                                     GLASS
                                                             }
                ).getStackForm(1L));
        Machine_HV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(503, "basicmachine.fermenter.tier.03", "Advanced Fermenter II", 3, "Fermenting Fluids",
                                                             sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", false, false, 0, "FERMENTER",
                                                             new Object[]{
                                                                     "WPW", "GMG", "WCW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G',
                                                                     GLASS
                                                             }
                ).getStackForm(1L));
        Machine_EV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(504, "basicmachine.fermenter.tier.04", "Advanced Fermenter III", 4, "Fermenting Fluids",
                                                             sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", false, false, 0, "FERMENTER",
                                                             new Object[]{
                                                                     "WPW", "GMG", "WCW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G',
                                                                     GLASS
                                                             }
                ).getStackForm(1L));
        Machine_IV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(505, "basicmachine.fermenter.tier.05", "Advanced Fermenter IV", 5, "Fermenting Fluids",
                                                             sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", false, false, 0, "FERMENTER",
                                                             new Object[]{
                                                                     "WPW", "GMG", "WCW", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G',
                                                                     GLASS
                                                             }
                ).getStackForm(1L));

        Machine_LV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(511, "basicmachine.fluidextractor.tier.01", "Basic Fluid Extractor", 1,
                                                                                   "Extracting Fluids from Items", sFluidExtractionRecipes, 1, 1, 16000, 0, 1,
                                                                                   "FluidExtractor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                                   "FLUID_EXTRACTOR", new Object[]{
                "GCG", "PME", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_MV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(512, "basicmachine.fluidextractor.tier.02", "Advanced Fluid Extractor", 2,
                                                                                   "Extracting Fluids from Items", sFluidExtractionRecipes, 1, 1, 16000, 0, 1,
                                                                                   "FluidExtractor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                                   "FLUID_EXTRACTOR", new Object[]{
                "GCG", "PME", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_HV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(513, "basicmachine.fluidextractor.tier.03", "Advanced Fluid Extractor II", 3,
                                                                                   "Extracting Fluids from Items", sFluidExtractionRecipes, 1, 1, 16000, 0, 1,
                                                                                   "FluidExtractor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                                   "FLUID_EXTRACTOR", new Object[]{
                "GCG", "PME", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_EV_FluidExtractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(514, "basicmachine.fluidextractor.tier.04", "Advanced Fluid Extractor III", 4,
                                                             "Extracting Fluids from Items", sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png",
                                                             sSoundList.get(Integer.valueOf(200)), false, false, 0, "FLUID_EXTRACTOR", new Object[]{
                        "GCG", "PME", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS
                }
                ).getStackForm(1L));
        Machine_IV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(515, "basicmachine.fluidextractor.tier.05", "Advanced Fluid Extractor IV", 5,
                                                                                   "Extracting Fluids from Items", sFluidExtractionRecipes, 1, 1, 16000, 0, 1,
                                                                                   "FluidExtractor.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                                   "FLUID_EXTRACTOR", new Object[]{
                "GCG", "PME", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));

        Machine_LV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(521, "basicmachine.fluidsolidifier.tier.01", "Basic Fluid Solidifier", 1,
                                                                                    "Cools Fluids down to form Solids", sFluidSolidficationRecipes, 1, 1, 16000,
                                                                                    0, 1, "FluidSolidifier.png", "", false, false, 0, "FLUID_SOLIDIFIER",
                                                                                    new Object[]{
                                                                                            "PGP", "WMW", "CBC", 'M', HULL, 'P', PUMP, 'C',
                                                                                            circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS, 'B', craftingChest
                                                                                    }
        ).getStackForm(1L));
        Machine_MV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(522, "basicmachine.fluidsolidifier.tier.02", "Advanced Fluid Solidifier", 2,
                                                                                    "Cools Fluids down to form Solids", sFluidSolidficationRecipes, 1, 1, 16000,
                                                                                    0, 1, "FluidSolidifier.png", "", false, false, 0, "FLUID_SOLIDIFIER",
                                                                                    new Object[]{
                                                                                            "PGP", "WMW", "CBC", 'M', HULL, 'P', PUMP, 'C',
                                                                                            circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS, 'B', craftingChest
                                                                                    }
        ).getStackForm(1L));
        Machine_HV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(523, "basicmachine.fluidsolidifier.tier.03", "Advanced Fluid Solidifier II", 3,
                                                             "Cools Fluids down to form Solids", sFluidSolidficationRecipes, 1, 1, 16000, 0, 1,
                                                             "FluidSolidifier.png", "", false, false, 0, "FLUID_SOLIDIFIER", new Object[]{
                        "PGP", "WMW", "CBC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS, 'B', craftingChest
                }
                ).getStackForm(1L));
        Machine_EV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(524, "basicmachine.fluidsolidifier.tier.04", "Advanced Fluid Solidifier III", 4,
                                                             "Cools Fluids down to form Solids", sFluidSolidficationRecipes, 1, 1, 16000, 0, 1,
                                                             "FluidSolidifier.png", "", false, false, 0, "FLUID_SOLIDIFIER", new Object[]{
                        "PGP", "WMW", "CBC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS, 'B', craftingChest
                }
                ).getStackForm(1L));
        Machine_IV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(525, "basicmachine.fluidsolidifier.tier.05", "Advanced Fluid Solidifier IV", 5,
                                                             "Cools Fluids down to form Solids", sFluidSolidficationRecipes, 1, 1, 16000, 0, 1,
                                                             "FluidSolidifier.png", "", false, false, 0, "FLUID_SOLIDIFIER", new Object[]{
                        "PGP", "WMW", "CBC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS, 'B', craftingChest
                }
                ).getStackForm(1L));

        Machine_LV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(531, "basicmachine.distillery.tier.01", "Basic Distillery", 1,
                                                                               "Extracting the most relevant Parts of Fluids", sDistilleryRecipes, 1, 1, 8000,
                                                                               0, 1, "Distillery.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                               "DISTILLERY", new Object[]{
                "GBG", "CMC", "WPW", 'M', HULL, 'P', PUMP, 'B', PIPE, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_MV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(532, "basicmachine.distillery.tier.02", "Advanced Distillery", 2,
                                                                               "Extracting the most relevant Parts of Fluids", sDistilleryRecipes, 1, 1, 16000,
                                                                               0, 1, "Distillery.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                               "DISTILLERY", new Object[]{
                "GBG", "CMC", "WPW", 'M', HULL, 'P', PUMP, 'B', PIPE, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_HV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(533, "basicmachine.distillery.tier.03", "Advanced Distillery II", 3,
                                                                               "Extracting the most relevant Parts of Fluids", sDistilleryRecipes, 1, 1, 24000,
                                                                               0, 1, "Distillery.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                               "DISTILLERY", new Object[]{
                "GBG", "CMC", "WPW", 'M', HULL, 'P', PUMP, 'B', PIPE, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_EV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(534, "basicmachine.distillery.tier.04", "Advanced Distillery III", 4,
                                                                               "Extracting the most relevant Parts of Fluids", sDistilleryRecipes, 1, 1, 32000,
                                                                               0, 1, "Distillery.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                               "DISTILLERY", new Object[]{
                "GBG", "CMC", "WPW", 'M', HULL, 'P', PUMP, 'B', PIPE, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_IV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(535, "basicmachine.distillery.tier.05", "Advanced Distillery IV", 5,
                                                                               "Extracting the most relevant Parts of Fluids", sDistilleryRecipes, 1, 1, 40000,
                                                                               0, 1, "Distillery.png", sSoundList.get(Integer.valueOf(200)), false, false, 0,
                                                                               "DISTILLERY", new Object[]{
                "GBG", "CMC", "WPW", 'M', HULL, 'P', PUMP, 'B', PIPE, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));

        Machine_LV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(541, "basicmachine.chemicalbath.tier.01", "Basic Chemical Bath", 1,
                                                                                 "Bathing Ores in Chemicals to separate them", sChemicalBathRecipes, 1, 3, 8000,
                                                                                 0, 1, "ChemicalBath.png", "", false, true, 0, "CHEMICAL_BATH", new Object[]{
                "VGW", "PGV", "CMC", 'M', HULL, 'P', PUMP, 'V', CONVEYOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_MV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(542, "basicmachine.chemicalbath.tier.02", "Advanced Chemical Bath", 2,
                                                                                 "Bathing Ores in Chemicals to separate them", sChemicalBathRecipes, 1, 3, 8000,
                                                                                 0, 1, "ChemicalBath.png", "", false, true, 0, "CHEMICAL_BATH", new Object[]{
                "VGW", "PGV", "CMC", 'M', HULL, 'P', PUMP, 'V', CONVEYOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_HV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(543, "basicmachine.chemicalbath.tier.03", "Advanced Chemical Bath II", 3,
                                                                                 "Bathing Ores in Chemicals to separate them", sChemicalBathRecipes, 1, 3, 8000,
                                                                                 0, 1, "ChemicalBath.png", "", false, true, 0, "CHEMICAL_BATH", new Object[]{
                "VGW", "PGV", "CMC", 'M', HULL, 'P', PUMP, 'V', CONVEYOR, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_EV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(544, "basicmachine.chemicalbath.tier.04", "Advanced Chemical Bath III", 4,
                                                                                 "Bathing Ores in Chemicals to separate them", sChemicalBathRecipes, 1, 3, 8000,
                                                                                 0, 1, "ChemicalBath.png", "", false, true, 0, "CHEMICAL_BATH", new Object[]{
                "VGW", "PGV", "CMC", 'M', HULL, 'P', PUMP, 'V', CONVEYOR, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));
        Machine_IV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(545, "basicmachine.chemicalbath.tier.05", "Advanced Chemical Bath IV", 5,
                                                                                 "Bathing Ores in Chemicals to separate them", sChemicalBathRecipes, 1, 3, 8000,
                                                                                 0, 1, "ChemicalBath.png", "", false, true, 0, "CHEMICAL_BATH", new Object[]{
                "VGW", "PGV", "CMC", 'M', HULL, 'P', PUMP, 'V', CONVEYOR, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS
        }
        ).getStackForm(1L));

        Machine_LV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(551, "basicmachine.polarizer.tier.01", "Basic Polarizer", 1, "Bipolarising your Magnets",
                                                             sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", sSoundList.get(Integer.valueOf(212)), false,
                                                             false, 0, "POLARIZER", new Object[]{
                        "ZSZ", "WMW", "ZSZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_MV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(552, "basicmachine.polarizer.tier.02", "Advanced Polarizer", 2, "Bipolarising your Magnets",
                                                             sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", sSoundList.get(Integer.valueOf(212)), false,
                                                             false, 0, "POLARIZER", new Object[]{
                        "ZSZ", "WMW", "ZCZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'C', circuitPower.get(PWR_LV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_HV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(553, "basicmachine.polarizer.tier.03", "Advanced Polarizer II", 3, "Bipolarising your Magnets",
                                                             sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", sSoundList.get(Integer.valueOf(212)), false,
                                                             false, 0, "POLARIZER", new Object[]{
                        "ZSZ", "WMW", "ZCZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'C', circuitPower.get(PWR_MV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_EV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(554, "basicmachine.polarizer.tier.04", "Advanced Polarizer III", 4, "Bipolarising your Magnets",
                                                             sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", sSoundList.get(Integer.valueOf(212)), false,
                                                             false, 0, "POLARIZER", new Object[]{
                        "ZSZ", "WMW", "ZCZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'C', circuitPower.get(PWR_HV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_IV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(555, "basicmachine.polarizer.tier.05", "Advanced Polarizer IV", 5, "Bipolarising your Magnets",
                                                             sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", sSoundList.get(Integer.valueOf(212)), false,
                                                             false, 0, "POLARIZER", new Object[]{
                        "ZSZ", "WMW", "ZCZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'C', circuitPower.get(PWR_EV), 'W', WIRE
                }
                ).getStackForm(1L));

        Machine_LV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(561, "basicmachine.electromagneticseparator.tier.01", "Basic Electromagnetic Separator", 1,
                                                             "Separating the magnetic Ores from the rest", sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1,
                                                             "ElectromagneticSeparator.png", sSoundList.get(Integer.valueOf(212)), false, false, 0,
                                                             "ELECTROMAGNETIC_SEPARATOR", new Object[]{
                        "VWZ", "WMS", "CWZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'V', CONVEYOR, 'C', circuitPower.get(PWR_LV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_MV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(562, "basicmachine.electromagneticseparator.tier.02", "Advanced Electromagnetic Separator", 2,
                                                             "Separating the magnetic Ores from the rest", sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1,
                                                             "ElectromagneticSeparator.png", sSoundList.get(Integer.valueOf(212)), false, false, 0,
                                                             "ELECTROMAGNETIC_SEPARATOR", new Object[]{
                        "VWZ", "WMS", "CWZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'V', CONVEYOR, 'C', circuitPower.get(PWR_MV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_HV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(563, "basicmachine.electromagneticseparator.tier.03", "Advanced Electromagnetic Separator II", 3,
                                                             "Separating the magnetic Ores from the rest", sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1,
                                                             "ElectromagneticSeparator.png", sSoundList.get(Integer.valueOf(212)), false, false, 0,
                                                             "ELECTROMAGNETIC_SEPARATOR", new Object[]{
                        "VWZ", "WMS", "CWZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'V', CONVEYOR, 'C', circuitPower.get(PWR_HV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_EV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(564, "basicmachine.electromagneticseparator.tier.04", "Advanced Electromagnetic Separator III", 4,
                                                             "Separating the magnetic Ores from the rest", sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1,
                                                             "ElectromagneticSeparator.png", sSoundList.get(Integer.valueOf(212)), false, false, 0,
                                                             "ELECTROMAGNETIC_SEPARATOR", new Object[]{
                        "VWZ", "WMS", "CWZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'V', CONVEYOR, 'C', circuitPower.get(PWR_EV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_IV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(565, "basicmachine.electromagneticseparator.tier.05", "Advanced Electromagnetic Separator IV", 5,
                                                             "Separating the magnetic Ores from the rest", sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1,
                                                             "ElectromagneticSeparator.png", sSoundList.get(Integer.valueOf(212)), false, false, 0,
                                                             "ELECTROMAGNETIC_SEPARATOR", new Object[]{
                        "VWZ", "WMS", "CWZ", 'M', HULL, 'S', STICK_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'V', CONVEYOR, 'C', circuitPower.get(PWR_IV), 'W', WIRE
                }
                ).getStackForm(1L));

        Machine_LV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(571, "basicmachine.autoclave.tier.01", "Basic Autoclave", 1, "Crystallizing your Dusts",
                                                             sAutoclaveRecipes, 2, 1, 8000, 0, 1, "Autoclave.png", "", false, false, 0, "AUTOCLAVE",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'I',
                                                                     PLATE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_MV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(572, "basicmachine.autoclave.tier.02", "Advanced Autoclave", 2, "Crystallizing your Dusts",
                                                             sAutoclaveRecipes, 2, 1, 8000, 0, 1, "Autoclave.png", "", false, false, 0, "AUTOCLAVE",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'I',
                                                                     PLATE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_HV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(573, "basicmachine.autoclave.tier.03", "Advanced Autoclave II", 3, "Crystallizing your Dusts",
                                                             sAutoclaveRecipes, 2, 1, 8000, 0, 1, "Autoclave.png", "", false, false, 0, "AUTOCLAVE",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'I',
                                                                     PLATE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_EV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(574, "basicmachine.autoclave.tier.04", "Advanced Autoclave III", 4, "Crystallizing your Dusts",
                                                             sAutoclaveRecipes, 2, 1, 8000, 0, 1, "Autoclave.png", "", false, false, 0, "AUTOCLAVE",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'I',
                                                                     PLATE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_IV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(575, "basicmachine.autoclave.tier.05", "Advanced Autoclave IV", 5, "Crystallizing your Dusts",
                                                             sAutoclaveRecipes, 2, 1, 8000, 0, 1, "Autoclave.png", "", false, false, 0, "AUTOCLAVE",
                                                             new Object[]{
                                                                     "IGI", "IMI", "CPC", 'M', HULL, 'P', PUMP, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'I',
                                                                     PLATE, 'G', GLASS
                                                             }
                ).getStackForm(1L));

        Machine_LV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(581, "basicmachine.mixer.tier.01", "Basic Mixer", 1, "Will it Blend?", sMixerRecipes, 6, 1, 16000,
                                                             0, 1, "Mixer.png", "", false, false, 0, "MIXER", new Object[]{
                        "GRG", "GEG", "CMC", 'M', HULL, 'E', MOTOR, 'R', ROTOR, 'C', circuitPower.get(PWR_LV), 'G', GLASS
                }
                ).getStackForm(1L));
        Machine_MV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(582, "basicmachine.mixer.tier.02", "Advanced Mixer", 2, "Will it Blend?", sMixerRecipes, 6, 1,
                                                             32000, 0, 1, "Mixer.png", "", false, false, 0, "MIXER", new Object[]{
                        "GRG", "GEG", "CMC", 'M', HULL, 'E', MOTOR, 'R', ROTOR, 'C', circuitPower.get(PWR_MV), 'G', GLASS
                }
                ).getStackForm(1L));
        Machine_HV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(583, "basicmachine.mixer.tier.03", "Advanced Mixer II", 3, "Will it Blend?", sMixerRecipes, 6, 4,
                                                             48000, 0, 1, "Mixer4.png", "", false, false, 0, "MIXER", new Object[]{
                        "GRG", "GEG", "CMC", 'M', HULL, 'E', MOTOR, 'R', ROTOR, 'C', circuitPower.get(PWR_HV), 'G', GLASS
                }
                ).getStackForm(1L));
        Machine_EV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(584, "basicmachine.mixer.tier.04", "Advanced Mixer III", 4, "Will it Blend?", sMixerRecipes, 9, 4,
                                                             64000, 0, 1, "Mixer6.png", "", false, false, 0, "MIXER", new Object[]{
                        "GRG", "GEG", "CMC", 'M', HULL, 'E', MOTOR, 'R', ROTOR, 'C', circuitPower.get(PWR_EV), 'G', GLASS
                }
                ).getStackForm(1L));
        Machine_IV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(585, "basicmachine.mixer.tier.05", "Advanced Mixer IV", 5, "Will it Blend?", sMixerRecipes, 9, 4,
                                                             128000, 0, 1, "Mixer6.png", "", false, false, 0, "MIXER", new Object[]{
                        "GRG", "GEG", "CMC", 'M', HULL, 'E', MOTOR, 'R', ROTOR, 'C', circuitPower.get(PWR_IV), 'G', GLASS
                }
                ).getStackForm(1L));

        Machine_LV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(591, "basicmachine.laserengraver.tier.01", "Basic Precision Laser Engraver", 1,
                                                             "Don't look directly at the Laser", sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
                                                             sSoundList.get(Integer.valueOf(212)), false, false, 0, "LASER_ENGRAVER", new Object[]{
                        "PEP", "CMC", "WCW", 'M', HULL, 'E', EMITTER, 'P', PISTON, 'C', circuitLogic.get(LOGIC_LV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_MV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(592, "basicmachine.laserengraver.tier.02", "Advanced Precision Laser Engraver", 2,
                                                             "Don't look directly at the Laser", sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
                                                             sSoundList.get(Integer.valueOf(212)), false, false, 0, "LASER_ENGRAVER", new Object[]{
                        "PEP", "CMC", "WCW", 'M', HULL, 'E', EMITTER, 'P', PISTON, 'C', circuitLogic.get(LOGIC_MV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_HV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(593, "basicmachine.laserengraver.tier.03", "Advanced Precision Laser Engraver II", 3,
                                                             "Don't look directly at the Laser", sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
                                                             sSoundList.get(Integer.valueOf(212)), false, false, 0, "LASER_ENGRAVER", new Object[]{
                        "PEP", "CMC", "WCW", 'M', HULL, 'E', EMITTER, 'P', PISTON, 'C', circuitLogic.get(LOGIC_HV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_EV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(594, "basicmachine.laserengraver.tier.04", "Advanced Precision Laser Engraver III", 4,
                                                             "Don't look directly at the Laser", sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
                                                             sSoundList.get(Integer.valueOf(212)), false, false, 0, "LASER_ENGRAVER", new Object[]{
                        "PEP", "CMC", "WCW", 'M', HULL, 'E', EMITTER, 'P', PISTON, 'C', circuitLogic.get(LOGIC_EV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_IV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(595, "basicmachine.laserengraver.tier.05", "Advanced Precision Laser Engraver IV", 5,
                                                             "Don't look directly at the Laser", sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
                                                             sSoundList.get(Integer.valueOf(212)), false, false, 0, "LASER_ENGRAVER", new Object[]{
                        "PEP", "CMC", "WCW", 'M', HULL, 'E', EMITTER, 'P', PISTON, 'C', circuitLogic.get(LOGIC_IV), 'W', WIRE
                }
                ).getStackForm(1L));

        Machine_LV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(601, "basicmachine.press.tier.01", "Basic Forming Press", 1, "Imprinting Images into things",
                                                             sPressRecipes, 2, 1, 0, 0, 1, "Press.png", sSoundList.get(Integer.valueOf(203)), false, false, 0,
                                                             "PRESS",
                                                             new Object[]{"WPW", "CMC", "WPW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_LV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_MV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(602, "basicmachine.press.tier.02", "Advanced Forming Press", 2, "Imprinting Images into things",
                                                             sPressRecipes, 2, 1, 0, 0, 1, "Press.png", sSoundList.get(Integer.valueOf(203)), false, false, 0,
                                                             "PRESS",
                                                             new Object[]{"WPW", "CMC", "WPW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_MV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_HV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(603, "basicmachine.press.tier.03", "Advanced Forming Press II", 3, "Imprinting Images into things",
                                                             sPressRecipes, 2, 1, 0, 0, 1, "Press.png", sSoundList.get(Integer.valueOf(203)), false, false, 0,
                                                             "PRESS",
                                                             new Object[]{"WPW", "CMC", "WPW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_HV), 'W', WIRE}
                ).getStackForm(1L));
        Machine_EV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(604, "basicmachine.press.tier.04", "Advanced Forming Press III", 4,
                                                                          "Imprinting Images into things", sPressRecipes, 2, 1, 0, 0, 1, "Press.png",
                                                                          sSoundList.get(Integer.valueOf(203)), false, false, 0, "PRESS", new Object[]{
                "WPW", "CMC", "WPW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_EV), 'W', WIRE
        }
        ).getStackForm(1L));
        Machine_IV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(605, "basicmachine.press.tier.05", "Advanced Forming Press IV", 5, "Imprinting Images into things",
                                                             sPressRecipes, 2, 1, 0, 0, 1, "Press.png", sSoundList.get(Integer.valueOf(203)), false, false, 0,
                                                             "PRESS",
                                                             new Object[]{"WPW", "CMC", "WPW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_IV), 'W', WIRE}
                ).getStackForm(1L));

        Machine_LV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(611, "basicmachine.hammer.tier.01", "Basic Forge Hammer", 1, "Stop, Hammertime!", sHammerRecipes,
                                                             1, 1, 0, 6, 3, "Hammer.png", sSoundList.get(Integer.valueOf(1)), false, false, 0, "HAMMER",
                                                             new Object[]{
                                                                     "WPW", "CMC", "WAW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_LV), 'W', WIRE, 'O',
                                                                     COIL_HEATING_DOUBLE, 'A', craftingAnvil
                                                             }
                ).getStackForm(1L));
        Machine_MV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(612, "basicmachine.hammer.tier.02", "Advanced Forge Hammer", 2, "Stop, Hammertime!",
                                                                           sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", sSoundList.get(Integer.valueOf(1)),
                                                                           false, false, 0, "HAMMER", new Object[]{
                "WPW", "CMC", "WAW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_MV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE, 'A', craftingAnvil
        }
        ).getStackForm(1L));
        Machine_HV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(613, "basicmachine.hammer.tier.03", "Advanced Forge Hammer II", 3, "Stop, Hammertime!",
                                                             sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", sSoundList.get(Integer.valueOf(1)), false, false, 0,
                                                             "HAMMER", new Object[]{
                        "WPW", "CMC", "WAW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_HV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE, 'A', craftingAnvil
                }
                ).getStackForm(1L));
        Machine_EV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(614, "basicmachine.hammer.tier.04", "Advanced Forge Hammer III", 4, "Stop, Hammertime!",
                                                             sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", sSoundList.get(Integer.valueOf(1)), false, false, 0,
                                                             "HAMMER", new Object[]{
                        "WPW", "CMC", "WAW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_EV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE, 'A', craftingAnvil
                }
                ).getStackForm(1L));
        Machine_IV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(615, "basicmachine.hammer.tier.05", "Advanced Forge Hammer IV", 5, "Stop, Hammertime!",
                                                             sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", sSoundList.get(Integer.valueOf(1)), false, false, 0,
                                                             "HAMMER", new Object[]{
                        "WPW", "CMC", "WAW", 'M', HULL, 'P', PISTON, 'C', circuitPower.get(PWR_IV), 'W', WIRE, 'O', COIL_HEATING_DOUBLE, 'A', craftingAnvil
                }
                ).getStackForm(1L));

        Machine_LV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(621, "basicmachine.fluidheater.tier.01", "Basic Fluid Heater", 1, "Heating up your Fluids",
                                                             sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", false, false, 0, "FLUID_HEATER",
                                                             new Object[]{
                                                                     "OGO", "PMP", "WCW", 'M', HULL, 'P', PUMP, 'O', COIL_HEATING_DOUBLE, 'C',
                                                                     circuitPower.get(PWR_LV), 'W', WIRE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_MV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(622, "basicmachine.fluidheater.tier.02", "Advanced Fluid Heater", 2, "Heating up your Fluids",
                                                             sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", false, false, 0, "FLUID_HEATER",
                                                             new Object[]{
                                                                     "OGO", "PMP", "WCW", 'M', HULL, 'P', PUMP, 'O', COIL_HEATING_DOUBLE, 'C',
                                                                     circuitPower.get(PWR_MV), 'W', WIRE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_HV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(623, "basicmachine.fluidheater.tier.03", "Advanced Fluid Heater II", 3, "Heating up your Fluids",
                                                             sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", false, false, 0, "FLUID_HEATER",
                                                             new Object[]{
                                                                     "OGO", "PMP", "WCW", 'M', HULL, 'P', PUMP, 'O', COIL_HEATING_DOUBLE, 'C',
                                                                     circuitPower.get(PWR_HV), 'W', WIRE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_EV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(624, "basicmachine.fluidheater.tier.04", "Advanced Fluid Heater III", 4, "Heating up your Fluids",
                                                             sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", false, false, 0, "FLUID_HEATER",
                                                             new Object[]{
                                                                     "OGO", "PMP", "WCW", 'M', HULL, 'P', PUMP, 'O', COIL_HEATING_DOUBLE, 'C',
                                                                     circuitPower.get(PWR_EV), 'W', WIRE, 'G', GLASS
                                                             }
                ).getStackForm(1L));
        Machine_IV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(625, "basicmachine.fluidheater.tier.05", "Advanced Fluid Heater IV", 5, "Heating up your Fluids",
                                                             sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", false, false, 0, "FLUID_HEATER",
                                                             new Object[]{
                                                                     "OGO", "PMP", "WCW", 'M', HULL, 'P', PUMP, 'O', COIL_HEATING_DOUBLE, 'C',
                                                                     circuitPower.get(PWR_IV), 'W', WIRE, 'G', GLASS
                                                             }
                ).getStackForm(1L));

        Machine_LV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(631, "basicmachine.slicer.tier.01", "Basic Slicing Machine", 1, "Slice of Life", sSlicerRecipes, 2,
                                                             1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER", new Object[]{
                        "WCW", "PMV", "WCW", 'M', HULL, 'P', PISTON, 'V', CONVEYOR, 'C', circuitLogic.get(LOGIC_LV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_MV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(632, "basicmachine.slicer.tier.02", "Advanced Slicing Machine", 2, "Slice of Life", sSlicerRecipes,
                                                             2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER", new Object[]{
                        "WCW", "PMV", "WCW", 'M', HULL, 'P', PISTON, 'V', CONVEYOR, 'C', circuitLogic.get(LOGIC_MV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_HV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(633, "basicmachine.slicer.tier.03", "Advanced Slicing Machine II", 3, "Slice of Life",
                                                             sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER", new Object[]{
                        "WCW", "PMV", "WCW", 'M', HULL, 'P', PISTON, 'V', CONVEYOR, 'C', circuitLogic.get(LOGIC_HV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_EV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(634, "basicmachine.slicer.tier.04", "Advanced Slicing Machine III", 4, "Slice of Life",
                                                             sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER", new Object[]{
                        "WCW", "PMV", "WCW", 'M', HULL, 'P', PISTON, 'V', CONVEYOR, 'C', circuitLogic.get(LOGIC_EV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_IV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(635, "basicmachine.slicer.tier.05", "Advanced Slicing Machine IV", 5, "Slice of Life",
                                                             sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER", new Object[]{
                        "WCW", "PMV", "WCW", 'M', HULL, 'P', PISTON, 'V', CONVEYOR, 'C', circuitLogic.get(LOGIC_IV), 'W', WIRE
                }
                ).getStackForm(1L));

        Machine_LV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(641, "basicmachine.sifter.tier.01", "Basic Sifting Machine", 1, "Stay calm and keep sifting",
                                                             sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER", new Object[]{
                        "WFW", "PMP", "CFC", 'M', HULL, 'P', PISTON, 'F', craftingFilter, 'C', circuitLogic.get(LOGIC_LV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_MV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(642, "basicmachine.sifter.tier.02", "Advanced Sifting Machine", 2, "Stay calm and keep sifting",
                                                             sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER", new Object[]{
                        "WFW", "PMP", "CFC", 'M', HULL, 'P', PISTON, 'F', craftingFilter, 'C', circuitLogic.get(LOGIC_MV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_HV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(643, "basicmachine.sifter.tier.03", "Advanced Sifting Machine II", 3, "Stay calm and keep sifting",
                                                             sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER", new Object[]{
                        "WFW", "PMP", "CFC", 'M', HULL, 'P', PISTON, 'F', craftingFilter, 'C', circuitLogic.get(LOGIC_HV), 'W', WIRE
                }
                ).getStackForm(1L));
        Machine_EV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(644, "basicmachine.sifter.tier.04", "Advanced Sifting Machine III", 4,
                                                                           "Stay calm and keep sifting", sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false,
                                                                           false, 0, "SIFTER", new Object[]{
                "WFW", "PMP", "CFC", 'M', HULL, 'P', PISTON, 'F', craftingFilter, 'C', circuitLogic.get(LOGIC_EV), 'W', WIRE
        }
        ).getStackForm(1L));
        Machine_IV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(645, "basicmachine.sifter.tier.05", "Advanced Sifting Machine IV", 5, "Stay calm and keep sifting",
                                                             sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER", new Object[]{
                        "WFW", "PMP", "CFC", 'M', HULL, 'P', PISTON, 'F', craftingFilter, 'C', circuitLogic.get(LOGIC_IV), 'W', WIRE
                }
                ).getStackForm(1L));

        Machine_LV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(651, "basicmachine.arcfurnace.tier.01", "Basic Arc Furnace", 1, "", sArcFurnaceRecipes, 1, 4,
                                                             16000, 0, 1, "ArcFurnace.png", sSoundList.get(Integer.valueOf(202)), false, false, 0,
                                                             "ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_LV), 'W', WIRE4, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_MV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(652, "basicmachine.arcfurnace.tier.02", "Advanced Arc Furnace", 2, "", sArcFurnaceRecipes, 1, 4,
                                                             24000, 0, 1, "ArcFurnace.png", sSoundList.get(Integer.valueOf(202)), false, false, 0,
                                                             "ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_MV), 'W', WIRE4, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_HV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(653, "basicmachine.arcfurnace.tier.03", "Advanced Arc Furnace II", 3, "", sArcFurnaceRecipes, 1, 4,
                                                             32000, 0, 1, "ArcFurnace.png", sSoundList.get(Integer.valueOf(202)), false, false, 0,
                                                             "ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_HV), 'W', WIRE4, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_EV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(654, "basicmachine.arcfurnace.tier.04", "Advanced Arc Furnace III", 4, "", sArcFurnaceRecipes, 1,
                                                             4, 48000, 0, 1, "ArcFurnace.png", sSoundList.get(Integer.valueOf(202)), false, false, 0,
                                                             "ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_EV), 'W', WIRE4, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_IV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(655, "basicmachine.arcfurnace.tier.05", "Advanced Arc Furnace IV", 5, "", sArcFurnaceRecipes, 1, 4,
                                                             64000, 0, 1, "ArcFurnace.png", sSoundList.get(Integer.valueOf(202)), false, false, 0,
                                                             "ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_IV), 'W', WIRE4, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));

        Machine_LV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(661, "basicmachine.plasmaarcfurnace.tier.01", "Basic Plasma Arc Furnace", 1, "",
                                                             sPlasmaArcFurnaceRecipes, 1, 4, 1000, 0, 1, "PlasmaArcFurnace.png",
                                                             sSoundList.get(Integer.valueOf(202)), false, false, 0, "PLASMA_ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "TPT", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_MV), 'W', WIRE4, 'T', PUMP, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_MV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(662, "basicmachine.plasmaarcfurnace.tier.02", "Advanced Plasma Arc Furnace", 2, "",
                                                             sPlasmaArcFurnaceRecipes, 1, 4, 2000, 0, 1, "PlasmaArcFurnace.png",
                                                             sSoundList.get(Integer.valueOf(202)), false, false, 0, "PLASMA_ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "TPT", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_HV), 'W', WIRE4, 'T', PUMP, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_HV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(663, "basicmachine.plasmaarcfurnace.tier.03", "Advanced Plasma Arc Furnace II", 3, "",
                                                             sPlasmaArcFurnaceRecipes, 1, 4, 4000, 0, 1, "PlasmaArcFurnace.png",
                                                             sSoundList.get(Integer.valueOf(202)), false, false, 0, "PLASMA_ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "TPT", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_EV), 'W', WIRE4, 'T', PUMP, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_EV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(664, "basicmachine.plasmaarcfurnace.tier.04", "Advanced Plasma Arc Furnace III", 4, "",
                                                             sPlasmaArcFurnaceRecipes, 1, 4, 8000, 0, 1, "PlasmaArcFurnace.png",
                                                             sSoundList.get(Integer.valueOf(202)), false, false, 0, "PLASMA_ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "TPT", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_IV), 'W', WIRE4, 'T', PUMP, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));
        Machine_IV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(665, "basicmachine.plasmaarcfurnace.tier.05", "Advanced Plasma Arc Furnace IV", 5, "",
                                                             sPlasmaArcFurnaceRecipes, 1, 4, 16000, 0, 1, "PlasmaArcFurnace.png",
                                                             sSoundList.get(Integer.valueOf(202)), false, false, 0, "PLASMA_ARC_FURNACE", new Object[]{
                        "WGW", "CMC", "TPT", 'M', HULL, 'P', PLATE, 'C', circuitPower.get(PWR_LUV), 'W', WIRE4, 'T', PUMP, 'G', cell.get(Graphite)
                }
                ).getStackForm(1L));

        Machine_LV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(671, "basicmachine.e_oven.tier.01", "Basic Electric Oven", 1,
                                                                         "Just a Furnace with a different Design", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png",
                                                                         sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_OVEN", new Object[]{
                "CEC", "CMC", "WEW", 'M', HULL, 'E', circuitPower.get(PWR_LV), 'W', WIRE, 'C', COIL_HEATING
        }
        ).getStackForm(1L));
        Machine_MV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(672, "basicmachine.e_oven.tier.02", "Advanced Electric Oven", 2,
                                                                         "Just a Furnace with a different Design", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png",
                                                                         sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_OVEN", new Object[]{
                "CEC", "CMC", "WEW", 'M', HULL, 'E', circuitPower.get(PWR_MV), 'W', WIRE, 'C', COIL_HEATING
        }
        ).getStackForm(1L));
        Machine_HV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(673, "basicmachine.e_oven.tier.03", "Advanced Electric Oven II", 3,
                                                                         "Just a Furnace with a different Design", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png",
                                                                         sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_OVEN", new Object[]{
                "CEC", "CMC", "WEW", 'M', HULL, 'E', circuitPower.get(PWR_HV), 'W', WIRE, 'C', COIL_HEATING
        }
        ).getStackForm(1L));
        Machine_EV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(674, "basicmachine.e_oven.tier.04", "Advanced Electric Oven III", 4,
                                                                         "Just a Furnace with a different Design", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png",
                                                                         sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_OVEN", new Object[]{
                "CEC", "CMC", "WEW", 'M', HULL, 'E', circuitPower.get(PWR_EV), 'W', WIRE, 'C', COIL_HEATING
        }
        ).getStackForm(1L));
        Machine_IV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(675, "basicmachine.e_oven.tier.05", "Advanced Electric Oven IV", 5,
                                                                         "Just a Furnace with a different Design", sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png",
                                                                         sSoundList.get(Integer.valueOf(207)), false, false, 0, "ELECTRIC_OVEN", new Object[]{
                "CEC", "CMC", "WEW", 'M', HULL, 'E', circuitPower.get(PWR_IV), 'W', WIRE, 'C', COIL_HEATING
        }
        ).getStackForm(1L));

        Machine_LV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1180, "basicmachine.circuitassembler.tier.01", "Basic Circuit Assembling Machine", 1,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_LV), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        Machine_MV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1181, "basicmachine.circuitassembler.tier.02", "Advanced Circuit Assembling Machine", 2,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_MV), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        Machine_HV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1182, "basicmachine.circuitassembler.tier.03", "Advanced Circuit Assembling Machine II", 3,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_HV), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        Machine_EV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1183, "basicmachine.circuitassembler.tier.04", "Advanced Circuit Assembling Machine III", 4,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_EV), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        Machine_IV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1184, "basicmachine.circuitassembler.tier.05", "Advanced Circuit Assembling Machine IV", 5,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_IV), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        Machine_LuV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1185, "basicmachine.circuitassembler.tier.06", "Advanced Circuit Assembling Machine V", 6,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_LUV), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        Machine_ZPM_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1186, "basicmachine.circuitassembler.tier.07", "Advanced Circuit Assembling Machine VI", 7,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_ZPM), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        Machine_UV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(1187, "basicmachine.circuitassembler.tier.08", "Advanced Circuit Assembling Machine VII", 8,
                                                             "Pick-n-Place all over the place", sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1,
                                                             "CircuitAssembler.png", "", false, false, 0, "CIRCUITASSEMBLER", new Object[]{
                        "ACE", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A', ROBOT_ARM, 'C', circuitLogic.get(LOGIC_UV), 'W', WIRE, 'E', EMITTER
                }
                ).getStackForm(1L));
        LOADED = true;
    }

}
