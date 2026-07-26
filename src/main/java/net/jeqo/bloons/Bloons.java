package net.jeqo.bloons;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.BalloonCore;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.CommandCore;
import net.jeqo.bloons.configuration.ConfigurationManager;
import net.jeqo.bloons.configuration.PluginConfiguration;
import net.jeqo.bloons.listeners.*;
import net.jeqo.bloons.listeners.multipart.MultipartBalloonPlayerJoinListener;
import net.jeqo.bloons.listeners.multipart.MultipartBalloonPlayerLeaveListener;
import net.jeqo.bloons.listeners.multipart.MultipartBalloonPlayerListener;
import net.jeqo.bloons.listeners.single.SingleBalloonPlayerJoinListener;
import net.jeqo.bloons.listeners.single.SingleBalloonPlayerLeaveListener;
import net.jeqo.bloons.listeners.single.SingleBalloonPlayerListener;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.health.UpdateChecker;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.health.Metrics;
import net.jeqo.bloons.utils.VersionChecker;
import org.bukkit.event.Listener;
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
    @Getter @Setter
    private static ConfigurationManager configurationManager;

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
        setInstance(this);
        setConfigurationManager(new ConfigurationManager(this));
        Logger.logInitialStartup();
        getConfigurationManager().initialize(Languages.getAvailableLanguages());
        initializeManagers();
        registerListeners();
        startRuntimeServices();
        initializeBalloonData();
        Logger.logFinalStartup();
    }

    @Override
    public void onDisable() {
        Logger.logInitialShutdown();
        shutdownSingleBalloons();
        shutdownMultipartBalloons();
        clearBalloonCaches();
        getListenerCore().unregisterListeners();
        Logger.logFinalShutdown();
    }

    /**
     * Checks for updates and notifies the user via a log to console
     * getDescription() is still used because of the usage of a plugin.yml.
     * Not planned to change
     */
    public void updateChecker() {
        int resourceId = 106243;
        new UpdateChecker(this, resourceId).getVersion(version -> {
            String currentVersion = this.getDescription().getVersion();

            if (VersionChecker.isVersionLower(currentVersion, version)) {
                Logger.logUpdateNotificationConsole();
            } else if (VersionChecker.isVersionHigher(currentVersion, version)) {
                Logger.logUnreleasedVersionNotification();
            }
        });
    }

    private void initializeManagers() {
        setCommandCore(new CommandCore(getInstance()));
        setListenerCore(new ListenerCore(getInstance()));
        setBalloonCore(new BalloonCore(getInstance()));
    }

    private void registerListeners() {
        Listener[] listeners = {
                new BalloonChickenLeashListener(),
                new BalloonMenuListener(),
                new BalloonChickenEntityListener(),
                new BalloonArmorStandEntityListener(),
                new SingleBalloonPlayerListener(),
                new SingleBalloonPlayerJoinListener(),
                new SingleBalloonPlayerLeaveListener(),
                new MultipartBalloonPlayerListener(),
                new MultipartBalloonPlayerJoinListener(),
                new MultipartBalloonPlayerLeaveListener()
        };

        for (Listener listener : listeners) {
            getListenerCore().stageListener(listener);
        }

        getListenerCore().registerListeners();
    }

    private void startRuntimeServices() {
        new Metrics(this, PluginConfiguration.BSTATS_PLUGIN_ID);
        updateChecker();
    }

    private void initializeBalloonData() {
        getBalloonCore().copyExampleBalloons();
        getConfigurationManager().reload();
        getBalloonCore().initialize();
    }

    private void shutdownSingleBalloons() {
        if (getPlayerSingleBalloons() == null || getPlayerSingleBalloons().isEmpty()) {
            return;
        }

        for (SingleBalloon owner : getPlayerSingleBalloons().values()) {
            owner.cancel();
        }
    }

    private void shutdownMultipartBalloons() {
        if (getPlayerMultipartBalloons().isEmpty()) {
            return;
        }

        for (MultipartBalloon owner : getPlayerMultipartBalloons().values()) {
            owner.destroy();
        }
    }

    private void clearBalloonCaches() {
        if (getPlayerSingleBalloons() != null) {
            getPlayerSingleBalloons().clear();
        }
        getPlayerMultipartBalloons().clear();
    }
}
