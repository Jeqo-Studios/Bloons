package net.jeqo.bloons.listeners;

import net.jeqo.bloons.configuration.BalloonConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class BalloonArmorStandEntityListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            event.setCancelled(true);
        }
    }
}
