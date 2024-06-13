package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.utils.MultipartBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MultipartBalloonPlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (MultipartBalloonManagement.getPlayerBalloon(event.getPlayer().getUniqueId()) != null) {
            MultipartBalloonType balloonType = MultipartBalloonManagement.getPlayerBalloon(event.getPlayer().getUniqueId()).getBalloonType();

            MultipartBalloonManagement.removePlayerBalloon(event.getPlayer().getUniqueId());

            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(balloonType, event.getPlayer());
            MultipartBalloon playerBalloon = builder.build();

            MultipartBalloonManagement.setPlayerBalloon(event.getPlayer().getUniqueId(), playerBalloon);
            playerBalloon.initialize();
            playerBalloon.run();
        }
    }
}
