package gregtech.common.items;
import gregtech.api.GregTech_API;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_Bronze;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;
import lombok.val;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Arrays;
import java.util.HashMap;

public class GT_MachineUpgrade extends GT_Generic_Item{
    private static HashMap<Integer,Integer> map = new HashMap<>();

    static {
        map.put(118,119);//alloy smelter
        map.put(103,104);//furnace
        map.put(106,107);//alloy smelter
        map.put(9332,9331);//alloy smelter
        map.put(109,110);//alloy smelter
        map.put(112,113);//alloy smelter
        map.put(115,116);//alloy smelter
    }

    public GT_MachineUpgrade() {
        super("machineupgrade",
                "Machine Upgrade",
                "Upgrades LP machines to HP",
                true);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float subX, float subY, float subZ) {
        val te = world.getTileEntity(x,y,z);
        if (!(te instanceof BaseMetaTileEntity base) || base.isClientSide()) return false;
        val meta = base.getMetaTileEntity();
        if (!(meta instanceof GT_MetaTileEntity_BasicMachine_Bronze machine)) return false;
        val mappedId = map.get(base.getMetaTileID());
        if (mappedId == null) return false;
        int metaBlock = world.getBlockMetadata(x,y,z);
        val inv = machine.mInventory.clone();
        Arrays.fill(machine.mInventory, null);
        val out = machine.mOutputItems.clone();
        Arrays.fill(machine.mOutputItems,null);
        val outFluid = machine.mOutputFluid;
        val facing = machine.mMainFacing;
        val sideMachine = base.getFrontFacing();
        val progeress = machine.mProgresstime;
        val maxTime = machine.mMaxProgresstime;
        val steam = machine.getSteamVar();
        val active = base.isActive();
        val fake = GT_Utility.getFakePlayer(base);
        if (fake == null) return false;
        val stack = new ItemStack(GregTech_API.sBlockMachines.getItem(world,x,y,z));
        stack.setItemDamage(mappedId);
        if (!(stack.getItem() instanceof GT_Item_Machines machineItem)) return false;
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x,y,z, world, world.getBlock(x,y,z), metaBlock, player);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return false;
        if (!world.setBlockToAir(x,y,z)) return false;
        machineItem.placeBlockAt(stack,player,world,x,y,z,side,subX,subY,subZ,metaBlock);
        val newTe = world.getTileEntity(x,y,z);
        if (!(newTe instanceof BaseMetaTileEntity newBase)) return false;//uhh something went wrong we should drop all valuables
        val newMeta = newBase.getMetaTileEntity();
        if (!(newMeta instanceof GT_MetaTileEntity_BasicMachine_Bronze newMachine)) return false;//uhh something went wrong we should drop all valuables
        System.arraycopy(inv,0,newMachine.mInventory,0,Math.min(inv.length,newMachine.mInventory.length));
        System.arraycopy(out,0,newMachine.mOutputItems,0,Math.min(out.length,newMachine.mOutputItems.length));
        newMachine.mOutputFluid = outFluid;
        newMachine.mMainFacing = facing;
        newMachine.mProgresstime = progeress/2;
        newMachine.mMaxProgresstime = maxTime/2;
        newMachine.setSteamVar(steam);
        newBase.setActive(active);
        newBase.setFrontFacing(sideMachine);
        --itemStack.stackSize;
        return true;
    }
}
