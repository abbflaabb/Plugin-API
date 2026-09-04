package org.abbas.api.events;

import org.abbas.api.events.enums.InteractTypes;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/**
 * Custom event fired when a player interacts with an object through SimpleEvents.
 *
 * <p>This event provides information about the interaction type, the clicked block,
 * the item in hand, and the clicked position. It can be cancelled to prevent the
 * interaction from occurring.</p>
 */
public class CustomPlayerInteractEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final InteractTypes interactType;
    private final Block clickedBlock;
    private final ItemStack itemInHand;
    private final Vector clickedPosition;
    private boolean cancelled;

    public CustomPlayerInteractEvent(@NotNull Player player,
                                     @NotNull InteractTypes interactType,
                                     @Nullable ItemStack itemInHand,
                                     @Nullable Block clickedBlock,
                                     @Nullable Vector clickedPosition) {
        this.player = player;
        this.interactType = interactType;
        this.itemInHand = itemInHand;
        this.clickedBlock = clickedBlock;
        this.clickedPosition = clickedPosition;
        this.cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return  handlers;
    }
    /**
     * Gets the player who interacted.
     *
     * @return the player involved in the interaction
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    /**
     * Gets the type of interaction that occurred.
     *
     * @return the interaction type
     */

    @NotNull
    public InteractTypes getInteractType() {
        return interactType;
    }
    /**
     * Gets the item in the player's hand during the interaction.
     *
     * @return the item in hand, or null if none
     */
    @Nullable
    public ItemStack getItemInHand() {
        return itemInHand;
    }
    /**
     * Gets the block that was clicked during the interaction.
     *
     * @return the clicked block, or null if none
     */
    @Nullable
    public Block getClickedBlock()  {
        return clickedBlock;
    }
    /**
     * Gets the position where the interaction occurred.
     *
     * @return the clicked position, or null if none
     */
    @Nullable
    public Vector getClickedPosition() {
        return  clickedPosition;
    }
    /**
     * Gets the location of the clicked block, adjusted by the clicked position.
     *
     * @return the adjusted location of the clicked block, or null if no block was clicked
     */
    @Nullable
    public Location getClickedLocation() {
        return clickedBlock != null ? clickedBlock.getLocation().add(clickedPosition) : null;
    }
    /**
     * Checks if a block was clicked during the interaction.
     *
     * @return true if a block was clicked, false otherwise
     */
    public boolean hasBlock() {
        return clickedBlock != null;
    }
    /**
     * Checks if an item is in the player's hand during the interaction.
     *
     * @return true if an item is in hand, false otherwise
     */
    public boolean hasItem() {
        return itemInHand != null;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
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
