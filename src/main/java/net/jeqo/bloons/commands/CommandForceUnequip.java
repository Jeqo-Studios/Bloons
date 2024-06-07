package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.utils.BalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
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
        this.setCommandDescription("Force unequips a balloon that you have equipped");
        this.setCommandSyntax("/bloons funequip <player>");
        this.setRequiredPermission(CommandPermission.FORCE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        MessageTranslations messageTranslations = new MessageTranslations(this.plugin);
        if (player == null) {
            Component playerNotFoundMessage = Component.text(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("player-not-found"));
            sender.sendMessage(playerNotFoundMessage);
            return false;
        }

        SingleBalloon owner = Bloons.playerBalloons.get(player.getUniqueId());
        if (owner == null) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            Component notEquippedMessage = Component.text(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("not-equipped"));
            sender.sendMessage(notEquippedMessage);
            return false;
        }
        BalloonManagement.removeBalloon(player, owner);
        Component unequipSuccessfulMessage = Component.text(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("unequipped"));
        sender.sendMessage(unequipSuccessfulMessage);
        return false;
    }
}
