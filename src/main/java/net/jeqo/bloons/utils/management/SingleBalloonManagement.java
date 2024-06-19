package net.jeqo.bloons.utils.management;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.events.balloon.single.SingleBalloonStoreEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonUnequipEvent;
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

        SingleBalloonUnequipEvent event = new SingleBalloonUnequipEvent(player, owner);
        event.callEvent();

        if (event.isCancelled()) return;

        owner.spawnRemoveParticle();
        owner.cancel();
        Bloons.getPlayerSingleBalloons().remove(player.getUniqueId());
        Bloons.getPlayerSingleBalloonID().remove(player.getUniqueId());
    }

    /**
     *                  Remove the balloon from the player quickly
     * @param player    The player to remove the balloon from, type org.bukkit.entity.Player
     * @param owner     The balloon, type net.jeqo.bloons.balloon.single.SingleBalloon
     */
    public static void quickRemoveBalloon(Player player, SingleBalloon owner) {
        if (owner == null) return;

        SingleBalloonUnequipEvent event = new SingleBalloonUnequipEvent(player, owner);
        event.callEvent();

        if (event.isCancelled()) return;

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

        SingleBalloonStoreEvent event = new SingleBalloonStoreEvent(balloon.getPlayer(), balloon);
        event.callEvent();

        if (event.isCancelled()) return;

        balloon.cancel();
    }
}
