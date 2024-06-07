package net.jeqo.bloons;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.commands.manager.CommandCore;
import net.jeqo.bloons.utils.UpdateChecker;
import net.jeqo.bloons.listeners.LeashHandlers;
import net.jeqo.bloons.listeners.ListenerCore;
import net.jeqo.bloons.listeners.MenuHandlers;
import net.jeqo.bloons.listeners.PlayerHandlers;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public final class Bloons extends JavaPlugin {
    @Getter @Setter
    private static Bloons instance;
    @Getter @Setter
    private static CommandCore commandCore;
    @Getter @Setter
    private static ListenerCore listenerCore;

    @Getter @Setter
    public static HashMap<UUID, SingleBalloon> playerBalloons = new HashMap<>();
    @Getter @Setter
    public static HashMap<UUID, String> playerBalloonID = new HashMap<>();

    @Override
    public void onEnable() {
        // Create an instance of the plugin
        setInstance(this);

        // Send startup message
        Logger.logStartup();

        // Register core managers within the plugin
        setCommandCore(new CommandCore(getInstance()));
        setListenerCore(new ListenerCore(getInstance()));

        // Stage listeners
        getListenerCore().stageListener(new PlayerHandlers());
        getListenerCore().stageListener(new LeashHandlers());
        getListenerCore().stageListener(new MenuHandlers());

        // Register all handlers
        getListenerCore().registerListeners();

        // Startup the metrics and update checker
        int pluginId = 16872;
        new Metrics(this, pluginId);
        updateChecker();

        // Generate config(s) and set defaults
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Log shutdown message
        Logger.logShutdown();

        // Unregister all balloons and stop the task
        for (SingleBalloon owner : playerBalloons.values()) {
            owner.cancel();
        }

        // Unregister all listeners in the manager
        getListenerCore().unregisterListeners();
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