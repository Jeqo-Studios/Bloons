package net.jeqo.bloons.listeners;

import net.jeqo.bloons.configuration.BalloonConfiguration;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import java.util.Objects;

public class LeashHandlers implements Listener {

    /**
     * Used to check if player tries to unleash from their balloon, if they do then cancel it
     */
    @EventHandler
    public void onLeash(PlayerLeashEntityEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            event.setCancelled(true);
        }
    }
}
