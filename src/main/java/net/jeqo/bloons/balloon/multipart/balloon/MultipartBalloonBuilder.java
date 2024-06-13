package net.jeqo.bloons.balloon.multipart.balloon;

import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import org.bukkit.entity.Player;

/**
 * A builder to create/build multipart balloons
 */
@Setter
public class MultipartBalloonBuilder {

    MultipartBalloonType balloonType;
    Player balloonOwner;

    /**
     * Constructs a balloon via an easy-to-use builder
     * @param balloonType The type of balloon to create
     * @param balloonOwner The player that owns the balloon
     */
    public MultipartBalloonBuilder(MultipartBalloonType balloonType, Player balloonOwner) {
        this.setBalloonType(balloonType);
        this.setBalloonOwner(balloonOwner);
    }

    /**
     * Builds the balloon to a MultipartBalloon instance
     * @return The balloon that was built
     */
    public MultipartBalloon build() {
        return new MultipartBalloon(this);
    }
}
