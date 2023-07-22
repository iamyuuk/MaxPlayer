package org.encinet.maxplayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class MaxPlayer extends JavaPlugin implements Listener {

    private int maxPlayerCount;

    @Override
    public void onEnable() {
        loadPlayerCount();
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("maxplayer")).setExecutor(this);
        check();
    }

    @Override
    public void onDisable() {
        savePlayerCount();
    }

    private void loadPlayerCount() {
        File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (config.contains("maxPlayerCount")) {
            maxPlayerCount = config.getInt("maxPlayerCount");
        } else {
            maxPlayerCount = Bukkit.getServer().getMaxPlayers();
        }
    }

    private void savePlayerCount() {
        File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        config.set("maxPlayerCount", maxPlayerCount);

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
     check();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "当前玩家最高数: " + maxPlayerCount);
        return true;
    }

    private void check() {
        int currentPlayerCount = Bukkit.getServer().getOnlinePlayers().size();

        if (currentPlayerCount > maxPlayerCount) {
            maxPlayerCount = currentPlayerCount;
            savePlayerCount();

            Bukkit.broadcastMessage(ChatColor.GOLD + "服务器玩家数突破新高！当前玩家数: " + currentPlayerCount);
        }
    }

}