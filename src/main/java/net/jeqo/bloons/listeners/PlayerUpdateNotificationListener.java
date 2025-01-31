package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.health.UpdateChecker;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.VersionChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerUpdateNotificationListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            if (Bloons.getInstance().getConfig().getBoolean("check-for-updates")) {
                // Check for an update if the player is an operator on the server
                new UpdateChecker(Bloons.getInstance(), 106243).getVersion(version -> {
                    String currentVersion = Bloons.getInstance().getDescription().getVersion();

                    if (VersionChecker.isVersionLower(currentVersion, version)) {
                        Logger.logUpdateNotificationPlayer(event.getPlayer());
                    } else if (VersionChecker.isVersionHigher(currentVersion, version)) {
                        Logger.logUnreleasedVersionNotificationPlayer(event.getPlayer());
                    }
                });
            }
        }
    }
}
