package ic2.api.item;

/**
 * Allows for charging, discharging and using electric items (IElectricItem).
 */
public final class ElectricItem {
    /**
     * IElectricItemManager to use for interacting with IElectricItem ItemStacks.
     *
     * This manager will act as a gateway and delegate the tasks to the final implementation
     * (rawManager or a custom one) as necessary.
     */
    public static IElectricItemManager manager;

    /**
     * Standard IElectricItemManager implementation, only call it directly from another
     * IElectricItemManager. Use manager instead.
     */
    public static IElectricItemManager rawManager;
}

