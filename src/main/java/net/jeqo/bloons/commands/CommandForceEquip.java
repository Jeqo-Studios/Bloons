package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.data.BalloonOwner;
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
            System.out.println("args.length < 1");
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("player-not-found"));
            return false;
        }
        String balloonId = args[1];
        if (!plugin.getConfig().contains("balloons." + balloonId)) {
            sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("balloon-not-found"));
            return false;
        }

        BalloonOwner.checkBalloonRemovalOrAdd(player.getPlayer(), balloonId);
        String balloonName2 = Bloons.getString("balloons." + balloonId + ".name");
        player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonName2));
        return false;
    }
}
