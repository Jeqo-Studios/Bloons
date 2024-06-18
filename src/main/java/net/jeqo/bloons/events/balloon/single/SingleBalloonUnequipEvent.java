package net.jeqo.bloons.events.balloon.single;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

@Getter @Setter
public class SingleBalloonUnequipEvent extends BloonsEvent {
    private Player player;
    private SingleBalloon balloon;

    public SingleBalloonUnequipEvent(Player player, SingleBalloon balloon) {
        this.player = player;
        this.balloon = balloon;
    }
}