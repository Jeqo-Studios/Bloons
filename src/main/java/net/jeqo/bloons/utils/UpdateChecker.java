package net.jeqo.bloons.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *                      A utility class intended to check for updates on the SpigotMC website
 * @param plugin        The plugin instance, type org.bukkit.plugin.java.JavaPlugin
 * @param resourceId    The resource ID of the plugin on SpigotMC, type int
 */
public record UpdateChecker(JavaPlugin plugin, int resourceId) {

    /**
     *                  Gets the latest version of the plugin available on SpigotMC
     * @param consumer  The consumer to accept the version, type java.util.function.Consumer[java.lang.String]
     */
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin(), () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId()).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                this.plugin().getLogger().info("[Bloons] Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
