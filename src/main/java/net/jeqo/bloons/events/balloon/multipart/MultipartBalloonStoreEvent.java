package net.jeqo.bloons.events.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

/**
 * An event that is called when a player stores a multipart balloon
 */
@Getter @Setter
public class MultipartBalloonStoreEvent extends BloonsEvent {
    private Player player;
    private MultipartBalloon balloon;

    /**
     *                 Constructor to trigger the MultipartBalloonStoreEvent
     * @param player   The player that is storing the balloon, type org.bukkit.entity.Player
     * @param balloon  The balloon that is being stored, type net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon
     */
    public MultipartBalloonStoreEvent(Player player, MultipartBalloon balloon) {
        this.setPlayer(player);
        this.setBalloon(balloon);
    }
}
