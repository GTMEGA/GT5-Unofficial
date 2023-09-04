package gregtech.api.enums;


import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Predicate;


public enum RSControlMode {
    // Ignore redstone entirely
    IGNORE(alwaysTrue()),
    LOW((Byte b) -> b == 0),
    HIGH((Byte b) -> b != 0),
    // Sentinel
    INVALID_MODE;


    public enum RSModeType {
        BINARY,
        SIGNAL_SENSITIVE,
        INVALID_MODE_TYPE
    }


    private final Predicate<Byte> predicate;

    public static RSControlMode getMode(final int mode) {
        if (mode < 0 || mode >= INVALID_MODE.ordinal()) {
            return INVALID_MODE;
        }
        return values()[mode];
    }

    private static Predicate<Byte> alwaysTrue() {
        return (Byte b) -> true;
    }

    RSControlMode() {
        this(alwaysFalse(), RSModeType.INVALID_MODE_TYPE);
    }

    private static Predicate<Byte> alwaysFalse() {
        return (Byte b) -> false;
    }

    RSControlMode(final Predicate<Byte> predicate, final RSModeType type) {
        this.predicate = predicate;
    }

    RSControlMode(final Predicate<Byte> predicate) {
        this(predicate, RSModeType.BINARY);
    }

    public boolean checkPredicate(final byte rs) {
        return this.predicate.test(rs);
    }

    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("rsControl", ordinal());
    }

    public static RSControlMode loadFromNBTData(NBTTagCompound compound) {
        if (compound.hasKey("rsControl")) {
            return getMode(compound.getInteger("rsControl"));
        }
        return INVALID_MODE;
    }

}
