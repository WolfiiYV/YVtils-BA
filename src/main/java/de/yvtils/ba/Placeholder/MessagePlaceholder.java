package de.yvtils.ba.Placeholder;

import de.yvtils.ba.Main;

public class MessagePlaceholder{
    public static String PREFIX = Main.getInstance().getConfig().getString("Placeholder.PREFIX");
    public static String PREFIXBA = Main.getInstance().getConfig().getString("Placeholder.PREFIXBA");
    public static String PREFIXLOBBY = Main.getInstance().getConfig().getString("Placeholder.PREFIXLOBBY");
    public static String PREFIXKICK = Main.getInstance().getConfig().getString("Placeholder.PREFIXKICK");
    public static String PREFIXCONNECT = Main.getInstance().getConfig().getString("Placeholder.PREFIXCONNECT");
    public static String PREFIXDISCONNECT = Main.getInstance().getConfig().getString("Placeholder.PREFIXDISCONNECT");
    public static String PREFIXENABLE = Main.getInstance().getConfig().getString("Placeholder.PREFIXENABLE");
    public static String PREFIXDISABLE = Main.getInstance().getConfig().getString("Placeholder.PREFIXDISABLE");
    public static String PREFIXERROR = Main.getInstance().getConfig().getString("Placeholder.PREFIXERROR");
    public static String PREFIXERELOAD = Main.getInstance().getConfig().getString("Placeholder.PREFIXERELOAD");
    public static String PREFIXUPDATE = Main.getInstance().getConfig().getString("Placeholder.PREFIXUPDATE");
    public static String PREFIXNOUPDATE = Main.getInstance().getConfig().getString("Placeholder.PREFIXNOUPDATE");

}
