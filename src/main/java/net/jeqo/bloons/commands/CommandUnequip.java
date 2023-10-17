package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandUnequip extends Command {

    public CommandUnequip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("equip");
        this.setCommandDescription("Equips a balloon to you");
        this.setCommandSyntax("/bloons unequip <balloon>");
        this.setRequiredPermission(CommandPermission.UNEQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        BalloonOwner balloonOwner1 = Bloons.playerBalloons.get(player.getUniqueId());
        if (balloonOwner1 == null) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
            return false;
        }
        Utils.removeBalloon(player, balloonOwner1);
        player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
        return false;
    }
}
