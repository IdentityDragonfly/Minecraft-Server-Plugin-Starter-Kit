package io.github.example.starterplugin.kits;

import io.github.example.starterplugin.config.ConfigManager;
import io.github.example.starterplugin.cooldown.CooldownManager;
import io.github.example.starterplugin.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StarterKitService {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final CooldownManager cooldownManager;

    public StarterKitService(JavaPlugin plugin, ConfigManager configManager, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
    }

    public void giveStarterKit(Player player) {
        FileConfiguration config = configManager.config();

        if (!config.getBoolean("starter-kit.enabled", true)) {
            player.sendMessage(configManager.message("kit-disabled"));
            return;
        }

        if (!player.hasPermission("starter.kit")) {
            player.sendMessage(configManager.message("no-permission"));
            return;
        }

        long cooldownSeconds = config.getLong("starter-kit.cooldown-seconds", 3600L);
        if (cooldownManager.isOnCooldown(player.getUniqueId(), cooldownSeconds)) {
            long remaining = cooldownManager.remainingSeconds(player.getUniqueId(), cooldownSeconds);
            player.sendMessage(configManager.message("kit-cooldown", Map.of("time", MessageUtils.formatDuration(remaining))));
            return;
        }

        List<ItemStack> items = loadKitItems(config);
        boolean droppedOverflow = false;

        for (ItemStack item : items) {
            HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(item);
            if (!overflow.isEmpty()) {
                droppedOverflow = true;
                Location location = player.getLocation();
                overflow.values().forEach(leftover -> player.getWorld().dropItemNaturally(location, leftover));
            }
        }

        cooldownManager.setCooldown(player.getUniqueId());
        player.sendMessage(configManager.message("kit-received"));

        if (droppedOverflow) {
            player.sendMessage(configManager.message("inventory-full"));
        }
    }

    private List<ItemStack> loadKitItems(FileConfiguration config) {
        List<ItemStack> items = new ArrayList<>();
        List<Map<?, ?>> configuredItems = config.getMapList("starter-kit.items");

        for (Map<?, ?> itemConfig : configuredItems) {
            Object materialValue = itemConfig.get("material");
            if (materialValue == null) {
                continue;
            }

            Material material = Material.matchMaterial(String.valueOf(materialValue));
            if (material == null) {
                plugin.getLogger().warning("Unknown material in starter kit: " + materialValue);
                continue;
            }

            int amount = parseInt(itemConfig.get("amount"), 1);
            ItemStack item = new ItemStack(material, Math.max(1, amount));
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                Object name = itemConfig.get("name");
                if (name != null) {
                    meta.displayName(MessageUtils.color(String.valueOf(name)));
                }

                Object loreValue = itemConfig.get("lore");
                if (loreValue instanceof List<?> loreLines) {
                    List<Component> lore = loreLines.stream()
                        .map(String::valueOf)
                        .map(MessageUtils::color)
                        .toList();
                    meta.lore(lore);
                }

                item.setItemMeta(meta);
            }

            items.add(item);
        }

        return items;
    }

    private int parseInt(Object value, int fallback) {
        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }
}
