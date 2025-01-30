package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloon;
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

        // If the specified player doesn't exist, send a message to the sender
        if (player == null) {
            String playerNotFoundMessage = Languages.getMessage("prefix") + Languages.getMessage("player-not-found");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', playerNotFoundMessage));
            return false;
        }

        SingleBalloon singleBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
        MultipartBalloon multipartBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());

        // If the player doesn't have any balloons equipped, send a message to the sender
        if (singleBalloon == null && multipartBalloon == null) {
            String notEquippedMessage = Languages.getMessage("prefix") + Languages.getMessage("not-equipped");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', notEquippedMessage));
            return false;
        }

        // If the player has a single balloon equipped, unequip it
        SingleBalloonManagement.removeBalloon(player, singleBalloon);

        // If the player has a multipart balloon equipped, unequip it
        if (multipartBalloon != null) {
            multipartBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        String unequipSuccessfulMessage = Languages.getMessage("prefix") + Languages.getMessage("unequipped");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', unequipSuccessfulMessage));
        return false;
    }
}
