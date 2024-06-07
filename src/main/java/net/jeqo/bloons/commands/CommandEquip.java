package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.events.balloon.SingleBalloonEquipEvent;
import net.jeqo.bloons.utils.BalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
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

        if (!this.getPlugin().getConfig().contains("balloons." + balloonID)) {
            Component balloonNotFoundMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("balloon-not-found"));
            player.sendMessage(balloonNotFoundMessage);
            return false;
        }

        if (!player.hasPermission(this.getPlugin().getConfig().getString("balloons." + balloonID + ".permission", "balloons." + balloonID))) {
            Component noPermissionMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("no-permission"));
            player.sendMessage(noPermissionMessage);
            return false;
        }

        // Call the equip event and check if it's cancelled, if it is, don't spawn the balloon or do anything
        SingleBalloonEquipEvent singleBalloonEquipEvent = new SingleBalloonEquipEvent(player, balloonID);
        singleBalloonEquipEvent.callEvent();

        if (singleBalloonEquipEvent.isCancelled()) return false;

        BalloonManagement.removeBalloon(player, Bloons.getPlayerBalloons().get(player.getUniqueId()));
        SingleBalloon.checkBalloonRemovalOrAdd(player, balloonID);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);

        String balloonName = messageTranslations.getString("balloons." + balloonID + ".name");
        Component equippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("equipped", balloonName));
        player.sendMessage(equippedMessage);

        return false;
    }
}
