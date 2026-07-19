package net.jeqo.bloons.utils;

import net.jeqo.bloons.logger.Logger;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Reflection-based compatibility helper for the 1.21.4+ CustomModelDataComponent API.
 * If the component API exists at runtime it will be used; otherwise it falls back
 * to setting the legacy single integer custom model data (parsing the first string).
 */
public final class CustomModelDataCompat {
    private static final boolean COMPONENT_AVAILABLE;
    private static final Method GET_COMPONENT;
    private static final Method SET_COMPONENT;
    private static final Class<?> COMPONENT_CLASS;
    private static final Method SET_STRINGS;
    private static final Method SET_FLOATS;

    static {
        boolean available = false;
        Method getComp = null;
        Method setComp = null;
        Class<?> compClass = null;
        Method setStrings = null;
        Method setFloats = null;
        try {
            compClass = Class.forName("org.bukkit.inventory.meta.components.CustomModelDataComponent");
            getComp = ItemMeta.class.getMethod("getCustomModelDataComponent");
            setComp = ItemMeta.class.getMethod("setCustomModelDataComponent", compClass);
            setStrings = compClass.getMethod("setStrings", List.class);
            setFloats = compClass.getMethod("setFloats", List.class);
            available = true;
        } catch (Throwable ignored) {}
        COMPONENT_AVAILABLE = available;
        GET_COMPONENT = getComp;
        SET_COMPONENT = setComp;
        COMPONENT_CLASS = compClass;
        SET_STRINGS = setStrings;
        SET_FLOATS = setFloats;
    }

    private CustomModelDataCompat() {}

    /**
     * Set custom model data strings on the provided ItemMeta.
     * - On 1.21.4+ uses CustomModelDataComponent (string list).
     * - On older versions attempts to parse the first string as an int and calls setCustomModelData.
     */
    public static void applyCustomModelData(ItemMeta meta, List<String> strings) {
        if (meta == null || strings == null || strings.isEmpty()) return;

        Integer legacyValue = parseLegacyValue(strings.get(0));
        if (COMPONENT_AVAILABLE) {
            try {
                Object comp = GET_COMPONENT.invoke(meta);
                if (legacyValue != null && SET_FLOATS != null) {
                    SET_FLOATS.invoke(comp, List.of(legacyValue.floatValue()));
                } else {
                    SET_STRINGS.invoke(comp, strings);
                }
                SET_COMPONENT.invoke(meta, comp);
                if (legacyValue != null) {
                    meta.setCustomModelData(legacyValue);
                }
                return;
            } catch (Throwable t) {
                Logger.logError("Failed to apply custom model data via CustomModelDataComponent, falling back to legacy method. Error: " + t.getMessage());
            }
        }

        if (legacyValue != null) {
            meta.setCustomModelData(legacyValue);
        } else {
            Logger.logError("Failed to apply custom model data: '" + strings.get(0) + "' is not a valid integer for legacy setCustomModelData");
        }
    }

    private static Integer parseLegacyValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
