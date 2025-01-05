package gregtech.common;

import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.UUID;


public class GT_Server extends GT_Proxy {
    @Override
    public boolean isServerSide() {
        return true;
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public boolean isBukkitSide() {
        return false;
    }

    @Override
    public void doSonictronSound(ItemStack aStack, World aWorld, double aX, double aY, double aZ) {
    }

    @Override
    public int addArmor(String aPrefix) {
        return 0;
    }

    @Override
    public EntityPlayer getThePlayer() {
        return null;
    }

    /**
     * @param uuid
     * @return
     */
    @Override
    public EntityPlayer getPlayerFromUUID(final UUID uuid) {
        for (val worldServer: MinecraftServer.getServer().worldServers) {
            final EntityPlayer result = worldServer.func_152378_a(uuid);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

}
