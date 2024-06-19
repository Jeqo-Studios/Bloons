package net.jeqo.bloons.listeners.multipart;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.events.balloon.multipart.*;
import net.jeqo.bloons.events.balloon.single.SingleBalloonForceUnequipEvent;
import net.jeqo.bloons.utils.management.MultipartBalloonManagement;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
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

        MultipartBalloonForceUnequipEvent multipartBalloonEquipEvent = new MultipartBalloonForceUnequipEvent(event.getPlayer(), balloon);
        multipartBalloonEquipEvent.callEvent();

        if (multipartBalloonEquipEvent.isCancelled()) return;

        balloon.destroy();
        MultipartBalloonManagement.removePlayerBalloon(event.getPlayer().getUniqueId());
    }

    /**
     *              When they respawn, add the balloon they back that they died with
     * @param event The event that is called when a player respawns, type org.bukkit.event.player.PlayerRespawnEvent
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        MultipartBalloon previousBalloon = Bloons.getPlayerMultipartBalloons().get(event.getPlayer().getPlayer().getUniqueId());
        MultipartBalloonType type = previousBalloon.getBalloonType();

        if (previousBalloon == null) return;

        MultipartBalloonForceEquipEvent multipartBalloonEquipEvent = new MultipartBalloonForceEquipEvent(event.getPlayer(), previousBalloon);
        multipartBalloonEquipEvent.callEvent();

        if (multipartBalloonEquipEvent.isCancelled()) return;

        MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, event.getPlayer());
        SingleBalloonManagement.removeBalloon(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));
        MultipartBalloon balloon = builder.build();
        balloon.initialize();
        balloon.run();

        MultipartBalloonManagement.setPlayerBalloon(event.getPlayer().getUniqueId(), balloon);
    }

    /**
     *              When they change worlds, store their balloon and move the balloon armor stand over
     * @param event The event that is called when a player changes worlds, type org.bukkit.event.player.PlayerChangedWorldEvent
     */
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        MultipartBalloon balloon = Bloons.getPlayerMultipartBalloons().get(event.getPlayer().getUniqueId());
        MultipartBalloon previousBalloon = Bloons.getPlayerMultipartBalloons().get(event.getPlayer().getPlayer().getUniqueId());
        MultipartBalloonType type = previousBalloon.getBalloonType();

        MultipartBalloonForceUnequipEvent multipartBalloonForceUnequipEvent = new MultipartBalloonForceUnequipEvent(event.getPlayer(), balloon);

        MultipartBalloonStoreEvent storeEvent = new MultipartBalloonStoreEvent(event.getPlayer(), balloon);
        storeEvent.callEvent();

        if (storeEvent.isCancelled()) return;

        if (balloon != null) {
            SingleBalloonForceUnequipEvent unequipEvent = new SingleBalloonForceUnequipEvent(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));
            unequipEvent.callEvent();

            if (unequipEvent.isCancelled()) return;
            multipartBalloonForceUnequipEvent.callEvent();

            if (multipartBalloonForceUnequipEvent.isCancelled()) return;

            balloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(event.getPlayer().getUniqueId());

            multipartBalloonForceUnequipEvent.callEvent();

            if (multipartBalloonForceUnequipEvent.isCancelled()) return;

            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, event.getPlayer());
            SingleBalloonManagement.removeBalloon(event.getPlayer(), Bloons.getPlayerSingleBalloons().get(event.getPlayer().getUniqueId()));
            MultipartBalloon newBalloon = builder.build();
            newBalloon.initialize();
            newBalloon.run();

            MultipartBalloonManagement.setPlayerBalloon(event.getPlayer().getUniqueId(), newBalloon);
        }
    }
}
