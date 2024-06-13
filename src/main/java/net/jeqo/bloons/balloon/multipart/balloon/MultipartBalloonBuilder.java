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

    public MultipartBalloonBuilder(MultipartBalloonType balloonType, Player balloonOwner) {
        this.setBalloonType(balloonType);
        this.setBalloonOwner(balloonOwner);
    }

    public MultipartBalloon build() {
        return new MultipartBalloon(this);
    }
}
