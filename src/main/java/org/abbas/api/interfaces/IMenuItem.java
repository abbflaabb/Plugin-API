package org.abbas.api.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public interface IMenuItem {

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);

    Component getDisplayName();
    void setClickAction(MenuItemClickAction action);
    MenuItemClickAction getClickAction();
    void setDisplayName(Component displayName);
}