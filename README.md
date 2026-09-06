# SimpleEvents

A lightweight Spigot/Paper API plugin for creating, dispatching, and listening to custom Bukkit-style events from Java plugins.

[![JitPack](https://jitpack.io/v/abbflaabb/SimpleEvents.svg)](https://jitpack.io/#abbflaabb/SimpleEvents)

## Overview

**SimpleEvents** provides a lightweight event API for Minecraft Spigot/Paper plugins.

It bridges selected Bukkit/Paper events into custom API events, allowing other plugins to listen to a consistent event layer without manually connecting every vanilla event.

SimpleEvents currently provides custom events for:

* Player join
* Player quit
* Player death
* Block break
* Block place
* Player chat
* Command processing
* Player level-up

The public API is located under:

```text
org.abbas.api.events
```

Internal implementation classes are kept separate from the public API.

---

## Features

* Lightweight custom event API
* Automatic Bukkit/Paper event bridging
* Cancellable custom events
* Plugin-owned listener registration
* Player level-up event support
* Adventure `Component` support
* Javadocs for public API classes and methods
* Backward-compatible API additions

---

## Project Structure

```text
src/main/java/
├── org/abbas/api/events/
│   ├── CustomBlockBreakEvent.java
│   ├── CustomBlockPlaceEvent.java
│   ├── CustomPlayerChatEvent.java
│   ├── CustomPlayerDeathEvent.java
│   ├── CustomPlayerJoinEvent.java
│   ├── CustomPlayerQuitEvent.java
│   ├── CustomProcessCommandEvent.java
│   └── PlayerLevelUpEvent.java
│
└── org/abbas/simpleEvents/
    ├── SimpleEvents.java
    └── internal/
        └── InternalEventBridge.java

src/main/resources/
└── plugin.yml
```

---

## Requirements

* Java 21
* Spigot/Paper 1.21+
* Maven
* SimpleEvents installed on the server

---

## Installation

Download the SimpleEvents JAR and place it in your server's:

```text
plugins/
```

folder.

Plugins using the API should declare SimpleEvents as a dependency.

### plugin.yml

```yaml
depend:
  - simpleEvents
```

Use `softdepend` instead if your plugin can operate without SimpleEvents.

---

## Maven

Add the JitPack repository:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add SimpleEvents as a dependency:

```xml
<dependency>
    <groupId>com.github.abbflaabb</groupId>
    <artifactId>SimpleEvents</artifactId>
    <version>1.1.3-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

Replace the version with the release or tag you want to use.

---

# Automatic Event Bridging

SimpleEvents automatically listens for selected Bukkit/Paper events and dispatches the corresponding custom API events.

| Bukkit/Paper Event             | SimpleEvents Event          |
| ------------------------------ | --------------------------- |
| `PlayerJoinEvent`              | `CustomPlayerJoinEvent`     |
| `PlayerQuitEvent`              | `CustomPlayerQuitEvent`     |
| `PlayerCommandPreprocessEvent` | `CustomProcessCommandEvent` |
| `PlayerDeathEvent`             | `CustomPlayerDeathEvent`    |
| `BlockBreakEvent`              | `CustomBlockBreakEvent`     |
| `BlockPlaceEvent`              | `CustomBlockPlaceEvent`     |
| `AsyncChatEvent`               | `CustomPlayerChatEvent`     |

Your plugin can listen directly to the SimpleEvents event instead of manually creating another Bukkit listener.

---

# Registering a Listener

SimpleEvents provides plugin-owned listener registration:

```java
SimpleEvents.registerListener(
        this,
        new MyListener()
);
```

Example:

```java
public class MyListener implements Listener {

    @EventHandler
    public void onJoin(CustomPlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Your logic here
    }
}
```

Register it with:

```java
SimpleEvents.registerListener(
        this,
        new MyListener()
);
```

You can also use the normal Bukkit registration system:

```java
getServer().getPluginManager().registerEvents(
        new MyListener(),
        this
);
```

---

# Custom Player Join Event

```java
@EventHandler
public void onJoin(CustomPlayerJoinEvent event) {

    Player player = event.getPlayer();

    event.setMessage(
            "Welcome " + player.getName() + "!"
    );
}
```

Available methods:

```text
getPlayer()
getMessage()
setMessage(String)
```

---

# Custom Player Quit Event

```java
@EventHandler
public void onQuit(CustomPlayerQuitEvent event) {

    event.setMessage(
            event.getPlayer().getName() + " left the server."
    );
}
```

Available methods:

```text
getPlayer()
getMessage()
setMessage(String)
```

---

# Custom Player Death Event

```java
@EventHandler
public void onDeath(CustomPlayerDeathEvent event) {

    event.setDeathMessage(
            Component.text("A player has died.")
    );
}
```

Available methods:

```text
getPlayer()
getDeathMessage()
setDeathMessage(Component)
getDeathMessageType()
getKiller()
```

---

# Custom Block Break Event

```java
@EventHandler
public void onBlockBreak(CustomBlockBreakEvent event) {

    Player player = event.getPlayer();
    Block block = event.getBlock();

    // Cancel the event if required.
    event.setCancelled(true);
}
```

Available methods:

```text
getPlayer()
getBlock()
getMessage()
setMessage(Component)
isCancelled()
setCancelled(boolean)
```

---

# Custom Block Place Event

Available methods:

```text
getBlock()
getReplacedBlockState()
getItemInHand()
getPlayer()
getMessage()
setMessage(Component)
canBuild()
setCanBuild(boolean)
isCancelled()
setCancelled(boolean)
```

Example:

```java
@EventHandler
public void onBlockPlace(CustomBlockPlaceEvent event) {

    if (!event.canBuild()) {
        event.setCancelled(true);
    }
}
```

---

# Custom Player Chat Event

```java
@EventHandler
public void onChat(CustomPlayerChatEvent event) {

    event.setChannelName("global");

    event.setMessage(
            Component.text(
                    event.getPlayer().getName()
                            + ": "
                            + event.getMessage()
            )
    );
}
```

Available methods:

```text
getPlayer()
getMessage()
setMessage(Component)
getChannelName()
setChannelName(String)
isCancelled()
setCancelled(boolean)
```

---

# Custom Process Command Event

```java
@EventHandler
public void onCommand(CustomProcessCommandEvent event) {

    List<Command> commands = event.getCommands();

    // Modify the command chain if required.
}
```

Available methods:

```text
getPlayer()
getMessage()
getCommands()
addCommand(Command)
isCancelled()
setCancelled(boolean)
```

---

# Player Level Up Event

`PlayerLevelUpEvent` is a custom event and does not have a direct Bukkit/Paper equivalent.

It is cancellable:

```java
@EventHandler
public void onLevelUp(PlayerLevelUpEvent event) {

    if (event.getNewLevel() >= 100) {
        event.setCancelled(true);
    }
}
```

Available methods:

```text
getPlayer()
getOldLevel()
getNewLevel()
isCancelled()
setCancelled(boolean)
```

### Firing the Event

The compatibility method:

```java
SimpleEvents.callCustomPlayerLevelUp(
        player,
        oldLevel,
        newLevel
);
```

If you need to inspect the cancellation state, use:

```java
PlayerLevelUpEvent event =
        SimpleEvents.callCustomPlayerLevelUpEvent(
                player,
                oldLevel,
                newLevel
        );

if (event.isCancelled()) {
    return;
}
```

---

# Manually Firing Events

SimpleEvents also provides helper methods for manually dispatching custom events.

### Player Join

```java
SimpleEvents.callCustomPlayerJoin(
        player,
        "Welcome back!"
);
```

### Player Quit

```java
SimpleEvents.callCustomPlayerQuit(
        player,
        "See you soon!"
);
```

### Command Processing

```java
List<Command> commands = new ArrayList<>();

SimpleEvents.callCustomProcessCommand(
        player,
        "Processing command chain",
        commands
);
```

---

# Available Events

### `CustomPlayerJoinEvent`

```text
getPlayer()
getMessage()
setMessage(String)
```

### `CustomPlayerQuitEvent`

```text
getPlayer()
getMessage()
setMessage(String)
```

### `CustomPlayerDeathEvent`

```text
getPlayer()
getDeathMessage()
setDeathMessage(Component)
getDeathMessageType()
getKiller()
```

### `CustomBlockBreakEvent`

```text
getPlayer()
getBlock()
getMessage()
setMessage(Component)
isCancelled()
setCancelled(boolean)
```

### `CustomBlockPlaceEvent`

```text
getBlock()
getReplacedBlockState()
getItemInHand()
getPlayer()
getMessage()
setMessage(Component)
canBuild()
setCanBuild(boolean)
isCancelled()
setCancelled(boolean)
```

### `CustomPlayerChatEvent`

```text
getPlayer()
getMessage()
setMessage(Component)
getChannelName()
setChannelName(String)
isCancelled()
setCancelled(boolean)
```

### `CustomProcessCommandEvent`

```text
getPlayer()
getMessage()
getCommands()
addCommand(Command)
isCancelled()
setCancelled(boolean)
```

### `PlayerLevelUpEvent`

```text
getPlayer()
getOldLevel()
getNewLevel()
isCancelled()
setCancelled(boolean)
```

---

# Building

Clone the repository and build with Maven:

```bash
mvn clean package
```

The compiled JAR will be generated inside:

```text
target/
```

---

# License

This project currently does not declare a license.

If you plan to make SimpleEvents an open-source project for other developers to use and modify, consider adding a license such as the MIT License.

---

# Contributing

Issues, suggestions, and pull requests are welcome.

If you find a bug or have an idea for improving the API, open an issue in the GitHub repository.

---

# Author

**Abbas**

SimpleEvents is designed to provide a simple and reusable event API for Spigot/Paper plugin developers.
