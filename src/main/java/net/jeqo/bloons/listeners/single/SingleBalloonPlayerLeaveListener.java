package net.jeqo.bloons.listeners.single;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.management.SingleBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listens for player leave events to manage single balloons
 */
public class SingleBalloonPlayerLeaveListener implements Listener {

    /**
     *              When a player quits, make sure to despawn and store their balloon in storage
     * @param event The event that is called when a player quits the server, type org.bukkit.event.player.PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        SingleBalloon owner = Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId());

        SingleBalloonManagement.storeBalloon(owner);
    }
}
