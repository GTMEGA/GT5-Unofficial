package gregtech.common;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import lombok.NonNull;
import lombok.val;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.ArrayList;
import java.util.Random;

import static gregtech.common.blocks.GT_Block_Ore_Abstract.OreSize;
import static gregtech.api.enums.GT_Values.debugOrevein;
import static gregtech.api.enums.GT_Values.oreveinPlacerOres;
import static gregtech.api.enums.GT_Values.oreveinPlacerOresMultiplier;

public class GT_Worldgen_GT_Ore_Layer extends GT_Worldgen {
    public static ArrayList<GT_Worldgen_GT_Ore_Layer> sList = new ArrayList<>();
    public static int sWeight = 0;
    public final short mMinY;
    public final short mMaxY;
    public final short mWeight;
    public final short mDensity;
    public final short mSize;
    public final Materials mPrimary;
    public final Materials mSecondary;
    public final Materials mBetween;
    public final Materials mSporadic;
    //public final String mBiome;
    public final String mRestrictBiome;
    public final boolean mOverworld;
    public final boolean mNether;
    public final boolean mEnd;
    public final boolean mEndAsteroid;

    public final String aTextWorldgen = "worldgen.";

    public GT_Worldgen_GT_Ore_Layer(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, boolean aOverworld, boolean aNether, boolean aEnd, Materials aPrimary, Materials aSecondary, Materials aBetween, Materials aSporadic) {
        super(aName, sList, aDefault);
        this.mOverworld = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Overworld", aOverworld);
        this.mNether = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Nether", aNether);
        this.mEnd = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "TheEnd", aEnd);
        this.mEndAsteroid = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "EndAsteroid", aEnd);
        this.mMinY = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MinHeight", aMinY));
        short mMaxY = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MaxHeight", aMaxY));
        if (mMaxY < (this.mMinY + 9))    {
            GT_Log.out.println(
                    "Oremix " + this.mWorldGenName +
                    " has invalid Min/Max heights!"
                );
            mMaxY = (short) (this.mMinY + 9);
        }
        this.mMaxY = mMaxY;
        this.mWeight = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "RandomWeight", aWeight));
        this.mDensity = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Density", aDensity));
        this.mSize = ((short) Math.max(1, GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Size", aSize)));

        short tPrimaryMeta = (short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName,
                                                                    "OrePrimaryLayer",
                                                                    aPrimary.mMetaItemSubID);
        this.mPrimary = tPrimaryMeta > 0 ? GregTech_API.sGeneratedMaterials[tPrimaryMeta] : null;

        short tSecondaryMeta = (short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName,
                                                                      "OreSecondaryLayer",
                                                                      aSecondary.mMetaItemSubID);
        this.mSecondary = tSecondaryMeta > 0 ? GregTech_API.sGeneratedMaterials[tSecondaryMeta] : null;

        short tBetweenMeta = (short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName,
                                                                    "OreSporadiclyInbetween",
                                                                    aBetween.mMetaItemSubID);
        this.mBetween = tBetweenMeta > 0 ? GregTech_API.sGeneratedMaterials[tBetweenMeta] : null;

        short tSporadicMeta = (short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName,
                                                                     "OreSporaticlyAround",
                                                                     aSporadic.mMetaItemSubID);
        this.mSporadic = tSporadicMeta > 0 ? GregTech_API.sGeneratedMaterials[tSporadicMeta] : null;

        this.mRestrictBiome = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName,
                                                             "RestrictToBiomeName",
                                                             "None");
        
        if (this.mEnabled) {
            sWeight += this.mWeight;
        }
    }

    @Override
    public WorldGenResult executeWorldgenChunkified(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, int aSeedX, int aSeedZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        if( mWorldGenName.equals("NoOresInVein") ) {
            if (debugOrevein) GT_Log.out.println(
                            " NoOresInVein"
            );
            // This is a special empty orevein
            new WorldGenResult(WorldGenStatus.ORE_PLACED, 0);
        }
        if (!isGenerationAllowed(aWorld, aDimensionType, ((aDimensionType == -1) && (this.mNether)) || ((aDimensionType == 0) && (this.mOverworld)) || ((aDimensionType == 1) && (this.mEnd)) ? aDimensionType : aDimensionType ^ 0xFFFFFFFF)) {
            /* // Debug code, but spams log
            if (debugOrevein) {
                GT_Log.out.println(
                    "Wrong dimension"
                );
            }
            */
            return new WorldGenResult(WorldGenStatus.WRONG_DIMENSION, 0);
        }
        /*if (!((aWorld.provider.getDimensionName().equalsIgnoreCase("Overworld")) || (aWorld.provider.getDimensionName().equalsIgnoreCase("Nether"))||(aWorld.provider.getDimensionName().equalsIgnoreCase("Underdark"))||(aWorld.provider.getDimensionName().equalsIgnoreCase("Twilight Forest"))||(aWorld.provider.getDimensionName().equalsIgnoreCase("Underdark"))||(aWorld.provider.getDimensionName().equalsIgnoreCase("The End"))))
        	return WRONG_DIMENSION;*/
        
        if (!this.mRestrictBiome.equals("None") && !(this.mRestrictBiome.equals(aBiome))) {
            return new WorldGenResult(WorldGenStatus.WRONG_BIOME, 0);
        }
        // For optimal performance, this should be done upstream. Meh
        String tDimensionName = aWorld.provider.getDimensionName();
        boolean isUnderdark = tDimensionName.equals("Underdark");
        
        int[] placeCount=new int[4];

        int tMinY = mMinY + aRandom.nextInt(mMaxY - mMinY - 5);
        // Determine West/East ends of orevein
        int wXVein = aSeedX - aRandom.nextInt(mSize);        // West side
        int eXVein = aSeedX + 16 + aRandom.nextInt(mSize);
        // Limit Orevein to only blocks present in current chunk
        int wX = Math.max( wXVein, aChunkX + 2);  // Bias placement by 2 blocks to prevent worldgen cascade.
        int eX = Math.min( eXVein, aChunkX + 2 + 16);

        // Get a block at the center of the chunk and the bottom of the orevein.
        Block tBlock = aWorld.getBlock(aChunkX + 7, tMinY, aChunkZ + 9);

        if (wX >= eX) {  //No overlap between orevein and this chunk exists in X
            if (tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, Blocks.stone) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, Blocks.netherrack) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, Blocks.end_stone) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, GregTech_API.sBlockGranites) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, GregTech_API.sBlockStones) ) {
                // Didn't reach, but could have placed. Save orevein for future use.
                return new WorldGenResult(WorldGenStatus.NO_OVERLAP, 0);
            } else {
                // Didn't reach, but couldn't place in test spot anywys, try for another orevein
                return new WorldGenResult(WorldGenStatus.NO_OVERLAP_AIR_BLOCK, 0);
            }
        }
        // Determine North/Sound ends of orevein
        int nZVein = aSeedZ - aRandom.nextInt(mSize);
        int sZVein = aSeedZ + 16 + aRandom.nextInt(mSize);
        
        int nZ = Math.max(nZVein, aChunkZ + 2);  // Bias placement by 2 blocks to prevent worldgen cascade.
        int sZ = Math.min(sZVein, aChunkZ + 2 + 16);
        if (nZ >= sZ) { //No overlap between orevein and this chunk exists in Z
            if (tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, Blocks.stone) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, Blocks.netherrack) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, Blocks.end_stone) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, GregTech_API.sBlockGranites) ||
                tBlock.isReplaceableOreGen(aWorld, aChunkX+7, tMinY, aChunkZ + 9, GregTech_API.sBlockStones) ) {
                // Didn't reach, but could have placed. Save orevein for future use.
                return new WorldGenResult(WorldGenStatus.NO_OVERLAP, 0);
            } else {
                // Didn't reach, but couldn't place in test spot anywys, try for another orevein
                return  new WorldGenResult(WorldGenStatus.NO_OVERLAP_AIR_BLOCK, 0);
            }
        }

        if (debugOrevein) {
            GT_Log.out.printf("Trying Orevein:%s Dimension=%s mX=%d mZ=%d oreseedX=%d oreseedZ=%d cY=%d",
                              this.mWorldGenName,
                              tDimensionName,
                              aChunkX / 16,
                              aChunkZ / 16,
                              aSeedX / 16,
                              aSeedZ / 16,
                              tMinY);
        }
        // Adjust the density down the more chunks we are away from the oreseed.  The 5 chunks surrounding the seed should always be max density due to truncation of Math.sqrt().
        int localDensity = (int) Math.max(1, ((((float) this.mDensity) * 1.25f /*to compensate for removing 2 layers*/) / (Math.sqrt(2 + Math.pow(aChunkX/16f - aSeedX/16f, 2) + Math.pow(aChunkZ/16f - aSeedZ/16f, 2)))));

        // To allow for early exit due to no ore placed in the bottom layer (probably because we are in the sky), unroll 1 pass through the loop
        // Now we do bottom-level-first oregen, and work our way upwards.
        // Layer 1 Secondary and Sporadic
        int level = tMinY - 1; //Dunno why, but the first layer is actually played one below tMinY.  Go figure.
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSecondary != null) ) {
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSecondary, isUnderdark, OreSize.Normal)) {
                        placeCount[1]++;
                    }
                }
                else if ((aRandom.nextInt(7) == 0) &&
                        ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) &&
                        (this.mSporadic != null)) {  // Sporadics are reduce by 1/7 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSporadic, isUnderdark, OreSize.Normal))
                        placeCount[3]++;
                }
            }
        }

        if ((placeCount[1]+placeCount[3])==0) {
            if (debugOrevein) GT_Log.out.println(
                " No ore in bottom layer"
            );
            return new WorldGenResult(WorldGenStatus.NO_ORE_IN_BOTTOM_LAYER, 0);  // Exit early, didn't place anything in the bottom layer
        }
        // Layers 2 Secondary and Sporadic
        for (level = tMinY; level < tMinY+1; level++) {
            for (int tX = wX; tX < eX; tX++) {
                int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
                for (int tZ = nZ; tZ < sZ; tZ++) {
                    int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                    if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSecondary != null) ) {
                        if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSecondary, isUnderdark, OreSize.Normal)) {
                            placeCount[1]++;
                        }
                    }
                    else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadic != null) ) {  // Sporadics are reduce by 1/7 to compensate
                        if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSporadic, isUnderdark, OreSize.Normal))
                            placeCount[3]++;
                    }
                }
            }
        }
        // Layer 3 is Secondary, in-between, and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mBetween != null) ) {  // Between are reduce by 1/2 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mBetween, isUnderdark, OreSize.Normal)) {
                        placeCount[2]++;
                    }
                }
                else if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSecondary != null) ) {
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSecondary, isUnderdark, OreSize.Normal)) {
                        placeCount[1]++;
                    }
                }
                else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadic != null) ) {  // Sporadics are reduce by 1/7 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSporadic, isUnderdark, OreSize.Normal))
                        placeCount[3]++;
                }
            }
        }
        level++; // Increment level to next layer
        // Layer 4 is In-between, and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mBetween != null) ) {  // Between are reduce by 1/2 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mBetween, isUnderdark, OreSize.Normal)) {
                        placeCount[2]++;
                    }
                }
                else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadic != null) ) {  // Sporadics are reduce by 1/7 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSporadic, isUnderdark, OreSize.Normal))
                        placeCount[3]++;
                }
            }
        }
        level++; // Increment level to next layer
        // Layer 5 is In-between, Primary and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mBetween != null) ) {  // Between are reduce by 1/2 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mBetween, isUnderdark, OreSize.Normal)) {
                        placeCount[2]++;
                    }
                }
                else if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimary != null) ) {
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mPrimary, isUnderdark, OreSize.Normal)) {
                        placeCount[1]++;
                    }
                }
                else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadic != null) ) {  // Sporadics are reduce by 1/7 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSporadic, isUnderdark, OreSize.Normal))
                        placeCount[3]++;
                }
            }
        }
        level++; // Increment level to next layer
        // Layer 6 is , Primary and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimary != null)) {
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mPrimary, isUnderdark, OreSize.Normal)) {
                        placeCount[1]++;
                    }
                }
                else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadic != null) ) {  // Sporadics are reduce by 1/7 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSporadic, isUnderdark, OreSize.Normal))
                        placeCount[3]++;
                }
            }
        }
        level++; // Increment level to next layer
        // Layer 7 is Primary and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimary != null) ) {
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mPrimary, isUnderdark, OreSize.Normal)) {
                        placeCount[1]++;
                    }
                }
                else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadic != null) ) {  // Sporadics are reduce by 1/7 to compensate
                    if (GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, level, tZ, this.mSporadic, isUnderdark, OreSize.Normal))
                        placeCount[3]++;
                }
            }
        }

        //Place small ores for the vein
        if( oreveinPlacerOres ) {
            int nSmallOres = (eX-wX)*(sZ-nZ)*this.mDensity/10 * oreveinPlacerOresMultiplier;
            //Small ores are placed in the whole chunk in which the vein appears.
            for( int nSmallOresCount = 0; nSmallOresCount < nSmallOres; nSmallOresCount++) {
                int tX = aRandom.nextInt(16) + aChunkX + 2;
                int tZ = aRandom.nextInt(16) + aChunkZ + 2;
                int tY = aRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mPrimary != null) {
                    GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, tY, tZ, this.mPrimary, isUnderdark, OreSize.Small);
                }

                tX = aRandom.nextInt(16) + aChunkX + 2;
                tZ = aRandom.nextInt(16) + aChunkZ + 2;
                tY = aRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mSecondary != null) {
                    GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, tY, tZ, this.mSecondary, isUnderdark, OreSize.Small);
                }

                tX = aRandom.nextInt(16) + aChunkX + 2;
                tZ = aRandom.nextInt(16) + aChunkZ + 2;
                tY = aRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mBetween != null) {
                    GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, tY, tZ, this.mBetween, isUnderdark, OreSize.Small);
                }

                tX = aRandom.nextInt(16) + aChunkX + 2;
                tZ = aRandom.nextInt(16) + aChunkZ + 2;
                tY = aRandom.nextInt(190) + 10; // Y height can vary from 10 to 200 for small ores.
                if (this.mSporadic != null) {
                    GT_Block_Ore_Abstract.setOreBlock(aWorld, tX, tY, tZ, this.mSporadic, isUnderdark, OreSize.Small);
                }
            }
        }

        if (debugOrevein) {
            GT_Log.out.printf(" wXVein%d eXVein%d nZVein%d sZVein%d locDen=%d Den=%s Sec=%d Spo=%d Bet=%d Pri=%d%n",
                              wXVein,
                              eXVein,
                              nZVein,
                              sZVein,
                              localDensity,
                              this.mDensity,
                              placeCount[1],
                              placeCount[3],
                              placeCount[2],
                              placeCount[0]);
        }

        val oresPlaced = placeCount[0] + placeCount[1] + placeCount[2] + placeCount[3];
        // Something (at least the bottom layer must have 1 block) must have been placed, return true
        return new WorldGenResult(WorldGenStatus.ORE_PLACED, oresPlaced);
    }

    public boolean containsMaterial(@NonNull Materials material) {
        if (this.mPrimary != null && material.mName.equals(this.mPrimary.mName)) {
            return true;
        }

        if (this.mSecondary != null && material.mName.equals(this.mSecondary.mName)) {
            return true;
        }

        if (this.mBetween != null && material.mName.equals(this.mBetween.mName)) {
            return true;
        }

        if (this.mSporadic != null && material.mName.equals(this.mSporadic.mName)) {
            return true;
        }

        return false;
    }
}
