package gregtech.common.items;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Utility;

import gregtech.common.blocks.GT_Block_Rubber_Log;
import lombok.val;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class GT_TreeTap extends GT_Generic_Item {
    public GT_TreeTap() {
        super("treetap",
              "Treetap",
              "Got resin on tap",
              true);

        this.setMaxDamage(20);
        this.setNoRepair();
        this.setMaxStackSize(1);
    }

    protected GT_TreeTap(String unlocalizedName, String itemName, String itemTooltip, boolean writeToLang) {
        super(unlocalizedName, itemName, itemTooltip, writeToLang);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
        if (world.isRemote) {
            return true;
        }

        Block block = world.getBlock(x, y, z);
        if (block == ItemList.Rubber_Log.getBlock()) {
            attemptExtract(entityplayer, world, x, y, z, side, null);

            itemstack.damageItem(1, entityplayer);
        }

        return true;
    }

    public static void ejectHarz(World world, int x, int y, int z, int side, int quantity) {
        double ejectX = (double)x + 0.5;
        double ejectY = (double)y + 0.5;
        double ejectZ = (double)z + 0.5;
        if (side == 2) {
            ejectZ -= 0.3;
        } else if (side == 5) {
            ejectX += 0.3;
        } else if (side == 3) {
            ejectZ += 0.3;
        } else if (side == 4) {
            ejectX -= 0.3;
        }

        for (int i = 0; i < quantity; i++) {
            EntityItem entityitem = new EntityItem(world, ejectX, ejectY, ejectZ, ItemList.Resin.get(1));
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }

    public static boolean attemptExtract(EntityPlayer entityplayer, World world, int x, int y, int z, int side, List<ItemStack> stacks) {
        if (world.isRemote) {
            return true;
        }

        int meta = world.getBlockMetadata(x, y, z);

        val blockDirection = GT_Block_Rubber_Log.Cardinal.of(meta);
        val extractSide    = ForgeDirection.getOrientation(side);

        val isExtractingOnFace = blockDirection.direction == extractSide;
        var nodeState          = GT_Block_Rubber_Log.NodeState.of(meta);

        if (nodeState == GT_Block_Rubber_Log.NodeState.NONE || !isExtractingOnFace) {
            return false;
        }

        if (!world.isRemote) {
            GT_Utility.sendSoundToPlayers(world, GregTech_API.sSoundList.get(239), 1.0F, -1.0F, x, y, z);

            switch (nodeState) {
                case DRY:
                    nodeState = GT_Block_Rubber_Log.NodeState.NONE;
                    break;
                case WET:
                    nodeState = GT_Block_Rubber_Log.NodeState.DRY;
                    break;
            }

            val newMeta = blockDirection.mask() | nodeState.mask();
            world.setBlockMetadataWithNotify(x, y, z, newMeta, 3);

            val amount = world.rand.nextInt(3) + 1;
            if (stacks != null) {
                val stack = ItemList.Resin.get(amount);

                stacks.add(stack);
            } else {
                ejectHarz(world, x, y, z, side, amount);
            }

            if (nodeState == GT_Block_Rubber_Log.NodeState.DRY) {
                val blockLog = ItemList.Rubber_Log.getBlock();
                world.scheduleBlockUpdate(x, y, z, blockLog, blockLog.tickRate(world));
            }
        }

        return true;
    }
}
