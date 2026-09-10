package org.abbas.api.menus.internal;

import org.abbas.api.interfaces.IMenu;
import org.abbas.api.interfaces.MenusAPI;

public class MenusAPIImpl implements MenusAPI {
    @Override
    public IMenu createMenu(String title, int size) {
        return new MenuImpl(size,title);
    }

}
