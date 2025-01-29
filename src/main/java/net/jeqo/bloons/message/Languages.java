package net.jeqo.bloons.message;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.logger.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A class to manage the translations and retrieving of messages
 */
public class Languages {

    /**
     * Create a new set/hashmap to store the locales to ensure that they're unique
     */
    @Getter
    private static final Set<String> availableLanguages = new HashSet<>();
    /**
     * The file extension used for language files
     */
    private static final String LANGUAGE_FILE_EXTENSION = ".yml";

    static {
        // Initialize available languages
        String[] languages = { "en_US", "es_ES" };
        // Add all languages to the set
        Collections.addAll(getAvailableLanguages(), languages);
    }

    /**
     * Copies all language files over from the languages directory
     */
    public static void copyLanguageFiles() {
        for (String language : getAvailableLanguages()) {
            if (!Files.exists(getLanguageFilePath(language))) {
                Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + File.separator + language + LANGUAGE_FILE_EXTENSION, false);
            }
        }
    }

    /**
     *                  Gets the path to a language file
     * @param language  The language to get the path for, type java.lang.String
     * @return          The path to the language file, type java.nio.file.Path
     */
    private static Path getLanguageFilePath(String language) {
        return Paths.get(Bloons.getInstance().getDataFolder() + File.separator + ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + File.separator + language + LANGUAGE_FILE_EXTENSION);
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

                return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("messages." + message)));
            }
        }

        // Fallback onto en_US.yml if translation is missing
        FileConfiguration englishConfig = YamlConfiguration.loadConfiguration(new File(folder + File.separator + "en_US.yml"));
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(englishConfig.getString("messages." + message)));
    }
}