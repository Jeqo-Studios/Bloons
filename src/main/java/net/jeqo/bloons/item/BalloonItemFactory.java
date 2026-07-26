package net.jeqo.bloons.item;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloonType;
import net.jeqo.bloons.colors.Color;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.CustomModelDataCompat;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds display items for balloon visuals and menu entries.
 */
public final class BalloonItemFactory {
    private static final String BALLOON_ID_KEY = "balloonId";
    private static final String LEATHER_MATERIAL_PREFIX = "LEATHER_";

    private BalloonItemFactory() {
    }

    public static ItemStack createSingleMenuItem(SingleBalloonType balloonType) {
        ItemStack item = createBaseItem(balloonType.getMaterial(), balloonType.getCustomModelData(), balloonType.getItemModel());
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            logInvalidItemMeta(balloonType.getMaterial());
            return null;
        }

        meta.setItemName(balloonType.getKey());
        applyLore(meta, balloonType.getLore());
        applyDisplayName(meta, balloonType.getName());
        applyColor(meta, balloonType.getColor(), balloonType.getMaterial(), balloonType.getMegModelID() == null);
        applyBalloonId(meta, balloonType.getId());

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createMultipartMenuItem(MultipartBalloonType balloonType) {
        String materialName = balloonType.getHeadModel().getMaterial();
        ItemStack item = createBaseItem(
                materialName,
                balloonType.getHeadModel().getCustomModelData(),
                balloonType.getHeadModel().getItemModel()
        );
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            logInvalidItemMeta(materialName);
            return null;
        }

        meta.setItemName(balloonType.getId());
        applyLore(meta, balloonType.getLore());
        applyDisplayName(meta, balloonType.getName());
        applyColor(meta, balloonType.getHeadModel().getColor(), materialName, true);
        applyBalloonId(meta, balloonType.getId());

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createSingleVisualItem(SingleBalloonType balloonType, String overrideColor) {
        if (balloonType == null) return new ItemStack(Material.BARRIER);

        ItemStack item = createBaseItem(balloonType.getMaterial(), balloonType.getCustomModelData(), balloonType.getItemModel());
        if (item == null) return new ItemStack(Material.BARRIER);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            logInvalidItemMeta(balloonType.getMaterial());
            return new ItemStack(Material.BARRIER);
        }

        String colorHex = isBlank(overrideColor) ? balloonType.getColor() : overrideColor;
        if ("potion".equalsIgnoreCase(colorHex) && balloonType.getMaterial().startsWith(LEATHER_MATERIAL_PREFIX)) {
            Logger.logWarning(String.format(Bloons.getConfigurationManager().getConfigString("material-not-dyeable"), balloonType.getMaterial()));
            item.setItemMeta(meta);
            return item;
        }

        applyColor(meta, colorHex, balloonType.getMaterial(), false);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createBaseItem(String materialName, String customModelData, String itemModel) {
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            Logger.logError(String.format(Bloons.getConfigurationManager().getConfigString("material-not-valid"), materialName));
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            logInvalidItemMeta(materialName);
            return null;
        }

        if (!isBlank(customModelData)) {
            CustomModelDataCompat.applyCustomModelData(meta, List.of(customModelData));
        }

        if (!isBlank(itemModel)) {
            NamespacedKey itemModelKey = NamespacedKey.fromString(itemModel);
            meta.setItemModel(itemModelKey);
        }

        item.setItemMeta(meta);
        return item;
    }

    private static void applyLore(ItemMeta meta, String[] loreLines) {
        if (loreLines == null) {
            return;
        }

        List<String> lore = new ArrayList<>(List.of(loreLines));
        lore.replaceAll(Color::fromHex);
        meta.setLore(lore);
    }

    private static void applyDisplayName(ItemMeta meta, String name) {
        if (name != null) {
            meta.setDisplayName(name);
        }
    }

    private static void applyBalloonId(ItemMeta meta, String balloonId) {
        NamespacedKey balloonIdKey = new NamespacedKey(Bloons.getInstance(), BALLOON_ID_KEY);
        meta.getPersistentDataContainer().set(balloonIdKey, PersistentDataType.STRING, balloonId);
    }

    private static void applyColor(ItemMeta meta, String color, String materialName, boolean warnWhenNotDyeable) {
        if (isBlank(color) || "potion".equalsIgnoreCase(color)) {
            return;
        }

        if (meta instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(Color.hexToColor(color));
            return;
        }

        if (meta instanceof org.bukkit.inventory.meta.FireworkEffectMeta fireworkMeta) {
            org.bukkit.FireworkEffect effect = org.bukkit.FireworkEffect.builder()
                    .withColor(Color.hexToColor(color))
                    .build();
            fireworkMeta.setEffect(effect);
            return;
        }

        if (warnWhenNotDyeable || !materialName.startsWith(LEATHER_MATERIAL_PREFIX)) {
            Logger.logWarning(String.format(Bloons.getConfigurationManager().getConfigString("material-not-dyeable"), materialName));
        }
    }

    private static void logInvalidItemMeta(String materialName) {
        Logger.logError(String.format(Bloons.getConfigurationManager().getConfigString("invalid-item-meta"), materialName));
    }

    private static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
