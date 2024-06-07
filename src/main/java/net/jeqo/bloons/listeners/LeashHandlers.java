package net.jeqo.bloons.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import java.util.Objects;

public class LeashHandlers implements Listener {

    /**
     * Used to check if player tries to unleash from their balloon, if they do then cancel it
     */
    @EventHandler
    public void onUnleash(PlayerUnleashEntityEvent event) {
        if (event.getEntity().customName() == null) return;

        if (event.getReason() == EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH) {
            event.setCancelled(true);
        }

        if (Objects.requireNonNull(event.getEntity().customName()).contains(Component.text("4001148"))) {
            event.setCancelled(true);
        }
    }
}
