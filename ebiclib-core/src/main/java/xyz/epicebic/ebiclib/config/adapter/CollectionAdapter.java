package xyz.epicebic.ebiclib.config.adapter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;

import xyz.epicebic.ebiclib.config.ConfigType;
import xyz.epicebic.ebiclib.config.ConfigurationManager;

public class CollectionAdapter<V, T extends Collection<V>> implements TypeAdapter<T> {
    private static Map<Class<?>, Supplier<? extends Collection<?>>> defaults;

    static {
        defaults = new HashMap<>();
        defaults.put(List.class, ArrayList::new);
        defaults.put(Set.class, LinkedHashSet::new);
        defaults.put(Queue.class, ArrayDeque::new);
    }

    private final StringAdapter<V> adapter;
    private final ConfigType<T> type;

    @SuppressWarnings("unchecked")
    public CollectionAdapter(ConfigurationManager manager, ConfigType<?> type) {
        this.adapter = (StringAdapter<V>) manager.getStringAdapter(type.getComponentTypes().get(0));
        this.type = (ConfigType<T>) type;
    }

    @SuppressWarnings("unchecked")
    public T read(ConfigurationSection config, String path) {
        T collection = (T) defaults.get(this.type.getType()).get();
        List<String> list = config.getStringList(path);

        list.forEach(value -> collection.add(this.adapter.fromString(value)));

        return collection;
    }

    @SuppressWarnings("unchecked")
    public void write(ConfigurationSection config, String path, Object value, boolean replace) {
        List<String> list = config.getStringList(path);
        if (replace) {
            list.clear();
        }

        ((T) value).forEach(entry -> {
            String toAdd = this.adapter.toString(entry);
            if (!list.contains(toAdd)) {
                list.add(toAdd);
            }
        });
        config.set(path, list);
    }
}
