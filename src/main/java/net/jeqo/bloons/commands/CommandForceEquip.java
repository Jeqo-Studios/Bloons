package net.jeqo.bloons.commands;

import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.utils.MessageTranslations;
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
        this.setCommandDescription("Force equips a balloon to you");
        this.setCommandSyntax("/bloons fequip <player> <balloon>");
        this.setRequiredPermission(CommandPermission.FORCE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            usage(sender);
        }

        Player player = Bukkit.getPlayer(args[0]);
        MessageTranslations messageTranslations = new MessageTranslations(this.plugin);

        if (player == null) {
            Component playerNotFoundMessage = Component.text(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("player-not-found"));
            sender.sendMessage(playerNotFoundMessage);
            return false;
        }

        String balloonID = args[1];
        if (!this.plugin.getConfig().contains("balloons." + balloonID)) {
            Component balloonNotFoundMessage = Component.text(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("balloon-not-found"));
            sender.sendMessage(balloonNotFoundMessage);
            return false;
        }

        SingleBalloon.checkBalloonRemovalOrAdd(player.getPlayer(), balloonID);
        String balloonName = messageTranslations.getString("balloons." + balloonID + ".name");
        Component equippedMessage = Component.text(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("equipped", balloonName));
        player.sendMessage(equippedMessage);
        return false;
    }
}
