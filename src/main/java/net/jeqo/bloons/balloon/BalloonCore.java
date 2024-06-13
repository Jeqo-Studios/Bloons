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
     * Creates a new instance of the balloon core manager with preset registered balloons
     * @param plugin The plugin instance
     * @param balloons The balloons to register
     */
    public BalloonCore(JavaPlugin plugin, ArrayList<MultipartBalloonType> balloons) {
        this.setPlugin(plugin);
        this.setBalloons(balloons);
    }


    /**
     * Creates a new empty balloon core
     * @param plugin The plugin instance
     */
    public BalloonCore(JavaPlugin plugin) {
        this.setPlugin(plugin);
    }

    /**
     * Initializes the balloons from the config
     */
    public void initialize() {
        ConfigurationSection multipartBalloonsSection = Bloons.getInstance().getConfig().getConfigurationSection("multipart-balloons");

        if (multipartBalloonsSection == null) {
            return;
        }

        for (String key : multipartBalloonsSection.getKeys(false)) {
            ConfigurationSection keySection = multipartBalloonsSection.getConfigurationSection(key);

            if (keySection == null) {
                return;
            }

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
                    new MultipartBalloonModel(BalloonModelType.HEAD, keySection.getString("head.material"), keySection.getString("head.color"), keySection.getInt("head.custom-model-data")),
                    new MultipartBalloonModel(BalloonModelType.BODY, keySection.getString("body.material"), keySection.getString("body.color"), keySection.getInt("body.custom-model-data")),
                    new MultipartBalloonModel(BalloonModelType.TAIL, keySection.getString("tail.material"), keySection.getString("tail.color"), keySection.getInt("tail.custom-model-data")));

            this.addBalloon(balloonType);
        }
    }

    /**
     * Adds a balloon to the registered balloons list
     * @param balloon The balloon to add
     */
    public void addBalloon(MultipartBalloonType balloon) {
        this.getBalloons().add(balloon);
    }

    /**
     * Removes a balloon from the registered balloons list
     * @param balloon The balloon to remove
     */
    public void removeBalloon(MultipartBalloonType balloon) {
        this.getBalloons().remove(balloon);
    }

    /**
     * Gets a balloon by its name from the registered balloons list
     * @param name The name of the balloon
     * @return A Multipart Balloon Type object
     */
    public MultipartBalloonType getBalloon(String name) {
        for (MultipartBalloonType balloon : this.getBalloons()) {
            if (balloon.getId().equalsIgnoreCase(name)) {
                return balloon;
            }
        }
        return null;
    }
}
