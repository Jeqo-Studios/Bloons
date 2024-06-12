package net.jeqo.bloons.balloon.single;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.configuration.BalloonConfiguration;
import net.jeqo.bloons.events.balloon.SingleBalloonEquipEvent;
import net.jeqo.bloons.events.balloon.SingleBalloonForceUnequipEvent;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.BalloonManagement;
import net.jeqo.bloons.utils.ColorManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
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
    /** Al physical elements of balloon **/
    private Player player;
    private ItemStack balloonVisual;
    private ArmorStand balloonArmorStand;
    public Chicken balloonChicken;

    /** Location Data **/
    private Location playerLocation;
    private Location moveLocation;

    /** Movement Data **/
    private int ticks = 0;
    private float targetYaw = 0.0F;

    public SingleBalloon(Player player, String balloonID) {
        this.setPlayer(player);

        // Configure the balloon visual elements
        this.setBalloonVisual(getConfiguredBalloonVisual(balloonID));
    }

    /**
     * What runs inside the extended bukkit runnable
     * Core functionality of how the balloon moves
     */
    public void run() {
        if (this.getBalloonArmorStand() == null) initializeBalloon();

        Location playerLocation = this.getPlayerLocation();
        playerLocation.setYaw(this.getPlayerLocation().getYaw());

        // If the ticks reach 20, set back to 0 and set a new target yaw
        if (this.getTicks() == 20) {
            this.setTargetYaw(ThreadLocalRandom.current().nextInt(10) - 5);
            this.setTicks(0);
        }

        if (this.getTargetYaw() > playerLocation.getYaw()) {
            playerLocation.setYaw(playerLocation.getYaw() + 0.2F);
        } else if (this.getTargetYaw() < playerLocation.getYaw()) {
            playerLocation.setYaw(playerLocation.getYaw() - 0.2F);
        }

        this.setMoveLocation(this.getBalloonArmorStand().getLocation().subtract(0.0D, 2.0D, 0.0D).clone());

        Vector vector = playerLocation.toVector().subtract(this.getMoveLocation().toVector());
        vector.multiply(0.3D);
        this.setMoveLocation(this.getMoveLocation().add(vector));
        double vectorZ = vector.getZ() * 50.0D * -1.0D;
        double vectorX = vector.getX() * 50.0D * -1.0D;
        this.getBalloonArmorStand().setHeadPose(new EulerAngle(Math.toRadians(vectorZ), Math.toRadians(playerLocation.getYaw()), Math.toRadians(vectorX)));

        this.teleport(this.getMoveLocation());
        this.setPlayerLocation(this.getPlayer().getLocation());
        this.getPlayerLocation().setYaw(playerLocation.getYaw());
        this.setTicks(this.getTicks() + 1);
    }

    /**
     * Cancels the current bukkit runnable instance and kills off the entities
     * @throws IllegalStateException If the task has already been cancelled
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
     * Teleports the balloon's entities to a specific location
     * @param location The location to teleport the balloon to
     */
    private void teleport(Location location) {
        this.getBalloonArmorStand().teleport(location.add(0.0D, 2.0D, 0.0D));
        this.getBalloonChicken().teleport(location.add(0.0D, 1.2D, 0.0D));
    }

    /**
     * Initializes the balloon. Sets the current players location, and initializes the armor stand, and chicken entities
     */
    private void initializeBalloon() {
        this.setPlayerLocation(this.getPlayer().getLocation());
        this.getPlayerLocation().setYaw(0.0F);

        ItemMeta meta = this.getBalloonVisual().getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.getBalloonVisual().setItemMeta(meta);

        this.initializeBalloonArmorStand();
        this.initializeBalloonChicken();
    }

    /**
     * Gets the item stack object of the visual appearance of the balloon
     * @param balloonID The balloon ID to get the visual appearance of
     * @return The item stack object of the visual appearance of the balloon
     */
    public ItemStack getConfiguredBalloonVisual(String balloonID) {
        MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());

        ConfigurationSection balloonConfiguration = Bloons.getInstance().getConfig().getConfigurationSection("balloons." + balloonID);

        if (balloonConfiguration == null) {
            Logger.logWarning("The balloon " + balloonID + " is not set in the configuration!");
            return null;
        }

        if (balloonConfiguration.getString("material") == null) {
            Logger.logWarning("The material of the balloon " + balloonID + " is not set!");
            return null;
        }

        ItemStack item = new ItemStack(Material.valueOf(balloonConfiguration.getString("material")));
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(balloonConfiguration.getInt("custom-model-data"));

        if (messageTranslations.getString("balloons." + balloonID + ".color") != null) {
            if (!messageTranslations.getString("balloons." + balloonID + ".color").equalsIgnoreCase("potion")) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
                leatherArmorMeta.setColor(ColorManagement.hexToColor(messageTranslations.getString("balloons." + balloonID + ".color")));
            } else {
                Logger.logWarning("The color of the balloonVisual " + balloonID + " is set, but the material is not a leather item!");
            }
        }
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Initializes the balloon's armor stand entity
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
     * Initializes the balloon's chicken entity
     */
    public void initializeBalloonChicken() {
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
     * Checks if a balloon needs to be removed or added
     * @param player The player to check
     * @param balloonID The balloon ID to check
     */
    public static void checkBalloonRemovalOrAdd(final Player player, final String balloonID) {
        new BukkitRunnable() {
            public void run() {
                SingleBalloon initialBalloon = Bloons.playerSingleBalloons.get(player.getUniqueId());
                if (initialBalloon != null) return;

                SingleBalloonForceUnequipEvent unequipEvent = new SingleBalloonForceUnequipEvent(player, null);
                unequipEvent.callEvent();

                if (unequipEvent.isCancelled()) return;

                BalloonManagement.removeBalloon(player, null);

                SingleBalloonEquipEvent equipEvent = new SingleBalloonEquipEvent(player, balloonID);
                equipEvent.callEvent();

                if (equipEvent.isCancelled()) return;

                SingleBalloon balloon = new SingleBalloon(player, balloonID);
                balloon.runTaskTimer(Bloons.getInstance(), 0L, 1L);
                Bloons.playerSingleBalloons.put(player.getUniqueId(), balloon);
                Bloons.playerSingleBalloonID.put(player.getUniqueId(), balloonID);

            }
        }.runTaskLater(Bloons.getInstance(), 1L);
    }
}
