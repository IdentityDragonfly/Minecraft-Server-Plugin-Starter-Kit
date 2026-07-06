package io.github.example.starterplugin.commands;

import io.github.example.starterplugin.StarterPlugin;
import io.github.example.starterplugin.config.ConfigManager;
import io.github.example.starterplugin.gui.StarterMenu;
import io.github.example.starterplugin.kits.StarterKitService;
import io.github.example.starterplugin.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class StarterCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("help", "kit", "menu", "reload");

    private final StarterPlugin plugin;
    private final ConfigManager configManager;
    private final StarterKitService starterKitService;
    private final StarterMenu starterMenu;

    public StarterCommand(
        StarterPlugin plugin,
        ConfigManager configManager,
        StarterKitService starterKitService,
        StarterMenu starterMenu
    ) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.starterKitService = starterKitService;
        this.starterMenu = starterMenu;
    }

    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender, label);
            return true;
        }

        String subcommand = args[0].toLowerCase(Locale.ROOT);
        switch (subcommand) {
            case "kit" -> handleKit(sender);
            case "menu" -> handleMenu(sender);
            case "reload" -> handleReload(sender);
            default -> sender.sendMessage(configManager.message("unknown-command"));
        }

        return true;
    }

    private void handleKit(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(configManager.message("player-only"));
            return;
        }

        starterKitService.giveStarterKit(player);
    }

    private void handleMenu(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(configManager.message("player-only"));
            return;
        }

        if (!player.hasPermission("starter.menu")) {
            player.sendMessage(configManager.message("no-permission"));
            return;
        }

        starterMenu.open(player);
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("starter.admin")) {
            sender.sendMessage(configManager.message("no-permission"));
            return;
        }

        configManager.reload();
        sender.sendMessage(configManager.message("reload-success"));
        plugin.getLogger().info("Configuration reloaded by " + sender.getName() + ".");
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(MessageUtils.color("&8&m--------------------------------------------------"));
        sender.sendMessage(MessageUtils.color("&a&lStarterPlugin &7- Minecraft Server Plugin Starter Kit"));
        sender.sendMessage(Component.empty());
        sender.sendMessage(MessageUtils.color("&f/" + label + " help &8- &7Show this help menu"));
        sender.sendMessage(MessageUtils.color("&f/" + label + " kit &8- &7Receive your starter kit"));
        sender.sendMessage(MessageUtils.color("&f/" + label + " menu &8- &7Open the starter GUI menu"));
        if (sender.hasPermission("starter.admin")) {
            sender.sendMessage(MessageUtils.color("&f/" + label + " reload &8- &7Reload the plugin config"));
        }
        sender.sendMessage(MessageUtils.color("&8&m--------------------------------------------------"));
    }

    @Override
    public @Nullable List<String> onTabComplete(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (args.length != 1) {
            return List.of();
        }

        String input = args[0].toLowerCase(Locale.ROOT);
        List<String> suggestions = new ArrayList<>();

        for (String subcommand : SUBCOMMANDS) {
            if (subcommand.equals("reload") && !sender.hasPermission("starter.admin")) {
                continue;
            }

            if (subcommand.startsWith(input)) {
                suggestions.add(subcommand);
            }
        }

        return suggestions;
    }
}
