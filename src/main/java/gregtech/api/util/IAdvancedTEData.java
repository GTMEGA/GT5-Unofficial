package gregtech.api.util;


import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;

import javax.annotation.Nonnull;
import java.util.List;


public interface IAdvancedTEData extends ISerializableObject {

    /**
     * Sends itself to crafters, only should be called server-side
     */
    default void detectAndSendChanges(final Container source, final List<ICrafting> crafters) {
        crafters.forEach(crafter -> sendChange(source, crafter));
    }

    /**
     * Actually sends the changes
     */
    void sendChange(final Container container, ICrafting crafter);

    /**
     * It's actually better in this instance to not copy, at least from what I've seen, so this is defaulted for your safety
     */
    @Nonnull
    default ISerializableObject copy() {
        return this;
    }

    /**
     * Receives changed data, only should be called client-side
     */
    void receiveChange(final int changeID, final int data);

}
