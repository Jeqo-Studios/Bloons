package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonEquipEvent;
import net.jeqo.bloons.management.MultipartBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MultipartBalloonPlayerJoinListener implements Listener {

    /**
     *              Checks if a multipart balloon needs to be added to the player joining
     * @param event The event that is called when a player joins the server, type org.bukkit.event.player.PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MultipartBalloon equippedBalloon = MultipartBalloonManagement.getPlayerBalloon(event.getPlayer().getUniqueId());
        if (equippedBalloon != null) {
            MultipartBalloonEquipEvent equipEvent = new MultipartBalloonEquipEvent(event.getPlayer(), equippedBalloon);
            equipEvent.callEvent();

            if (equipEvent.isCancelled()) return;

            MultipartBalloonType balloonType = MultipartBalloonManagement.getPlayerBalloon(event.getPlayer().getUniqueId()).getType();

            MultipartBalloonManagement.removePlayerBalloon(event.getPlayer().getUniqueId());

            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(balloonType, event.getPlayer());
            MultipartBalloon playerBalloon = builder.build();

            MultipartBalloonManagement.setPlayerBalloon(event.getPlayer().getUniqueId(), playerBalloon);
            playerBalloon.initialize();
            playerBalloon.run();
        }
    }
}
