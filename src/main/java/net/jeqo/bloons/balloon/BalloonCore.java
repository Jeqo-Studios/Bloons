package net.jeqo.bloons.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * The core class to handle the registering of multipart balloons
 */
public class BalloonCore {
    @Setter @Getter
    private JavaPlugin plugin;
    @Setter @Getter
    public static ArrayList<MultipartBalloonType> multipartBalloonTypes = new ArrayList<>();
    @Setter @Getter
    public static ArrayList<SingleBalloonType> singleBalloonTypes = new ArrayList<>();

    /**
     *                          Creates a new instance of the balloon core manager with preset registered balloons
     * @param plugin            The plugin instance, type org.bukkit.plugin.java.JavaPlugin
     * @param balloons          The balloons to register, type java.util.ArrayList<net.jeqo.bloons.balloon.multipart.MultipartBalloonType>
     * @param singleBalloons    The single balloons to register, type java.util.ArrayList<net.jeqo.bloons.balloon.single.SingleBalloonType>
     */
    public BalloonCore(JavaPlugin plugin, ArrayList<MultipartBalloonType> balloons, ArrayList<SingleBalloonType> singleBalloons) {
        this.setPlugin(plugin);
        this.setMultipartBalloonTypes(balloons);
        this.setSingleBalloonTypes(singleBalloons);
    }

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
        getSingleBalloonTypes().clear();

        // Set the array to be full of all multipart balloons
        this.setMultipartBalloonTypes(ConfigConfiguration.getMultipartBalloons());

        // Set the array to be full of all single balloons
        this.setSingleBalloonTypes(ConfigConfiguration.getSingleBalloons());
    }

    /**
     * Copies the example balloons folder to the plugin's data folder if it doesn't exist
     */
    public void copyExampleBalloons() {
        // Copy the entire example balloons folder to the plugin's data folder if it doesn't exist
        Bloons.getInstance().saveResource(ConfigConfiguration.BALLOON_CONFIGURATION_FOLDER, false);
    }

    /**
     *                  Adds a balloon to the registered balloons list
     * @param balloon   The balloon to add, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType
     */
    public void addMultipartBalloon(MultipartBalloonType balloon) {
        this.getMultipartBalloonTypes().add(balloon);
    }

    /**
     *                  Removes a balloon from the registered balloons list
     * @param balloon   The balloon to remove, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType
     */
    public void removeMultipartBalloon(MultipartBalloonType balloon) {
        this.getMultipartBalloonTypes().remove(balloon);
    }

    /**
     *                  Adds a single balloon to the registered balloons list
     * @param balloon   The single balloon to add, type net.jeqo.bloons.balloon.single.SingleBalloonType
     */
    public void addSingleBalloon(SingleBalloonType balloon) {
        this.getSingleBalloonTypes().add(balloon);
    }

    /**
     *                  Removes a single balloon from the registered balloons list
     * @param balloon   The single balloon to remove, type net.jeqo.bloons.balloon.single.SingleBalloonType
     */
    public void removeSingleBalloon(SingleBalloonType balloon) {
        this.getSingleBalloonTypes().remove(balloon);
    }

    /**
     *                  Retrieves a balloon by its ID from the registered balloons list
     * @param ID        The ID of the balloon, type java.lang.String
     * @return          The balloon with the specified name, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType/null
     */
    public static MultipartBalloonType getMultipartBalloonByID(String ID) {
        // Loop over every balloon in the registered balloons list
        for (MultipartBalloonType balloon : getMultipartBalloonTypes()) {
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
     * @return          The single balloon with the specified ID, type net.jeqo.bloons.balloon.single.SingleBalloonType/null
     */
    public static SingleBalloonType getSingleBalloonByID(String ID) {
        // Loop over every single balloon in the registered balloons list
        for (SingleBalloonType balloon : getSingleBalloonTypes()) {
            // Check if the single balloon's name matches the specified name
            if (balloon.getId().equalsIgnoreCase(ID)) {
                return balloon;
            }
        }

        // Return null if the single balloon is not found
        return null;
    }
}
