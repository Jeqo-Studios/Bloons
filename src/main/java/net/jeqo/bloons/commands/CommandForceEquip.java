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
        this.setCommandSyntax("/bloons fequip <player> <balloon>");
        this.setRequiredPermission(CommandPermission.FORCE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // If the arguments aren't within the range of the command, send the usage message
        if (args.length < 1) usage(sender);

        Player player = Bukkit.getPlayer(args[0]);

        // If the player isn't found, send a message to the sender
        if (player == null) {
            String playerNotFoundMessage = Languages.getMessage("prefix") + Languages.getMessage("player-not-found");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', playerNotFoundMessage));
            return false;
        }

        String balloonID = args[1];

        // If the balloon ID isn't found in both balloon types, send a message to the sender
        if (Bloons.getBalloonCore().containsSingleBalloon(balloonID) && Bloons.getBalloonCore().containsMultipartBalloon(balloonID)) {
            String balloonNotFoundMessage = Languages.getMessage("prefix") + Languages.getMessage("balloon-not-found");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', balloonNotFoundMessage));
            return false;
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
            SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);

            // Check if a balloon needs to be added or removed
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID);

            String equippedMessage = Languages.getMessage("prefix") + String.format(Languages.getMessage("equipped"), singleBalloonType.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', equippedMessage));
        }

        return false;
    }
}
