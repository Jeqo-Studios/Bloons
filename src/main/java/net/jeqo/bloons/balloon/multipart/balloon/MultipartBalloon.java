package net.jeqo.bloons.balloon.multipart.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.nodes.ModelNode;
import net.jeqo.bloons.configuration.BalloonConfiguration;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The core class to handle the creation of a balloon and the functionality of the balloon
 * tied into the ModelNode utilities
 */
@Getter
public class MultipartBalloon {
    @Setter
    private MultipartBalloonType balloonType;
    @Setter
    private Player balloonOwner;

    @Setter
    private Chicken balloonChicken;

    @Setter
    private ModelNode tentacle;
    @Setter
    private BukkitRunnable runnable;

    private final List<ModelNode> modelNodes = new ArrayList<>();

    /**
     * Initializes the balloons functionality
     */
    public void initialize() {
        // Things that only need to be set up once and not looped over
        ModelNode current = new ModelNode((float) this.getBalloonOwner().getLocation().getX(), (float) this.getBalloonOwner().getLocation().getY() + 2, (float) this.getBalloonOwner().getLocation().getZ(),
                                         (float) ((float) this.getBalloonType().getDistanceBetweenNodes() + this.getBalloonType().getTailNodeOffset()), 0, getBalloonType(), this.getBalloonOwner(),
                                         this.getBalloonType().getMaxNodeJointAngle(), this.getBalloonType().getYAxisInterpolation(), this.getBalloonType().getTurningSplineInterpolation());

        // Add the current node to the list of model nodes
        this.getModelNodes().add(current);

        // Create a new node for each node in the balloon type
        for (int i = 1; i < this.getBalloonType().getNodeCount(); i++) {
            ModelNode next = getModelNode(i, current);
            this.getModelNodes().add(next);
            current.child = next;
            current = next;
        }

        // Set the tentacle to the current node
        this.setTentacle(current);

        // Finally, attach a lead from the player to the balloon front node
        this.initializeBalloonLead();
    }

    /**
     * Initializes the balloon lead, which is a chicken entity that holds the leash for the balloon to the player
     */
    public void initializeBalloonLead() {
        // Location to spawn the lead holder at
        Location location = new Location(this.getBalloonOwner().getWorld(), this.getBalloonOwner().getLocation().getX(), this.getBalloonOwner().getLocation().getY() + 2, this.getBalloonOwner().getLocation().getZ());

        // Configure the chicken entity properties
        this.setBalloonChicken(this.getBalloonOwner().getWorld().spawn(location, Chicken.class));
        this.getBalloonChicken().setInvulnerable(true);
        this.getBalloonChicken().setInvisible(true);
        this.getBalloonChicken().setBaby();
        this.getBalloonChicken().setSilent(true);
        this.getBalloonChicken().setAgeLock(true);
        this.getBalloonChicken().setAware(false);
        this.getBalloonChicken().setCollidable(false);
        this.getBalloonChicken().customName(Component.text(BalloonConfiguration.BALLOON_CHICKEN_ID));
    }

    /**
     *                  Gets the next model node in the balloon based on the current node and the index
     * @param index     The index of the node, type int
     * @param current   The current node, type net.jeqo.bloons.balloon.multipart.nodes.ModelNode
     * @return          The next model node, type net.jeqo.bloons.balloon.multipart.nodes.ModelNode
     */
    private @NotNull ModelNode getModelNode(int index, ModelNode current) {
        ModelNode next;

        // If the index is the last node, create a head node
        if (index == this.getBalloonType().getNodeCount() - 1) {
            next = new ModelNode(current, (float) ((float) this.getBalloonType().getDistanceBetweenNodes() + this.getBalloonType().getHeadNodeOffset()),
                    index, getBalloonType(), this.getBalloonOwner(), this.getBalloonType().getMaxNodeJointAngle(), this.getBalloonType().getYAxisInterpolation(),
                    this.getBalloonType().getTurningSplineInterpolation());

        // Otherwise, create a body node
        } else {
            next = new ModelNode(current, (float) ((float) this.getBalloonType().getDistanceBetweenNodes() + this.getBalloonType().getBodyNodeOffset()),
                    index, getBalloonType(), this.getBalloonOwner(), this.getBalloonType().getMaxNodeJointAngle(), this.getBalloonType().getYAxisInterpolation(),
                    this.getBalloonType().getTurningSplineInterpolation());
        }

        return next;
    }

