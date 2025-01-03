package net.jeqo.bloons.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *                  A utility class intended to get messages from the config.yml file
 * @param instance  The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
 */
public record MessageTranslations(JavaPlugin instance) {

    /**
     *                      Converts a message containing MiniMessage serialized strings to a Component that can be used to
     *                      send formatted messages with gradients, rainbows, etc.
     * @param messagePrefix The prefix of the message, type java.lang.String
     * @param messageSuffix The suffix of the message, type java.lang.String
     * @return              The formatted message as a Component, type net.kyori.adventure.text.Component
     */
    public Component getSerializedString(String messagePrefix, String messageSuffix) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(messagePrefix + messageSuffix);
    }

    /**
     *                  Converts a message containing MiniMessage serialized strings to a Component that can be used to
     *                  send formatted messages with gradients, rainbows, etc.
     * @param message   The message to convert to a deserialized component, type java.lang.String
     * @return          The formatted message as a Component, type net.kyori.adventure.text.Component
     */
    public Component getSerializedString(String message) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

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
