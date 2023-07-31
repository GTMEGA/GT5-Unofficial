package gregtech.api.interfaces;


import gregtech.api.enums.RSControlMode;


public interface IRedstoneSensitive {

    RSControlMode getMode();

    byte[] getRSValues();

    byte getMaxRSValue();

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
