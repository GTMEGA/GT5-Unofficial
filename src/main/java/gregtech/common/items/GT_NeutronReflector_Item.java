package gregtech.common.items;


import gregtech.api.items.GT_Generic_Item;
import net.minecraft.item.ItemStack;


public class GT_NeutronReflector_Item extends GT_Generic_Item {

    public GT_NeutronReflector_Item(String aUnlocalized, String aEnglish, int aMaxDamage) {
        super(aUnlocalized, aEnglish, "Undestructable");
        this.setMaxStackSize(64);
        this.setMaxDamage(aMaxDamage);
    }

    /* @Override
    public void processChamber(IReactor aReactor, ItemStack aStack, int x, int y, boolean aHeatRun) {
    } */

    /* @Override
    public boolean acceptUraniumPulse(
            IReactor reactor, ItemStack yourStack, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun
                                     ) {
        if (!heatrun) {
            ((IReactorComponent) pulsingStack.getItem()).acceptUraniumPulse(reactor, pulsingStack, yourStack, pulseX, pulseY, youX, youY, heatrun);
        }
        return true;
    } */

    /* @Override
    public boolean canStoreHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return false;
    } */

    /* @Override
    public int getMaxHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return 0;
    } */

    /* @Override
    public int getCurrentHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return 0;
    } */

    /* @Override
    public int alterHeat(IReactor aReactor, ItemStack aStack, int x, int y, int aHeat) {
        return aHeat;
    } */

    /* @Override
    public float influenceExplosion(IReactor aReactor, ItemStack aStack) {
        return -1.0F;
    } */

}
