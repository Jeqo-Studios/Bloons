package net.jeqo.bloons.gui.menus;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.colors.Color;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.message.MessageTranslations;
import net.jeqo.bloons.utils.CustomModelDataCompat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A class that represents a balloon menu
 */
@Getter
public class BalloonMenu {
    /**
     * The pages of the menu
     */
    public ArrayList<Inventory> pages = new ArrayList<>();
    /**
     * The unique identifier of the menu
     */
    @Setter
    public UUID id;
    /**
     * The current page index
     */
    @Setter
    public int currentPageIndex = 0;
    /**
     * The users that have the menu open
     */
    @Getter
    public static HashMap<UUID, BalloonMenu> users = new HashMap<>();
    private final MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());

    /**
     *              Gets a blank GUI menu with the next page, previous page, and unequip buttons
     * @param name  The name of the GUI menu, type java.lang.String
     * @return      The blank GUI menu, type org.bukkit.inventory.Inventory
     */
    private Inventory getBlankPage(String name){
        int pageSize = this.getMessageTranslations().getInt("menu-size");
        Inventory page = Bukkit.createInventory(null, pageSize, Color.fromHex(name));

        // Create next page button
        ItemStack nextPage = new ItemStack(Material.valueOf(this.getMessageTranslations().getString("buttons.next-page.material")));
        ItemMeta nextMeta = nextPage.getItemMeta();
        assert nextMeta != null;
        nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.getMessageTranslations().getString("buttons.next-page.name")));
        CustomModelDataCompat.applyCustomModelData(nextMeta, List.of(this.getMessageTranslations().getString("buttons.next-page.custom-model-data")));
        nextPage.setItemMeta(nextMeta);

        // Create previous page button
        ItemStack prevPage = new ItemStack(Material.valueOf(this.getMessageTranslations().getString("buttons.previous-page.material")));
        ItemMeta prevMeta = prevPage.getItemMeta();
        assert prevMeta != null;
        prevMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.getMessageTranslations().getString("buttons.previous-page.name")));
        CustomModelDataCompat.applyCustomModelData(prevMeta, List.of(this.getMessageTranslations().getString("buttons.previous-page.custom-model-data")));
        prevPage.setItemMeta(prevMeta);

        // Create remove/unequip balloon button
        ItemStack removeBalloon = new ItemStack(Material.valueOf(this.getMessageTranslations().getString("buttons.unequip.material")));
        ItemMeta removeMeta = removeBalloon.getItemMeta();
        assert removeMeta != null;
        removeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.getMessageTranslations().getString("buttons.unequip.name")));

        CustomModelDataCompat.applyCustomModelData(removeMeta, List.of(this.getMessageTranslations().getString("buttons.unequip.custom-model-data")));
        removeBalloon.setItemMeta(removeMeta);

        // Add buttons to GUI
        List<String> previousPageSlots = Bloons.getInstance().getConfig().getStringList("buttons.previous-page.slots");
        for (String previousPageSlot : previousPageSlots) {
            if (Integer.parseInt(previousPageSlot) < pageSize) {
                page.setItem(Integer.parseInt(previousPageSlot), prevPage);
            } else {
                Logger.logWarning(String.format(Languages.getMessage("menu-slot-out-of-bounds"), "Previous"));
            }
        }

        List<String> unequipSlots = Bloons.getInstance().getConfig().getStringList("buttons.unequip.slots");
        for (String unequipSlot : unequipSlots) {
            if (Integer.parseInt(unequipSlot) < pageSize) {
                page.setItem(Integer.parseInt(unequipSlot), removeBalloon);
            } else {
                Logger.logWarning(String.format(Languages.getMessage("menu-slot-out-of-bounds"), "Unequip"));
            }
        }

        List<String> nextPageSlots = Bloons.getInstance().getConfig().getStringList("buttons.next-page.slots");
        for (String nextPageSlot : nextPageSlots) {
            if (Integer.parseInt(nextPageSlot) < pageSize) {
                page.setItem(Integer.parseInt(nextPageSlot), nextPage);
            } else {
                Logger.logWarning(String.format(Languages.getMessage("menu-slot-out-of-bounds"), "Next"));
            }
        }

        return page;
    }

    /**
     *                  Creates a new balloon menu
     * @param items     The items to display in the menu, type java.util.ArrayList[org.bukkit.inventory.ItemStack]
     * @param name      The name of the menu, type java.lang.String
     * @param player    The player to open the menu for, type org.bukkit.entity.Player
     */
    public BalloonMenu(ArrayList<ItemStack> items, String name, Player player) {
        this.setId(UUID.randomUUID());
        Inventory page = getBlankPage(name);
        int slot = 0;

        for (ItemStack item : items) {
            while (isSideSlot(slot)) {
                slot++;
            }

            page.setItem(slot, item);
            slot++;

            // Check if we need to start a new page
            if (slot >= this.getMessageTranslations().getInt("balloon-slots")) {
                this.getPages().add(page);
                page = getBlankPage(name);
                slot = 0;
            }
        }

        // Add the last page if it has items
        if (slot > 0) {
            this.getPages().add(page);
        }

        player.openInventory(this.getPages().get(this.getCurrentPageIndex()));
        users.put(player.getUniqueId(), this);
    }

    /**
     *                 Checks if the slot is a side slot
     * @param slot     The slot to check, type int
     * @return         True if the slot is a side slot, false otherwise, type boolean
     */
    private boolean isSideSlot(int slot) {
        return slot == 0 || slot == 8 || slot == 9 || slot == 17 || slot == 18 || slot == 26 || slot == 27 || slot == 35 || slot == 36 || slot == 44;
    }

}