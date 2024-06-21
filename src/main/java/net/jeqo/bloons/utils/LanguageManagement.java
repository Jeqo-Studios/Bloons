package net.jeqo.bloons.utils;

import net.jeqo.bloons.Bloons;

public class LanguageManagement {

    /**
     * Copies all language files over from the languages directory
     */
    public static void copyLanguageFiles() {
        Bloons.getInstance().saveResource("languages/en_US.yml", false);
    }
}
