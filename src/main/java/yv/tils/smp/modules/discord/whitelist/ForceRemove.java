package yv.tils.smp.modules.discord.whitelist;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import yv.tils.smp.SMPPlugin;
import yv.tils.smp.logger.ConsoleLog;
import yv.tils.smp.modules.discord.BotManager.BotStartStop;
import yv.tils.smp.modules.discord.EmbedManager.whitelist.RoleHierarchyError;
import yv.tils.smp.placeholder.MessagePlaceholder;
import yv.tils.smp.placeholder.StringReplacer;
import yv.tils.smp.utils.configs.discord.DiscordConfigManager;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 4.6.8
 * @since 4.6.8.1
 */
public class ForceRemove extends ListenerAdapter {

    public StringSelectMenu.Builder createMenu(int site) {

        List<String> list = SMPPlugin.getInstance().WhitelistManager;

        if (list.size()-1 <= 0) {
            return StringSelectMenu.create("players").setPlaceholder(LanguageFile.getMessage(LanguageMessage.WHITELIST_EMPTY)).setDisabled(true).addOption("null", "null");
        }

        List<SelectOption> options = new ArrayList<>();

        int a = ((site-1)*25)+1;

        for (int i = a; i < list.size(); i++) {
            options.add(SelectOption.of(list.get(i), list.get(i)));
            if (i >= a+24) break;
        }

        return StringSelectMenu.create("players").setPlaceholder("Discord Tag,Minecraft Name, UUID")
                .addOptions(options);
    }

    public void onStringSelectInteraction(StringSelectInteractionEvent e) {
        List<String> list = SMPPlugin.getInstance().WhitelistManager;

        if (e.getInteraction().getValues().isEmpty()) return;
        else {
            Guild guild = e.getGuild();

            String values = e.getValues().toString();

            values = values.replace("[", "");
            values = values.replace("]", "");

            String[] args = values.split(",");

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

            list.remove(args[0] + "," + args[1] + "," + args[2]);
            player.setWhitelisted(false);
            new DiscordConfigManager().LinkedWriter(args[0], null);

            User user = null;
            try {
                user = BotStartStop.getInstance().jda.getUserById(args[0]);
            }catch (NumberFormatException ignored) {
                reply(e.getMember().getUser().getGlobalName(), args[1], args[0], list.size(), args, e);
                return;
            }

            new ConsoleLog(user + " " + args[0]);

            try {
                try {
                    String role = new DiscordConfigManager().ConfigRequest().getString("WhitelistFeature.Role");
                    role = role.replace(" ", "");
                    String[] roles = role.split(",");
                    for (String s : roles) {
                        guild.removeRoleFromMember(user, guild.getRoleById(s)).queue();
                    }
                }catch (HierarchyException ignored) {
                    e.reply("").setEmbeds(new RoleHierarchyError().Embed(new DiscordConfigManager().ConfigRequest().getString("WhitelistFeature.Role"), guild).build()).setEphemeral(true).queue();
                }
            }catch (IllegalArgumentException ignored) {}

            reply(e.getMember().getUser().getGlobalName(), args[1], args[0], list.size(), args, e);
            }
    }

    private void reply(String exec, String mc, String dc, int listSize, String[] args, StringSelectInteractionEvent e) {
        List<String> list1 = new ArrayList();
        List<String> list2 = new ArrayList();
        list1.add("DISCORDUSER");
        list2.add(exec);
        list1.add("MCNAME");
        list2.add(mc);
        list1.add("DCNAME");
        list2.add(dc);

        Bukkit.getConsoleSender().sendMessage(new StringReplacer().ListReplacer(MessagePlaceholder.PREFIXDC + " §f" + LanguageFile.getMessage(LanguageMessage.MODULE_DISCORD_CMD_REGISTERED_REMOVE), list1, list2));
        e.editMessageEmbeds(new yv.tils.smp.modules.discord.EmbedManager.whitelist.discord.ForceRemove().EmbedRemoved((listSize-1), Bukkit.hasWhitelist(), args, 1).build()).setActionRow(createMenu(1).build()).queue();
    }
}