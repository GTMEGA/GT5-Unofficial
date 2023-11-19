package gregtech.common.items;


import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Network;
import gregtech.common.blocks.GT_Block_Ore;
import gregtech.common.gui.prosp_toolkit.ProspectorsToolkitContainer;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_AdvSeismicProspector;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemProspectingToolkit extends GT_Generic_Item {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ProspectorInteractionHandler {

        public static final ProspectorInteractionHandler INSTANCE = new ProspectorInteractionHandler();


        public Object getServerGUI(final EntityPlayer aPlayer) {
            val currentItem = aPlayer.inventory.getCurrentItem();
            if (currentItem != null && currentItem.getItem() instanceof ItemProspectingToolkit) {
                return new ProspectorsToolkitContainer(aPlayer, currentItem);
            }
            return null;
        }

    }

    @RequiredArgsConstructor
    @Getter
    public enum ProspectorMessage {
        CLEARED("Cleared prospecting data");


        public interface IMessage {

            String getString(final EntityPlayer player, final Object... args);

        }


        @NonNull
        private final IMessage message;

        ProspectorMessage(final String message) {
            this((player, args) -> message);
        }

        public void send(final EntityPlayer player, final Object... args) {
            String msg;
            try {
                // Throwing this into a try-catch in case some formatting somewhere does something crimge.
                // Presumably, all code run within is only related to strings, so any errors thrown should be localized.
                // Ven, please don't yell at me
                msg = getMessageUnsafe(player, args);
            } catch (final Throwable t) {
                GT_Log.err.println("Error sending message: " + t.getMessage());
                return;
            }
            GT_Utility.sendChatToPlayer(player, msg);
        }

        private String getMessageUnsafe(final EntityPlayer player, final Object[] args) {
            return getMessage().getString(player, args);
        }

    }


    public static final int MAX_SCAN_WITHOUT_ORE = 7;

    public ItemProspectingToolkit() {
        super("prospectors_toolkit", "Prospector's Toolkit", "Analyzes and stores data about ores in the world.");
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(false);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     *
     * @param stack
     * @param world
     * @param player
     */
    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        if (player.isSneaking()) {
            clearNBT(stack);
            ProspectorMessage.CLEARED.send(player);
        }
        return stack;
    }

    /**
     * This is called when the item is used, before the block is activated.
     *
     * @param stack  The Item Stack
     * @param player The Player that used the item
     * @param world  The Current World
     * @param x      Target X Position
     * @param y      Target Y Position
     * @param z      Target Z Position
     * @param side   The side of the target hit
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return Return true to prevent any further processing.
     */
    @Override
    public boolean onItemUseFirst(ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        return itemUse(stack, player, world, x, y, z);
    }

    private boolean itemUse(ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        if (player instanceof EntityPlayerMP) {
            if (player.isSneaking()) {
                if (stack.hasTagCompound()) {
                    clearNBT(stack);
                    ProspectorMessage.CLEARED.send(player);
                }
            } else if (!world.isRemote) {
                val targetBlock = world.getBlock(x, y, z);
                val targetTileEntity = world.getTileEntity(x, y, z);
                if (targetBlock != null && targetTileEntity instanceof BaseMetaTileEntity) {
                    val baseMetaTileEntity = (BaseMetaTileEntity) targetTileEntity;
                    val metaTileEntity = baseMetaTileEntity.getMetaTileEntity();
                    if (metaTileEntity instanceof GT_MetaTileEntity_AdvSeismicProspector) {
                        val advancedSeismicProspector = (GT_MetaTileEntity_AdvSeismicProspector) metaTileEntity;
                        if (advancedSeismicProspector.canBeReadFrom()) {
                            stack = doBetterProspectingLoop(baseMetaTileEntity, advancedSeismicProspector, stack, player, world);
                        }
                    }
                } else if (stack.hasTagCompound()) {
                    player.openGui(GT_Values.GT, -3, world, x, y, z);
                }
            }
        }
        return player instanceof EntityPlayerMP;
    }

    public void clearNBT(final ItemStack aStack) {
        aStack.setTagCompound(new NBTTagCompound());
    }

    private ItemStack doBetterProspectingLoop(final BaseMetaTileEntity baseMetaTileEntity, final GT_MetaTileEntity_AdvSeismicProspector advancedSeismicProspector, final ItemStack stack, final EntityPlayer player, final World world) {
//        clearNBT(stack);
        val radius = advancedSeismicProspector.getRadius();
        val xBase = baseMetaTileEntity.getXCoord();
        val zBase = baseMetaTileEntity.getZCoord();
        val xMin = xBase - radius;
        val xMax = xBase + radius;
        val zMin = zBase - radius;
        val zMax = zBase + radius;
        int bucketSize = 3;
        val buckets = new HashMap<int[], Map<Materials, Integer>>();
        var bucketCoords = new int[]{
                0,
                0
        };
        Map<Materials, Integer> bucket = null;
        var freshBucket = false;
        for (int currentX = xMin; currentX <= xMax; currentX++) {
            for (int currentZ = zMin; currentZ <= zMax; currentZ++) {
                if (currentX % bucketSize == 0 || currentZ % bucketSize == 0) {
                    if (bucket != null && !bucket.isEmpty()) {
                        buckets.put(bucketCoords, bucket);
                    }
                    freshBucket = true;
                    bucketCoords = new int[]{
                            currentX,
                            currentZ
                    };
                    bucket = new HashMap<>();
                }
                boolean foundInVerticalSlice = false;
                int verticalLayersWithoutOre = 0;
                val sliceMap = new HashMap<Materials, Integer>();
                for (int currentY = 0; currentY <= 255 && verticalLayersWithoutOre < MAX_SCAN_WITHOUT_ORE; currentY++) {
                    val block = world.getBlock(currentX, currentY, currentZ);
                    if (block == Blocks.air) {
                        continue;
                    }
                    val meta = world.getBlockMetadata(currentX, currentY, currentZ);
                    if (block instanceof GT_Block_Ore) {
                        val ore = (GT_Block_Ore) block;
                        val material = ore.getOreType();
                        sliceMap.compute(material, (key, value) -> value == null ? 1 : value + 1);
                        foundInVerticalSlice = true;
                        verticalLayersWithoutOre = 0;
                    } else if (foundInVerticalSlice) {
                        verticalLayersWithoutOre++;
                    }
                }
                if (!sliceMap.isEmpty()) {
                    val dumbJVMRestriction = bucket;
                    sliceMap.forEach((material, count) -> dumbJVMRestriction.merge(material, count, Integer::sum));
                    freshBucket = false;
                }
            }
        }
        if (!freshBucket && bucket != null && !bucket.isEmpty()) {
            buckets.put(bucketCoords, bucket);
        }
        val maxOreMap = new HashMap<int[], Materials>();
        buckets.forEach((coords, bucketMap) -> {
            val max = Collections.max(bucketMap.entrySet(), Map.Entry.comparingByValue()).getKey();
            maxOreMap.put(coords, max);
        });
        val nbt = new NBTTagCompound();
        nbt.setIntArray("xRange", new int[]{
                xMin,
                xMax
        });
        nbt.setIntArray("zRange", new int[]{
                zMin,
                zMax
        });
        nbt.setIntArray("center", new int[]{
                xBase,
                zBase
        });
        nbt.setInteger("radius", radius);
        nbt.setInteger("dimension", world.provider.dimensionId);
        val maxOres = new NBTTagCompound();
        maxOreMap.forEach((coords, material) -> {
            val key = String.format("%d,%d", coords[0], coords[1]);
            val value = material.mDefaultLocalName;
            System.out.println(key + ": " + value);
            maxOres.setString(key, value);
        });
        nbt.setTag("maxOres", maxOres);
        stack.setTagCompound(nbt);
        return stack;
    }

    /**
     * @param aWorld  The world
     * @param aX      The X Position
     * @param aY      The X Position
     * @param aZ      The X Position
     * @param aPlayer The Player that is wielding the item
     * @return
     */
    @Override
    public boolean doesSneakBypassUse(final World aWorld, final int aX, final int aY, final int aZ, final EntityPlayer aPlayer) {
        return false;
    }

    /**
     * @param aList
     * @param aStack
     * @param aPlayer
     */
    @SuppressWarnings(
            {
                    "unchecked",
                    "rawtypes"
            }
    )
    @Override
    protected void addAdditionalToolTips(final List aList, final ItemStack aStack, final EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        if (aStack == null || aList == null) {
            return;
        }
        aList.add("Use with a seismic prospector to find ore veins.");
        aList.add("Once you have gathered data, right click to view it.");
        aList.add("Shift + right click to clear the data.");
        aList.add("Re-usable.");
        val nbt = aStack.getTagCompound();
        if (nbt == null) {
            aList.add("No data stored.");
            return;
        }
        val xRange = nbt.getIntArray("xRange");
        val zRange = nbt.getIntArray("zRange");
        val center = nbt.getIntArray("center");
        val radius = nbt.getInteger("radius");
        val dimension = nbt.getInteger("dimension");
        val maxOres = nbt.getCompoundTag("maxOres");
        val xMin = xRange[0];
        val xMax = xRange[1];
        val zMin = zRange[0];
        val zMax = zRange[1];
        val xBase = center[0];
        val zBase = center[1];
        val xRangeStr = String.format("%d to %d", xMin, xMax);
        val zRangeStr = String.format("%d to %d", zMin, zMax);
        val centerStr = String.format("%d, %d", xBase, zBase);
        val radiusStr = String.format("%d", radius);
        val dimensionStr = String.format("%d", dimension);
        aList.add("X Range: " + xRangeStr);
        aList.add("Z Range: " + zRangeStr);
        aList.add("Center: " + centerStr);
        aList.add("Radius: " + radiusStr);
        aList.add("Dimension: " + dimensionStr);
        aList.add("Max Ores:");
        val maxOresKeys = maxOres.func_150296_c();
        for (val oKey : maxOresKeys) {
            val key = oKey.toString();
            val value = maxOres.getString(key);
            val coords = key.split(",");
            val x = Integer.parseInt(coords[0]);
            val z = Integer.parseInt(coords[1]);
            val ore = Materials.getMaterialsMap().get(value);
            val oreName = ore.mDefaultLocalName;
            val metaName = ore.mOreValue > 1 ? String.format(" (%d)", ore.mOreValue) : "";
            val oreStr = String.format("%s%s", oreName, metaName);
            val coordsStr = String.format("%d, %d", x, z);
            aList.add(String.format("%s: %s", coordsStr, oreStr));
        }
    }

}
