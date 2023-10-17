package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.commands.manager.enums.CommandAccess;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public abstract class Command {

    protected JavaPlugin plugin;

    @Getter @Setter
    private CommandPermission requiredPermission;
    @Getter @Setter
    private CommandAccess requiredAccess = CommandAccess.ENABLED;
    @Getter @Setter
    private String commandSyntax;
    @Getter @Setter
    private String commandDescription;
    @Getter
    private final ArrayList<String> commandAliases = new ArrayList<>();

    public Command(JavaPlugin providedPlugin) {
        plugin = providedPlugin;
    }

    /**
     * Adds alias commands to the current command
     * Note: All aliases added here must be added to the plugin.yml file
     * @param alias The alias to add to the command
     */
    public void addCommandAlias(String alias) {
        commandAliases.add(alias);
    }

    public abstract boolean execute(CommandSender sender, String[] args) throws Exception;

    /**
     * Checks if the command meets the requirements to be executed
     * @param s The sender of the command
     * @param perm The permission required to execute the command
     * @return Whether or not the command meets the requirements
     */
    public boolean hasRequirement(CommandSender s, CommandPermission perm) {
        switch (perm) {
            case EQUIP -> {
                if (s instanceof Player) {
                    if (!s.hasPermission("bloons.equip")) {
                        return false;
                    }
                }
            }
            case UNEQUIP -> {
                if (s instanceof Player) {
                    if (!s.hasPermission("bloons.unequip")) {
                        return false;
                    }
                }
            }
            case FORCE -> {
                if (s instanceof Player) {
                    if (!s.hasPermission("bloons.force")) {
                        return false;
                    }
                }
            }
            case RELOAD -> {
                if (s instanceof Player) {
                    if (!s.hasPermission("bloons.reload")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
