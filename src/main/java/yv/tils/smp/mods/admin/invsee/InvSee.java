package yv.tils.smp.mods.admin.invsee;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;
import yv.tils.smp.utils.internalapi.StringReplacer;

import java.util.*;

/**
 * @version 4.6.8
 * @since 4.6.8
 */
public class InvSee implements CommandExecutor {

    public static Map<UUID, UUID> invSee = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) return false;

        if (!player.hasPermission("yvtils.smp.command.invsee")) {
            player.sendMessage(LanguageFile.getMessage(LanguageMessage.MISSING_PERMISSION) + " yvtils.smp.command.invsee");
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            player.sendMessage(LanguageFile.getMessage(LanguageMessage.PLAYER_NOT_ONLINE));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        player.openInventory(getInventory(target));
        invSee.put(player.getUniqueId(), target.getUniqueId());
        return false;
    }


    private Inventory getInventory(Player player) {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1.add("PLAYER");
        list2.add(player.getName());

        Inventory inv = Bukkit.createInventory(null, 54, new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.MODULE_INVSEE_INVENTORY), list1, list2));

        ItemStack[] armour = player.getInventory().getArmorContents();
        ItemStack[] invContent = player.getInventory().getStorageContents();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        List<ItemStack> armourList = new ArrayList<>(Arrays.asList(armour));
        List<ItemStack> contents = new ArrayList<>(Arrays.asList(invContent));

        for (int i = 0; i < 9; i++) {
            contents.add(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        Collections.reverse(armourList);

        Collections.addAll(contents, armourList.toArray(new ItemStack[0]));
        for (int i = 0; i < 4; i++) {
            contents.add(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        contents.add(offHand);

        ItemStack[] cont = contents.toArray(new ItemStack[0]);

        inv.setContents(cont);
        return inv;
    }
}
