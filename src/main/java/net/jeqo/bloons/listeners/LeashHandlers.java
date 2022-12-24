package net.jeqo.bloons.listeners;

import net.jeqo.bloons.data.BalloonOwner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Chicken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import java.util.Objects;

public class LeashHandlers implements Listener {

    @EventHandler
    public void onUnleash(PlayerUnleashEntityEvent e) {
        if (e.getReason() == EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH) {
            if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().contains("4001148")) {
                e.setCancelled(true);
            }
        }
    }

    public void deadBalloonChecker() {
        Objects.requireNonNull(Bukkit.getWorld("world")).getEntities().forEach(entity -> {
            if (entity.getCustomName() != null && entity.getCustomName().contains("4001148")) {
                entity.remove();
            }
            if (entity.getCustomName() != null && entity.getCustomName().contains("4001147")) {
                entity.remove();
            }
        });
    }

}
