package yv.tils.smp.manager.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import yv.tils.smp.manager.commands.Fly;
import yv.tils.smp.mods.admin.vanish.Vanish;
import yv.tils.smp.mods.server.connect.PlayerJoin;
import yv.tils.smp.utils.updater.VersionGetter;

/**
 * @version CH2-1.0.0
 * @since 4.6.6
 */
public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        new VersionGetter().onPlayerJoin(e.getPlayer());
        new PlayerJoin().onPlayerJoin(e);
        new Fly().onRejoin(e);
        new Vanish().onRejoin(e);
        new Vanish().onOtherJoin(e.getPlayer());
    }
}