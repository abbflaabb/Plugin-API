package org.abbas.api.menus.internal;

import net.kyori.adventure.text.Component;
import org.abbas.PluginAPI.internal.InternalEventBridge;
import org.abbas.api.interfaces.IMenu;
import org.abbas.api.interfaces.IMenuItem;
import org.abbas.api.interfaces.MenusAPI;
import org.bukkit.inventory.ItemStack;

public class MenusAPIImpl implements MenusAPI {

    private final InternalEventBridge eventBridge;

    public MenusAPIImpl(InternalEventBridge eventBridge) {
        this.eventBridge = eventBridge;
    }

    @Override
    public IMenu createMenu(Component title, int size) {
        MenuImpl menu = new MenuImpl(size, title);

        eventBridge.registerMenu(menu);

        return menu;
    }

    @Override
    public IMenuItem createItem(ItemStack itemStack) {
        return new MenuItemImpl(itemStack);
    }
}