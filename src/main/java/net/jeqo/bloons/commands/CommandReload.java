package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.types.CommandPermission;
import net.jeqo.bloons.events.general.BloonsConfigReloadEvent;
import net.jeqo.bloons.utils.LanguageManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
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

        // Trigger a custom event upon the reload of the config
        BloonsConfigReloadEvent bloonsConfigReloadEvent = new BloonsConfigReloadEvent();
        bloonsConfigReloadEvent.callEvent();

        if (bloonsConfigReloadEvent.isCancelled()) return false;

        // Reload the main config.yml and its defaults
        Bloons.getInstance().reloadConfig();
        Bloons.getInstance().getConfig().options().copyDefaults();
        Bloons.getInstance().saveDefaultConfig();

        // Refresh balloons and their configurations from their respective files
        Bloons.getBalloonCore().initialize();

        Component configReloadedMessage = messageTranslations.getSerializedString(LanguageManagement.getMessage("prefix"), LanguageManagement.getMessage("config-reloaded"));
        sender.sendMessage(configReloadedMessage);

        return false;
    }
}
