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

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

public class CommandEquip extends Command {

    public CommandEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("equip");
        this.setCommandDescription("Equips a balloon to you");
        this.setCommandSyntax("/bloons equip <balloon>");
        this.setRequiredPermission(CommandPermission.EQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            usage(sender);
        }

        Player player = (Player) sender;
        String str1 = args[0];

        if (!plugin.getConfig().contains("balloons." + str1)) {
            player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("balloon-not-found"));
            return false;
        }

        if (!player.hasPermission(plugin.getConfig().getString("balloons." + str1 + ".permission", "balloons." + str1))) {
            player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
            return false;
        }

        Utils.removeBalloon(player, Bloons.playerBalloons.get(player.getUniqueId()));
        BalloonOwner.checkBalloonRemovalOrAdd(player, str1);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        String balloonName = Bloons.getString("balloons." + str1 + ".name");
        player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonName));
        return false;
    }
}
