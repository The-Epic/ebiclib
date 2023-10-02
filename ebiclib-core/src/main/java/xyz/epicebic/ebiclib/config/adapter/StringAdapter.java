package xyz.epicebic.ebiclib.config.adapter;

import org.bukkit.configuration.ConfigurationSection;

public interface StringAdapter<T> extends TypeAdapter<T> {
    public String toString(T value);

    public T fromString(String value);

    default T read(ConfigurationSection config, String path) {
        String value = config.getString(path);
        return value == null ? null : fromString(value);
    }

    @SuppressWarnings("unchecked")
    default void write(ConfigurationSection config, String path, Object value, boolean replace) {
        if (value == null) {
            return;
        }
        
        if (config.isSet(path) && !replace) {
            return;
        }

        config.set(path, toString((T) value));
    }
}
