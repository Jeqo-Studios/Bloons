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
            Component balloonNotFoundMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("balloon-not-found"));
            player.sendMessage(balloonNotFoundMessage);
            return false;
        }

        SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);
        MultipartBalloonType multipartBalloonType = Bloons.getBalloonCore().getMultipartBalloonByID(balloonID);

        if (singleBalloonType != null) {
            if (!player.hasPermission(singleBalloonType.getPermission())) {
                Component noPermissionMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("no-permission"));
                player.sendMessage(noPermissionMessage);
                return false;
            }
        }

        if (multipartBalloonType != null) {
            if (!player.hasPermission(multipartBalloonType.getPermission())) {
                Component noPermissionMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("no-permission"));
                player.sendMessage(noPermissionMessage);
                return false;
            }
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

        // Play a sound regardless of the balloon type and when it executes successfully
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);

        return false;
    }
}
