package yv.tils.smp.utils.configs.discord;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import yv.tils.smp.SMPPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @since 4.6.8
 * @version 4.6.8
 */
public class LinkedAccountsConfig {
    File file = new File(SMPPlugin.getInstance().getDataFolder() + "/discord", "linkedAccounts.yml");
    YamlConfiguration ymlfile = YamlConfiguration.loadConfiguration(file);

    public void StringInput() {
        ymlfile.addDefault("DiscordName#Tag", "Minecraft Username + UUID");
        ymlfile.options().copyDefaults(true);
        fileSave();
    }

    public void fileSave() {
        try {
            ymlfile.save(file);
        } catch (IOException e) {
            System.out.println("-------");
            Bukkit.getConsoleSender().sendMessage("File creation Error! Please get Support on the YVtils Support Discord");
            System.out.println("-------");
        }
    }
}
