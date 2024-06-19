package net.jeqo.bloons.events.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

/**
 * An event that is called when a player force equips a multipart balloon
 */
@Getter @Setter
public class MultipartBalloonForceEquipEvent extends BloonsEvent {
    private Player player;
    private MultipartBalloon balloon;
    private String balloonID;

    /**
     *                  Constructor to trigger the MultipartBalloonForceEquipEvent
     * @param player    The player that is equipping the balloon, type org.bukkit.entity.Player
     * @param balloon   The balloon that is being equipped, type net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon
     * @param balloonID The ID of the balloon that is being force equipped, type java.lang.String
     */
    public MultipartBalloonForceEquipEvent(Player player, MultipartBalloon balloon, String balloonID) {
        this.setPlayer(player);
        this.setBalloon(balloon);
        this.setBalloonID(balloonID);
    }

    /**
     *                Constructor to trigger the MultipartBalloonForceEquipEvent
     * @param player  The player that is equipping the balloon, type org.bukkit.entity.Player
     * @param balloon The balloon that is being force equipped, type net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon
     */
    public MultipartBalloonForceEquipEvent(Player player, MultipartBalloon balloon) {
        this.setPlayer(player);
        this.setBalloon(balloon);
    }

    /**
     *                  Constructor to trigger the MultipartBalloonForceEquipEvent
     * @param player    The player that is equipping the balloon, type org.bukkit.entity.Player
     * @param balloonID The ID of the balloon that is being force equipped, type java.lang.String
     */
    public MultipartBalloonForceEquipEvent(Player player, String balloonID) {
        this.setPlayer(player);
        this.setBalloonID(balloonID);
    }

    /**
     *               Constructor to trigger the MultipartBalloonForceEquipEvent
     * @param player The player that is equipping the balloon, type org.bukkit.entity.Player
     */
    public MultipartBalloonForceEquipEvent(Player player) {
        this.setPlayer(player);
    }
}
