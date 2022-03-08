package yv.tils.smp.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import yv.tils.smp.placeholder.LanguagePlaceholder;
import yv.tils.smp.SMPPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;

public class BotStartStop {



    File file1 = new File(SMPPlugin.getInstance().getDataFolder(), "MinecraftDiscordBridge.yml");
    YamlConfiguration modifyFile1 = YamlConfiguration.loadConfiguration(file1);

    String token = modifyFile1.getString("BotToken");
    String activity = modifyFile1.getString("Activity");
    String activitymessage = modifyFile1.getString("ActivityMessage");
    String activity_streaming_url = modifyFile1.getString("ActivityStreamingUrl");
    String status = modifyFile1.getString("OnlineStatus");


    public void TokenCheck() {
        if (token.equals(LanguagePlaceholder.ConfigCreateBotToken())) {
            Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.NoBotTokenGiven());
            Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.BotStartupFailed());
            return;
        }else {
            BotSettings();
        }
    }

    JDABuilder builder = JDABuilder.createDefault(token);

    public void BotSettings() {
        //Activity -> Streaming; Watching; Playing; Competing; None;

        switch (activity.toLowerCase()) {
            case "streaming" -> builder.setActivity(Activity.streaming(activitymessage, activity_streaming_url));
            case "watching" -> builder.setActivity(Activity.watching(activitymessage));
            case "playing" -> builder.setActivity(Activity.playing(activitymessage));
            case "competing" -> builder.setActivity(Activity.competing(activitymessage));
            case "none" -> builder.setActivity(null);
            default -> builder.setActivity(Activity.playing("Minecraft"));
        }

        //Status -> Online; Idle; DND; Offline; Invisible; Unknown

        switch (status.toLowerCase()) {
            case "online" -> builder.setStatus(OnlineStatus.ONLINE);
            case "idle" -> builder.setStatus(OnlineStatus.IDLE);
            case "dnd" -> builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
            case "offline" -> builder.setStatus(OnlineStatus.OFFLINE);
            case "invisible" -> builder.setStatus(OnlineStatus.INVISIBLE);
            default -> builder.setStatus(OnlineStatus.ONLINE);
        }

        //register

        builder.addEventListeners(new WhitelistMessageGetter());

        try {
            SMPPlugin.getInstance().jda = builder.build();
            Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.BotStartupFinished());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        if (SMPPlugin.getInstance().jda != null) {
            try {
                builder.setStatus(OnlineStatus.OFFLINE);
                SMPPlugin.getInstance().jda.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("------------------------");
                e.getCause();
                System.out.println("------------------------");
            }
        }
    }



}
