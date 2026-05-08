package gregtech.loaders.postload;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.events.GT_OreVeinLocations;
import gregtech.api.util.GT_Log;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.GT_Worldgen_Rubber_Tree;
import gregtech.common.GT_Worldgen_Stone;
import gregtech.common.GT_Worldgenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressWarnings("ALL")
public class GT_Worldgenloader implements Runnable {

    public void run() {

        new GT_Worldgenerator();

        //These are unused in MEGA
        new GT_Worldgen_Stone("overworld.stone.blackgranite.tiny", true, GregTech_API.sBlockGranites, 0, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.small", true, GregTech_API.sBlockGranites, 0, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.medium", true, GregTech_API.sBlockGranites, 0, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.large", true, GregTech_API.sBlockGranites, 0, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.huge", true, GregTech_API.sBlockGranites, 0, 0, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("overworld.stone.redgranite.tiny", true, GregTech_API.sBlockGranites, 8, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.small", true, GregTech_API.sBlockGranites, 8, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.medium", true, GregTech_API.sBlockGranites, 8, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.large", true, GregTech_API.sBlockGranites, 8, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.huge", true, GregTech_API.sBlockGranites, 8, 0, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("overworld.stone.marble.tiny", true, GregTech_API.sBlockStones, 0, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.small", true, GregTech_API.sBlockStones, 0, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.medium", true, GregTech_API.sBlockStones, 0, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.large", true, GregTech_API.sBlockStones, 0, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.huge", true, GregTech_API.sBlockStones, 0, 0, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("overworld.stone.basalt.tiny", true, GregTech_API.sBlockStones, 8, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.small", true, GregTech_API.sBlockStones, 8, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.medium", true, GregTech_API.sBlockStones, 8, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.large", true, GregTech_API.sBlockStones, 8, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.huge", true, GregTech_API.sBlockStones, 8, 0, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.blackgranite.tiny", false, GregTech_API.sBlockGranites, 0, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.small", false, GregTech_API.sBlockGranites, 0, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.medium", false, GregTech_API.sBlockGranites, 0, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.large", false, GregTech_API.sBlockGranites, 0, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.huge", false, GregTech_API.sBlockGranites, 0, -1, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.redgranite.tiny", false, GregTech_API.sBlockGranites, 8, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.small", false, GregTech_API.sBlockGranites, 8, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.medium", false, GregTech_API.sBlockGranites, 8, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.large", false, GregTech_API.sBlockGranites, 8, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.huge", false, GregTech_API.sBlockGranites, 8, -1, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.marble.tiny", false, GregTech_API.sBlockStones, 0, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.small", false, GregTech_API.sBlockStones, 0, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.medium", false, GregTech_API.sBlockStones, 0, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.large", false, GregTech_API.sBlockStones, 0, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.huge", false, GregTech_API.sBlockStones, 0, -1, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.basalt.tiny", false, GregTech_API.sBlockStones, 8, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.small", false, GregTech_API.sBlockStones, 8, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.medium", false, GregTech_API.sBlockStones, 8, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.large", false, GregTech_API.sBlockStones, 8, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.huge", false, GregTech_API.sBlockStones, 8, -1, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Rubber_Tree();

        if (GregTech_API.sWorldgenFile.get("worldgen", "doOreGeneration", true)) {
            //Small Ores
            //Common Overworld Ores
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.copper", true, 30, 70, 32, true, false, false, true, true, false, Materials.Copper);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.tin", true, 10, 60, 32, true, false, false, true, true, true, Materials.Tin);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.coal", true, 30, 70, 160, true, false, false, false, false, false, Materials.Coal);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.iron", true, 30, 70, 196, true, false, false, true, true, false, Materials.Iron);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.zinc", true, 30, 70, 48, true, false, false, true, true, false, Materials.Zinc);
            //Uncommon Overworld Ores
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.lapis", true, 5, 30, 12, true, false, false, true, true, false, Materials.Lapis);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.diamond", true, 5, 15, 6, true, false, false, true, true, true, Materials.Diamond);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.emerald", true, 65, 90, 16, true, false, false, false, true, true, Materials.Emerald);
            //Common Nether Ores
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.netherquartz", true, 10, 120, 80, false, true, false, false, false, false, Materials.NetherQuartz);
            //Uncommon Nether Ores
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.gold", true, 5, 110, 16, true, true, false, true, true, true, Materials.Gold);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.bismuth", true, 80, 120, 8, false, true, false, true, true, false, Materials.Bismuth);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.firestone", true, 5, 15, 8, false, true, false, Materials.Firestone);
            //Rare Nether Ores
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.netherstar", true, 101, 115, 6, false, true, false, Materials.NetherStar);
            //TwF Ores
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.amethyst", true, 5, 35, 4, false, false, false, false, true, true, Materials.Amethyst);
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.lapis", true, 3, 35, 8, true, false, false, false, true, true, Materials.Lapis);

            //Overworld Ore Veins
            //Unlocalized name, Enabled by defaut, Min Y Spawn Height (7-242), Max Y Spawn Height (14-249), Relative Spawn Weight (1-x), Vein Density (1-10), Size (Appx. Diameter (8-32)), Spawn in Overworld?, Spawn in Nether?, Spawn in End?, Primary (Top ore layer), Secondary (Bottom ore layer), Between (Middle Ore Layer), Sporadic (Randomly placed in vein)
            //Ore veins are 7 blocks tall, Primary, Between, and Secondary overlap by 1 block. Sporadic Spawns anywhere.
            new GT_Worldgen_GT_Ore_Layer("ore.mix.coal", true, 50, 80, 130, 6, 32, true, false, false, Materials.Coal, Materials.Coal, Materials.Coal, Materials.Coal);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.magnetite", true, 55, 70, 180, 6, 32, true, false, false, Materials.Magnetite, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Magnetite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.zinc", true, 70, 80, 150, 6, 32, true, false, false, Materials.Zinc, Materials.Tin, Materials.Sphalerite, Materials.Cassiterite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.tin", true, 55, 60, 50, 4, 32, true, false, false, Materials.Tin, Materials.Zinc, Materials.Cassiterite, Materials.Chalcopyrite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.copper", true, 30, 40, 100, 5, 32, true, false, false, Materials.Copper, Materials.Chalcopyrite, Materials.Pyrite, Materials.Iron);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.owbauxite", true, 10, 30, 25, 4, 32, true, false, false, Materials.Bauxite, Materials.Bauxite, Materials.Bauxite, Materials.Bauxite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.salts", true, 40, 45, 50, 2, 24, true, false, false, Materials.Salt, Materials.Saltpeter, Materials.Spodumene, Materials.Salt);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.redstone", true, 5, 15, 60, 3, 24, true, false, false, Materials.Redstone, Materials.Ruby, Materials.Redstone, Materials.Cinnabar);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.nickel", true, 10, 40, 50, 3, 24, true, false, false, Materials.Nickel, Materials.Cobaltite, Materials.Pentlandite, Materials.Cobaltite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.manganese", true, 20, 30, 30, 2, 20, true, false, false, Materials.Spessartine, Materials.Spessartine, Materials.Tantalite, Materials.Pyrolusite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.diamond", true, 7, 20, 40, 2, 20, true, false, false, Materials.Diamond, Materials.Graphite, Materials.Graphite, Materials.Diamond);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.galena", true, 7, 45, 25, 5, 16, true, false, false, Materials.Galena, Materials.Galena, Materials.Galena, Materials.Silver);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.calcite", true, 20, 45, 40, 4, 16, true, false, false, Materials.Calcite, Materials.Apatite, Materials.Calcite, Materials.TricalciumPhosphate);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.oilsand", true, 50, 80, 60, 10, 28, true, false, false, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.alunite", true, 80, 200, 80, 3, 24, true, false, false, Materials.Alunite, Materials.Kyanite, Materials.Cassiterite, Materials.Chalcopyrite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.garnet", true, 50, 60, 45, 3, 24, true, false, false, Materials.GarnetSand, Materials.Asbestos, Materials.Asbestos, Materials.GarnetRed);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.mica", true, 20, 40, 50, 5, 24, true, false, false, Materials.Kyanite, Materials.Mica, Materials.Kyanite, Materials.Cassiterite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.quartz", true, 10, 30, 15, 5, 20, true, false, false, Materials.Quartzite, Materials.Quartzite, Materials.Quartzite, Materials.NetherQuartz);

            //Required For recipes and compat for now.
            //TODO: Remove for 1.8.x
