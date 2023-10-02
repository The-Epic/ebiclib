package xyz.epicebic.ebiclib.config.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import xyz.epicebic.ebiclib.config.ConfigField;
import xyz.epicebic.ebiclib.config.ConfigType;
import xyz.epicebic.ebiclib.config.ConfigurationManager;
import xyz.epicebic.ebiclib.config.ReflectionHelper;
import xyz.epicebic.ebiclib.config.annotation.ConfigEntry;

public class ConfigMappableAdapter<T> implements TypeAdapter<T> {
    private final ConfigurationManager manager;
    private final List<ConfigField> fields = new ArrayList<>();
    private final ConfigType<T> type;

    @SuppressWarnings("unchecked")
    public ConfigMappableAdapter(ConfigurationManager manager, ConfigType<?> type) {
        this.manager = manager;
        this.type = (ConfigType<T>) type;

        cacheFields(this.type.getType());
    }

    public T read(ConfigurationSection config, String path) {
        try {
            T object = this.type.getType().getDeclaredConstructor().newInstance();

            for (ConfigField field : this.fields) {
                String fullPath = path + "." + field.getPath();

                if (!config.isSet(fullPath)) {
                    // logger.log(Level.WARNING, "No configuration entry found for {0}", path);
                    continue;
                }

                ConfigType<?> type = ConfigType.get(field.getField());
                TypeAdapter<?> adapter = this.manager.getAdapter(type);
                if (adapter == null) {
                    // logger.log(Level.WARNING, "No configuration adapter found for {0}",
                    // type.getClass());
                    continue;
                }

                Object readValue = adapter.read(config, fullPath);
                if (readValue == null) {
                    // logger.log(Level.WARNING, "Failed to read value for {0} of type {1}",
                    // new Object[] { path, type.getType() });
                    continue;
                }

                ReflectionHelper.setField(field, readValue, object);
            }

            return object;
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void write(ConfigurationSection config, String path, Object value, boolean replace) {
        for (ConfigField field : this.fields) {
            String fullPath = path + "." + field.getPath();

            if (!fullPath.isEmpty()) {
                config.setComments(fullPath, field.getComments());
            }

            ConfigType<?> type = ConfigType.get(field.getField());
            TypeAdapter<?> adapter = this.manager.getAdapter(type);
            if (adapter == null) {
                // logger.log(Level.WARNING, "No configuration adapter found for {0}",
                // type.getType());
                continue;
            }

            Object writeValue = ReflectionHelper.getField(field, value);
            if (writeValue == null) {
                // logger.log(Level.WARNING, "null");
                continue;
            }

            adapter.write(config, fullPath, writeValue, replace);
            config.setComments(fullPath, field.getComments());
        }
    }

    private void cacheFields(Class<? super T> class1) {
        for (Field field : class1.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigEntry.class)) {
                continue;
            }

            this.fields.add(new ConfigField(field));
        }

        Class<? super T> superClass = class1.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            cacheFields(superClass);
        }
    }
}
