package net.jeqo.bloons;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.BalloonCore;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.CommandCore;
import net.jeqo.bloons.listeners.*;
import net.jeqo.bloons.listeners.multipart.MultipartBalloonPlayerJoinListener;
import net.jeqo.bloons.listeners.multipart.MultipartBalloonPlayerLeaveListener;
import net.jeqo.bloons.listeners.single.SingleBalloonPlayerListener;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.health.UpdateChecker;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.health.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The main class of the plugin that houses the core managers and the plugin instance
 */
public final class Bloons extends JavaPlugin {
    @Getter @Setter
    private static Bloons instance;
    @Getter @Setter
    private static CommandCore commandCore;
    @Getter @Setter
    private static ListenerCore listenerCore;
    @Getter @Setter
    private static BalloonCore balloonCore;

    /**
     * A map of all players with a single balloon
     */
    @Getter @Setter
    public static HashMap<UUID, SingleBalloon> playerSingleBalloons = new HashMap<>();
    /**
     * A map of all players with a single balloon and its ID
     */
    @Getter @Setter
    public static HashMap<UUID, String> playerSingleBalloonID = new HashMap<>();

    /**
     * A map of all players with a multipart balloon
     */
    @Getter
    public static final Map<UUID, MultipartBalloon> playerMultipartBalloons = new HashMap<>();

    @Override
    public void onEnable() {
        // Create an instance of the plugin
        setInstance(this);

        // Send initial startup message
        Logger.logInitialStartup();

        /*
         * KEEP CONFIGURATION INITIALIZATION AT THE TOP TO PREVENT
         * INITIAL LOADING OF THE PLUGIN AND ITS ISSUES
         */

        // Copy over language files
        Languages.copyLanguageFiles();

        // Generate config(s) and set defaults
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Register core managers within the plugin
        setCommandCore(new CommandCore(getInstance()));
        setListenerCore(new ListenerCore(getInstance()));
        setBalloonCore(new BalloonCore(getInstance()));

        // Stage listeners
        getListenerCore().stageListener(new SingleBalloonPlayerListener());
        getListenerCore().stageListener(new BalloonUnleashListener());
        getListenerCore().stageListener(new BalloonMenuListener());
        getListenerCore().stageListener(new BalloonEntityListener());

        getListenerCore().stageListener(new MultipartBalloonPlayerJoinListener());
        getListenerCore().stageListener(new MultipartBalloonPlayerLeaveListener());

        // Register all handlers
        getListenerCore().registerListeners();

        // Startup the metrics and update checker
        // This is the ID of the plugin on bStats, this should be kept as a constant
        int pluginId = 16872;
        new Metrics(this, pluginId);
        updateChecker();

        // Copy over example balloons folder
        getBalloonCore().copyExampleBalloons();

        // Initialize multipart balloons
        getBalloonCore().initialize();

        // Send final startup message
        Logger.logFinalStartup();
    }

    @Override
    public void onDisable() {
        // Log an initial shutdown message
        Logger.logInitialShutdown();

        if (getPlayerSingleBalloons() != null) {
            // Unregister all balloons and stop the task
            for (SingleBalloon owner : getPlayerSingleBalloons().values()) {
                owner.cancel();
            }
        }

        // Unregister all balloons and stop the task if it exists
        if (getPlayerMultipartBalloons() != null) {
            for (MultipartBalloon owner : getPlayerMultipartBalloons().values()) {
                owner.destroy();
            }
        }

        // Clear all balloon data if it exists
        if (getPlayerSingleBalloons() != null) getPlayerSingleBalloons().clear();

        // Unregister all listeners in the manager
        getListenerCore().unregisterListeners();

        // Send final shutdown message
        Logger.logFinalShutdown();
    }

    /**
     * Checks for updates and notifies the user via a log to console
     * getDescription() is still used because of the usage of a plugin.yml.
     * Not planned to change
     */
    public void updateChecker() {
        // Resource ID for the plugin on SpigotMC
        int resourceId = 106243;
        new UpdateChecker(this, resourceId).getVersion(version -> {
            String currentVersion = this.getDescription().getVersion();

            if (isVersionLower(currentVersion, version)) {
                Logger.logUpdateNotificationConsole();
            } else if (isVersionHigher(currentVersion, version)) {
                Logger.logUnreleasedVersionNotification();
            }
        });
    }

    public boolean isVersionLower(String current, String latest) {
        return compareVersions(current, latest) < 0;
    }

    public boolean isVersionHigher(String current, String latest) {
        return compareVersions(current, latest) > 0;
    }

    /**
     * Compares two version strings (e.g., "1.2.3" vs. "1.2.4").
     * Returns:
     *  - A negative value if v1 < v2
     *  - Zero if v1 == v2
     *  - A positive value if v1 > v2
     */
    public int compareVersions(String v1, String v2) {
        String[] v1Parts = v1.split("\\.");
        String[] v2Parts = v2.split("\\.");

        int length = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            int part1 = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int part2 = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            if (part1 != part2) {
                return Integer.compare(part1, part2);
            }
        }
        return 0;
    }
}