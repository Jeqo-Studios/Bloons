package net.jeqo.bloons.utils;

import org.bukkit.plugin.java.JavaPlugin;

public record MessageTranslations(JavaPlugin instance) {

    public String getMessage(String id, String arg) {
        return ColorManagement.fromHex(String.format(this.instance.getConfig().getString("messages." + id, ""), arg));
    }

    public String getMessage(String id) {
        return ColorManagement.fromHex(instance.getConfig().getString("messages." + id, ""));
    }

    public String getString(String path) {
        return this.instance.getConfig().getString(path);
    }

    public Integer getInt(String path) {
        return this.instance.getConfig().getInt(path);
    }
}