//            new GT_Worldgen_GT_Ore_Layer("ore.mix.apatite", true, 40, 60, 40, 2, 16, false, false, false, Materials.Apatite, Materials.Apatite, Materials.TricalciumPhosphate, Materials.Pyrochlore);
//            new GT_Worldgen_GT_Ore_Layer("ore.mix.lapis", true, 5, 30, 45, 4, 16, false, false, false, Materials.Lapis, Materials.Calcite, Materials.Calcite, Materials.Lapis);

            //Twilight Forest-Only Veins
            new GT_Worldgen_GT_Ore_Layer("ore.mix.olivine", true, 10, 40, 30, 2, 16, false, false, false, Materials.Magnesium, Materials.Olivine, Materials.Olivine, Materials.Bismuth);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.aquaignis", true, 5, 35, 60, 2, 16, false, false, false, Materials.InfusedWater, Materials.InfusedFire, Materials.Amber, Materials.Cinnabar);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.terraaer", true, 5, 35, 60, 2, 16, false, false, false, Materials.InfusedEarth, Materials.InfusedAir, Materials.Amber, Materials.Cinnabar);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.perditioordo", true, 5, 35, 60, 2, 16, false, false, false, Materials.InfusedEntropy, Materials.InfusedOrder, Materials.Amber, Materials.Cinnabar);

            //Nether-Only Veins
            new GT_Worldgen_GT_Ore_Layer("ore.mix.titanium", true, 70, 90, 50, 6, 24, false, true, false, Materials.Ilmenite, Materials.Rutile, Materials.Ilmenite, Materials.Ilmenite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.nuclear",   true, 5, 50, 50, 7, 16, false, true, false, Materials.Pitchblende, Materials.Thorium, Materials.Pitchblende, Materials.Uraninite);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.firestone", true, 5, 30, 50, 3, 1, false, true, false, Materials.Firestone, Materials.Firestone, Materials.Firestone, Materials.Firestone);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.infusedgold",true, 5, 100, 50, 2, 5, false, true, false, Materials.InfusedGold, Materials.InfusedGold, Materials.InfusedGold, Materials.InfusedGold);
            new GT_Worldgen_GT_Ore_Layer("ore.mix.netherstar",true, 116, 120, 1, 1, 2, false, true, false, Materials.NetherStar, Materials.NetherStar, Materials.NetherStar, Materials.NetherStar);
            //Old nether veins.
