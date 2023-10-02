package xyz.epicebic.ebiclib.config.adapter;

import org.bukkit.Material;

public class MaterialAdapter implements StringAdapter<Material> {

    @Override
    public String toString(Material value) {
        return value.name();
    }

    @Override
    public Material fromString(String value) {
        return Material.matchMaterial(value);
    }
}
