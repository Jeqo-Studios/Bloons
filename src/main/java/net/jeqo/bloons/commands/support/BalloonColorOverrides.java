package net.jeqo.bloons.commands.support;

/**
 * Optional color overrides supplied during balloon equip commands.
 */
public record BalloonColorOverrides(
        String singleColor,
        String headColor,
        String bodyColor,
        String tailColor
) {
    public static BalloonColorOverrides none() {
        return new BalloonColorOverrides(null, null, null, null);
    }
}
