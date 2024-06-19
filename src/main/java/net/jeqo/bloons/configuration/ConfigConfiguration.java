package net.jeqo.bloons.configuration;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.model.BalloonModelType;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonModel;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;

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
            //
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

        // For every file in the balloon configuration folder, if a configuration type is multipart, add it to an array list
        for (File file : Objects.requireNonNull(new File(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER).listFiles())) {
            // For every section in the configuration file
            for (String section : Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection(file.getName())).getKeys(false)) {
                // If the section is a multipart balloon type, add it to the array list
                if (section.equals(BalloonConfiguration.MULTIPART_BALLOON_TYPE_IDENTIFIER)) {
                    // Add the multipart balloon type to the array list
                    multipartBalloons.add(new MultipartBalloonType(
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".id"),
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".permission"),
                            Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".name"),
                            Bloons.getInstance().getConfig().getStringList(file.getName() + "." + section + ".lore").toArray(new String[0]),
                            Bloons.getInstance().getConfig().getInt(file.getName() + "." + section + ".node-count"),
                            Bloons.getInstance().getConfig().getInt(file.getName() + "." + section + ".distance-between-nodes"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + ".head-node-offset"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + ".body-node-offset"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + ".tail-node-offset"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + "max-joint-angle"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + "y-axis-interpolation"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + "turning-spline-interpolation"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + "passive-sine-wave-speed"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + "passive-sine-wave-amplitude"),
                            Bloons.getInstance().getConfig().getDouble(file.getName() + "." + section + "passive-nose-sine-wave-amplitude"),
                            new MultipartBalloonModel(BalloonModelType.HEAD, Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".head.material"), Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".head.color"), Bloons.getInstance().getConfig().getInt(file.getName() + "." + section + ".head.custom-model-data")),
                            new MultipartBalloonModel(BalloonModelType.BODY, Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".body.material"), Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".body.color"), Bloons.getInstance().getConfig().getInt(file.getName() + "." + section + ".body.custom-model-data")),
                            new MultipartBalloonModel(BalloonModelType.TAIL, Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".tail.material"), Bloons.getInstance().getConfig().getString(file.getName() + "." + section + ".tail.color"), Bloons.getInstance().getConfig().getInt(file.getName() + "." + section + ".tail.custom-model-data"))
                    ));
                }
            }
        }

        return multipartBalloons;
    }
}
