package net.jeqo.bloons.commands.manager.types;

/**
 * The access levels of a command
 */
public enum CommandAccess {
    ENABLED, // Used to signify the command is enabled and can be used
    DISABLED // Used to specify the command is disabled and can't be used by anyone
             // including console and administrators/op's
}
