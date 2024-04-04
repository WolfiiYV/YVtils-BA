package yv.tils.smp.utils.invSync;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import yv.tils.smp.YVtils;
import yv.tils.smp.mods.admin.vanish.Vanish;

/**
 * @version CH2-1.0.0
 * @since CH2-1.0.0
 */
public class InvClose {
    public void invClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();

        if (InvOpen.invOpen.containsKey(player.getUniqueId()) && InvOpen.invOpen.get(player.getUniqueId())) {
            InvOpen.invOpen.remove(player.getUniqueId());
            InvOpen.inventory.remove(player.getUniqueId());
            InvOpen.containerLocation.remove(player.getUniqueId());
        }

        if (!Vanish.vanish.containsKey(player.getUniqueId())) return;
        if (Vanish.vanish.get(player.getUniqueId())) {
            for (Player nearPlayer : player.getWorld().getPlayers()) {
                double distance = nearPlayer.getLocation().distance(player.getLocation());
                if (distance <= 16) {
                    Bukkit.getScheduler().runTaskLater(YVtils.getInstance(), nearPlayer::stopAllSounds, 1);
                }
            }
        }
    }
}