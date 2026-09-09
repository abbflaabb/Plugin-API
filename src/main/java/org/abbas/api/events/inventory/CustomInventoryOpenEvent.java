package org.abbas.api.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class CustomInventoryOpenEvent extends Event implements Cancellable {
    public static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final Inventory inventory;
    private final InventoryView inventoryView;

    public CustomInventoryOpenEvent(Player player, Inventory inventory, InventoryView inventoryView, boolean cancelled) {
        this.player = player;
        this.inventory = inventory;
        this.inventoryView = inventoryView;
        this.cancelled = cancelled;
    }
    public @NonNull Player getPlayer() {
        return  player;
    }
    public @NonNull Inventory getInventory() {
        return inventory;
    }
    public @NonNull InventoryView getInventoryView() {
        return  inventoryView;
    }

    public @NonNull InventoryView getView() {
        return inventoryView;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
