package yv.tils.smp.modules.discord.CommandManager;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import org.bukkit.Bukkit;
import yv.tils.smp.SMPPlugin;
import yv.tils.smp.modules.discord.EmbedManager.commands.ServerInfoEmbed;
import yv.tils.smp.modules.discord.EmbedManager.whitelist.ForceAdd;
import yv.tils.smp.modules.discord.EmbedManager.whitelist.AccountCheck;
import yv.tils.smp.modules.discord.EmbedManager.whitelist.ForceRemove;

import java.io.File;

public class CommandHandler extends ListenerAdapter  {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        String command = e.getName();
        String args = e.getSubcommandName();

        switch (command) {
            case "mcinfo" -> {

                System.out.println(e.getMember().getPermissions());

                File serverIcon = new File(".\\server-icon.png");
                if (serverIcon.exists()) {
                    e.reply("").setEmbeds(new ServerInfoEmbed().Embed(e.getUser()).build()).setEphemeral(true).addFiles(FileUpload.fromData(serverIcon, "server-icon.png")).queue();
                }else {
                    e.reply("").setEmbeds(new ServerInfoEmbed().Embed(e.getUser()).build()).setEphemeral(true).queue();
                }
            }
            case "whitelist" -> {
                switch (args) {
                    case "forceadd" -> { //DC Name + MC Name
                        try {
                            e.reply("").setEmbeds(new ForceAdd().onMessageReceived(e.getOption("mc_name").getAsString(), e.getOption("dc_name").getAsMember(), e.getMember()).build()).setEphemeral(true).queue();
                        }catch (NullPointerException ignored) {
                            Member member = null;
                            e.reply("").setEmbeds(new ForceAdd().onMessageReceived(e.getOption("mc_name").getAsString(), member, e.getMember()).build()).setEphemeral(true).queue();
                        }
                    }
                    case "forceremove" -> {
                        e.reply("").setEmbeds(new yv.tils.smp.modules.discord.EmbedManager.whitelist.Embed.discord.ForceRemove().Embed((SMPPlugin.getInstance().WhitelistManager.size()-1), Bukkit.hasWhitelist()).build()).addActionRow(new ForceRemove().createMenu().build()).setEphemeral(true).queue();
                    }
                    case "check" -> {
                        e.reply("").setEmbeds(new AccountCheck().WhitelistCheck(e.getOption("name").getAsString()).build()).setEphemeral(true).queue();
                    }
                }
            }
        }
    }
}
