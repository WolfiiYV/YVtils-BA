package yv.tils.smp.manager.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import yv.tils.smp.mods.admin.vanish.VanishGUI;
import yv.tils.smp.mods.ccr.InvListener;
import yv.tils.smp.mods.other.SpawnElytra;
import yv.tils.smp.utils.invSync.InvClose;
import yv.tils.smp.utils.invSync.InvOpen;
import yv.tils.smp.utils.invSync.InvSync;

/**
 * @version CH2-1.0.0
 * @since CH2-1.0.0
 */
public class InventoryListener implements Listener {
    @EventHandler
    public void onSwapHandEvent(PlayerSwapHandItemsEvent e) {
        SpawnElytra.getInstance().onHandSwap(e);
    }

    @EventHandler
    public void onInvCloseEvent(InventoryCloseEvent e) {
        new InvListener().onInvClose(e.getInventory(), e.getPlayer());
        new VanishGUI().guiClose(e);
        new InvClose().invClose(e);
    }

    @EventHandler
    public void onInvClickEvent(InventoryClickEvent e) {
        new InvListener().onInvClick(e);
        new VanishGUI().invInteraction(e);
        new InvSync().invChange(e);
    }

    @EventHandler
    public void onInvOpenEvent(InventoryOpenEvent e) {
        new InvOpen().invOpen(e);
    }
}