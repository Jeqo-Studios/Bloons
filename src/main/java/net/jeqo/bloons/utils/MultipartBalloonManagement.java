package net.jeqo.bloons.utils;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;

import java.util.UUID;

public class MultipartBalloonManagement {

    // Add a method to set a player's balloon
    public static void setPlayerBalloon(UUID playerId, MultipartBalloon balloon) {
        Bloons.getPlayerMultipartBalloons().put(playerId, balloon);
    }

    // Add a method to get a player's balloon
    public static MultipartBalloon getPlayerBalloon(UUID playerId) {
        return Bloons.getPlayerMultipartBalloons().get(playerId);
    }

    // Add a method to remove a player's balloon
    public static void removePlayerBalloon(UUID playerId) {
        MultipartBalloon balloon = Bloons.getPlayerMultipartBalloons().remove(playerId);
        if (balloon != null) {
            balloon.destroy();
        }
    }
}
