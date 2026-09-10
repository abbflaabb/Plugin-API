package org.abbas.PluginAPI;

import net.kyori.adventure.text.Component;
import org.abbas.api.events.*;
import org.abbas.api.enums.InteractTypes;
import org.abbas.PluginAPI.internal.InternalEventBridge;
import org.abbas.api.events.inventory.*;
import org.abbas.api.interfaces.DatabaseAPI;
import org.abbas.api.interfaces.MenusAPI;
import org.abbas.api.menus.internal.MenusAPIImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DeathMessageType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Main plugin class and public entry point for the SimpleEvents API.
 *
 * <p>Other plugins can use the static API methods to fire SimpleEvents custom
 * events and register listeners. The plugin also installs an internal bridge
 * that converts selected Bukkit/Paper events into SimpleEvents events.</p>
 */
public final class API extends JavaPlugin {

    private static @Nullable API instance;
    private static MenusAPI menusAPI;
    /**
     * Returns the currently enabled SimpleEvents plugin instance.
     *
     * @return the plugin instance, or {@code null} when SimpleEvents is not enabled
     */
    public static @Nullable API getInstance() {
        return instance;
    }
    /**
     * Enables SimpleEvents and registers the internal event bridge.
     */
    @Override
    public void onEnable() {
        instance = this;
        menusAPI = new MenusAPIImpl();
        Bukkit.getPluginManager().registerEvents(new InternalEventBridge(), this);
    }

    /**
     * Disables SimpleEvents and clears the cached plugin instance.
     */
    @Override
    public void onDisable() {
        instance = null;
        menusAPI = null;
    }

