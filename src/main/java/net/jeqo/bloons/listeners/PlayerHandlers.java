package net.jeqo.bloons.listeners;

import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerHandlers implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BalloonOwner owner = (BalloonOwner) Bloons.playerBalloons.get(e.getPlayer().getUniqueId());
        Utils.removeBalloon(e.getPlayer(), owner);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String id = Bloons.playerBalloonID.get(e.getPlayer().getUniqueId());
        if (id != null) {
            BalloonOwner.checkBalloonRemovalOrAdd(e.getPlayer(), id);
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        BalloonOwner owner = (BalloonOwner) Bloons.playerBalloons.get(e.getEntity().getPlayer().getUniqueId());
        Utils.removeBalloon(e.getEntity().getPlayer(), owner);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        String id = Bloons.playerBalloonID.get(e.getPlayer().getUniqueId());
        if (id != null) {
            BalloonOwner.checkBalloonRemovalOrAdd(e.getPlayer(), id);
        }
    }


    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        BalloonOwner owner = (BalloonOwner) Bloons.playerBalloons.get(e.getPlayer().getUniqueId());
        Utils.removeBalloon(e.getPlayer(), owner);
        String id = Bloons.playerBalloonID.get(e.getPlayer().getUniqueId());
        if (id != null) {
            BalloonOwner.checkBalloonRemovalOrAdd(e.getPlayer(), id);
        }
    }
}