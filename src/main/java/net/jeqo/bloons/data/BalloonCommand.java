package net.jeqo.bloons.data;

import net.jeqo.bloons.Bloons;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.jeqo.bloons.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

public class BalloonCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player;
        String str1;
        BalloonOwner balloonOwner1;
        String balloonId;
        BalloonOwner owner;
        Bloons plugin = Bloons.getInstance();
        if (args.length < 1) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage("Only players may execute this command!");
                return true;
            }
            if (!sender.hasPermission("bloons.menu")) {
                sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                return true;
            }
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            for (String key : Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("balloons")).getKeys(false)) {

                ConfigurationSection keySection = Objects.requireNonNull(Bloons.getInstance().getConfig().getConfigurationSection("balloons")).getConfigurationSection(key);



                if (Bloons.getString("hide-balloons-without-permission").equalsIgnoreCase("true")) {
                    if (player.hasPermission(Bloons.getString("balloons." + key + ".permission"))) {
                        assert keySection != null;
                        ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")))));
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        meta.setLocalizedName(Bloons.getString("balloons." + key + ".id"));
                        if (Bloons.getString("balloons." + key + ".lore") != null) {
                            List<String> lore = keySection.getStringList("lore");
                            for (int i = 0; i < lore.size(); i++) {
                                lore.set(i, Utils.hex(lore.get(i)));
                            }
                            meta.setLore(lore);
                        }
                        meta.setDisplayName(Utils.hex(keySection.getString("name")));
                        meta.setCustomModelData(keySection.getInt("custom-model-data"));
                        if (Bloons.getString("balloons." + key + ".color") != null) {
                            if (!Bloons.getString("balloons." + key + ".color").equalsIgnoreCase("potion")) {
                                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
                                leatherArmorMeta.setColor(Utils.hexToColor(keySection.getString("color")));
                            } else {
                                Utils.warn("The color of the balloon " + key + " is set, but the material is not a leather item!");
                            }
                        }
                        item.setItemMeta(meta);

                        items.add(item);
                    }
                }



                else {
                    assert keySection != null;
                    ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")))));
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    meta.setLocalizedName(Bloons.getString("balloons." + key + ".id"));
                    if (Bloons.getString("balloons." + key + ".lore") != null) {
                        List<String> lore = keySection.getStringList("lore");
                        for (int i = 0; i < lore.size(); i++) {
                            lore.set(i, Utils.hex(lore.get(i)));
                        }
                        meta.setLore(lore);
                    }
                    meta.setDisplayName(Utils.hex(keySection.getString("name")));
                    meta.setCustomModelData(keySection.getInt("custom-model-data"));
                    if (Bloons.getString("balloons." + key + ".color") != null) {
                        if (!Bloons.getString("balloons." + key + ".color").equalsIgnoreCase("potion")) {
                            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
                            leatherArmorMeta.setColor(Utils.hexToColor(keySection.getString("color")));
                        } else {
                            Utils.warn("The color of the balloon " + key + " is set, but the material is not a leather item!");
                        }
                    }
                    item.setItemMeta(meta);

                    items.add(item);
                }



            }
                new BalloonMenu(items, Bloons.getString("menu-title"), player);
                return true;
        }





        switch (args[0]) {
            case "equip":
                if (args.length < 2) {
                    usage(sender);
                    return true;
                }
                if (!sender.hasPermission("bloons.equip")) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                    return true;
                }
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.sendMessage("Only players may execute this command!");
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

                Utils.removeBalloon(player, (BalloonOwner) Bloons.playerBalloons.get(player.getUniqueId()));
                BalloonOwner.checkBalloonRemovalOrAdd(player, str1);
                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                String balloonName = Bloons.getString("balloons." + str1 + ".name");
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonName));
                return true;


            case "unequip":
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.sendMessage("Only players may execute this command!");
                    return true;
                }
                if (!sender.hasPermission("bloons.unequip")) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                    return true;
                }
                balloonOwner1 = (BalloonOwner) Bloons.playerBalloons.get(player.getUniqueId());
                if (balloonOwner1 == null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                    return true;
                }
                Utils.removeBalloon(player, balloonOwner1);
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
                return true;


            case "fequip":
                if (args.length < 3) {
                    usage(sender);
                    return true;
                }
                if (!sender.hasPermission("bloons.force")) {
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

                BalloonOwner.checkBalloonRemovalOrAdd(player.getPlayer(), balloonId);
                String balloonName2 = Bloons.getString("balloons." + balloonId + ".name");
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonName2));
                return true;


            case "funequip":
                if (!sender.hasPermission("bloons.force")) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("no-permission"));
                    return true;
                }
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("player-not-found"));
                    return true;
                }
                owner = (BalloonOwner) Bloons.playerBalloons.get(player.getUniqueId());
                if (owner == null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
                    sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                    return true;
                }
                Utils.removeBalloon(player, owner);
                sender.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
                return true;


            case "reload":
            case "rl":
                if (!sender.hasPermission("bloons.reload")) {
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
        sender.sendMessage("");
        if (sender.hasPermission("bloons.menu")) {
            sender.sendMessage(Utils.hex("   #ff00cc/#e30ed5b#c61cddl#aa2be6o#8e39eeo#7147f7n#5555ffs &7- Open the balloon menu"));
        }
        if (sender.hasPermission("bloons.equip")) {
            sender.sendMessage(Utils.hex("   #ff00cc/#f008d1b#e00fd5l#d117dao#c11fdfo#b227e3n#a22ee8s #9336ece#833ef1q#7446f6u#644dfai#5555ffp &7- Equip a balloon"));
        }
        if (sender.hasPermission("bloons.unequip")) {
            sender.sendMessage(Utils.hex("   #ff00cc/#f207d0b#e50dd4l#d814d8o#cb1adco#be21e0n#b127e4s #a32ee7u#9634ebn#893befe#7c41f3q#6f48f7u#624efbi#5555ffp &7- Unequip a balloon"));
        }
        if (sender.hasPermission("bloons.force")) {
            sender.sendMessage(Utils.hex("   #ff00cc/#f107d0b#e30ed5l#d515d9o#c61cddo#b823e1n#aa2be6s #9c32eaf#8e39eee#8040f2q#7147f7u#634efbi#5555ffp &7- Equip a balloon to a player"));
            sender.sendMessage(Utils.hex("   #ff00cc/#f306d0b#e70cd3l#db12d7o#ce18dbo#c21eden#b624e2s #aa2be6f#9e31e9u#9237edn#863df0e#7943f4q#6d49f8u#614ffbi#5555ffp &7- Unequip a balloon from a player"));
        }
        if (sender.hasPermission("bloons.reload")) {
            sender.sendMessage(Utils.hex("   #ff00cc/#f107d0b#e30ed5l#d515d9o#c61cddo#b823e1n#aa2be6s #9c32ear#8e39eee#8040f2l#7147f7o#634efba#5555ffd &7- Reload the Bloons config"));
        }
        sender.sendMessage("");
        sender.sendMessage(Utils.hex("   #ff00ccB#f406cfl#e80bd3o#dd11d6o#d217dan#c61cdds #bb22e01#b028e4.#a42de70#9933eb.#8e39ee1#823ef3-#7744f5B#6c4af8E#604ffcT#5555ffA &7- &fMade by Jeqo"));
        sender.sendMessage("");
    }

    String bloonsPrefix = Utils.hex("#ff00cc[#e207c5B#c50fbdl#a816b6o#8a1dafo#6d24a8n#502ca0s#333399] &r");
}