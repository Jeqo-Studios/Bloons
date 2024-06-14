package net.jeqo.bloons.utils.management;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;

import java.util.UUID;

/**
 * A class to manage the active balloons tied to a player
 */
public class MultipartBalloonManagement {

    /**
     * Set the player's balloon in the active balloons map
     * @param playerId The player's UUID
     * @param balloon The balloon to set
     */
    public static void setPlayerBalloon(UUID playerId, MultipartBalloon balloon) {
        Bloons.getPlayerMultipartBalloons().put(playerId, balloon);
    }

    /**
     * Get the player's balloon from the active balloons map
     * @param playerId The player's UUID
     * @return The player's balloon
     */
    public static MultipartBalloon getPlayerBalloon(UUID playerId) {
        return Bloons.getPlayerMultipartBalloons().get(playerId);
    }

    /**
     * Remove the player's balloon from the active balloons map
     * @param playerId The player's UUID
     */
    public static void removePlayerBalloon(UUID playerId) {
        MultipartBalloon balloon = Bloons.getPlayerMultipartBalloons().remove(playerId);
        if (balloon != null) {
            balloon.destroy();
        }
    }
}
