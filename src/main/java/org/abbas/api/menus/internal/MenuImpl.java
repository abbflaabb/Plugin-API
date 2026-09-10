package org.abbas.api.menus.internal;

import org.abbas.api.interfaces.IMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuImpl implements IMenu {
    private final int size;
    private final String title;
    private final Inventory inventory;

    public MenuImpl(int size, String title) {
        this.size = size;
        this.title = title;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public void close(Player player) {
        player.closeInventory();
    }

    @Override
    public void setItem(ItemStack itemStack, int slot) {
        inventory.setItem(slot, itemStack);
    }
}
