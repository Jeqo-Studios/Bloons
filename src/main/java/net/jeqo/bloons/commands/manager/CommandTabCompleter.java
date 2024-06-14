package net.jeqo.bloons.commands.manager;

import net.jeqo.bloons.Bloons;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandTabCompleter implements TabCompleter {
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("bloons.reload")) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("fequip")) {
                    List<String> singleBalloons = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("single-balloons")).getKeys(false).stream().toList();
                    List<String> multipartBalloons = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("multipart-balloons")).getKeys(false).stream().toList();
                    return List.of(singleBalloons, multipartBalloons).stream().flatMap(List::stream).toList();
                } else {
                    return List.of("");
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("unequip")) {
                    return List.of("");
                } else if (args[0].equalsIgnoreCase("funequip")) {
                    return null;
                } else if (args[0].equalsIgnoreCase("equip")) {
                    List<String> singleBalloons = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("single-balloons")).getKeys(false).stream().toList();
                    List<String> multipartBalloons = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("multipart-balloons")).getKeys(false).stream().toList();
                    return List.of(singleBalloons, multipartBalloons).stream().flatMap(List::stream).toList();
                } else if (args[0].equalsIgnoreCase("fequip")) {
                    return null;
                }
            } else if (args.length == 1) {
                return List.of("equip", "unequip", "fequip", "funequip", "reload", "rl");
            }
            return Collections.emptyList();


        } else {

            if (args.length == 3) {
                return List.of("");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("unequip")) {
                    return List.of("");
                }
                List<String> singleBalloons = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("single-balloons")).getKeys(false).stream().toList();
                List<String> multipartBalloons = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("multipart-balloons")).getKeys(false).stream().toList();
                return List.of(singleBalloons, multipartBalloons).stream().flatMap(List::stream).toList();
            }
            return List.of("equip", "unequip");
        }
    }
}
