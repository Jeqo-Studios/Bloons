package net.jeqo.bloons.gui;

import net.jeqo.bloons.utils.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * A class that represents a clickable item in a GUI, both interactable and
 * non-interactable.
 */
public abstract class GUIClickableItem {
    public static HashMap<String, GUIClickableItem> itemData = new HashMap<>();
    private final String uuid;

    /**
     * Creates a new clickable item
     */
    public GUIClickableItem() {
        this.uuid = UUID.randomUUID().toString();
        itemData.put(uuid, this);
    }

    /**
     * Gets the finalized item with the GUI NBT data attached
     * @return The finalized item
     */
    public NBTItem getFinalizedItem() {
        NBTItem item = getItem();
        item.setStringFlag(GUIHelpers.getClickableItemFlag(), uuid);
        return item;
    }

    /**
     * What runs when the item is clicked
     * @param event The event that is fired when the item is clicked
     */
    public abstract void run(InventoryClickEvent event);

    /**
     * The slot that the item is in
     * @return The slot that the item is in as an Integer
     */
    public abstract int getSlot();

    /**
     * The item that is displayed in the GUI
     * @return The item as a WynncraftItem object
     */
    public abstract NBTItem getItem();

    /**
     * Whether the item can be picked up
     * @return Whether the item can be picked up
     */
    public boolean canPickup() {
        return false;
    }
}
