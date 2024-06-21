package net.jeqo.bloons.balloon.single;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.BalloonConfiguration;
import net.jeqo.bloons.events.balloon.single.SingleBalloonEquipEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonForceUnequipEvent;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.LanguageManagement;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.jeqo.bloons.utils.ColorManagement;
import net.kyori.adventure.text.Component;
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
    private Player player;
    private ItemStack balloonVisual;
    private ArmorStand balloonArmorStand;
    public Chicken balloonChicken;

    private Location playerLocation;
    private Location moveLocation;

    private int ticks = 0;
    private float targetYaw = 0.0F;

    private static final String leatherMaterialPrefix = "LEATHER_"; // A constant to define a dyeable material

    /**
     *                      Constructor for the SingleBalloon class
     * @param player        The player to attach the balloon to, type org.bukkit.entity.Player
     * @param balloonID     The ID of the balloon to attach to the player, type java.lang.String
     */
    public SingleBalloon(Player player, String balloonID) {
        this.setPlayer(player);

        // Configure the balloon visual elements
        this.setBalloonVisual(this.getConfiguredBalloonVisual(balloonID));
    }

    /**
     * What runs inside the extended bukkit runnable, it's
     * the control center of the core functionality of how the balloon moves
     */
    public void run() {
        // If the balloon armor stand is null, initialize the balloon
        if (this.getBalloonArmorStand() == null) initializeBalloon();

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
        this.setMoveLocation(this.getBalloonArmorStand().getLocation().subtract(0.0D, 2.0D, 0.0D).clone());

        // Set the vector to the player location minus the move location
        Vector vector = playerLocation.toVector().subtract(this.getMoveLocation().toVector());
        vector.multiply(0.3D);
        this.setMoveLocation(this.getMoveLocation().add(vector));
        double vectorZ = vector.getZ() * 50.0D * -1.0D;
        double vectorX = vector.getX() * 50.0D * -1.0D;
        this.getBalloonArmorStand().setHeadPose(new EulerAngle(Math.toRadians(vectorZ), Math.toRadians(playerLocation.getYaw()), Math.toRadians(vectorX)));

        // Teleport the balloon to the move location and set the player location yaw
        this.teleport(this.getMoveLocation());
        this.setPlayerLocation(this.getPlayer().getLocation());
        this.getPlayerLocation().setYaw(playerLocation.getYaw());
        this.setTicks(this.getTicks() + 1);
    }

    /**
     *                                  Cancels the current bukkit runnable instance and kills off the entities
     * @throws IllegalStateException    If the task has already been cancelled
     */
    public synchronized void cancel() throws IllegalStateException {
        this.getBalloonArmorStand().remove();
        this.getBalloonChicken().remove();
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
        this.getBalloonArmorStand().teleport(location.add(0.0D, 2.0D, 0.0D));
        this.getBalloonChicken().teleport(location.add(0.0D, 1.2D, 0.0D));
    }

    /**
     * Initializes the balloon and its subcomponents.
     * Sets the current players location, and initializes the armor stand, and chicken entities
     */
    private void initializeBalloon() {
        this.setPlayerLocation(this.getPlayer().getLocation());
        this.getPlayerLocation().setYaw(0.0F);

        // Create and set the balloons visual appearance/model
        ItemMeta meta = this.getBalloonVisual().getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.getBalloonVisual().setItemMeta(meta);

        // Initialize the armor stand and lead to the player
        this.initializeBalloonArmorStand();
        this.initializeBalloonLead();
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
            Logger.logError(String.format(LanguageManagement.getMessage("balloon-not-set"), balloonID));
            return new ItemStack(Material.BARRIER);
        }

        // If the material of the balloon is not set, log an error and return null
        if (singleBalloonType.getMaterial() == null) {
            Logger.logError(String.format(LanguageManagement.getMessage("material-not-set"), balloonID));
            return new ItemStack(Material.BARRIER);
        }

        Material material = Material.getMaterial(singleBalloonType.getMaterial());

        // If the material is not valid, log an error and return null
        if (material == null) {
            Logger.logError(String.format(LanguageManagement.getMessage("material-not-valid"), balloonID, singleBalloonType.getMaterial()));
            return new ItemStack(Material.BARRIER);
        }

        // Generate the item and set the custom model data meta
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(singleBalloonType.getCustomModelData());

        // If the color of the balloon is not set, log an error and return null
        if (singleBalloonType.getColor() != null && singleBalloonType.getMaterial().startsWith(leatherMaterialPrefix)) {
            // If the color of the balloon is set to potion, log a warning and return null
            if (singleBalloonType.getColor().equalsIgnoreCase("potion")) {
                Logger.logWarning(String.format(LanguageManagement.getMessage("material-not-dyeable"), material));
                return item;
            }

            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(ColorManagement.hexToColor(singleBalloonType.getColor()));
        }

        // Finally, set the item meta
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Initializes the balloon's armor stand entity with the proper configurations
     */
    public void initializeBalloonArmorStand() {
        this.setBalloonArmorStand(this.getPlayerLocation().getWorld().spawn(this.getPlayerLocation(), ArmorStand.class));
        this.getBalloonArmorStand().setBasePlate(false);
        this.getBalloonArmorStand().setVisible(false);
        this.getBalloonArmorStand().setInvulnerable(true);
        this.getBalloonArmorStand().setCanPickupItems(false);
        this.getBalloonArmorStand().setGravity(false);
        this.getBalloonArmorStand().setSmall(false);
        this.getBalloonArmorStand().setMarker(true);
        this.getBalloonArmorStand().setCollidable(false);
        this.getBalloonArmorStand().getEquipment().setHelmet(this.getBalloonVisual());
        this.getBalloonArmorStand().customName(Component.text(BalloonConfiguration.BALLOON_ARMOR_STAND_ID));
    }

    /**
     * Initializes the balloon's lead to the player (chicken entity)
     */
    public void initializeBalloonLead() {
        this.setBalloonChicken(this.getPlayerLocation().getWorld().spawn(this.getPlayerLocation(), Chicken.class));
        this.getBalloonChicken().setInvulnerable(true);
        this.getBalloonChicken().setInvisible(true);
        this.getBalloonChicken().setSilent(true);
        this.getBalloonChicken().setBaby();
        this.getBalloonChicken().setAgeLock(true);
        this.getBalloonChicken().setAware(false);
        this.getBalloonChicken().setCollidable(false);
        this.getBalloonChicken().setLeashHolder(this.getPlayer());
        this.getBalloonChicken().customName(Component.text(BalloonConfiguration.BALLOON_CHICKEN_ID));
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

                // Call the unequip event and check if it's cancelled
                SingleBalloonForceUnequipEvent unequipEvent = new SingleBalloonForceUnequipEvent(player, null);
                unequipEvent.callEvent();

                if (unequipEvent.isCancelled()) return;

                // Remove the balloon from the player
                SingleBalloonManagement.removeBalloon(player, null);

                // Call the equip event and check if it's cancelled
                SingleBalloonEquipEvent equipEvent = new SingleBalloonEquipEvent(player, balloonID);
                equipEvent.callEvent();

                if (equipEvent.isCancelled()) return;

                // Create a new balloon and add it to the player/start the runnables and add the player to the maps
                SingleBalloon balloon = new SingleBalloon(player, balloonID);
                balloon.runTaskTimer(Bloons.getInstance(), 0L, 1L);
                Bloons.getPlayerSingleBalloons().put(player.getUniqueId(), balloon);
                Bloons.getPlayerSingleBalloonID().put(player.getUniqueId(), balloonID);

            }
        }.runTaskLater(Bloons.getInstance(), 1L);
    }
}
