package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.commands.manager.types.CommandAccess;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * The base class for all commands within the plugin
 */
@Getter @Setter
public abstract class Command {

    protected JavaPlugin plugin;

    private CommandPermission requiredPermission;
    private CommandAccess requiredAccess = CommandAccess.ENABLED;
    private String commandSyntax;
    public String commandDescription;
    private ArrayList<String> commandAliases = new ArrayList<>();

    /**
     *                          Creates a new command instance
     * @param providedPlugin    The plugin instance, type org.bukkit.plugin.java.JavaPlugin
     */
    public Command(JavaPlugin providedPlugin) {
        this.setPlugin(providedPlugin);
    }

    /**
     *                  Adds alias commands to the current command, needed especially for the primary
     *                  command name.
     *                  Note: All aliases added here must be added to the plugin.yml file
     * @param alias     The alias to add to the command, type java.lang.String
     */
    public void addCommandAlias(String alias) {
        this.getCommandAliases().add(alias);
    }

    /**
     *                      The functionality run on the successful execution of a command
     * @param sender        The sender of the command, type org.bukkit.command.CommandSender
     * @param args          The arguments of the command, type java.lang.String[]
     * @return              Whether the command was executed successfully, type boolean
     * @throws Exception    If an error occurs during command execution, type java.lang.Exception
     */
    public abstract boolean execute(CommandSender sender, String[] args) throws Exception;

    /**
     *                      Checks if the command meets the requirements to be executed
     * @param sender        The sender of the command, type org.bukkit.command.CommandSender
     * @param permission    The permission required to execute the command, type net.jeqo.bloons.commands.manager.types.CommandPermission
     * @return              Whether the command meets the requirements, type boolean
     */
    public boolean hasRequirement(CommandSender sender, CommandPermission permission) {
        if (sender instanceof Player) {
            return sender.hasPermission(permission.getPermission());
        }

        return true;
    }
}
