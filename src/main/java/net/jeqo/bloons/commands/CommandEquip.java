package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.logger.LoggingLevel;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.management.SingleBalloonManagement;
import net.jeqo.bloons.management.MultipartBalloonManagement;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

/**
 * A command used to equip a balloon
 */
public class CommandEquip extends Command {

    /**
     * Constructor for the CommandEquip class
     * @param plugin The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("equip");
        this.setCommandDescription("Equip a balloon");
        this.setCommandSyntax("/bloons equip <balloon> [#HEAD] [#BODY] [#TAIL]  (single balloons still accept \\\\[#RRGGBB\\\\] as before)");
        this.setRequiredPermission(CommandPermission.EQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return false;

        // If the args isn't within the range of the command, send the usage message
        if (args.length < 1) {
            usage(player);
            return false;
        }

        String balloonID = args[0];

        // Prepare containers for possible overrides
        String singleColorOverride = null;
        String headOverride = null;
        String bodyOverride = null;
        String tailOverride = null;

        // Regex for hex color #RRGGBB
        final String hexRegex = "^#([A-Fa-f0-9]{6})$";

        // Determine if the balloonID maps to multipart or single
        SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);
        MultipartBalloonType multipartBalloonType = Bloons.getBalloonCore().getMultipartBalloonByID(balloonID);

        // Parse color args depending on type
        if (multipartBalloonType != null) {
            // Accept up to 3 hex colors: head, body, tail in that order
            if (args.length >= 2) {
                headOverride = args[1];
                if (!headOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), headOverride);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
            if (args.length >= 3) {
                bodyOverride = args[2];
                if (!bodyOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), bodyOverride);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
            if (args.length >= 4) {
                tailOverride = args[3];
                if (!tailOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), tailOverride);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
        } else {
            // single balloon: old behaviour, optional single hex at args[1]
            if (args.length >= 2) {
                singleColorOverride = args[1];
                if (!singleColorOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), singleColorOverride);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
        }

        // Permission checks
        if (singleBalloonType != null) {
            if (!player.hasPermission(singleBalloonType.getPermission())) {
                String noPermissionMessage = Languages.getMessage("prefix") + Languages.getMessage("no-permission");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
                return false;
            }
        }

        if (multipartBalloonType != null) {
            if (!player.hasPermission(multipartBalloonType.getPermission())) {
                String noPermissionMessage = Languages.getMessage("prefix") + Languages.getMessage("no-permission");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
                return false;
            }
        }

        // If player has a previous multipart balloon, unequip it
        MultipartBalloonType type = multipartBalloonType;
        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());
        if (previousBalloon != null) {
            previousBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        // Equip multipart balloon (with optional head/body/tail overrides)
        if (type != null) {
            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, player);

            // NOTE: Ensure MultipartBalloonBuilder has these setter methods implemented to apply overrides.
            if (headOverride != null) builder.setHeadColorOverride(headOverride);
            if (bodyOverride != null) builder.setBodyColorOverride(bodyOverride);
            if (tailOverride != null) builder.setTailColorOverride(tailOverride);

            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            MultipartBalloon balloon = builder.build();
            balloon.initialize();
            balloon.run();

            MultipartBalloonManagement.setPlayerBalloon(player.getUniqueId(), balloon);

            String equippedMessage = Languages.getMessage("prefix") + String.format(Languages.getMessage("equipped"), type.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', equippedMessage));

        } else {
            // Single balloon path (same as before), pass the singleColorOverride
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID, singleColorOverride);

            if (singleBalloonType == null) {
                Logger.logToPlayer(LoggingLevel.ERROR, player, "The current balloon type is null! Please correct this in the config.");
                return false;
            } else {
                String equippedMessage = Languages.getMessage("prefix") + String.format(Languages.getMessage("equipped"), singleBalloonType.getName());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', equippedMessage));
            }
        }

        // Play a sound regardless of the balloon type and when it executes successfully
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);

        return false;
    }
}
