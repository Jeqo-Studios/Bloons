package net.jeqo.bloons.gui.item.filler;

import net.jeqo.bloons.gui.GUIClickableItem;
import net.jeqo.bloons.utils.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * An example class to represent how a premade filler item can be created
 */
public class BlackGlassPaneFillerItem {
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
                NBTItem item = new NBTItem(new ItemStack(Material.COOKIE));
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text(" "));
                item.setItemMeta(meta);
                return item;
            }
        };
    }
}
