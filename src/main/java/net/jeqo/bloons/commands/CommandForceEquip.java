package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonEquipEvent;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonUnequipEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonEquipEvent;
import net.jeqo.bloons.utils.Languages;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.jeqo.bloons.utils.management.MultipartBalloonManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());

        // If the player isn't found, send a message to the sender
        if (player == null) {
            Component playerNotFoundMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("player-not-found"));
            sender.sendMessage(playerNotFoundMessage);
            return false;
        }

        String balloonID = args[1];

        // If the balloon ID isn't found in both balloon types, send a message to the sender
        if (Bloons.getBalloonCore().containsSingleBalloon(balloonID) && Bloons.getBalloonCore().containsMultipartBalloon(balloonID)) {
            Component balloonNotFoundMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("balloon-not-found"));
            sender.sendMessage(balloonNotFoundMessage);
            return false;
        }

        MultipartBalloonType type = Bloons.getBalloonCore().getMultipartBalloonByID(balloonID);
        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());

        // If the player has a previous multipart balloon, unequip it
        if (previousBalloon != null) {
            MultipartBalloonUnequipEvent multipartBalloonUnequipEvent = new MultipartBalloonUnequipEvent(player, previousBalloon);
            multipartBalloonUnequipEvent.callEvent();

            if (multipartBalloonUnequipEvent.isCancelled()) return false;

            previousBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        // If the balloon ID is a multipart balloon type, equip the balloon with the multipart associated methods
        if (type != null) {
            MultipartBalloonEquipEvent multipartBalloonEquipEvent = new MultipartBalloonEquipEvent(player, balloonID);
            multipartBalloonEquipEvent.callEvent();

            if (multipartBalloonEquipEvent.isCancelled()) return false;

            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, player);
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            MultipartBalloon balloon = builder.build();
            balloon.initialize();
            balloon.run();

            MultipartBalloonManagement.setPlayerBalloon(player.getUniqueId(), balloon);

            Component equippedMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), String.format(Languages.getMessage("equipped"), type.getName()));
            player.sendMessage(equippedMessage);

        // If the balloon ID is a single balloon type, equip the balloon with the single associated methods
        } else {
            SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);

            // Call the equip event and check if it's cancelled, if it is, don't spawn the balloon or do anything
            SingleBalloonEquipEvent singleBalloonEquipEvent = new SingleBalloonEquipEvent(player, balloonID);
            singleBalloonEquipEvent.callEvent();

            if (singleBalloonEquipEvent.isCancelled()) return false;

            // Check if a balloon needs to be added or removed
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID);

            Component equippedMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), String.format(Languages.getMessage("equipped"), singleBalloonType.getName()));
            player.sendMessage(equippedMessage);
        }

        return false;
    }
}
