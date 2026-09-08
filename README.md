# Plugin-API

A lightweight Spigot/Paper API for bridging Bukkit/Paper events into clean, reusable custom events for other Java plugins.

[![JitPack](https://jitpack.io/v/abbflaabb/SimpleEvents.svg)](https://jitpack.io/#abbflaabb/SimpleEvents)

## Overview

Plugin-API is a small plugin API designed to keep custom event logic out of your main plugin and make it reusable across plugins.

It automatically listens for common Paper/Bukkit events and fires matching custom events under the `org.abbas.api.events` package. This lets other plugins listen to a consistent, plugin-owned event layer instead of wiring up vanilla event listeners manually.

The main public entry point is:

```java
org.abbas.PluginAPI.API
```

The event classes live in:

```java
org.abbas.api.events
```

---

## What's new in this update

This update brings the API in line with the current implementation and expands the bridge coverage for modern Paper builds:

- Java 21 + Paper 1.21.1 support
- Updated Maven dependency setup
- Expanded custom event coverage for movement, interaction, and kill events
- More consistent API helper methods via `API.callCustom...()`
- `Component`-based messaging support with Kyori Adventure
- Listener registration helper through `API.registerListener(...)`

---

## Features

- Lightweight custom event API
- Automatic Bukkit/Paper bridging
- Cancellable custom events
- Plugin-owned listener registration
- Player movement, interaction, and kill events
- Adventure `Component` message support
- Level-up event support without a direct Bukkit equivalent
- Backward-friendly helper methods for dispatching events

---

## Requirements

- Java 21
- Paper/Spigot 1.21.1+
- Maven
- Plugin-API installed on the target server

---

## Installation

Place the generated JAR in your server's `plugins/` folder.

If another plugin depends on Plugin-API, declare it in `plugin.yml`:

```yaml
depend:
  - Plugin-API
```

Use `softdepend` if your plugin can run without it:

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
    <version>1.1.4-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

Replace the version with the tag or release you want to use.

---

## Event bridging

Plugin-API automatically listens for relevant Paper/Bukkit events and dispatches matching custom events.

| Bukkit/Paper event | Plugin-API events           |
| --- |-----------------------------|
| `PlayerJoinEvent` | `CustomPlayerJoinEvent`     |
| `PlayerQuitEvent` | `CustomPlayerQuitEvent`     |
| `PlayerCommandPreprocessEvent` | `CustomProcessCommandEvent` |
| `PlayerDeathEvent` | `CustomPlayerDeathEvent`    |
| `PlayerKill` flow | `CustomPlayerKillEvent`     |
| `BlockBreakEvent` | `CustomBlockBreakEvent`     |
| `BlockPlaceEvent` | `CustomBlockPlaceEvent`     |
| `AsyncChatEvent` | `CustomPlayerChatEvent`     |
| `PlayerMoveEvent` | `CustomPlayerMoveEvent`     |
| `PlayerInteractEvent` | `CustomPlayerInteractEvent` |

`PlayerLevelUpEvent` is a custom event without a direct Paper equivalent and is fired manually by the plugin that owns the level system.

---

## Registering a listener

Use the built-in helper to register a listener with the source plugin as owner:

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

You can still register with Bukkit normally when needed:

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

### Command event

```java
@EventHandler
public void onCommand(CustomProcessCommandEvent event) {
    String message = event.getMessage();
    if (message.startsWith("/home")) {
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
    // React to movement changes here
}
```

### Block break event

```java
@EventHandler
public void onBlockBreak(CustomBlockBreakEvent event) {
    if (event.getPlayer().hasPermission("example.break-protected")) {
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

The API exposes static helper methods for firing custom events manually.

```java
API.callCustomPlayerJoin(player, Component.text("Welcome back!"));
API.callCustomPlayerQuit(player, "See you soon!");
API.callCustomPlayerChat(player, Component.text("Hello"), "global", false);
API.callCustomProcessCommand(player, "/warp hub");
API.callCustomPlayerDeath(player, Component.text("Someone died"), DeathMessageType.DEFAULT, null);
API.callCustomPlayerMove(player, from, to);
API.callCustomPlayerInteract(player, InteractTypes.RIGHT_CLICK_BLOCK, item, block, null);
```

For level-up events, use:

```java
PlayerLevelUpEvent event = API.callCustomPlayerLevelUpEvent(player, oldLevel, newLevel);
if (event.isCancelled()) {
    return;
}
```

---

## Supported events

Current public events in `org.abbas.api.events` include:

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
- `PlayerLevelUpEvent`

Most of these implement Bukkit's usual `Event` pattern and support cancellation where relevant.

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
│   ├── enums/
│   └── events/
│       ├── CustomBlockBreakEvent.java
│       ├── CustomBlockPlaceEvent.java
│       ├── CustomPlayerChatEvent.java
│       ├── CustomPlayerDeathEvent.java
│       ├── CustomPlayerInteractEvent.java
│       ├── CustomPlayerJoinEvent.java
│       ├── CustomPlayerKillEvent.java
│       ├── CustomPlayerMoveEvent.java
│       ├── CustomPlayerQuitEvent.java
│       ├── CustomPlayerTeleportEvent.java
│       ├── CustomProcessCommandEvent.java
│       └── PlayerLevelUpEvent.java
│
└── resources/
    └── plugin.yml
```

---

## Building

To build the plugin locally:

```bash
mvn clean package
```

The JAR is generated in:

```text
target/
```

---

## License

This project does not currently declare a license.

---

## Contributing

Issues, suggestions, and pull requests are welcome.

If you find a bug or have an idea for improving the event API, open an issue or contribute a patch to the repository.

---

## Author

**Abbas**
