package net.jeqo.bloons.data;

import net.jeqo.bloons.Bloons;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import net.jeqo.bloons.utils.Utils;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class BalloonOwner extends BukkitRunnable {
    private final Player player;
    private final ItemStack balloon;
    private final String balloonId;
    private ArmorStand armorStand;
    public Chicken chicken;
    private Location playerLocation;
    private Location moveLocation;
    private int ticks = 0;
    private float targetYaw = 0.0F;

    public BalloonOwner(Player player, String balloonId) {
        this.player = player;
        this.balloonId = balloonId;
        ConfigurationSection configuration = Bloons.getInstance().getConfig().getConfigurationSection("balloons." + balloonId);
        assert configuration != null;
        ItemStack item = new ItemStack(Material.valueOf(configuration.getString("material")));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(configuration.getInt("custom-model-data"));
        if (Bloons.getString("balloons." + balloonId + ".color") != null) {
            if (!Bloons.getString("balloons." + balloonId + ".color").equalsIgnoreCase("potion")) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
                leatherArmorMeta.setColor(Utils.hexToColor(Bloons.getString("balloons." + balloonId + ".color")));
            } else {
                Utils.warn("The color of the balloon " + balloonId + " is set, but the material is not a leather item!");
            }
        }
        item.setItemMeta(meta);
        this.balloon = item;
    }


    public void run() {
        if (this.armorStand == null) {
            initBalloon();
        }
        Location location = this.player.getLocation();
        location.setYaw(this.playerLocation.getYaw());
        if (this.ticks == 20) {
            this.targetYaw = (ThreadLocalRandom.current().nextInt(10) - 5);
            this.ticks = 0;
        }
        if (this.targetYaw > location.getYaw()) {
            location.setYaw(location.getYaw() + 0.2F);
        } else if (this.targetYaw < location.getYaw()) {
            location.setYaw(location.getYaw() - 0.2F);
        }
        this.moveLocation = this.armorStand.getLocation().subtract(0.0D, 2.0D, 0.0D).clone();
        Vector vector = location.toVector().subtract(this.moveLocation.toVector());
        vector.multiply(0.3D);
        this.moveLocation = this.moveLocation.add(vector);
        double value1 = vector.getZ() * 50.0D * -1.0D;
        double value2 = vector.getX() * 50.0D * -1.0D;
        this.armorStand.setHeadPose(new EulerAngle(Math.toRadians(value1), Math.toRadians(location.getYaw()), Math.toRadians(value2)));
        teleport(this.moveLocation);
        this.playerLocation = this.player.getLocation();
        this.playerLocation.setYaw(location.getYaw());
        this.ticks++;
    }


    public synchronized void cancel() throws IllegalStateException {
        this.armorStand.remove();
        this.chicken.remove();
        super.cancel();
    }

    public void spawnRemoveParticle() {
        this.moveLocation.getWorld().spawnParticle(Particle.CLOUD, this.moveLocation, 5, 0.0D, 0.0D, 0.0D, 0.1D);
    }
    public String getBalloonId() {
        return this.balloonId;
    }

    private void teleport(Location location) {
        this.armorStand.teleport(location.add(0.0D, 2.0D, 0.0D));
        this.chicken.teleport(location.add(0.0D, 1.2D, 0.0D));
    }

    private void initBalloon() {
        this.playerLocation = this.player.getLocation();
        this.playerLocation.setYaw(0.0F);

        ItemMeta meta = this.balloon.getItemMeta();
        meta.addItemFlags(new ItemFlag[] {ItemFlag.HIDE_UNBREAKABLE });
        this.balloon.setItemMeta(meta);

        this.armorStand = (ArmorStand) Objects.requireNonNull(this.playerLocation.getWorld()).spawn(this.playerLocation, ArmorStand.class);
        this.armorStand.setBasePlate(false);
        this.armorStand.setVisible(false);
        this.armorStand.setInvulnerable(true);
        this.armorStand.setCanPickupItems(false);
        this.armorStand.setGravity(false);
        this.armorStand.setSmall(false);
        this.armorStand.setMarker(true);
        this.armorStand.setCollidable(false);
        this.armorStand.getEquipment().setHelmet(this.balloon);
        this.armorStand.setCustomName("4001147");

        this.chicken = (Chicken)this.playerLocation.getWorld().spawn(this.playerLocation, Chicken.class);
        this.chicken.setInvulnerable(true);
        this.chicken.setInvisible(true);
        this.chicken.setSilent(true);
        this.chicken.setBaby();
        this.chicken.setAgeLock(true);
        this.chicken.setAware(false);
        this.chicken.setCollidable(false);
        this.chicken.setLeashHolder(player);
        this.chicken.setCustomName("4001148");
    }


    public static void checkBalloonRemovalOrAdd(final Player player, final String balloonId) {
        (new BukkitRunnable() {
            public void run() {
                BalloonOwner owner = (BalloonOwner) Bloons.playerBalloons.get(player.getUniqueId());
                if (owner != null) {
                    return;
                }
                Utils.removeBalloon(player, owner);
                BalloonOwner balloonOwner = new BalloonOwner(player, balloonId);
                balloonOwner.runTaskTimer((Plugin) Bloons.getInstance(), 0L, 1L);
                Bloons.playerBalloons.put(player.getUniqueId(), balloonOwner);
                Bloons.playerBalloonID.put(player.getUniqueId(), balloonId);

            }
        }).runTaskLater((Plugin) Bloons.getInstance(), 1L);
    }
}