package net.jeqo.bloons.balloon.model;

import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.message.Languages;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * A class to handle the creation of balloon models with custom model data and color metadata
 */
public class BalloonModel {
    private static final String DYEABLE_MATERIAL_PREFIX = "LEATHER_";

    /**
     *                          Generates a coloured model with the specified colour and custom model data
     * @param material          The Material type you want to generate the item as, type org.bukkit.Material
     * @param colour            The colour to dye the item if able to be dyed, type org.bukkit.Color
     * @param customModelData   The custom model data value, type int
     * @return                  The generated item created with the corresponding metadata, type org.bukkit.inventory.ItemStack.
     */
    public static ItemStack createColouredModel(Material material, Color colour, int customModelData) {
        // Check if the material is dyeable and contains leather attributes
        if (!material.name().contains(DYEABLE_MATERIAL_PREFIX)) {
            Logger.logError("Material " + material.name() + " is not a dyeable material.");
            return new ItemStack(material);
        }

        ItemStack generatedItem = new ItemStack(material);
        LeatherArmorMeta generatedItemLeatherMeta = (LeatherArmorMeta) generatedItem.getItemMeta();

        if (generatedItemLeatherMeta == null) return generatedItem;

        // Set the color and custom model data of the item in the metadata
        generatedItemLeatherMeta.setColor(colour);
        generatedItemLeatherMeta.setCustomModelData(customModelData);
        generatedItem.setItemMeta(generatedItemLeatherMeta);

        return generatedItem;
    }

    /**
     *                          Generates a coloured model with the specified color derived from the given RGB values
     * @param material          The Material type you want to generate the item as, type org.bukkit.Material
     * @param colourRed         The red colour value in the RGB, within the range of 0-255, type int
     * @param colourGreen       The green colour value in the RGB, within the range of 0-255, type int
     * @param colourBlue        The blue colour value in the RGB, within the range of 0-255, type int
     * @param customModelData   The custom model data value attached to the item metadata, type int
     * @return                  The generated item created with the corresponding metadata, type org.bukkit.inventory.ItemStack.
     *                          If the RGB values are not within the valid range of 0-255, or if the provided material is not dyeable,
     *                          the method will return null.
     */
    public static ItemStack createColouredModel(Material material, int colourRed, int colourGreen, int colourBlue, int customModelData) {
        // Check if the material is dyeable and contains leather attributes
        if (!material.name().contains(DYEABLE_MATERIAL_PREFIX)) {
            Logger.logWarning(String.format(Languages.getMessage("material-not-dyeable"), material));
            return new ItemStack(material);
        }

        // Check if the provided color values are within the valid ranges of RGB
        if (colourRed < 0 || colourRed > 255 || colourGreen < 0 || colourGreen > 255 || colourBlue < 0 || colourBlue > 255) {
            Logger.logError(Languages.getMessage("invalid-rgb-values"));
            return null;
        }

        ItemStack generatedItem = new ItemStack(material);
        LeatherArmorMeta generatedItemMeta = (LeatherArmorMeta) generatedItem.getItemMeta();
        Color color = Color.fromRGB(colourRed, colourGreen, colourBlue);

        // Set the color and custom model data of the item in the metadata
        assert generatedItemMeta != null; // Equivalent of setting to regular leather colour, assume we aren't
        generatedItemMeta.setColor(color);
        generatedItemMeta.setCustomModelData(customModelData);
        generatedItem.setItemMeta(generatedItemMeta);

        return generatedItem;
    }

    /**
     *                          Creates a model from any item without a specified colour and with custom model data in the metadata
     * @param material          The Material type of the item you want to generate, type org.bukkit.Material
     * @param customModelData   The custom model data value attached to the item metadata, type int
     * @return                  The generated item created with the corresponding metadata, type org.bukkit.inventory.ItemStack.
     */
    public static ItemStack createBlankModel(Material material, int customModelData) {
        ItemStack generatedItem = new ItemStack(material);
        ItemMeta generatedItemMeta = generatedItem.getItemMeta();

        // Check if the item supports ItemMeta
        if (generatedItemMeta == null) {
            Logger.logError(String.format("The specified material does not support ItemMeta: %s", material));
            return generatedItem;
        }

        // Set the custom model data of the item
        generatedItemMeta.setCustomModelData(customModelData);
        generatedItem.setItemMeta(generatedItemMeta);

        return generatedItem;
    }
}
