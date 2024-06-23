package net.jeqo.bloons.commands.manager.types;

/**
 * The access levels of a command
 */
public enum CommandAccess {
    /**
     * Used to signify the command is enabled and can be used
     */
    ENABLED,
    /**
     * Used to specify the command is disabled and can't be used by anyone
     * including console and administrators/op's
     */
    DISABLED
}
