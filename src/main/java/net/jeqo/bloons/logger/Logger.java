package net.jeqo.bloons.logger;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.configuration.PluginConfiguration;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A utility class intended to log messages easily to the Bukkit console
 */
public class Logger {
    /**
     *                  Log a message to the console with STDOUT
     * @param message   The message to log, type java.lang.String
     */
    public static void logWithSTDOUT(String message) {
        System.out.println(message);
    }

    /**
     *                  Logs a message to the specified player
     * @param level     The logging level, type net.jeqo.bloons.logger.LoggingLevel
     * @param player    The player to log the message to, type org.bukkit.entity.Player
     * @param message   The message to log, type java.lang.String
     */
    public static void logToPlayer(LoggingLevel level, Player player, String message) {
        Component component = Component.text("[" + level.getName() + "] " + message).color(level.getColor());
        player.sendMessage(component);
    }

    /**
     *                  Logs a message to the specified player with the plugin prefix
     * @param player    The player to log the message to, type org.bukkit.entity.Player
     * @param message   The message to log, type java.lang.String
     */
    public static void logToPlayer(Player player, String message) {
        MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());
        Component component = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix") + message);
        player.sendMessage(component);
    }

    /**
     *                  Log a message to the console
     * @param level     The logging level, type net.jeqo.bloons.logger.LoggingLevel
     * @param message   The message to log, type java.lang.String
     */
    public static void log(LoggingLevel level, String message) {
        Component component = Component.text("[" + level.getName() + "] " + message).color(level.getColor());
        Bukkit.getServer().getConsoleSender().sendMessage(component);
    }

    /**
     *                  Logs a message to the console with the WARNING level
     * @param message   The message to log, type java.lang.String
     */
    public static void logWarning(String message) {
        log(LoggingLevel.WARNING, message);
    }

    /**
     *                  Logs a message to the console with the INFO level
     * @param message   The message to log, type java.lang.String
     */
    public static void logInfo(String message) {
        log(LoggingLevel.INFO, message);
    }

    /**
     *                  Logs a message to the console with the ERROR level
     * @param message   The message to log, type java.lang.String
     */
    public static void logError(String message) {
        log(LoggingLevel.ERROR, message);
    }

    /**
     *                  Logs a message to the console with the DEBUG level
     * @param message   The message to log, type java.lang.String
     */
    public static void logDebug(String message) {
        log(LoggingLevel.DEBUG, message);
    }

    /**
     * Logs an initialization message to the Bukkit console containing the plugin name
     */
    public static void logInitialStartup() {
        log(LoggingLevel.INFO, "Initializing " + PluginConfiguration.getName() + " plugin...");
    }

    /**
     * Logs a final startup message to the Bukkit console containing plugin information
     */
    public static void logFinalStartup() {
        // Calculate basic stats for final startup
        int totalConfigurationCount = Bloons.getBalloonCore().getSingleBalloonTypes().size();
        long totalConfigurationFileCount = ConfigConfiguration.getBalloonConfigurationCount();

        // Log info messages with the plugin information
        log(LoggingLevel.INFO, PluginConfiguration.getName() + " plugin has initialized!");
        log(LoggingLevel.INFO, "Version: " + PluginConfiguration.getVersion());
        log(LoggingLevel.INFO, "Developers: " + PluginConfiguration.DEVELOPER_CREDITS);
        log(LoggingLevel.INFO, "Website: " + PluginConfiguration.getURL());
        log(LoggingLevel.INFO, "Total Balloon Configurations: " + totalConfigurationCount);
        log(LoggingLevel.INFO, "Total Configuration Files: " + totalConfigurationFileCount);
    }

    /**
     * Logs an update notification to the Bukkit console
     */
    public static void logUpdateNotificationConsole() {
        logInfo("A new update is available for " + PluginConfiguration.getName() + " plugin");
        logInfo("You can find the update at: https://jeqo.net/bloons");
    }

    /**
     * Logs an update notification to a player
     */
    public static void logUpdateNotificationPlayer(Player player) {
        logToPlayer(player, "A new update is available for " + PluginConfiguration.getName() + " plugin");
        logToPlayer(player, "You can find the update at: https://jeqo.net/bloons");
    }

    /**
     * Logs an initial shutdown message to the Bukkit console
     */
    public static void logInitialShutdown() {
        log(LoggingLevel.INFO, "Shutting down " + PluginConfiguration.getName() + " plugin...");
    }

    /**
     * Logs a final shutdown message to the Bukkit console
     */
    public static void logFinalShutdown() {
        log(LoggingLevel.INFO, PluginConfiguration.getName() + " plugin has been shutdown gracefully");
    }
}
