package org.abbas.api.events.menus;

import org.abbas.api.interfaces.IMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuClickEvent extends Event implements Cancellable {
    public static HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final IMenu menu;
    private final int slot;
    private final ItemStack item;
    private final InventoryAction action;
    private final ClickType clickType;
    boolean cancelled;
    public MenuClickEvent(Player player, IMenu menu, int slot, ItemStack item, InventoryAction action, ClickType clickType) {
        this.player = player;
        this.menu = menu;
        this.slot = slot;
        this.item = item;
        this.action = action;
        this.clickType = clickType;
    }
    public Player getPlayer() {
        return  player;
    }
    public IMenu getMenu() {
        return  menu;
    }
    public int getSlot() {
        return   slot;
    }
    public ItemStack getItem() {
        return   item;
    }
    public InventoryAction getAction() {
        return   action;
    }
    public ClickType getClickType() {
        return    clickType;
    }
    public boolean isRightClick() {
        return clickType.isRightClick();
    }
    public boolean isLeftClick() {
        return clickType.isLeftClick();
    }
    public boolean isShiftClick() {
        return clickType.isShiftClick();
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return  HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
