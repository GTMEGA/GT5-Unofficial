package gregtech.common;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.events.GT_OreVeinLocations;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import lombok.val;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import static gregtech.api.enums.GT_Values.debugOrevein;
import static gregtech.api.enums.GT_Values.debugWorldGen;
import static gregtech.api.enums.GT_Values.oreveinAttempts;
import static gregtech.api.enums.GT_Values.oreveinMaxPlacementAttempts;
import static gregtech.api.enums.GT_Values.oreveinPercentage;

public class GT_Worldgenerator implements IWorldGenerator {
    private static int mEndAsteroidProbability = 300;
    private static int mSize = 100;
    private static int endMinSize = 50;
    private static int endMaxSize = 200;
    private static boolean endAsteroids = true;
    public static List<Runnable> mList = new ArrayList<>();
    public static HashSet<Long> ProcChunks = new HashSet<Long>();
    public static Hashtable<Long, GT_Worldgen_GT_Ore_Layer> validOreveins = new Hashtable(1024);
    public boolean mIsGenerating = false;
    public static final Object listLock = new Object();


    public GT_Worldgenerator() {
        endAsteroids = GregTech_API.sWorldgenFile.get("endasteroids", "GenerateAsteroids", true);
        endMinSize = GregTech_API.sWorldgenFile.get("endasteroids", "AsteroidMinSize", 50);
        endMaxSize = GregTech_API.sWorldgenFile.get("endasteroids", "AsteroidMaxSize", 200);
        mEndAsteroidProbability = GregTech_API.sWorldgenFile.get("endasteroids", "AsteroidProbability", 300);
        GameRegistry.registerWorldGenerator(this, 1073741823);
        if (debugWorldGen) {
            GT_Log.out.println(
                            "GT_Worldgenerator created"
            );
        }
    }

    @Override
    public void generate(Random aRandom, int aX, int aZ, World aWorld, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        synchronized (listLock)
        {
            mList.add(new WorldGenContainer(new XSTR(Math.abs(aRandom.nextInt()) +1), aX, aZ, aWorld.provider.dimensionId, aWorld, aChunkGenerator, aChunkProvider, aWorld.getBiomeGenForCoords(aX * 16 + 8, aZ * 16 + 8).biomeName));
            if (debugWorldGen) {
                GT_Log.out.printf("ADD WorldSeed:%d DimId%d chunk x:%d z:%d SIZE: %d%n",
                                  aWorld.getSeed(),
                                  aWorld.provider.dimensionId,
                                  aX,
                                  aZ,
                                  mList.size());
            }
        }

        if (!this.mIsGenerating) {
            this.mIsGenerating = true;
            int mList_sS= mList.size();
            mList_sS = Math.min(mList_sS, 5); // Run a maximum of 5 chunks at a time through worldgen. Extra chunks get done later.
            for (int i = 0; i < mList_sS; i++) {
                WorldGenContainer toRun = (WorldGenContainer) mList.get(0);
                if (debugWorldGen) {
                    GT_Log.out.printf("RUN WorldSeed:%d DimId%d chunk x:%d z:%d SIZE: %d i: %d%n",
                                      aWorld.getSeed(),
                                      aWorld.provider.dimensionId,
                                      toRun.mX,
                                      toRun.mZ,
                                      mList.size(),
                                      i);
                }

                synchronized (listLock)
                {
                    mList.remove(0);
                }

                toRun.run();
            }
            this.mIsGenerating = false;
        }
    }

    public static class WorldGenContainer
            implements Runnable {
        public final Random mRandom;
        public final int mX;
        public final int mZ;
        public final int mDimensionType;
        public final World mWorld;
        public final IChunkProvider mChunkGenerator;
        public final IChunkProvider mChunkProvider;
        public final String mBiome;
        // Used for outputting orevein weights and bins
        //        static int test=0;


        // Local class to track which orevein seeds must be checked when doing chunkified worldgen
        class NearbySeeds {
            public int mX;
            public int mZ;
            NearbySeeds( int x, int z) {
                this.mX = x;
                this.mZ = z;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof GT_Worldgenerator.WorldGenContainer.NearbySeeds)) return false;
                GT_Worldgenerator.WorldGenContainer.NearbySeeds that = (GT_Worldgenerator.WorldGenContainer.NearbySeeds) o;
                if (this.mX != that.mX) return false;
                return this.mZ == that.mZ;
            }

