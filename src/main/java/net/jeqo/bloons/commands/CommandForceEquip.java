package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.management.SingleBalloonManagement;
import net.jeqo.bloons.management.MultipartBalloonManagement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

/**
 * A command used to force equip a balloon to the specified player
 */
public class CommandForceEquip extends Command {

    /**
     *                  Constructor for the CommandForceEquip class
     * @param plugin    The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandForceEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("fequip");
        this.setCommandDescription("Equip a balloon to a player");
        this.setCommandSyntax("/bloons fequip <player> <balloon> [#HEAD] [#BODY] [#TAIL]  (single balloons still accept \\[#RRGGBB\\] as before)");
        this.setRequiredPermission(CommandPermission.FORCE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Require at least player and balloon
        if (args.length < 2) usage(sender);

        Player player = Bukkit.getPlayer(args[0]);

        // If the player isn't found, send a message to the sender
        if (player == null) {
            String playerNotFoundMessage = Languages.getMessage("prefix") + Languages.getMessage("player-not-found");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', playerNotFoundMessage));
            return false;
        }

        String balloonID = args[1];

        // Optional color overrides
        String colorOverride = null; // single balloons
        String headOverride = null;
        String bodyOverride = null;
        String tailOverride = null;

        final String hexRegex = "^#([A-Fa-f0-9]{6})$";

        // Determine types
        MultipartBalloonType type = Bloons.getBalloonCore().getMultipartBalloonByID(balloonID);
        SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);

        // Parse color args depending on type
        if (type != null) {
            // args start at index 2: [player, balloon, head, body, tail]
            if (args.length >= 3) {
                headOverride = args[2];
                if (!headOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), headOverride);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
            if (args.length >= 4) {
                bodyOverride = args[3];
                if (!bodyOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), bodyOverride);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
            if (args.length >= 5) {
                tailOverride = args[4];
                if (!tailOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), tailOverride);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
        } else {
            // single balloon: optional single hex at args[2]
            if (args.length >= 3) {
                colorOverride = args[2];
                if (!colorOverride.matches(hexRegex)) {
                    String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), colorOverride);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                    return false;
                }
            }
        }

        // If the balloon ID isn't found in both balloon types, send a message to the sender
        if (Bloons.getBalloonCore().containsSingleBalloon(balloonID) && Bloons.getBalloonCore().containsMultipartBalloon(balloonID)) {
            String balloonNotFoundMessage = Languages.getMessage("prefix") + Languages.getMessage("balloon-not-found");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', balloonNotFoundMessage));
            return false;
        }

        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());

        // If the player has a previous multipart balloon, unequip it
        if (previousBalloon != null) {
            previousBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        // If the balloon ID is a multipart balloon type, equip the balloon with the multipart associated methods
        if (type != null) {
            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, player);

            // apply overrides if present
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

            // If the balloon ID is a single balloon type, equip the balloon with the single associated methods
        } else {
            // Check if a balloon needs to be added or removed, pass optional colour override
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID, colorOverride);

            String equippedMessage = Languages.getMessage("prefix") + String.format(Languages.getMessage("equipped"), singleBalloonType.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', equippedMessage));
        }

        return false;
    }
}
