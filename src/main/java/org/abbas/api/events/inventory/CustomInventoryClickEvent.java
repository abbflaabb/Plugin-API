package org.abbas.api.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class CustomInventoryClickEvent extends Event implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Inventory inventory;
    private final InventoryView view;
    private final Player player;
    private int slot;
    private int rawSlot;
    private final ItemStack currentItem;
    private final ItemStack cursor;
    private final ClickType clickType;
    private final InventoryAction inventoryAction;

    public CustomInventoryClickEvent(Inventory inventory, InventoryView view, Player player, int slot, int rawSlot, ItemStack currentItem, ItemStack cursor, ClickType clickType, InventoryAction inventoryAction, boolean cancelled) {
        this.inventory = inventory;
        this.view = view;
        this.player = player;
        this.slot = slot;
        this.rawSlot = rawSlot;
        this.currentItem = currentItem;
        this.cursor = cursor;
        this.clickType = clickType;
        this.inventoryAction = inventoryAction;
        this.cancelled = cancelled;
    }
    public @NonNull Player getPlayer() {
        return  player;
    }
    public @NonNull Inventory getInventory() {
        return inventory;
    }
    public @NonNull InventoryView getView() {
        return  view;
    }
    public int getSlot() {
        return slot;
    }
    public int getSlotIndex() {
        return slot;
    }
    public int getRawSlot() {
        return  rawSlot;
    }
    public ItemStack getCurrentItem() {
        return currentItem;
    }
    public ItemStack getCursor() {
        return cursor;
    }
    public ClickType getClickType() {
        return clickType;
    }
    public InventoryAction getInventoryAction() {
        return  inventoryAction;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
