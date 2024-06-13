package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.events.balloon.SingleBalloonForceUnequipEvent;
import net.jeqo.bloons.events.balloon.SingleBalloonUnequipEvent;
import net.jeqo.bloons.utils.BalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.jeqo.bloons.utils.MultipartBalloonManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandForceUnequip extends Command {

    public CommandForceUnequip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("funequip");
        this.setCommandDescription("Unequip a balloon to a player");
        this.setCommandSyntax("/bloons funequip <player>");
        this.setRequiredPermission(CommandPermission.FORCE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());
        if (player == null) {
            Component playerNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("player-not-found"));
            sender.sendMessage(playerNotFoundMessage);
            return false;
        }

        SingleBalloon singleBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
        MultipartBalloon multipartBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());

        if (singleBalloon == null && multipartBalloon == null) {
            Component notEquippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("not-equipped"));
            player.sendMessage(notEquippedMessage);
            return false;
        }

        if (singleBalloon != null) {
            SingleBalloonUnequipEvent singleBalloonUnequipEvent = new SingleBalloonUnequipEvent(player, singleBalloon);
            singleBalloonUnequipEvent.callEvent();

            if (singleBalloonUnequipEvent.isCancelled()) return false;

            BalloonManagement.removeBalloon(player, singleBalloon);
        }

        if (multipartBalloon != null) {
            multipartBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        BalloonManagement.removeBalloon(player, singleBalloon);
        Component unequipSuccessfulMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("unequipped"));
        sender.sendMessage(unequipSuccessfulMessage);
        return false;
    }
}
