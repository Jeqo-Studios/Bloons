package net.jeqo.bloons.utils;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.logger.Logger;
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
public class LanguageManagement {

    /**
     * Create a new set/hashmap to store the locales to ensure that they're unique
     */
    private static final Set<String> availableLanguages = new HashSet<>();

    static {
        // Initialize available languages
        String[] languages = {
                "af_ZA", "ar_AE", "ar_BH", "ar_DZ", "ar_EG", "ar_IQ", "ar_JO", "ar_KW", "ar_LB", "ar_LY", "ar_MA",
                "ar_MO", "ar_QA", "ar_SA", "ar_SY", "ar_TN", "ar_YE", "az_AZ", "be_BY", "bg_BG", "bn_IN", "bs_BA",
                "ca_ES", "cs_CZ", "da_DK", "de_AT", "de_CH", "de_DE", "de_LI", "de_LU", "el_GR", "en_AU", "en_BZ",
                "en_CA", "en_GB", "en_IE", "en_JM", "en_NZ", "en_PH", "en_TT", "en_US", "en_VI", "en_ZA", "en_ZW",
                "es_US", "et_EE", "eu_ES", "fi_FL", "fo_FO", "fr_BE", "fr_CA", "fr_CH", "fr_FR", "fr_LU", "fr_MC",
                "gl_ES", "gu_IN", "he_IL", "hi_IN", "hr_HR", "hu_HU", "hy_AM", "id_ID", "is_IS", "it_CH", "it_IT",
                "ja_JP", "ka_GE", "kk_KZ", "kn_IN", "ko_KR", "kok_IN", "lt_LT", "lv_LV", "mk_MK", "ml_IN", "mr_IN",
                "ms_BN", "ms_MY", "mt_MT", "nl_BE", "nl_NL", "pt_PT", "ru_RU", "sq_AL", "zh_CN", "zh_MO", "zh_SG",
                "zh_TW"
        };
        // Add all languages to the set
        Collections.addAll(availableLanguages, languages);
    }

    /**
     * Copies all language files over from the languages directory
     */
    public static void copyLanguageFiles() {
        for (String language : availableLanguages) {
            if (!Files.exists(getLanguageFilePath(language))) {
                Bloons.getInstance().saveResource(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + File.separator + language + ".yml", false);
            }
        }
    }

    /**
     *                  Gets the path to a language file
     * @param language  The language to get the path for, type java.lang.String
     * @return          The path to the language file, type java.nio.file.Path
     */
    private static Path getLanguageFilePath(String language) {
        return Paths.get(Bloons.getInstance().getDataFolder() + File.separator + ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + File.separator + language + ".yml");
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