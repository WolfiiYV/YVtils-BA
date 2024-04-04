package yv.tils.smp.mods.status;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import yv.tils.smp.utils.color.HexSupport;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;
import yv.tils.smp.utils.configs.status.StatusConfigManager;
import yv.tils.smp.utils.internalapi.StringReplacer;

import java.util.ArrayList;
import java.util.List;

/**
 * @version CH2-1.0.0
 * @since 4.6.6
 */

public class StatusCommand implements CommandExecutor {

    List<String> defaultStatus = new StatusConfigManager().ConfigRequest().getStringList("Default-Status");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String labels, String[] args) {
        if (args.length < 1) {
            sendUsage(sender);
            return true;
        }

        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "clear" -> {
                if (args.length == 2) {
                    OfflinePlayer player1 = Bukkit.getOfflinePlayer(args[1]);
                    Player player2 = Bukkit.getPlayer(args[1]);
                    if (sender.hasPermission("yvtils.smp.command.status.clear.other")) {
                        if (new StatusConfigManager().SavedRequest().get(String.valueOf(player1.getUniqueId())) != null) {

                            List<String> list1 = new ArrayList<>();
                            List<String> list2 = new ArrayList<>();
                            list1.add("PLAYER");
                            list2.add(player1.getName());

                            if (player2 != null) {
                                if (player2.isOnline()) {
                                    player2.setDisplayName(player2.getName());
                                    player2.setPlayerListName(player2.getName());
                                    new NametagManager().removePlayer(player2);
                                }
                            }

                            clearStatus(null, player1);
                            player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_CLEAR_OTHER_CLEARED), list1, list2));
                        } else {
                            sender.sendMessage(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_OTHER_PLAYER_HAS_NO_STATUS));
                        }
                    } else {
                        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_CLEAR_OTHER_UNALLOWED));
                    }
                } else if (args.length == 1) {
                    if (new StatusConfigManager().SavedRequest().get(String.valueOf(player.getUniqueId())) != null) {
                        player.setDisplayName(player.getName());
                        player.setPlayerListName(player.getName());
                        new NametagManager().removePlayer(player);
                        clearStatus(player, null);
                    } else {
                        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_PLAYER_HAS_NO_STATUS));
                    }
                } else {
                    sendUsage(sender);
                }
            }
            case "set" -> {
                if (args.length >= 2) {

                    StringBuilder builder = new StringBuilder();
                    for (int x = 1; x < args.length; x++) {
                        builder.append(args[x]).append(" ");
                    }
                    String status = builder.toString();

                    status = status.substring(0, status.length() - 1);

                    if (ChatColor.stripColor(HexSupport.hex(status)).length() > new StatusConfigManager().ConfigRequest().getInt("MaxStatusLength")) {
                        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_CUSTOM_STATUS_TOO_LONG));
                        return true;
                    }
                    if (sender.hasPermission("yvtils.smp.command.status.set")) {
                        player.setDisplayName(HexSupport.hex(status) + " " + player.getName());
                        player.setPlayerListName(HexSupport.hex(status) + " " + player.getName());
                        new NametagManager().addPlayer(player, HexSupport.hex(status) + " ");
                        saveStatus(player, status);
                    } else {
                        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_NOT_ALLOWED_TO_SET_CUSTOM_STATUS));
                    }
                } else {
                    sendUsage(sender);
                }
            }
            case "default" -> {
                for (String s : defaultStatus) {
                    StringBuilder builder = new StringBuilder();

                    for (int x = 1; x < args.length; x++) {
                        builder.append(args[x]).append(" ");
                    }

                    String status = builder.substring(0, builder.toString().length() - 1);

                    if (!defaultStatus.contains(status)) {
                        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_NO_DEFAULT_STATUS));
                        break;
                    }
                    if (status.equals(s)) {
                        player.setDisplayName(HexSupport.hex(s) + " " + player.getName());
                        player.setPlayerListName(HexSupport.hex(s) + " " + player.getName());
                        new NametagManager().addPlayer(player, HexSupport.hex(s) + " ");
                        saveStatus(player, s);
                    }
                }
            }
            default -> sendUsage(sender);

        }
        return false;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.COMMAND_USAGE) + " " + ChatColor.BLUE +
                "/status <set> <Prefix> (Hex & Color Codes are supported!)\n" +
                "/status <default> <Prefix>\n" +
                "/status clear [Player]");
    }

    private void saveStatus(Player player, String status) {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1.add("STATUS");
        list2.add(status);

        String args = new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_SET), list1, list2);
        player.sendMessage(HexSupport.hex(args));
        Bukkit.getConsoleSender().sendMessage(player.getName() + ": " + new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_SET), list1, list2));
        new StatusConfigManager().SavedWriter(String.valueOf(player.getUniqueId()), status);
    }

    private void clearStatus(Player player, OfflinePlayer player1) {
        if (player == null) {
            new StatusConfigManager().SavedWriter(String.valueOf(player1.getUniqueId()), null);
        } else {
            player.sendMessage(LanguageFile.getMessage(LanguageMessage.MODULE_STATUS_CLEAR_CLEARED));
            new StatusConfigManager().SavedWriter(String.valueOf(player.getUniqueId()), null);
        }
    }
}