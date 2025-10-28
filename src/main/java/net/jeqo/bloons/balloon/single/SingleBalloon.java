package net.jeqo.bloons.balloon.single;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.BalloonConfiguration;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.management.SingleBalloonManagement;
import net.jeqo.bloons.colors.Color;
import net.jeqo.bloons.utils.CustomModelDataCompat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter @Setter
public class SingleBalloon extends BukkitRunnable {
    /** Must have variables related to any balloon **/
    private SingleBalloonType type;
    private Player player;
    private ArmorStand armorStand;
    public Chicken chicken;

    /**
     * Only used for non-MEG balloons to configure the visual appearance of the balloon
     */
    private ItemStack visual;

    /** MEG related variables **/
    private SingleMEGBalloonHandler megHandler;
    private boolean hasMEGModel;

    /** Variables used for the movement of the balloon **/
    private Location playerLocation;
    private Location moveLocation;

    private int ticks = 0;
    private float targetYaw = 0.0F;

    // A prefix that is needed for dyable materials
    private static final String LEATHER_MATERIAL_PREFIX = "LEATHER_";
    /**
     * Optional override colour for the balloon visual (hex string like #RRGGBB)
     */
    private String overrideColor;

    /**
     *                      Constructor for the SingleBalloon class (existing usage)
     * @param player        The player to attach the balloon to, type org.bukkit.entity.Player
     * @param balloonID     The ID of the balloon to attach to the player, type java.lang.String
     */
    public SingleBalloon(Player player, String balloonID) {
        this(player, balloonID, null);
    }

    /**
     *                      Constructor for the SingleBalloon class with colour override
     * @param player        The player to attach the balloon to, type org.bukkit.entity.Player
     * @param balloonID     The ID of the balloon to attach to the player, type java.lang.String
     * @param overrideColor Optional hex colour override (e.g. "#ff0000")
     */
    public SingleBalloon(Player player, String balloonID, String overrideColor) {
        this.setPlayer(player);
        this.setType(Bloons.getBalloonCore().getSingleBalloonByID(balloonID));
        this.setOverrideColor(overrideColor);

        if (this.getType().getMegModelID() == null) {
            this.setVisual(getConfiguredBalloonVisual(balloonID, overrideColor));
        }
    }

    /**
     * Initializes the balloon and its subcomponents.
     * Sets the current players location, and initializes the armor stand, and chicken entities
     */
    private void initializeBalloon() {
        this.setPlayerLocation(this.getPlayer().getLocation());
        this.getPlayerLocation().setYaw(0.0F);

        if (this.getType().getMegModelID() == null) {
            // Create and set the balloons visual appearance/model
            ItemMeta meta = this.getVisual().getItemMeta();

            if (meta != null) {
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }

            this.getVisual().setItemMeta(meta);
        }

        // Initialize the armor stand and lead to the player
        this.initializeBalloonArmorStand();
        this.initializeBalloonLead();
    }

    /**
     * Initializes the balloon's armor stand entity with the proper configurations
     */
    public void initializeBalloonArmorStand() {
        if (this.getPlayerLocation().getWorld() == null) {
            Logger.logError("Player world is not currently set. Could not initialize balloon, removing from player.");
            SingleBalloonManagement.removeBalloon(player, null);
            return;
        }

        this.setArmorStand(this.getPlayerLocation().getWorld().spawn(this.getPlayerLocation(), ArmorStand.class));
        this.getArmorStand().setBasePlate(false);
        this.getArmorStand().setVisible(false);
        this.getArmorStand().setInvisible(true);
        this.getArmorStand().setInvulnerable(true);
        this.getArmorStand().setCanPickupItems(false);
        this.getArmorStand().setGravity(false);
        this.getArmorStand().setSmall(false);
        this.getArmorStand().setMarker(true);
        this.getArmorStand().setCollidable(false);

        // Check if MEG model ID exists and ModelEngine is available
        if (this.getType().getMegModelID() != null && !this.getType().getMegModelID().isEmpty()) {
            try {
                this.setMegHandler(new SingleMEGBalloonHandler());
                this.getMegHandler().initialize(this.getArmorStand(), this.getType().getMegModelID());
                this.setHasMEGModel(true);
            } catch (ClassNotFoundException e) {
                Logger.logError("ModelEngine is not installed but balloon " + this.getType().getId() + " requires it!");
                this.getArmorStand().getEquipment().setHelmet(this.getVisual());
            } catch (Exception e) {
                Logger.logError("Failed to create MEG model for balloon " + this.getType().getId() + ": " + e.getMessage());
                this.getArmorStand().getEquipment().setHelmet(this.getVisual());
            }
        } else {
            this.getArmorStand().getEquipment().setHelmet(this.getVisual());
        }

        this.getArmorStand().setCustomName(BalloonConfiguration.BALLOON_ARMOR_STAND_ID);
    }

