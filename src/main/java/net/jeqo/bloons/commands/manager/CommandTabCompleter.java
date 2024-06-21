package net.jeqo.bloons.commands.manager;

import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    /**
     *                  Called when a player (or the console) uses the tab key
     * @param sender    Source of the command.  For players tab-completing a
     *                  command inside a command block, this will be the player, not
     *                  the command block. Type org.bukkit.command.CommandSender
     * @param command   Command which was executed, type org.bukkit.command.Command
     * @param label     Alias of the command which was used, type java.lang.String
     * @param args      The arguments passed to the command, including final
     *                  partial argument to be completed, type java.lang.String[]
     * @return          A List of possible completions for the final argument, type java.util.List<java.lang.String> or null
     */
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission(CommandPermission.RELOAD.getPermission())) {
            // If the player has the reload permission and the command has 3 arguments we can assume it's the fequip command
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("fequip")) {
                    // Get the list of all the multipart balloons
                    return getBalloonTabComplete();
                } else {
                    // If the command isn't fequip then return an empty list
                    return List.of("");
                }
            }

            // If the player has the reload permission and the command has 2 arguments we can assume it's the unequip commands and the
            // regular access level equip command
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("unequip")) {
                    // No arguments are needed for the unequip command
                    return List.of("");
                } else if (args[0].equalsIgnoreCase("funequip")) {
                    // No arguments are needed for the funequip command
                    return null;
                } else if (args[0].equalsIgnoreCase("equip")) {
                    // Get the list of all the balloons
                    return getBalloonTabComplete();
                } else if (args[0].equalsIgnoreCase("fequip")) {
                    return null;
                }
            } else if (args.length == 1) {
                // If the player has the reload permission and the command has 1 argument we need to return all command names
                return List.of("equip", "unequip", "fequip", "funequip", "reload", "rl");
            }
            return Collections.emptyList();


        } else {
            if (args.length == 3) {
                // If the player doesn't have the reload permission and the command has 3 arguments we can return nothing
                return List.of("");
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("unequip")) {
                    // No arguments are needed for the unequip command
                    return List.of("");
                }

                // Otherwise, we need to return all the balloons
                return getBalloonTabComplete();
            }

            // If the player isn't an administrator, only show the unequip and equip commands as available
            return List.of("equip", "unequip");
        }
    }

    public List<String> getBalloonTabComplete() {
        List<String> singleBalloons = ConfigConfiguration.getSingleBalloons().stream().map(singleBalloonType -> singleBalloonType.getId().toLowerCase()).toList();
        List<String> multipartBalloons = ConfigConfiguration.getMultipartBalloons().stream().map(multipartBalloonType -> multipartBalloonType.getId().toLowerCase()).toList();
        return List.of(singleBalloons, multipartBalloons).stream().flatMap(List::stream).toList();
    }
}
