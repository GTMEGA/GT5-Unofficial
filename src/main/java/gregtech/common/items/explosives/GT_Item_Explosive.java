package gregtech.common.items.explosives;


import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


@Getter
public class GT_Item_Explosive<TierType extends Enum<TierType> & IGT_ExplosiveTier<TierType>> extends ItemBlock {

    protected final /* @NonNull  */ TierType tier;

    private final String mName;

/*     @SuppressWarnings("unchecked")
    public GT_Item_Explosive(final @NonNull Block block) {
        super(block);
        if (block instanceof GT_Block_Explosive<?>) {
            val explosive = (GT_Block_Explosive<TierType>) block;
            this.tier = explosive.getTier();
            this.mName = explosive.getUnlocalizedName();
        } else {
            this.tier = null;
            this.mName = block.getUnlocalizedName();
        }
//        this.tier = block.getTier();
//        this.mName = block.getUnlocalizedName();
    } */

    /**
     * @param block The block this item is associated with
     * @param uName The unlocalized name of this item
     */
    @SuppressWarnings("unchecked")
    public GT_Item_Explosive(final Block block, final String uName) {
        this(block, uName, block instanceof GT_Block_Explosive<?> ? ((GT_Block_Explosive<TierType>) block).getTier().asType() : null);
//        super(block);
//        String elName;
//        if (block instanceof GT_Block_Explosive<?>) {
//            val explosive = (GT_Block_Explosive<TierType>) block;
//            this.tier = explosive.getTier();
//            this.mName = tier.getULName(uName);
//            explosive.setItem(this);
//            elName = tier.getELName(eName);
//        } else {
//            this.tier = null;
//            this.mName = block.getUnlocalizedName();
//            elName = eName;
//        }
//        setMaxDamage(0);
//        setHasSubtypes(false);
//        setCreativeTab(GregTech_API.TAB_GREGTECH);
//        GT_LanguageManager.addStringLocalization(mName + ".name", elName, true);
    }

    /**
     * @param block The block this item is associated with
     * @param uName The unlocalized name of this item
     * @param tier  The tier of this explosive, note that 0 will be represented as 1 in the name
     */
    @SuppressWarnings("unchecked")
    public GT_Item_Explosive(final Block block, final String uName, final TierType tier) {
        super(block);
        this.tier = tier;
        this.mName = tier != null ? tier.getULName(uName) : block.getUnlocalizedName();
        if (block instanceof GT_Block_Explosive<?>) {
            ((GT_Block_Explosive<TierType>) block).setItem(this);
        }
        setMaxDamage(0);
        setHasSubtypes(false);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
        GT_LanguageManager.addStringLocalization(mName + ".name", tier != null ? tier.getELName() : "[Invalid]", true);
    }

    /**
     * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
     *
     * @param newName
     */
    @Override
    public ItemBlock setUnlocalizedName(final String newName) {
        return this;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     *
     * @param stack
     */
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return mName;
    }

}
