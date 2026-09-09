package org.abbas.PluginAPI.internal;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.abbas.api.events.*;
import org.abbas.api.enums.InteractTypes;
import org.abbas.api.events.inventory.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.damage.DeathMessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;


/**
 * Internal bridge that listens to vanilla Bukkit/Paper events and
 * automatically fires the corresponding simpleEvents custom events.
 * This exists purely so that plugins depending on simpleEvents can listen
 * to the {@code org.abbas.api.events.*} custom events directly, without
 * needing to call {@code SimpleEvents.callCustom...} themselves.
 * Where the custom event is {@link org.bukkit.event.Cancellable}, this
 * bridge propagates {@code isCancelled()} (and, where applicable, message
 * edits) back onto the vanilla event after dependent plugins have had a
 * chance to react — otherwise cancelling the custom event would have no
 * real effect on gameplay.
 * {@link PlayerLevelUpEvent} is intentionally NOT bridged here: it has no
 * vanilla Bukkit equivalent, and is meant to be fired manually by whichever
 * plugin implements its own leveling system via
 * {@code SimpleEvents.callCustomPlayerLevelUp(...)}.
 */
public final class InternalEventBridge implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onJoin(PlayerJoinEvent event) {

        CustomPlayerJoinEvent customEvent =
                new CustomPlayerJoinEvent(
                        event.getPlayer(),
                        event.joinMessage()
                );

        Bukkit.getPluginManager().callEvent(customEvent);

