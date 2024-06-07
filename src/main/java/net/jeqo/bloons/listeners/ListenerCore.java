package net.jeqo.bloons.listeners;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;

/**
 * Core class for handling listeners
 */
@Setter @Getter
public class ListenerCore {
    private final Bloons plugin;
    private ArrayList<Listener> listeners;

    public ListenerCore(Bloons plugin) {
        this.plugin = plugin;

        this.setListeners(new ArrayList<>());
    }

    /**
     * Adds a listener to the list of listeners
     * @param listener The listener to add
     */
    public void stageListener(Listener listener) {
        this.getListeners().add(listener);
    }

    /**
     * Registers all listeners in the listeners list
     */
    public void registerListeners() {
        for (Listener listener : this.getListeners()) {
            this.getPlugin().getServer().getPluginManager().registerEvents(listener, this.getPlugin());
        }
    }

    /**
     * Unregisters all listeners and clear staged listeners
     */
    public void unregisterListeners() {
        this.setListeners(new ArrayList<>());
        HandlerList.unregisterAll(this.getPlugin());
    }
}
