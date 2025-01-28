package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.message.MessageTranslations;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A command used to reload the Bloons configurations
 */
public class CommandReload extends Command {

    /**
     *                 Constructor for the CommandReload class
     * @param plugin   The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandReload(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("reload");
        this.addCommandAlias("rl");
        this.setCommandDescription("Reload the Bloons config");
        this.setCommandSyntax("/bloons reload");
        this.setRequiredPermission(CommandPermission.RELOAD);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessageTranslations messageTranslations = new MessageTranslations(this.getPlugin());

        // Reload the main config.yml and its defaults
        Bloons.getInstance().reloadConfig();
        Bloons.getInstance().getConfig().options().copyDefaults();
        Bloons.getInstance().saveDefaultConfig();

        // Refresh balloons and their configurations from their respective files
        Bloons.getBalloonCore().initialize();

        String configReloadedMessage = Languages.getMessage("prefix") + Languages.getMessage("config-reloaded");
        sender.sendMessage(configReloadedMessage);

        return false;
    }
}
