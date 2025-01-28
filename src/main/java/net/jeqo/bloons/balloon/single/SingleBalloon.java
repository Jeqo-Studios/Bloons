package net.jeqo.bloons.balloon.single;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.BalloonConfiguration;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.message.Languages;
import net.jeqo.bloons.management.SingleBalloonManagement;
import net.jeqo.bloons.colors.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private ModeledEntity modeledEntity;
    private ActiveModel activeModel;
    private AnimationHandler animationHandler;
    private final String defaultIdleAnimationID = "idle";

    /** Variables used for the movement of the balloon **/
    private Location playerLocation;
    private Location moveLocation;

    private int ticks = 0;
    private float targetYaw = 0.0F;

    // A prefix that is needed for dyable materials
    private static final String LEATHER_MATERIAL_PREFIX = "LEATHER_";

    /**
     *                      Constructor for the SingleBalloon class
     * @param player        The player to attach the balloon to, type org.bukkit.entity.Player
     * @param balloonID     The ID of the balloon to attach to the player, type java.lang.String
     */
    public SingleBalloon(Player player, String balloonID) {
        this.setPlayer(player);
        this.setType(Bloons.getBalloonCore().getSingleBalloonByID(balloonID));

        if (this.getType().getMegModelID() == null) {
            this.setVisual(getConfiguredBalloonVisual(balloonID));
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
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
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
        this.setArmorStand(this.getPlayerLocation().getWorld().spawn(this.getPlayerLocation(), ArmorStand.class));
        this.getArmorStand().setBasePlate(false);
        this.getArmorStand().setVisible(false);
        this.getArmorStand().setInvulnerable(true);
        this.getArmorStand().setCanPickupItems(false);
        this.getArmorStand().setGravity(false);
        this.getArmorStand().setSmall(false);
        this.getArmorStand().setMarker(true);
        this.getArmorStand().setCollidable(false);
        if (this.getType().getMegModelID() == null) {
            this.getArmorStand().getEquipment().setHelmet(this.getVisual());
        } else {
            try {
                // Create the entity and tag it onto the armor stand
                ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(this.getArmorStand());
                ActiveModel activeModel = ModelEngineAPI.createActiveModel(this.getType().getMegModelID());

                modeledEntity.addModel(activeModel, true);

                // Set the animation handler to the one of the active model
                this.setAnimationHandler(activeModel.getAnimationHandler());

                // If an idle animation exists, play it initially
                this.getAnimationHandler().playAnimation(this.getDefaultIdleAnimationID(), 0.3, 0.3, 1,true);
            } catch (Exception e) {
                Logger.logError("An error occurred while creating the MEG model for the balloon " + this.getType().getId() + "! This is because a MEG model error occurred.");
                e.printStackTrace();
            }
        }
        this.getArmorStand().setCustomName(BalloonConfiguration.BALLOON_ARMOR_STAND_ID);
    }

    /**
     * Initializes the balloon's lead to the player (chicken entity)
     */
    public void initializeBalloonLead() {
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
        Vector vector = playerLocation.toVector().subtract(this.getMoveLocation().toVector());
        vector.multiply(0.3D);
        this.setMoveLocation(this.getMoveLocation().add(vector));
        double vectorZ = vector.getZ() * 50.0D * -1.0D;
        double vectorX = vector.getX() * 50.0D * -1.0D;
        // Create EulerAngle to tilt parts of the armor body
        EulerAngle tiltAngle = new EulerAngle(Math.toRadians(vectorZ), Math.toRadians(playerLocation.getYaw()), Math.toRadians(vectorX));

        // Set the pose(s) of the armor stand
        ArmorStand armorStand = this.getArmorStand();

        // Set the pose of only the head regardless of the model type
        armorStand.setHeadPose(tiltAngle);

        // Only set the entire pose of the armor stand if it uses MEG, this is to reduce lag across the server
        // when having 100's of models/armor stands used simultaneously
        if (this.getType().getMegModelID() != null) {
            armorStand.setBodyPose(tiltAngle);
            armorStand.setLeftArmPose(tiltAngle);
            armorStand.setRightArmPose(tiltAngle);
            armorStand.setLeftLegPose(tiltAngle);
            armorStand.setRightLegPose(tiltAngle);
        }

        // Teleport the balloon to the move location and set the player location yaw
        this.teleport(this.getMoveLocation());

        // If the balloon armor stand is more than 5 blocks away, teleport to player location
        if (this.getArmorStand().getLocation().distance(playerLocation) > 5.0D) {
            this.teleport(playerLocation);
        }

        // If all parts of a MEG balloon exist and the idle animation exists and it isn't playing, play it
        if (this.getAnimationHandler() != null) {
            if (this.getAnimationHandler().getAnimation(this.getDefaultIdleAnimationID()) != null) {
                if (!this.getAnimationHandler().isPlayingAnimation(this.getDefaultIdleAnimationID())) {
                    this.getAnimationHandler().playAnimation(this.getDefaultIdleAnimationID(), 0.3, 0.3, 1,true);
                }
            }
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
        if (this.getModeledEntity() != null) {
            // Remove the MEG model if it exists
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(this.getArmorStand());
            modeledEntity.removeModel(this.getType().getMegModelID());
        }
        this.getArmorStand().remove();
        this.getChicken().remove();
        super.cancel();
    }

    /**
     * Spawns the particle effect when the balloon is removed
     */
    public void spawnRemoveParticle() {
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
     * @param balloonID     The balloon ID to get the visual appearance of, type java.lang.String
     * @return              The item object that contains the configured balloon model, returns a barrier if there is an issue, type org.bukkit.inventory.ItemStack
     */
    public ItemStack getConfiguredBalloonVisual(String balloonID) {
        SingleBalloonType singleBalloonType = Bloons.getBalloonCore().getSingleBalloonByID(balloonID);

        // If there isn't a configuration for the balloon, log an error and return null
        if (singleBalloonType == null) {
            Logger.logError(String.format(Languages.getMessage("balloon-not-set"), balloonID));
            return new ItemStack(Material.BARRIER);
        }

        // If the material of the balloon is not set, log an error and return null
        if (singleBalloonType.getMaterial() == null) {
            Logger.logError(String.format(Languages.getMessage("material-not-set"), balloonID));
            return new ItemStack(Material.BARRIER);
        }

        Material material = Material.getMaterial(singleBalloonType.getMaterial());

        // If the material is not valid, log an error and return null
        if (material == null) {
            Logger.logError(String.format(Languages.getMessage("material-not-valid"), balloonID, singleBalloonType.getMaterial()));
            return new ItemStack(Material.BARRIER);
        }

        // Generate the item and set the custom model data meta
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(singleBalloonType.getCustomModelData());

        // If the color of the balloon is not set, log an error and return null
        if (singleBalloonType.getColor() != null && singleBalloonType.getMaterial().startsWith(LEATHER_MATERIAL_PREFIX)) {
            // If the color of the balloon is set to potion, log a warning and return null
            if (singleBalloonType.getColor().equalsIgnoreCase("potion")) {
                Logger.logWarning(String.format(Languages.getMessage("material-not-dyeable"), material));
                return item;
            }

            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(Color.hexToColor(singleBalloonType.getColor()));
        }

        // Finally, set the item meta
        item.setItemMeta(meta);

        return item;
    }

    /**
     *                  Checks if a balloon needs to be removed or added
     * @param player    The player to check, type org.bukkit.entity.Player
     * @param balloonID The balloon ID to check, type java.lang.String
     */
    public static void checkBalloonRemovalOrAdd(final Player player, final String balloonID) {
        new BukkitRunnable() {
            public void run() {
                // Gets and checks if the player has a balloon already
                SingleBalloon initialBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
                if (initialBalloon != null) return;

                // Remove the balloon from the player
                SingleBalloonManagement.removeBalloon(player, null);

                // Create a new balloon and add it to the player/start the runnables and add the player to the maps
                SingleBalloon balloon = new SingleBalloon(player, balloonID);
                balloon.runTaskTimer(Bloons.getInstance(), 0L, 1L);
                Bloons.getPlayerSingleBalloons().put(player.getUniqueId(), balloon);
                Bloons.getPlayerSingleBalloonID().put(player.getUniqueId(), balloonID);

            }
        }.runTaskLater(Bloons.getInstance(), 1L);
    }
}
