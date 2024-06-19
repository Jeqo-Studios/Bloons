package net.jeqo.bloons.configuration;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.model.BalloonModelType;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonModel;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.logger.Logger;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

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

        // For every file in the balloon configuration folder, if a configuration type is single, add it to an array list
        for (File file : Objects.requireNonNull(new File(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER).listFiles())) {
            // For every section in the configuration file
            for (String key : Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection(file.getName())).getKeys(false)) {
                // If the section is a single balloon type, add it to the array list
                if (key.equals(BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER)) {
                    // Add the single balloon type to the array list
                    singleBalloons.add(new SingleBalloonType(
                            key,
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + key + ".id"),
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + key + ".permission"),
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + key + ".material"),
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + key + ".color"),
                            Bloons.getInstance().getConfig().getInt(file.getName() + "." + key + ".customModelData"),
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + key + ".name"),
                            Bloons.getInstance().getConfig().getStringList(file.getName() + "." + key + ".lore").toArray(new String[0])
                    ));
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
            Logger.logWarning("No files found in configuration folder: " + folder.getPath());
            return multipartBalloons;
        }

        // Process each file
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();

                // Get the configuration section
                ConfigurationSection section = Bloons.getInstance().getConfig().getConfigurationSection(fileName);
                if (section == null) {
                    Logger.logWarning("Configuration section not found for file: " + fileName);
                    continue;
                }

                // Process each key in the section
                for (String key : section.getKeys(false)) {
                    if (key.equals(BalloonConfiguration.MULTIPART_BALLOON_TYPE_IDENTIFIER)) {
                        String basePath = fileName + "." + key;

                        try {
                            multipartBalloons.add(new MultipartBalloonType(
                                    Bloons.getInstance().getConfig().getString(basePath + ".id"),
                                    Bloons.getInstance().getConfig().getString(basePath + ".permission"),
                                    Bloons.getInstance().getConfig().getString(basePath + ".name"),
                                    Bloons.getInstance().getConfig().getStringList(basePath + ".lore").toArray(new String[0]),
                                    Bloons.getInstance().getConfig().getInt(basePath + ".node-count"),
                                    Bloons.getInstance().getConfig().getInt(basePath + ".distance-between-nodes"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".head-node-offset"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".body-node-offset"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".tail-node-offset"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".max-joint-angle"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".y-axis-interpolation"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".turning-spline-interpolation"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".passive-sine-wave-speed"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".passive-sine-wave-amplitude"),
                                    Bloons.getInstance().getConfig().getDouble(basePath + ".passive-nose-sine-wave-amplitude"),
                                    new MultipartBalloonModel(
                                            BalloonModelType.HEAD,
                                            Bloons.getInstance().getConfig().getString(basePath + ".head.material"),
                                            Bloons.getInstance().getConfig().getString(basePath + ".head.color"),
                                            Bloons.getInstance().getConfig().getInt(basePath + ".head.custom-model-data")
                                    ),
                                    new MultipartBalloonModel(
                                            BalloonModelType.BODY,
                                            Bloons.getInstance().getConfig().getString(basePath + ".body.material"),
                                            Bloons.getInstance().getConfig().getString(basePath + ".body.color"),
                                            Bloons.getInstance().getConfig().getInt(basePath + ".body.custom-model-data")
                                    ),
                                    new MultipartBalloonModel(
                                            BalloonModelType.TAIL,
                                            Bloons.getInstance().getConfig().getString(basePath + ".tail.material"),
                                            Bloons.getInstance().getConfig().getString(basePath + ".tail.color"),
                                            Bloons.getInstance().getConfig().getInt(basePath + ".tail.custom-model-data")
                                    )
                            ));
                        } catch (Exception e) {
                            Logger.logWarning("Error processing multipart balloon type for section: " + basePath + " in file: " + fileName + " - " + e.getMessage());
                        }
                    }
                }
            }
        }

        return multipartBalloons;
    }
}
