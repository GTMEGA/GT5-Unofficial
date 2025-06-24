package gregtech.api.damagesources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

import javax.annotation.Nullable;

public class GT_DamageSources {
    public static DamageSource getElectricDamage() {
        return new DamageSourceElectric();
    }

    public static DamageSource getExplodingDamage() {
        return new DamageSourceExploding();
    }

    public static DamageSource getCombatDamage(String aType, EntityLivingBase aPlayer, IChatComponent aDeathMessage) {
        return new DamageSourceCombat(aType, aPlayer, aDeathMessage);
    }

    public static DamageSource getHeatDamage() {
        return new DamageSourceHeat();
    }

    public static DamageSource getFrostDamage() {
        return new DamageSourceFrost();
    }

    private static class DamageSourceElectric extends DamageSource {
        public DamageSourceElectric() {
            super("electric");
            setDifficultyScaled();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return new ChatComponentText(EnumChatFormatting.RED + aTarget.getCommandSenderName() + EnumChatFormatting.WHITE + " was shocked");
        }
    }

    private static class DamageSourceCombat extends EntityDamageSource {
        private final IChatComponent mDeathMessage;

        public DamageSourceCombat(String aType, EntityLivingBase aPlayer, IChatComponent aDeathMessage) {
            super(aType, aPlayer);
            mDeathMessage = aDeathMessage;
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return mDeathMessage == null ? super.func_151519_b(aTarget) : mDeathMessage;
        }
    }

    private static class DamageSourceFrost extends DamageSource {
        public DamageSourceFrost() {
            super("frost");
            setDifficultyScaled();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return new ChatComponentText(EnumChatFormatting.RED + aTarget.getCommandSenderName() + EnumChatFormatting.WHITE + " got frozen");
        }
    }

    private static class DamageSourceHeat extends DamageSource {
        public DamageSourceHeat() {
            super("steam");
            setDifficultyScaled();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return new ChatComponentText(EnumChatFormatting.RED + aTarget.getCommandSenderName() + EnumChatFormatting.WHITE + " was boiled alive");
        }
    }

    public static class DamageSourceHotItem extends DamageSourceHeat {
        @Nullable
        private final ItemStack stack;

        public DamageSourceHotItem(@Nullable ItemStack cause) {
            this.stack = cause;
        }

        @Nullable
        public ItemStack getDamagingStack() {
            return stack;
        }
    }

    public static class DamageSourceExploding extends DamageSource {
        public DamageSourceExploding() {
            super("exploded");
            setDamageAllowedInCreativeMode();
            setDamageBypassesArmor();
            setDamageIsAbsolute();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return new ChatComponentText(EnumChatFormatting.RED + aTarget.getCommandSenderName() + EnumChatFormatting.WHITE + " exploded");
        }
    }
}
