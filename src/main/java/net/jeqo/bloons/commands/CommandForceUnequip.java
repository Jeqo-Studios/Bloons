package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.utils.Utils;
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
        if (player == null) {
            sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("player-not-found"));
            return false;
        }

        BalloonOwner owner = Bloons.playerBalloons.get(player.getUniqueId());
        if (owner == null) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
            return false;
        }
        Utils.removeBalloon(player, owner);
        sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
        return false;
    }
}
