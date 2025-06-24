package gregtech.api.interfaces.tileentity;


/**
 * To access my Machines a bit easier
 */
public interface IUpgradableMachine extends IMachineProgress {
    /**
     * Accepts Upgrades. Some Machines have an Upgrade Limit.
     */
    boolean isUpgradable();

    /**
     * Accepts Muffler Upgrades
     */
    boolean isMufflerUpgradable();

    /**
     * Adds Muffler Upgrade
     */
    boolean addMufflerUpgrade();

    /**
     * Does this Machine have a Muffler
     */
    boolean hasMufflerUpgrade();

    /**
     * Does this Machine have a Steam-Converter
     */
    boolean hasSteamEngineUpgrade();
}
