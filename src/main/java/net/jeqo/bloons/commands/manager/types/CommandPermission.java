package net.jeqo.bloons.commands.manager.types;

import lombok.Getter;

/**
 * The permissions required to execute a command
 */
@Getter
public enum CommandPermission {
    /**
     * The permission required to equip a balloon
     */
    EQUIP("bloons.equip"),
    /**
     * The permission required to unequip a balloon
     */
    UNEQUIP("bloons.unequip"),
    /**
     * The permission required to force equip or force unequip a balloon
     */
    FORCE("bloons.force"),
    /**
     * The permission required to reload the plugin
     */
    RELOAD("bloons.reload");

    private final String permission;

    /**
     *                   Create a new command permission level
     * @param permission The name of the Minecraft permission required to execute the command, type java.lang.String
     */
    CommandPermission(String permission) {
        this.permission = permission;
    }
}
