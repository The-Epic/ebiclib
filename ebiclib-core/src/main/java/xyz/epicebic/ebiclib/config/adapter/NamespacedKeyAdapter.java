package xyz.epicebic.ebiclib.config.adapter;

import org.bukkit.NamespacedKey;

public class NamespacedKeyAdapter implements StringAdapter<NamespacedKey> {

    @Override
    public String toString(NamespacedKey value) {
        return value.toString();
    }

    @Override
    public NamespacedKey fromString(String value) {
        return NamespacedKey.fromString(value);
    }
}
