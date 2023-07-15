package org.encinet.maxplayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
public final class MaxPlayer extends JavaPlugin implements Listener {
    private File configFile;
    private YamlConfiguration config;
    private int maxOnlinePlayers;
    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.contains("maxOnlinePlayers")) {
            config.set("maxOnlinePlayers", 0);
        }
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("maxplayers")).setExecutor(this);
        getLogger().info("插件已开启 这是一个用来记录服务器最大在线人数的插件");
        check();
    }
    @Override
    public void onDisable() {
        config.set("maxOnlinePlayers", maxOnlinePlayers);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getLogger().info("插件已关闭");
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) { check(); }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        sender.sendMessage("服务器的最高在线人数是：" + maxOnlinePlayers);
        return true;
    }
    private void check() {
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        if (onlinePlayers > maxOnlinePlayers) {
            maxOnlinePlayers = onlinePlayers;
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                player.sendMessage("恭喜！新的最高在线人数" + maxOnlinePlayers);
            }
        }
    }
}