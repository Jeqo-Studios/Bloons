package net.jeqo.bloons.events.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

/**
 * An event that is called when a player force equips a single balloon
 */
@Getter @Setter
public class SingleBalloonForceEquipEvent extends BloonsEvent {
    private Player player;
    private String balloonID;

    /**
     *                  Constructor to trigger the SingleBalloonForceEquipEvent
     * @param player    The player that is equipping the balloon, type org.bukkit.entity.Player
     * @param balloonID The ID of the balloon that is being force equipped, type java.lang.String
     */
    public SingleBalloonForceEquipEvent(Player player, String balloonID) {
        this.setPlayer(player);
        this.setBalloonID(balloonID);
    }
}
