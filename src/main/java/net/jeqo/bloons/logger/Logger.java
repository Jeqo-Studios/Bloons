package net.jeqo.bloons.logger;

import net.jeqo.bloons.configuration.PluginConfiguration;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;

/**
 * A utility class intended to log messages easily to the Bukkit console
 */
public class Logger {
    /**
     * Log a message to the console with STDOUT
     * @param message The message to log
     */
    public static void logWithSTDOUT(String message) {
        System.out.println(message);
    }

    /**
     * Logs a message to the specified player
     * @param level The logging level
     * @param player The player to log the message to
     * @param message The message to log
     */
    public static void logToPlayer(LoggingLevel level, Player player, String message) {
        Component component = Component.text(level.getColor() + "[" + level.getName() + "] " + message);
        player.sendMessage(component);
    }

    /**
     * Log a message to the console
     * @param level The logging level
     * @param message The message to log
     */
    public static void log(LoggingLevel level, String message) {
        Component component = Component.text(level.getColor() + "[" + level.getName() + "] " + message);
        Bukkit.getServer().getConsoleSender().sendMessage(component);
    }

    /**
     * Logs a message to the console with the WARNING level
     * @param message The message to log
     */
    public static void logWarning(String message) {
        log(LoggingLevel.WARNING, message);
    }

    /**
     * Logs a message to the console with the INFO level
     * @param message The message to log
     */
    public static void logInfo(String message) {
        log(LoggingLevel.INFO, message);
    }

    /**
     * Logs a message to the console with the ERROR level
     * @param message The message to log
     */
    public static void logError(String message) {
        log(LoggingLevel.ERROR, message);
    }

    /**
     * Logs a message to the console with the DEBUG level
     * @param message The message to log
     */
    public static void logDebug(String message) {
        log(LoggingLevel.DEBUG, message);
    }

    /**
     * Logs an initialization message to the Bukkit console
     */
    public static void logInitialization() throws XmlPullParserException, IOException {
        log(LoggingLevel.INFO, "Initializing" + PluginConfiguration.getName() + " plugin...");
    }

    /**
     * Logs a startup message to the Bukkit console containing plugin information
     */
    public static void logStartup() throws XmlPullParserException, IOException {
        log(LoggingLevel.INFO, PluginConfiguration.getName() + "plugin has initialized");
        log(LoggingLevel.INFO, "Version: " + PluginConfiguration.getVersion());
        log(LoggingLevel.INFO, "Developers: " + PluginConfiguration.DEVELOPER_CREDITS);
    }

    /**
     * Logs a shutdown message to the Bukkit console
     */
    public static void logShutdown() throws XmlPullParserException, IOException {
        log(LoggingLevel.INFO, PluginConfiguration.getName() + "plugin has been shutdown gracefully");
    }
}
