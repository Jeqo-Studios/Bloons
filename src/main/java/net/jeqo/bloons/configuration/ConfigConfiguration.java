package net.jeqo.bloons.configuration;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.model.BalloonSegmentType;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonModel;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.logger.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public static String getBalloonConfigurationFolder() {
        return Bloons.getConfigurationManager().getBalloonFolderPath().toString();
    }

    /**
     *          Checks if the server is running Paper
     * @return  True if the server is running Paper, false otherwise, type boolean
     */
    public static boolean isPaperServer() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException ignored) {
            // Fallback: check version string
        }

        try {
            String version = org.bukkit.Bukkit.getBukkitVersion();
            return version.toLowerCase().contains("paper");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *         Checks if the server has Model Engine installed
     * @return True if Model Engine is installed, false otherwise, type boolean
     */
    public static boolean serverHasModelEngine() {
        try {
            Class.forName("com.ticxo.modelengine.core.ModelEngine");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    /**
     *          Gets the number of configuration files currently in the balloon configuration folder
     * @return  The number of configuration files in the balloon configuration folder. Returns 0 upon none found. type long
     */
    public static long getBalloonConfigurationCount() {
        return Bloons.getConfigurationManager().getBalloonConfigurationCount();
    }

    /**
     *         Gets all the single balloon types from the configuration files
     * @return The single balloon types from the configuration files,
     *         returns an empty array list if no single balloons are found, type java.util.ArrayList[net.jeqo.bloons.balloon.single.SingleBalloonType]
     */
    public static ArrayList<SingleBalloonType> getSingleBalloons() {
        ArrayList<SingleBalloonType> singleBalloons = new ArrayList<>();
        for (File file : getBalloonConfigurationFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection section = getRootSection(config, file);
            if (section == null) {
                continue;
            }

            for (String key : section.getKeys(false)) {
                String type = getBalloonType(config, file.getName(), key);
                if (!BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER.equals(type)) {
                    continue;
                }

                SingleBalloonType singleBalloon = createSingleBalloonType(config, file.getName(), key);
                if (singleBalloon != null) {
                    singleBalloons.add(singleBalloon);
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
        ArrayList<MultipartBalloonType> multipartBalloons = new ArrayList<>();
        for (File file : getBalloonConfigurationFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection section = getRootSection(config, file);
            if (section == null) {
                continue;
            }

            for (String key : section.getKeys(false)) {
                String type = getBalloonType(config, file.getName(), key);
                if (!BalloonConfiguration.MULTIPART_BALLOON_TYPE_IDENTIFIER.equals(type)) {
                    continue;
                }

                MultipartBalloonType multipartBalloon = createMultipartBalloonType(config, file.getName(), key);
                if (multipartBalloon != null) {
                    multipartBalloons.add(multipartBalloon);
                }
            }
        }

        return multipartBalloons;
    }

    private static List<File> getBalloonConfigurationFiles() {
        List<File> files = Bloons.getConfigurationManager().getBalloonConfigurationFiles();
        if (files.isEmpty()) {
            Logger.logWarning(String.format(Bloons.getConfigurationManager().getConfigString("no-configuration-files-found"), getBalloonConfigurationFolder()));
        }
        return files;
    }

    private static ConfigurationSection getRootSection(FileConfiguration config, File file) {
        ConfigurationSection section = config.getConfigurationSection("");
        if (section == null) {
            Logger.logWarning(String.format(Bloons.getConfigurationManager().getConfigString("configuration-section-not-found"), file.getPath()));
        }
        return section;
    }

    private static String getBalloonType(FileConfiguration config, String fileName, String key) {
        String type = config.getString(key + ".type", BalloonConfiguration.SINGLE_BALLOON_TYPE_IDENTIFIER);
        if (type == null || type.isBlank()) {
            Logger.logError(String.format(Bloons.getConfigurationManager().getConfigString("balloon-type-not-found"), key, fileName));
            return null;
        }
        return type;
    }

    private static SingleBalloonType createSingleBalloonType(FileConfiguration config, String fileName, String key) {
        if (config.getString(key + ".meg-model-id") != null) {
            return createMegBalloonType(config, fileName, key);
        }

        try {
            return new SingleBalloonType(
                    key,
                    config.getString(key + ".id"),
                    config.getString(key + ".permission"),
                    config.getDouble(key + ".leash-height"),
                    config.getDouble(key + ".balloon-height"),
                    config.getString(key + ".material"),
                    config.getString(key + ".color"),
                    config.getString(key + ".custom-model-data"),
                    config.getString(key + ".item-model"),
                    config.getString(key + ".name"),
                    config.getStringList(key + ".lore").toArray(new String[0])
            );
        } catch (Exception e) {
            Logger.logWarning("Error processing single balloon type for section: " + key + " in file: " + fileName + " - " + e.getMessage());
            return null;
        }
    }

    private static SingleBalloonType createMegBalloonType(FileConfiguration config, String fileName, String key) {
        if (!isPaperServer()) {
            Logger.logWarning("Model Engine balloons are only supported on Paper servers. Skipping MEG balloon type: " + key + " in file: " + fileName);
            return null;
        }

        if (!serverHasModelEngine()) {
            Logger.logWarning("Model Engine plugin not found. Skipping MEG balloon type: " + key + " in file: " + fileName);
            return null;
        }

        try {
            return new SingleBalloonType(
                    key,
                    config.getString(key + ".id"),
                    config.getString(key + ".permission"),
                    config.getString(key + ".icon.material"),
                    config.getString(key + ".icon.custom-model-data"),
                    config.getString(key + ".meg-model-id"),
                    config.getString(key + ".icon.name"),
                    config.getStringList(key + ".icon.lore").toArray(new String[0])
            );
        } catch (Exception e) {
            Logger.logWarning("Error processing MEG balloon type for section: " + key + " in file: " + fileName + " - " + e.getMessage());
            return null;
        }
    }

    private static MultipartBalloonType createMultipartBalloonType(FileConfiguration config, String fileName, String key) {
        try {
            int nodeCount = config.getInt(key + ".node-count", 5);
            List<MultipartBalloonModel> bodyModels = getBodyModels(config, key, nodeCount);

            return new MultipartBalloonType(
                    config.getString(key + ".id"),
                    config.getString(key + ".permission"),
                    config.getString(key + ".name"),
                    config.getStringList(key + ".lore").toArray(new String[0]),
                    config.getInt(key + ".node-count"),
                    config.getDouble(key + ".distance-between-nodes"),
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
                    config.getBoolean(key + ".roll-oscillation-enabled"),
                    config.getDouble(key + ".roll-oscillation-amplitude"),
                    config.getDouble(key + ".roll-oscillation-phase-offset"),
                    createSegmentModel(config, key, "head", BalloonSegmentType.HEAD),
                    bodyModels,
                    createSegmentModel(config, key, "tail", BalloonSegmentType.TAIL),
                    config.getBoolean(key + ".tail-particles.enabled"),
                    config.getString(key + ".tail-particles.type", "DUST"),
                    config.getString(key + ".tail-particles.color", "#0000FF"),
                    config.getInt(key + ".tail-particles.count", 5),
                    config.getDouble(key + ".tail-particles.speed", 0.01)
            );
        } catch (Exception e) {
            Logger.logWarning(String.format(Bloons.getConfigurationManager().getConfigString("balloon-process-error"), key, fileName, e));
            return null;
        }
    }

    private static List<MultipartBalloonModel> getBodyModels(FileConfiguration config, String key, int nodeCount) {
        List<MultipartBalloonModel> bodyModels = new ArrayList<>();
        MultipartBalloonModel defaultBody = createSegmentModel(config, key, "body", BalloonSegmentType.BODY);

        for (int i = nodeCount - 2; i >= 1; i--) {
            String path = key + ".body-" + i;
            if (config.contains(path + ".material")) {
                bodyModels.add(createSegmentModel(config, key, "body-" + i, BalloonSegmentType.BODY));
            } else {
                bodyModels.add(defaultBody);
            }
        }

        if (bodyModels.isEmpty()) {
            bodyModels.add(defaultBody);
        }

        return bodyModels;
    }

    private static MultipartBalloonModel createSegmentModel(FileConfiguration config, String key, String segmentPath, BalloonSegmentType segmentType) {
        return new MultipartBalloonModel(
                segmentType,
                config.getString(key + "." + segmentPath + ".material"),
                config.getString(key + "." + segmentPath + ".color"),
                config.getString(key + "." + segmentPath + ".custom-model-data"),
                config.getString(key + "." + segmentPath + ".item-model")
        );
    }
}
