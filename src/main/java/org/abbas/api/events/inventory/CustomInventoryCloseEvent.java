package org.abbas.api.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class CustomInventoryCloseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Inventory inventory;
    private final InventoryView inventoryView;
    private final Player player;

    public CustomInventoryCloseEvent(
            Inventory inventory,
            InventoryView inventoryView,
            Player player
    ) {
        this.inventory = inventory;
        this.inventoryView = inventoryView;
        this.player = player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public InventoryView getInventoryView() {
        return inventoryView;
    }

    public InventoryView getView() {
        return inventoryView;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSize() {
        return inventory.getSize();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
