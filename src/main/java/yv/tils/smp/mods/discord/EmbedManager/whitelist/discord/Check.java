package yv.tils.smp.mods.discord.EmbedManager.whitelist.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;
import yv.tils.smp.utils.internalapi.StringReplacer;

import java.awt.*;
import java.util.ArrayList;

/**
 * @version 4.6.8
 * @since 4.6.8
 */
public class Check {
    EmbedBuilder builder = new EmbedBuilder();
    String url = "https://yvnetwork.de/wp-content/uploads/2022/03/YVtils-SMP.png";

    public EmbedBuilder Embed(String mc, String dc, boolean b) {

        java.util.List<String> list1 = new ArrayList();
        java.util.List<String> list2 = new ArrayList();
        list1.add("MCNAME");
        list2.add(mc);
        list1.add("DCNAME");

        if (dc.matches("\\d+")) {
            list2.add("<@" + dc + ">");
        }else {
            list2.add(dc);
        }

        if (b) {
            return builder
                    .setTitle(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_CHECK_TITLE))
                    .setDescription(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_CHECK_WHITELISTED_DESC), list1, list2))
                    .setColor(new Color(0x85BA4C))
                    .setFooter("YVtils-SMP • https://yvnetwork.de/yvtils-smp/spigot", "https://yvnetwork.de/wp-content/uploads/2022/03/YVtils-SMP.png")
                    .setAuthor("Whitelist Administration", null, url);
        }else {
            return builder
                    .setTitle(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_CHECK_TITLE))
                    .setDescription(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.EMBED_CMD_WHITELIST_CHECK_NOT_WHITELISTED_DESC), list1, list2))
                    .setColor(new Color(0xCC4848))
                    .setFooter("YVtils-SMP • https://yvnetwork.de/yvtils-smp/spigot", "https://yvnetwork.de/wp-content/uploads/2022/03/YVtils-SMP.png")
                    .setAuthor("Whitelist Administration", null, url);
        }
    }
}