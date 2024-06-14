package net.jeqo.bloons.balloon.multipart.nodes;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Handles the movement and functionality of a single node, model, or armor stand in a multipart balloon
 */
@Getter @Setter
public class ModelNode {
    public ModelNodeVector pointA;
    public ModelNodeVector pointB = new ModelNodeVector();

    public ModelNode parent = null;
    public ModelNode child = null;

    ArmorStand balloonArmorStand;

    float index;
    float length;
    private MultipartBalloonType balloonType;
    private Player balloonOwner;
    double maxNodeJointAngle;
    double yAxisInterpolation;
    double turningSplineInterpolation;

    /**
     * Builder for creating lead segment.
     * @param x X axis position.
     * @param z Z axis position.
     * @param length Length of segment.
     * @param index Index number of segment.
     */
    public ModelNode(float x, float y, float z, float length, int index, MultipartBalloonType balloonType, Player balloonOwner, double maxNodeJointAngle, double yAxisInterpolation, double turningSplineInterpolation) {
        this.setLength(length);
        this.setIndex(index);
        this.setBalloonType(balloonType);
        this.setBalloonOwner(balloonOwner);
        this.setPointA(new ModelNodeVector(x, y, z));
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setYAxisInterpolation(yAxisInterpolation);
        this.setTurningSplineInterpolation(turningSplineInterpolation);

        this.setBalloonArmorStand(this.getBalloonOwner().getWorld().spawn(new Location(this.getBalloonOwner().getWorld(), x, balloonOwner.getLocation().getY() + 2, z), ArmorStand.class)); // Change Y value to the right height
        this.getBalloonArmorStand().setVisible(false);
        this.getBalloonArmorStand().setInvulnerable(true);
        this.getBalloonArmorStand().setInvisible(true);
        this.getBalloonArmorStand().setSilent(true);
        this.getBalloonArmorStand().setCollidable(false);
        this.getBalloonArmorStand().setLeashHolder(balloonOwner);

        float dx = length * (float)cos(0);
        float dz = length * (float)sin(0);
        this.getPointB().set(this.getPointA().x + dx, this.getPointA().y, this.getPointA().z + dz);
    }

    /**
     * Builder for following segments.
     * @param parent Leading segment.
     * @param length Length of segment.
     * @param index Index number.
     */
    public ModelNode(ModelNode parent, float length, int index, MultipartBalloonType balloonType, Player balloonOwner, double maxNodeJointAngle, double yAxisInterpolation, double turningSplineInterpolation) {
        this.setParent(parent);
        this.setPointA(parent.getPointB().copy());
        this.setLength(length);
        this.setIndex(index);
        this.setBalloonType(balloonType);
        this.setBalloonOwner(balloonOwner);
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setYAxisInterpolation(yAxisInterpolation);
        this.setTurningSplineInterpolation(turningSplineInterpolation);

        this.setBalloonArmorStand(this.getBalloonOwner().getWorld().spawn(new Location(this.getBalloonOwner().getWorld(), this.getPointA().x, balloonOwner.getLocation().getY() + 2, this.getPointA().z), ArmorStand.class)); // Change Y value to the right height
        this.getBalloonArmorStand().setVisible(false);
        this.getBalloonArmorStand().setInvulnerable(true);
        this.getBalloonArmorStand().setInvisible(true);
        this.getBalloonArmorStand().setSilent(true);
        this.getBalloonArmorStand().setCollidable(false);
        this.getBalloonArmorStand().setLeashHolder(balloonOwner);

        float dx = length * (float)cos(0);
        float dz = length * (float)sin(0);
        this.getPointB().set(this.getPointA().x + dx, this.getPointA().z, this.getPointA().z + dz);
    }

    /**
     * Follows to the child's point A location.
     */
    public void follow() {
        float targetX = this.getChild().getPointB().x;
        float targetY = this.getChild().getPointB().y;
        float targetZ = this.getChild().getPointB().z;
        this.follow(targetX, targetY, targetZ);
    }

