package net.jeqo.bloons.listeners;

import net.jeqo.bloons.configuration.BalloonConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

/**
 * A class that listens for events related to balloon entities
 */
public class BalloonChickenEntityListener implements Listener {

    /**
     *              Stop the chicken from going through the portal to prevent unleashing from player to balloon
     * @param event The event that is called when an entity goes through a portal, type org.bukkit.event.entity.EntityPortalEvent
     */
    @EventHandler
    public void onChickenPortalLeave(EntityPortalEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            event.setCancelled(true);
        }
    }
}
