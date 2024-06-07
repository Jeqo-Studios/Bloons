package net.jeqo.bloons.commands.utils;

import net.jeqo.bloons.utils.ColorManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class ErrorHandling {

    public static void usage(CommandSender sender) {
        sender.sendMessage("");
        if (sender.hasPermission("bloons.menu")) {
            Component menuMessage = Component.text(ColorManagement.fromHex("#ff00cc/#e30ed5b#c61cddl#aa2be6o#8e39eeo#7147f7n#5555ffs &7- Open the balloon menu"));
            sender.sendMessage(menuMessage);
        }
        if (sender.hasPermission("bloons.equip")) {
            Component equipMessage = Component.text(ColorManagement.fromHex("#ff00cc/#e30ed5b#c61cddl#aa2be6o#8e39eeq#7147fui#5555ffp &7- Equip a balloon"));
            sender.sendMessage(equipMessage);
        }
        if (sender.hasPermission("bloons.unequip")) {
            Component unequipMessage = Component.text(ColorManagement.fromHex("#ff00cc/#e30ed5b#c61cddl#aa2be6o#8e39eeu#7147fni#5555ffp &7- Unequip a balloon"));
            sender.sendMessage(unequipMessage);
        }
        if (sender.hasPermission("bloons.force")) {
            Component forceEquipMessage = Component.text(ColorManagement.fromHex("   #ff00cc/#f107d0b#e30ed5l#d515d9o#c61cddo#b823e1n#aa2be6s #9c32eaf#8e39eee#8040f2q#7147f7u#634efbi#5555ffp &7- Equip a balloon to a player"));
            Component forceUnequipMessage = Component.text(ColorManagement.fromHex("   #ff00cc/#f306d0b#e70cd3l#db12d7o#ce18dbo#c21eden#b624e2s #aa2be6f#9e31e9u#9237edn#863df0e#7943f4q#6d49f8u#614ffbi#5555ffp &7- Unequip a balloon from a player"));
            sender.sendMessage(forceEquipMessage);
            sender.sendMessage(forceUnequipMessage);
        }
        if (sender.hasPermission("bloons.reload")) {
            Component reloadMessage = Component.text(ColorManagement.fromHex("   #ff00cc/#f107d0b#e30ed5l#d515d9o#c61cddo#b823e1n#aa2be6r #9c32eeo#8e39ee1#8040f2l#7147f7o#634efbi#5555ffd &7- Reload the Bloons config"));
            sender.sendMessage(reloadMessage);
        }
        sender.sendMessage(Component.text(""));
        Component creditsMessage = Component.text(ColorManagement.fromHex("   #ff00ccB#f406cfl#e80bd3o#dd11d6o#d217dan#c61cdds #bb22e01#b028e4.#a42de70#9933eb.#8e39ee1#823ef5-#7744f5B#6c4af8E#604ffcT#5555ffA &7- &fMade by Jeqo"));
        sender.sendMessage(creditsMessage);
        sender.sendMessage(Component.text(""));
    }
}
