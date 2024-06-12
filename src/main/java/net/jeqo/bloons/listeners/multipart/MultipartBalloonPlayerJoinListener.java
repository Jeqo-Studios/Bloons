package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MultipartBalloonPlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Bloons.getPlayerBalloon(event.getPlayer().getUniqueId()) != null) {
            MultipartBalloonType balloonType = Bloons.getPlayerBalloon(event.getPlayer().getUniqueId()).getBalloonType();

            Bloons.removePlayerBalloon(event.getPlayer().getUniqueId());

            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(balloonType, event.getPlayer());
            MultipartBalloon playerBalloon = builder.build();

            Bloons.setPlayerBalloon(event.getPlayer().getUniqueId(), playerBalloon);
            playerBalloon.initialize();
            playerBalloon.run();
        }
    }
}
