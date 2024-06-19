package net.jeqo.bloons.gui.item.border;

import net.jeqo.bloons.gui.GUIClickableItem;
import net.jeqo.bloons.utils.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * An example class to represent how a premade border item can be created
 */
public class BlackGlassPaneBorder {

    /**
     *              A method to create a black glass pane border item
     * @param slot  The slot of the item in the inventory, type int
     * @return      A GUIClickableItem object that represents the black glass pane border item, type net.jeqo.bloons.gui.GUIClickableItem
     */
    public static GUIClickableItem getItem(int slot) {
        return new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent event) {
                event.setCancelled(true);
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public NBTItem getItem() {
                // Create and set the item meta for the black glass pane border
                NBTItem item = new NBTItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text(" "));
                item.setItemMeta(meta);

                return item;
            }
        };
    }
}
