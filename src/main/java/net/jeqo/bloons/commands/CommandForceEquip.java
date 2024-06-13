package net.jeqo.bloons.commands;

import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.events.balloon.SingleBalloonForceEquipEvent;
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
        if (!this.getPlugin().getConfig().contains(ConfigConfiguration.SINGLE_BALLOON_SECTION + balloonID)) {
            Component balloonNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("balloon-not-found"));
            sender.sendMessage(balloonNotFoundMessage);
            return false;
        }

        SingleBalloonForceEquipEvent singleBalloonForceEquipEvent = new SingleBalloonForceEquipEvent(player, balloonID);
        singleBalloonForceEquipEvent.callEvent();

        if (singleBalloonForceEquipEvent.isCancelled()) return false;

        SingleBalloon.checkBalloonRemovalOrAdd(player.getPlayer(), balloonID);
        String balloonName = messageTranslations.getString(ConfigConfiguration.SINGLE_BALLOON_SECTION + balloonID + ".name");
        Component equippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("equipped", balloonName));
        player.sendMessage(equippedMessage);
        return false;
    }
}
