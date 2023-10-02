package xyz.epicebic.ebiclib.util;

import org.bukkit.Bukkit;

public class VersionUtils {
    private VersionUtils() {
    }

    public static String getServerVersion() {
        return Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-"));
    }

    public static String getServerVersionUnderscores() {
        return getServerVersion().replace(".", "_");
    }

    public static String getServerNMSVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
