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
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.jeqo.bloons.utils.management.MultipartBalloonManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

public class CommandEquip extends Command {

    public CommandEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("equip");
        this.setCommandDescription("Equip a balloon");
        this.setCommandSyntax("/bloons equip <balloon>");
        this.setRequiredPermission(CommandPermission.EQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return false;

        // If the args isn't within the range of the command, send the usage message
        if (args.length < 1) usage(player);

        String balloonID = args[0];
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());

        // If the balloon ID isn't found in both balloon types, send a message to the player
        if (Bloons.getBalloonCore().containsSingleBalloon(balloonID) && Bloons.getBalloonCore().containsMultipartBalloon(balloonID)) {
            Component balloonNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("balloon-not-found"));
            player.sendMessage(balloonNotFoundMessage);
            return false;
        }

        // If the player doesn't have the permission to equip the balloon, send a message to the player
        if (!player.hasPermission(Bloons.getBalloonCore().getSingleBalloonByID(balloonID).getPermission())|| !player.hasPermission(Bloons.getBalloonCore().getMultipartBalloonByID(balloonID).getPermission())) {
            Component noPermissionMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("no-permission"));
            player.sendMessage(noPermissionMessage);
            return false;
        }

        MultipartBalloonType type = Bloons.getBalloonCore().getMultipartBalloonByID(balloonID);
        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());

        // If they have a previous multipart balloon equipped, unequip it
        if (previousBalloon != null) {
            MultipartBalloonUnequipEvent multipartBalloonUnequipEvent = new MultipartBalloonUnequipEvent(player, previousBalloon);
            multipartBalloonUnequipEvent.callEvent();

            if (multipartBalloonUnequipEvent.isCancelled()) return false;

            previousBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        // If it's a multipart balloon type, equip the balloon with the multipart associated methods
        if (type != null) {

            // Call the equip event and check if it's cancelled, if it is, don't spawn the balloon or do anything
            MultipartBalloonEquipEvent multipartBalloonEquipEvent = new MultipartBalloonEquipEvent(player, balloonID);
            multipartBalloonEquipEvent.callEvent();

            if (multipartBalloonEquipEvent.isCancelled()) return false;

            MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, player);
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            MultipartBalloon balloon = builder.build();
            balloon.initialize();
            balloon.run();

            MultipartBalloonManagement.setPlayerBalloon(player.getUniqueId(), balloon);

            Component equippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("equipped", type.getName()));
            player.sendMessage(equippedMessage);

        // If it's a single balloon type, equip the balloon with the single balloon associated methods
        } else {
            SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);

            // Call the equip event and check if it's cancelled, if it is, don't spawn the balloon or do anything
            SingleBalloonEquipEvent singleBalloonEquipEvent = new SingleBalloonEquipEvent(player, balloonID);
            singleBalloonEquipEvent.callEvent();

            if (singleBalloonEquipEvent.isCancelled()) return false;

            // Check if a balloon needs to be added or removed
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID);

            Component equippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("equipped", singleBalloonType.getName()));
            player.sendMessage(equippedMessage);
        }

        // Play a sound regardless of the balloon type and when it executes successfully
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);

        return false;
    }
}
