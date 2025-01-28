package net.jeqo.bloons.message;

import org.bukkit.plugin.java.JavaPlugin;

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
        return this.instance.getConfig().getString(path);
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
