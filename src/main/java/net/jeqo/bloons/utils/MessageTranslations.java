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

    public String getString(String path) {
        return this.instance.getConfig().getString(path);
    }

    public Integer getInt(String path) {
        return this.instance.getConfig().getInt(path);
    }
}
