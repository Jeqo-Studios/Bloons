package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonUnequipEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonUnequipEvent;
import net.jeqo.bloons.utils.Languages;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.jeqo.bloons.utils.management.MultipartBalloonManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());
        Player player = Bukkit.getPlayer(args[0]);

        // If the specified player doesn't exist, send a message to the sender
        if (player == null) {
            Component playerNotFoundMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("player-not-found"));
            sender.sendMessage(playerNotFoundMessage);
            return false;
        }

        SingleBalloon singleBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
        MultipartBalloon multipartBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());

        // If the player doesn't have any balloons equipped, send a message to the sender
        if (singleBalloon == null && multipartBalloon == null) {
            Component notEquippedMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("not-equipped"));
            player.sendMessage(notEquippedMessage);
            return false;
        }

        // If the player has a single balloon equipped, unequip it
        if (singleBalloon != null) {
            SingleBalloonUnequipEvent singleBalloonUnequipEvent = new SingleBalloonUnequipEvent(player, singleBalloon);
            singleBalloonUnequipEvent.callEvent();

            if (singleBalloonUnequipEvent.isCancelled()) return false;

            SingleBalloonManagement.removeBalloon(player, singleBalloon);
        }

        // If the player has a multipart balloon equipped, unequip it
        if (multipartBalloon != null) {
            MultipartBalloonUnequipEvent multipartBalloonUnequipEvent = new MultipartBalloonUnequipEvent(player, multipartBalloon);
            multipartBalloonUnequipEvent.callEvent();

            if (multipartBalloonUnequipEvent.isCancelled()) return false;

            multipartBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        Component unequipSuccessfulMessage = messageTranslations.getSerializedString(Languages.getMessage("prefix"), Languages.getMessage("unequipped"));
        sender.sendMessage(unequipSuccessfulMessage);
        return false;
    }
}
