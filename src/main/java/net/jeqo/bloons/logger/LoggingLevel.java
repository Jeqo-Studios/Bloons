package net.jeqo.bloons.logger;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The different levels of logging that can be used in the plugin
 * Contains the name and color of the logging level to use in the Bukkit console
 */
@Getter
public enum LoggingLevel {
    /**
     * Used in the case there is a soft error
     */
    WARNING("WARNING", NamedTextColor.RED, "§c"),
    /**
     * Used in the case there is an informational message
     */
    INFO("INFO", NamedTextColor.YELLOW, "§e"),
    /**
     * Used in the case there is a hard error
     */
    ERROR("ERROR", NamedTextColor.DARK_RED, "§4"),
    /**
     * Used for debugging purposes only
     */
    DEBUG("DEBUG", NamedTextColor.WHITE, "§f");

    private final String name;
    private final NamedTextColor color;
    private final String spigotColor;

    /**
     *              Create a new logging level
     * @param name  The name of the logging level to use in the console, type java.lang.String
     * @param color The Minecraft chat color of the logging level, type net.kyori.adventure.text.format.NamedTextColor
     * @param spigotColor The colour used to send coloured messages using the spigot message system
     */
    LoggingLevel(String name, NamedTextColor color, String spigotColor) {
        this.name = name;
        this.color = color;
        this.spigotColor = spigotColor;
    }
}