package net.jeqo.bloons.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to convert messages with hex codes, and hex strings to Bukkit colors
 */
public class ColorManagement {

    /**
     *                  Converts a message with hex codes to a Bukkit color
     * @param message   The message to convert to Bukkit color from hex, type java.lang.String
     * @return          The Bukkit color, type org.bukkit.Color
     */
    public static String fromHex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }
            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     *                  Checks if a string is a valid hex code
     * @param string    The string to check the validity of, type java.lang.String
     */
    public static boolean isHexCode(String string) {
        return string.matches("#[a-fA-F0-9]{6}");
    }

    /**
     *                  Converts a hex string to a Bukkit color
     * @param string    The hex string to convert, type java.lang.String
     * @return          The Bukkit color, type org.bukkit.Color
     */
    public static Color hexToColor(String string) {
        return Color.fromRGB(Integer.parseInt(string.substring(1), 16));
    }
}