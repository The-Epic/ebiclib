package xyz.epicebic.ebiclib.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;

public class CustomInventoryManager implements Listener {
    private final HashMap<InventoryView, CustomInventory> inventoryMap = new HashMap<>();

    public void openInventory(HumanEntity player, CustomInventory inventory) {
        this.inventoryMap.put(inventory.open(player), inventory);
    }

    public CustomInventory getInventory(InventoryView view) {
        return this.inventoryMap.get(view);
    }

    public CustomInventory removeInventory(InventoryView view) {
        return this.inventoryMap.remove(view);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;

        CustomInventory inventory = getInventory(event.getView());

        if (inventory != null) {
            inventory.consumeClickEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        CustomInventory inventory = removeInventory(event.getView());

        if (inventory != null) {
            inventory.consumeCloseEvent(event);
        }
    }
}