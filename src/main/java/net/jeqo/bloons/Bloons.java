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
import net.jeqo.bloons.utils.LanguageManagement;
import net.jeqo.bloons.utils.UpdateChecker;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Bloons extends JavaPlugin {
    @Getter @Setter
    private static Bloons instance;
    @Getter @Setter
    private static CommandCore commandCore;
    @Getter @Setter
    private static ListenerCore listenerCore;
    @Getter @Setter
    private static BalloonCore balloonCore;

    @Getter @Setter
    public static HashMap<UUID, SingleBalloon> playerSingleBalloons = new HashMap<>();
    @Getter @Setter
    public static HashMap<UUID, String> playerSingleBalloonID = new HashMap<>();

    @Getter
    public static final Map<UUID, MultipartBalloon> playerMultipartBalloons = new HashMap<>();

    @Override
    public void onEnable() {
        // Create an instance of the plugin
        setInstance(this);

        // Send initial startup message
        Logger.logInitialStartup();

        // Copy over language files
        LanguageManagement.copyLanguageFiles();

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
        int pluginId = 16872;
        new Metrics(this, pluginId);
        updateChecker();

        // Generate config(s) and set defaults
        getConfig().options().copyDefaults();
        saveDefaultConfig();

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

        if (getPlayerMultipartBalloons() != null) {
            // Unregister all multipart balloons
            for (MultipartBalloon owner : getPlayerMultipartBalloons().values()) {
                owner.destroy();
            }
        }

        // Clear all balloon data if it exists
        if (getPlayerSingleBalloons() != null) {
            getPlayerSingleBalloons().clear();
        }

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
        new UpdateChecker(this, 106243).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                Logger.logUpdateNotificationConsole();
            }
        });
    }
}