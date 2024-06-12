package net.jeqo.bloons.balloon.multipart;

import org.bukkit.entity.Player;

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
