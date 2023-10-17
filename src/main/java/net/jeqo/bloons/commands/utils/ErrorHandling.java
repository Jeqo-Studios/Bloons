package net.jeqo.bloons.commands.utils;

import net.jeqo.bloons.utils.Utils;
import org.bukkit.command.CommandSender;

public class ErrorHandling {

    public static void usage(CommandSender sender) {
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
        sender.sendMessage(Utils.hex("   #ff00ccB#f406cfl#e80bd3o#dd11d6o#d217dan#c61cdds #bb22e01#b028e4.#a42de70#9933eb.#8e39ee1#823ef5-#7744f5B#6c4af8E#604ffcT#5555ffA &7- &fMade by Jeqo"));
        sender.sendMessage("");
    }
}
