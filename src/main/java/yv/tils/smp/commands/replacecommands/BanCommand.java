package yv.tils.smp.commands.replacecommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import yv.tils.smp.utils.language.LanguageFile;
import yv.tils.smp.utils.language.LanguageMessage;
import yv.tils.smp.placeholder.MessagePlaceholder;
import yv.tils.smp.placeholder.StringReplacer;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 4.6.6
 * @version 4.6.6
 */
public class BanCommand implements Listener {

    @EventHandler
    public void onPlayerBanCommandPreProcess(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String cmd = event.getMessage();
        final String[] args = cmd.split(" ");

        String cmdlowercase = args[0].toLowerCase();

        List<String> list1 = new ArrayList();
        List<String> list2 = new ArrayList();
        list1.add("COMMAND");
        list2.add("§e/mod ban");

        if (cmdlowercase.equals("/ban")) {
            event.setCancelled(true);
                if (player.hasPermission("yvtils.smp.command.moderation.ban")) {

                    TextComponent c = new TextComponent(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.COMMAND_REPLACE_NEW_COMMAND_INFO), list1, list2));
                    c.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "/mod ban"));
                    c.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(LanguageFile.getMessage(LanguageMessage.COMMAND_REPLACE_COPY_COMMAND_TO_CLIPBOARD))));
                    player.spigot().sendMessage(c);

                } else {
                    player.sendMessage(MessagePlaceholder.PERMISSIONERROR + " yvtils.smp.command.moderation.ban");
                }}}}