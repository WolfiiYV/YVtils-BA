package yv.tils.smp.mods.admin.vanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import yv.tils.smp.YVtils;
import yv.tils.smp.mods.server.connect.PlayerJoin;
import yv.tils.smp.mods.server.connect.PlayerQuit;
import yv.tils.smp.placeholder.Prefix;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;
import yv.tils.smp.utils.internalapi.StringReplacer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @version CH2-1.0.0
 * @since CH2-1.0.0
 */
public class Vanish implements CommandExecutor {

    public static Map<UUID, UUID> exec_target = new HashMap<>();
    public static Map<UUID, Boolean> vanish = new HashMap<>(); //Default: false
    public static Map<UUID, Boolean> oldVanish = new HashMap<>(); //Default: false
    public static Map<UUID, Integer> layer = new HashMap<>(); //Default: 1
    public static Map<UUID, Boolean> itemPickup = new HashMap<>(); //Default: false
    public static Map<UUID, Boolean> invInteraction = new HashMap<>(); //Default: true
    public static Map<UUID, Boolean> mobTarget = new HashMap<>(); //Default: true

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;

        if (args.length > 2) {
            sendUsage(sender);
            return false;
        }

        if (args.length == 0) {
            if (!vanish.containsKey(player.getUniqueId())) vanish.put(player.getUniqueId(), false);
            if (!layer.containsKey(player.getUniqueId())) layer.put(player.getUniqueId(), 1);
            if (!itemPickup.containsKey(player.getUniqueId())) itemPickup.put(player.getUniqueId(), false);
            if (!invInteraction.containsKey(player.getUniqueId())) invInteraction.put(player.getUniqueId(), true);
            if (!mobTarget.containsKey(player.getUniqueId())) mobTarget.put(player.getUniqueId(), true);

            new VanishGUI().vanishGUI(player);
            return true;
        } else {
            if (args[0].equals("quick") || args[0].equals("q")) {

                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(LanguageFile.getMessage(LanguageMessage.PLAYER_NOT_ONLINE));
                        return false;
                    }

                    if (!vanish.containsKey(target.getUniqueId())) vanish.put(target.getUniqueId(), false);
                    if (!layer.containsKey(target.getUniqueId())) layer.put(target.getUniqueId(), 1);
                    if (!itemPickup.containsKey(target.getUniqueId())) itemPickup.put(target.getUniqueId(), false);
                    if (!invInteraction.containsKey(target.getUniqueId()))
                        invInteraction.put(target.getUniqueId(), true);
                    if (!mobTarget.containsKey(target.getUniqueId())) mobTarget.put(target.getUniqueId(), true);

                    if (vanish.get(target.getUniqueId())) {
                        new VanishGUI().vanishRegister(target, false);
                        disableVanish(target);
                    } else {
                        new VanishGUI().vanishRegister(target, true);
                        enableVanish(target);
                    }
                    return true;
                }

                if (!vanish.containsKey(player.getUniqueId())) vanish.put(player.getUniqueId(), false);
                if (!layer.containsKey(player.getUniqueId())) layer.put(player.getUniqueId(), 1);
                if (!itemPickup.containsKey(player.getUniqueId())) itemPickup.put(player.getUniqueId(), false);
                if (!invInteraction.containsKey(player.getUniqueId())) invInteraction.put(player.getUniqueId(), true);
                if (!mobTarget.containsKey(player.getUniqueId())) mobTarget.put(player.getUniqueId(), true);