        event.joinMessage(customEvent.getMessage());
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onQuit(PlayerQuitEvent event) {
        CustomPlayerQuitEvent customEvent =
                new CustomPlayerQuitEvent(
                        event.getPlayer(),
                        event.quitMessage()
                );

        Bukkit.getPluginManager().callEvent(customEvent);

        event.quitMessage(customEvent.getMessage());
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        CustomInventoryCloseEvent customEvent = new CustomInventoryCloseEvent(
                event.getInventory(),
                event.getView(),
                player
        );
        Bukkit.getPluginManager().callEvent(customEvent);
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        CustomInventoryClickEvent customEvent = new CustomInventoryClickEvent(
                event.getInventory(),
                event.getView(),
                player,
                event.getSlot(),
                event.getRawSlot(),
                event.getCurrentItem(),
                event.getCursor(),
                event.getClick(),
                event.getAction(),
                event.isCancelled()
        );
        Bukkit.getPluginManager().callEvent(customEvent);
        event.setCancelled(customEvent.isCancelled());
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        CustomInventoryDragEvent customEvent = new CustomInventoryDragEvent(
                player,
                event.getInventory(),
                event.getView(),
                event.getOldCursor(),
                event.getCursor(),
                event.getNewItems(),
                event.getRawSlots(),
                event.getType(),
                event.isCancelled()
        );
        Bukkit.getPluginManager().callEvent(customEvent);
        event.setCancelled(customEvent.isCancelled());
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {

        CustomInventoryMoveItemEvent customEvent =
                new CustomInventoryMoveItemEvent(
                        event.getSource(),
                        event.getDestination(),
                        event.getInitiator(),
                        event.getItem(),
                        event.isCancelled()
                );

        Bukkit.getPluginManager().callEvent(customEvent);

        event.setCancelled(customEvent.isCancelled());
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {

        CustomInventoryPickupItemEvent customEvent =
                new CustomInventoryPickupItemEvent(
                        event.getInventory(),
                        event.getItem(),
                        event.isCancelled()
                );

        Bukkit.getPluginManager().callEvent(customEvent);

        event.setCancelled(customEvent.isCancelled());
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        CustomProcessCommandEvent customEvent =
                new CustomProcessCommandEvent(
                        event.getPlayer(),
                        event.getMessage()
                );

        Bukkit.getPluginManager().callEvent(customEvent);

        if (customEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setMessage(customEvent.getMessage());
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onDeath(PlayerDeathEvent event) {
        Component deathMessage = event.deathMessage() != null
                ? event.deathMessage()
                : Component.empty();

        EntityDamageEvent lastDamageCause = event.getEntity().getLastDamageCause();
        DeathMessageType deathMessageType = lastDamageCause != null
                ? lastDamageCause.getDamageSource().getDamageType().getDeathMessageType()
                : DeathMessageType.DEFAULT;
        Player killer = event.getEntity().getKiller();

        CustomPlayerDeathEvent deathEvent =
                new CustomPlayerDeathEvent(
                        event.getEntity(),
                        deathMessage,
                        deathMessageType,
                        killer
                );

        Bukkit.getPluginManager().callEvent(deathEvent);
        if (killer != null) {
            CustomPlayerKillEvent killEvent =
                    new CustomPlayerKillEvent(
                            event.getEntity(),
                            killer,
                            deathMessageType,
                            lastDamageCause != null
                                    ? lastDamageCause.getDamageSource()
                                    : null,
                            event.getEntity().getLocation(),
                            killer.getInventory().getItemInMainHand(),
                            event.isCancelled()
                    );

            Bukkit.getPluginManager().callEvent(killEvent);
        }
        event.deathMessage(deathEvent.getDeathMessage());
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onMove(PlayerMoveEvent event) {
        CustomPlayerMoveEvent customEvent = new CustomPlayerMoveEvent(
                event.getPlayer(),
                event.getFrom(),
                event.getTo()
        );
        Bukkit.getPluginManager().callEvent(customEvent);

        event.setCancelled(customEvent.isCancelled());
        event.setTo(customEvent.getTo());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        CustomBlockBreakEvent customEvent = new CustomBlockBreakEvent(
                event.getPlayer(),
                Component.empty(),
                event.getBlock(),
                event.isCancelled()
        );
        Bukkit.getPluginManager().callEvent(customEvent);

        event.setCancelled(customEvent.isCancelled());
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInteraction(PlayerInteractEvent event) {
        InteractTypes type = switch (event.getAction()) {
            case LEFT_CLICK_BLOCK -> InteractTypes.LEFT_CLICK_BLOCK;
            case RIGHT_CLICK_BLOCK -> InteractTypes.RIGHT_CLICK_BLOCK;
            case LEFT_CLICK_AIR -> InteractTypes.LEFT_CLICK_AIR;
            case RIGHT_CLICK_AIR -> InteractTypes.RIGHT_CLICK_AIR;
            default -> null; // PHYSICAL (pressure plates etc.) — not modeled yet
        };

        if (type == null) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        CustomPlayerInteractEvent customEvent = new CustomPlayerInteractEvent(
                event.getPlayer(),
                type,
                event.getItem(),
                clickedBlock,
                null
        );
        Bukkit.getPluginManager().callEvent(customEvent);

        if (customEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        CustomInventoryOpenEvent customEvent = new CustomInventoryOpenEvent(
                        player,
                        event.getInventory(),
                        event.getView(),
                        event.isCancelled());
        Bukkit.getPluginManager().callEvent(customEvent);

        event.setCancelled(customEvent.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockPlace(BlockPlaceEvent event) {
        BlockState replacedState = event.getBlockReplacedState();

        CustomBlockPlaceEvent customEvent = new CustomBlockPlaceEvent(
                event.getBlock(),
                replacedState,
                event.getItemInHand(),
                Component.empty(),
                event.getPlayer()
        );
        Bukkit.getPluginManager().callEvent(customEvent);

        event.setCancelled(customEvent.isCancelled());
        event.setBuild(customEvent.canBuild());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onChat(AsyncChatEvent event) {
        CustomPlayerChatEvent customEvent = new CustomPlayerChatEvent(
                event.getPlayer(),
                event.message(),
                "global",
                event.isCancelled()
        );
        Bukkit.getPluginManager().callEvent(customEvent);

        event.setCancelled(customEvent.isCancelled());
        if (!customEvent.getMessage().equals(event.message())) {
            event.message(customEvent.getMessage());
        }
    }
}