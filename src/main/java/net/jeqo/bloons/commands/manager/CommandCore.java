package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import net.jeqo.bloons.commands.*;
import net.jeqo.bloons.commands.manager.enums.CommandAccess;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

@Getter
public class CommandCore implements CommandExecutor {
    private final ArrayList<Command> commands;
    private final JavaPlugin plugin;

    public CommandCore(JavaPlugin providedPlugin) {
        this.plugin = providedPlugin;
        this.commands = new ArrayList<>();

        addCommand(new CommandEquip(this.getPlugin()));
        addCommand(new CommandForceEquip(this.getPlugin()));
        addCommand(new CommandForceUnequip(this.getPlugin()));
        addCommand(new CommandReload(this.getPlugin()));
        addCommand(new CommandUnequip(this.getPlugin()));

        registerCommands();
    }

    /**
     * Registers all commands in the commands list
     */
    public void registerCommands() {
        Objects.requireNonNull(this.getPlugin().getCommand("bloons")).setExecutor(this);
    }

    /**
     * Adds a command to the commands list
     * @param command The command to add
     */
    public void addCommand(Command command) {
        this.getCommands().add(command);
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

        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());

        for (Command currentCommand : getCommands()) {
            if (currentCommand.getCommandAliases().contains(subcommand)) {
                if (!meetsRequirements(currentCommand, sender)) {
                    sender.sendMessage(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("no-permission"));
                    return false;
                }

                if (currentCommand.getRequiredAccess() == CommandAccess.DISABLED) {
                    Component commandDisabledMessage = Component.text("This command is currently disabled.").color(NamedTextColor.RED);
                    sender.sendMessage(commandDisabledMessage);
                    return false;
                }

                try {
                    currentCommand.execute(sender, subcommandArgs);
                } catch (Exception ignored) {
                }
                return true;
            }
        }

        usage(sender);
        return false;
    }

    /**
     * Checks if the player sending the command meets the requirements to execute the command
     * @param command The command to check
     * @param sender The sender of the command
     * @return Whether the user meets the requirements
     */
    public boolean meetsRequirements(Command command, CommandSender sender) {
        return command.hasRequirement(sender, command.getRequiredPermission());
    }
}
