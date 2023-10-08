package xyz.epicebic.ebiclib.config;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import xyz.epicebic.ebiclib.collection.WeightedRandom;
import xyz.epicebic.ebiclib.config.adapter.*;
import xyz.epicebic.ebiclib.config.annotation.ConfigMappable;

public class ConfigurationManager {

    private final Map<ConfigType<?>, TypeAdapter<?>> adapters = new HashMap<>();
    private final Logger logger;

    public ConfigurationManager(Plugin plugin) {
        this.logger = plugin.getLogger();

        registerTypeAdapter(long.class, new PrimitiveAdapter<>(Long::parseLong));
        registerTypeAdapter(Long.class, new PrimitiveAdapter<>(Long::parseLong));

        registerTypeAdapter(int.class, new PrimitiveAdapter<>(Integer::parseInt));
        registerTypeAdapter(Integer.class, new PrimitiveAdapter<>(Integer::parseInt));

        registerTypeAdapter(double.class, new PrimitiveAdapter<>(Double::parseDouble));
        registerTypeAdapter(Double.class, new PrimitiveAdapter<>(Double::parseDouble));

        registerTypeAdapter(float.class, new PrimitiveAdapter<>(Float::parseFloat));
        registerTypeAdapter(Float.class, new PrimitiveAdapter<>(Float::parseFloat));

        registerTypeAdapter(boolean.class, new PrimitiveAdapter<>(Boolean::parseBoolean));
        registerTypeAdapter(Boolean.class, new PrimitiveAdapter<>(Boolean::parseBoolean));

        registerTypeAdapter(String.class, new StringTypeAdapter());
        registerTypeAdapter(NamespacedKey.class, new NamespacedKeyAdapter());
        registerTypeAdapter(Material.class, new MaterialAdapter());

        registerTypeAdapter(ItemStack.class, new ItemStackAdapter());

    }

    public <T> void registerTypeAdapter(Class<T> clazz, TypeAdapter<T> adapter) {
        this.adapters.put(new ConfigType<>(clazz), adapter);
    }

    public <T> ReloadableObject<T> createReloadable(File file, T target) {
        return new ReloadableObject<>(this, file, target);
    }

    public <T> ReloadableClass<T> createStaticReloadable(File file, Class<T> target) {
        return new ReloadableClass<>(this, file, target);
    }

    public <T> StringAdapter<T> getStringAdapter(ConfigType<T> type) {
        TypeAdapter<T> adapter = getAdapter(type);

        if (adapter instanceof StringAdapter<T> stringAdapter) {
            return stringAdapter;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getAdapter(ConfigType<T> type) {
        TypeAdapter<T> adapter = (TypeAdapter<T>) this.adapters.get(type);

        if (adapter != null) {
            return adapter;
        }

        adapter = (TypeAdapter<T>) createAdapter(type);
        this.adapters.put(type, adapter);
        return adapter;
    }

    private TypeAdapter<?> createAdapter(ConfigType<?> type) {
        if (Enum.class.isAssignableFrom(type.getType())) {
            return new EnumAdapter<>(type.getType());
        }
        if (Collection.class.isAssignableFrom(type.getType())) {
            return new CollectionAdapter<>(this, type);
        }

        if (Map.class.isAssignableFrom(type.getType())) {
            return new MapAdapter<>(this, type);
        }

        if (WeightedRandom.class.isAssignableFrom(type.getType())) {
            return new WeightedRandomAdapter<>(this, type);
        }

        if (type.getType().isAnnotationPresent(ConfigMappable.class)) {
            return new ConfigMappableAdapter<>(this, type);
        }
        return null;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
