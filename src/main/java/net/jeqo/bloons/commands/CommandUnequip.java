package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonUnequipEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonUnequipEvent;
import net.jeqo.bloons.utils.LanguageManagement;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.jeqo.bloons.utils.management.MultipartBalloonManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandUnequip extends Command {

    public CommandUnequip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("unequip");
        this.setCommandDescription("Unequip a balloon");
        this.setCommandSyntax("/bloons unequip <balloon>");
        this.setRequiredPermission(CommandPermission.UNEQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // If the sender isn't a player, return out of the command
        if (!(sender instanceof Player player)) return false;

        SingleBalloon singleBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
        MultipartBalloon multipartBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());

        // If the player doesn't have any balloons equipped, send a message to the player
        if (singleBalloon == null && multipartBalloon == null) {
            Component notEquippedMessage = messageTranslations.getSerializedString(LanguageManagement.getMessage("prefix"), LanguageManagement.getMessage("not-equipped"));
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
            MultipartBalloonUnequipEvent multipartBalloonEquipEvent = new MultipartBalloonUnequipEvent(player, multipartBalloon);
            multipartBalloonEquipEvent.callEvent();

            if (multipartBalloonEquipEvent.isCancelled()) return false;

            multipartBalloon.destroy();
            MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
        }

        Component unequipSuccessfulMessage = messageTranslations.getSerializedString(LanguageManagement.getMessage("prefix"), LanguageManagement.getMessage("unequipped"));
        player.sendMessage(unequipSuccessfulMessage);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
        return false;
    }
}
