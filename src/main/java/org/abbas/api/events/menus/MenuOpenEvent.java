package org.abbas.api.events.menus;

import org.abbas.api.interfaces.IMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MenuOpenEvent extends Event {
    public static HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final IMenu menu;

    public MenuOpenEvent(Player player, IMenu menu) {
        this.player = player;
        this.menu = menu;
    }
    public Player getPlayer() {
        return player;
    }
    public IMenu getMenu() {
        return  menu;
    }

    public static HandlerList getHandlerList()
    {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers()
    {
        return HANDLERS;
    }
}
