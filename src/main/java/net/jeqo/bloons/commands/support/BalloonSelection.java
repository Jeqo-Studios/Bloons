package net.jeqo.bloons.commands.support;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;

/**
 * Resolves a balloon id against the registered single and multipart types.
 */
public record BalloonSelection(
        String balloonId,
        SingleBalloonType singleType,
        MultipartBalloonType multipartType
) {
    public static BalloonSelection resolve(String balloonId) {
        return new BalloonSelection(
                balloonId,
                Bloons.getBalloonCore().getSingleBalloonByID(balloonId),
                Bloons.getBalloonCore().getMultipartBalloonByID(balloonId)
        );
    }

    public boolean isMultipart() {
        return multipartType != null;
    }

    public boolean isSingle() {
        return multipartType == null && singleType != null;
    }
}
