package net.jeqo.bloons.configuration;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;

/**
 * A class that contains configurations and information regarding the plugin
 */
public class PluginConfiguration {
    // The developer credits for the plugin, displayed on startup
    public static final String DEVELOPER_CREDITS = "Jeqo and Gucci Fox";

    /**
     * Get the version of the plugin from the pom.xml file
     * @return The version of the plugin
     * @throws IOException If the file cannot be read
     * @throws XmlPullParserException If the file cannot be parsed
     */
    public static String getVersion() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        return model.getVersion();
    }

    /**
     * Get the name of the plugin from the pom.xml file
     * @return The name of the plugin
     * @throws IOException If the file cannot be read
     * @throws XmlPullParserException If the file cannot be parsed
     */
    public static String getName() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        return model.getName();
    }

    /**
     * Get the description of the plugin from the pom.xml file
     * @return The description of the plugin
     * @throws IOException If the file cannot be read
     * @throws XmlPullParserException If the file cannot be parsed
     */
    public static String getDescription() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        return model.getDescription();
    }

    /**
     * Gets the website URL of the plugin from the pom.xml file
     * @return The website URL of the plugin
     * @throws IOException If the file cannot be read
     * @throws XmlPullParserException If the file cannot be parsed
     */
    public static String getURL() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        return model.getUrl();
    }
}
