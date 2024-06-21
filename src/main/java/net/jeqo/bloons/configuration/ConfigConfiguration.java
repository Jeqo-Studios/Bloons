package net.jeqo.bloons.configuration;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.LanguageManagement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * A class that contains configurations for the plugin configuration file
 */
public class ConfigConfiguration {
    // The folder that stores the balloons to be loaded
    public static final String BALLOON_CONFIGURATION_FOLDER = "balloons";
    // The folder that stores the languages to be loaded
    public static final String LANGUAGES_CONFIGURATION_FOLDER = "languages";

    /**
     *          Gets the number of configuration files currently in the balloon configuration folder
     * @return  The number of configuration files in the balloon configuration folder. Returns 0 upon none found. type long
     */
    public static long getBalloonConfigurationCount() {
        try {
            // Walk through the files in the balloons folder and count them/return the count
            Path path = Path.of(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER);
            return Files.walk(path).filter(Files::isRegularFile).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Upon an IO exception, return 0
        return 0;
    }

    /**
     *         Gets all the single balloon types from the configuration files
     * @return The single balloon types from the configuration files,
     *         returns an empty array list if no single balloons are found, type java.util.ArrayList<net.jeqo.bloons.balloon.single.SingleBalloonType>
     */
    public static ArrayList<SingleBalloonType> getSingleBalloons() {
        // Start an array of single balloon types that's empty
        ArrayList<SingleBalloonType> singleBalloons = new ArrayList<>();
        File folder = new File(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning(String.format(LanguageManagement.getMessage("configuration-folder-not-found"), folder.getPath()));
            return singleBalloons;
        }

        // List files in the folder
        File[] files = folder.listFiles();
        if (files == null) {
            Logger.logWarning(String.format(LanguageManagement.getMessage("no-configuration-files-found"), folder.getPath()));
            return singleBalloons;
        }

        // Loop through each file in the balloons directory
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();

                // Load the configuration file
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                // Get the configuration section
                ConfigurationSection section = config.getConfigurationSection("");
                if (section == null) {
                    Logger.logWarning(String.format(LanguageManagement.getMessage("configuration-section-not-found"), folder.getPath()));
                    continue;
                }

                // Process each key in the section
                for (String key : section.getKeys(false)) {

                    // Determine the type of balloon
                    String type = config.getString(key + ".type", BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER);

                    // If there is no type configuration, log an error and continue through the other iterations
                    if (type.isBlank()) {
                        Logger.logError(String.format(LanguageManagement.getMessage("balloon-type-not-found"), key, fileName));
                        continue;
                    }

                    if (!type.equals(BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER)) continue;

                    try {
                        // Add the single balloon type to the array list
                        singleBalloons.add(new SingleBalloonType(
                                key,
                                config.getString(key + ".id"),
                                config.getString(key + ".permission"),
                                config.getString(key + ".material"),
                                config.getString(key + ".color"),
                                config.getInt(key + ".custom-model-data"),
                                config.getString(key + ".name"),
                                config.getStringList(key + ".lore").toArray(new String[0])
                        ));
                    } catch (Exception e) {
                        Logger.logWarning(String.format(LanguageManagement.getMessage("balloon-process-error"), key, fileName, e.getMessage()));
                    }
                }
            }
        }

        return singleBalloons;
    }
}
