package net.jeqo.bloons.commands.manager;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.*;
import net.jeqo.bloons.commands.manager.types.CommandAccess;
import net.jeqo.bloons.configuration.ConfigConfiguration;
import net.jeqo.bloons.configuration.PluginConfiguration;
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

    /**
     *                          Creates a new instance of the command core
     * @param providedPlugin    The plugin instance, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandCore(JavaPlugin providedPlugin) {
        this.plugin = providedPlugin;
        this.commands = new ArrayList<>();
        this.messageTranslations = new MessageTranslations(this.getPlugin());

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
            if (!(sender instanceof Player player)) {
                Component consoleMessage = Component.text("This command can only be executed by a player.").color(NamedTextColor.RED);
                sender.sendMessage(consoleMessage);
                return true;
            }

            if (!player.hasPermission("bloons.menu")) {
                Component noPermission = this.getMessageTranslations().getSerializedString(this.getMessageTranslations().getMessage("prefix"), this.getMessageTranslations().getMessage("no-permission"));
                player.sendMessage(noPermission);
                return true;
            }

            ArrayList<ItemStack> items = new ArrayList<>();
            ConfigurationSection singleBalloonsSection = Bloons.getInstance().getConfig().getConfigurationSection(ConfigConfiguration.SINGLE_BALLOON_SECTION.replace(".", ""));
            ConfigurationSection multipartBalloonsSection = Bloons.getInstance().getConfig().getConfigurationSection(ConfigConfiguration.MULTIPART_BALLOON_SECTION.replace(".", ""));

            if (singleBalloonsSection == null && multipartBalloonsSection == null) return false;

            for (String key : singleBalloonsSection.getKeys(false)) {
                ConfigurationSection keySection = singleBalloonsSection.getConfigurationSection(key);
                if (keySection == null) continue;

                if (shouldAddBalloon(player, key)) {
                    ItemStack item = createBalloonItem(keySection, key);
                    items.add(item);
                }
            }

            for (String key : multipartBalloonsSection.getKeys(false)) {
                ConfigurationSection keySection = multipartBalloonsSection.getConfigurationSection(key);
                if (keySection == null) continue;

                if (shouldAddMultipartBalloon(player, key)) {
                    ItemStack item = createMultipartBalloonItem(keySection, key);
                    items.add(item);
                }
            }

            new BalloonMenu(items, this.getMessageTranslations().getString("menu-title"), player);
            return true;
        }


        // Define what a subcommand really is
        String subcommand = args[0].toLowerCase();
        String[] subcommandArgs = Arrays.copyOfRange(args, 1, args.length);

        for (Command currentCommand : getCommands()) {
            if (currentCommand.getCommandAliases().contains(subcommand)) {
                if (!meetsRequirements(currentCommand, sender)) {
                    sender.sendMessage(this.getMessageTranslations().getSerializedString(this.getMessageTranslations().getMessage("prefix"), this.getMessageTranslations().getMessage("no-permission")));
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
     *                  Checks if the player sending the command meets the requirements to execute the command
     * @param command   The command to check, type net.jeqo.bloons.commands.manager.Command
     * @param sender    The sender of the command, type org.bukkit.command.CommandSender
     * @return          Whether the user meets the requirements, type boolean
     */
    public boolean meetsRequirements(Command command, CommandSender sender) {
        return command.hasRequirement(sender, command.getRequiredPermission());
    }

    /**
     *                  Checks if we should add the balloon to the menu
     * @param player    The player to check, type org.bukkit.entity.Player
     * @param key       The key of the balloon, type java.lang.String
     * @return          Whether we should add the balloon to the menu, type boolean
     */
    private boolean shouldAddBalloon(Player player, String key) {
        if (this.getMessageTranslations().getString("hide-balloons-without-permission").equalsIgnoreCase("true")) {
            return player.hasPermission(this.getMessageTranslations().getString(ConfigConfiguration.SINGLE_BALLOON_SECTION + key + ".permission"));
        }
        return true;
    }

    /**
     *                 Checks if we should add the multipart balloon to the menu
     * @param player   The player to check, type org.bukkit.entity.Player
     * @param key      The key of the balloon, type java.lang.String
     * @return         Whether we should add the balloon to the menu, type boolean
     */
    private boolean shouldAddMultipartBalloon(Player player, String key) {
        if (this.getMessageTranslations().getString("hide-balloons-without-permission").equalsIgnoreCase("true")) {
            return player.hasPermission(this.getMessageTranslations().getString(ConfigConfiguration.MULTIPART_BALLOON_SECTION + key + ".permission"));
        }
        return true;
    }

    /**
     *                      Creates an ItemStack for a balloon
     * @param keySection    The configuration section of the balloon, type org.bukkit.configuration.ConfigurationSection
     * @param key           The key of the balloon, type java.lang.String
     * @return              The ItemStack of the balloon, type org.bukkit.inventory.ItemStack
     */
    private ItemStack createBalloonItem(ConfigurationSection keySection, String key) {
        Material material = Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")));
        if (material == null) return null;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        meta.setLocalizedName(this.getMessageTranslations().getString(ConfigConfiguration.SINGLE_BALLOON_SECTION + key + ".id"));
        setBalloonLore(meta, keySection);
        setBalloonDisplayName(meta, keySection);
        meta.setCustomModelData(keySection.getInt("custom-model-data"));
        setBalloonColor(meta, key, keySection);

        item.setItemMeta(meta);
        return item;
    }

    /**
     *                      Creates an ItemStack for a multipart balloon
     * @param keySection    The configuration section of the balloon, type org.bukkit.configuration.ConfigurationSection
     * @param key           The key of the balloon, type java.lang.String
     * @return              The ItemStack of the balloon, type org.bukkit.inventory.ItemStack
     */
    private ItemStack createMultipartBalloonItem(ConfigurationSection keySection, String key) {
        Material material = Material.matchMaterial(Objects.requireNonNull(keySection.getString("head.material")));
        if (material == null) return null;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        meta.setLocalizedName(this.getMessageTranslations().getString(ConfigConfiguration.MULTIPART_BALLOON_SECTION + key + ".id"));
        setBalloonLore(meta, keySection);
        setBalloonDisplayName(meta, keySection);
        meta.setCustomModelData(keySection.getInt("head.custom-model-data"));

        if (keySection.getString("head.color") != null) {
            setBalloonColor(meta, key, keySection);
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     *                      Sets the lore of the balloon
     * @param meta          The ItemMeta of the balloon, type org.bukkit.inventory.meta.ItemMeta
     * @param keySection    The configuration section of the balloon, type org.bukkit.configuration.ConfigurationSection
     */
    private void setBalloonLore(ItemMeta meta, ConfigurationSection keySection) {
        if (keySection.contains("lore")) {
            List<String> lore = keySection.getStringList("lore");
            lore.replaceAll(ColorManagement::fromHex);
            meta.setLore(lore);
        }
    }

    /**
     *                      Sets the display name of the balloon
     * @param meta          The ItemMeta of the balloon, type org.bukkit.inventory.meta.ItemMeta
     * @param keySection    The configuration section of the balloon, type org.bukkit.configuration.ConfigurationSection
     */
    private void setBalloonDisplayName(ItemMeta meta, ConfigurationSection keySection) {
        String name = keySection.getString("name");
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());
        if (name != null) {
            meta.displayName(messageTranslations.getSerializedString(name));
        }
    }

    /**
     *                      Sets the color of the balloon
     * @param meta          The ItemMeta of the balloon, type org.bukkit.inventory.meta.ItemMeta
     * @param key           The key of the balloon, type java.lang.String
     * @param keySection    The configuration section of the balloon, type org.bukkit.configuration.ConfigurationSection
     */
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
