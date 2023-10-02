package xyz.epicebic.ebiclib.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Optional;
import java.util.function.Consumer;

public class FileUtils {
    private FileUtils() {
    }

    public static final FilenameFilter YML_FILE_FILTER = (dir, name) -> name.endsWith("yml") || name.endsWith("yaml");

    public static void saveResource(@NotNull Plugin source, @NotNull String resourceName) {
        File resourceFile = new File(source.getDataFolder(), resourceName);

        // Copy file if needed
        if (!resourceFile.exists() && source.getResource(resourceName) != null) {
            source.saveResource(resourceName, false);
        }
    }

    public static Optional<File> loadResourceFile(@NotNull Plugin source, @NotNull String resourceName) {
        File resourceFile = new File(source.getDataFolder(), resourceName);

        // Copy file if needed
        if (!resourceFile.exists() && source.getResource(resourceName) != null) {
            source.saveResource(resourceName, false);
        }

        // File still doesn't exist, return empty
        if (!resourceFile.exists()) {
            return Optional.empty();
        }
        return Optional.of(resourceFile);
    }

    public static Optional<YamlConfiguration> loadResource(@NotNull Plugin source, @NotNull String resourceName) {
        Optional<File> optional = loadResourceFile(source, resourceName);

        if (optional.isPresent()) {
            return Optional.of(YamlConfiguration.loadConfiguration(optional.get()));
        } else {
            return Optional.empty();
        }
    }

    public static void visitFiles(@NotNull File folder, @NotNull Consumer<File> consumer) {
        visitFiles(folder, null, consumer);
    }

    public static void visitFiles(@NotNull File folder, FilenameFilter filter, @NotNull Consumer<File> consumer) {
        for (File file : folder.listFiles(filter)) {
            if (file.isDirectory()) {
                visitFiles(file, consumer);
            }

            consumer.accept(file);
        }
    }
}