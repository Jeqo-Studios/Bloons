package net.jeqo.bloons.gui.item.filler;

import net.jeqo.bloons.gui.GUIClickableItem;
import net.jeqo.bloons.item.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * An example class to represent how a premade filler item can be created
 */
public class BlackGlassPaneFillerItem {

    /**
     *             A method to create a black glass pane filler item
     * @param slot The slot of the item in the inventory, type int
     * @return     A GUIClickableItem object that represents the black glass pane filler item, type net.jeqo.bloons.gui.GUIClickableItem
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
                // Create and set the item meta for the black glass pane filler
                NBTItem item = new NBTItem(new ItemStack(Material.COOKIE));
                ItemMeta meta = item.getItemMeta();

                if (meta == null) return null;

                meta.setDisplayName("");
                item.setItemMeta(meta);

                return item;
            }
        };
    }
}
