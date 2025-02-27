package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.management.MultipartBalloonManagement;
import net.jeqo.bloons.management.SingleBalloonManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public class MultipartBalloonPlayerListener implements Listener {

    /**
     *              When they die, remove the balloon they had equipped
     * @param event The event that is called when a player dies, type org.bukkit.event.entity.PlayerDeathEvent
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        MultipartBalloon balloon = Bloons.getPlayerMultipartBalloons().get(Objects.requireNonNull(event.getEntity().getPlayer()).getUniqueId());

        balloon.destroy();
        MultipartBalloonManagement.removePlayerBalloon(event.getEntity().getUniqueId());
    }

    /**
     *              When they respawn, add the balloon they back that they died with
     * @param event The event that is called when a player respawns, type org.bukkit.event.player.PlayerRespawnEvent
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        MultipartBalloon previousBalloon = Bloons.getPlayerMultipartBalloons().get(event.getPlayer().getUniqueId());

        if (previousBalloon == null) return;

        SingleBalloonManagement.removeBalloon(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));
        previousBalloon.initialize();
        previousBalloon.run();

        MultipartBalloonManagement.setPlayerBalloon(event.getPlayer().getUniqueId(), previousBalloon);
    }

    /**
     *              When they change worlds, store their balloon and move the balloon armor stand over
     * @param event The event that is called when a player changes worlds, type org.bukkit.event.player.PlayerChangedWorldEvent
     */
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        MultipartBalloon balloon = Bloons.getPlayerMultipartBalloons().get(event.getPlayer().getUniqueId());
        MultipartBalloon previousBalloon = Bloons.getPlayerMultipartBalloons().get(event.getPlayer().getUniqueId());
        MultipartBalloonType type = previousBalloon.getType();

        if (balloon != null) {
            balloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(event.getPlayer().getUniqueId());

            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, event.getPlayer());
            SingleBalloonManagement.removeBalloon(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));
            MultipartBalloon newBalloon = builder.build();
            newBalloon.initialize();
            newBalloon.run();

            MultipartBalloonManagement.setPlayerBalloon(event.getPlayer().getUniqueId(), newBalloon);
        }
    }
}
