package net.jeqo.bloons.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.jeqo.bloons.Bloons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class BalloonCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player;
        String str1;
        BalloonRunner balloonRunner1;
        String balloonId;
        BalloonRunner runner;
        if (args.length < 1) {
            usage(sender);
            return true;
        }

        Bloons plugin = Bloons.getInstance();

        switch (args[0]) {
            case "menu":
                if (sender instanceof Player) {
                    player = (Player) sender;

                    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    for (String key : Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("balloons")).getKeys(false)) {

                        ConfigurationSection keySection = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("balloons")).getConfigurationSection(key);

                        assert keySection != null;
                        ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")))));
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        if (Bloons.config("balloons." + key + ".lore") != null) {
                            List<String> lore = keySection.getStringList("lore");
                            for (int i = 0; i < lore.size(); i++) {
                                lore.set(i, Utils.hex(lore.get(i)));
                            }
                            meta.setLore(lore);
                        }
                        meta.setDisplayName(Utils.hex(keySection.getString("name")));
                        meta.setCustomModelData(keySection.getInt("custom-model-data"));
                        item.setItemMeta(meta);

                        items.add(item);
                    }

                    new ScrollerInventory(items, Bloons.config("menu-title"), player);
                    return true;
                } else {
                    sender.sendMessage("Only players may execute this command!");
                    return true;
                }
            case "equip":
                if (args.length < 2) {
                    usage(sender);
                    return true;
                }
                if (sender instanceof Player) {
                    player = (Player)sender;
                }
                else { sender.sendMessage("Only players may execute this command!");
                    return true;
                }
                str1 = args[1];

                if (!plugin.getConfig().contains("balloons." + str1)) {
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("balloon-not-found"));
                    return true;
                }

                if (!player.hasPermission(plugin.getConfig().getString("balloons." + str1 + ".permission", "balloons." + str1))) {
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                    return true;
                }

                Utils.checkBalloonRemovalOrAdd(player, str1);
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", str1));
                return true;


            case "unequip": if (sender instanceof Player) {
                player = (Player) sender;
            } else { sender.sendMessage("Only players may execute this command!");
                return true;
            }
                balloonRunner1 = (BalloonRunner) Bloons.playerBalloons.get(player.getUniqueId());
                if (balloonRunner1 == null) {
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                    return true;
                }
                Utils.removeBalloon(player, balloonRunner1);
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
                return true;


            case "fequip": if (args.length < 3) {
                usage(sender);
                return true;
            }
                if (!sender.hasPermission("balloon.force")) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                    return true;
                }
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("player-not-found"));
                    return true;
                }
                balloonId = args[2];
                if (!plugin.getConfig().contains("balloons." + balloonId)) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("balloon-not-found"));
                    return true;
                }
                Utils.checkBalloonRemovalOrAdd(player.getPlayer(), balloonId);
                sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonId));
                return true;


            case "funequip": if (!sender.hasPermission("balloon.force")) {
                sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                return true;
            }
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("player-not-found"));
                    return true;
                }
                runner = (BalloonRunner) Bloons.playerBalloons.get(player.getUniqueId());
                if (runner == null) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                    return true;
                }
                Utils.removeBalloon(player, runner);
                sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
                return true;


            case "reload": if (!sender.hasPermission("balloon.reload")) {
                sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                return true;
            }
                Bloons.getInstance().reloadConfig();
                sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("config-reloaded"));
                return true;
        }
        usage(sender);
        return true;
    }
    void usage(CommandSender sender) {
        sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("usage"));
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("bloons.reload")) {
            if (args.length == 3) {
                return null;
                }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("unequip") || args[0].equalsIgnoreCase("funequip")) {
                    return null;
                } else {
                    return Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("balloons")).getKeys(false).stream().toList();
                }
            } else if (args.length == 1) {
                return List.of("equip", "unequip", "fequip", "funequip", "reload");
            }
            return Collections.emptyList();
        } else {
            if (args.length == 3) {
                return List.of("");
            }
            if (args.length == 2) {
                return Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("balloons")).getKeys(false).stream().toList();
            }
            return List.of("equip", "unequip");
        }
    }
}