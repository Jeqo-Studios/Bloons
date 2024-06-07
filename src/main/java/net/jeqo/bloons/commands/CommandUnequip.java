package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.utils.BalloonManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandUnequip extends Command {

    public CommandUnequip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("unequip");
        this.setCommandDescription("Equips a balloon to you");
        this.setCommandSyntax("/bloons unequip <balloon>");
        this.setRequiredPermission(CommandPermission.UNEQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return false;

        SingleBalloon singleBalloon = Bloons.getPlayerBalloons().get(player.getUniqueId());
        MessageTranslations messageTranslations = new MessageTranslations(this.plugin);

        if (singleBalloon == null) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            Component notEquippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("not-equipped"));
            player.sendMessage(notEquippedMessage);
            return false;
        }

        BalloonManagement.removeBalloon(player, singleBalloon);
        Component unequipSuccessfulMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("unequipped"));
        player.sendMessage(unequipSuccessfulMessage);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
        return false;
    }
}
