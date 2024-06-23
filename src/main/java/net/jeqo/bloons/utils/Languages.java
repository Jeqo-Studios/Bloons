package net.jeqo.bloons.utils;

import lombok.Getter;
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
        String[] languages = {
                "af_ZA", "ar_SA", "az_AZ", "be_BY", "bg_BG", "bn_IN", "bs_BA", "cs_CZ", "cy_GB", "da_DK", "de_DE", "el_GR",
                "en_US", "es_ES", "et_EE", "eu_ES", "fa_IR", "fo_FO", "fi_FI", "fr_FR", "gl_ES", "gu_IN", "he_IL", "hi_IN",
                "hr_HR", "hu_HU", "hy_AM", "id_ID", "is_IS", "it_IT", "ja_JP", "ka_GE", "kk_KZ", "kn_IN", "ko_KR", "kok_IN",
                "lt_LT", "lv_LV", "mk_MK", "ml_IN", "mn_MN", "mr_IN", "ms_MY", "mt_MT", "nb_NO", "nl_NL", "nn_NO", "pa_IN",
                "pl_PL", "pt_BR", "ro_RO", "ru_RU", "se_NO", "sk_SK", "sq_AL", "sr_BA", "sv_SE", "sw_KE", "syr_SY", "ta_IN",
                "te_IN", "th_TH", "tn_ZA", "tr_TR", "uk_UA", "uz_UZ", "vi_VN", "xh_ZA", "zh_CN", "zh_TW"
        };
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

                return config.getString("messages." + message);
            }
        }

        return "";
    }
}