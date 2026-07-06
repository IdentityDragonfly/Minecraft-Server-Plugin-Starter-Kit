package io.github.example.starterplugin.config;

import io.github.example.starterplugin.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class ConfigManager {

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.reloadConfig();
    }

    public FileConfiguration config() {
        return plugin.getConfig();
    }

    public String rawPrefix() {
        return config().getString("prefix", "&8[&aStarterPlugin&8]&r");
    }

    public Component prefix() {
        return MessageUtils.color(rawPrefix());
    }

    public Component message(String path) {
        return MessageUtils.color(rawMessage(path));
    }

    public Component message(String path, Map<String, String> placeholders) {
        String value = rawMessage(path);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            value = value.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return MessageUtils.color(value);
    }

    public String rawMessage(String path) {
        return rawPrefix() + " " + config().getString("messages." + path, "&cMissing message: " + path);
    }
}
