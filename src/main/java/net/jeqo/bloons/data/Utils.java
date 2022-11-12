package net.jeqo.bloons.data;
import net.jeqo.bloons.Bloons;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void balloonInventory() {

    }
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

    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}