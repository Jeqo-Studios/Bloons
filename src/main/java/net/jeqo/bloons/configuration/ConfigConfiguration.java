package net.jeqo.bloons.configuration;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.model.BalloonModelType;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonModel;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.logger.Logger;
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
    public static final String BALLOON_CONFIGURATION_FOLDER = "balloons"; // The folder that stores the balloons to be loaded

    /**
     *          Gets the number of configuration files currently in the balloon configuration folder
     * @return  The number of configuration files in the balloon configuration folder. Returns 0 upon none found. type long
     */
    public static long getBalloonConfigurationCount() {
        try {
            Path path = Path.of(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER);
            return Files.walk(path).filter(Files::isRegularFile).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     *         Gets all the single balloon types from the configuration files
     * @return The single balloon types from the configuration files,
     *         returns an empty array list if no single balloons are found, type java.util.ArrayList<net.jeqo.bloons.balloon.single.SingleBalloonType>
     */
    public static ArrayList<SingleBalloonType> getSingleBalloons() {
        ArrayList<SingleBalloonType> singleBalloons = new ArrayList<>();
        File folder = new File(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning("Configuration folder not found: " + folder.getPath());
            return singleBalloons;
        }

        // List files in the folder
        File[] files = folder.listFiles();
        if (files == null) {
            Logger.logWarning( "No files found in configuration folder: " + folder.getPath());
            return singleBalloons;
        }

        // Process each file
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();

                // Load the configuration file
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                // Get the configuration section
                ConfigurationSection section = config.getConfigurationSection("");
                if (section == null) {
                    Logger.logWarning("Configuration section not found for file: " + fileName);
                    continue;
                }

                // Process each key in the section
                for (String key : section.getKeys(false)) {

                    // Determine the type of balloon
                    String type = config.getString(key + ".type", BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER);
                    if (type.equals(BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER)) {
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
     *          returns an empty array list if no multipart balloons are found, type java.util.ArrayList<net.jeqo.bloons.balloon.multipart.MultipartBalloonType>
     */
    public static ArrayList<MultipartBalloonType> getMultipartBalloons() {
        ArrayList<MultipartBalloonType> multipartBalloons = new ArrayList<>();
        File folder = new File(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning("Configuration folder not found: " + folder.getPath());
            return multipartBalloons;
        }

        // List files in the folder
        File[] files = folder.listFiles();
        if (files == null) {
            Logger.logWarning( "No files found in configuration folder: " + folder.getPath());
            return multipartBalloons;
        }

        // Process each file
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();

                // Load the configuration file
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                // Get the configuration section
                ConfigurationSection section = config.getConfigurationSection("");
                if (section == null) {
                    Logger.logWarning("Configuration section not found for file: " + fileName);
                    continue;
                }

                // Process each key in the section
                for (String key : section.getKeys(false)) {

                    // Determine the type of balloon
                    String type = config.getString(key + ".type", BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER);
                    if (type.equals(BalloonConfiguration.MULTIPART_BALLOON_TYPE_IDENTIFIER)) {
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
                                            BalloonModelType.HEAD,
                                            config.getString(key + ".head.material"),
                                            config.getString(key + ".head.color"),
                                            config.getInt(key + ".head.custom-model-data")
                                    ),
                                    new MultipartBalloonModel(
                                            BalloonModelType.BODY,
                                            config.getString(key + ".body.material"),
                                            config.getString(key + ".body.color"),
                                            config.getInt(key + ".body.custom-model-data")
                                    ),
                                    new MultipartBalloonModel(
                                            BalloonModelType.TAIL,
                                            config.getString(key + ".tail.material"),
                                            config.getString(key + ".tail.color"),
                                            config.getInt(key + ".tail.custom-model-data")
                                    )
                            ));
                        } catch (Exception e) {
                            Logger.logWarning("Error processing multipart balloon type for section: " + key + " in file: " + fileName + " - " + e.getMessage());
                        }
                    }
                }
            }
        }

        return multipartBalloons;
    }
}
