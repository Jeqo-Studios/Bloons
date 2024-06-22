package net.jeqo.bloons.balloon.multipart.nodes;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
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
         *                  Builder for creating lead segment.
     * @param x             X-axis position, type float
     * @param y             Y-axis position, type float
     * @param z             Z-axis position, type float
     * @param length        Length of segment in blocks, type float
     * @param index         Index number of segment, type int
     * @param balloonType   Type of balloon, type net.jeqo.bloons.balloon.multipart.MultipartBalloonType
     * @param balloonOwner  Owner of the balloon, type org.bukkit.entity.Player
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

        // Spawn an armor stand at the provided coordinates and with the players height + 2
        this.initializeArmorStand(x, this.getBalloonOwner().getLocation().getY() + 2, z);

        float dx = length * (float)cos(0);
        float dz = length * (float)sin(0);

        // Calculate the point B location based on the angle and length
        this.getPointB().set(this.getPointA().x + dx, this.getPointA().y, this.getPointA().z + dz);
    }

    /**
     *                  Builder for following segments.
     * @param parent    Leading segment, type net.jeqo.bloons.balloon.multipart.nodes.ModelNode
     * @param length    Length of segment in blocks, type float
     * @param index     Index number in the balloon, type int
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

        // Spawn an armor stand at the point A location with the height of the player + 2
        this.initializeArmorStand(this.getPointA().x, this.getBalloonOwner().getLocation().getY() + 2, this.getPointA().z);

        float dx = length * (float)cos(0);
        float dz = length * (float)sin(0);

        // Calculate the point B location based on the angle and length
        this.getPointB().set(this.getPointA().x + dx, this.getPointA().z, this.getPointA().z + dz);
    }

    /**
     *              Initializes the armor stand with the correct settings for a balloon node.
     * @param x     X-axis position, type double
     * @param y     Y-axis position, type double
     * @param z     Z-axis position, type double
     */
    public void initializeArmorStand(double x, double y, double z) {
        this.setBalloonArmorStand(this.getBalloonOwner().getWorld().spawn(new Location(this.getBalloonOwner().getWorld(), x, y, z), ArmorStand.class));

        this.getBalloonArmorStand().setVisible(false);
        this.getBalloonArmorStand().setInvulnerable(true);
        this.getBalloonArmorStand().setInvisible(true);
        this.getBalloonArmorStand().setSilent(true);
        this.getBalloonArmorStand().setCollidable(false);
    }

    /**
     * Follows to the child's, also known as the previous nodes, point A location.
     */
    public void follow() {
        float targetX = this.getChild().getPointB().x;
        float targetY = this.getChild().getPointB().y;
        float targetZ = this.getChild().getPointB().z;
        this.follow(targetX, targetY, targetZ);
    }

    /**
     *                  Makes point A follow to the desired location.
     * @param targetX   Target X-axis, type float
     * @param targetY   Target Y axis, type float
     * @param targetZ   Target Z axis, type float
     */
    public void follow(float targetX, float targetY, float targetZ) {
        ModelNodeVector target = new ModelNodeVector(targetX, targetY, targetZ);
        ModelNodeVector dir = ModelNodeVector.subtract(target, this.getPointB());

        double targetAngle = dir.heading();

        // If the node has a child, check if the angle between the child and the target angle is greater than the max angle
        if (this.getChild() != null) {
            double childAngle = this.getChild().heading();

            double difference = Math.abs(targetAngle - childAngle);

            if (difference > Math.PI) {
                difference = 2 * Math.PI - difference;
            }

            // Check if the difference is greater than the max angle
            // If it is, set the target angle to the max angle
            int sign = (targetAngle - childAngle >= 0 && targetAngle - childAngle <= Math.toRadians(180)) || (targetAngle - childAngle <= Math.toRadians(-180) && targetAngle - childAngle >= Math.toRadians(-360)) ? 1 : -1;

            double maxAngle = Math.toRadians(this.getMaxNodeJointAngle()); // Change this based on how much freedom you want (35 is a good number)

            if (difference > maxAngle){
                targetAngle = (childAngle + (maxAngle * sign)) % (Math.PI * 2.0);
            }
        }

        // Set the target point A location
        this.setPointA(target);

        // Smoothly interpolate the angle
        double currentAngle = this.heading();
        double interpolatedAngle = lerpAngle(currentAngle, targetAngle, this.getTurningSplineInterpolation()); // Higher = snappier Lower = less snappy

        // Interpolate the x and z values based on the angle
        double interpolatedDx = this.getLength() * cos((float) interpolatedAngle);
        double interpolatedDz = this.getLength() * sin((float) interpolatedAngle);

        // Smoothly interpolate the y value
        double interpolatedY = lerpVal(this.getPointB().y, this.getPointA().y, this.getYAxisInterpolation());   // Change this to make it smoother or
                                                                                                                // to make it more sudden, this seems to be the sweet spot

        // Set the new point B location
        this.getPointB().set((float) (this.getPointA().x - interpolatedDx), (float) interpolatedY, (float) (this.getPointA().z - interpolatedDz));
    }

    /**
     *                              Linear interpolation function for angles.
     * @param startAngle            The starting angle in radians, type double
     * @param endAngle              The ending angle in radians, type double
     * @param interpolationFactor   The interpolation factor, ranging from 0 to 1, type double
     * @return                      The interpolated angle between startAngle and endAngle, type double
     */
    private double lerpAngle(double startAngle, double endAngle, double interpolationFactor) {
        double difference = endAngle - startAngle;

        // If the difference is greater than PI, subtract 2PI from the difference
        if (difference > Math.PI) {
            difference -= 2 * Math.PI;
        } else if (difference < -Math.PI) {
            difference += 2 * Math.PI;
        }

        // Return the interpolated angle
        return startAngle + interpolationFactor * difference;
    }

    /**
     *                              Linear interpolation function for values.
     * @param startVal              The starting value. type double
     * @param endVal                The ending value, type double
     * @param interpolationFactor   The interpolation factor, ranging from 0 to 1, type double
     * @return                      The interpolated value between startVal and endVal, type double
     */
    private double lerpVal(double startVal, double endVal, double interpolationFactor) {
        // Calculate the difference between the two values and return the interpolated value
        double difference = endVal - startVal;
        return startVal + interpolationFactor * difference;
    }

    /**
     *          Gets the heading of two node vectors
     * @return  The heading of the two node vectors, type float
     */
    public float heading(){
        return ModelNodeVector.subtract(this.getPointA(), this.getPointB()).heading();
    }

    /**
     *                  Calculates the head pose of the armor stand.
     * @param pointA    The first point, type org.bukkit.util.Vector
     * @param pointB    The second point, type org.bukkit.util.Vector
     * @return          The Euler angle of the armor stand, type org.bukkit.util.EulerAngle
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
    public void display() {
        EquipmentSlot slot = EquipmentSlot.HEAD;
        // Sets the segments finalized models based on their position in the balloon
        if (this.getIndex() == this.getBalloonType().getNodeCount() - 1) {
            // Set the head model
            this.getBalloonArmorStand().setItem(slot, this.getBalloonType().getHeadModel().getFinalizedModel());
        } else if (this.getIndex() == 0) {
            // Set the tail model
            this.getBalloonArmorStand().setItem(slot, this.getBalloonType().getTailModel().getFinalizedModel());
        } else {
            // Set the body model
            this.getBalloonArmorStand().setItem(slot, this.getBalloonType().getBodyModel().getFinalizedModel());
        }

        // Creates a new Bukkit vector based on the position of the two points
        Vector pointAVector = new Vector(this.getPointA().x, this.getPointA().y, this.getPointA().z);
        Vector pointBVector = new Vector(this.getPointB().x, this.getPointB().y, this.getPointB().z);

        // Set the direction for the armor stand to face
        this.getBalloonArmorStand().setHeadPose(calculateHeadPose(pointAVector, pointBVector));
        // Teleport it to the correct location
        this.getBalloonArmorStand().teleport(new Location(this.getBalloonOwner().getWorld(), (this.getPointA().x + this.getPointB().x) / 2.0, (this.getPointA().y + this.getPointB().y) / 2.0, (this.getPointA().z + this.getPointB().z) / 2.0)); // Change Y value to the right height
    }

    /**
     * Destroys the armor stand and removes it from the world
     */
    public void destroy() {
        if (this.getBalloonArmorStand() != null && !this.getBalloonArmorStand().isDead()) {
            this.getBalloonArmorStand().remove();
        }
    }
}
