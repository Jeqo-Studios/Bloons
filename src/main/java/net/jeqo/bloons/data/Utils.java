package net.jeqo.bloons.data;
import net.jeqo.bloons.Bloons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static void checkBalloonRemovalOrAdd(final Player player, final String balloonId) {
        (new BukkitRunnable()
        {
            public void run() {
                BalloonOwner owner = (BalloonOwner) Bloons.playerBalloons.get(player.getUniqueId());
                if (owner != null) {
                    return;
                }
                Utils.removeBalloon(player, owner);
                BalloonOwner balloonOwner = new BalloonOwner(player, balloonId);
                balloonOwner.runTaskTimer((Plugin) Bloons.getInstance(), 0L, 1L);
                Bloons.playerBalloons.put(player.getUniqueId(), balloonOwner);
            }
        }).runTaskLater((Plugin) Bloons.getInstance(), 1L);
    }

    public static void removeBalloon(Player player, BalloonOwner owner) {
        if (owner != null) {
            owner.spawnRemoveParticle();
            owner.cancel();
            Bloons.playerBalloons.remove(player.getUniqueId());
        }
    }

    public static String hex(String message) { Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}"); Matcher matcher = pattern.matcher(message); while (matcher.find()) { String hexCode = message.substring(matcher.start(), matcher.end()); String replaceSharp = hexCode.replace('#', 'x'); char[] ch = replaceSharp.toCharArray(); StringBuilder builder = new StringBuilder(""); for (char c : ch) { builder.append("&" + c);} message = message.replace(hexCode, builder.toString()); matcher = pattern.matcher(message);} return ChatColor.translateAlternateColorCodes('&', message); }

    public static void log(@NotNull String text) {
        Bukkit.getLogger().log(Level.INFO, text);
    }

    public static void warn(@NotNull String text) {
        Bukkit.getLogger().log(Level.WARNING, text);
    }
}