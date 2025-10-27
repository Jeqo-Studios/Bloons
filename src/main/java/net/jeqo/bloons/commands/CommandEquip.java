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
     *                  Constructor for the CommandEquip class
     * @param plugin    The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("equip");
        this.setCommandDescription("Equip a balloon");
        this.setCommandSyntax("/bloons equip <balloon> [#RRGGBB]");
        this.setRequiredPermission(CommandPermission.EQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return false;

        // If the args isn't within the range of the command, send the usage message
        if (args.length < 1) usage(player);

        String balloonID = args[0];

        // Optional color override
        String colorOverride = null;
        if (args.length >= 2) {
            colorOverride = args[1];
            if (!colorOverride.matches("^#([A-Fa-f0-9]{6})$")) {
                String invalidHex = Languages.getMessage("prefix") + String.format(Languages.getMessage("invalid-hex-code"), colorOverride);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidHex));
                return false;
            }
        }

        // If the balloon ID isn't found in both balloon types, send a message to the player
        if (Bloons.getBalloonCore().containsSingleBalloon(balloonID) && Bloons.getBalloonCore().containsMultipartBalloon(balloonID)) {
            String balloonNotFoundMessage = Languages.getMessage("prefix") + Languages.getMessage("balloon-not-found");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', balloonNotFoundMessage));
            return false;
        }

        SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);
        MultipartBalloonType multipartBalloonType = Bloons.getBalloonCore().getMultipartBalloonByID(balloonID);

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

        MultipartBalloonType type = Bloons.getBalloonCore().getMultipartBalloonByID(balloonID);
        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());

        // If the player has a previous multipart balloon, unequip it
        if (previousBalloon != null) {
            previousBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        // If the balloon ID is a multipart balloon type, equip the balloon with the multipart associated methods
        if (type != null) {
            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, player);
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            MultipartBalloon balloon = builder.build();
            balloon.initialize();
            balloon.run();

            MultipartBalloonManagement.setPlayerBalloon(player.getUniqueId(), balloon);

            String equippedMessage = Languages.getMessage("prefix") + String.format(Languages.getMessage("equipped"), type.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', equippedMessage));

            // If the balloon ID is a single balloon type, equip the balloon with the single associated methods
        } else {
            // Check if a balloon needs to be added or removed, pass override color if present
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID, colorOverride);

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
