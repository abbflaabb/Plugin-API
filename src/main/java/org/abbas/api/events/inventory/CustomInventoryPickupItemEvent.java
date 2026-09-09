package org.abbas.api.events.inventory;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class CustomInventoryPickupItemEvent extends Event implements Cancellable {

    public static final HandlerList handlerList = new HandlerList();

    private boolean cancelled;

    private final Inventory inventory;
    private final Item item;

    public CustomInventoryPickupItemEvent(
            Inventory inventory,
            Item item,
            boolean cancelled
    ) {
        this.inventory = inventory;
        this.item = item;
        this.cancelled = cancelled;
    }

    public @NonNull Inventory getInventory() {
        return inventory;
    }

    public @NonNull Item getItem() {
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