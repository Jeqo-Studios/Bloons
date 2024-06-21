package net.jeqo.bloons.commands.manager.types;

import lombok.Getter;

/**
 * The permissions required to execute a command
 */
@Getter
public enum CommandPermission {
    EQUIP("bloons.equip"),
    UNEQUIP("bloons.unequip"),
    FORCE("bloons.force"),
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
