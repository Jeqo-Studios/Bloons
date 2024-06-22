package net.jeqo.bloons.utils;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.logger.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

/**
 * A class to manage the translations and retrieving of messages
 */
public class LanguageManagement {

    /**
     * Copies all language files over from the languages directory
     */
    public static void copyLanguageFiles() {
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/en_US.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/es_US.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/fr_FR.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/ja_JP.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/ko_KR.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/pt_PT.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/ru_RU.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/zh_CN.yml", false);
        Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + "/de_DE.yml", false);
    }

    /**
     *                  Gets a message from the language file specified in the config
     * @param message   The message to get from the language file, type java.lang.String
     * @return          The message from the language file, type java.lang.String/null
     */
    public static String getMessage(String message) {
        File folder = new File(Bloons.getInstance().getDataFolder() + File.separator + ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning("Language folder not found: " + folder.getPath());
            return "";
        }

        // List files in the folder, if none it will return an empty string
        File[] files = folder.listFiles();
        if (files == null) {
            Logger.logWarning("No language files in folder folder: " + folder.getPath());
            return "";
        }

        // Loop through every language file to find the one specified in the config
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();

                if (!fileName.startsWith(Objects.requireNonNull(Bloons.getInstance().getConfig().getString("language")))) continue;

                // Load the configuration file
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                return config.getString("messages." + message);
            }
        }

        return "";
    }
}