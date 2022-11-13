package net.jeqo.bloons.listeners;

import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BalloonOwner owner = (BalloonOwner) Bloons.playerBalloons.get(e.getPlayer().getUniqueId());
        Utils.removeBalloon(e.getPlayer(), owner);
    }
}