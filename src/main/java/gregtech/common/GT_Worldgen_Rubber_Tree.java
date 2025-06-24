package gregtech.common;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.world.GT_Worldgen;

import gregtech.common.blocks.GT_Block_Rubber_Log;
import lombok.val;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class GT_Worldgen_Rubber_Tree extends GT_Worldgen {
    public static final int maxHeight = 8;

    public GT_Worldgen_Rubber_Tree() {
        super("rubberTrees", GregTech_API.sWorldgenList, true);
    }

    @Override
    public boolean executeWorldgen(World world, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        BiomeGenBase biomegenbase = world.getWorldChunkManager().getBiomeGenAt(aChunkX * 16 + 16, aChunkZ * 16 + 16);
        if (biomegenbase != null && biomegenbase.biomeName != null) {
            int rubbertrees = 0;
            if (BiomeDictionary.isBiomeOfType(biomegenbase, BiomeDictionary.Type.SWAMP)) {
                rubbertrees += aRandom.nextInt(10) + 5;
            }

            if (BiomeDictionary.isBiomeOfType(biomegenbase, BiomeDictionary.Type.FOREST) || BiomeDictionary.isBiomeOfType(biomegenbase, BiomeDictionary.Type.JUNGLE)) {
                rubbertrees += aRandom.nextInt(5) + 1;
            }

            if (aRandom.nextInt(100) + 1 <= rubbertrees * 2) {
                return this.generateTree(world, aRandom, aChunkX + aRandom.nextInt(16), rubbertrees, aChunkZ + aRandom.nextInt(16));
            }
        }

        return false;
    }

    public boolean generateTree(World world, Random random, int x, int count, int z) {
        while (count > 0) {
            int y = world.getHeight() - 1;

            while (world.isAirBlock(x, y - 1, z) && y > 0) {
                y--;
            }

            if (!grow(world, x, y, z, random)) {
                count -= 3;
            }

            x += random.nextInt(15) - 7;
            z += random.nextInt(15) - 7;
            count--;
        }

        return true;
    }

    public static boolean grow(World world, int x, int y, int z, Random random) {
        Block woodBlock = ItemList.Rubber_Log.getBlock();
        Block leavesBlock = ItemList.Rubber_Leaves.getBlock();

        int treeholechance = 25;
        int height = getGrowHeight(world, x, y, z);
        if (height < 2) {
            return false;
        } else {
            height -= random.nextInt(height / 2 + 1);

            for (int cHeight = 0; cHeight < height; cHeight++) {
                world.setBlock(x, y + cHeight, z, woodBlock, 0, 3);
                if (random.nextInt(100) <= treeholechance) {
                    treeholechance -= 10;

                    val facing = GT_Block_Rubber_Log.Cardinal.values()[(random.nextInt(
                            GT_Block_Rubber_Log.Cardinal.values().length))];
                    val meta = facing.mask() | GT_Block_Rubber_Log.NodeState.WET.mask();

                    world.setBlockMetadataWithNotify(x, y + cHeight, z, meta, 3);
                } else {
                    world.setBlockMetadataWithNotify(x, y + cHeight, z, 1, 3);
                }

                if (height < 4 || height < 7 && cHeight > 1 || cHeight > 2) {
                    for (int cx = x - 2; cx <= x + 2; cx++) {
                        for (int cz = z - 2; cz <= z + 2; cz++) {
                            int c = Math.max(1, cHeight + 4 - height);
                            boolean gen = cx > x - 2 &&
                                          cx < x + 2 &&
                                          cz > z - 2 &&
                                          cz < z + 2 ||
                                          cx > x - 2 &&
                                          cx < x + 2 &&
                                          random.nextInt(c) == 0 ||
                                          cz > z - 2 &&
                                          cz < z + 2 &&
                                          random.nextInt(c) == 0;
                            if (gen && world.isAirBlock(cx, y + cHeight, cz)) {
                                world.setBlock(cx, y + cHeight, cz, leavesBlock, 0, 3);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i <= height / 4 + random.nextInt(2); i++) {
                if (world.isAirBlock(x, y + height + i, z)) {
                    world.setBlock(x, y + height + i, z, leavesBlock, 0, 3);
                }
            }

            return true;
        }
    }

    public static int getGrowHeight(World world, int x, int y, int z) {
        Block base = world.getBlock(x, y - 1, z);
        if (!base.isAir(world, x, y - 1, z)
            && base.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (IPlantable) ItemList.Rubber_Sapling.getBlock())
            && (world.isAirBlock(x, y, z) ||world.getBlock(x, y, z) == ItemList.Rubber_Sapling.getBlock())) {
            int height;
            for (height = 1; world.isAirBlock(x, y + 1, z) && height < 8; height++) {
                y++;
            }

            return height;
        } else {
            return 0;
        }
    }
}
