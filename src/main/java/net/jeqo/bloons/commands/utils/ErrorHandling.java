package net.jeqo.bloons.commands.utils;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.PluginConfiguration;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class ErrorHandling {

    /**
     *                  Sends a message to the sender that the command was not found.
     *                  Based on the commands the player has access to, it will also send a list of available commands
     * @param sender    The sender of the command, type org.bukkit.command.CommandSender
     */
    public static void usage(CommandSender sender) {
        MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());

        sender.sendMessage(Component.text(""));
        if (sender.hasPermission("bloons.menu")) {
            Component menuMessage = messageTranslations.getSerializedString("   <gradient:#ff00cc:#5555ff>/bloons</gradient> <gray>- Open the balloon menu");
            sender.sendMessage(menuMessage);
        }

        if (sender.hasPermission("bloons.equip")) {
            Component equipMessage = messageTranslations.getSerializedString("   <gradient:#ff00cc:#5555ff>/bloons equip</gradient> <gray>- " + Bloons.getCommandCore().getCommandDescription("equip"));
            sender.sendMessage(equipMessage);
        }

        if (sender.hasPermission("bloons.unequip")) {
            Component unequipMessage = messageTranslations.getSerializedString("   <gradient:#ff00cc:#5555ff>/bloons unequip</gradient> <gray>- " + Bloons.getCommandCore().getCommandDescription("unequip"));
            sender.sendMessage(unequipMessage);
        }

        if (sender.hasPermission("bloons.force")) {
            Component forceEquipMessage = messageTranslations.getSerializedString("   <gradient:#ff00cc:#5555ff>/bloons fequip</gradient> <gray>- " + Bloons.getCommandCore().getCommandDescription("fequip"));
            Component forceUnequipMessage = messageTranslations.getSerializedString("   <gradient:#ff00cc:#5555ff>/bloons funequip</gradient> <gray>- " + Bloons.getCommandCore().getCommandDescription("funequip"));
            sender.sendMessage(forceEquipMessage);
            sender.sendMessage(forceUnequipMessage);
        }

        if (sender.hasPermission("bloons.reload")) {
            Component reloadMessage = messageTranslations.getSerializedString("   <gradient:#ff00cc:#5555ff>/bloons reload</gradient> <gray>- " + Bloons.getCommandCore().getCommandDescription("reload"));
            sender.sendMessage(reloadMessage);
        }

        // Send the credits no matter what at the end of the help section
        sender.sendMessage(Component.text(""));
        Component creditsMessage = messageTranslations.getSerializedString("   <gradient:#ff00cc:#5555ff>Bloons " + PluginConfiguration.getVersion() + "</gradient> <gray>- <white>Made by " + PluginConfiguration.DEVELOPER_CREDITS);
        sender.sendMessage(creditsMessage);
        sender.sendMessage(Component.text(""));
    }
}
