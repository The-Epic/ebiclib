package xyz.epicebic.ebiclib.config.adapter;

import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import xyz.epicebic.ebiclib.collection.WeightedRandom;
import xyz.epicebic.ebiclib.config.ConfigType;
import xyz.epicebic.ebiclib.config.ConfigurationManager;

public class WeightedRandomAdapter<T> implements TypeAdapter<WeightedRandom<T>> {

    private final StringAdapter<T> adapter;

    @SuppressWarnings("unchecked")
    public WeightedRandomAdapter(ConfigurationManager manager, ConfigType<?> type) {
        this.adapter = (StringAdapter<T>) manager.getStringAdapter(type.getComponentTypes().get(0));
    }

    public WeightedRandom<T> read(ConfigurationSection config, String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        WeightedRandom<T> random = new WeightedRandom<>();

        for (String key : section.getKeys(false)) {
            double weight = section.getDouble(key);
            T value = this.adapter.fromString(key);

            random.add(weight, value);
        }
        return random;
    }

    @SuppressWarnings("unchecked")
    public void write(ConfigurationSection config, String path, Object value, boolean replace) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (config.isSet(path) && !replace) {
            return;
        }
        
        if (section == null) {
            section = config.createSection(path);
        }

        for (Entry<Double, T> entry : ((WeightedRandom<T>) value).getEntries()) {
            section.set(this.adapter.toString(entry.getValue()), entry.getKey());
        }
    }
}
