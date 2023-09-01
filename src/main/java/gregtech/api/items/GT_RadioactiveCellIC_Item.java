package gregtech.api.items;


import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;


public class GT_RadioactiveCellIC_Item extends GT_RadioactiveCell_Item {

    private class ItemStackCoord {

        public ItemStack stack;

        public int x;

        public int y;

        public ItemStackCoord(ItemStack stack1, int x1, int y1) {
            this.stack = stack1;
            this.x = x1;
            this.y = y1;
        }

    }

    public final int numberOfCells;

    public final float sEnergy;

    public final int sRadiation;

    public final float sHeat;

    public final ItemStack sDepleted;

    public final boolean sMox;

    public GT_RadioactiveCellIC_Item(
            String aUnlocalized,
            String aEnglish,
            int aCellcount,
            int maxDamage,
            float aEnergy,
            int aRadiation,
            float aHeat,
            ItemStack aDepleted,
            boolean aMox
                                    ) {
        super(aUnlocalized, aEnglish, aCellcount);
        setMaxStackSize(64);
        this.maxDmg = maxDamage;
        this.numberOfCells = aCellcount;
        this.sEnergy = aEnergy;
        this.sRadiation = aRadiation;
        this.sHeat = aHeat;
        this.sDepleted = aDepleted;
        this.sMox = aMox;

    }

}
