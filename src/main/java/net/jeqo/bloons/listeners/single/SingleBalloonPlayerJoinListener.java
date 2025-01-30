package net.jeqo.bloons.listeners.single;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.management.SingleBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SingleBalloonPlayerJoinListener implements Listener {

    /**
     *              When a player joins, add the balloon back if they left with one, or just don't add anything
     * @param event The event that is called when a player joins the server, type org.bukkit.event.player.PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String balloonID = Bloons.getPlayerSingleBalloonID().get(event.getPlayer().getUniqueId());

        // If they have a balloon active, remove it and add it back to reduce issues
        if (balloonID != null) {
            SingleBalloonManagement.removeBalloon(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));

            SingleBalloon.checkBalloonRemovalOrAdd(event.getPlayer(), balloonID);
        }
    }
}
