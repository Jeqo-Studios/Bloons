package net.jeqo.bloons.gui;

import net.jeqo.bloons.utils.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * A listener to handle GUI click events
 */
public class GUIListener implements Listener {

    /**
     *              Handles the inventory click event of a GUI
     * @param event The event that is fired when an item is clicked in an inventory, type org.bukkit.event.inventory.InventoryClickEvent
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        NBTItem item = (NBTItem) event.getCurrentItem();

        if (item.getType() != Material.AIR) return;

        if (!item.hasKey(GUIHelpers.getClickableItemFlag())) return;

        // Get the clickable item and run it, and/or cancel it if it can't be picked up
        GUIClickableItem clickableItem = GUIClickableItem.itemData.get(item.getStringFlag(GUIHelpers.getClickableItemFlag()));
        clickableItem.run(event);
        if (!clickableItem.canPickup()) {
            event.setCancelled(true);
        }
    }
}
