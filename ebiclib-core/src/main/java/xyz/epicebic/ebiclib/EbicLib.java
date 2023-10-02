package xyz.epicebic.ebiclib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import xyz.epicebic.ebiclib.config.ConfigurationManager;
import xyz.epicebic.ebiclib.inventory.CustomInventoryManager;

import java.util.logging.Logger;

public class EbicLib {

    private static EbicLib instance;

    private final Plugin plugin;
    private final ConfigurationManager configManager;
    private final CustomInventoryManager inventoryManager = new CustomInventoryManager();

    public EbicLib(Plugin plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigurationManager(plugin);
    }

    public static void initialize(Plugin plugin) {
        instance = new EbicLib(plugin);
        Bukkit.getPluginManager().registerEvents(getInventoryManager(), plugin);
    }

    public static void cleanup() {
        instance = null;
    }

    public static EbicLib getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialized");
        }

        return instance;
    }

    public static Plugin getPluginInstance() {
        return getInstance().plugin;
    }

    public static Logger getLogger() {
        return getInstance().plugin.getLogger();
    }

    public static ConfigurationManager getConfigurationManager() {
        return getInstance().configManager;
    }

    public static CustomInventoryManager getInventoryManager() {
        return getInstance().inventoryManager;
    }
}
