package net.jeqo.bloons.configuration;

import net.jeqo.bloons.Bloons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class that contains configurations for the plugin configuration file
 */
public class ConfigConfiguration {
    public static final String BALLOON_CONFIGURATION_FOLDER = "balloons"; // The folder that stores the balloons to be loaded

    /**
     *          Gets the number of configuration files currently in the balloon configuration folder
     * @return  The number of configuration files in the balloon configuration folder. Returns 0 upon none found. type long
     */
    public long getBalloonConfigurationCount() {
        try {
            //
            Path path = Path.of(Bloons.getInstance().getDataFolder() + File.separator + BALLOON_CONFIGURATION_FOLDER);
            return Files.walk(path).filter(Files::isRegularFile).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public
}
