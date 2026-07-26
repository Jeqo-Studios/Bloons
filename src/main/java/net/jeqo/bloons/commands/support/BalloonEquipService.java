package net.jeqo.bloons.commands.support;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.management.MultipartBalloonManagement;
import net.jeqo.bloons.management.SingleBalloonManagement;
import org.bukkit.entity.Player;

/**
 * Shared balloon equip workflow used by self and force equip commands.
 */
public final class BalloonEquipService {
    private BalloonEquipService() {
    }

    public static String equip(Player player, BalloonSelection selection, BalloonColorOverrides overrides) {
        removeExistingMultipartBalloon(player);

        if (selection.isMultipart()) return equipMultipartBalloon(player, selection, overrides);

        return equipSingleBalloon(player, selection, overrides);
    }

    private static String equipMultipartBalloon(Player player, BalloonSelection selection, BalloonColorOverrides overrides) {
        MultipartBalloonBuilder builder = new MultipartBalloonBuilder(selection.multipartType(), player);

        if (overrides.headColor() != null) {
            builder.setHeadColorOverride(overrides.headColor());
        }
        if (overrides.bodyColor() != null) {
            builder.setBodyColorOverride(overrides.bodyColor());
        }
        if (overrides.tailColor() != null) {
            builder.setTailColorOverride(overrides.tailColor());
        }

        SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));

        MultipartBalloon balloon = builder.build();
        balloon.initialize();
        balloon.run();

        MultipartBalloonManagement.setPlayerBalloon(player.getUniqueId(), balloon);
        return selection.multipartType().getName();
    }

    private static String equipSingleBalloon(Player player, BalloonSelection selection, BalloonColorOverrides overrides) {
        SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
        SingleBalloon.checkBalloonRemovalOrAdd(player, selection.balloonId(), overrides.singleColor());
        return selection.singleType() != null ? selection.singleType().getName() : null;
    }

    private static void removeExistingMultipartBalloon(Player player) {
        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());
        if (previousBalloon == null) return;

        previousBalloon.destroy();
        MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
    }
}
