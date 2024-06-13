package net.jeqo.bloons.events.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

@Getter
@Setter
public class MultipartBalloonEquipEvent extends BloonsEvent {
    private Player player;
    private MultipartBalloon balloon;
    private String balloonID;

    public MultipartBalloonEquipEvent(Player player, MultipartBalloon balloon, String balloonID) {
        this.player = player;
        this.balloon = balloon;
        this.balloonID = balloonID;
    }

    public MultipartBalloonEquipEvent(Player player, MultipartBalloon balloon) {
        this.player = player;
        this.balloon = balloon;
    }

    public MultipartBalloonEquipEvent(Player player, String balloonID) {
        this.player = player;
        this.balloonID = balloonID;
    }

    public MultipartBalloonEquipEvent(Player player) {
        this.player = player;
    }
}
