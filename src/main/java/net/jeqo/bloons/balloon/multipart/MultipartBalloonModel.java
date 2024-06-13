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

/**
 * A class to aid in the creation of the models used in the multipart balloons
 */
@Getter @Setter
public class MultipartBalloonModel {

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

    /**
     * Gets the finalized item of a multipart balloon model
     * @return The finalized item as an ItemStack
     */
    public ItemStack getFinalizedModel() {
        Material material = Material.getMaterial(this.getMaterial().toUpperCase());
        if (material == null) {
            Logger.logError("Material " + this.getMaterial() + " is not a valid material.");
            return null;
        }

        if (this.getColor() == null && this.getCustomModelData() == null) {
            return new ItemStack(material);
        } else if (this.getColor() == null) {
            return BalloonModel.createBlankModel(material, this.getCustomModelData());
        } else if (this.getColor().startsWith("#")) {
            // Check if valid hex code
            if (!ColorManagement.isHexCode(this.getColor())) {
                Logger.logError("Color " + this.getColor() + " is not a valid hex code.");
                return null;
            }

            Color color = ColorManagement.hexToColor(this.getColor());
            return BalloonModel.createColouredModel(material, color, this.getCustomModelData());
        } else if (this.getCustomModelData() == null) {
            return new ItemStack(material);
        }

        return null;
    }
}
