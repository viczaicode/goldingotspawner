package com.feketebor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class GoldSpawner implements Listener {
    private Plugin plugin;
    private ConfigManager configManager;
    private BukkitRunnable spawnerTask;

    public GoldSpawner(Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void startSpawning() {
        spawnerTask = new BukkitRunnable() {
            @Override
            public void run() {
                List<Location> spawnPoints = configManager.getSpawnPoints();

                for (Location location : spawnPoints) {
                    // Törlés minden aranyrudat az adott ponton
                    for (Entity entity : location.getWorld().getEntities()) {
                        if (entity instanceof Item) {
                            Item item = (Item) entity;
                            if (item.getItemStack().getType() == Material.GOLD_INGOT) {
                                if (item.getLocation().distanceSquared(location) <= 1) { // Ellenőrzés a helyzet környékén
                                    item.remove();
                                }
                            }
                        }
                    }

                    // Új spawnolás
                    Item goldIngot = location.getWorld().dropItemNaturally(location, new ItemStack(Material.GOLD_INGOT));
                    //goldIngot.setCustomName("$250");
                    goldIngot.setPickupDelay(0);
                    //goldIngot.setCustomNameVisible(true);
                }
            }
        };
        spawnerTask.runTaskTimer(plugin, 0, 6000); // 6000 ticks = 5 perc
    }



    public void stopSpawning() {
        if (spawnerTask != null) {
            spawnerTask.cancel();
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (item.getItemStack().getType() == Material.GOLD_INGOT) {
            event.setCancelled(true);
            item.remove();
            String command = configManager.getRewardCommand().replace("{player}", event.getPlayer().getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command); // Execute the reward command
        }
    }
}
