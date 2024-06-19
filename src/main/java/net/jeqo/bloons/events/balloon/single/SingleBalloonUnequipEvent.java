package net.jeqo.bloons.events.balloon.single;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

/**
 * An event that is called when a player unequips a single balloon
 */
@Getter @Setter
public class SingleBalloonUnequipEvent extends BloonsEvent {
    private Player player;
    private SingleBalloon balloon;

    /**
     *                  Constructor to trigger the SingleBalloonUnequipEvent
     * @param player    The player that is unequipping the balloon, type org.bukkit.entity.Player
     * @param balloon   The balloon that is being unequipped, type net.jeqo.bloons.balloon.single.SingleBalloon
     */
    public SingleBalloonUnequipEvent(Player player, SingleBalloon balloon) {
        this.setPlayer(player);
        this.setBalloon(balloon);
    }
}
