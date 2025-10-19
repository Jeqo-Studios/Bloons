package net.jeqo.bloons.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.logger.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The core class to handle the registering of multipart balloons
 */
@Setter @Getter
public class BalloonCore {
    /**
     * The plugin instance that runs the balloon core, type org.bukkit.plugin.java.JavaPlugin
     */
    private JavaPlugin plugin;
    /**
     * Contains all valid and loaded multipart balloon types/configurations
     */
    public ArrayList<MultipartBalloonType> multipartBalloonTypes = new ArrayList<>();
    /**
     * Contains all valid and loaded single balloon types/configurations
     */
    public ArrayList<SingleBalloonType> singleBalloonTypes = new ArrayList<>();
    /**
     * Contains all example balloon files to copy to the plugin's data folder
     */
    private final HashMap<String, ExampleBalloonType> exampleBalloons = new HashMap<>() {{
        put("color_pack_example.yml", ExampleBalloonType.REGULAR);
        put("dyeable_example.yml", ExampleBalloonType.REGULAR);
        put("meg_example.yml", ExampleBalloonType.MEG);
        put("multipart_example.yml", ExampleBalloonType.REGULAR);
    }};

    /**
     *                  Creates a new empty balloon core instance
     * @param plugin    The plugin instance, type org.bukkit.plugin.java.JavaPlugin
     */
    public BalloonCore(JavaPlugin plugin) {
        this.setPlugin(plugin);
    }

    /**
     * Initializes the balloons from the config and clears the current balloons map
     */
    public void initialize() {

        // Clear the current balloons list to reduce memory usage
        this.getMultipartBalloonTypes().clear();
        this.getSingleBalloonTypes().clear();

        // Set the array to be full of all multipart balloons
        this.setMultipartBalloonTypes(ConfigConfiguration.getMultipartBalloons());

        // Set the array to be full of all single balloons
        this.setSingleBalloonTypes(ConfigConfiguration.getSingleBalloons());
    }

    /**
     * Copies the example balloons folder to the plugin's data folder if it doesn't exist
     */
    public void copyExampleBalloons() {
        File marker = new File(this.getPlugin().getDataFolder(), ".examples_copied");
        if (marker.exists()) return;

        // Save all example files in the balloons folder in /resources
        for (Map.Entry<String, ExampleBalloonType> example : this.getExampleBalloons().entrySet()) {
            if (example.getValue() == ExampleBalloonType.MEG && !ConfigConfiguration.isPaperServer()) {
                Logger.logWarning("The MEG example balloon requires a Paper server to function. Skipping MEG example balloon copy.");
                continue;
            }

            File file = new File(Bloons.getInstance().getDataFolder() + File.separator + ConfigConfiguration.BALLOON_CONFIGURATION_FOLDER + File.separator + example.getKey());
            if (file.exists()) continue;

            Bloons.getInstance().saveResource(ConfigConfiguration.BALLOON_CONFIGURATION_FOLDER + File.separator + example.getKey(), false);
        }

        // Create marker file to indicate initial copy completed
        try {
            if (!marker.getParentFile().exists()) marker.getParentFile().mkdirs();
            marker.createNewFile();
        } catch (Exception e) {
            Logger.logWarning("Could not create example marker file: " + e.getMessage());
        }
    }

    /**
     *                  Retrieves a balloon by its ID from the registered balloons list
     * @param ID        The ID of the balloon, type java.lang.String
     * @return          The balloon with the specified name, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType
     *                  Returns null if no balloon is found by the specified ID
     */
    public MultipartBalloonType getMultipartBalloonByID(String ID) {
        // Loop over every balloon in the registered balloons list
        for (MultipartBalloonType balloon : this.getMultipartBalloonTypes()) {
            // Check if the balloon's name matches the specified name
            if (balloon.getId().equalsIgnoreCase(ID)) {
                return balloon;
            }
        }

        // Return null if the balloon is not found
        return null;
    }

    /**
     *                  Retrieves a single balloon by its ID from the registered balloons list
     * @param ID        The ID of the balloon, type java.lang.String
     * @return          The single balloon with the specified ID, type net.jeqo.bloons.balloon.single.SingleBalloonType
     *                  Returns null if no balloon is found by the specified ID
     */
    public SingleBalloonType getSingleBalloonByID(String ID) {
        // Loop over every single balloon in the registered balloons list
        for (SingleBalloonType balloon : this.getSingleBalloonTypes()) {
            // Check if the single balloon's name matches the specified name
            if (balloon.getId().equalsIgnoreCase(ID)) {
                return balloon;
            }
        }

        // Return null if the single balloon is not found
        return null;
    }

    /**
     *                 Checks if the registered balloons list contains a balloon with the specified ID
     * @param ID       The ID of the balloon, type java.lang.String
     * @return         Whether the balloon is in the registered balloons list, type boolean
     */
    public boolean containsMultipartBalloon(String ID) {
        return getMultipartBalloonByID(ID) == null;
    }

    /**
     *                Checks if the registered balloons list contains a single balloon with the specified ID
     * @param ID      The ID of the balloon, type java.lang.String
     * @return        Whether the single balloon is in the registered balloons list, type boolean
     */
    public boolean containsSingleBalloon(String ID) {
        return getSingleBalloonByID(ID) == null;
    }
}
