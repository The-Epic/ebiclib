package xyz.epicebic.ebiclib.config.adapter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class ItemStackAdapter implements TypeAdapter<ItemStack> {
    @Override
    public ItemStack read(ConfigurationSection config, String path) {
        return ItemStack.deserialize(config.getConfigurationSection(path).getValues(true));
    }

    @Override
    public void write(ConfigurationSection config, String path, Object value, boolean replace) {
        if (config.isSet(path) && !replace) {
            return;
        }

        config.set(path, ((ItemStack) value).serialize());
    }
}