//            new GT_Worldgen_GT_Ore_Layer("ore.mix.saltpeterelectrotine", true, 5, 45, 55, 5, 16, false, true, false, Materials.Electrotine, Materials.Saltpeter, Materials.Saltpeter, Materials.Sphalerite);
//            new GT_Worldgen_GT_Ore_Layer("ore.mix.beryllium", true, 5, 30, 30, 2, 16, false, true, false, Materials.Beryllium, Materials.Beryllium, Materials.Emerald, Materials.Thorium);
//            new GT_Worldgen_GT_Ore_Layer("ore.mix.tetrahedrite", true, 90, 120, 50, 4, 24, false, true, false, Materials.Tetrahedrite, Materials.Tetrahedrite, Materials.Stibnite, Materials.Sphalerite);
//            new GT_Worldgen_GT_Ore_Layer("ore.mix.quartz", true, 90, 120, 50, 6, 24, false, true, false, Materials.NetherQuartz, Materials.CertusQuartz, Materials.NetherQuartz, Materials.Barite);
//            new GT_Worldgen_GT_Ore_Layer("ore.mix.sulfur", true, 5, 20, 50, 4, 24, false, true, false, Materials.Sulfur, Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite);
        }

        GT_OreVeinLocations.addOreVeins(GT_Worldgen_GT_Ore_Layer.sList);

        //DO NOT DELETE V THIS V - this is needed so that gregtech generates its Ore Layer's first (the ones up there), which can then be transformed into "GT_Worldgen_GT_Ore_Layer_Space". Also Reflexion is slow.
        try {
            Class clazz = Class.forName("bloodasp.galacticgreg.WorldGenGaGT");
            Constructor constructor=clazz.getConstructor();
            Method method=clazz.getMethod("run");
            method.invoke(constructor.newInstance());
            GT_Log.out.println("Started Galactic Greg ore gen code");
            //this function calls Galactic Greg and enables its generation.
        } catch (Exception e) {
            // ClassNotFound is expected if Galactic Greg is absent, so only report if other problem
            if (!(e instanceof ClassNotFoundException)) {
                GT_Log.err.println("Unable to start Galactic Greg ore gen code");
                e.printStackTrace(GT_Log.err);
            }
        }
        //DO NOT DELETE ^ THIS ^
    }
}