    /**
     * Runs the balloons' functionality that needs to loop infinitely
     */
    public void run() {
        // Ensure the previous runnable is canceled before creating a new one
        if (this.getRunnable() != null) this.getRunnable().cancel();

        long timeInTicks = 1; // Internally, this stays at one tick to ensure constant updating of positioning

        double speed = this.getBalloonType().getPassiveSineWaveSpeed(); // Adjust the speed of the sine wave
        double amplitude = this.getBalloonType().getPassiveSineWaveAmplitude(); // Adjust the amplitude of the sine wave
        double noseAmplitude = this.getBalloonType().getPassiveNoseSineWaveAmplitude(); // Adjust how much the nose of the first node goes up and down

        this.setRunnable(new BukkitRunnable() {
            double yOffset = 0; // Initial offset for the sine curve

            @Override
            public void run() {
                // Gets the constantly updated owners location
                Location balloonOwnerLocation = getBalloonOwner().getLocation();

                // Calculate the Y offset using a sine function with adjusted amplitude
                double sinValue = Math.sin(yOffset);
                double newY = balloonOwnerLocation.getY() + 2 + amplitude * sinValue; // Adjust the amplitude and offset as needed

                // Adjust the nose amplitude based on the sine value
                double noseOffset = noseAmplitude * sinValue;

                // Calculate the midpoint between Point A and Point B in the leading balloon
                double midpointX = (getTentacle().getPointA().x + getTentacle().getPointB().x) / 2.0;
                double midpointZ = (getTentacle().getPointA().z + getTentacle().getPointA().z) / 2.0;

                // Teleport the chicken holding the leash constantly
                Location leadTeleportPoint = new Location(getBalloonOwner().getWorld(), midpointX, getTentacle().getPointA().y + 1.5, midpointZ);
                getBalloonChicken().teleport(leadTeleportPoint);
                getBalloonChicken().setLeashHolder(getBalloonOwner());

                // Constantly teleport the balloons
                getTentacle().follow((float) balloonOwnerLocation.getX(), (float) (newY + noseOffset), (float) balloonOwnerLocation.getZ());
                getTentacle().display();

                // Make the other segments follow
                ModelNode next = getTentacle().getParent();
                while (next != null) {
                    next.follow();
                    next.display();

                    next = next.getParent();
                }

                // Increment the yOffset based on the speed for the next iteration
                yOffset += speed; // Adjust the speed of the sine wave as needed
            }
        });

        // Constantly run the runnable with the specified time in ticks
        this.getRunnable().runTaskTimer(Bloons.getInstance(), timeInTicks, timeInTicks);
    }

    /**
     * Destroys the balloons visual appearance and functionality
     * This should be initiated with the removal of the player from the balloons array
     */
    public void destroy() {
        // If the runnable is running, cancel it and nullify it
        if (this.getRunnable() != null) {
            this.getRunnable().cancel();

            this.setRunnable(null);
        }

        // Remove the chicken first to reduce lead dropping on armor stand clears
        this.getBalloonChicken().remove();

        // Loop through every node and destroy it (remove the armor stand mainly)
        for (ModelNode modelNode : this.getModelNodes()) {
            modelNode.destroy();
        }

        // Clear the model nodes list to prevent memory leaks
        this.getModelNodes().clear();
    }

    /**
     * Hooks into the builder to retrieve the built variables
     * @param builder The builder to retrieve the variables from
     */
    MultipartBalloon(MultipartBalloonBuilder builder) {
        this.setBalloonType(builder.balloonType);
        this.setBalloonOwner(builder.balloonOwner);
    }
}
