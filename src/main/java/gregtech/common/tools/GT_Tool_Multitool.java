package gregtech.common.tools;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ToolHarvestHelper;
import gregtech.common.items.behaviors.Behaviour_Screwdriver;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import static gregtech.common.tools.GT_Tool_Scoop.sBeeHiveMaterial;

public class GT_Tool_Multitool extends GT_Tool {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 2048;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 2048;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 2048;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 4096;
    }

    @Override
    public int getBaseQuality() {
        return 4;
    }

    @Override
    public float getBaseDamage() {
        return 26F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 100000.0F;
    }

    @Override
    public String getCraftingSound() {
        return (String) GregTech_API.sSoundList.get(100);
    }

    @Override
    public String getEntityHitSound() {
        return (String) GregTech_API.sSoundList.get(234);
    }

    @Override
    public String getBreakingSound() {
        return (String) GregTech_API.sSoundList.get(0);
    }

    @Override
    public String getMiningSound() {
        return (String) GregTech_API.sSoundList.get(233);
    }

    @Override
    public boolean canBlock() {
        return false;
    }

    @Override
    public boolean isCrowbar() {
        return true;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "wrench")
                || GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "scoop")
                || GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "cutter")
                || GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "pickaxe")
                || GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "axe")
                || GT_ToolHarvestHelper.isAppropriateMaterial(aBlock, Material.piston, Material.plants, Material.leaves, Material.gourd, Material.circuits, Material.glass, Material.vine, Material.cloth, Material.clay, Material.web, Material.rock, Material.iron, Material.sand, Material.sponge, Material.ground, Material.grass, Material.carpet, Material.wood)
                || GT_ToolHarvestHelper.isAppropriateMaterial(aBlock ,sBeeHiveMaterial)
                || GT_ToolHarvestHelper.isSpecialBlock(aBlock, Blocks.hopper, Blocks.dispenser, Blocks.dropper);
    }

    @Override
    public boolean isCorrectTool(String toolClass) {
        return true;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mIconSet.mTextures[49] : Textures.ItemIcons.HANDLE_SOLDERING;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        aItem.addItemBehavior(aID, new Behaviour_Screwdriver(1, 200));
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(EnumChatFormatting.RED + aEntity.getCommandSenderName() + EnumChatFormatting.WHITE + " got atomized (by " + EnumChatFormatting.GREEN + aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE + ")");
    }
}
