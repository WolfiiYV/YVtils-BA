package yv.tils.smp.utils.invSync;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import yv.tils.smp.mods.admin.vanish.Vanish;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @version CH2-1.0.0
 * @since CH2-1.0.0
 */
public class InvOpen {

    public static Map<UUID, Location> containerLocation = new HashMap<>();
    public static Map<UUID, ItemStack[]> inventory = new HashMap<>();
    public static Map<UUID, Boolean> invOpen = new HashMap<>();

    public void invOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();

        if (Vanish.vanish.containsKey(player.getUniqueId()) && Vanish.vanish.get(player.getUniqueId())) {
            if (invOpen.containsKey(player.getUniqueId()) && invOpen.get(player.getUniqueId())) return;

            if (e.getInventory().getLocation() != null) {
                inventory.put(player.getUniqueId(), e.getInventory().getContents());
                containerLocation.put(player.getUniqueId(), e.getInventory().getLocation());

                if (Vanish.vanish.get(player.getUniqueId())) {
                    for (Player nearPlayer : player.getWorld().getPlayers()) {
                        double distance = nearPlayer.getLocation().distance(player.getLocation());
                        if (distance <= 16) {
                            nearPlayer.stopAllSounds();
                        }
                    }
                }

                e.setCancelled(true);

                player.closeInventory();

                customInv(player);
            }
        }
    }

    private void customInv(Player player) {
        Inventory inv = Bukkit.createInventory(player, inventory.get(player.getUniqueId()).length, "Container Clone");
        inv.setContents(inventory.get(player.getUniqueId()));

        invOpen.put(player.getUniqueId(), true);

        player.openInventory(inv);
    }
}