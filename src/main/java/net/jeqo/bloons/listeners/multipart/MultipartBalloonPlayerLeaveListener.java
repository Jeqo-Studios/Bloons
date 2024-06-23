package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonUnequipEvent;
import net.jeqo.bloons.management.MultipartBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MultipartBalloonPlayerLeaveListener implements Listener {

    /**
     *              Checks if a multipart balloon needs to be removed from the player leaving
     * @param event The event that is called when a player leaves the server, type org.bukkit.event.player.PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        MultipartBalloon playerBalloon = MultipartBalloonManagement.getPlayerBalloon(event.getPlayer().getUniqueId());
        if (playerBalloon != null) {
            MultipartBalloonUnequipEvent unequipEvent = new MultipartBalloonUnequipEvent(event.getPlayer(), playerBalloon);
            unequipEvent.callEvent();

            if (unequipEvent.isCancelled()) return;

            playerBalloon.destroy();
        }
    }
}
