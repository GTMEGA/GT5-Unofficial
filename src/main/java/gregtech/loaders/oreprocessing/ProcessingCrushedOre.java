package gregtech.loaders.oreprocessing;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ProcessingCrushedOre implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingCrushedOre() {
		OrePrefixes.crushedCentrifuged.add(this);
        OrePrefixes.crushedPurified.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
		switch (aPrefix) {
			case crushedCentrifuged:
				GT_Values.RA.addForgeHammerRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L), 8, 24);
				//TODO: Implement a +1% per tier chance for this maceration step
				GT_ModHandler.addPulverisationRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, GT_Utility.selectItemInList(2, aMaterial.mMacerateInto, aMaterial.mOreByProducts), 1L), 10, false);
				break;
			case crushedPurified:
				GT_ModHandler.addThermalCentrifugeRecipe(GT_Utility.copyAmount(1L, aStack), new int[] {7000, 3000}, (int) Math.min(5000L, Math.abs(aMaterial.getMass() * 20L)), GT_OreDictUnificator.get(aPrefix == OrePrefixes.crushed ? OrePrefixes.crushedCentrifuged : OrePrefixes.crushedCentrifuged, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.crushedCentrifuged, GT_Utility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts), 1L));

				ItemStack tGem = GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
				if(tGem!=null){
				switch (aMaterial.mName) {
					case "Tanzanite": case "Sapphire": case "Olivine": case "GreenSapphire": case "Opal": case "Amethyst": case "Emerald": case "Ruby":
					case "Amber": case "Diamond": case "FoolsRuby": case "BlueTopaz": case "GarnetRed": case "Topaz": case "Jasper": case "GarnetYellow":
						GT_Values.RA.addSifterRecipe(GT_Utility.copyAmount(1L, aStack), new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L), GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L), tGem, tGem, tGem, GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L)}, new int[]{500, 2000, 2000, 3000, 5000, 7500}, 800, 16);
						break;
					case "Coal":
						GT_Values.RA.addSifterRecipe(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Coal, 1L), new ItemStack[]{new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 1, 0), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1L)}, new int[]{10000, 9000, 8000, 7000, 6000, 250}, 360, 16);
							break;
					default:
						GT_Values.RA.addSifterRecipe(GT_Utility.copyAmount(1L, aStack), new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L), GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L), tGem, GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L), GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L)}, new int[]{500, 2000, 2000, 3000, 5000, 7500}, 800, 16);
				}}
				break;
		default:
			break;
		}
	}
}
