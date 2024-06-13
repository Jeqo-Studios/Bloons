package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MultipartBalloonPlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        MultipartBalloon playerBalloon = Bloons.getPlayerBalloon(event.getPlayer().getUniqueId());
        if (playerBalloon != null) {
            playerBalloon.destroy();
        }
    }
}
