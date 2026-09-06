package org.abbas.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Custom event fired before a player is teleported.
 */
public final class CustomPlayerTeleportEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Location from;
    private Location to;
    private final PlayerTeleportEvent.TeleportCause teleportCause;
    private boolean cancelled;

    public CustomPlayerTeleportEvent(
            @NotNull Player player,
            @NotNull Location from,
            @NotNull Location to,
            @NotNull PlayerTeleportEvent.TeleportCause teleportCause
    ) {
        this.player = Objects.requireNonNull(player, "player");
        this.from = Objects.requireNonNull(from, "from").clone();
        this.to = Objects.requireNonNull(to, "to").clone();
        this.teleportCause = Objects.requireNonNull(teleportCause, "teleportCause");
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public Location getFrom() {
        return from.clone();
    }

    @NotNull
    public Location getTo() {
        return to.clone();
    }

    public void setTo(@NotNull Location to) {
        this.to = Objects.requireNonNull(to, "to").clone();
    }

    @NotNull
    public PlayerTeleportEvent.TeleportCause getTeleportCause() {
        return teleportCause;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
