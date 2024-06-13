package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.configuration.ConfigConfiguration;
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

        if (args.length < 1) {
            usage(sender);
        }

        String balloonID = args[0];
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());

        if (!this.getPlugin().getConfig().contains(ConfigConfiguration.SINGLE_BALLOON_SECTION + balloonID) && !this.getPlugin().getConfig().contains(ConfigConfiguration.MULTIPART_BALLOON_SECTION + balloonID)) {
            Component balloonNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("balloon-not-found"));
            player.sendMessage(balloonNotFoundMessage);
            return false;
        }

        if (!player.hasPermission(this.getPlugin().getConfig().getString(ConfigConfiguration.SINGLE_BALLOON_SECTION + balloonID + ".permission", ConfigConfiguration.SINGLE_BALLOON_SECTION + balloonID)) || !player.hasPermission(this.getPlugin().getConfig().getString(ConfigConfiguration.MULTIPART_BALLOON_SECTION + balloonID + ".permission", ConfigConfiguration.MULTIPART_BALLOON_SECTION + balloonID))) {
            Component noPermissionMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("no-permission"));
            player.sendMessage(noPermissionMessage);
            return false;
        }

        MultipartBalloonType type = Bloons.getBalloonCore().getBalloon(balloonID);
        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());
        if (previousBalloon != null) {
            MultipartBalloonUnequipEvent multipartBalloonUnequipEvent = new MultipartBalloonUnequipEvent(player, previousBalloon);
            multipartBalloonUnequipEvent.callEvent();

            if (multipartBalloonUnequipEvent.isCancelled()) return false;

            previousBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }
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

            String balloonName = messageTranslations.getString(ConfigConfiguration.MULTIPART_BALLOON_SECTION + balloonID + ".name");
            Component equippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("equipped", balloonName));
            player.sendMessage(equippedMessage);
        } else {

            // Call the equip event and check if it's cancelled, if it is, don't spawn the balloon or do anything
            SingleBalloonEquipEvent singleBalloonEquipEvent = new SingleBalloonEquipEvent(player, balloonID);
            singleBalloonEquipEvent.callEvent();

            if (singleBalloonEquipEvent.isCancelled()) return false;

            // Check if a balloon needs to be added or removed
            SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID);

            String balloonName = messageTranslations.getString(ConfigConfiguration.SINGLE_BALLOON_SECTION + balloonID + ".name");
            Component equippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("equipped", balloonName));
            player.sendMessage(equippedMessage);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);

        return false;
    }
}
