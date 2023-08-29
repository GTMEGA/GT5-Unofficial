package gregtech.api.interfaces;


import gregtech.api.enums.RSControlMode;


public interface IRedstoneSensitive {

    RSControlMode getRedstoneMode();

    byte[] getRSValues();

    default byte getMaxRSValue() {
        byte max = 0;
        for (byte i = 0; i < getRSValues().length; i++) {
            max = (byte) Math.max(max, getRSValues()[i]);
        }
        return max;
    }

    default void setMode(final int newMode) {
        setMode(RSControlMode.getMode(newMode));
    }

    void setMode(final RSControlMode newMode);

    void updateRSValues(final byte side, final byte rsSignal);

    void processRS();

    default boolean isValidMode(final RSControlMode mode) {
        return true;
    }

}
