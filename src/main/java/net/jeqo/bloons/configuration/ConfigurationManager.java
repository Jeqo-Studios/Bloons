package net.jeqo.bloons.configuration;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.logger.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Centralizes plugin configuration lifecycle and filesystem access.
 */
@Getter
public class ConfigurationManager {
    private static final String LANGUAGE_FILE_EXTENSION = ".yml";

    private final Bloons plugin;
    private final Path dataFolderPath;
    private final Path balloonFolderPath;
    private final Path languageFolderPath;
    private List<File> balloonConfigurationFiles = new ArrayList<>();
    private FileConfiguration activeLanguage = new YamlConfiguration();
    private String activeLanguageCode = "en_US";

    /**
     *                  Creates a configuration manager for the plugin runtime
     * @param plugin    The owning plugin instance, type net.jeqo.bloons.Bloons
     */
    public ConfigurationManager(Bloons plugin) {
        this.plugin = plugin;
        this.dataFolderPath = plugin.getDataFolder().toPath();
        this.balloonFolderPath = this.dataFolderPath.resolve(ConfigConfiguration.BALLOON_CONFIGURATION_FOLDER);
        this.languageFolderPath = this.dataFolderPath.resolve(ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER);
    }

    /**
     *                              Prepares directories, config defaults, and bundled resource files
     * @param bundledLanguages      The language files that should exist in the data folder, type java.util.Collection[java.lang.String]
     */
    public void initialize(Collection<String> bundledLanguages) {
        ensureDirectory(this.dataFolderPath);
        ensureDirectory(this.balloonFolderPath);
        ensureDirectory(this.languageFolderPath);

        this.plugin.getConfig().options().copyDefaults(true);
        this.plugin.saveDefaultConfig();

        copyLanguageFiles(bundledLanguages);
        reload();
    }

    /**
     * Reloads the plugin config and refreshes cached file-backed runtime state.
     */
    public void reload() {
        this.plugin.reloadConfig();
        this.plugin.getConfig().options().copyDefaults(true);
        refreshBalloonConfigurationFiles();
        refreshActiveLanguage();
    }

    /**
     *                              Ensures each bundled language file exists in the plugin data folder
     * @param bundledLanguages      The bundled language identifiers to copy if missing, type java.util.Collection[java.lang.String]
     */
    public void copyLanguageFiles(Collection<String> bundledLanguages) {
        for (String language : bundledLanguages) {
            saveBundledResourceIfMissing(
                    ConfigConfiguration.LANGUAGES_CONFIGURATION_FOLDER + File.separator + language + LANGUAGE_FILE_EXTENSION
            );
        }
    }

    /**
     *                              Saves a bundled resource when the target file does not already exist
     * @param resourcePath          The relative resource path inside the jar, type java.lang.String
     */
    public void saveBundledResourceIfMissing(String resourcePath) {
        Path targetPath = this.dataFolderPath.resolve(resourcePath);
        if (Files.exists(targetPath)) {
            return;
        }

        ensureDirectory(Objects.requireNonNull(targetPath.getParent()));
        this.plugin.saveResource(resourcePath, false);
    }

    /**
     *          Gets the number of balloon configuration files currently discovered on disk
     * @return  The number of cached balloon configuration files, type long
     */
    public long getBalloonConfigurationCount() {
        return this.balloonConfigurationFiles.size();
    }

    /**
     *          Gets an immutable snapshot of the currently discovered balloon configuration files
     * @return  The balloon configuration files, type java.util.List[java.io.File]
     */
    public List<File> getBalloonConfigurationFiles() {
        return List.copyOf(this.balloonConfigurationFiles);
    }

    /**
     *                      Gets a translated message from the active language with English fallback
     * @param path        The message key beneath the `messages` section, type java.lang.String
     * @return            The translated and colorized message, type java.lang.String
     */
    public String getMessage(String path) {
        String translated = this.activeLanguage.getString("messages." + path);
        if (translated == null && !"en_US".equals(this.activeLanguageCode)) {
            FileConfiguration fallback = loadYaml(this.languageFolderPath.resolve("en_US.yml"));
            translated = fallback.getString("messages." + path);
        }

        if (translated == null) {
            Logger.logWarning("Missing translation for message key: " + path);
            return "";
        }

        return ChatColor.translateAlternateColorCodes('&', translated);
    }

    /**
     *                      Gets a colorized string from the main plugin config
     * @param path        The config path to read, type java.lang.String
     * @return            The configured string or an empty string when missing, type java.lang.String
     */
    public String getConfigString(String path) {
        String value = this.plugin.getConfig().getString(path);
        if (value == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes('&', value);
    }

    /**
     *                      Gets an integer value from the main plugin config
     * @param path        The config path to read, type java.lang.String
     * @return            The configured integer value, type int
     */
    public int getConfigInt(String path) {
        return this.plugin.getConfig().getInt(path);
    }

    /**
     *                      Gets a string list from the main plugin config
     * @param path        The config path to read, type java.lang.String
     * @return            The configured string list, type java.util.List[java.lang.String]
     */
    public List<String> getConfigStringList(String path) {
        return this.plugin.getConfig().getStringList(path);
    }

    /**
     * Refreshes the cached set of balloon configuration files from the data folder.
     */
    private void refreshBalloonConfigurationFiles() {
        File folder = this.balloonFolderPath.toFile();

        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning("Balloon configuration folder not found: " + folder.getPath());
            this.balloonConfigurationFiles = new ArrayList<>();
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (files == null) {
            Logger.logWarning("No balloon configuration files found in folder: " + folder.getPath());
            this.balloonConfigurationFiles = new ArrayList<>();
            return;
        }

        this.balloonConfigurationFiles = new ArrayList<>(List.of(files));
    }

    /**
     * Reloads the active language file selected by the current plugin config.
     */
    private void refreshActiveLanguage() {
        this.activeLanguageCode = this.plugin.getConfig().getString("language", "en_US");
        Path languageFile = this.languageFolderPath.resolve(this.activeLanguageCode + LANGUAGE_FILE_EXTENSION);

        if (!Files.exists(languageFile)) {
            Logger.logWarning("Language file not found for " + this.activeLanguageCode + ", falling back to en_US");
            this.activeLanguageCode = "en_US";
            languageFile = this.languageFolderPath.resolve("en_US.yml");
        }

        this.activeLanguage = loadYaml(languageFile);
    }

    /**
     *                      Loads a YAML configuration file from disk
     * @param path        The file path to read, type java.nio.file.Path
     * @return            The loaded YAML configuration, type org.bukkit.configuration.file.FileConfiguration
     */
    private FileConfiguration loadYaml(Path path) {
        return YamlConfiguration.loadConfiguration(path.toFile());
    }

    /**
     *                      Creates a directory tree if it does not already exist
     * @param path        The directory path to create, type java.nio.file.Path
     */
    private void ensureDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to create configuration directory: " + path, exception);
        }
    }
}
