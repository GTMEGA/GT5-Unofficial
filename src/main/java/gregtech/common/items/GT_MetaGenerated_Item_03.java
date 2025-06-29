package gregtech.common.items;

import cpw.mods.fml.common.eventhandler.Event;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.items.GT_MetaGenerated_Item_X32;
import gregtech.api.render.TextureFactory;
import gregtech.common.covers.GT_Cover_SolarPanel;
import lombok.*;

import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UEV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UHV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UIV;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class GT_MetaGenerated_Item_03
        extends GT_MetaGenerated_Item_X32 {
    public static GT_MetaGenerated_Item_03 INSTANCE;

    public GT_MetaGenerated_Item_03() {
        super("metaitem.03", OrePrefixes.crateGtDust, OrePrefixes.crateGtIngot, OrePrefixes.crateGtGem, OrePrefixes.crateGtPlate);
        INSTANCE = this;
        int tLastID = 0;
        Object[] o = new Object[0];

//        ItemList.Circuit_Parts_PetriDish.set(addItem(tLastID = 22, "Petri Dish", "For cultivating cells", o));

        val registerCircuitEvent = new RegisterCircuitEvent();
        EVENT_BUS.post(registerCircuitEvent);

//        ItemList.Tube_Wires.set(addItem(tLastID = 110, "Tube Wires", "For the Vacuum Tubes", o));

        ItemList.Cover_SolarPanel_UHV.set(addItem(tLastID = 130, "Solar Panel (UHV)", "Ultimate High Voltage Solar Panel (Needs cleaning with right click)", new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L), new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L), new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 128L)));
        ItemList.Cover_SolarPanel_UEV.set(addItem(tLastID = 131, "Solar Panel (UEV)", "Ultimate Extreme Voltage Solar Panel (Needs cleaning with right click)", new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L), new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256L), new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 256L)));
        ItemList.Cover_SolarPanel_UIV.set(addItem(tLastID = 132, "Solar Panel (UIV)", "Ultimate Insane Voltage Solar Panel (Needs cleaning with right click)", new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L), new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 512L), new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 512L)));

        GregTech_API.registerCover(ItemList.Cover_SolarPanel_UHV.get(1L), TextureFactory.of(SOLARPANEL_UHV), new GT_Cover_SolarPanel(2097152));
        GregTech_API.registerCover(ItemList.Cover_SolarPanel_UEV.get(1L), TextureFactory.of(SOLARPANEL_UEV), new GT_Cover_SolarPanel(8388608));
        GregTech_API.registerCover(ItemList.Cover_SolarPanel_UIV.get(1L), TextureFactory.of(SOLARPANEL_UIV), new GT_Cover_SolarPanel(33554432));

        ItemList.ULV_Coil.set(addItem(tLastID = 140, "Magnetic Coil ULV", "Red Alloy Coil", o));
        ItemList.LV_Coil.set(addItem(tLastID = 141, "Magnetic Coil LV", "Cupronickel Coil", o));
        ItemList.MV_Coil.set(addItem(tLastID = 142, "Magnetic Coil MV", "Nichrome Coil", o));
        ItemList.HV_Coil.set(addItem(tLastID = 143, "Magnetic Coil HV", "Kanthal Coil", o));
        ItemList.EV_Coil.set(addItem(tLastID = 144, "Magnetic Coil EV", "Tungstensteel Coil", o));
        ItemList.IV_Coil.set(addItem(tLastID = 145, "Magnetic Coil IV", "Iridium Coil", o));
        ItemList.LuV_Coil.set(addItem(tLastID = 146, "Magnetic Coil LuV", "Osmiridium Coil", o));
        ItemList.ZPM_Coil.set(addItem(tLastID = 147, "Magnetic Coil ZPM", "Europium Coil", o));
        ItemList.UV_Coil.set(addItem(tLastID = 148, "Magnetic Coil UV", "Fluxed Coil", o));
        ItemList.UHV_Coil.set(addItem(tLastID = 149, "Magnetic Coil UHV", "Tritanium Coil", o));
    }

    @Override
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return aDoShowAllItems;
    }

    // Cancel to prevent the creation of circuit items, will leave empty values in ItemList that *must* be filled by whatever cancles it.
    public class RegisterCircuitEvent extends Event {
        @Override
        public boolean isCancelable() {
            return true;
        }
    }
}
