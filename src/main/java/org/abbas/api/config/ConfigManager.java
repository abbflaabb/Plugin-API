package org.abbas.api.config;

import org.abbas.api.enums.ConfigType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class ConfigManager {

    private final JavaPlugin plugin;

    private final Map<ConfigType, FileConfiguration> configs =
            new EnumMap<>(ConfigType.class);

    private final Map<ConfigType, File> files =
            new EnumMap<>(ConfigType.class);

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        for (ConfigType type : ConfigType.values()) {
            load(type);
        }
    }

    public void load(ConfigType type) {

        File file = new File(
                plugin.getDataFolder(),
                type.getFileName()
        );

        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();

            if (plugin.getResource(type.getFileName()) != null) {
                plugin.saveResource(type.getFileName(), false);
            } else {
                try {
                    if (!file.createNewFile()) {
                        throw new IOException("Failed to create config file: " + file.getName());
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(
                            "Could not create missing config file: " + type.getFileName(),
                            e
                    );
                }
            }
        }

        FileConfiguration config =
                YamlConfiguration.loadConfiguration(file);

        files.put(type, file);
        configs.put(type, config);
    }

    public FileConfiguration getConfig(ConfigType type) {
        return configs.get(type);
    }

    public void reload(ConfigType type) {
        load(type);
    }

    public void reloadAll() {
        load();
    }

    public void save(ConfigType type) {

        FileConfiguration config = configs.get(type);
        File file = files.get(type);

        if (config == null || file == null) {
            return;
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe(
                    "Could not save " + type.getFileName()
            );
            e.printStackTrace();
        }
    }

    public void saveAll() {
        for (ConfigType type : ConfigType.values()) {
            save(type);
        }
    }
}