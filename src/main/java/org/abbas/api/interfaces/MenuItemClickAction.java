package org.abbas.api.interfaces;

import org.abbas.api.events.menus.MenuClickEvent;

public interface MenuItemClickAction {
    void execute(MenuClickEvent event);
}
