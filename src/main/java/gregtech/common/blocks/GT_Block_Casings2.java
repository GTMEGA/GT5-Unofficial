package gregtech.common.blocks;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_Block_Casings2 extends GT_Block_Casings_Abstract {
    public GT_Block_Casings2() {
        super(GT_Item_Casings2.class, "gt.blockcasings2", GT_Material_Casings.INSTANCE);
        for (int i = 0; i < 16; i++) {
            if (i != 6) {
                Textures.BlockIcons.casingTexturePages[0][(i + 16)] = TextureFactory.of(this, i);
            }
        }
        //Special handler for Pyrolyse Oven Casing on hatches...
        Textures.BlockIcons.casingTexturePages[0][22] = TextureFactory.of(Block.getBlockFromItem(ItemList.Casing_ULV.get(1).getItem()), 0, ForgeDirection.UNKNOWN, Dyes.MACHINE_METAL.mRGBa);

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Solid Steel Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Lightweight Aluminium Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Bronze Gear Box Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Steel Gear Box Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Stainless Steel Gear Box Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Assembling Line Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Processor Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Data Drive Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Containment Field Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Assembler Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Pump Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Motor Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Brass Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Steel Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Aluminium Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Titanium Pipe Casing");
        ItemList.Casing_SolidSteel.set(new ItemStack(this, 1, 0));
        ItemList.Casing_FrostProof.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Gearbox_Bronze.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Gearbox_Steel.set(new ItemStack(this, 1, 3));
        ItemList.Casing_Gearbox_Titanium.set(new ItemStack(this, 1, 4));
        ItemList.Casing_Gearbox_TungstenSteel.set(new ItemStack(this, 1, 5));
        ItemList.Casing_Processor.set(new ItemStack(this, 1, 6));
        ItemList.Casing_DataDrive.set(new ItemStack(this, 1, 7));
        ItemList.Casing_ContainmentField.set(new ItemStack(this, 1, 8));
        ItemList.Casing_Assembler.set(new ItemStack(this, 1, 9));
        ItemList.Casing_Pump.set(new ItemStack(this, 1, 10));
        ItemList.Casing_Motor.set(new ItemStack(this, 1, 11));
        ItemList.Casing_Pipe_Bronze.set(new ItemStack(this, 1, 12));
        ItemList.Casing_Pipe_Steel.set(new ItemStack(this, 1, 13));
        ItemList.Casing_Pipe_Titanium.set(new ItemStack(this, 1, 14));
        ItemList.Casing_Pipe_TungstenSteel.set(new ItemStack(this, 1, 15));
        
}

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
            case 1:
                return Textures.BlockIcons.MACHINE_CASING_FROST_PROOF.getIcon();
            case 2:
                return Textures.BlockIcons.MACHINE_CASING_GEARBOX_BRONZE.getIcon();
            case 3:
                return Textures.BlockIcons.MACHINE_CASING_GEARBOX_STEEL.getIcon();
            case 4:
                return Textures.BlockIcons.MACHINE_CASING_GEARBOX_TITANIUM.getIcon();
            case 5:
                return Textures.BlockIcons.MACHINE_CASING_GEARBOX_TUNGSTENSTEEL.getIcon();
            case 6:
                return Textures.BlockIcons.MACHINE_CASING_PROCESSOR.getIcon();
            case 7:
                return Textures.BlockIcons.MACHINE_CASING_DATA_DRIVE.getIcon();
            case 8:
                return Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_FIELD.getIcon();
            case 9:
                return Textures.BlockIcons.MACHINE_CASING_ASSEMBLER.getIcon();
            case 10:
                return Textures.BlockIcons.MACHINE_CASING_PUMP.getIcon();
            case 11:
                return Textures.BlockIcons.MACHINE_CASING_MOTOR.getIcon();
            case 12:
                return Textures.BlockIcons.MACHINE_CASING_PIPE_BRONZE.getIcon();
            case 13:
                return Textures.BlockIcons.MACHINE_CASING_PIPE_STEEL.getIcon();
            case 14:
                return Textures.BlockIcons.MACHINE_CASING_PIPE_TITANIUM.getIcon();
            case 15:
                return Textures.BlockIcons.MACHINE_CASING_PIPE_TUNGSTENSTEEL.getIcon();
        }
        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    @Override
    public float getExplosionResistance(Entity aTNT, World aWorld, int aX, int aY, int aZ, double eX, double eY, double eZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ) == 8 ? Blocks.bedrock.getExplosionResistance(aTNT) : super.getExplosionResistance(aTNT, aWorld, aX, aY, aZ, eX, eY, eZ);
    }
}
