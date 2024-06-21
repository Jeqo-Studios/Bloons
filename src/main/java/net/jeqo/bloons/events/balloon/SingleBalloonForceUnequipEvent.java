package net.jeqo.bloons.events.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

/**
 * An event that is called when a player force unequips a single balloon
 */
@Getter @Setter
public class SingleBalloonForceUnequipEvent extends BloonsEvent {
    private Player player;
    private SingleBalloon balloon;

    /**
     *                  Constructor to trigger the SingleBalloonForceUnequipEvent
     * @param player    The player that is unequipping the balloon, type org.bukkit.entity.Player
     * @param balloon   The balloon that is being force unequipped, type net.jeqo.bloons.balloon.single.SingleBalloon
     */
    public SingleBalloonForceUnequipEvent(Player player, SingleBalloon balloon) {
        this.setPlayer(player);
        this.setBalloon(balloon);
    }
}
