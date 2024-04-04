package yv.tils.smp.mods.status;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 * @version 4.6.6
 * @since 4.6.6
 */
public class NametagManager {
    private Team checkTeam(String name, Player player) {
        Team team = player.getScoreboard().getTeam(name);
        if (team != null) {
            return team;
        }
        return player.getScoreboard().registerNewTeam(name);
    }

    public void addPlayer(Player player, String prefix) {
        Team team = checkTeam(player.getUniqueId() + "UUID", player);
        team.setPrefix(prefix);
        team.addEntry(player.getName());
    }

    public void removePlayer(Player player) {
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }
}
