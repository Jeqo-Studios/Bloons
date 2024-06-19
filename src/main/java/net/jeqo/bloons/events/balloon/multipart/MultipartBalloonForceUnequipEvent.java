package net.jeqo.bloons.events.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

/**
 * An event that is called when a player force unequips a multipart balloon
 */
@Getter @Setter
public class MultipartBalloonForceUnequipEvent extends BloonsEvent {
    private Player player;
    private MultipartBalloon balloon;

    /**
     *                 Constructor to trigger the MultipartBalloonForceUnequipEvent
     * @param player   The player that is unequipping the balloon, type org.bukkit.entity.Player
     * @param balloon  The balloon that is being force unequipped, type net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon
     */
    public MultipartBalloonForceUnequipEvent(Player player, MultipartBalloon balloon) {
        this.setPlayer(player);
        this.setBalloon(balloon);
    }
}
