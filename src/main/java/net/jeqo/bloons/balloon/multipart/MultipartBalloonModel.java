package net.jeqo.bloons.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.model.BalloonModel;
import net.jeqo.bloons.balloon.model.BalloonModelType;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.ColorManagement;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
public abstract class MultipartBalloonModel {

    private final BalloonModelType modelType;
    private final String material;
    private final String color;
    private Integer customModelData = null;

    public MultipartBalloonModel(BalloonModelType modelType, String material, String color, int customModelData) {
        this.modelType = modelType;
        this.material = material;
        this.color = color;
        this.customModelData = customModelData;
    }

    public MultipartBalloonModel(BalloonModelType modelType, String material, String color) {
        this.modelType = modelType;
        this.material = material;
        this.color = color;
    }

    /**
     * Gets the finalized item of a multipart balloon model
     * @return The finalized item as an ItemStack
     */
    public ItemStack getFinalizedModel() {
        Material material = Material.getMaterial(this.material.toUpperCase());
        if (material == null) {
            Logger.logError("Material " + this.material + " is not a valid material.");
            return null;
        }

        if (this.color == null && this.customModelData == null) {
            return new ItemStack(material);
        } else if (this.color == null) {
            return BalloonModel.createBlankModel(material, this.customModelData);
        } else if (color.startsWith("#")) {
            // Check if valid hex code
            if (!ColorManagement.isHexCode(this.color)) {
                Logger.logError("Color " + this.color + " is not a valid hex code.");
                return null;
            }

            Color color = ColorManagement.hexToColor(this.color);
            return BalloonModel.createColouredModel(material, color, this.customModelData);
        } else if (this.customModelData == null) {
            return new ItemStack(material);
        }

        return null;
    }
}
