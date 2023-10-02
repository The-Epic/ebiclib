package xyz.epicebic.ebiclib.config.adapter;

import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

public class PrimitiveAdapter<T> implements StringAdapter<T> {

    private final Function<String, T> reader;

    public PrimitiveAdapter(Function<String, T> reader) {
        this.reader = reader;
    }

    @Override
    public void write(ConfigurationSection config, String path, Object value, boolean replace) {
        if (config.isSet(path) && !replace) {
            return;
        }
        
        config.set(path, value);
    }

    @Override
    public String toString(T value) {
        return String.valueOf(value);
    }

    @Override
    public T fromString(String value) {
        return reader.apply(value);
    }
}
