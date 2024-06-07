package net.jeqo.bloons.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A utility class intended to get messages from the config.yml file
 *
 * @param instance The instance of the plugin
 */
public record MessageTranslations(JavaPlugin instance) {

    /**
     * Get a message from the config.yml file
     * @param id The ID of the message
     * @param arg The argument to format the message with
     * @return The formatted message
     */
    public String getMessage(String id, String arg) {
        return String.format(this.instance.getConfig().getString("messages." + id, ""), arg);
    }

    /**
     * Get a message from the config.yml file
     * @param id The ID of the message
     * @return The message as a string
     */
    public String getMessage(String id) {
        return this.instance.getConfig().getString("messages." + id, "");
    }

    /**
     * Converts a message containing MiniMessage serialized strings to a Component that can be used to
     * send formatted messages with gradients, rainbows, etc.
     * @param messagePrefix The prefix of the message
     * @param messageSuffix The suffix of the message
     * @return The formatted message as a Component
     */
    public Component getSerializedString(String messagePrefix, String messageSuffix) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(messagePrefix + messageSuffix);
    }

    /**
     * Converts a message containing MiniMessage serialized strings to a Component that can be used to
     * send formatted messages with gradients, rainbows, etc.
     * @param message The message to convert
     * @return The formatted message as a Component
     */
    public Component getSerializedString(String message) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(message);
    }

    /**
     * Get a string from the config.yml file
     * @param path The path to the string
     * @return The string at the specified path
     */
    public String getString(String path) {
        return this.instance.getConfig().getString(path);
    }

    /**
     * Get an integer from the config.yml file
     * @param path The path to the integer
     * @return The integer at the specified path
     */
    public Integer getInt(String path) {
        return this.instance.getConfig().getInt(path);
    }
}
