package gregtech.loaders.postload;

import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class GT_BookAndLootLoader implements Runnable {
    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding worldgenerated Chest Content.");
        if (GT_Mod.gregtechproxy.mIncreaseDungeonLoot) {
            ChestGenHooks tChest = ChestGenHooks.getInfo("bonusChest");
            tChest.setMax(tChest.getMax() + 8);
            tChest.setMin(tChest.getMin() + 4);
            tChest = ChestGenHooks.getInfo("dungeonChest");
            tChest.setMax(tChest.getMax() + 6);
            tChest.setMin(tChest.getMin() + 3);
            tChest = ChestGenHooks.getInfo("pyramidDesertyChest");
            tChest.setMax(tChest.getMax() + 8);
            tChest.setMin(tChest.getMin() + 4);
            tChest = ChestGenHooks.getInfo("pyramidJungleChest");
            tChest.setMax(tChest.getMax() + 16);
            tChest.setMin(tChest.getMin() + 8);
            tChest = ChestGenHooks.getInfo("pyramidJungleDispenser");
            tChest.setMax(tChest.getMax() + 2);
            tChest.setMin(tChest.getMin() + 1);
            tChest = ChestGenHooks.getInfo("mineshaftCorridor");
            tChest.setMax(tChest.getMax() + 4);
            tChest.setMin(tChest.getMin() + 2);
            tChest = ChestGenHooks.getInfo("villageBlacksmith");
            tChest.setMax(tChest.getMax() + 12);
            tChest.setMin(tChest.getMin() + 6);
            tChest = ChestGenHooks.getInfo("strongholdCrossing");
            tChest.setMax(tChest.getMax() + 8);
            tChest.setMin(tChest.getMin() + 4);
            tChest = ChestGenHooks.getInfo("strongholdCorridor");
            tChest.setMax(tChest.getMax() + 6);
            tChest.setMin(tChest.getMin() + 3);
            tChest = ChestGenHooks.getInfo("strongholdLibrary");
            tChest.setMax(tChest.getMax() + 16);
            tChest.setMin(tChest.getMin() + 8);
        }
        ChestGenHooks.addItem("bonusChest", new WeightedRandomChestContent(ItemList.Bottle_Purple_Drink.get(1L), 8, 16, 2));

        ChestGenHooks.addItem("dungeonChest", new WeightedRandomChestContent(ItemList.Bottle_Holy_Water.get(1L), 4, 8, 20));
        ChestGenHooks.addItem("dungeonChest", new WeightedRandomChestContent(ItemList.Bottle_Purple_Drink.get(1L), 8, 16, 80));

        ChestGenHooks.addItem("pyramidDesertyChest", new WeightedRandomChestContent(ItemList.Bottle_Holy_Water.get(1L), 4, 8, 2));

        ChestGenHooks.addItem("pyramidJungleChest", new WeightedRandomChestContent(ItemList.ZPM.getWithCharge(1L, 2147483647), 1, 1, 1));

        ChestGenHooks.addItem("pyramidJungleDispenser", new WeightedRandomChestContent(new ItemStack(Items.fire_charge, 2), 2, 8, 30));
        ChestGenHooks.addItem("pyramidJungleDispenser", new WeightedRandomChestContent(new ItemStack(Blocks.tnt, 1), 2, 8, 30));


        ChestGenHooks.addItem("villageBlacksmith", new WeightedRandomChestContent(ItemList.McGuffium_239.get(1L), 1, 1, 1));

        ChestGenHooks.addItem("strongholdCrossing", new WeightedRandomChestContent(ItemList.Bottle_Holy_Water.get(1L), 4, 8, 6));
        ChestGenHooks.addItem("strongholdCrossing", new WeightedRandomChestContent(ItemList.McGuffium_239.get(1L), 1, 1, 10));

    }
}
