package net.jeqo.bloons.data;
import net.jeqo.bloons.Bloons;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Utils {
    public static void checkBalloonRemovalOrAdd(final Player player, final String balloonId) {
        (new BukkitRunnable()
        {
            public void run() {
                BalloonRunner runner = (BalloonRunner) Bloons.playerBalloons.get(player.getUniqueId());
                if (runner != null) {
                    return;
                }
                Utils.removeBalloon(player, runner);
                BalloonRunner balloonRunner = new BalloonRunner(player, balloonId);
                balloonRunner.runTaskTimer((Plugin) Bloons.getInstance(), 0L, 1L);
                Bloons.playerBalloons.put(player.getUniqueId(), balloonRunner);
            }
        }).runTaskLater((Plugin) Bloons.getInstance(), 1L);
    }

    public static void removeBalloon(Player player, BalloonRunner runner) {
        if (runner != null) {
            runner.spawnRemoveParticle();
            runner.cancel();
            Bloons.playerBalloons.remove(player.getUniqueId());
        }
    }
}