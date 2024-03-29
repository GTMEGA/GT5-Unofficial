package gregtech.common;

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
        return MinecraftServer.getServer().getEntityWorld().func_152378_a(uuid);
    }

}
