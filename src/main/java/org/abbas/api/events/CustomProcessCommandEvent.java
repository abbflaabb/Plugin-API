package org.abbas.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;


/**
 * Custom event fired before a player command is processed.
 *
 * <p>Dependent plugins can cancel the command, modify its message, or add
 * command objects to the event's command list.</p>
 */
public class CustomProcessCommandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private String message;
    private boolean cancel = false;

    /**
     * Creates a custom command-processing event.
     *
     * @param player the player who issued the command
     * @param message the command message
     */
    public CustomProcessCommandEvent(
            @NonNull Player player,
            @NonNull String message
    ) {
        this.player = player;
        this.message = message;
    }


    /** @return the player who issued the command */
    @NonNull
    public Player getPlayer() {
        return player;
    }

    /** @return the current command message */
    @NonNull
    public String getMessage() {
        return message;
    }

    /** @param message the new command message */
    public void setMessage(String message) {
        this.message = message;
    }

    /** @return {@code true} when command processing is cancelled */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /** @param cancel whether command processing should be cancelled */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /** @return the handler list used by this event */
    @NonNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /** @return the handler list used by Bukkit's event system */
    @NonNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
