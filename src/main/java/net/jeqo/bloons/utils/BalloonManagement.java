package net.jeqo.bloons.utils;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.SingleBalloon;
import org.bukkit.entity.Player;

public class BalloonManagement {

    /**
     * Remove the balloon from the player
     * @param player The player to remove the balloon from
     * @param owner The balloon
     */
    public static void removeBalloon(Player player, SingleBalloon owner) {
        if (owner != null) {
            owner.spawnRemoveParticle();
            owner.cancel();
            Bloons.playerBalloons.remove(player.getUniqueId());
            Bloons.playerBalloonID.remove(player.getUniqueId());
        }
    }

    /**
     * Remove the balloon from the player quickly
     * @param player The player to remove the balloon from
     * @param owner The balloon
     */
    public static void quickRemoveBalloon(Player player, SingleBalloon owner) {
        if (owner != null) {
            owner.cancel();
            Bloons.playerBalloons.remove(player.getUniqueId());
            Bloons.playerBalloonID.remove(player.getUniqueId());
        }
    }

    /**
     * Store the balloon in storage and just cancel the runnable
     * @param owner The balloon
     */
    public static void storeBalloon(SingleBalloon owner) {
        if (owner != null) {
            owner.cancel();
        }
    }
}