                if (vanish.get(player.getUniqueId())) {
                    new VanishGUI().vanishRegister(player, false);
                    oldVanish.put(player.getUniqueId(), !vanish.get(player.getUniqueId()));
                    disableVanish(player);
                } else {
                    new VanishGUI().vanishRegister(player, true);
                    oldVanish.put(player.getUniqueId(), !vanish.get(player.getUniqueId()));
                    enableVanish(player);
                }
                return true;
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(LanguageFile.getMessage(LanguageMessage.PLAYER_NOT_ONLINE));
                    return false;
                }

                if (args.length == 1) {
                    if (!vanish.containsKey(target.getUniqueId())) vanish.put(target.getUniqueId(), false);
                    if (!layer.containsKey(target.getUniqueId())) layer.put(target.getUniqueId(), 1);
                    if (!itemPickup.containsKey(target.getUniqueId())) itemPickup.put(target.getUniqueId(), false);
                    if (!invInteraction.containsKey(target.getUniqueId()))
                        invInteraction.put(target.getUniqueId(), true);
                    if (!mobTarget.containsKey(target.getUniqueId())) mobTarget.put(target.getUniqueId(), true);

                    exec_target.put(player.getUniqueId(), target.getUniqueId());

                    new VanishGUI().vanishGUI(player);
                    return true;
                }
            }
        }


        return false;
    }


    public void enableVanish(Player player) {
        String quitMessage = new PlayerQuit().generateQuitMessage(player);

        playerHide(player);

        player.setCanPickupItems(itemPickup.get(player.getUniqueId()));

        player.setSleepingIgnored(true);
        player.setSilent(true);

        if ((oldVanish.containsKey(player.getUniqueId()) && vanish.containsKey(player.getUniqueId())) && oldVanish.get(player.getUniqueId()) == vanish.get(player.getUniqueId())) {
            player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_REFRESH), List.of("PREFIX"), List.of(Prefix.PREFIX)));

            for (var entry : exec_target.entrySet()) {
                if (entry.getValue() == entry.getKey()) return;
                if (entry.getValue().equals(player.getUniqueId())) {
                    Player target = Bukkit.getPlayer(entry.getKey());
                    if (target != null) {
                        target.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_REFRESH_OTHER), List.of("PREFIX", "PLAYER"), List.of(Prefix.PREFIX, player.getName())));
                    }
                }
            }
            return;
        }

        Bukkit.broadcastMessage(quitMessage);
        player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_ACTIVATE), List.of("PREFIX"), List.of(Prefix.PREFIX)));
    }

    public void disableVanish(Player player) {
        String joinMessage = new PlayerJoin().generateJoinMessage(player);

        for (Player target : Bukkit.getOnlinePlayers()) {
            target.showPlayer(YVtils.getInstance(), player);
            if (vanish.containsKey(target.getUniqueId()) && vanish.get(target.getUniqueId()))
                player.hidePlayer(YVtils.getInstance(), target);
        }

        player.setSleepingIgnored(false);
        player.setCanPickupItems(true);
        player.setSilent(false);

        if ((oldVanish.containsKey(player.getUniqueId()) && vanish.containsKey(player.getUniqueId())) && oldVanish.get(player.getUniqueId()) == vanish.get(player.getUniqueId())) {
            player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_REFRESH), List.of("PREFIX"), List.of(Prefix.PREFIX)));

            for (var entry : exec_target.entrySet()) {
                if (entry.getValue() == entry.getKey()) return;
                if (entry.getValue().equals(player.getUniqueId())) {
                    Player target = Bukkit.getPlayer(entry.getKey());
                    if (target != null) {
                        target.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_REFRESH_OTHER), List.of("PREFIX", "PLAYER"), List.of(Prefix.PREFIX, player.getName())));
                    }
                }
            }

            return;
        }

        Bukkit.broadcastMessage(joinMessage);
        player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_DEACTIVATE), List.of("PREFIX"), List.of(Prefix.PREFIX)));
    }

    public void onOtherJoin(Player target) {
        for (var entry : vanish.entrySet()) {
            if (entry.getValue()) {
                Player player = Bukkit.getPlayer(entry.getKey());

                if (player == null) return;

                target.hidePlayer(YVtils.getInstance(), player);
                player.hidePlayer(YVtils.getInstance(), target);

                if (!vanish.containsKey(target.getUniqueId())) player.showPlayer(YVtils.getInstance(), target);

                if (!layer.get(player.getUniqueId()).equals(4)) {
                    for (var entry2 : layer.entrySet()) {
                        if (entry2.getValue() <= layer.get(player.getUniqueId())) {
                            for (Player viewer : Bukkit.getOnlinePlayers()) {
                                if ((viewer.getUniqueId().equals(entry2.getKey()) && entry2.getValue() != 4) && (vanish.containsKey(viewer.getUniqueId()) && vanish.get(viewer.getUniqueId()))) {
                                    player.showPlayer(YVtils.getInstance(), viewer);
                                    viewer.showPlayer(YVtils.getInstance(), player);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void onRejoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        oldVanish.put(player.getUniqueId(), vanish.get(player.getUniqueId()));
        if (vanish.get(player.getUniqueId())) {
            enableVanish(player);

            playerHide(player);
        } else {
            disableVanish(player);
        }
    }

    private void playerHide(Player player) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.hidePlayer(YVtils.getInstance(), player);
            player.hidePlayer(YVtils.getInstance(), target);

            if (!vanish.containsKey(target.getUniqueId())) player.showPlayer(YVtils.getInstance(), target);
        }

        if (!layer.get(player.getUniqueId()).equals(4)) {
            for (var entry : layer.entrySet()) {
                if (entry.getValue() <= layer.get(player.getUniqueId())) {
                    for (Player viewer : Bukkit.getOnlinePlayers()) {
                        if ((viewer.getUniqueId().equals(entry.getKey()) && entry.getValue() != 4) && (vanish.containsKey(viewer.getUniqueId()) && vanish.get(viewer.getUniqueId()))) {
                            player.showPlayer(YVtils.getInstance(), viewer);
                            viewer.showPlayer(YVtils.getInstance(), player);
                        }
                    }
                }
            }
        }
    }

    public void onGamemodeSwitch(PlayerGameModeChangeEvent e) {
        Player player = e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        if (vanish.get(player.getUniqueId())) {
            enableVanish(player);
        } else {
            disableVanish(player);
        }
    }

    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        if (vanish.get(player.getUniqueId())) {
            enableVanish(player);
        } else {
            disableVanish(player);
        }
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.COMMAND_USAGE) + " " + ChatColor.BLUE + "/v [quick]\n" +
                "/v [player] [quick]");
    }
}