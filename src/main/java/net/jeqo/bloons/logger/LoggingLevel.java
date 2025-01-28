package net.jeqo.bloons.logger;

import lombok.Getter;
import org.bukkit.ChatColor;

/**
 * The different levels of logging that can be used in the plugin
 * Contains the name and color of the logging level to use in the Bukkit console
 */
@Getter
public enum LoggingLevel {
    /**
     * Used in the case there is a soft error
     */
    WARNING("WARNING", ChatColor.RED),
    /**
     * Used in the case there is an informational message
     */
    INFO("INFO", ChatColor.YELLOW),
    /**
     * Used in the case there is a hard error
     */
    ERROR("ERROR", ChatColor.DARK_RED),
    /**
     * Used for debugging purposes only
     */
    DEBUG("DEBUG", ChatColor.WHITE);

    private final String name;
    private final ChatColor color;

    /**
     *              Create a new logging level
     * @param name  The name of the logging level to use in the console, type java.lang.String
     * @param color The Minecraft chat color of the logging level, type net.kyori.adventure.text.format.NamedTextColor
     */
    LoggingLevel(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }
}