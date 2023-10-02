package xyz.epicebic.ebiclib.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;

import xyz.epicebic.ebiclib.config.adapter.TypeAdapter;
import xyz.epicebic.ebiclib.config.annotation.ConfigEntry;
import xyz.epicebic.ebiclib.config.annotation.PostLoad;

public abstract class ConfigReloadable<T> {
    protected final ConfigurationManager manager;
    protected final File file;
    protected final List<ConfigField> fields = new ArrayList<>();
    private final Method postLoadMethod;
    protected final Logger logger;

    protected ConfigReloadable(ConfigurationManager manager, File file, Class<T> clazz) {
        this.manager = manager;
        this.file = file;
        this.postLoadMethod = findPostLoadMethod(clazz);
        this.logger = manager.getLogger();

        cacheFields(clazz);
       // Collections.reverse(this.fields);
    }

    public ConfigReloadable<T> load() {
        load(true);

        return this;
    }

    public ConfigReloadable<T> load(boolean includeStatic) {
        if (!prepareFile()) {
            // File error
            return this;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (ConfigField field : this.fields) {
            if (field.isStatic() && !includeStatic) {
                continue;
            }

            String path = field.getPath();

            if (!config.isSet(path)) {
                logger.log(Level.WARNING, "No configuration entry found for {0}", path);
                continue;
            }

            ConfigType<?> type = ConfigType.get(field.getField());
            TypeAdapter<?> adapter = this.manager.getAdapter(type);
            if (adapter == null) {
                logger.log(Level.WARNING, "No configuration adapter found for {0}", type.getClass());
                continue;
            }

            Object readValue = adapter.read(config, path);
            if (readValue == null) {
                logger.log(Level.WARNING, "Failed to read value for {0} of type {1}",
                        new Object[] { path, type.getType() });
                continue;
            }

            try {
                setField(field, readValue);
            } catch (ReflectiveOperationException ex) {
                logger.log(Level.WARNING, "Failed to read value for {0} of type {1}",
                        new Object[] { path, type.getType() });
                ex.printStackTrace();
            }
        }
        
        if (this.postLoadMethod != null) {
            this.postLoad(this.postLoadMethod);
        }
        return this;
    }

    public ConfigReloadable<T> saveDefaults() {
        save(false);

        return this;
    }

    public ConfigReloadable<T> save() {
        save(true);

        return this;
    }

    public ConfigReloadable<T> save(boolean replace) {
        if (!prepareFile()) {
            // File error
            return this;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (ConfigField field : this.fields) {
            String path = field.getPath();

            if (!path.isEmpty()) {
                config.setComments(path, field.getComments());
            }

            ConfigType<?> type = ConfigType.get(field.getField());
            TypeAdapter<?> adapter = this.manager.getAdapter(type);
            if (adapter == null) {
                logger.log(Level.WARNING, "No configuration adapter found for {0}", type.getType());
                continue;
            }

            Object writeValue = getField(field);
            if (writeValue == null) {
                logger.log(Level.WARNING, "null");
                continue;
            }

            adapter.write(config, path, writeValue, replace);
            config.setComments(path, field.getComments());
        }

        try {
            config.save(file);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to save configuration file {0}", file.getName());
            ex.printStackTrace();
        }
        return this;
    }

    protected abstract void setField(ConfigField field, Object value) throws ReflectiveOperationException;

    protected abstract Object getField(ConfigField field);
    
    protected abstract void postLoad(Method postLoadMethod2);

    private boolean prepareFile() {
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                logger.log(Level.WARNING, "Error creating file {0}", file.getName());
                return false;
            }

            if (!file.exists()) {
                return file.createNewFile();
            }

            return true;
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error creating file {0}", file.getName());
            return false;
        }
    }

    private void cacheFields(Class<? super T> clazz) {
        int index = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigEntry.class)) {
                continue;
            }

            this.fields.add(index++, new ConfigField(field));
        }

        Class<? super T> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            cacheFields(superClass);
        }
    }
    
    private Method findPostLoadMethod(Class<? super T> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostLoad.class)) {
                return method;
            }
        }

        Class<? super T> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return findPostLoadMethod(superClass);
        }

        return null;
    }
}
