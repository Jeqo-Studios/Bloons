package net.jeqo.bloons.events.balloon.single;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

@Getter @Setter
public class SingleBalloonEquipEvent extends BloonsEvent {
    private Player player;
    private String balloonID;

    public SingleBalloonEquipEvent(Player player, String balloonID) {
        this.player = player;
        this.balloonID = balloonID;
    }

    public SingleBalloonEquipEvent(Player player) {
        this.player = player;
    }
}
