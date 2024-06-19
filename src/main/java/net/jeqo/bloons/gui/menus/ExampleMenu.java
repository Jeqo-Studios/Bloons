package net.jeqo.bloons.gui.menus;

import net.jeqo.bloons.gui.GUI;
import net.jeqo.bloons.gui.GUIClickableItem;
import net.jeqo.bloons.gui.item.border.BlackGlassPaneBorder;
import net.jeqo.bloons.gui.item.filler.BlackGlassPaneFillerItem;
import net.jeqo.bloons.utils.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ExampleMenu extends GUI {
    Inventory inventory;

    /**
     *          The inventory that is displayed to the player
     * @return  The inventory, type org.bukkit.inventory.Inventory
     */
    @Override
    public Inventory inventory() {
        this.inventory = Bukkit.createInventory(null, slots(), name());
        return this.inventory;
    }

    /**
     *          The name that is displayed as the GUI title
     * @return  The name, type net.kyori.adventure.text.Component
     */
    @Override
    public Component name() {
        return Component.text("Example GUI");
    }

    /**
     *          The number of slots that are present within the GUI
     *          It should be a multiple of 9. 27 and 54 are common values.
     * @return  The number of slots contained within the GUI, type int
     */
    @Override
    public int slots() {
        return 27;
    }

    /**
     *          The filler item that is used to fill empty slots in the GUI
     * @return  The filler item, type net.jeqo.bloons.gui.GUIClickableItem
     */
    @Override
    public GUIClickableItem fillerItem(int slot) {
        return BlackGlassPaneFillerItem.getItem(slot);
    }

    /**
     *              The border item that is used to create a border around the GUI
     * @param slot  The slot of the item in the inventory, type int
     * @return      The border item, type net.jeqo.bloons.gui.GUIClickableItem
     */
    @Override
    public GUIClickableItem borderItem(int slot) {
        return BlackGlassPaneBorder.getItem(slot);
    }

    /**
     *                      Creates a clickable item that can't be picked up with the click event cancelled
     * @param currentItem   The item that is being clicked on
     * @param slot          The slot that the item is in
     * @return              A GUIClickableItem object that represents the item that can't be picked up, type net.jeqo.bloons.gui.GUIClickableItem
     */
    @Override
    public GUIClickableItem cantPickup(NBTItem currentItem, int slot) {
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
                return currentItem;
            }
        };
    }

    /**
     *                  Determines what happens when the GUI is closed
     * @param player    The player that closed the GUI, type org.bukkit.entity.Player
     */
    @Override
    public void onClose(Player player) {
        player.sendMessage("You closed the GUI!");
    }

    /**
     *                  Determines what happens when the GUI is opened
     * @param player    The player that opened the GUI, type org.bukkit.entity.Player
     */
    @Override
    public void open(Player player) {
        fillAllSlots();
    }

    /**
     * Determines what happens when the GUI is updated (this happens every 20 ticks)
     */
    @Override
    public void update() {}
}