    /**
     * Makes point A follow to the desired location.
     * @param targetX Target X axis.
     * @param targetY Target Y axis.
     * @param targetZ Target Z axis.
     */
    public void follow(float targetX, float targetY, float targetZ) {
        ModelNodeVector target = new ModelNodeVector(targetX, targetY, targetZ);
        ModelNodeVector dir = ModelNodeVector.subtract(target, this.getPointB());

        double targetAngle = dir.heading();
        if (this.getChild() != null){
            double childAngle = this.getChild().heading();

            double difference = Math.abs(targetAngle - childAngle);

            if (difference > Math.PI) {
                difference = 2 * Math.PI - difference;
            }

            int sign = (targetAngle - childAngle >= 0 && targetAngle - childAngle <= Math.toRadians(180)) || (targetAngle - childAngle <= Math.toRadians(-180) && targetAngle - childAngle >= Math.toRadians(-360)) ? 1 : -1;

            double maxAngle = Math.toRadians(this.getMaxNodeJointAngle()); // Change this based on how much freedom you want (35 is a good number)

            if (difference > maxAngle){
                targetAngle = (childAngle + (maxAngle * sign)) % (Math.PI * 2.0);
            }
        }

        this.setPointA(target);

        // Smoothly interpolate the angle
        double currentAngle = this.heading();
        double interpolatedAngle = lerpAngle(currentAngle, targetAngle, this.getTurningSplineInterpolation()); // Higher = snappier Lower = less snappy

        double interpolatedDx = this.getLength() * cos((float) interpolatedAngle);
        double interpolatedDz = this.getLength() * sin((float) interpolatedAngle);

        double interpolatedY = lerpVal(this.getPointB().y, this.getPointA().y, this.getYAxisInterpolation()); // Change this to make it smoother or to make it more sudden, this seems to be the sweet spot

        this.getPointB().set((float) (this.getPointA().x - interpolatedDx), (float) interpolatedY, (float) (this.getPointA().z - interpolatedDz));
    }

    /**
     * Linear interpolation function for angles.
     * @param startAngle The starting angle in radians.
     * @param endAngle The ending angle in radians.
     * @param t The interpolation factor, ranging from 0 to 1.
     * @return The interpolated angle between startAngle and endAngle.
     */
    private double lerpAngle(double startAngle, double endAngle, double t) {
        double difference = endAngle - startAngle;
        if (difference > Math.PI) {
            difference -= 2 * Math.PI;
        } else if (difference < -Math.PI) {
            difference += 2 * Math.PI;
        }
        return startAngle + t * difference;
    }

    /**
     * Linear interpolation function for values.
     * @param startVal The starting value.
     * @param endVal The ending value.
     * @param t The interpolation factor, ranging from 0 to 1.
     * @return The interpolated value between startVal and endVal.
     */
    private double lerpVal(double startVal, double endVal, double t) {
        double difference = endVal - startVal;
        return startVal + t * difference;
    }

    /**
     * Gets the heading of two node vectors
     * @return The heading of the two node vectors as a float
     */
    public float heading(){
        return ModelNodeVector.subtract(this.getPointA(), this.getPointB()).heading();
    }

    /**
     * Calculates the head pose of the armor stand.
     * @param pointA The first point.
     * @param pointB The second point.
     * @return The Euler angle of the armor stand.
     */
    public EulerAngle calculateHeadPose(Vector pointA, Vector pointB) {
        Location loc = new Location(this.getBalloonOwner().getWorld(), pointA.getX(), pointA.getY(), pointA.getZ());
        loc.setDirection(pointB.subtract(pointA));
        Vector direction = loc.getDirection();

        double yaw = Math.atan2(-direction.getX(), direction.getZ()) + Math.toRadians(180.0);
        double pitch = Math.asin(direction.getY());
        double roll = 0.0;

        return new EulerAngle(pitch, yaw, roll);
    }

    /**
     * Sets the correct position and item in the armor stand.
     */
    public void show() {
        /*
         * This will set the head of the armor stand based on the balloon type
         */
        if (this.getIndex() == this.getBalloonType().getNodeCount() - 1) {
            this.getBalloonArmorStand().setItem(EquipmentSlot.HEAD, this.getBalloonType().getHeadModel().getFinalizedModel());
        } else if (this.getIndex() == 0) {
            this.getBalloonArmorStand().setItem(EquipmentSlot.HEAD, this.getBalloonType().getTailModel().getFinalizedModel());
        } else {
            this.getBalloonArmorStand().setItem(EquipmentSlot.HEAD, this.getBalloonType().getBodyModel().getFinalizedModel());
        }

        Vector pointAVector = new Vector(this.getPointA().x, this.getPointA().y, this.getPointA().z);
        Vector pointBVector = new Vector(this.getPointB().x, this.getPointB().y, this.getPointB().z);

        // Set the direction for the armor stand to face
        this.getBalloonArmorStand().setHeadPose(calculateHeadPose(pointAVector, pointBVector));
        // Teleport it to the correct location
        this.getBalloonArmorStand().teleport(new Location(this.getBalloonOwner().getWorld(), (this.getPointA().x + this.getPointB().x) / 2.0, (this.getPointA().y + this.getPointB().y) / 2.0, (this.getPointA().z + this.getPointB().z) / 2.0)); // Change Y value to the right height
    }

    public void destroy() {
        if (this.getBalloonArmorStand() != null && !this.getBalloonArmorStand().isDead()) {
            this.getBalloonArmorStand().remove();
        }
    }
}
