package xyz.epicebic.ebiclib.util;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

public class WordUtils {
    private WordUtils() {
    }

    public static String upperCaseFirstLetterOnly(final String word) {
        return upperCaseFirstLetter(word.toLowerCase(Locale.ROOT));
    }

    public static String upperCaseFirstLetter(final String word) {
        if (word.length() < 1) return word;
        if (word.length() == 1) return word.toUpperCase(Locale.ROOT);
        return word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1);
    }


    public static String getNiceName(@NotNull final NamespacedKey key) {
        return getNiceName(key.getKey());
    }

    public static String getNiceName(@NotNull final String string) {
        final String[] split = string.split("_");
        final Iterator<String> iterator = Arrays.stream(split).iterator();
        final StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(upperCaseFirstLetterOnly(iterator.next().toLowerCase(Locale.ROOT)));
            if (iterator.hasNext()) builder.append(" ");
        }
        return builder.toString();
    }
}
