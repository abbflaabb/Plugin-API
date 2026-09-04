package org.abbas.api.events;

import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DeathMessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class CustomPlayerKillEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player victim;
    private final Player killer;
    private final DeathMessageType type;
    private final DamageSource source;
    private final Location location;
    private final ItemStack weapon;
    private boolean cancelled;
    public CustomPlayerKillEvent(Player victim, Player killer, DeathMessageType type, DamageSource source, Location location, ItemStack weapon, boolean cancelled) {
        this.victim = victim;
        this.killer = killer;
        this.type = type;
        this.source = source;
        this.location = location;
        this.weapon = weapon;
        this.cancelled = cancelled;
    }

    @NotNull
    public Player getVictim() {
        return victim;
    }
    @NotNull
    public Player getKiller() {
        return killer;
    }
    public DeathMessageType getType() {
        return type;
    }
    @NotNull
    public DamageSource getSource() {
        return  source;
    }
    public ItemStack getItem() {
        return weapon;
    }
    @NotNull
    public Location getLocation() {
        return location.clone();
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NonNull HandlerList getHandlers() {
        return  HANDLERS;
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
