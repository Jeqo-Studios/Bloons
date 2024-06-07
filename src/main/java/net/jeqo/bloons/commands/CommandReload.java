package net.jeqo.bloons.commands;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.commands.manager.Command;
import net.jeqo.bloons.commands.manager.enums.CommandPermission;
import net.jeqo.bloons.events.balloon.general.BloonsConfigReloadEvent;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandReload extends Command {

    public CommandReload(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("reload");
        this.addCommandAlias("rl");
        this.setCommandDescription("Reloads the bloons config");
        this.setCommandSyntax("/bloons reload");
        this.setRequiredPermission(CommandPermission.RELOAD);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessageTranslations messageTranslations = new MessageTranslations(this.plugin);

        BloonsConfigReloadEvent bloonsConfigReloadEvent = new BloonsConfigReloadEvent();
        bloonsConfigReloadEvent.callEvent();

        if (bloonsConfigReloadEvent.isCancelled()) return false;

        Bloons.getInstance().reloadConfig();
        Component configReloadedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("config-reloaded"));
        sender.sendMessage(configReloadedMessage);

        return false;
    }
}
