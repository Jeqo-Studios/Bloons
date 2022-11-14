package net.jeqo.bloons.data;

import net.jeqo.bloons.Bloons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void removeBalloon(Player player, BalloonOwner owner) {
        if (owner != null) {
            owner.spawnRemoveParticle();
            owner.cancel();
            Bloons.playerBalloons.remove(player.getUniqueId());
        }
    }

    public static void quickRemoveBalloon(Player player, BalloonOwner owner) {
        if (owner != null) {
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

    public static Color hexToColor(String string) {
        return Color.fromRGB(Integer.parseInt(string.substring(1), 16));
    }
}