    /**
     * Initializes the balloon's lead to the player (chicken entity)
     */
    public void initializeBalloonLead() {
        if (this.getPlayerLocation().getWorld() == null) {
            Logger.logError("Player world is not currently set. Could not initialize balloon, removing from player.");
            // Remove the balloon from the player
            SingleBalloonManagement.removeBalloon(player, null);
            return;
        }

        this.setChicken(this.getPlayerLocation().getWorld().spawn(this.getPlayerLocation(), Chicken.class));
        this.getChicken().setInvulnerable(true);
        this.getChicken().setInvisible(true);
        this.getChicken().setSilent(true);
        this.getChicken().setBaby();
        this.getChicken().setAgeLock(true);
        this.getChicken().setAware(false);
        this.getChicken().setCollidable(false);
        this.getChicken().setLeashHolder(this.getPlayer());
        this.getChicken().setCustomName(BalloonConfiguration.BALLOON_CHICKEN_ID);
    }

    /**
     * What runs inside the extended bukkit runnable, it's
     * the control center of the core functionality of how the balloon moves
     */
    public void run() {
        // If the balloon armor stand is null, initialize the balloon
        if (this.getArmorStand() == null) initializeBalloon();

        // Every tick, retrieve the updated player location
        Location playerLocation = this.getPlayerLocation();
        playerLocation.setYaw(this.getPlayerLocation().getYaw());

        // If the ticks reach 20, set back to 0 and set a new target yaw
        if (this.getTicks() == 20) {
            this.setTargetYaw(ThreadLocalRandom.current().nextInt(10) - 5);
            this.setTicks(0);
        }

        // If the target yaw is greater than the player location yaw, add 0.2 to the yaw
        if (this.getTargetYaw() > playerLocation.getYaw()) {
            playerLocation.setYaw(playerLocation.getYaw() + 0.2F);
        } else if (this.getTargetYaw() < playerLocation.getYaw()) {
            playerLocation.setYaw(playerLocation.getYaw() - 0.2F);
        }

        // Set the move location to the armor stand location minus 2 on the Y axis
        this.setMoveLocation(this.getArmorStand().getLocation().subtract(0.0D, this.getType().getBalloonHeight(), 0.0D).clone());

        // Set the vector to the player location minus the move location
        Vector playerToBalloon = playerLocation.toVector().subtract(this.getMoveLocation().toVector());
        playerToBalloon.multiply(0.3D);
        this.setMoveLocation(this.getMoveLocation().add(playerToBalloon));

        // Compute local tilt
        Vector direction = playerLocation.getDirection().setY(0).normalize();
        double yawRad = Math.atan2(direction.getX(), direction.getZ());
        Quaternionf toLocal = new Quaternionf().rotateY((float) -yawRad);
        Vector localTilt = playerToBalloon.clone();
        toLocal.transform(localTilt.toVector3f());

        double pitch = Math.toRadians(localTilt.getZ() * 50.0D * -1.0D);
        double roll  = Math.toRadians(localTilt.getX() * 50.0D * -1.0D);

        EulerAngle tiltAngle = new EulerAngle(pitch, yawRad, roll);

        ArmorStand armorStand = this.getArmorStand();
        armorStand.setHeadPose(tiltAngle);
        if (this.hasMEGModel && this.getMegHandler() != null) {
            this.getMegHandler().updateRotation(pitch, roll);
        }

        // Teleport the balloon to the move location and set the player location yaw
        this.teleport(this.getMoveLocation());

        // If the balloon armor stand is more than 5 blocks away, teleport to player location
        if (this.getArmorStand().getLocation().distance(playerLocation) > 5.0D) {
            this.teleport(playerLocation);
        }

        // If all parts of a MEG balloon exist and the idle animation exists and it isn't playing, play it
        if (this.hasMEGModel && this.getMegHandler() != null) {
            this.getMegHandler().updateAnimation();
        }

        this.setPlayerLocation(this.getPlayer().getLocation());
        this.getPlayerLocation().setYaw(playerLocation.getYaw());
        this.setTicks(this.getTicks() + 1);
    }

    /**
     *                                  Cancels the current bukkit runnable instance and kills off the entities
     * @throws IllegalStateException    If the task has already been cancelled
     */
    public synchronized void cancel() throws IllegalStateException {
        if (this.hasMEGModel && this.getMegHandler() != null) {
            this.getMegHandler().destroy();
        }
        this.getArmorStand().remove();
        this.getChicken().remove();
        super.cancel();
    }

    /**
     * Spawns the particle effect when the balloon is removed
     */
    public void spawnRemoveParticle() {
        if (this.getMoveLocation().getWorld() == null) return;

        this.getMoveLocation().getWorld().spawnParticle(Particle.CLOUD, this.getMoveLocation(), 5, 0.0D, 0.0D, 0.0D, 0.1D);
    }

