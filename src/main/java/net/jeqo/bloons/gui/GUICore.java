package net.jeqo.bloons.gui;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.logger.LoggingLevel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public class GUICore {
    private ArrayList<GUI> guis;
    private JavaPlugin plugin;

    /**
     * Creates a new instance of the GUI core
     */
    public GUICore(JavaPlugin plugin) {
        if (plugin == null) {
            Logger.log(LoggingLevel.ERROR, "Plugin cannot be null. Please provide a valid instance.");
        }

        this.plugin = plugin;
        this.guis = new ArrayList<>();
    }

    /**
     * Creates a new instance of the GUI core with a list of GUIs
     * @param guis The GUIs to register
     */
    public GUICore(JavaPlugin plugin, GUI... guis) {
        if (plugin == null) {
            Logger.log(LoggingLevel.ERROR, "Plugin cannot be null. Please provide a valid instance.");
        }

        this.plugin = plugin;
        this.guis = new ArrayList<>();
        this.guis.addAll(Arrays.asList(guis));
    }

    /**
     * Creates a new instance of the GUI core with a list of GUIs
     * @param guis The list of GUIs to register
     */
    public GUICore(JavaPlugin plugin, ArrayList<GUI> guis) {
        if (plugin == null) {
            Logger.log(LoggingLevel.ERROR, "The Wynncraft plugin cannot be null. Please provide a valid instance.");
        }

        this.plugin = plugin;
        this.guis = guis;
    }

    /**
     * Registers a GUI to the GUI core
     * @param gui The GUI to register
     */
    public void registerGUI(GUI gui) {
        this.guis.add(gui);
    }

    /**
     * Unregisters a GUI from the GUI core
     * @param gui The GUI to unregister
     */
    public void unregisterGUI(GUI gui) {
        this.guis.remove(gui);
    }

    /**
     * Opens a GUI to the specified player
     * @param gui The GUI to open
     */
    public void openGUI(GUI gui, Player player) {
        if (!this.getGuis().contains(gui)) {
            Logger.log(LoggingLevel.ERROR, "The GUI " + gui.name() + " is not registered in the GUI core. Please register it before opening it.");
        } else {
            // Open the GUI and start a task to update the GUI every 20 ticks
            gui.open(player);
            player.openInventory(gui.inventory());
            gui.startUpdater(this.getPlugin());

            // Trigger the event specified in the GUI if it exists
            if (gui.triggerEvent() != null) {
                gui.triggerEvent().callEvent();
            }
        }
    }

    /**
     * Closes a GUI for the specified player
     * @param gui The GUI to close
     */
    public void closeGUI(GUI gui, Player player) {
        if (!this.getGuis().contains(gui)) {
            Logger.log(LoggingLevel.ERROR, "The GUI " + gui.name() + " is not registered in the GUI core. Please register it before closing it.");
        } else {
            // Close the GUI and stop the task to update the GUI
            gui.onClose(player);
            player.closeInventory();
            gui.stopUpdater();
        }
    }
}
