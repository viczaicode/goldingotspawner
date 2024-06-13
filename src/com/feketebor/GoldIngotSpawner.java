package com.feketebor;

import org.bukkit.plugin.java.JavaPlugin;

public class GoldIngotSpawner extends JavaPlugin {
    private static GoldIngotSpawner instance;
    private ConfigManager configManager;
    private GoldSpawner goldSpawner;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        goldSpawner = new GoldSpawner(this, configManager);

        this.getCommand("setgoldspawnpoint").setExecutor(new SetGoldSpawnPointCommand(this, configManager));

        goldSpawner.startSpawning();

        getLogger().info("GoldIngotSpawner enabled!");
    }

    @Override
    public void onDisable() {
        goldSpawner.stopSpawning();
        getLogger().info("GoldIngotSpawner disabled!");
    }

    public static GoldIngotSpawner getInstance() {
        return instance;
    }
}
