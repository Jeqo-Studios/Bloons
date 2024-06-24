package net.jeqo.bloons.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

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
    private final String[] exampleBalloons = new String[] {
            "color_pack_example.yml",
            "dyeable_example.yml",
            "meg_example.yml",
            "multipart_example.yml"
    };

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
        // List of example balloon files
        String[] exampleBalloons = new String[] {
                "/color_pack_example.yml",
                "/dyeable_example.yml",
                "/multipart_example.yml"
        };

        // Save all example files in the balloons folder in /resources
        for (String example : this.getExampleBalloons()) {
            File file = new File(Bloons.getInstance().getDataFolder() + File.separator + ConfigConfiguration.BALLOON_CONFIGURATION_FOLDER + File.separator + example);
            if (file.exists()) continue;

            Bloons.getInstance().saveResource(ConfigConfiguration.BALLOON_CONFIGURATION_FOLDER + example, false);
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
