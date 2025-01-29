package net.jeqo.bloons.message;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 *                  A utility class intended to get messages from the config.yml file
 * @param instance  The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
 */
public record MessageTranslations(JavaPlugin instance) {

    /**
     *              Get a string from the config.yml file
     * @param path  The path to the string, type java.lang.String
     * @return      The value at the specified path, type java.lang.String
     */
    public String getString(String path) {
        if (this.instance.getConfig().getString(path) != null) {
            return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.instance.getConfig().getString(path)));
        }
        return "";
    }

    /**
     *              Get an integer from the config.yml file
     * @param path  The path to the integer, type java.lang.String
     * @return      The value at the specified path, type java.lang.Integer
     */
    public Integer getInt(String path) {
        return this.instance.getConfig().getInt(path);
    }
}
