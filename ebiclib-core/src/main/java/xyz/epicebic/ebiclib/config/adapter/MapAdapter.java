package xyz.epicebic.ebiclib.config.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import xyz.epicebic.ebiclib.config.ConfigType;
import xyz.epicebic.ebiclib.config.ConfigurationManager;

public class MapAdapter<K, V, T extends Map<K, V>> implements TypeAdapter<T> {
    private final TypeAdapter<V> valueAdapter;
    private final StringAdapter<K> keyAdapter;

    @SuppressWarnings("unchecked")
    public MapAdapter(ConfigurationManager manager, ConfigType<?> type) {
        this.keyAdapter = (StringAdapter<K>) manager.getStringAdapter(type.getComponentTypes().get(0));
        this.valueAdapter = (TypeAdapter<V>) manager.getAdapter(type.getComponentTypes().get(1));
    }

    @SuppressWarnings("unchecked")
    public T read(ConfigurationSection config, String path) {
        T map = (T) new LinkedHashMap<>();

        if (!config.isConfigurationSection(path)) {
            return map;
        }

        ConfigurationSection section = config.getConfigurationSection(path);
        boolean isString = (this.keyAdapter instanceof StringTypeAdapter);

        for (String key : section.getKeys(isString)) {
            if (isString && section.isConfigurationSection(key)) {
                continue;
            }
            
            K mapKey = this.keyAdapter.fromString(key);
            V mapValue = this.valueAdapter.read(section, key);
            map.put(mapKey, mapValue);

        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public void write(ConfigurationSection config, String path, Object value, boolean replace) {
        ((T) value).forEach((k, v) -> {
            String newPath = new StringBuilder(path).append(".").append(this.keyAdapter.toString(k)).toString();
            if (!replace && config.isSet(newPath)) {
                return;
            }
            this.valueAdapter.write(config, newPath, v, replace);
        });
    }
}
