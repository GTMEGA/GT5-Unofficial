package gregtech.common.tools;


import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;


public class GT_Tool_BuzzSaw extends GT_Tool_Saw {

    @Override
    public float getBaseDamage() {
        return 1.0F;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return false;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadBuzzSaw.mTextureIndex]
                            : Textures.ItemIcons.HANDLE_BUZZSAW;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 100;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 300;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.0F;
    }

    @Override
    public String getMiningSound() {
        return GregTech_API.sSoundList.get(104);
    }

    @Override
    public String getCraftingSound() {
        return GregTech_API.sSoundList.get(104);
    }

    @Override
    public String getEntityHitSound() {
        return GregTech_API.sSoundList.get(105);
    }

    @Override
    public String getBreakingSound() {
        return GregTech_API.sSoundList.get(0);
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
                EnumChatFormatting.RED + aEntity.getCommandSenderName() + EnumChatFormatting.WHITE + " got buzzed by " + EnumChatFormatting.GREEN +
                aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE);
    }

}
