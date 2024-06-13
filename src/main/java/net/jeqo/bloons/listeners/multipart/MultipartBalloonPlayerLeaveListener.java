package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonUnequipEvent;
import net.jeqo.bloons.utils.MultipartBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MultipartBalloonPlayerLeaveListener implements Listener {

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
