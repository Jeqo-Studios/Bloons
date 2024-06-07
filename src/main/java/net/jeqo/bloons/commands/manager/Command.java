package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.commands.manager.enums.CommandAccess;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

@Getter
public abstract class Command {

    protected JavaPlugin plugin;

    @Setter
    private CommandPermission requiredPermission;
    @Setter
    private CommandAccess requiredAccess = CommandAccess.ENABLED;
    @Setter
    private String commandSyntax;
    @Setter
    private String commandDescription;
    private final ArrayList<String> commandAliases = new ArrayList<>();

    public Command(JavaPlugin providedPlugin) {
        this.plugin = providedPlugin;
    }

    /**
     * Adds alias commands to the current command
     * Note: All aliases added here must be added to the plugin.yml file
     * @param alias The alias to add to the command
     */
    public void addCommandAlias(String alias) {
        this.getCommandAliases().add(alias);
    }

    public abstract boolean execute(CommandSender sender, String[] args) throws Exception;

    /**
     * Checks if the command meets the requirements to be executed
     * @param sender The sender of the command
     * @param permission The permission required to execute the command
     * @return Whether the command meets the requirements
     */
    public boolean hasRequirement(CommandSender sender, CommandPermission permission) {
        switch (permission) {
            case EQUIP -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("bloons.equip")) {
                        return false;
                    }
                }
            }
            case UNEQUIP -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("bloons.unequip")) {
                        return false;
                    }
                }
            }
            case FORCE -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("bloons.force")) {
                        return false;
                    }
                }
            }
            case RELOAD -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("bloons.reload")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
