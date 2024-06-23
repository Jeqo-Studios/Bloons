package net.jeqo.bloons.gui;

import net.jeqo.bloons.events.BloonsEvent;
import net.jeqo.bloons.item.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * An abstract class that represents a GUI instance
 */
public abstract class GUI {
    BukkitTask updater;

    /**
     *          The inventory that is displayed to the player
     * @return  The inventory, type org.bukkit.inventory.Inventory
     */
    public abstract Inventory inventory();

    /**
     *          The name that is displayed as the GUI title
     * @return  The name, type net.kyori.adventure.text.Component
     */
    public abstract Component name();

    /**
     *          The number of slots that are present within the GUI
     *          It should be a multiple of 9. 27 and 54 are common values.
     * @return  The number of slots contained within the GUI, type int
     */
    public abstract int slots();

    /**
     *              The filler item that is used to fill empty slots in the GUI
     * @param slot  The slot that the item is in, type int
     * @return      The filler item, type net.jeqo.bloons.gui.GUIClickableItem
     */
    public abstract GUIClickableItem fillerItem(int slot);

    /**
     *              The border item that is used to create a border around the GUI
     * @param slot  The slot that the item is in, type int
     * @return      The border item, type net.jeqo.bloons.gui.GUIClickableItem
     */
    public abstract GUIClickableItem borderItem(int slot);

    /**
     *              The item that is displayed in the GUI but can't be picked up
     * @param item  The item that can't be picked up, type net.jeqo.bloons.utils.NBTItem
     * @param slot  The slot that the item is in, type int
     * @return      The item, type net.jeqo.bloons.gui.GUIClickableItem
     */
    public abstract GUIClickableItem cantPickup(NBTItem item, int slot);

    /**
     *          The event that is fired when the GUI is opened or triggered
     * @return  The event, type net.jeqo.bloons.events.BloonsEvent
     */
    public BloonsEvent triggerEvent() {
        return null;
    }

    /**
     *                  Determines what happens when the GUI is closed
     * @param player    The player that closed the GUI, type org.bukkit.entity.Player
     */
    public abstract void onClose(Player player);

    /**
     *                  Determines what happens when the GUI is opened
     * @param player    The player that opened the GUI, type org.bukkit.entity.Player
     */
    public abstract void open(Player player);

    /**
     * Determines what happens when the GUI is updated (this happens every 20 ticks)
     */
    public abstract void update();

    /**
     *                  Starts the GUI updater to update the GUI every 20 ticks
     *                  This should be executed upon the opening of the GUI
     * @param plugin    The plugin that the GUI is being opened in
     */
    public void startUpdater(JavaPlugin plugin) {
        this.updater = Bukkit.getScheduler().runTaskTimer(plugin, this::update, 0, 20);
    }

    /**
     * Cancels the GUI updater to stop updating the GUI
     * This should be executed upon the closing of the GUI
     */
    public void stopUpdater() {
        this.updater.cancel();
    }

    /**
     *              Adds an item to the GUI
     * @param item  The item to add, type net.jeqo.bloons.gui.GUIClickableItem
     */
    public void addItem(GUIClickableItem item) {
        inventory().setItem(item.getSlot(), item.getFinalizedItem().getItem());
    }

    /**
     *              Adds an item to the GUI at a specific slot
     * @param item  The item to add, type net.jeqo.bloons.gui.GUIClickableItem
     * @param slot  The slot to add the item to, type int
     */
    public void addItem(GUIClickableItem item, int slot) {
        inventory().setItem(slot, item.getItem());
    }

    /**
     *              Gets the item in a specific slot
     * @param slot  The slot to get the item from, type int
     */
    public void getItem(int slot) {
        inventory().getItem(slot);
    }

    /**
     * Sets a border around the GUI with a specific item
     */
    public void setBorder() {
        int size = inventory().getSize();
        if (size < 27) return;

        for (int i = 0; i < 9; i++) {
            addItem(borderItem(i), i);
        }

        for(int i = 0; i < Math.ceil(size / 10.0); i++) {
            addItem(borderItem(i), (8 + (9 * i)));
            if(9 + (9 * i) > size - 1) continue;
            addItem(borderItem(i), (9 + (9 * i)));
        }

        for(int i = size - 9; i < size; i++) {
            addItem(borderItem(i), (i));
        }
    }

    /**
     * Fills all empty slots in the GUI with the filler item
     */
    public void fillEmptySlots() {
        for (int i = 0; i < slots(); i++) {
            if (inventory().getItem(i) == null) {
                inventory().setItem(i, fillerItem(i).getItem());
            }
        }
    }

    /**
     * Fills all slots in the GUI with the filler item
     */
    public void fillAllSlots() {
        for (int i = 0; i < slots(); i++) {
            inventory().setItem(i, fillerItem(i).getItem());
        }
    }
}