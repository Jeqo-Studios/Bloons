package net.jeqo.bloons.commands.utils;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.PluginConfiguration;
import net.jeqo.bloons.message.MessageTranslations;
import org.bukkit.command.CommandSender;

/**
 * A class used to handle errors on commands and to send the usage message to the sender
 */
public class ErrorHandling {

    /**
     *                  Sends a message to the sender that the command was not found.
     *                  Based on the commands the player has access to, it will also send a list of available commands
     * @param sender    The sender of the command, type org.bukkit.command.CommandSender
     */
    public static void usage(CommandSender sender) {
        // MessageTranslations will need to adapt for plain text
        MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());

        sender.sendMessage(""); // Blank line for spacing
        if (sender.hasPermission("bloons.menu")) {
            String menuMessage = "   §d/bloons §7- Open the balloon menu";
            sender.sendMessage(menuMessage);
        }

        if (sender.hasPermission("bloons.equip")) {
            String equipMessage = "   §d/bloons equip §7- " + Bloons.getCommandCore().getCommandDescription("equip");
            sender.sendMessage(equipMessage);
        }

        if (sender.hasPermission("bloons.unequip")) {
            String unequipMessage = "   §d/bloons unequip §7- " + Bloons.getCommandCore().getCommandDescription("unequip");
            sender.sendMessage(unequipMessage);
        }

        if (sender.hasPermission("bloons.force")) {
            String forceEquipMessage = "   §d/bloons fequip §7- " + Bloons.getCommandCore().getCommandDescription("fequip");
            String forceUnequipMessage = "   §d/bloons funequip §7- " + Bloons.getCommandCore().getCommandDescription("funequip");
            sender.sendMessage(forceEquipMessage);
            sender.sendMessage(forceUnequipMessage);
        }

        if (sender.hasPermission("bloons.reload")) {
            String reloadMessage = "   §d/bloons reload §7- " + Bloons.getCommandCore().getCommandDescription("reload");
            sender.sendMessage(reloadMessage);
        }

        // Send the credits no matter what at the end of the help section
        sender.sendMessage(""); // Blank line for spacing
        String creditsMessage = "   §dBloons " + PluginConfiguration.getVersion() + " §7- Made by " + PluginConfiguration.DEVELOPER_CREDITS;
        sender.sendMessage(creditsMessage);
        sender.sendMessage(""); // Blank line for spacing
    }
}
