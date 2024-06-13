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

@Setter @Getter
public class BalloonCore {
    private JavaPlugin plugin;
    private ArrayList<MultipartBalloonType> balloons = new ArrayList<>();

    public BalloonCore(JavaPlugin plugin, ArrayList<MultipartBalloonType> balloons) {
        this.plugin = plugin;
        this.balloons = balloons;
    }

    public BalloonCore(JavaPlugin plugin) {
        this.plugin = plugin;
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

    public void addBalloon(MultipartBalloonType balloon) {
        this.balloons.add(balloon);
    }

    public void removeBalloon(MultipartBalloonType balloon) {
        this.balloons.remove(balloon);
    }

    public MultipartBalloonType getBalloon(String name) {
        for (MultipartBalloonType balloon : this.balloons) {
            if (balloon.getId().equalsIgnoreCase(name)) {
                return balloon;
            }
        }
        return null;
    }
}
