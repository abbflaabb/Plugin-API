package org.abbas.api.menus.internal;

import net.kyori.adventure.text.Component;
import org.abbas.api.interfaces.IMenuItem;
import org.abbas.api.interfaces.MenuItemClickAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuItemImpl implements IMenuItem {

    private ItemStack itemStack;
    private MenuItemClickAction clickAction;

    public MenuItemImpl(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Component getDisplayName() {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }

        return itemStack.getItemMeta().displayName();
    }

    @Override
    public void setDisplayName(Component displayName) {
        if (itemStack == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.displayName(displayName);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void setClickAction(MenuItemClickAction action) {
        this.clickAction = action;
    }

    @Override
    public MenuItemClickAction getClickAction() {
        return clickAction;
    }
}