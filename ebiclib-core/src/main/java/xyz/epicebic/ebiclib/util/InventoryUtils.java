package xyz.epicebic.ebiclib.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryUtils {
    private InventoryUtils() {

    }

    public static int getTotalAmount(@NotNull Inventory inventory, @NotNull ItemStack target) {
        int amount = 0;

        for (ItemStack item : inventory) {
            if (item == null) {
                continue;
            }

            if (item.isSimilar(target)) {
                amount += item.getAmount();
            }
        }
        return amount;
    }
}
