package net.jeqo.bloons.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.model.BalloonModelType;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonModel;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * The core class to handle the registering of multipart balloons
 */
@Setter @Getter
public class BalloonCore {
    private JavaPlugin plugin;
    private ArrayList<MultipartBalloonType> balloons = new ArrayList<>();

    /**
     *                  Creates a new instance of the balloon core manager with preset registered balloons
     * @param plugin    The plugin instance, type org.bukkit.plugin.java.JavaPlugin
     * @param balloons  The balloons to register, type java.util.ArrayList<net.jeqo.bloons.balloon.multipart.MultipartBalloonType>
     */
    public BalloonCore(JavaPlugin plugin, ArrayList<MultipartBalloonType> balloons) {
        this.setPlugin(plugin);
        this.setBalloons(balloons);
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
        // Retrieves the multipart balloons section from the config
        ConfigurationSection multipartBalloonsSection = Bloons.getInstance().getConfig().getConfigurationSection("multipart-balloons");

        // Check if the section is null
        if (multipartBalloonsSection == null) return;

        // Clear the current balloons list to reduce memory usage
        this.getBalloons().clear();

        // Loop over every key in the multipart balloons section
        for (String key : multipartBalloonsSection.getKeys(false)) {
            // Get the data within the section
            ConfigurationSection keySection = multipartBalloonsSection.getConfigurationSection(key);

            // Check if the section is null, if it is then return out of the loop
            if (keySection == null) return;

            // Create a new multipart balloon type object with the data from the config
            // TODO: Manipulate this so that configurations can be left out and will use default values
            MultipartBalloonType balloonType = new MultipartBalloonType(
                    key,
                    keySection.getString("permission"),
                    keySection.getString("name"),
                    keySection.getStringList("lore").toArray(new String[0]),
                    keySection.getInt("node-count"),
                    keySection.getDouble("distance-between-nodes"),
                    keySection.getDouble("head-node-offset"),
                    keySection.getDouble("body-node-offset"),
                    keySection.getDouble("tail-node-offset"),
                    keySection.getDouble("max-node-joint-angle"),
                    keySection.getDouble("y-axis-interpolation"),
                    keySection.getDouble("turning-spline-interpolation"),
                    keySection.getDouble("passive-sine-wave-speed"),
                    keySection.getDouble("passive-sine-wave-amplitude"),
                    keySection.getDouble("passive-nose-sine-wave-amplitude"),
                    new MultipartBalloonModel(BalloonModelType.HEAD, keySection.getString("head.material"), keySection.getString("head.color"), keySection.getInt("head.custom-model-data")),
                    new MultipartBalloonModel(BalloonModelType.BODY, keySection.getString("body.material"), keySection.getString("body.color"), keySection.getInt("body.custom-model-data")),
                    new MultipartBalloonModel(BalloonModelType.TAIL, keySection.getString("tail.material"), keySection.getString("tail.color"), keySection.getInt("tail.custom-model-data")));

            // Add the balloon to the registered balloons list
            this.addBalloon(balloonType);
        }
    }

    /**
     *                  Adds a balloon to the registered balloons list
     * @param balloon   The balloon to add, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType
     */
    public void addBalloon(MultipartBalloonType balloon) {
        this.getBalloons().add(balloon);
    }

    /**
     *                  Removes a balloon from the registered balloons list
     * @param balloon   The balloon to remove, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType
     */
    public void removeBalloon(MultipartBalloonType balloon) {
        this.getBalloons().remove(balloon);
    }

    /**
     *                  Retrieves a balloon by its name from the registered balloons list
     * @param name      The name of the balloon, type java.lang.String
     * @return          The balloon with the specified name, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType/null
     */
    public MultipartBalloonType getBalloon(String name) {
        // Loop over every balloon in the registered balloons list
        for (MultipartBalloonType balloon : this.getBalloons()) {
            // Check if the balloon's name matches the specified name
            if (balloon.getId().equalsIgnoreCase(name)) {
                return balloon;
            }
        }

        // Return null if the balloon is not found
        return null;
    }
}
