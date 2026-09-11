package org.abbas.api.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IMenu {

    void open(Player player);

    void close(Player player);

    void setItem(IMenuItem item, int slot);

    IMenuItem getItem(int slot);
    void clear();

    Inventory getInventory();

    int getSize();

    Component getTitle();

    boolean isEditable();

    void setEditable(boolean editable);
}