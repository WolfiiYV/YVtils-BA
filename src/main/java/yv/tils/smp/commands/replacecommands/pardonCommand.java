package yv.tils.smp.commands.replacecommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import yv.tils.smp.LanguageSystem.LanguagePlaceholder;
import yv.tils.smp.placeholder.MessagePlaceholder;

/**
 * @since 4.6.6
 * @version 4.6.6
 */
public class pardonCommand implements Listener {

    @EventHandler
    public void onPlayerPardonCommandPreProcess(PlayerCommandPreprocessEvent event) {

        final Player player = event.getPlayer();
        final String cmd = event.getMessage();
        final String[] args = cmd.split(" ");

        String cmdlowercase = cmd.toLowerCase();

        if (cmdlowercase.startsWith("/pardon")) {
            event.setCancelled(true);
                if (player.hasPermission("yv.tils.smp.command.moderation.unban")) {
                    TextComponent c = new TextComponent(LanguagePlaceholder.Replace1());
                    TextComponent click = new TextComponent("§e/mod unban");


                    click.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "/mod unban"));
                    click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Click to copy to Clipboard")));

                    c.addExtra(click);
                    player.spigot().sendMessage(c);

                } else {
                    player.sendMessage(MessagePlaceholder.PERMISSIONERROR + ": yv.tils.smp.command.moderation.unban");
                }}}}