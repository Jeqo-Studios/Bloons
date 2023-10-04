package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.data.UpdateChecker;
import net.jeqo.bloons.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public class PlayerHandlers implements Listener {

    @EventHandler
    /**
     * When a player quits, make sure to despawn and store their balloon in storage
     */
    public void onQuit(PlayerQuitEvent e) {
        BalloonOwner owner = Bloons.playerBalloons.get(e.getPlayer().getUniqueId());
        Utils.storeBalloon(e.getPlayer(), owner);
    }

    @EventHandler
    /**
     * When a player joins, add the balloon back if they left with one, or just don't add anything
     */
    public void onJoin(PlayerJoinEvent e) {
        String id = Bloons.playerBalloonID.get(e.getPlayer().getUniqueId());
        if (id != null) {
            Utils.removeBalloon(e.getPlayer(), (BalloonOwner) Bloons.playerBalloons.get(e.getPlayer().getUniqueId()));
            BalloonOwner.checkBalloonRemovalOrAdd(e.getPlayer(), id);
        }

        if (e.getPlayer().isOp()) {
            Player p = e.getPlayer();
            new UpdateChecker(Bloons.getInstance(), 106243).getVersion(version -> {
                if (!Bloons.getInstance().getDescription().getVersion().equals(version)) {
                    p.sendMessage("");
                    p.sendMessage(Utils.hex(Bloons.getMessage("prefix") + "&eNew update! " + version + " is now available."));
                    p.sendMessage(Utils.hex(Bloons.getMessage("prefix") + "&eDownload it here: &nhttps://jeqo.net/spigot/bloons"));
                    p.sendMessage("");
                }
            });
        }
    }

    @EventHandler
    /**
     * When they die, remove their balloon
     */
    public void onDeath(PlayerDeathEvent e) {
        BalloonOwner owner = Bloons.playerBalloons.get(Objects.requireNonNull(e.getEntity().getPlayer()).getUniqueId());
        Utils.removeBalloon(e.getEntity().getPlayer(), owner);
    }

    @EventHandler
    /**
     * When they respawn, add the balloon they back that they died with
     */
    public void onRespawn(PlayerRespawnEvent e) {
        String id = Bloons.playerBalloonID.get(e.getPlayer().getUniqueId());
        if (id != null) {
            BalloonOwner.checkBalloonRemovalOrAdd(e.getPlayer(), id);
        }
    }

    @EventHandler
    /**
     * When they change worlds, store their balloon and move the balloon armor stand over
     */
    public void onWorldChange(PlayerChangedWorldEvent e) {
        BalloonOwner owner = Bloons.playerBalloons.get(e.getPlayer().getUniqueId());
        Utils.storeBalloon(e.getPlayer(), owner);

        String id = Bloons.playerBalloonID.get(e.getPlayer().getUniqueId());
        if (id != null) {
            Utils.removeBalloon(e.getPlayer(), Bloons.playerBalloons.get(e.getPlayer().getUniqueId()));
            BalloonOwner.checkBalloonRemovalOrAdd(e.getPlayer(), id);
        }
    }
}