package net.jeqo.bloons.balloon.model;

import net.jeqo.bloons.logger.Logger;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * A class to handle the creation of balloon models with custom model data and color metadata
 */
public class BalloonModel {
    private static final String leatherMaterialPrefix = "LEATHER_"; // A constant to define a dyeable material

    /**
     *                          Generates a coloured model with the specified colour and custom model data
     * @param material          The Material type you want to generate the item as, type org.bukkit.Material
     * @param colour            The colour to dye the item if able to be dyed, type org.bukkit.Color
     * @param customModelData   The custom model data value, type int
     * @return                  The generated item created with the corresponding metadata, type org.bukkit.inventory.ItemStack.
     */
    public static ItemStack createColouredModel(Material material, Color colour, int customModelData) {
        // Check if the material is dyeable and contains leather attributes
        if (!material.name().contains(leatherMaterialPrefix)) {
            Logger.logError("Material " + material.name() + " is not a dyeable material.");
            return new ItemStack(material);
        }

        ItemStack generatedItem = new ItemStack(material);
        LeatherArmorMeta generatedItemMeta = (LeatherArmorMeta) generatedItem.getItemMeta();

        // Set the color and custom model data of the item in the metadata
        generatedItemMeta.setColor(colour);
        generatedItemMeta.setCustomModelData(customModelData);
        generatedItem.setItemMeta(generatedItemMeta);

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
        if (!material.name().contains(leatherMaterialPrefix)) {
            Logger.logError("Material " + material.name() + " is not a dyeable material.");
            return new ItemStack(material);
        }

        // Check if the provided color values are within the valid ranges of RGB
        if (colourRed < 0 || colourRed > 255 || colourGreen < 0 || colourGreen > 255 || colourBlue < 0 || colourBlue > 255) {
            Logger.logError("RGB values must be between 0 and 255 to be valid.");
            return null;
        }

        ItemStack generatedItem = new ItemStack(material);
        LeatherArmorMeta generatedItemMeta = (LeatherArmorMeta) generatedItem.getItemMeta();
        Color color = Color.fromRGB(colourRed, colourGreen, colourBlue);

        // Set the color and custom model data of the item in the metadata
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

        // Set the custom model data of the item
        generatedItemMeta.setCustomModelData(customModelData);
        generatedItem.setItemMeta(generatedItemMeta);

        return generatedItem;
    }
}
