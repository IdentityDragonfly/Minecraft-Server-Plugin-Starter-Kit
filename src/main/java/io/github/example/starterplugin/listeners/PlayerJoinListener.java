package io.github.example.starterplugin.listeners;

import io.github.example.starterplugin.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public final class PlayerJoinListener implements Listener {

    private final ConfigManager configManager;

    public PlayerJoinListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(configManager.message("welcome", Map.of("player", player.getName())));
    }
}
