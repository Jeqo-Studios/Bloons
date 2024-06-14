package net.jeqo.bloons.events.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

@Getter
@Setter
public class MultipartBalloonForceEquipEvent extends BloonsEvent {
    private Player player;
    private MultipartBalloon balloon;
    private String balloonID;

    public MultipartBalloonForceEquipEvent(Player player, MultipartBalloon balloon, String balloonID) {
        this.player = player;
        this.balloon = balloon;
        this.balloonID = balloonID;
    }

    public MultipartBalloonForceEquipEvent(Player player, MultipartBalloon balloon) {
        this.player = player;
        this.balloon = balloon;
    }

    public MultipartBalloonForceEquipEvent(Player player, String balloonID) {
        this.player = player;
        this.balloonID = balloonID;
    }

    public MultipartBalloonForceEquipEvent(Player player) {
        this.player = player;
    }
}
