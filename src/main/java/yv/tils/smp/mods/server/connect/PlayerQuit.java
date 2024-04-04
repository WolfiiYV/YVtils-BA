package yv.tils.smp.mods.server.connect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import yv.tils.smp.YVtils;
import yv.tils.smp.mods.admin.vanish.Vanish;
import yv.tils.smp.placeholder.Prefix;

import java.util.Collections;
import java.util.List;

/**
 * @version CH2-1.0.0
 * @since CH2-1.0.0
 */
public class PlayerQuit {
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        String quitMessage = generateQuitMessage(player);

        if (Vanish.vanish.containsKey(player.getUniqueId()) && Vanish.vanish.get(player.getUniqueId())) {
            e.setQuitMessage(null);
            return;
        }

        if (YVtils.getInstance().getConfig().getBoolean("SendQuitMessage")) {
            Bukkit.getConsoleSender().sendMessage(Prefix.PREFIXDISCONNECT + ChatColor.RED + " « " + ChatColor.GRAY + player.getName());
            e.setQuitMessage(quitMessage);
        } else {
            Bukkit.getConsoleSender().sendMessage(Prefix.PREFIXDISCONNECT + ChatColor.RED + " « " + ChatColor.GRAY + player.getName());
            e.setQuitMessage(null);
        }

        YVtils.getInstance().getRecentMessages().remove(e.getPlayer().getUniqueId());
    }

    public String generateQuitMessage(Player player) {
        String playerName = player.getName();
        List<String> list = YVtils.getInstance().getConfig().getStringList("QuitMessage");

        list.replaceAll(s -> s.replace("player", playerName));

        Collections.shuffle(list);

        return list.get(0);
    }
}