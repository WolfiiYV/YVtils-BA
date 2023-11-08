package yv.tils.smp.mods.discord.EmbedManager.whitelist.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import yv.tils.smp.internalapi.StringReplacer;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 4.6.8.1
 * @since 4.6.8
 */
public class ForceRemove {
    EmbedBuilder builder = new EmbedBuilder();
    String url = "https://yvnetwork.de/wp-content/uploads/2022/03/YVtils-SMP.png";

    public EmbedBuilder Embed(Integer playercount, boolean whitelist, int site) {

        String status;
        if (whitelist) status = "on";
        else status = "off";

        int maxsite = (playercount - 1)/25+1;

        return builder
                .setTitle(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_REMOVE_TITLE))
                .setDescription(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_REMOVE_DESC))
                .addField("Whitelisted Players:", String.valueOf(playercount), true)
                .addField("Whitelist Status", status, true)
                .setColor(new Color(0xBA4C59))
                .setFooter("YVtils-SMP • Site " + site + "/" + maxsite, "https://yvnetwork.de/wp-content/uploads/2022/03/YVtils-SMP.png")
                .setAuthor("Whitelist Administration", "https://yvnetwork.de/yvtils-smp/spigot", url);
    }

    public EmbedBuilder EmbedRemoved(Integer playercount, boolean whitelist, String[] acc, int site) {

        String status;
        if (whitelist) status = "on";
        else status = "off";

        int maxsite = (playercount - 1)/25+1;

        List<String> list1 = new ArrayList();
        List<String> list2 = new ArrayList();
        list1.add("MCNAME");
        list2.add(acc[1]);
        list1.add("DCNAME");

        if (acc[0].matches("\\d+")) {
            list2.add("<@" + acc[0] + ">");
        }else {
            list2.add(acc[0]);
        }

        return builder
                .setTitle(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_REMOVED_TITLE), list1, list2))
                .setDescription(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_REMOVED_DESC))
                .addField("Whitelisted Players:", String.valueOf(playercount), true)
                .addField("Whitelist Status", status, true)
                .setColor(new Color(0xBA4C59))
                .setFooter("YVtils-SMP • Site " + site + "/" + maxsite, "https://yvnetwork.de/wp-content/uploads/2022/03/YVtils-SMP.png")
                .setAuthor("Whitelist Administration", "https://yvnetwork.de/yvtils-smp/spigot", url);
    }

}