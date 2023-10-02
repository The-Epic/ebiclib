package xyz.epicebic.ebiclib.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.epicebic.ebiclib.util.MathsUtils;

import java.util.Arrays;
import java.util.List;

public abstract class PagedArrayCustomInventory<T> extends CustomInventory {
    private final List<T[]> items;
    private final int itemsPerPage;
    private final int maxPage;

    private int page = 0;

    protected PagedArrayCustomInventory(Inventory inventory, List<T[]> items, int maxIndex) {
        super(inventory);
        this.items = items;
        this.itemsPerPage = maxIndex;
        this.maxPage = this.items.size() - 1;
    }

    protected abstract ItemStack asItemStack(T entry);

    protected abstract void onItemClicked(InventoryClickEvent event, T item);

    protected void refreshOptions() {
        T[] entry = this.items.get(page);
        ItemStack[] stackArray = Arrays.stream(entry).map(this::asItemStack).toArray(ItemStack[]::new);

        for (int i = 0; i < this.itemsPerPage; i++) {
            if (i >= stackArray.length) {
                setItem(i, null);
                removeButton(i);
            } else {
                addButton(i, stackArray[i], this::onClick);
            }
        }
    }

    protected void changePage(int amount) {
        this.page = MathsUtils.clamp(this.page + amount, 0, this.maxPage);

        refreshOptions();
    }

    protected int getPage() {
        return this.page;
    }

    private void onClick(InventoryClickEvent event) {
        T[] entry = this.items.get(this.page);
        onItemClicked(event, entry[event.getSlot()]);
    }
}