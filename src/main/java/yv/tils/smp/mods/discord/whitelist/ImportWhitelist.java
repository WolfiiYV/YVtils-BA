package yv.tils.smp.mods.discord.whitelist;

import org.bukkit.configuration.file.YamlConfiguration;
import yv.tils.smp.YVtils;
import yv.tils.smp.utils.configs.discord.DiscordConfigManager;
import yv.tils.smp.utils.internalapi.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 4.6.8
 * @since 4.6.8.1
 */
public class ImportWhitelist {

    YamlConfiguration linkedRequest = new DiscordConfigManager().LinkedRequest();
    List<String> WhitelistManager = YVtils.getInstance().WhitelistManager;

    public void Importer() {
        //DC ID, MC NAME, UUID
        String WhitelistKeys = linkedRequest.getKeys(false).toString();
        String[] WhitelistKey = WhitelistKeys.split(", ");

        for (int i = 0; i < WhitelistKey.length; i++) {

            WhitelistKey[i] = WhitelistKey[i].replace("[", "");
            WhitelistKey[i] = WhitelistKey[i].replace("]", "");

            String content;

            if (linkedRequest.getString(WhitelistKey[i]) != null) {
                content = linkedRequest.getString(WhitelistKey[i]);
            }else {
                content = "null n-u-l-l";
            }

            String[] content_s = content.split(" ");

            WhitelistManager.add(WhitelistKey[i] + "," + content_s[0] + "," + content_s[1]);
        }
        new Log(WhitelistManager.toString());
    }


    /**
     * @since 4.6.8.1
     * request[0] = Discord ID;
     * request[1] = Minecraft NAME;
     * request[2] = Minecraft UUID
     */
    public List<String> reader(String dc, String mc, String uuid) {
        List<String> request = new ArrayList<>();
        loop:
            for (int i = 0; i < WhitelistManager.size(); i++) {
                String manager = WhitelistManager.get(i);

                String[] split = manager.split(",");

                for (int o = 0; o < split.length; o++) {
                    if (split[o].equals(dc) || split[o].equals(mc) || split[o].equals(uuid)) {

                        request.add(split[0]);
                        request.add(split[1]);
                        request.add(split[2]);

                        break loop;
                    }
                }
            }

        request.add("null");
        request.add("null");
        request.add("null");
        new Log(request.toString());
        return request;
    }
}