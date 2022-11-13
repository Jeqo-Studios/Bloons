package net.jeqo.bloons.data;

import net.jeqo.bloons.Bloons;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
                item.setItemMeta(meta);

                items.add(item);
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
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
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
        sender.sendMessage(Utils.hex("   #ff00cc/#dd09c4b#bb11bbl#991ab3o#7722aao#552ba2n#333399s &7- Open the balloon menu"));
        sender.sendMessage(Utils.hex("   #ff00cc/#f503c9b#eb05c7l#e008c4o#d60ac2o#cc0dbfn#c20fbds #b812bae#ad14b8q#a317b5u#991ab3i#8f1cb0p #851fad<#7a21abb#7024a8a#6626a6l#5c29a3l#522ba1o#472e9eo#3d309cn#333399> &7- Equip a balloon"));
        sender.sendMessage(Utils.hex("   #ff00cc/#ef04c8b#e008c4l#d00cc0o#c010bco#b114b8n#a118b4s #911bb1u#811fadn#7223a9e#6227a5q#522ba1u#432f9di#333399p &7- Unequip a balloon"));
        sender.sendMessage("");
        sender.sendMessage(Utils.hex("   #ff00ccB#f103c9l#e407c5o#d60ac2o#c90eben#bb11bbs #ad14b81#a018b4.#921bb10#851fad.#7722aa0#6925a7-#5c29a3B#4e2ca0E#41309cT#333399A &7- &fMade by Jeqo"));
        sender.sendMessage("");
    }

    String bloonsPrefix = Utils.hex("#ff00cc[#e207c5B#c50fbdl#a816b6o#8a1dafo#6d24a8n#502ca0s#333399] &r");
}