    /**
     * Registers a listener using the consuming plugin as its owner.
     *
     * @param plugin   the plugin that owns the listener
     * @param listener the listener to register
     * @throws IllegalStateException if SimpleEvents is not enabled
     */
    public static void registerListener(
            Plugin plugin,
            Listener listener
    ) {
        if (instance == null) {
            throw new IllegalStateException("SimpleEvents is not enabled");
        }

        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Fires a level-up event without returning the event object.
     *
     * <p>This method is kept as the simple compatibility-oriented API. Use
     * {@link #callCustomPlayerLevelUpEvent(Player, int, int)} when the caller
     * needs to inspect the final cancellation state.</p>
     *
     * @param player   the player whose level is changing
     * @param oldLevel the previous level
     * @param newLevel the new level
     */
    public static void callCustomPlayerLevelUp(Player player, int oldLevel, int newLevel) {
        PlayerLevelUpEvent event = new PlayerLevelUpEvent(player, oldLevel, newLevel);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Fires a level-up event and returns the dispatched event.
     *
     * <p>The caller can use the returned object to determine whether another
     * leveling operation should proceed.</p>
     *
     * @param player   the player whose level is changing
     * @param oldLevel the previous level
     * @param newLevel the new level
     * @return the dispatched level-up event
     */
    public static PlayerLevelUpEvent callCustomPlayerLevelUpEvent(
            Player player,
            int oldLevel,
            int newLevel
    ) {
        PlayerLevelUpEvent event = new PlayerLevelUpEvent(player, oldLevel, newLevel);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * Fires a custom player-join event.
     *
     * @param player  the player who joined
     * @param message the join message, or {@code null}
     */
    public static void callCustomPlayerJoin(
            @NonNull Player player,
            @Nullable Component message
    ) {
        Bukkit.getPluginManager().callEvent(new CustomPlayerJoinEvent(player, message));
    }

    /**
     * Fires a custom player-quit event.
     *
     * @param player  the player who quit
     * @param message the quit message, or {@code null}
     */
    public static void callCustomPlayerQuit(
            @NonNull Player player,
            @Nullable Component message
    ) {
        Bukkit.getPluginManager().callEvent(new CustomPlayerQuitEvent(player, message));
    }

    /**
     * Fires a custom command-processing event.
     *
     * @param player  the player who issued the command
     * @param message the command message
     * @return the dispatched event
     */
    public static CustomProcessCommandEvent callCustomProcessCommand(
            @NonNull Player player,
            @NonNull String message
    ) {
        CustomProcessCommandEvent event =
                new CustomProcessCommandEvent(player, message);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }


    /**
     * Fires a custom player-death event.
     *
     * @param player           the player who died
     * @param deathMessage     the current death message
     * @param deathMessageType the death message type
     * @param killer           the player killer, or {@code null}
     */
    public static void callCustomPlayerDeath(
            @NonNull Player player,
            @NonNull Component deathMessage,
            @NonNull DeathMessageType deathMessageType,
            @Nullable Player killer
    ) {
        Bukkit.getPluginManager().callEvent(
                new CustomPlayerDeathEvent(player, deathMessage, deathMessageType, killer)
        );
    }

    /**
     * Fires a custom block-place event.
     *
     * @param block              the block being placed
     * @param replacedBlockState the state of the replaced block
     * @param itemInHand         the item used for placement
     * @param message            the event message
     * @param player             the player placing the block
     */
    public static void callCustomBlockPlace(
            @NonNull Block block,
            @NonNull BlockState replacedBlockState,
            @NonNull ItemStack itemInHand,
            @NonNull Component message,
            @NonNull Player player
    ) {
        Bukkit.getPluginManager().callEvent(
                new CustomBlockPlaceEvent(block, replacedBlockState, itemInHand, message, player)
        );
    }

    /**
     * Fires a custom block-break event.
     *
     * @param player    the player who broke the block
     * @param block     the broken block
     * @param message   the event message
     * @param cancelled the initial cancellation state
     */
    public static void callCustomBlockBreak(
            @NonNull Player player,
            @NonNull Block block,
            @NonNull Component message,
            boolean cancelled
    ) {
        Bukkit.getPluginManager().callEvent(
                new CustomBlockBreakEvent(player, message, block, cancelled)
        );
    }

    /**
     * Fires a custom player-chat event.
     *
     * @param player      the player who sent the message
     * @param message     the chat message
     * @param channelName the logical chat channel
     * @param cancelled   the initial cancellation state
     */
    public static void callCustomPlayerChat(
            @NonNull Player player,
            @NonNull Component message,
            @NonNull String channelName,
            boolean cancelled
    ) {
        Bukkit.getPluginManager().callEvent(
                new CustomPlayerChatEvent(player, message, channelName, cancelled)
        );
    }
    // for CustomPlayerMoveEvent event
    public static void callCustomPlayerMove(@NonNull Player player, @NonNull Location from, @NonNull Location to) {
        Bukkit.getPluginManager().callEvent(
                new CustomPlayerMoveEvent(player, from, to)
        );
    }
    public static void callCustomPlayerInteract(@NonNull Player player,
                                                @NonNull InteractTypes type,
                                                @Nullable ItemStack itemInHand,
                                                @Nullable Block clickedBlock,
                                                @Nullable Vector clickedPosition) {
        Bukkit.getPluginManager().callEvent(
                new CustomPlayerInteractEvent(player, type, itemInHand, clickedBlock, clickedPosition)
        );
    }
    /**
     * Fires a custom player-kill event.
     *
     * @param victim       the player who was killed
     * @param killer       the player who made the kill
     * @param type         the death message type
     * @param source       the source of the damage
     * @param location     the location where the kill occurred
     * @param weapon       the weapon used by the killer, or {@code null}
     * @param cancelled    the initial cancellation state
     */
    public static void callCustomPlayerKill(
            @NonNull Player victim,
            @NonNull Player killer,
            @NonNull DeathMessageType type,
            @Nullable DamageSource source,
            @NonNull Location location,
            @Nullable ItemStack weapon,
            boolean cancelled) {
        Bukkit.getPluginManager().callEvent(
                new CustomPlayerKillEvent(
                        victim,
                        killer,
                        type, source, location, weapon, cancelled));
    }
    public static void callCustomInventoryOpen(
            @NonNull Player player,
            @NonNull Inventory inventory,
            @NonNull InventoryView inventoryView,
            boolean cancelled
            ) {
        Bukkit.getPluginManager().callEvent(new CustomInventoryOpenEvent(
                player,
                inventory,
                inventoryView,
                cancelled)
        );
    }
    public static void callCustomInventoryClose(
            @NonNull Player player,
            @NonNull Inventory inventory,
            @NonNull InventoryView inventoryView
    ) {
        Bukkit.getPluginManager().callEvent(new CustomInventoryCloseEvent(
                inventory, inventoryView, player));
    }
    public static void callCustomInventoryClick(
            @NonNull Player player,
            @NonNull Inventory inventory,
            @NonNull InventoryView inventoryView,
            @NonNull InventoryAction inventoryAction,
            @NonNull ItemStack currentItem,
            @NonNull ItemStack cursor,
            @NonNull ClickType clickType,
            boolean cancelled,
            int slot,
            int rawSlot
            ) {
        Bukkit.getPluginManager().callEvent(new CustomInventoryClickEvent(
                inventory,
                inventoryView,
                player,slot,rawSlot,
                currentItem,cursor,clickType,
                inventoryAction,cancelled
        ));
    }
    public static void callCustomInventoryDrag(
            @NonNull Player player,
            @NonNull Inventory inventory,
            @NonNull InventoryView view,
            @NonNull ItemStack oldCursor,
            @NonNull ItemStack newItems,
            @NonNull Map<Integer, ItemStack> newslots,
            @NonNull Set<Integer> rawslots,
            @NonNull DragType type,
            boolean cancelled
            ) {
        Bukkit.getPluginManager().callEvent(new CustomInventoryDragEvent(
                player,
                inventory,
                view,
                oldCursor,
                newItems, newslots, rawslots, type, cancelled
        ));
    }
    public static void callCustomInventoryMoveItem(
            @NonNull Inventory source,
            @NonNull Inventory destination,
            @NonNull Inventory initiator,
            @NonNull ItemStack item,
            boolean cancelled
    ) {
        CustomInventoryMoveItemEvent event = new CustomInventoryMoveItemEvent(
                source,
                destination,
                initiator,
                item,
                cancelled
        );
        Bukkit.getPluginManager().callEvent(event);
    }
    public static void callCustomInventoryPickupItem(
            @NonNull Inventory inventory,
            @NonNull Item item,
            boolean cancelled
    ) {
        CustomInventoryPickupItemEvent event = new CustomInventoryPickupItemEvent(
                inventory,
                item,
                cancelled
        );
        Bukkit.getPluginManager().callEvent(event);
    }
}
