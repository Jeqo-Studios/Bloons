package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.*;
import net.jeqo.bloons.commands.manager.enums.CommandAccess;
import net.jeqo.bloons.gui.menus.BalloonMenu;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.ColorManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.jeqo.bloons.commands.utils.ErrorHandling.usage;

@Getter
public class CommandCore implements CommandExecutor {
    private final ArrayList<Command> commands;
    private final JavaPlugin plugin;
    private final MessageTranslations messageTranslations;

    public CommandCore(JavaPlugin providedPlugin) {
        this.plugin = providedPlugin;
        this.commands = new ArrayList<>();
        this.messageTranslations = new MessageTranslations(this.getPlugin());

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
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players may execute this command!");
                return true;
            }

            if (!player.hasPermission("bloons.menu")) {
                player.sendMessage(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("no-permission"));
                return true;
            }

            ArrayList<ItemStack> items = new ArrayList<>();
            ConfigurationSection balloonsSection = Bloons.getInstance().getConfig().getConfigurationSection("balloons");

            if (balloonsSection != null) {
                for (String key : balloonsSection.getKeys(false)) {
                    ConfigurationSection keySection = balloonsSection.getConfigurationSection(key);
                    if (keySection == null) continue;

                    if (shouldAddBalloon(player, key)) {
                        ItemStack item = createBalloonItem(keySection, key);
                        items.add(item);
                    }
                }
            }

            new BalloonMenu(items, messageTranslations.getString("menu-title"), player);
            return true;
        }


        // Define what a subcommand really is
        String subcommand = args[0].toLowerCase();
        String[] subcommandArgs = Arrays.copyOfRange(args, 1, args.length);

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

    private boolean shouldAddBalloon(Player player, String key) {
        if (messageTranslations.getString("hide-balloons-without-permission").equalsIgnoreCase("true")) {
            return player.hasPermission(messageTranslations.getString("balloons." + key + ".permission"));
        }
        return true;
    }

    private ItemStack createBalloonItem(ConfigurationSection keySection, String key) {
        Material material = Material.matchMaterial(keySection.getString("material"));
        if (material == null) return null;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        meta.setLocalizedName(messageTranslations.getString("balloons." + key + ".id"));
        setBalloonLore(meta, keySection);
        setBalloonDisplayName(meta, keySection);
        meta.setCustomModelData(keySection.getInt("custom-model-data"));
        setBalloonColor(meta, key, keySection);

        item.setItemMeta(meta);
        return item;
    }

    private void setBalloonLore(ItemMeta meta, ConfigurationSection keySection) {
        if (keySection.contains("lore")) {
            List<String> lore = keySection.getStringList("lore");
            lore.replaceAll(ColorManagement::fromHex);
            meta.setLore(lore);
        }
    }

    private void setBalloonDisplayName(ItemMeta meta, ConfigurationSection keySection) {
        String name = keySection.getString("name");
        if (name != null) {
            meta.setDisplayName(ColorManagement.fromHex(name));
        }
    }

    private void setBalloonColor(ItemMeta meta, String key, ConfigurationSection keySection) {
        String color = keySection.getString("color");
        if (color != null && !color.equalsIgnoreCase("potion")) {
            if (meta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) meta).setColor(ColorManagement.hexToColor(color));
            } else {
                Logger.logWarning("The color of the balloon " + key + " is set, but the material is not a leather item!");
            }
        }
    }
}
