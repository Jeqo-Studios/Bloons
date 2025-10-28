package net.jeqo.bloons.balloon.model;

import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.CustomModelDataCompat;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

/**
 * A class to handle the creation of balloon models with custom model data and color metadata
 */
public class BalloonModel {
    private static final String DYEABLE_MATERIAL_PREFIX = "LEATHER_";

    /**
     *                          Generates a coloured model with the specified colour and custom model data
     * @param material          The Material type you want to generate the item as, type org.bukkit.Material
     * @param colour            The colour to dye the item if able to be dyed, type org.bukkit.Color
     * @param customModelData   The custom model data value, type String
     * @param itemModel         The item model name used to reference and set the item model, type java.lang.String
     * @return                  The generated item created with the corresponding metadata, type org.bukkit.inventory.ItemStack.
     */
    public static ItemStack createColouredModel(Material material, Color colour, String customModelData, String itemModel) {
        if (!material.name().contains(DYEABLE_MATERIAL_PREFIX) && !material.name().equals("FIREWORK_STAR")) {
            Logger.logError("Material " + material.name() + " is not a dyeable material.");
            return new ItemStack(material);
        }

        ItemStack generatedItem = new ItemStack(material);

        if (material.name().equals("FIREWORK_STAR")) {
            ItemMeta meta = generatedItem.getItemMeta();
            if (meta instanceof org.bukkit.inventory.meta.FireworkEffectMeta fireworkMeta) {
                org.bukkit.FireworkEffect effect = org.bukkit.FireworkEffect.builder()
                    .withColor(colour)
                    .build();
                fireworkMeta.setEffect(effect);

                if (customModelData != null && !customModelData.isEmpty()) {
                    CustomModelDataCompat.applyCustomModelData(fireworkMeta, List.of(customModelData));
                }

                if (itemModel != null && !itemModel.isEmpty()) {
                    NamespacedKey itemModelKey = NamespacedKey.fromString(itemModel);
                    fireworkMeta.setItemModel(itemModelKey);
                }

                generatedItem.setItemMeta(fireworkMeta);
            }
            return generatedItem;
        }

        LeatherArmorMeta generatedItemLeatherMeta = (LeatherArmorMeta) generatedItem.getItemMeta();
        if (generatedItemLeatherMeta == null) return generatedItem;

        generatedItemLeatherMeta.setColor(colour);

        if (itemModel != null && !itemModel.isEmpty()) {
            NamespacedKey itemModelKey = NamespacedKey.fromString(itemModel);
            generatedItemLeatherMeta.setItemModel(itemModelKey);
        }

        if (customModelData != null && !customModelData.isEmpty()) {
            CustomModelDataCompat.applyCustomModelData(generatedItemLeatherMeta, List.of(customModelData));
        }

        generatedItem.setItemMeta(generatedItemLeatherMeta);

        return generatedItem;
    }

    /**
     *                          Creates a model from any item without a specified colour and with custom model data in the metadata
     * @param material          The Material type of the item you want to generate, type org.bukkit.Material
     * @param customModelData   The custom model data value attached to the item metadata, type String
     * @param itemModel         The item model name used to reference and set the item model, type java.lang.String
     * @return                  The generated item created with the corresponding metadata, type org.bukkit.inventory.ItemStack.
     */
    public static ItemStack createBlankModel(Material material, String customModelData, String itemModel) {
        ItemStack generatedItem = new ItemStack(material);
        ItemMeta generatedItemMeta = generatedItem.getItemMeta();

        // Check if the item supports ItemMeta
        if (generatedItemMeta == null) {
            Logger.logError(String.format("The specified material does not support ItemMeta: %s", material));
            return generatedItem;
        }

        // Set the custom model data of the item
        if (customModelData != null && !customModelData.isEmpty()) {
            CustomModelDataCompat.applyCustomModelData(generatedItemMeta, List.of(customModelData));
        }

        if (itemModel != null && !itemModel.isEmpty()) {
            NamespacedKey itemModelKey = NamespacedKey.fromString(itemModel);
            generatedItemMeta.setItemModel(itemModelKey);
        }

        generatedItem.setItemMeta(generatedItemMeta);

        return generatedItem;
    }
}
