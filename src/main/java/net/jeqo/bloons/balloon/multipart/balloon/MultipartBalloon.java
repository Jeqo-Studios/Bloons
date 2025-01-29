package net.jeqo.bloons.balloon.multipart.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.nodes.MultipartBalloonNode;
import net.jeqo.bloons.configuration.BalloonConfiguration;
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
    /**
     * The type of balloon that the multipart balloon is creating
     */
    @Setter
    private MultipartBalloonType type;
    /**
     * The owner of the balloon
     */
    @Setter
    private Player owner;
    /**
     * The chicken that is used to attach the player via a lead to the balloon
     */
    @Setter
    private Chicken chicken;
    /**
     * The tentacle node that is the front of the balloon
     */
    @Setter
    private MultipartBalloonNode tentacle;
    /**
     * The runnable that is used to constantly update the balloons' position
     */
    @Setter
    private BukkitRunnable runnable;
    /**
     * The list of model nodes that are used to create the balloon
     */
    private final List<MultipartBalloonNode> multipartBalloonNodes = new ArrayList<>();

    /**
     * Initializes the balloons functionality
     */
    public void initialize() {
        // Things that only need to be set up once and not looped over
        MultipartBalloonNode current = new MultipartBalloonNode((float) this.getOwner().getLocation().getX(), (float) this.getOwner().getLocation().getY(), (float) this.getOwner().getLocation().getZ(),
                (float) ((float) this.getType().getDistanceBetweenNodes() + this.getType().getTailNodeOffset()), 0, getType(), this.getOwner(),
                this.getType().getMaxNodeJointAngle(), this.getType().getYAxisInterpolation(), this.getType().getTurningSplineInterpolation());

        // Add the current node to the list of model nodes
        this.getMultipartBalloonNodes().add(current);

        // Create a new node for each node in the balloon type
        for (int i = 1; i < this.getType().getNodeCount(); i++) {
            MultipartBalloonNode next = getModelNode(i, current);
            this.getMultipartBalloonNodes().add(next);
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
        Location location = new Location(this.getOwner().getWorld(), this.getOwner().getLocation().getX(), this.getOwner().getLocation().getY() + 2, this.getOwner().getLocation().getZ());

        // Configure the chicken entity properties
        this.setChicken(this.getOwner().getWorld().spawn(location, Chicken.class));
        this.getChicken().setInvulnerable(true);
        this.getChicken().setInvisible(true);
        this.getChicken().setBaby();
        this.getChicken().setSilent(true);
        this.getChicken().setAgeLock(true);
        this.getChicken().setAware(false);
        this.getChicken().setCollidable(false);
        this.getChicken().setCustomName(BalloonConfiguration.BALLOON_CHICKEN_ID);
    }

    /**
     *                  Gets the next model node in the balloon based on the current node and the index
     * @param index     The index of the node, type int
     * @param current   The current node, type net.jeqo.bloons.balloon.multipart.nodes.ModelNode
     * @return          The next model node, type net.jeqo.bloons.balloon.multipart.nodes.ModelNode
     */
    private @NotNull MultipartBalloonNode getModelNode(int index, MultipartBalloonNode current) {
        MultipartBalloonNode next;

        // If the index is the last node, create a head node
        if (index == this.getType().getNodeCount() - 1) {
            next = new MultipartBalloonNode(current, (float) ((float) this.getType().getDistanceBetweenNodes() + this.getType().getHeadNodeOffset()),
                    index, getType(), this.getOwner(), this.getType().getMaxNodeJointAngle(), this.getType().getYAxisInterpolation(),
                    this.getType().getTurningSplineInterpolation());

        // Otherwise, create a body node
        } else {
            next = new MultipartBalloonNode(current, (float) ((float) this.getType().getDistanceBetweenNodes() + this.getType().getBodyNodeOffset()),
                    index, getType(), this.getOwner(), this.getType().getMaxNodeJointAngle(), this.getType().getYAxisInterpolation(),
                    this.getType().getTurningSplineInterpolation());
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

        double speed = this.getType().getPassiveSineWaveSpeed(); // Adjust the speed of the sine wave
        double amplitude = this.getType().getPassiveSineWaveAmplitude(); // Adjust the amplitude of the sine wave
        double noseAmplitude = this.getType().getPassiveNoseSineWaveAmplitude(); // Adjust how much the nose of the first node goes up and down

        final boolean[] isInitialized = {false};

        this.setRunnable(new BukkitRunnable() {
            double yOffset = 0; // Initial offset for the sine curve

            @Override
            public void run() {
                // Gets the constantly updated owners location
                Location balloonOwnerLocation = getOwner().getLocation();

                // Calculate the Y offset using a sine function with adjusted amplitude
                double sinValue = Math.sin(yOffset);
                double newY = balloonOwnerLocation.getY() + 2 + amplitude * sinValue; // Adjust the amplitude and offset as needed

                // Adjust the nose amplitude based on the sine value
                double noseOffset = noseAmplitude * sinValue;

                // Calculate the midpoint between Point A and Point B in the leading balloon
                double midpointX = (getTentacle().getPointA().x + getTentacle().getPointB().x) / 2.0;
                double midpointZ = (getTentacle().getPointA().z + getTentacle().getPointB().z) / 2.0;

                // Constantly teleport the balloons
                getTentacle().follow((float) balloonOwnerLocation.getX(), (float) (newY + noseOffset), (float) balloonOwnerLocation.getZ());
                getTentacle().display();

                // Make the other segments follow
                MultipartBalloonNode next = getTentacle().getParent();
                while (next != null) {
                    next.follow();
                    next.display();

                    next = next.getParent();
                }

                // Increment the yOffset based on the speed for the next iteration
                yOffset += speed; // Adjust the speed of the sine wave as needed

                // Checks if it's done initializing the fall down/up animation and sets it to true
                if (balloonOwnerLocation.distance(new Location(balloonOwnerLocation.getWorld(), getTentacle().getPointA().x, getTentacle().getPointA().y, getTentacle().getPointA().z)) > 2) {
                    isInitialized[0] = true;
                }

                // If it's done initialized and isn't far away, add the lead and constantly teleport it to
                // not break the lead
                if (isInitialized[0]) {
                    // Teleport the chicken holding the leash constantly
                    Location leadTeleportPoint = new Location(getOwner().getWorld(), midpointX, getTentacle().getPointA().y + type.getLeashHeight(), midpointZ);
                    getChicken().teleport(leadTeleportPoint);
                    getChicken().setLeashHolder(getOwner());
                }
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
        this.getChicken().remove();

        // Loop through every node and destroy it (remove the armor stand mainly)
        for (MultipartBalloonNode multipartBalloonNode : this.getMultipartBalloonNodes()) {
            multipartBalloonNode.destroy();
        }

        // Clear the model nodes list to prevent memory leaks
        this.getMultipartBalloonNodes().clear();
    }

    /**
     * Hooks into the builder to retrieve the built variables
     * @param builder The builder to retrieve the variables from
     */
    MultipartBalloon(MultipartBalloonBuilder builder) {
        this.setType(builder.balloonType);
        this.setOwner(builder.balloonOwner);
    }
}
