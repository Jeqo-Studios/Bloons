package net.jeqo.bloons.listeners.single;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.management.SingleBalloonManagement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

/**
 * A class that listens for player events related to single balloons and their management
 */
public class SingleBalloonPlayerListener implements Listener {

    /**
     *              When they die, remove their balloon
     * @param event The event that is called when a player dies, type org.bukkit.event.entity.PlayerDeathEvent
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        SingleBalloon balloonOwner = Bloons.getPlayerSingleBalloons().get(Objects.requireNonNull(event.getEntity().getPlayer()).getUniqueId());

        SingleBalloonManagement.removeBalloon(event.getEntity().getPlayer(), balloonOwner);
    }

    /**
     *              When they respawn, add the balloon they back that they died with
     * @param event The event that is called when a player respawns, type org.bukkit.event.player.PlayerRespawnEvent
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        String balloonID = Bloons.getPlayerSingleBalloonID().get(event.getPlayer().getUniqueId());

        if (balloonID != null) {
            SingleBalloon.checkBalloonRemovalOrAdd(event.getPlayer(), balloonID);
        }
    }

    /**
     *              When they change worlds, store their balloon and move the balloon armor stand over
     * @param event The event that is called when a player changes worlds, type org.bukkit.event.player.PlayerChangedWorldEvent
     */
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        SingleBalloon balloonOwner = Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId());
        String balloonID = Bloons.getPlayerSingleBalloonID().get(event.getPlayer().getUniqueId());

        SingleBalloonManagement.storeBalloon(balloonOwner);

        if (balloonID != null) {
            SingleBalloonManagement.removeBalloon(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));

            SingleBalloon.checkBalloonRemovalOrAdd(event.getPlayer(), balloonID);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        SingleBalloon balloonOwner = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
        String balloonID = Bloons.getPlayerSingleBalloonID().get(player.getUniqueId());
        String overrideColor = balloonOwner != null ? balloonOwner.getOverrideColor() : null;

        if (balloonOwner != null && balloonOwner.chicken != null) {
            if (event.getFrom().getWorld().equals(event.getTo().getWorld())) {
                final var chicken = balloonOwner.chicken;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (chicken.isValid()) chicken.teleport(player.getLocation());
                    }
                }.runTaskLater(Bloons.getInstance(), 1L);
                return;
            }

            SingleBalloonManagement.storeBalloon(balloonOwner);
            SingleBalloonManagement.removeBalloon(player, balloonOwner);

            final String finalOverride = overrideColor;
            if (balloonID != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID, finalOverride);
                    }
                }.runTaskLater(Bloons.getInstance(), 1L);
            }
        } else if (balloonID != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID, overrideColor);
                }
            }.runTaskLater(Bloons.getInstance(), 1L);
        }
    }
}