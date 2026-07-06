package io.github.example.starterplugin.listeners;

import io.github.example.starterplugin.config.ConfigManager;
import io.github.example.starterplugin.gui.StarterMenuHolder;
import io.github.example.starterplugin.kits.StarterKitService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class MenuClickListener implements Listener {

    private final ConfigManager configManager;
    private final StarterKitService starterKitService;

    public MenuClickListener(ConfigManager configManager, StarterKitService starterKitService) {
        this.configManager = configManager;
        this.starterKitService = starterKitService;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof StarterMenuHolder)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        int clickedSlot = event.getRawSlot();
        int kitSlot = configManager.config().getInt("gui.kit-slot", 11);
        int closeSlot = configManager.config().getInt("gui.close-slot", 15);

        if (clickedSlot == kitSlot) {
            starterKitService.giveStarterKit(player);
            player.closeInventory();
            return;
        }

        if (clickedSlot == closeSlot) {
            player.closeInventory();
        }
    }
}
