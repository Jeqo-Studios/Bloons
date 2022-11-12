package net.jeqo.bloons.listeners;

import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BalloonOwner owner = (BalloonOwner) Bloons.playerBalloons.get(event.getPlayer().getUniqueId());
        Utils.removeBalloon(event.getPlayer(), owner);
    }
}