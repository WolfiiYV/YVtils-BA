package yv.tils.smp.mods.admin.vanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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

    private static Vanish instance;
    public static Vanish getInstance() {return instance;}

    public static Map<UUID, UUID> exec_target = new HashMap<>();

    public static Map<UUID, Boolean> vanish = new HashMap<>(); //Default: false
    public static Map<UUID, Boolean> alreadyVanished = new HashMap<>(); //Default: false
    public static Map<UUID, Integer> layer = new HashMap<>(); //Default: 1
    public static Map<UUID, Boolean> itemPickup = new HashMap<>(); //Default: false
    public static Map<UUID, Boolean> invInteraction = new HashMap<>(); //Default: true
    public static Map<UUID, Boolean> mobTarget = new HashMap<>(); //Default: true

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        instance = this;

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
        }else {
            if (args[0].equals("quick") || args[0].equals("q")) {
                if (!vanish.containsKey(player.getUniqueId())) vanish.put(player.getUniqueId(), false);
                if (!layer.containsKey(player.getUniqueId())) layer.put(player.getUniqueId(), 1);
                if (!itemPickup.containsKey(player.getUniqueId())) itemPickup.put(player.getUniqueId(), false);
                if (!invInteraction.containsKey(player.getUniqueId())) invInteraction.put(player.getUniqueId(), true);
                if (!mobTarget.containsKey(player.getUniqueId())) mobTarget.put(player.getUniqueId(), true);

                if (vanish.get(player.getUniqueId())) {
                    new VanishGUI().vanishRegister(player, false);
                    disableVanish(player);
                }else {
                    new VanishGUI().vanishRegister(player, true);
                    enableVanish(player);
                }
                return true;
            }else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(LanguageFile.getMessage(LanguageMessage.PLAYER_NOT_ONLINE));
                    return false;
                }

                if (args.length == 2) {
                    if (args[1].equals("quick") || args[1].equals("q")) {
                        if (!vanish.containsKey(target.getUniqueId())) vanish.put(target.getUniqueId(), false);
                        if (!layer.containsKey(target.getUniqueId())) layer.put(target.getUniqueId(), 1);
                        if (!itemPickup.containsKey(target.getUniqueId())) itemPickup.put(target.getUniqueId(), false);
                        if (!invInteraction.containsKey(target.getUniqueId())) invInteraction.put(target.getUniqueId(), true);
                        if (!mobTarget.containsKey(target.getUniqueId())) mobTarget.put(target.getUniqueId(), true);

                        if (vanish.get(target.getUniqueId())) {
                            new VanishGUI().vanishRegister(target, false);
                            disableVanish(target);
                        }else {
                            new VanishGUI().vanishRegister(target, true);
                            enableVanish(target);
                        }
                        return true;
                    }
                } else if (args.length == 1) {
                    if (!vanish.containsKey(target.getUniqueId())) vanish.put(target.getUniqueId(), false);
                    if (!layer.containsKey(target.getUniqueId())) layer.put(target.getUniqueId(), 1);
                    if (!itemPickup.containsKey(target.getUniqueId())) itemPickup.put(target.getUniqueId(), false);
                    if (!invInteraction.containsKey(target.getUniqueId())) invInteraction.put(target.getUniqueId(), true);
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
        String quitMessage= new PlayerQuit().generateQuitMessage(player);

        for (Player target : Bukkit.getOnlinePlayers()) {
            target.hidePlayer(YVtils.getInstance(), player);
        }

        if (!layer.get(player.getUniqueId()).equals(4)) {
            for (var entry : layer.entrySet()) {
                if (entry.getValue() >= layer.get(player.getUniqueId())) {
                    for (Player viewer : Bukkit.getOnlinePlayers()) {
                        if (viewer.getUniqueId().equals(entry.getKey()) && entry.getValue() != 4) {
                            player.showPlayer(YVtils.getInstance(), viewer);
                            viewer.showPlayer(YVtils.getInstance(), player);
                        }
                    }
                }
            }
        }

        player.setCanPickupItems(itemPickup.get(player.getUniqueId()));

        if (!invInteraction.get(player.getUniqueId())) {
            // Silent Inventory Interaction
            // https://pastebin.com/LF7u1HvH <- Use as reference
        }

        player.setSleepingIgnored(true);
        player.setSilent(true);

        if (alreadyVanished.containsKey(player.getUniqueId()) && alreadyVanished.get(player.getUniqueId())) {
            //TODO: Send Message to player that vanish got refreshed
            alreadyVanished.put(player.getUniqueId(), false);
            return;
        }

        Bukkit.broadcastMessage(quitMessage);
        player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_ACTIVATE), List.of("PREFIX"), List.of(Prefix.PREFIX)));
    }

    public void disableVanish(Player player) {
        String joinMessage = new PlayerJoin().generateJoinMessage(player);

        for (Player target : Bukkit.getOnlinePlayers()) {
            target.showPlayer(YVtils.getInstance(), player);
        }

        player.setSleepingIgnored(false);
        player.setCanPickupItems(true);
        player.setSilent(false);

        //Disable Silent Inventory Interaction

        if (!(alreadyVanished.containsKey(player.getUniqueId()) && alreadyVanished.get(player.getUniqueId()))) {
            //TODO: Send Message to player that vanish got refreshed
            alreadyVanished.put(player.getUniqueId(), false);
            return;
        }

        Bukkit.broadcastMessage(joinMessage);
        player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.VANISH_DEACTIVATE), List.of("PREFIX"), List.of(Prefix.PREFIX)));
    }

    public void onRejoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        if (vanish.get(player.getUniqueId())) {
            enableVanish(player);
        }else {
            disableVanish(player);
        }
    }

    public void onGamemodeSwitch(PlayerGameModeChangeEvent e) {
        Player player = e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        if (vanish.get(player.getUniqueId())) {
            enableVanish(player);
        }else {
            disableVanish(player);
        }
    }

    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        if (vanish.get(player.getUniqueId())) {
            enableVanish(player);
        }else {
            disableVanish(player);
        }
    }

    public void chestOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        if (vanish.get(player.getUniqueId())) {
            for (Player nearPlayer : player.getWorld().getPlayers()) {
                if (nearPlayer.getLocation().distance(player.getLocation()) <= 20) {
                    System.out.println(nearPlayer.getName());
                    Bukkit.getScheduler().runTaskLater(YVtils.getInstance(), () -> {
                        nearPlayer.stopSound(Sound.BLOCK_CHEST_OPEN);
                        nearPlayer.stopSound(Sound.BLOCK_BARREL_OPEN);
                        nearPlayer.stopSound(Sound.BLOCK_SHULKER_BOX_OPEN);
                    }, 1L);
                }
            }
        }
    }

    public void chestClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (!vanish.containsKey(player.getUniqueId())) return;
        if (vanish.get(player.getUniqueId())) {
            for (Player nearPlayer : player.getWorld().getPlayers()) {
                if (nearPlayer.getLocation().distance(player.getLocation()) <= 20) {
                    System.out.println(nearPlayer.getName());
                    Bukkit.getScheduler().runTaskLater(YVtils.getInstance(), () -> {
                        nearPlayer.stopSound(Sound.BLOCK_CHEST_CLOSE);
                        nearPlayer.stopSound(Sound.BLOCK_BARREL_CLOSE);
                        nearPlayer.stopSound(Sound.BLOCK_SHULKER_BOX_CLOSE);
                    }, 1L);
                }
            }
        }
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(LanguageFile.getMessage(LanguageMessage.COMMAND_USAGE) + " " + ChatColor.BLUE + "/v [quick]\n" +
                "/v [player] [quick]");
    }
}