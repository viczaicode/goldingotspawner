package com.feketebor;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGoldSpawnPointCommand implements CommandExecutor {
    private GoldIngotSpawner plugin;
    private ConfigManager configManager;

    public SetGoldSpawnPointCommand(GoldIngotSpawner plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;
        configManager.addSpawnPoint(player.getLocation());
        player.sendMessage("Gold ingot spawn point set!");

        return true;
    }
}
