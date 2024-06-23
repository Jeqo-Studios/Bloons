package net.jeqo.bloons.item;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * An extension of an Item with the utilities to manage NBT
 * data easier and more efficiently
 */
@Getter @Setter
public class NBTItem extends ItemStack {
    private ItemStack item;

    /**
     *              Creates a new NBTItem with NBT data from an existing ItemStack
     * @param item  The item to derive the NBTItem from, type org.bukkit.inventory.ItemStack
     */
    public NBTItem(ItemStack item) {
        this.setItem(item);
    }

    /**
     *              Checks if the item has a key in its NBT data
     * @param key   The key to check for, type java.lang.String
     * @return      Whether the key exists in the NBT data, type boolean
     */
    public boolean hasKey(String key) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);
        PersistentDataContainer container = this.getItem().getItemMeta().getPersistentDataContainer();

        return container.getKeys().contains(nbtKey);
    }

    /**
     *              Sets a string flag in the NBT data of the item
     * @param key   The key to set, type java.lang.String
     * @param value The value to set the key to, type java.lang.String
     */
    public void setStringFlag(String key, String value) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);

        ItemMeta meta = getItem().getItemMeta();
        meta.getPersistentDataContainer().set(nbtKey, PersistentDataType.STRING, value);

        this.getItem().setItemMeta(meta);
    }

    /**
     *              Gets a string flag from the NBT data of the item
     * @param key   The key to get, type java.lang.String
     * @return      The value of the key, type java.lang.String
     */
    public String getStringFlag(String key) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);
        PersistentDataContainer container = this.getItem().getItemMeta().getPersistentDataContainer();

        if (!container.has(nbtKey, PersistentDataType.STRING)) return null;

        return container.get(nbtKey, PersistentDataType.STRING);
    }

    /**
     *              Sets an integer flag in the NBT data of the item
     * @param key   The key to set, type java.lang.String
     * @param value The value to set the key to, type int
     */
    public void setIntegerFlag(String key, int value) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);

        ItemMeta meta = this.getItem().getItemMeta();
        meta.getPersistentDataContainer().set(nbtKey, PersistentDataType.INTEGER, value);

        this.getItem().setItemMeta(meta);
    }

    /**
     *              Gets an integer flag from the NBT data of the item
     * @param key   The key to get, type java.lang.String
     * @return      The value of the key, type int
     */
    public int getIntegerFlag(String key) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);
        PersistentDataContainer container = this.getItem().getItemMeta().getPersistentDataContainer();

        if (!container.has(nbtKey, PersistentDataType.INTEGER)) return 0;

        return container.get(nbtKey, PersistentDataType.INTEGER);
    }

    /**
     *              Sets a double flag in the NBT data of the item
     * @param key   The key to set, type java.lang.String
     * @param value The value to set the key to, type double
     */
    public void setDoubleFlag(String key, double value) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);

        ItemMeta meta = this.getItem().getItemMeta();
        meta.getPersistentDataContainer().set(nbtKey, PersistentDataType.DOUBLE, value);

        this.getItem().setItemMeta(meta);
    }

    /**
     *              Gets a double flag from the NBT data of the item
     * @param key   The key to get, type java.lang.String
     * @return      The value of the key, type double
     */
    public double getDoubleFlag(String key) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);
        PersistentDataContainer container = this.getItem().getItemMeta().getPersistentDataContainer();

        if (!container.has(nbtKey, PersistentDataType.DOUBLE)) return 0.0;

        return container.get(nbtKey, PersistentDataType.DOUBLE);
    }

    /**
     *              Sets a boolean flag in the NBT data of the item
     * @param key   The key to set, type java.lang.String
     * @param value The value to set the key to, type boolean
     */
    public void setBooleanFlag(String key, boolean value) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);

        ItemMeta meta = this.getItem().getItemMeta();
        meta.getPersistentDataContainer().set(nbtKey, PersistentDataType.BOOLEAN, value);

        this.getItem().setItemMeta(meta);
    }

    /**
     *              Gets a boolean flag from the NBT data of the item
     * @param key   The key to get, type java.lang.String
     * @return      The value of the key, type boolean
     */
    public boolean getBooleanFlag(String key) {
        NamespacedKey nbtKey = new NamespacedKey(Bloons.getInstance(), key);
        PersistentDataContainer container = this.getItem().getItemMeta().getPersistentDataContainer();

        if (!container.has(nbtKey, PersistentDataType.BOOLEAN)) return false;

        return Boolean.TRUE.equals(container.get(nbtKey, PersistentDataType.BOOLEAN));
    }
}
