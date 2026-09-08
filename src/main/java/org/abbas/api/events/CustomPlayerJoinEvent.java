package org.abbas.api.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Custom event fired when a player joins the server.
 *
 * <p>The join message may be changed or set to {@code null}. This event is
 * not cancellable.</p>
 */
public class CustomPlayerJoinEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private @Nullable Component message;

    /**
     * Creates a custom player-join event.
     *
     * @param player the player who joined
     * @param message the join message, or {@code null}
     */
    public CustomPlayerJoinEvent(Player player, @org.jetbrains.annotations.Nullable Component message) {
        this.player = player;
        this.message = message;
    }

    /** @return the player who joined */
    public @NonNull Player getPlayer() {
        return player;
    }

    /**
     * @return the join message, or {@code null}
     */
    public @org.jetbrains.annotations.Nullable Component getMessage() {
        return message;
    }

    /** @param message the new join message, or {@code null} */
    public void setMessage(@Nullable Component message) {
        this.message = message;
    }

    /** @return the handler list used by this event */
    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /** @return the handler list used by Bukkit's event system */
    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
