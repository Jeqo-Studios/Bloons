package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.utils.UpdateChecker;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.BalloonManagement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;

public class PlayerHandlers implements Listener {

    /**
     * When a player quits, make sure to despawn and store their balloon in storage
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        SingleBalloon owner = Bloons.playerBalloons.get(e.getPlayer().getUniqueId());
        BalloonManagement.storeBalloon(e.getPlayer(), owner);
    }

    /**
     * When a player joins, add the balloon back if they left with one, or just don't add anything
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String balloonID = Bloons.playerBalloonID.get(event.getPlayer().getUniqueId());

        // If they have a balloon active, remove it and add it back to reduce issues
        if (balloonID != null) {
            BalloonManagement.removeBalloon(event.getPlayer(), Bloons.playerBalloons.get(event.getPlayer().getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(event.getPlayer(), balloonID);
        }

        if (event.getPlayer().isOp()) {
            // Check for an update if the player is an operator on the server
            new UpdateChecker(Bloons.getInstance(), 106243).getVersion(version -> {
                if (!Bloons.getInstance().getDescription().getVersion().equals(version)) {
                    Logger.logUpdateNotificationPlayer(player);
                }
            });
        }
    }

    /**
     * When they die, remove their balloon
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        SingleBalloon balloonOwner = Bloons.playerBalloons.get(Objects.requireNonNull(event.getEntity().getPlayer()).getUniqueId());
        BalloonManagement.removeBalloon(event.getEntity().getPlayer(), balloonOwner);
    }

    /**
     * When they respawn, add the balloon they back that they died with
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        String balloonID = Bloons.playerBalloonID.get(event.getPlayer().getUniqueId());

        if (balloonID != null) {
            SingleBalloon.checkBalloonRemovalOrAdd(event.getPlayer(), balloonID);
        }
    }

    /**
     * When they change worlds, store their balloon and move the balloon armor stand over
     */
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        SingleBalloon balloonOwner = Bloons.playerBalloons.get(event.getPlayer().getUniqueId());
        String balloonID = Bloons.playerBalloonID.get(event.getPlayer().getUniqueId());
        BalloonManagement.storeBalloon(event.getPlayer(), balloonOwner);

        if (balloonID != null) {
            BalloonManagement.removeBalloon(event.getPlayer(), Bloons.playerBalloons.get(event.getPlayer().getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(event.getPlayer(), balloonID);
        }
    }
}