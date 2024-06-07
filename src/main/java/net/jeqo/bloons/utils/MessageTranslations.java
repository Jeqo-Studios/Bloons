package net.jeqo.bloons.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public record MessageTranslations(JavaPlugin instance) {

    public String getMessage(String id, String arg) {
        return String.format(this.instance.getConfig().getString("messages." + id, ""), arg);
    }

    public String getMessage(String id) {
        return this.instance.getConfig().getString("messages." + id, "");
    }

    public Component getSerializedString(String messagePrefix, String messageSuffix) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(messagePrefix + messageSuffix);
    }

    public Component getSerializedString(String message) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(message);
    }

    public String convertBukkitColorsToAdventure(String message) {
        message = message.replace("§0", "<black>");
        message = message.replace("§1", "<dark_blue>");
        message = message.replace("§2", "<dark_green>");
        message = message.replace("§3", "<dark_aqua>");
        message = message.replace("§4", "<dark_red>");
        message = message.replace("§5", "<dark_purple>");
        message = message.replace("§6", "<gold>");
        message = message.replace("§7", "<gray>");
        message = message.replace("§8", "<dark_gray>");
        message = message.replace("§9", "<blue>");
        message = message.replace("§a", "<green>");
        message = message.replace("§b", "<aqua>");
        message = message.replace("§c", "<red>");
        message = message.replace("§d", "<light_purple>");
        message = message.replace("§e", "<yellow>");
        message = message.replace("§f", "<white>");

        return message;
    }


    public String getString(String path) {
        return this.instance.getConfig().getString(path);
    }

    public Integer getInt(String path) {
        return this.instance.getConfig().getInt(path);
    }
}
