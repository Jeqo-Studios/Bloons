package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.management.MultipartBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listens for player join events to manage multipart balloons
 */
public class MultipartBalloonPlayerJoinListener implements Listener {

    /**
     *              Checks if a multipart balloon needs to be added to the player joining
     * @param event The event that is called when a player joins the server, type org.bukkit.event.player.PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var uuid = event.getPlayer().getUniqueId();
        MultipartBalloon equippedBalloon = MultipartBalloonManagement.getPlayerBalloon(uuid);
        if (equippedBalloon == null) return;

        MultipartBalloonType balloonType = equippedBalloon.getType();
        if (balloonType == null) {
            MultipartBalloonManagement.removePlayerBalloon(uuid);
            return;
        }

        String headOverride = equippedBalloon.getHeadColorOverride();
        String bodyOverride = equippedBalloon.getBodyColorOverride();
        String tailOverride = equippedBalloon.getTailColorOverride();

        MultipartBalloonManagement.removePlayerBalloon(uuid);

        MultipartBalloonBuilder builder = new MultipartBalloonBuilder(balloonType, event.getPlayer());
        if (headOverride != null) builder.setHeadColorOverride(headOverride);
        if (bodyOverride != null) builder.setBodyColorOverride(bodyOverride);
        if (tailOverride != null) builder.setTailColorOverride(tailOverride);

        MultipartBalloon playerBalloon = builder.build();
        MultipartBalloonManagement.setPlayerBalloon(uuid, playerBalloon);
        playerBalloon.initialize();
        playerBalloon.run();
    }
}
