package net.jeqo.bloons.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

public class LeashHandlers implements Listener {

    @EventHandler
    /**
     * Used to check if player tries to unleash from their balloon, if they do then cancel it
     */
    public void onUnleash(PlayerUnleashEntityEvent e) {
        if (e.getReason() == EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH) {
            if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().contains("4001148")) {
                e.setCancelled(true);
            }
        }
    }
}
