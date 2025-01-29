package net.jeqo.bloons.listeners.single;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.health.UpdateChecker;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.management.SingleBalloonManagement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public class SingleBalloonPlayerListener implements Listener {

    /**
     *              When a player quits, make sure to despawn and store their balloon in storage
     * @param event The event that is called when a player quits the server, type org.bukkit.event.player.PlayerQuitEvent
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        SingleBalloon owner = Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId());

        SingleBalloonManagement.storeBalloon(owner);
    }

    /**
     *              When a player joins, add the balloon back if they left with one, or just don't add anything
     * @param event The event that is called when a player joins the server, type org.bukkit.event.player.PlayerJoinEvent
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String balloonID = Bloons.getPlayerSingleBalloonID().get(event.getPlayer().getUniqueId());

        // If they have a balloon active, remove it and add it back to reduce issues
        if (balloonID != null) {
            SingleBalloonManagement.removeBalloon(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));

            SingleBalloon.checkBalloonRemovalOrAdd(event.getPlayer(), balloonID);
        }

        if (event.getPlayer().isOp()) {
            if (Bloons.getInstance().getConfig().getBoolean("check-for-updates")) {
                // Check for an update if the player is an operator on the server
                new UpdateChecker(Bloons.getInstance(), 106243).getVersion(version -> {
                    String currentVersion = Bloons.getInstance().getDescription().getVersion();

                    if (Bloons.getInstance().isVersionLower(currentVersion, version)) {
                        Logger.logUpdateNotificationPlayer(event.getPlayer());
                    } else if (Bloons.getInstance().isVersionHigher(currentVersion, version)) {
                        Logger.logUnreleasedVersionNotificationPlayer(event.getPlayer());
                    }
                });
            }
        }
    }

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
}