    /**
     *                  Teleports the balloon's entities to a specified location
     * @param location  The location to teleport the balloon to, type org.bukkit.Location
     */
    private void teleport(Location location) {
        this.getArmorStand().teleport(location.add(0.0D, this.getType().getBalloonHeight(), 0.0D));
        this.getChicken().teleport(location.add(0.0D, this.getType().getLeashHeight(), 0.0D));
    }

    /**
     *                      Retrieves the item stack object of the visual appearance of the balloon
     * Overload that accepts an optional override color
     * @param balloonID     The balloon ID to get the visual appearance of, type java.lang.String
     * @param overrideColor Optional hex color override (e.g. "#RRGGBB")
     * @return              The item object that contains the configured balloon model, returns a barrier if there is an issue, type org.bukkit.inventory.ItemStack
     */
    public ItemStack getConfiguredBalloonVisual(String balloonID, String overrideColor) {
        SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);

        if (singleBalloonType == null) {
            Logger.logError(String.format(Languages.getMessage("balloon-not-set"), balloonID));
            return new ItemStack(Material.BARRIER);
        }

        if (singleBalloonType.getMaterial() == null) {
            Logger.logError(String.format(Languages.getMessage("material-not-set"), balloonID));
            return new ItemStack(Material.BARRIER);
        }

        Material material = Material.getMaterial(singleBalloonType.getMaterial());
        if (material == null) {
            Logger.logError(String.format(Languages.getMessage("material-not-valid"), balloonID, singleBalloonType.getMaterial()));
            return new ItemStack(Material.BARRIER);
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Set custom model data if present
            String customModelData = singleBalloonType.getCustomModelData();
            if (customModelData != null && !customModelData.isEmpty()) {
                CustomModelDataCompat.applyCustomModelData(meta, List.of(customModelData));
            }

            // Set item model if present
            String itemModel = singleBalloonType.getItemModel();
            if (itemModel != null && !itemModel.isEmpty()) {
                NamespacedKey itemModelKey = NamespacedKey.fromString(itemModel);
                meta.setItemModel(itemModelKey);
            }

            // Decide which colour to use: override takes precedence
            String colorHex = (overrideColor != null && !overrideColor.isEmpty()) ? overrideColor : singleBalloonType.getColor();

            if (colorHex != null) {
                if (singleBalloonType.getMaterial().startsWith(LEATHER_MATERIAL_PREFIX)) {
                    if (colorHex.equalsIgnoreCase("potion")) {
                        Logger.logWarning(String.format(Languages.getMessage("material-not-dyeable"), material));
                        item.setItemMeta(meta);
                        return item;
                    }
                    if (meta instanceof LeatherArmorMeta leatherArmorMeta) {
                        leatherArmorMeta.setColor(Color.hexToColor(colorHex));
                        item.setItemMeta(leatherArmorMeta);
                        return item;
                    }
                } else if (material == Material.FIREWORK_STAR && meta instanceof org.bukkit.inventory.meta.FireworkEffectMeta fireworkMeta) {
                    org.bukkit.FireworkEffect effect = org.bukkit.FireworkEffect.builder()
                            .withColor(Color.hexToColor(colorHex))
                            .build();
                    fireworkMeta.setEffect(effect);
                    item.setItemMeta(fireworkMeta);
                    return item;
                }
            }
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     *                  Checks if a balloon needs to be removed or added (existing API)
     * @param player    The player to check, type org.bukkit.entity.Player
     * @param balloonID The balloon ID to check, type java.lang.String
     */
    public static void checkBalloonRemovalOrAdd(final Player player, final String balloonID) {
        checkBalloonRemovalOrAdd(player, balloonID, null);
    }

    /**
     *                  Checks if a balloon needs to be removed or added with optional color override
     * @param player        The player to check, type org.bukkit.entity.Player
     * @param balloonID     The balloon ID to check, type java.lang.String
     * @param overrideColor Optional hex color override (e.g. "#RRGGBB")
     */
    public static void checkBalloonRemovalOrAdd(final Player player, final String balloonID, final String overrideColor) {
        new BukkitRunnable() {
            public void run() {
                // Gets and checks if the player has a balloon already
                SingleBalloon initialBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
                if (initialBalloon != null) return;

                // Remove the balloon from the player
                SingleBalloonManagement.removeBalloon(player, null);

                // Create a new balloon and add it to the player/start the runnables and add the player to the maps
                SingleBalloon balloon = new SingleBalloon(player, balloonID, overrideColor);
                balloon.runTaskTimer(Bloons.getInstance(), 0L, 1L);
                Bloons.getPlayerSingleBalloons().put(player.getUniqueId(), balloon);
                Bloons.getPlayerSingleBalloonID().put(player.getUniqueId(), balloonID);

            }
        }.runTaskLater(Bloons.getInstance(), 1L);
    }
}
