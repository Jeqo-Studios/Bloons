package net.jeqo.bloons.balloon.multipart.balloon;

import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import org.bukkit.entity.Player;

/**
 * A builder to create/build multipart balloons
 */
public class MultipartBalloonBuilder {

    MultipartBalloonType balloonType;
    Player balloonOwner;

    public MultipartBalloonBuilder(MultipartBalloonType balloonType, Player balloonOwner) {
        this.balloonType = balloonType;
        this.balloonOwner = balloonOwner;
    }

    public MultipartBalloon build() {
        return new MultipartBalloon(this);
    }
}
