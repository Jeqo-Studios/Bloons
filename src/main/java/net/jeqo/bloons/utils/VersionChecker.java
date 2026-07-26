package net.jeqo.bloons.utils;

/**
 * A utility class for version comparison
 */
public class VersionChecker {

    /**
     *                Checks if the current version is lower than the latest version
     * @param current The current version
     * @param latest  The latest version
     * @return        Whether the current version is lower than the latest version
     */
    public static boolean isVersionLower(String current, String latest) {
        return compareVersions(current, latest) < 0;
    }

    /**
     *                Checks if the current version is higher than the latest version
     * @param current The current version
     * @param latest  The latest version
     * @return        Whether the current version is higher than the latest version
     */
    public static boolean isVersionHigher(String current, String latest) {
        return compareVersions(current, latest) > 0;
    }

    /**
     * Compares two version strings such as {@code 1.2.3} and {@code 1.2.4}.
     *
     * @param v1 the first version string
     * @param v2 the second version string
     * @return a negative value if {@code v1} is lower than {@code v2}, zero if they
     *         are equal, or a positive value if {@code v1} is higher than {@code v2}
     */
    public static int compareVersions(String v1, String v2) {
        String[] v1Parts = v1.split("\\.");
        String[] v2Parts = v2.split("\\.");

        int length = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            int part1 = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int part2 = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            if (part1 != part2) {
                return Integer.compare(part1, part2);
            }
        }
        return 0;
    }
}
