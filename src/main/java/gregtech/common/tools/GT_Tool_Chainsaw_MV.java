package gregtech.common.tools;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IAOETool;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.util.GT_TreeBorker;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class GT_Tool_Chainsaw_MV extends GT_Tool_Chainsaw_LV implements IAOETool {
    @Override
    public int getToolDamagePerBlockBreak() {
        return 200;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 400;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 3200;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 800;
    }

    @Override
    public int getBaseQuality() {
        return 1;
    }

    @Override
    public float getBaseDamage() {
        return 3.5F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 3.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 2.0F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? gregtech.api.items.GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadChainsaw.mTextureIndex] : Textures.ItemIcons.POWER_UNIT_MV;
    }

    /**
     * @return
     */
    @Override
    public int getMaxAOESize() {
        return 0;
    }

    /**
     * @param stack
     * @param player
     */
    @Override
    public void onRightClick(final ItemStack stack, final EntityPlayer player) {

    }

    /**
     * @return
     */
    @Override
    public float getDamageMultiplyer() {
        return 1.25f;
    }

    /**
     * @return
     */
    @Override
    public float getSpeedReduction() {
        return 1.0f;
    }

    /**
     * @param digSpeed
     * @param stats
     * @param stack
     * @return
     */
    @Override
    public float getDigSpeed(final float digSpeed, final IToolStats stats, final ItemStack stack) {
        return digSpeed;
    }

    /**
     * @param stack
     * @param stats
     * @param damagePerBlock
     * @param timeToTakeCenter
     * @param digSpeed
     * @param world
     * @param block
     * @param x
     * @param y
     * @param z
     * @param player
     * @return
     */
    @Override
    public float onBlockDestroyed(
            final ItemStack stack,
            final IToolStats stats,
            final float damagePerBlock,
            final float timeToTakeCenter,
            final float digSpeed,
            final World world,
            final Block block,
            final int x,
            final int y,
            final int z,
            final EntityLivingBase player
                                 ) {
        if (!(player instanceof EntityPlayerMP)) {
            return 0;
        }
        var result = 0.0f;
        val borker = new GT_TreeBorker(world, x, y, z, 1, 96, 96, -1, false);
        if (borker.isValidBlock(x, y, z)) {
            borker.borkTrees(x, y, z);
        }
        for (val pos: borker.getPositions()) {
            val tX = pos[0];
            val tY = pos[1];
            val tZ = pos[2];
            val currentBlock = world.getBlock(tX, tY, tZ);
            val hardness = currentBlock.getBlockHardness(world, tX, tY, tZ) * getDamageMultiplyer();
            result += hardness * damagePerBlock;
            breakBlock(stack, stats, 0, hardness, 0, digSpeed, world, tX, tY, tZ, (EntityPlayerMP) player, currentBlock);
        }
        return result;
    }

}
