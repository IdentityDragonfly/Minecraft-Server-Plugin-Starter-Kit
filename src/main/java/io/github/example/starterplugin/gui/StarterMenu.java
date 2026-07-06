package io.github.example.starterplugin.gui;

import io.github.example.starterplugin.config.ConfigManager;
import io.github.example.starterplugin.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class StarterMenu {

    private final ConfigManager configManager;

    public StarterMenu(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void open(Player player) {
        int size = normalizeSize(configManager.config().getInt("gui.size", 27));
        String title = configManager.config().getString("gui.title", "&aStarter Menu");

        StarterMenuHolder holder = new StarterMenuHolder();
        Inventory inventory = Bukkit.createInventory(holder, size, MessageUtils.color(title));
        holder.setInventory(inventory);

        fill(inventory);

        int kitSlot = configManager.config().getInt("gui.kit-slot", 11);
        int closeSlot = configManager.config().getInt("gui.close-slot", 15);

        if (isValidSlot(kitSlot, inventory.getSize())) {
            inventory.setItem(kitSlot, menuItem(
                Material.CHEST,
                "&aStarter Kit",
                List.of("&7Click to receive your starter kit.", "&8Permission: starter.kit")
            ));
        }

        if (isValidSlot(closeSlot, inventory.getSize())) {
            inventory.setItem(closeSlot, menuItem(
                Material.BARRIER,
                "&cClose Menu",
                List.of("&7Click to close this menu.")
            ));
        }

        player.openInventory(inventory);
    }

    private void fill(Inventory inventory) {
        String fillerMaterialName = configManager.config().getString("gui.filler-material", "GRAY_STAINED_GLASS_PANE");
        Material fillerMaterial = Material.matchMaterial(fillerMaterialName == null ? "GRAY_STAINED_GLASS_PANE" : fillerMaterialName);
        if (fillerMaterial == null) {
            fillerMaterial = Material.GRAY_STAINED_GLASS_PANE;
        }

        ItemStack filler = menuItem(fillerMaterial, " ", List.of());
        for (int index = 0; index < inventory.getSize(); index++) {
            inventory.setItem(index, filler);
        }
    }

    private ItemStack menuItem(Material material, String name, List<String> loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(MessageUtils.color(name));
            meta.lore(loreLines.stream().map(MessageUtils::color).toList());
            item.setItemMeta(meta);
        }

        return item;
    }

    private int normalizeSize(int configuredSize) {
        int size = Math.max(9, Math.min(54, configuredSize));
        return size - (size % 9);
    }

    private boolean isValidSlot(int slot, int size) {
        return slot >= 0 && slot < size;
    }
}
