package net.jeqo.bloons.management;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import org.bukkit.entity.Player;

/**
 * A class to manage balloons and their removal
 */
public class SingleBalloonManagement {

    /**
     *                  Remove the balloon from the player
     * @param player    The player to remove the balloon from, type org.bukkit.entity.Player
     * @param owner     The balloon, type net.jeqo.bloons.balloon.single.SingleBalloon
     */
    public static void removeBalloon(Player player, SingleBalloon owner) {
        if (owner == null) return;

        owner.spawnRemoveParticle();
        owner.cancel();
        Bloons.getPlayerSingleBalloons().remove(player.getUniqueId());
        Bloons.getPlayerSingleBalloonID().remove(player.getUniqueId());
    }

    /**
     *                  Store the balloon in storage and just cancel the runnable
     * @param balloon   The balloon, type net.jeqo.bloons.balloon.single.SingleBalloon
     */
    public static void storeBalloon(SingleBalloon balloon) {
        if (balloon == null) return;

        balloon.cancel();
    }
}
