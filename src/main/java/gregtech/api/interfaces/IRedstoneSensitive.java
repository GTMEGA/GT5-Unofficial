package gregtech.api.interfaces;


import gregtech.api.enums.RSControlMode;


public interface IRedstoneSensitive {

    RSControlMode getRedstoneMode();

    default byte getMaxRSValue() {
        byte max = 0;
        for (byte i = 0; i < getRSValues().length; i++) {
            max = (byte) Math.max(max, getRSValues()[i]);
        }
        return max;
    }

    byte[] getRSValues();

    default void setMode(final int newMode) {
        setMode(RSControlMode.getMode(newMode));
    }

    void setMode(final RSControlMode newMode);

    void updateRSValues(final byte side, final byte rsSignal);

    /**
     * Update state based upon redstone input
     */
    void processRS();

    default boolean isValidMode(final RSControlMode mode) {
        return true;
    }

    /**
     * Indicates whether the client MTE should be updated, useful for texture stuff
     */
    default boolean receiveRSClientUpdates() {
        return false;
    }

    /**
     * How many ticks apart the checks are
     */
    default int rsTickRate() {
        return 2;
    }

}
