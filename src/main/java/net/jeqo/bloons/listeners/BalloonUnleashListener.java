package net.jeqo.bloons.listeners;

import net.jeqo.bloons.configuration.BalloonConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

public class BalloonUnleashListener implements Listener {

    /**
     *              Used to check if player tries to unleash from their balloon, if they do then cancel it
     * @param event The event that is called when a player leashes an entity
     */
    @EventHandler
    public void onLeash(PlayerLeashEntityEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            event.setCancelled(true);
        }
    }

    /**
     *              Prevents unleashing of chicken with the internal ID
     * @param event The event that is called when a player unleashes an entity, type org.bukkit.event.entity.PlayerUnleashEntityEvent
     */
    @EventHandler
    public void onUnleash(PlayerUnleashEntityEvent event) {
        if (event.getReason() == EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH || event.getReason() == EntityUnleashEvent.UnleashReason.HOLDER_GONE) {
            if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
                event.setCancelled(true);
            }
        }
    }
}
