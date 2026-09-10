# Plugin-API

A lightweight Spigot/Paper event bridge API for plugin developers who want reusable custom events without writing manual Bukkit listeners for every vanilla event.

[![](https://jitpack.io/v/abbflaabb/Plugin-API.svg)](https://jitpack.io/#abbflaabb/Plugin-API)

## Overview

Plugin-API provides a clean, plugin-owned custom event layer for Minecraft servers running Paper/Spigot. It listens to selected Bukkit/Paper events and then fires matching custom events under the `org.abbas.api.events` package.

This lets other plugins listen to a consistent API instead of depending on fragile vanilla event wiring in every plugin.

Public API entry points:

```java
org.abbas.PluginAPI.API
org.abbas.api.events
org.abbas.api.events.inventory
org.abbas.api.enums
org.abbas.api.config
org.abbas.api.interfaces
```

Legacy compatibility package:

```java
org.abbas.api.events.inventory
```

This older `inventory` name is still present for compatibility with existing plugins, but the canonical package is `org.abbas.api.events.inventory`.

---

## Latest update

This README reflects the current implementation of the project and the modern Paper build it targets:

- Java 21 support
- Paper/Spigot 1.21.1+ compatibility
- Updated Maven/JitPack setup
- Expanded event bridge coverage for movement, interaction, kill, and inventory flows
- Adventure `Component`-based message handling
- Static helper methods for dispatching custom events
- Inventory compatibility package cleanup with `org.abbas.api.events.inventory` and legacy `inventory` alias support
- Cancellation propagation for custom movement, interaction, block, chat, and inventory events

## Changelog

- 1.2.0 (unreleased): README refreshed, fixed typos, clarified usage, bumped Maven dependency example to 1.2.0.
- 1.1.4: Previous snapshot with core event bridging and API helpers.

---

## Features

- Lightweight custom event API
- Automatic Bukkit/Paper event bridging
- Cancellable custom events where applicable
- Plugin-owned listener registration
- Support for joins, quits, deaths, chat, blocks, movement, interaction, and level-up events
- Adventure `Component` support
- API helpers for manual event dispatch
- Configuration helpers and reusable database interfaces

---

## Requirements

- Java 21
- Paper or Spigot 1.21.1+
- Maven
- Plugin-API installed on the server

---

## Installation

Place the compiled JAR in the server's `plugins/` directory.

If another plugin depends on Plugin-API, declare it in `plugin.yml`:

```yaml
depend:
  - Plugin-API
```

Use `softdepend` if the plugin can work without it:

```yaml
softdepend:
  - Plugin-API
```

---

## Maven dependency

Add JitPack:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the dependency:

```xml
<dependency>
    <groupId>com.github.abbflaabb</groupId>
    <artifactId>Plugin-API</artifactId>
    <version>1.2.0</version>
    <scope>provided</scope>
</dependency>
```

Replace the version with the tag or release you want to use.

---

## Public API

The core class is:

```java
org.abbas.PluginAPI.API
```

Main helper methods include:

```java
API.registerListener(plugin, listener);
API.callCustomPlayerJoin(player, message);
API.callCustomPlayerQuit(player, message);
API.callCustomProcessCommand(player, message);
API.callCustomPlayerDeath(player, message, type, killer);
API.callCustomPlayerMove(player, from, to);
API.callCustomPlayerInteract(player, type, item, block, clickedPosition);
API.callCustomPlayerLevelUpEvent(player, oldLevel, newLevel);
API.callCustomInventoryOpen(player, inventory, view, cancelled);
API.callCustomInventoryClose(player, inventory, view);
API.callCustomInventoryClick(player, inventory, view, action, currentItem, cursor, clickType, cancelled, slot, rawSlot);
API.callCustomInventoryDrag(player, inventory, view, oldCursor, newItems, newSlots, rawSlots, type, cancelled);
API.callCustomInventoryMoveItem(source, destination, initiator, item, cancelled);
API.callCustomInventoryPickupItem(inventory, item, cancelled);
```

---

## Event bridging

Plugin-API listens for the following Bukkit/Paper events and fires compatible custom events:

| Bukkit/Paper event | Plugin-API events |
| --- | --- |
| `PlayerJoinEvent` | `CustomPlayerJoinEvent` |
| `PlayerQuitEvent` | `CustomPlayerQuitEvent` |
| `PlayerCommandPreprocessEvent` | `CustomProcessCommandEvent` |
| `PlayerDeathEvent` | `CustomPlayerDeathEvent` |
| `PlayerMoveEvent` | `CustomPlayerMoveEvent` |
| `BlockBreakEvent` | `CustomBlockBreakEvent` |
| `BlockPlaceEvent` | `CustomBlockPlaceEvent` |
| `AsyncChatEvent` | `CustomPlayerChatEvent` |
| `PlayerInteractEvent` | `CustomPlayerInteractEvent` |
| `InventoryOpenEvent` | `CustomInventoryOpenEvent` |
| `InventoryCloseEvent` | `CustomInventoryCloseEvent` |
| `InventoryClickEvent` | `CustomInventoryClickEvent` |
| `InventoryDragEvent` | `CustomInventoryDragEvent` |
| `InventoryMoveItemEvent` | `CustomInventoryMoveItemEvent` |
| `InventoryPickupItemEvent` | `CustomInventoryPickupItemEvent` |

The project also defines kill and teleport events alongside the automatic bridge:

- `CustomPlayerKillEvent`
- `CustomPlayerTeleportEvent`

`PlayerLevelUpEvent` is not bridged from a Bukkit equivalent; it is intended to be fired manually by the plugin that owns the leveling system.

---

## Registering listeners

Use the built-in helper:

```java
API.registerListener(this, new MyListener());
```

Example:

```java
public class MyListener implements Listener {

    @EventHandler
    public void onJoin(CustomPlayerJoinEvent event) {
        Player player = event.getPlayer();
        System.out.println(player.getName() + " joined the server.");
    }
}
```

You can also register listeners normally with Bukkit:

```java
getServer().getPluginManager().registerEvents(new MyListener(), this);
```

---

## Example usage

### Join event

```java
@EventHandler
public void onJoin(CustomPlayerJoinEvent event) {
    event.setMessage(Component.text("Welcome back, " + event.getPlayer().getName() + "!"));
}
```

### Quit event

```java
@EventHandler
public void onQuit(CustomPlayerQuitEvent event) {
    event.setMessage(event.getPlayer().getName() + " left the server.");
}
```

### Chat event

```java
@EventHandler
public void onChat(CustomPlayerChatEvent event) {
    event.setMessage(Component.text(event.getPlayer().getName() + ": " + event.getMessage()));
}
```

### Command event

```java
@EventHandler
public void onCommand(CustomProcessCommandEvent event) {
    if (event.getMessage().startsWith("/home")) {
        event.setCancelled(true);
    }
}
```

### Move event

```java
@EventHandler
public void onMove(CustomPlayerMoveEvent event) {
    Location from = event.getFrom();
    Location to = event.getTo();
    // handle movement logic here
}
```

### Block break event

```java
@EventHandler
public void onBlockBreak(CustomBlockBreakEvent event) {
    if (event.getPlayer().hasPermission("example.blockbreak")) {
        event.setCancelled(true);
    }
}
```

### Level-up event

```java
@EventHandler
public void onLevelUp(PlayerLevelUpEvent event) {
    if (event.getNewLevel() >= 100) {
        event.setCancelled(true);
    }
}
```

---

## Manual event dispatch

The API also exposes helper methods that let plugins fire custom events programmatically:

```java
API.callCustomPlayerJoin(player, Component.text("Welcome back!"));
API.callCustomPlayerQuit(player, "See you soon!");
API.callCustomProcessCommand(player, "/warp hub");
API.callCustomPlayerDeath(player, Component.text("Someone died"), DeathMessageType.DEFAULT, null);
API.callCustomPlayerMove(player, from, to);
API.callCustomPlayerInteract(player, InteractTypes.RIGHT_CLICK_BLOCK, item, block, null);
```

Level-up example:

```java
PlayerLevelUpEvent event = API.callCustomPlayerLevelUpEvent(player, oldLevel, newLevel);
if (event.isCancelled()) {
    return;
}
```

---

## Available custom events

The project currently includes these public event classes in `org.abbas.api.events`:

- `CustomPlayerJoinEvent`
- `CustomPlayerQuitEvent`
- `CustomPlayerDeathEvent`
- `CustomPlayerKillEvent`
- `CustomPlayerChatEvent`
- `CustomProcessCommandEvent`
- `CustomBlockBreakEvent`
- `CustomBlockPlaceEvent`
- `CustomPlayerMoveEvent`
- `CustomPlayerInteractEvent`
- `CustomPlayerTeleportEvent`
- `PlayerLevelUpEvent`

Most of these follow Bukkit's standard event model and support cancellation when relevant.

---

## Utility APIs

### Config support

```java
ConfigManager configManager = new ConfigManager(this);
configManager.load();
FileConfiguration config = configManager.getConfig(ConfigType.MAIN);
```

Available config types:

- `ConfigType.MAIN` -> `config.yml`
- `ConfigType.MESSAGES` -> `messages.yml`

### Database interface

```java
public interface DatabaseAPI {
    void execute(String sql, Object... params);
    <T> T query(String sql, ResultMapper<T> mapper, Object... parameters);
    List<Map<String, Object>> query(String sql, Object... parameters);
    int update(String sql, Object... parameters);
    void close();
}
```

```java
@FunctionalInterface
public interface ResultMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
```

### Interaction types

```java
public enum InteractTypes {
    LEFT_CLICK_BLOCK,
    RIGHT_CLICK_BLOCK,
    LEFT_CLICK_AIR,
    RIGHT_CLICK_AIR,
    PHYSICAL
}
```

---

## Project structure

```text
src/main/java/
├── org/abbas/PluginAPI/
│   ├── API.java
│   └── internal/
│       └── InternalEventBridge.java
│
├── org/abbas/api/
│   ├── config/
│   │   ├── ConfigManager.java
│   │   └── ConfigType.java
│   ├── enums/
│   │   └── InteractTypes.java
│   ├── events/
│   │   ├── CustomBlockBreakEvent.java
│   │   ├── CustomBlockPlaceEvent.java
│   │   ├── CustomPlayerChatEvent.java
│   │   ├── CustomPlayerDeathEvent.java
│   │   ├── CustomPlayerInteractEvent.java
│   │   ├── CustomPlayerJoinEvent.java
│   │   ├── CustomPlayerKillEvent.java
│   │   ├── CustomPlayerMoveEvent.java
│   │   ├── CustomPlayerQuitEvent.java
│   │   ├── CustomPlayerTeleportEvent.java
│   │   ├── CustomProcessCommandEvent.java
│   │   └── PlayerLevelUpEvent.java
│   └── interfaces/
│       ├── DatabaseAPI.java
│       └── ResultMapper.java
│
└── resources/
    └── plugin.yml
```

---

## Building

Build the project with Maven:

```bash
mvn clean package
```

The compiled JAR will be generated in the `target/` directory.

---

## License

This project does not currently declare a license.

---

## Contributing

Issues, suggestions, and pull requests are welcome.

If you find a bug or have an idea for improving the API, open an issue or submit a patch in the repository.

---

## Author

**Abbas**

Plugin-API is designed to provide a reusable event layer for Paper/Spigot plugin developers.
