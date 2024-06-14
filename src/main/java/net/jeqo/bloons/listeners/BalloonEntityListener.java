package net.jeqo.bloons.listeners;

import net.jeqo.bloons.configuration.BalloonConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class BalloonEntityListener implements Listener {

    /**
     * Stop the chicken from going through the portal to prevent unleashing from player to balloon
     * @param event The event that is called when an entity goes through a portal
     */
    @EventHandler
    public void onChickenPortalLeave(EntityPortalEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            event.setCancelled(true);
        }
    }
}
