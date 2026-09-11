package org.abbas.api.interfaces;


import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public interface MenusAPI {
    IMenu createMenu(Component title, int size);
    IMenuItem createItem(ItemStack item);
}
