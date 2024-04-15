package gregtech.common.sf_logic;


import net.minecraft.item.ItemStack;


public interface ISF_Logic<FilterDataType extends ISF_Data<FilterDataType>> {

    boolean doesStackMatch(final FilterDataType filterData, final ItemStack stack);

}
