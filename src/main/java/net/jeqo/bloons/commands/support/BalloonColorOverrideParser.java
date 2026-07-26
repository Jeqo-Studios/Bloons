package net.jeqo.bloons.commands.support;

import net.jeqo.bloons.Bloons;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Parses optional hex color overrides for single and multipart balloons.
 */
public final class BalloonColorOverrideParser {
    private static final String HEX_REGEX = "^#([A-Fa-f0-9]{6})$";

    private BalloonColorOverrideParser() {
    }

    public static BalloonColorOverrides parse(CommandSender sender, String[] args, int colorArgStartIndex, BalloonSelection selection) {
        if (selection.isMultipart()) return parseMultipartOverrides(sender, args, colorArgStartIndex);

        return parseSingleOverride(sender, args, colorArgStartIndex);
    }

    private static BalloonColorOverrides parseMultipartOverrides(CommandSender sender, String[] args, int colorArgStartIndex) {
        String headOverride = readValidatedColor(sender, args, colorArgStartIndex);
        if (headOverride == null && args.length > colorArgStartIndex) return null;

        String bodyOverride = readValidatedColor(sender, args, colorArgStartIndex + 1);
        if (bodyOverride == null && args.length > colorArgStartIndex + 1) return null;

        String tailOverride = readValidatedColor(sender, args, colorArgStartIndex + 2);
        if (tailOverride == null && args.length > colorArgStartIndex + 2) return null;

        return new BalloonColorOverrides(null, headOverride, bodyOverride, tailOverride);
    }

    private static BalloonColorOverrides parseSingleOverride(CommandSender sender, String[] args, int colorArgStartIndex) {
        String singleOverride = readValidatedColor(sender, args, colorArgStartIndex);
        if (singleOverride == null && args.length > colorArgStartIndex) return null;

        return new BalloonColorOverrides(singleOverride, null, null, null);
    }

    private static String readValidatedColor(CommandSender sender, String[] args, int index) {
        if (args.length <= index) return null;

        String color = args[index];
        if (!color.matches(HEX_REGEX)) {
            String invalidHex = Bloons.getConfigurationManager().getConfigString("prefix")
                    + String.format(Bloons.getConfigurationManager().getConfigString("invalid-hex-code"), color);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
            return null;
        }

        return color;
    }
}
