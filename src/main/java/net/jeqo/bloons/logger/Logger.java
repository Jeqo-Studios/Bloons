package net.jeqo.bloons.logger;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.configuration.PluginConfiguration;
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
        String formattedMessage = String.format("%s[%s] %s", level.getColor(), level.getName(), message);
        player.sendMessage(formattedMessage);
    }

    /**
     *                  Logs a message to the specified player with the plugin prefix
     * @param player    The player to log the message to, type org.bukkit.entity.Player
     * @param message   The message to log, type java.lang.String
     */
    public static void logToPlayer(Player player, String message) {
        player.sendMessage(message);
    }

    /**
     *                  Log a message to the console
     * @param level     The logging level, type net.jeqo.bloons.logger.LoggingLevel
     * @param message   The message to log, type java.lang.String
     */
    public static void log(LoggingLevel level, String message) {
        String loggedMessage = String.format("%s[%s] %s", level.getColor(), level.getName(), message);
        Bukkit.getServer().getConsoleSender().sendMessage(loggedMessage);
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
        int totalConfigurationCount = Bloons.getBalloonCore().getMultipartBalloonTypes().size() + Bloons.getBalloonCore().getSingleBalloonTypes().size();
        long totalConfigurationFileCount = ConfigConfiguration.getBalloonConfigurationCount();

        // Log info messages with the plugin information
        log(LoggingLevel.INFO, PluginConfiguration.getName() + " plugin has initialized!");
        log(LoggingLevel.INFO, "Version: " + PluginConfiguration.getVersion());
        log(LoggingLevel.INFO, "Developers: " + PluginConfiguration.DEVELOPER_CREDITS);
        log(LoggingLevel.INFO, "Website: " + PluginConfiguration.getURL());
        log(LoggingLevel.INFO, "Total Balloon Configurations: " + totalConfigurationCount);
        log(LoggingLevel.INFO, "Total Configuration Files: " + totalConfigurationFileCount);
        log(LoggingLevel.INFO, "Single Balloons Loaded: " + Bloons.getBalloonCore().getSingleBalloonTypes().size());
        log(LoggingLevel.INFO, "Multipart Balloons Loaded: " + Bloons.getBalloonCore().getMultipartBalloonTypes().size());
    }

    /**
     * Logs an update notification to the Bukkit console
     */
    public static void logUpdateNotificationConsole() {
        logInfo("A new update is available for " + PluginConfiguration.getName() + " plugin");
        logInfo("You can find the update at: https://jeqo.net/bloons");
    }

    /**
     * Logs an unreleased plugin version notification to the Bukkit console
     */
    public static void logUnreleasedVersionNotification() {
        logInfo("The version for the " + PluginConfiguration.getName() + " plugin is higher than latest version");
        logInfo("You are currently running an unreleased version of the plugin that is NOT stable");
    }

    /**
     *                  Logs an update notification to a player
     * @param player    The player to log the update notification to, type org.bukkit.entity.Player
     */
    public static void logUpdateNotificationPlayer(Player player) {
        logToPlayer(LoggingLevel.INFO, player, "A new update is available for " + PluginConfiguration.getName() + " plugin");
        logToPlayer(LoggingLevel.INFO, player, "You can find the update at: https://jeqo.net/bloons");
    }

    /**
     *                  Logs an unreleased version notification to a player
     * @param player    The player to log the unreleased version notification to, type org.bukkit.entity.Player
     */
    public static void logUnreleasedVersionNotificationPlayer(Player player) {
        logToPlayer(LoggingLevel.INFO, player, "The version for the " + PluginConfiguration.getName() + " plugin is higher than latest version");
        logToPlayer(LoggingLevel.INFO, player, "You are currently running an unreleased version of the plugin that is NOT stable");
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
