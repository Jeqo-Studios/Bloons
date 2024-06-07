package net.jeqo.bloons.events.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

@Getter @Setter
public class SingleBalloonForceEquipEvent extends BloonsEvent {
    private Player player;
    private String balloonID;

    public SingleBalloonForceEquipEvent(Player player, String balloonID) {
        this.player = player;
        this.balloonID = balloonID;
    }
}
