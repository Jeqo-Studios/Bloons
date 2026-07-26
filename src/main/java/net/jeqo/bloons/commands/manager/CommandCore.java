package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.commands.*;
import net.jeqo.bloons.commands.manager.types.CommandAccess;
import net.jeqo.bloons.configuration.PluginConfiguration;
import net.jeqo.bloons.gui.menus.BalloonMenu;
import net.jeqo.bloons.item.BalloonItemFactory;
import net.jeqo.bloons.logger.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

/**
 * Handles the core functionality of commands and their restrictive access
 */
@Getter
public class CommandCore implements CommandExecutor {
    private final ArrayList<Command> commands;
    private final JavaPlugin plugin;

    /**
     *                          Creates a new instance of the command core
     * @param providedPlugin    The plugin instance, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandCore(JavaPlugin providedPlugin) {
        this.plugin = providedPlugin;
        this.commands = new ArrayList<>();

        // Add any commands you want registered here
        addCommand(new CommandEquip(this.getPlugin()));
        addCommand(new CommandForceEquip(this.getPlugin()));
        addCommand(new CommandForceUnequip(this.getPlugin()));
        addCommand(new CommandReload(this.getPlugin()));
        addCommand(new CommandUnequip(this.getPlugin()));

        // Register all commands staged
        registerCommands();

        Objects.requireNonNull(this.getPlugin().getCommand(PluginConfiguration.COMMAND_BASE)).setTabCompleter(new CommandTabCompleter());
    }

    /**
     * Registers all commands in the commands list
     */
    public void registerCommands() {
        Objects.requireNonNull(this.getPlugin().getCommand(PluginConfiguration.COMMAND_BASE)).setExecutor(this);
    }

    /**
     *                      Gets a commands description by its alias
     * @param commandAlias  The alias of the command, type java.lang.String
     * @return              The description of the command, type java.lang.String
     */
    public String getCommandDescription(String commandAlias) {
        for (Command command : this.getCommands()) {
            if (command.getCommandAliases().contains(commandAlias)) {
                return command.getCommandDescription();
            }
        }
        return null;
    }

    /**
     *                  Adds a command to the commands list
     * @param command   The command to add, type net.jeqo.bloons.commands.manager.Command
     */
    public void addCommand(Command command) {
        this.getCommands().add(command);
    }

    /**
     *                  Executes the command
     * @param sender    Source of the command, type org.bukkit.command.CommandSender
     * @param command   Command which was executed, type org.bukkit.command.Command
     * @param label     Alias of the command which was used, type java.lang.String
     * @param args      Passed command arguments, type java.lang.String[]
     * @return          Whether the command was executed successfully, type boolean
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player player)) return false;

            if (!player.hasPermission("bloons.menu")) {
                String noPermission = Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("no-permission");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));
                return true;
            }

            ArrayList<ItemStack> items = buildMenuItems(player);
            if (items == null) {
                Logger.logError(ChatColor.translateAlternateColorCodes('&', Bloons.getConfigurationManager().getConfigString("no-balloons-registered")));
                return false;
            }
            new BalloonMenu(items, Bloons.getConfigurationManager().getConfigString("menu-title"), player);
            return true;
        }


        // Define what a subcommand really is
        String subcommand = args[0].toLowerCase();
        String[] subcommandArgs = Arrays.copyOfRange(args, 1, args.length);

        // Loop over every command registered, check the permission, and execute the command
        for (Command currentCommand : getCommands()) {
            if (currentCommand.getCommandAliases().contains(subcommand)) {
                // Check if the sender has the permission to execute the command
                if (!meetsRequirements(currentCommand, sender)) {
                    sender.sendMessage(Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("no-permission"));
                    return false;
                }

                // Check if the command is disabled
                if (currentCommand.getRequiredAccess() == CommandAccess.DISABLED) {
                    sender.sendMessage(Bloons.getConfigurationManager().getConfigString("prefix") + Bloons.getConfigurationManager().getConfigString("command-disabled"));
                    return false;
                }

                // Execute the command
                try {
                    currentCommand.execute(sender, subcommandArgs);
                } catch (Exception ignored) {
                }
                return true;
            }
        }

        // If the command isn't here, show the usage menu
        usage(sender);
        return false;
    }

    /**
     *                  Checks if the player sending the command meets the requirements to execute the command
     * @param command   The command to check, type net.jeqo.bloons.commands.manager.Command
     * @param sender    The sender of the command, type org.bukkit.command.CommandSender
     * @return          Whether the user meets the requirements, type boolean
     */
    public boolean meetsRequirements(Command command, CommandSender sender) {
        return command.hasRequirement(sender, command.getRequiredPermission());
    }

    /**
     *                              Checks if we should add the balloon to the menu
     * @param player                The player to check, type org.bukkit.entity.Player
     * @param singleBalloonType     The key of the balloon, type java.lang.String
     * @return                      Whether we should add the balloon to the menu, type boolean
     */
    private boolean shouldAddSingleBalloon(Player player, SingleBalloonType singleBalloonType) {
        if (Bloons.getConfigurationManager().getConfigString("hide-balloons-without-permission").equalsIgnoreCase("true")) {
            if (singleBalloonType.getPermission() == null) return true;

            return player.hasPermission(singleBalloonType.getPermission());
        }
        return true;
    }

    /**
     *                              Checks if we should add the multipart balloon to the menu
     * @param player                The player to check, type org.bukkit.entity.Player
     * @param multipartBalloonType  The key of the balloon, type java.lang.String
     * @return                      Whether we should add the balloon to the menu, type boolean
     */
    private boolean shouldAddMultipartBalloon(Player player, MultipartBalloonType multipartBalloonType) {
        if (Bloons.getConfigurationManager().getConfigString("hide-balloons-without-permission").equalsIgnoreCase("true")) {
            if (multipartBalloonType.getPermission() == null) return true;

            return player.hasPermission(multipartBalloonType.getPermission());
        }
        return true;
    }

    private ArrayList<ItemStack> buildMenuItems(Player player) {
        ArrayList<SingleBalloonType> singleBalloonTypes = Bloons.getBalloonCore().getSingleBalloonTypes();
        ArrayList<MultipartBalloonType> multipartBalloonTypes = Bloons.getBalloonCore().getMultipartBalloonTypes();

        if (singleBalloonTypes == null && multipartBalloonTypes == null) return null;

        ArrayList<ItemStack> items = new ArrayList<>();
        addSingleBalloonItems(player, items, singleBalloonTypes);
        addMultipartBalloonItems(player, items, multipartBalloonTypes);
        return items;
    }

    private void addSingleBalloonItems(Player player, List<ItemStack> items, List<SingleBalloonType> balloonTypes) {
        if (balloonTypes == null) return;

        for (SingleBalloonType singleBalloon : balloonTypes) {
            if (singleBalloon == null || !shouldAddSingleBalloon(player, singleBalloon)) continue;

            ItemStack item = BalloonItemFactory.createSingleMenuItem(singleBalloon);
            if (item != null) {
                items.add(item);
            }
        }
    }

    private void addMultipartBalloonItems(Player player, List<ItemStack> items, List<MultipartBalloonType> balloonTypes) {
        if (balloonTypes == null) return;

        for (MultipartBalloonType multipartBalloon : balloonTypes) {
            if (multipartBalloon == null || !shouldAddMultipartBalloon(player, multipartBalloon)) continue;

            ItemStack item = BalloonItemFactory.createMultipartMenuItem(multipartBalloon);
            if (item != null) {
                items.add(item);
            }
        }
    }
}
