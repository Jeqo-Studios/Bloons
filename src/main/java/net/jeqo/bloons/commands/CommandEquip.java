package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.commands.support.BalloonColorOverrideParser;
import net.jeqo.bloons.commands.support.BalloonColorOverrides;
import net.jeqo.bloons.commands.support.BalloonEquipService;
import net.jeqo.bloons.commands.support.BalloonSelection;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.logger.LoggingLevel;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

/**
 * A command used to equip a balloon
 */
public class CommandEquip extends Command {

    /**
     * Constructor for the CommandEquip class
     * @param plugin The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandEquip(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("equip");
        this.setCommandDescription("Equip a balloon");
        this.setCommandSyntax("/bloons equip <balloon> [#HEAD] [#BODY] [#TAIL]  (single balloons still accept \\\\[#RRGGBB\\\\] as before)");
        this.setRequiredPermission(CommandPermission.EQUIP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length < 1) {
            usage(player);
            return false;
        }

        BalloonSelection selection = BalloonSelection.resolve(args[0]);
        BalloonColorOverrides overrides = BalloonColorOverrideParser.parse(player, args, 1, selection);
        if (overrides == null) return false;

        if (selection.singleType() != null && !player.hasPermission(selection.singleType().getPermission())) {
            player.sendMessage(Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("no-permission"));
            return false;
        }

        if (selection.multipartType() != null && !player.hasPermission(selection.multipartType().getPermission())) {
            player.sendMessage(Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("no-permission"));
            return false;
        }

        String equippedBalloonName = BalloonEquipService.equip(player, selection, overrides);
        if (equippedBalloonName == null) {
            Logger.logToPlayer(LoggingLevel.ERROR, player, "The current balloon type is null! Please correct this in the config.");
            return false;
        }

        String equippedMessage = Bloons.getConfigurationManager().getConfigString("prefix")
                + String.format(Bloons.getConfigurationManager().getConfigString("equipped"), equippedBalloonName);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', equippedMessage));
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        return false;
    }
}
