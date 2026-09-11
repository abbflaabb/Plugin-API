package org.abbas.api.menus.internal;

import net.kyori.adventure.text.Component;
import org.abbas.api.interfaces.IMenu;
import org.abbas.api.interfaces.IMenuItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class MenuImpl implements IMenu {

    private final int size;
    private final Component title;
    private final Inventory inventory;

    private final Map<Integer, IMenuItem> items = new HashMap<>();
    private boolean editable;

    public MenuImpl(int size, Component title) {
        this.size = size;
        this.title = title;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Component getTitle() {
        return title;
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
    public void setItem(IMenuItem item, int slot) {
        items.put(slot, item);
        inventory.setItem(slot, item.getItemStack());
    }

    @Override
    public IMenuItem getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public void clear() {
        items.clear();
        inventory.clear();
    }
    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}