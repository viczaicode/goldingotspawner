package com.feketebor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private Plugin plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void addSpawnPoint(Location location) {
        List<String> spawnPoints = config.getStringList("spawnPoints");
        spawnPoints.add(serializeLocation(location));
        config.set("spawnPoints", spawnPoints);
        saveConfig();
    }

    public List<Location> getSpawnPoints() {
        List<Location> locations = new ArrayList<>();
        List<String> spawnPoints = config.getStringList("spawnPoints");
        for (String s : spawnPoints) {
            locations.add(deserializeLocation(s));
        }
        return locations;
    }

    public String getRewardCommand() {
        return config.getString("rewardCommand", "eco give {player} 250");
    }

    private String serializeLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    private Location deserializeLocation(String s) {
        String[] parts = s.split(",");
        return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
