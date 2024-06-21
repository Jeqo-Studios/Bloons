package net.jeqo.bloons.events.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

/**
 * An event that is called when a player stores a single balloon
 */
@Getter @Setter
public class SingleBalloonStoreEvent extends BloonsEvent {
    private Player player;
    private SingleBalloon balloon;

    /**
     *                  Constructor to trigger the SingleBalloonStoreEvent
     * @param player    The player that is storing the balloon, type org.bukkit.entity.Player
     * @param balloon   The balloon that is being stored, type net.jeqo.bloons.balloon.single.SingleBalloon
     */
    public SingleBalloonStoreEvent(Player player, SingleBalloon balloon) {
        this.setPlayer(player);
        this.setBalloon(balloon);
    }
}
