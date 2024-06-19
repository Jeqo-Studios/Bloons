package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonEquipEvent;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonUnequipEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonEquipEvent;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.jeqo.bloons.utils.management.MultipartBalloonManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

public class CommandForceEquip extends Command {

    public CommandForceEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("fequip");
        this.setCommandDescription("Equip a balloon to a player");
        this.setCommandSyntax("/bloons fequip <player> <balloon>");
        this.setRequiredPermission(CommandPermission.FORCE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            usage(sender);
        }

        Player player = Bukkit.getPlayer(args[0]);
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());

        if (player == null) {
            Component playerNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("player-not-found"));
            sender.sendMessage(playerNotFoundMessage);
            return false;
        }

        String balloonID = args[1];
        if (!this.getPlugin().getConfig().contains(ConfigConfiguration.SINGLE_BALLOON_SECTION + balloonID) && !this.getPlugin().getConfig().contains(ConfigConfiguration.MULTIPART_BALLOON_SECTION + balloonID)) {
            Component balloonNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("balloon-not-found"));
            sender.sendMessage(balloonNotFoundMessage);
            return false;
        }

        MultipartBalloonType type = Bloons.getBalloonCore().getMultipartBalloonByName(balloonID);
        MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());
        if (previousBalloon != null) {
            MultipartBalloonUnequipEvent multipartBalloonUnequipEvent = new MultipartBalloonUnequipEvent(player, previousBalloon);
            multipartBalloonUnequipEvent.callEvent();

            if (multipartBalloonUnequipEvent.isCancelled()) return false;

            previousBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

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

        return false;
    }
}
