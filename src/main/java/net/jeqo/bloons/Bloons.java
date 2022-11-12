package net.jeqo.bloons;

import net.jeqo.bloons.data.BalloonCommand;
import net.jeqo.bloons.data.BalloonRunner;
import net.jeqo.bloons.listeners.PlayerLeave;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Bloons extends JavaPlugin {

    public static HashMap<UUID, BalloonRunner> playerBalloons = new HashMap<>();
    private static Bloons instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents((Listener)new PlayerLeave(), (Plugin)this);

        BalloonCommand command = new BalloonCommand();
        Objects.requireNonNull(getCommand("balloon")).setExecutor((CommandExecutor)command);
        Objects.requireNonNull(getCommand("balloon")).setTabCompleter((TabCompleter)command);
    }

    @Override
    public void onDisable() {
        for (BalloonRunner runner : playerBalloons.values()) {
            runner.cancel();
        }

        HandlerList.unregisterAll((Plugin)this);
    }

    public static String getMessage(String id, String arg) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getInstance().getConfig().getString("messages." + id, ""), arg));
    }

    public static String getMessage(String id) {
        return ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("messages." + id, ""));
    }

    public static Bloons getInstance() {
        return instance;
    }
}