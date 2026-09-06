package org.abbas.simpleEvents.internal;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.abbas.api.events.*;
import org.abbas.api.events.enums.InteractTypes;
import org.abbas.simpleEvents.SimpleEvents;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.damage.DeathMessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

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
        CustomPlayerJoinEvent customEvent = new CustomPlayerJoinEvent(
                event.getPlayer(), event.getJoinMessage());
        Bukkit.getPluginManager().callEvent(customEvent);
        event.setJoinMessage(customEvent.getMessage());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onQuit(PlayerQuitEvent event) {
        CustomPlayerQuitEvent customEvent = new CustomPlayerQuitEvent(
                event.getPlayer(), event.getQuitMessage());
        Bukkit.getPluginManager().callEvent(customEvent);
        event.setQuitMessage(customEvent.getMessage());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onCommand(PlayerCommandPreprocessEvent event) {

        List<Command> commands = new ArrayList<>();

        CustomProcessCommandEvent customEvent =
                new CustomProcessCommandEvent(
                        event.getPlayer(),
                        event.getMessage(),
                        commands
                );

        Bukkit.getPluginManager().callEvent(customEvent);

        // Custom event cancelled -> cancel the real Bukkit event
        if (customEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        // Allow external plugins to modify the command
        if (!customEvent.getMessage().equals(event.getMessage())) {
            event.setMessage(customEvent.getMessage());
        }
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
            if (killEvent.isCancelled()) {
                event.setCancelled(true);
            }
        }
        event.deathMessage(deathEvent.getDeathMessage());
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) {
            return;
        }

        CustomPlayerMoveEvent customEvent = new CustomPlayerMoveEvent(
                event.getPlayer(),
                event.getFrom(),
                event.getTo()
        );
        Bukkit.getPluginManager().callEvent(customEvent);

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
                event.getClickedPosition()
        );
        Bukkit.getPluginManager().callEvent(customEvent);

        if (customEvent.isCancelled()) {
            event.setCancelled(true);
        }
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
        event.message(customEvent.getMessage());
    }
}
