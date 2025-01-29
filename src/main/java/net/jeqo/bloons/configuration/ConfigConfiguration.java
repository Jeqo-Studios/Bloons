package net.jeqo.bloons.configuration;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.model.BalloonSegmentType;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonModel;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.message.Languages;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A class that contains configurations for the plugin configuration file
 */
public class ConfigConfiguration {
    /**
     * The folder that stores the balloons to be loaded
     */
    public static final String BALLOON_CONFIGURATION_FOLDER = "balloons";
    /**
     * The folder that stores the languages to be loaded
     */
    public static final String LANGUAGES_CONFIGURATION_FOLDER = "languages";

    @Getter
    private static final String balloonConfigurationFolder = Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER;

    /**
     *          Gets the number of configuration files currently in the balloon configuration folder
     * @return  The number of configuration files in the balloon configuration folder. Returns 0 upon none found. type long
     */
    public static long getBalloonConfigurationCount() {
        try {
            Path path = Path.of(getBalloonConfigurationFolder());
            try (Stream<Path> paths = Files.walk(path)) {
                return paths.filter(Files::isRegularFile).count();
            }
        } catch (Exception e) {
            Logger.logError(String.format("An error occurred while counting balloon configuration files: %s", e.getMessage()));
            return 0;
        }
    }

    /**
     *         Gets all the single balloon types from the configuration files
     * @return The single balloon types from the configuration files,
     *         returns an empty array list if no single balloons are found, type java.util.ArrayList[net.jeqo.bloons.balloon.single.SingleBalloonType]
     */
    public static ArrayList<SingleBalloonType> getSingleBalloons() {
        // Start an array of single balloon types that's empty
        ArrayList<SingleBalloonType> singleBalloons = new ArrayList<>();
        File folder = new File(getBalloonConfigurationFolder());

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning(String.format(Languages.getMessage("configuration-folder-not-found"), folder.getPath()));
            return singleBalloons;
        }

        // List files in the folder
        File[] files = folder.listFiles();
        if (files == null) {
            Logger.logWarning(String.format(Languages.getMessage("no-configuration-files-found"), folder.getPath()));
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
                    Logger.logWarning(String.format(Languages.getMessage("configuration-section-not-found"), folder.getPath()));
                    continue;
                }

                // Process each key in the section
                for (String key : section.getKeys(false)) {

                    // Determine the type of balloon
                    String type = config.getString(key + ".type", BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER);

                    if (type.isBlank()) {
                        Logger.logError(String.format(Languages.getMessage("balloon-type-not-found"), key, fileName));
                        continue;
                    }

                    if (!type.equals(BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER)) continue;

                    if (config.getString(key + ".meg-model-id") != null) {
                        // Process the MEG balloon type
                        try {
                            singleBalloons.add(new SingleBalloonType(
                                    key,
                                    config.getString(key + ".id"),
                                    config.getString(key + ".permission"),
                                    config.getString(key + ".icon.material"),
                                    config.getInt(key + ".icon.custom-model-data"),
                                    config.getString(key + ".meg-model-id"),
                                    config.getString(key + ".icon.name"),
                                    config.getStringList(key + ".icon.lore").toArray(new String[0])
                            ));
                        } catch (Exception e) {
                            Logger.logWarning("Error processing MEG balloon type for section: " + key + " in file: " + fileName + " - " + e.getMessage());
                        }
                    } else {
                        // Process the non-MEG balloon type
                        try {
                            // Add the single balloon type to the array list
                            singleBalloons.add(new SingleBalloonType(
                                    key,
                                    config.getString(key + ".id"),
                                    config.getString(key + ".permission"),
                                    config.getDouble(key + ".leash-height"),
                                    config.getDouble(key + ".balloon-height"),
                                    config.getString(key + ".material"),
                                    config.getString(key + ".color"),
                                    config.getInt(key + ".custom-model-data"),
                                    config.getString(key + ".name"),
                                    config.getStringList(key + ".lore").toArray(new String[0])
                            ));
                        } catch (Exception e) {
                            Logger.logWarning("Error processing multipart balloon type for section: " + key + " in file: " + fileName + " - " + e.getMessage());
                        }
                    }
                }
            }
        }

        return singleBalloons;
    }

    /**
     *          Gets all the multipart balloon types from the configuration files
     * @return  The multipart balloon types from the configuration files,
     *          returns an empty array list if no multipart balloons are found, type java.util.ArrayList[net.jeqo.bloons.balloon.multipart.MultipartBalloonType]
     */
    public static ArrayList<MultipartBalloonType> getMultipartBalloons() {
        // Start an array of single balloon types that's empty
        ArrayList<MultipartBalloonType> multipartBalloons = new ArrayList<>();
        File folder = new File(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning(String.format(Languages.getMessage("configuration-folder-not-found"), folder.getPath()));
            return multipartBalloons;
        }

        // List files in the folder
        File[] files = folder.listFiles();
        if (files == null) {
            Logger.logWarning(String.format(Languages.getMessage("no-configuration-files-found"), folder.getPath()));
            return multipartBalloons;
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
                    Logger.logWarning(String.format(Languages.getMessage("configuration-section-not-found"), folder.getPath()));
                    continue;
                }

                // Process each key in the section
                for (String key : section.getKeys(false)) {

                    // Determine the type of balloon
                    String type = config.getString(key + ".type", BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER);

                    if (type.isBlank()) {
                        Logger.logError(String.format(Languages.getMessage("balloon-type-not-found"), key, fileName));
                        continue;
                    }

                    if (!type.equals(BalloonConfiguration.MULTIPART_BALLOON_TYPE_IDENTIFIER)) continue;

                    try {
                        multipartBalloons.add(new MultipartBalloonType(
                                config.getString(key + ".id"),
                                config.getString(key + ".permission"),
                                config.getString(key + ".name"),
                                config.getStringList(key + ".lore").toArray(new String[0]),
                                config.getInt(key + ".node-count"),
                                config.getInt(key + ".distance-between-nodes"),
                                config.getDouble(key + ".leash-height"),
                                config.getDouble(key + ".head-node-offset"),
                                config.getDouble(key + ".body-node-offset"),
                                config.getDouble(key + ".tail-node-offset"),
                                config.getDouble(key + ".max-joint-angle"),
                                config.getDouble(key + ".y-axis-interpolation"),
                                config.getDouble(key + ".turning-spline-interpolation"),
                                config.getDouble(key + ".passive-sine-wave-speed"),
                                config.getDouble(key + ".passive-sine-wave-amplitude"),
                                config.getDouble(key + ".passive-nose-sine-wave-amplitude"),
                                new MultipartBalloonModel(
                                        BalloonSegmentType.HEAD,
                                        config.getString(key + ".head.material"),
                                        Objects.requireNonNull(config.getString(key + ".head.color")),
                                        config.getInt(key + ".head.custom-model-data")
                                ),
                                new MultipartBalloonModel(
                                        BalloonSegmentType.BODY,
                                        config.getString(key + ".body.material"),
                                        Objects.requireNonNull(config.getString(key + ".body.color")),
                                        config.getInt(key + ".body.custom-model-data")
                                ),
                                new MultipartBalloonModel(
                                        BalloonSegmentType.TAIL,
                                        config.getString(key + ".tail.material"),
                                        Objects.requireNonNull(config.getString(key + ".tail.color")),
                                        config.getInt(key + ".tail.custom-model-data")
                                )
                        ));
                    } catch (Exception e) {
                        Logger.logWarning(String.format(Languages.getMessage("balloon-process-error"), key, fileName, e.getMessage()));
                    }
                }
            }
        }

        return multipartBalloons;
    }
}
