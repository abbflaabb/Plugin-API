package org.abbas.api.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IMenu {
    void open(Player player);
    void close(Player player);
    void setItem(ItemStack itemStack, int slot);
    interface MenuItem {
        void setDisplayName(String displayName);
        String getDisplayName();
    }
}
