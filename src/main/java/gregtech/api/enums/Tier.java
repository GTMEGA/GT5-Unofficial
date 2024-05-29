package gregtech.api.enums;

/**
 * Experimental Class for later
 */
public class Tier {
    public static final Tier[]
            ELECTRIC = new Tier[]{
            new Tier(SubTag.ENERGY_ELECTRICITY, 0, 8, 1, 1, 1, Materials.Tin, ItemList.Hull_ULV, OrePrefixes.cableGt01.get(Materials.RedAlloy), OrePrefixes.cableGt04.get(Materials.RedAlloy)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 1, 32, 1, 1, 1, Materials.Steel, ItemList.Hull_LV, OrePrefixes.cableGt01.get(Materials.Tin), OrePrefixes.cableGt04.get(Materials.Tin)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 2, 128, 1, 1, 1, Materials.StainlessSteel, ItemList.Hull_MV, OrePrefixes.cableGt01.get(Materials.Copper), OrePrefixes.cableGt04.get(Materials.Copper)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 3, 512, 1, 1, 1, Materials.Aluminium, ItemList.Hull_HV, OrePrefixes.cableGt01.get(Materials.Gold), OrePrefixes.cableGt04.get(Materials.Gold)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 4, 2048, 1, 1, 1, Materials.Titanium, ItemList.Hull_EV, OrePrefixes.cableGt01.get(Materials.Aluminium), OrePrefixes.cableGt04.get(Materials.Aluminium)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 5, 8192, 1, 1, 1, Materials.TungstenSteel, ItemList.Hull_IV, OrePrefixes.cableGt01.get(Materials.Platinum), OrePrefixes.cableGt04.get(Materials.Platinum)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 6, 32768, 1, 1, 1, Materials.Chrome, ItemList.Hull_LuV, OrePrefixes.cableGt01.get(Materials.NiobiumTitanium), OrePrefixes.cableGt04.get(Materials.NiobiumTitanium)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 7, 131072, 1, 1, 1, Materials.Iridium, ItemList.Hull_ZPM, OrePrefixes.cableGt01.get(Materials.Naquadah), OrePrefixes.cableGt04.get(Materials.Naquadah)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 8, 524288, 1, 1, 1, Materials.Osmium, ItemList.Hull_UV, OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), OrePrefixes.wireGt01.get(Materials.SuperconductorUHV)),
            new Tier(SubTag.ENERGY_ELECTRICITY, 9, 2097152, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), OrePrefixes.wireGt04.get(Materials.SuperconductorUHV)),
            new Tier(SubTag.ENERGY_ELECTRICITY,10, 8388608, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), OrePrefixes.wireGt04.get(Materials.SuperconductorUHV)),
            new Tier(SubTag.ENERGY_ELECTRICITY,11, 33554432, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), OrePrefixes.wireGt04.get(Materials.SuperconductorUHV)),
            new Tier(SubTag.ENERGY_ELECTRICITY,12, 134217728, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), OrePrefixes.wireGt04.get(Materials.SuperconductorUHV)),
            new Tier(SubTag.ENERGY_ELECTRICITY,13, 536870912, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), OrePrefixes.wireGt04.get(Materials.SuperconductorUHV)),
            new Tier(SubTag.ENERGY_ELECTRICITY,14, 1073741824, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), OrePrefixes.wireGt04.get(Materials.SuperconductorUHV)),
            new Tier(SubTag.ENERGY_ELECTRICITY,15, Integer.MAX_VALUE-7, 1, 1, 1, Materials.Neutronium, ItemList.Hull_MAX, OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), OrePrefixes.wireGt04.get(Materials.SuperconductorUHV)),
            //READ GT_VALUES CLASS BEFORE YOU START ADDING STUFF TO TIERS 8+
    }, ROTATIONAL = new Tier[]{
            new Tier(SubTag.ENERGY_ROTATIONAL, 1, 32, 1, 1, 1, Materials.Wood, OrePrefixes.frameGt.get(Materials.Wood), OrePrefixes.stick.get(Materials.Wood), OrePrefixes.ingot.get(Materials.Wood)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 1, 32, 1, 2, 2, Materials.WoodSealed, OrePrefixes.frameGt.get(Materials.WoodSealed), OrePrefixes.stick.get(Materials.WoodSealed), OrePrefixes.ingot.get(Materials.WoodSealed)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 2, 128, 1, 1, 1, Materials.Stone, OrePrefixes.frameGt.get(Materials.Stone), OrePrefixes.stick.get(Materials.Stone), OrePrefixes.ingot.get(Materials.Stone)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 2, 128, 1, 2, 2, Materials.IronWood, OrePrefixes.frameGt.get(Materials.IronWood), OrePrefixes.stick.get(Materials.IronWood), OrePrefixes.ingot.get(Materials.IronWood)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 3, 512, 1, 1, 1, Materials.Bronze, OrePrefixes.frameGt.get(Materials.Bronze), OrePrefixes.stick.get(Materials.Bronze), OrePrefixes.ingot.get(Materials.Bronze)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 3, 512, 1, 2, 2, Materials.Brass, OrePrefixes.frameGt.get(Materials.Brass), OrePrefixes.stick.get(Materials.Brass), OrePrefixes.ingot.get(Materials.Brass)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 4, 2048, 1, 1, 1, Materials.Steel, OrePrefixes.frameGt.get(Materials.Steel), OrePrefixes.stick.get(Materials.Steel), OrePrefixes.ingot.get(Materials.Steel)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 4, 2048, 1, 2, 2, Materials.Titanium, OrePrefixes.frameGt.get(Materials.Titanium), OrePrefixes.stick.get(Materials.Titanium), OrePrefixes.ingot.get(Materials.Titanium)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 5, 8192, 1, 1, 1, Materials.TungstenSteel, OrePrefixes.frameGt.get(Materials.TungstenSteel), OrePrefixes.stick.get(Materials.TungstenSteel), OrePrefixes.ingot.get(Materials.TungstenSteel)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 6, 32768, 1, 1, 1, Materials.Iridium, OrePrefixes.frameGt.get(Materials.Iridium), OrePrefixes.stick.get(Materials.Iridium), OrePrefixes.ingot.get(Materials.Iridium)),
            new Tier(SubTag.ENERGY_ROTATIONAL, 9, Integer.MAX_VALUE-7, 1, 1, 1, Materials.Neutronium, OrePrefixes.frameGt.get(Materials.Neutronium), OrePrefixes.stick.get(Materials.Neutronium), OrePrefixes.ingot.get(Materials.Neutronium)),
    }, STEAM = new Tier[]{
            new Tier(SubTag.ENERGY_STEAM, 1, 32, 1, 1, 1, Materials.Bronze, OrePrefixes.frameGt.get(Materials.Bronze), OrePrefixes.pipeMedium.get(Materials.Bronze), OrePrefixes.pipeHuge.get(Materials.Bronze)),
            new Tier(SubTag.ENERGY_STEAM, 2, 128, 1, 1, 1, Materials.Steel, OrePrefixes.frameGt.get(Materials.Steel), OrePrefixes.pipeMedium.get(Materials.Steel), OrePrefixes.pipeHuge.get(Materials.Steel)),
            new Tier(SubTag.ENERGY_STEAM, 3, 512, 1, 1, 1, Materials.Titanium, OrePrefixes.frameGt.get(Materials.Titanium), OrePrefixes.pipeMedium.get(Materials.Titanium), OrePrefixes.pipeHuge.get(Materials.Titanium)),
            new Tier(SubTag.ENERGY_STEAM, 4, 2048, 1, 1, 1, Materials.TungstenSteel, OrePrefixes.frameGt.get(Materials.TungstenSteel), OrePrefixes.pipeMedium.get(Materials.TungstenSteel), OrePrefixes.pipeHuge.get(Materials.TungstenSteel)),
            new Tier(SubTag.ENERGY_STEAM, 5, 8192, 1, 1, 1, Materials.Iridium, OrePrefixes.frameGt.get(Materials.Iridium), OrePrefixes.pipeMedium.get(Materials.Iridium), OrePrefixes.pipeHuge.get(Materials.Iridium)),
            new Tier(SubTag.ENERGY_STEAM, 9, Integer.MAX_VALUE - 7, 1, 1, 1, Materials.Neutronium, OrePrefixes.frameGt.get(Materials.Neutronium), OrePrefixes.pipeMedium.get(Materials.Neutronium), OrePrefixes.pipeHuge.get(Materials.Neutronium)),
    };
    /**
     * Used for Crafting Recipes
     */
    public final Object mHullObject;
    public final Object mConductingObject;
    public final Object mLargerConductingObject;
    private final SubTag mType;
    private final byte mRank;
    private final long mPrimaryValue;
    private final long mSecondaryValue;
    private final long mSpeedMultiplier;
    private final long mEnergyCostMultiplier;
    private final Materials mMaterial;

    public Tier(SubTag aType, int aRank, long aPrimaryValue, long aSecondaryValue, long aSpeedMultiplier, long aEnergyCostMultiplier, Materials aMaterial, Object aHullObject, Object aConductingObject, Object aLargerConductingObject) {
        mType = aType;
        mRank = (byte) aRank;
        mPrimaryValue = aPrimaryValue;
        mSecondaryValue = aSecondaryValue;
        mSpeedMultiplier = aSpeedMultiplier;
        mEnergyCostMultiplier = Math.max(mSpeedMultiplier, aEnergyCostMultiplier);
        mMaterial = aMaterial;

        mHullObject = aHullObject;
        mConductingObject = aConductingObject;
        mLargerConductingObject = aLargerConductingObject;
    }

    public byte getRank() {
        return mRank;
    }

    public SubTag getEnergyType() {
        return mType;
    }

    public long getEnergyPrimary() {
        return mPrimaryValue;
    }

    public long getEnergySecondary() {
        return mSecondaryValue;
    }

    public long getSpeedMultiplier() {
        return mSpeedMultiplier;
    }

    public long getEnergyCostMultiplier() {
        return mEnergyCostMultiplier;
    }

    public Materials getMaterial() {
        return mMaterial;
    }
}
