package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.events.balloon.SingleBalloonUnequipEvent;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
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
            Component playerNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("player-not-found"));
            sender.sendMessage(playerNotFoundMessage);
            return false;
        }

        SingleBalloon singleBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());

        // If the player doesn't have any balloons equipped, send a message to the sender
        if (singleBalloon == null) {
            Component notEquippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("not-equipped"));
            player.sendMessage(notEquippedMessage);
            return false;
        }

        // If the player has a single balloon equipped, unequip it
        SingleBalloonUnequipEvent singleBalloonUnequipEvent = new SingleBalloonUnequipEvent(player, singleBalloon);
        singleBalloonUnequipEvent.callEvent();

        if (singleBalloonUnequipEvent.isCancelled()) return false;

        SingleBalloonManagement.removeBalloon(player, singleBalloon);

        Component unequipSuccessfulMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("unequipped"));
        sender.sendMessage(unequipSuccessfulMessage);
        return false;
    }
}