            @Override
            public int hashCode() {
                int result = this.mX;
                result = 31 * result + this.mZ;
                return result;
            }
        };

        public static ArrayList<GT_Worldgenerator.WorldGenContainer.NearbySeeds> seedList = new ArrayList<>();

        // aX and aZ are now the by-chunk X and Z for the chunk of interest
        public WorldGenContainer(Random aRandom, int aX, int aZ, int aDimensionType, World aWorld, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider, String aBiome) {
            this.mRandom = aRandom;
            this.mX = aX;
            this.mZ = aZ;
            this.mDimensionType = aDimensionType;
            this.mWorld = aWorld;
            this.mChunkGenerator = aChunkGenerator;
            this.mChunkProvider = aChunkProvider;
            this.mBiome = aBiome;
        }

        // How to evaluate oregen distribution
        // - Enable debugOreveins
        // - Fly around for a while, or teleport jumping ~320 blocks at a time, with
        //   a 15-30s pause for worldgen to catch up
        // - Do this across a large area, at least 2000x2000 blocks for good numbers
        // - Open logs\gregtech.log
        // - Using notepad++, do a Search | Find  - enter "Added" for the search term
        // - Select Find All In Current Document
        // - In the Search window, right-click and Select All
        // - Copy and paste to a new file
        // - Delete extraneous stuff at top, and blank line at bottom.  Line count is
        //   # of total oreveins
        // - For simple spot checks, use Find All in Current Document for specific
        //   oremixes, ie ore.mix.diamond, to check how many appear in the list.
        // - For more complex work, import file into Excel, and sort based on oremix
        //   column.  Drag select the oremix names, in the bottom right will be how many
        //   entries to add in a seperate tab to calculate %ages.
        //
        // When using the ore weights, discount or remove the high altitude veins since
        // their high weight are offset by their rareness. I usually just use zero for them.
        // Actual spawn rates will vary based upon the average height of the stone layers
        // in the dimension. For example veins that range above and below the average height
        // will be less, and veins that are completely above the average height will be much less.

        public GT_OreVeinLocations.VeinData worldGenFindVein(int oreseedX, int oreseedZ) {
            // Explanation of oreveinseed implementation.
            // (long)this.mWorld.getSeed()<<16)    Deep Dark does two oregen passes, one with getSeed set to +1 the original world seed.  This pushes that +1 off the low bits of oreseedZ, so that the hashes are far apart for the two passes.
            // ((this.mWorld.provider.dimensionId & 0xffL)<<56)    Puts the dimension in the top bits of the hash, to make sure to get unique hashes per dimension
            // ((long)oreseedX & 0x000000000fffffffL) << 28)    Puts the chunk X in the bits 29-55. Cuts off the top few bits of the chunk so we have bits for dimension.
            // ( (long)oreseedZ & 0x000000000fffffffL ))    Puts the chunk Z in the bits 0-27. Cuts off the top few bits of the chunk so we have bits for dimension.
            long oreveinSeed = ((long)this.mWorld.getSeed()<<16) ^  ((long)((this.mWorld.provider.dimensionId & 0xffL)<<56) |( ((long)oreseedX & 0x000000000fffffffL) << 28) | ( (long)oreseedZ & 0x000000000fffffffL )); // Use an RNG that is identical every time it is called for this oreseed.
            XSTR oreveinRNG = new XSTR( oreveinSeed );
            int oreveinPercentageRoll = oreveinRNG.nextInt(100); // Roll the dice, see if we get an orevein here at all
            String tDimensionName = "";

            if (debugOrevein) {
                tDimensionName = this.mWorld.provider.getDimensionName();
            }

            if (debugOrevein) {
                GT_Log.out.printf(" Finding oreveins for oreveinSeed=%d mX=%d mZ=%d oreseedX=%d oreseedZ=%d worldSeed=%d%n",
                                  oreveinSeed,
                                  this.mX,
                                  this.mZ,
                                  oreseedX,
                                  oreseedZ,
                                  this.mWorld.getSeed());
            }

            GT_OreVeinLocations.VeinData oreVeinMix = null;

            // Search for a valid orevein for this dimension
            if( !validOreveins.containsKey(oreveinSeed) ) {
                if ( (oreveinPercentageRoll<oreveinPercentage) && (GT_Worldgen_GT_Ore_Layer.sWeight > 0) && (!GT_Worldgen_GT_Ore_Layer.sList.isEmpty())) {
                    int placementAttempts = 0;
                    int i;

                    for( i = 0; (i < oreveinAttempts) && (oreVeinMix == null) && (placementAttempts<oreveinMaxPlacementAttempts); i++ ) {
                        int tRandomWeight = oreveinRNG.nextInt(GT_Worldgen_GT_Ore_Layer.sWeight);

                        for (GT_Worldgen_GT_Ore_Layer tWorldGen : GT_Worldgen_GT_Ore_Layer.sList) {
                            tRandomWeight -= ( tWorldGen).mWeight;

                            if (tRandomWeight > 0) {
                                continue;
                            }

                            try {
                                // Adjust the seed so that this layer has a series of unique random numbers.  Otherwise multiple attempts at this same oreseed will get the same offset and X/Z values. If an orevein failed, any orevein with the
                                // same minimum heights would fail as well.  This prevents that, giving each orevein a unique height each pass through here.
                                val result = tWorldGen.executeWorldgenChunkified(this.mWorld, new XSTR( oreveinSeed ^ (tWorldGen.mPrimary.mMetaItemSubID)), this.mBiome, this.mDimensionType, this.mX*16, this.mZ*16, oreseedX*16, oreseedZ*16, this.mChunkGenerator, this.mChunkProvider);

                                switch (result.status) {
                                    case NO_OVERLAP:
                                    case ORE_PLACED:
                                        if (debugOrevein) {
                                            GT_Log.out.printf(" Added near oreveinSeed=%d %s tries at oremix=%d placementAttempts=%d dimensionName=%s%n",
                                                              oreveinSeed,
                                                              tWorldGen.mWorldGenName,
                                                              i,
                                                              placementAttempts,
                                                              tDimensionName);
                                        }

                                        validOreveins.put(oreveinSeed, tWorldGen);
                                        oreVeinMix = new GT_OreVeinLocations.VeinData(tWorldGen.mWorldGenName, result.blocksPlaced, result.blocksPlaced);
                                        break;
                                    case NO_OVERLAP_AIR_BLOCK:
                                        if (debugOrevein) {
                                            GT_Log.out.printf(" No overlap and air block in test spot=%d %s tries at oremix=%d placementAttempts=%d dimensionName=%s%n",
                                                              oreveinSeed,
                                                              tWorldGen.mWorldGenName,
                                                              i,
                                                              placementAttempts,
                                                              tDimensionName);
                                        }
                                    case NO_ORE_IN_BOTTOM_LAYER:
                                        placementAttempts++;
                                        // Should do retry in this case until out of chances
                                        break;
                                }
                                break; // Try the next orevein
                            } catch (Throwable e) {
                                if (debugOrevein)
                                    GT_Log.out.printf("Exception occurred on oreVein%s oreveinSeed=%d mX=%d mZ=%d oreseedX=%d oreseedZ=%d%n",
                                                      tWorldGen,
                                                      oreveinSeed,
                                                      this.mX,
                                                      this.mZ,
                                                      oreseedX,
                                                      oreseedZ);

                                e.printStackTrace(GT_Log.err);
                            }
                        }
                    }

                    // Only add an empty orevein if unable to place a vein at the oreseed chunk.
                    if ((oreVeinMix == null) && (this.mX == oreseedX) && (this.mZ == oreseedZ)){
                        if (debugOrevein) {
                            GT_Log.out.printf(" Empty oreveinSeed=%d mX=%d mZ=%d oreseedX=%d oreseedZ=%d tries at oremix=%d placementAttempts=%d dimensionName=%s%n",
                                              oreveinSeed,
                                              this.mX,
                                              this.mZ,
                                              oreseedX,
                                              oreseedZ,
                                              i,
                                              placementAttempts,
                                              tDimensionName);
                        }
                        validOreveins.put(oreveinSeed, GT_Worldgen_GT_Ore_Layer.EMPTY_VEIN );
                    }
                } else if (oreveinPercentageRoll >= oreveinPercentage) {
                    if (debugOrevein) {
                        GT_Log.out.printf(" Skipped oreveinSeed=%d mX=%d mZ=%d oreseedX=%d oreseedZ=%d RNG=%d %%=%d dimensionName=%s%n",
                                          oreveinSeed,
                                          this.mX,
                                          this.mZ,
                                          oreseedX,
                                          oreseedZ,
                                          oreveinPercentageRoll,
                                          oreveinPercentage,
                                          tDimensionName);
                    }

                    validOreveins.put(oreveinSeed, GT_Worldgen_GT_Ore_Layer.EMPTY_VEIN);
                }
            } else {
                // oreseed is located in the previously processed table
                if (debugOrevein) {
                    GT_Log.out.printf(" Valid oreveinSeed=%d Valid oreveins.size()=%d ", oreveinSeed, validOreveins.size());
                }

                GT_Worldgen_GT_Ore_Layer tWorldGen = validOreveins.get(oreveinSeed);
                oreveinRNG.setSeed(oreveinSeed ^ (tWorldGen.mPrimary.mMetaItemSubID));  // Reset RNG to only be based on oreseed X/Z and type of vein

                val result = tWorldGen.executeWorldgenChunkified(this.mWorld, oreveinRNG, this.mBiome, this.mDimensionType, this.mX * 16, this.mZ * 16, oreseedX * 16, oreseedZ * 16, this.mChunkGenerator, this.mChunkProvider);

                switch( result.status ) {
                    case NO_ORE_IN_BOTTOM_LAYER:
                        if (debugOrevein) GT_Log.out.println(" No ore in bottom layer");

                        break;
                    case NO_OVERLAP:
                        if (debugOrevein) GT_Log.out.println(" No overlap");
                }

                oreVeinMix = new GT_OreVeinLocations.VeinData(tWorldGen.mWorldGenName, result.blocksPlaced, result.blocksPlaced);
            }

            return oreVeinMix;
        }

        @Override
        public void run() {
            long startTime = System.nanoTime();
            int oreveinMaxSize;

            // Do GT_Stones and GT_small_ores oregen for this chunk
            try {
                for (GT_Worldgen tWorldGen : GregTech_API.sWorldgenList) {
                    /*
                    if (debugWorldGen) GT_Log.out.println(
                        "tWorldGen.mWorldGenName="+tWorldGen.mWorldGenName
                    );
                    */
                    tWorldGen.executeWorldgen(this.mWorld, this.mRandom, this.mBiome, this.mDimensionType, this.mX*16, this.mZ*16, this.mChunkGenerator, this.mChunkProvider);
                }
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
            long leftOverTime = System.nanoTime();

            // Determine bounding box on how far out to check for oreveins affecting this chunk
            // For now, manually reducing oreveinMaxSize when not in the Underdark for performance
            // Leave Deep Dark/Underdark max oregen at 32, instead of 64
            oreveinMaxSize=32;

            int wXbox = this.mX - (oreveinMaxSize/16);
            int eXbox = this.mX + (oreveinMaxSize/16 + 1); // Need to add 1 since it is compared using a <
            int nZbox = this.mZ - (oreveinMaxSize/16);
            int sZbox = this.mZ + (oreveinMaxSize/16 + 1);

            // Search for orevein seeds and add to the list;
            for( int x = wXbox; x < eXbox; x++ ) {
                for( int z = nZbox; z < sZbox; z++ ) {
                    // Determine if this X/Z is an orevein seed
                    if (((Math.abs(x) % 3) == 1) && ((Math.abs(z) % 3) == 1 ) ) {
                        if (debugWorldGen) GT_Log.out.println(
                            "Adding seed x="+x+
                            " z="+z
                        );
                        seedList.add( new GT_Worldgenerator.WorldGenContainer.NearbySeeds(x, z));
                    }
                }
            }

            // Now process each oreseed vs this requested chunk
            var closestSeedDistance = Long.MAX_VALUE;
            GT_OreVeinLocations.VeinData veinData = null;

            while (!seedList.isEmpty()) {
                val currentOreSeed = seedList.remove(0);

                val distFromCurrent = ((this.mX - currentOreSeed.mX) * (this.mX - currentOreSeed.mX)) +
                                      ((this.mZ - currentOreSeed.mZ) * (this.mZ - currentOreSeed.mZ));

                if (debugWorldGen) {
                    GT_Log.out.printf("Processing seed x=%d z=%d%n", currentOreSeed.mX, currentOreSeed.mZ);
                }

                val tempUsedMix = worldGenFindVein(currentOreSeed.mX, currentOreSeed.mZ);

                if (distFromCurrent < closestSeedDistance) {
                    closestSeedDistance = distFromCurrent;
                    veinData = tempUsedMix;
                }
            }

            long oregenTime = System.nanoTime();

            Chunk tChunk = this.mWorld.getChunkFromBlockCoords(this.mX << 4, this.mZ << 4);
            if (tChunk != null) {
                tChunk.isModified = true;

                if (debugWorldGen && veinData == null) {
                    for (val player : this.mWorld.playerEntities) {
                        val message = String.format("No orevein selected for chunk: %s", tChunk.getChunkCoordIntPair());

                        ((EntityPlayer) player).addChatMessage(new ChatComponentText(message));
                    }
                }

                if (veinData != null && veinData.oresPlaced > 0) {
                    GT_OreVeinLocations.recordOreVeinInChunk(tChunk, veinData);
                }
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            if (debugWorldGen) {
                GT_Log.out.printf(" Oregen took %d Leftover gen took %d Worldgen took %d nanoseconds%n",
                                  oregenTime - leftOverTime,
                                  leftOverTime - startTime,
                                  duration);
            }
        }
    }
}
