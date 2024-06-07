package net.jeqo.bloons.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A custom even class that can be used to create our own bloons events
 * These are nearly the same as Bukkit events and can be used interchangeably
 */
public class BloonsEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;

    /**
     * Gets all listeners that are listening to this event
     * @return a list of handlers
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Returns whether the event is cancelled.
     * @return true if the event is cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets whether the event is cancelled.
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Cancels the current event
     */
    public void cancel() {
        this.setCancelled(true);
    }

    /**
     * Unregisters the listener
     * @param plugin the plugin to unregister the event from
     */
    public void unregister(Plugin plugin) {
        this.getHandlers().unregister(plugin);
    }

    /**
     * Unregisters all listeners from the event
     */
    public void unregisterAll() {
        HandlerList.unregisterAll();
    }
}
