package org.abbas.api.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Set;

public class CustomInventoryDragEvent extends Event implements Cancellable {
    public static final HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final Inventory inventory;
    private final InventoryView view;
    private final ItemStack oldCursor;
    private final ItemStack newItems;
    private final Map<Integer, ItemStack> newslots;
    private final Set<Integer> rawslots;
    private final DragType dragType;

    public CustomInventoryDragEvent(Player player, Inventory inventory, InventoryView view, ItemStack oldCursor, ItemStack newItems, Map<Integer, ItemStack> newslots, Set<Integer> rawslots, DragType dragType, boolean cancelled) {
        this.player = player;
        this.inventory = inventory;
        this.view = view;
        this.oldCursor = oldCursor;
        this.newItems = newItems;
        this.newslots = newslots;
        this.rawslots = rawslots;
        this.dragType = dragType;
        this.cancelled = cancelled;
    }
    public @NonNull Player getPlayer() {
        return player;
    }
    public @NonNull Inventory getInventory() {
        return inventory;
    }
    public @NonNull InventoryView getInventoryView() {
        return view;
    }

    public @NonNull InventoryView getView() {
        return view;
    }
    public @NonNull ItemStack getOldCursor() {
        return oldCursor;
    }
    public @NonNull ItemStack getNewItems() {
        return newItems;
    }
    public @NonNull Map<Integer, ItemStack> getNewslots() {
        return  newslots;
    }
    public @NonNull Set<Integer> getRawslots() {
        return  rawslots;
    }
    public @NonNull DragType getDragType() {
        return  dragType;
    }
    public int getSize() {
        return inventory.getSize();
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
