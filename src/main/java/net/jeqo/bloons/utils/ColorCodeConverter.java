package net.jeqo.bloons.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A class used to convert color codes between Adventure and Minecraft
 */
public class ColorCodeConverter {

    private static final Map<String, String> colorCodes = new HashMap<>();

    static {
        colorCodes.put("<black>", "§0");
        colorCodes.put("<dark_blue>", "§1");
        colorCodes.put("<dark_green>", "§2");
        colorCodes.put("<dark_aqua>", "§3");
        colorCodes.put("<dark_red>", "§4");
        colorCodes.put("<dark_purple>", "§5");
        colorCodes.put("<gold>", "§6");
        colorCodes.put("<gray>", "§7");
        colorCodes.put("<dark_gray>", "§8");
        colorCodes.put("<blue>", "§9");
        colorCodes.put("<green>", "§a");
        colorCodes.put("<aqua>", "§b");
        colorCodes.put("<red>", "§c");
        colorCodes.put("<light_purple>", "§d");
        colorCodes.put("<yellow>", "§e");
        colorCodes.put("<white>", "§f");
    }

    /**
     * Converts all basic adventure color codes to Minecraft color codes
     * @param message The message to convert
     * @return The converted message
     */
    public static String adventureToColorCode(String message) {
        for (Map.Entry<String, String> entry : colorCodes.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

    /**
     * Converts all Minecraft color codes to Adventure color codes
     * @param message The message to convert
     * @return The converted message
     */
    public static String colorCodeToAdventure(String message) {
        for (Map.Entry<String, String> entry : colorCodes.entrySet()) {
            message = message.replace(entry.getValue(), entry.getKey());
        }
        return message;
    }
}

