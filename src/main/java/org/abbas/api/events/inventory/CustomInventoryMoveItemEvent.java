package org.abbas.api.events.inventory;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class CustomInventoryMoveItemEvent extends Event implements Cancellable {

    public static final HandlerList handlerList = new HandlerList();

    private boolean cancelled;

    private final Inventory source;
    private final Inventory destination;
    private final Inventory initiator;
    private final ItemStack item;

    public CustomInventoryMoveItemEvent(
            Inventory source,
            Inventory destination,
            Inventory initiator,
            ItemStack item,
            boolean cancelled
    ) {
        this.source = source;
        this.destination = destination;
        this.initiator = initiator;
        this.item = item;
        this.cancelled = cancelled;
    }

    public @NonNull Inventory getSource() {
        return source;
    }

    public @NonNull Inventory getDestination() {
        return destination;
    }

    public @NonNull Inventory getInitiator() {
        return initiator;
    }

    public @NonNull ItemStack getItem() {
        return item;
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
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}