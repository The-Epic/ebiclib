package xyz.epicebic.ebiclib.util;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Iterator;

public class MaterialUtils {
    private MaterialUtils() {
    }

    public static String getNiceMaterialName(final Material mat) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<String> iterator = Arrays.stream(mat.name().split("_")).iterator();
        while (iterator.hasNext()) {
            builder.append(WordUtils.upperCaseFirstLetterOnly(iterator.next()));
            if (iterator.hasNext()) builder.append(" ");
        }
        return builder.toString();
    }
}
