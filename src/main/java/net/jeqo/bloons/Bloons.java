package net.jeqo.bloons;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.commands.manager.CommandManager;
import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.data.UpdateChecker;
import net.jeqo.bloons.listeners.LeashHandlers;
import net.jeqo.bloons.listeners.MenuHandlers;
import net.jeqo.bloons.listeners.PlayerHandlers;
import net.jeqo.bloons.utils.Metrics;
import net.jeqo.bloons.utils.Utils;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Bloons extends JavaPlugin {

    public static HashMap<UUID, BalloonOwner> playerBalloons = new HashMap<>();
    public static HashMap<UUID, String> playerBalloonID = new HashMap<>();
    @Getter @Setter
    private static Bloons instance;

    @Override
    public void onEnable() {
        Utils.log("|---[ BLOONS ]-------------------------------------------------------|");
        Utils.log("|                           Plugin loaded.                           |");
        Utils.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        setInstance(this);

        new CommandManager(getInstance());
        loadListeners();

        new Metrics(this, pluginId);
        updateChecker();

        getConfig().options().copyDefaults(); saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Utils.log("|---[ BLOONS ]-------------------------------------------------------|");
        Utils.log("|                          Shutting down...                          |");
        Utils.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        for (BalloonOwner owner : playerBalloons.values()) {
            owner.cancel();
        }

        HandlerList.unregisterAll(this);
    }

    int pluginId = 16872;
    public void updateChecker() {
        new UpdateChecker(this, 106243).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                Utils.warn("|---[ BLOONS ]-------------------------------------------------------|");
                Utils.warn("|                  There is a new update available!                  |");
                Utils.warn("|                   https://jeqo.net/spigot/bloons                   |");
                Utils.warn("|-------------------------------------------------[ MADE BY JEQO ]---|");
            }
        });
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new LeashHandlers(), this);
        getServer().getPluginManager().registerEvents(new PlayerHandlers(), this);
        getServer().getPluginManager().registerEvents(new MenuHandlers(), this);
    }
    public static String getMessage(String id, String arg) {
        return Utils.hex(String.format(getInstance().getConfig().getString("messages." + id, ""), arg));
    }

    public static String getMessage(String id) {
        return Utils.hex(getInstance().getConfig().getString("messages." + id, ""));
    }

    public static String getString(String path) {
        return getInstance().getConfig().getString(path);
    }

    public static Integer getInt(String path) {
        return getInstance().getConfig().getInt(path);
    }
}