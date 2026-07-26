package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.message.Languages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A command used to reload the Bloons configurations
 */
public class CommandReload extends Command {

    /**
     *                 Constructor for the CommandReload class
     * @param plugin   The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandReload(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("reload");
        this.addCommandAlias("rl");
        this.setCommandDescription("Reload the Bloons config");
        this.setCommandSyntax("/bloons reload");
        this.setRequiredPermission(CommandPermission.RELOAD);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Bloons.getConfigurationManager().reload();

        Bloons.getBalloonCore().initialize();

        String configReloadedMessage = Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("config-reloaded");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configReloadedMessage));

        return false;
    }
}
