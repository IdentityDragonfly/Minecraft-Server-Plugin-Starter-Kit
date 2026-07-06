package io.github.example.starterplugin;

import io.github.example.starterplugin.commands.StarterCommand;
import io.github.example.starterplugin.config.ConfigManager;
import io.github.example.starterplugin.cooldown.CooldownManager;
import io.github.example.starterplugin.gui.StarterMenu;
import io.github.example.starterplugin.kits.StarterKitService;
import io.github.example.starterplugin.listeners.MenuClickListener;
import io.github.example.starterplugin.listeners.PlayerJoinListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class StarterPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private CooldownManager cooldownManager;
    private StarterKitService starterKitService;
    private StarterMenu starterMenu;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        this.cooldownManager = new CooldownManager();
        this.starterKitService = new StarterKitService(this, configManager, cooldownManager);
        this.starterMenu = new StarterMenu(configManager);

        registerCommands();
        registerListeners();

        getLogger().info("StarterPlugin enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("StarterPlugin disabled.");
    }

    private void registerCommands() {
        PluginCommand starterCommand = getCommand("starter");
        if (starterCommand == null) {
            getLogger().severe("Command 'starter' is missing from plugin.yml.");
            return;
        }

        StarterCommand executor = new StarterCommand(this, configManager, starterKitService, starterMenu);
        starterCommand.setExecutor(executor);
        starterCommand.setTabCompleter(executor);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(configManager), this);
        getServer().getPluginManager().registerEvents(new MenuClickListener(configManager, starterKitService), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public StarterKitService getStarterKitService() {
        return starterKitService;
    }

    public StarterMenu getStarterMenu() {
        return starterMenu;
    }
}
