package gregtech.api.util;

import gregtech.api.GregTech_API;
import lombok.val;
import lombok.var;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import static gregtech.api.enums.GT_Values.E;

//TODO: Remove this whole stupid class. It is DOG slow and we don't use this shit anyway.
@Deprecated
public final class GT_Config implements Runnable {
    public static boolean troll = false;

    public static Configuration sConfigFileIDs;

    public final Configuration mConfig;
    private final boolean isStupid;

    public GT_Config(Configuration aConfig) {
        this(aConfig, false);
    }

    public GT_Config(Configuration aConfig, boolean isStupid) {
        this.mConfig = aConfig;
        this.isStupid = isStupid;
        if (isStupid)
            return;

        mConfig.load();
        mConfig.save();
        GregTech_API.sAfterGTPreload.add(this); // in case of crash on startup
        GregTech_API.sAfterGTLoad.add(this); // in case of crash on startup
        GregTech_API.sAfterGTPostload.add(this);
    }

    @Override
    public void run() {
        save();
    }

    public void save() {
        mConfig.save();
    }

    public void load() {
        if (!isStupid)
            mConfig.load();
    }

    public static int addIDConfig(Object aCategory, String aName, int aDefault) {
        if (GT_Utility.isStringInvalid(aName))
            return aDefault;
        val tProperty = sConfigFileIDs.get(aCategory.toString().replaceAll("\\|", "."),
                                           aName.replaceAll("\\|", "."),
                                           aDefault);
        val rResult = tProperty.getInt(aDefault);
        if (!tProperty.wasRead() && GregTech_API.sPostloadFinished)
            sConfigFileIDs.save();
        return rResult;
    }

    private static String getStackConfigName(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack))
            return E;

        Object rName = GT_OreDictUnificator.getAssociation(aStack);
        if (rName != null)
            return rName.toString();
        try {
            rName = aStack.getUnlocalizedName();
            if (GT_Utility.isStringValid(rName))
                return rName.toString();
        } catch (Throwable ignored) {

        }
        var sName = aStack.getItem().toString();
        val tmp = sName.split("@");
        if (tmp.length > 0)
            sName = tmp[0];
        return sName + "." + aStack.getItemDamage();
    }

    public boolean get(Object aCategory, ItemStack aStack, boolean aDefault) {
        val aName = getStackConfigName(aStack);
        return get(aCategory, aName, aDefault);
    }

    public boolean get(Object aCategory, String aName, boolean aDefault) {
        if (isStupid || GT_Utility.isStringInvalid(aName))
            return aDefault;
        val tProperty = mConfig.get(aCategory.toString().replaceAll("\\|", "_"),
                                    (aName + "_" + aDefault).replaceAll("\\|", "_"),
                                    aDefault);
        val rResult = tProperty.getBoolean(aDefault);
        if (!tProperty.wasRead() && GregTech_API.sPostloadFinished)
            mConfig.save();
        return rResult;
    }

    public int get(Object aCategory, ItemStack aStack, int aDefault) {
        return get(aCategory, getStackConfigName(aStack), aDefault);
    }

    public int get(Object aCategory, String aName, int aDefault) {
        if (isStupid || GT_Utility.isStringInvalid(aName))
            return aDefault;
        val tProperty = mConfig.get(aCategory.toString().replaceAll("\\|", "_"),
                                         (aName + "_" + aDefault).replaceAll("\\|", "_"),
                                         aDefault);
        val rResult = tProperty.getInt(aDefault);
        if (!tProperty.wasRead() && GregTech_API.sPostloadFinished)
            mConfig.save();
        return rResult;
    }

    public int get(final String aCategory, final String aKey, final int aDefaultValue, final String... aComments) {
        if (isStupid)
            return aDefaultValue;

        val tCommentBuilder = new StringBuilder();
        for (val tComment : aComments)
            tCommentBuilder.append(tComment).append('\n');
        tCommentBuilder.append("Default: ").append(aDefaultValue);
        return mConfig.get(aCategory, aKey, aDefaultValue, tCommentBuilder.toString()).getInt();
    }

    public double get(Object aCategory, ItemStack aStack, double aDefault) {
        return get(aCategory, getStackConfigName(aStack), aDefault);
    }

    public double get(Object aCategory, String aName, double aDefault) {
        if (isStupid || GT_Utility.isStringInvalid(aName))
            return aDefault;
        val tProperty = mConfig.get(aCategory.toString().replaceAll("\\|", "_"),
                                         (aName + "_" + aDefault).replaceAll("\\|", "_"),
                                         aDefault);
        val rResult = tProperty.getDouble(aDefault);
        if (!tProperty.wasRead() && GregTech_API.sPostloadFinished)
            mConfig.save();
        return rResult;
    }

    public String get(Object aCategory, ItemStack aStack, String aDefault) {
        return get(aCategory, getStackConfigName(aStack), aDefault);
    }

    public String get(Object aCategory, String aName, String aDefault) {
        if (isStupid || GT_Utility.isStringInvalid(aName))
            return aDefault;
        val tProperty = mConfig.get(aCategory.toString().replaceAll("\\|", "_"),
                                         (aName + "_" + aDefault).replaceAll("\\|", "_"),
                                         aDefault);
        val rResult = tProperty.getString();
        if (!tProperty.wasRead() && GregTech_API.sPostloadFinished)
            mConfig.save();
        return rResult;
    }
}
