package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.commands.support.BalloonColorOverrideParser;
import net.jeqo.bloons.commands.support.BalloonColorOverrides;
import net.jeqo.bloons.commands.support.BalloonEquipService;
import net.jeqo.bloons.commands.support.BalloonSelection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

/**
 * A command used to force equip a balloon to the specified player
 */
public class CommandForceEquip extends Command {

    /**
     *                  Constructor for the CommandForceEquip class
     * @param plugin    The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandForceEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("fequip");
        this.setCommandDescription("Equip a balloon to a player");
        this.setCommandSyntax("/bloons fequip <player> <balloon> [#HEAD] [#BODY] [#TAIL]  (single balloons still accept \\[#RRGGBB\\] as before)");
        this.setRequiredPermission(CommandPermission.FORCE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            usage(sender);
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            String playerNotFoundMessage = Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("player-not-found");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', playerNotFoundMessage));
            return false;
        }

        BalloonSelection selection = BalloonSelection.resolve(args[1]);
        BalloonColorOverrides overrides = BalloonColorOverrideParser.parse(sender, args, 2, selection);
        if (overrides == null) {
            return false;
        }

        if (Bloons.getBalloonCore().containsSingleBalloon(args[1]) && Bloons.getBalloonCore().containsMultipartBalloon(args[1])) {
            String balloonNotFoundMessage = Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("balloon-not-found");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', balloonNotFoundMessage));
            return false;
        }

        String equippedBalloonName = BalloonEquipService.equip(player, selection, overrides);
        String equippedMessage = Bloons.getConfigurationManager().getConfigString("prefix")
                + String.format(Bloons.getConfigurationManager().getConfigString("equipped"), equippedBalloonName);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', equippedMessage));
        return false;
    }
}
