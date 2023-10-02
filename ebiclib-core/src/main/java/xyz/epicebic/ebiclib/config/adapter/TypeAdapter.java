package xyz.epicebic.ebiclib.config.adapter;

import org.bukkit.configuration.ConfigurationSection;

public interface TypeAdapter<T> {

    T read(ConfigurationSection config, String path);
    
    void write(ConfigurationSection config, String path, Object value, boolean replace);
}
