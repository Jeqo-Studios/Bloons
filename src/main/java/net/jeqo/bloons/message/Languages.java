package net.jeqo.bloons.message;

import lombok.Getter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A class to manage the translations and retrieving of messages
 */
public class Languages {

    /**
     * Create a new set/hashmap to store the locales to ensure that they're unique
     */
    @Getter
    private static final Set<String> availableLanguages = new HashSet<>();
    static {
        // Initialize available languages
        String[] languages = {
                "en_US",
                "es_ES"
        };

        // Add all languages to the set
        Collections.addAll(getAvailableLanguages(), languages);
    }
}
