package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.*;
import net.jeqo.bloons.commands.manager.enums.CommandAccess;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

public class CommandManager implements CommandExecutor {
    @Getter
    private final ArrayList<Command> commands;
    private final JavaPlugin plugin;

    public CommandManager(JavaPlugin providedPlugin) {
        plugin = providedPlugin;
        commands = new ArrayList<>();

        addCommand(new CommandEquip(plugin));
        addCommand(new CommandForceEquip(plugin));
        addCommand(new CommandForceUnequip(plugin));
        addCommand(new CommandReload(plugin));
        addCommand(new CommandUnequip(plugin));

        registerCommands();
    }

    /**
     * Registers all commands in the commands list
     */
    public void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("bloons")).setExecutor(this);
    }

    /**
     * Adds a command to the commands list
     * @param c The command to add
     */
    public void addCommand(Command c) {
        commands.add(c);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            usage(sender);
            return false;
        }

        // Define what a subcommand really is
        String subcommand = args[0].toLowerCase();
        String[] subcommandArgs = Arrays.copyOfRange(args, 1, args.length);

        boolean commandMatched = false;
        for (Command c : getCommands()) {
            if (c.getCommandAliases().contains(subcommand)) {
                if (!meetsRequirements(c, sender)) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                    return false;
                }

                if (c.getRequiredAccess() == CommandAccess.DISABLED) {
                    sender.sendMessage(ChatColor.RED + "This command is currently disabled.");
                    return false;
                }

                try {
                    c.execute(sender, subcommandArgs);
                } catch (Exception ignored) {
                }
                commandMatched = true;
                return true;
            }
        }
        if (!commandMatched) {
            usage(sender);
            return false;
        }
        return false;
    }

    /**
     * Checks if the player sending the command meets the requirements to execute the command
     * @param c The command to check
     * @param s The sender of the command
     * @return Whether the user meets the requirements
     */
    public boolean meetsRequirements(Command c, CommandSender s) {
        return c.hasRequirement(s, c.getRequiredPermission());
    }
}
