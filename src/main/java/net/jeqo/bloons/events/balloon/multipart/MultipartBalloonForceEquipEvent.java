package net.jeqo.bloons.events.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

@Getter
@Setter
public class MultipartBalloonForceEquipEvent extends BloonsEvent {
    private Player player;
    private String balloonID;

    public MultipartBalloonForceEquipEvent(Player player, String balloonID) {
        this.player = player;
        this.balloonID = balloonID;
    }
}
