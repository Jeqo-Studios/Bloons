package net.jeqo.bloons.balloon.multipart.balloon;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.multipart.nodes.ModelNode;
import net.jeqo.bloons.balloon.multipart.nodes.ModelNodeVector;
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

    private final List<ModelNode> modelNodes = new ArrayList<>();

    @Setter
    private Chicken balloonChicken;

    @Setter
    private ModelNode tentacle;
    @Setter
    private BukkitRunnable runnable;

    /**
     * Initializes the balloons functionality
     */
    public void initialize() {
        // Things that only need to be set up once and not looped over
        ModelNode current = new ModelNode((float) this.getBalloonOwner().getLocation().getX(), (float) this.getBalloonOwner().getLocation().getY() + 2, (float) this.getBalloonOwner().getLocation().getZ(),
                                         (float) ((float) this.getBalloonType().getDistanceBetweenNodes() + this.getBalloonType().getTailNodeOffset()), 0, getBalloonType(), this.getBalloonOwner(),
                                         this.getBalloonType().getMaxNodeJointAngle(), this.getBalloonType().getYAxisInterpolation(), this.getBalloonType().getTurningSplineInterpolation());

        this.getModelNodes().add(current);

        // Create a new node for each node in the balloon type
        for (int i = 1; i < this.getBalloonType().getNodeCount(); i++) {
            ModelNode next = getModelNode(i, current);
            this.getModelNodes().add(next);
            current.child = next;
            current = next;
        }
        this.setTentacle(current);

        this.setBalloonChicken(this.getBalloonOwner().getWorld().spawn(new Location(this.getBalloonOwner().getWorld(), this.getBalloonOwner().getLocation().getX(), this.getBalloonOwner().getLocation().getY() + 2, this.getBalloonOwner().getLocation().getZ()), Chicken.class));
        this.getBalloonChicken().setInvulnerable(true);
        this.getBalloonChicken().setInvisible(true);
        this.getBalloonChicken().setBaby();
        this.getBalloonChicken().setSilent(true);
        this.getBalloonChicken().setAgeLock(true);
        this.getBalloonChicken().setAware(false);
        this.getBalloonChicken().setCollidable(false);
        this.getBalloonChicken().customName(Component.text(BalloonConfiguration.BALLOON_CHICKEN_ID));
    }

    private @NotNull ModelNode getModelNode(int i, ModelNode current) {
        ModelNode next;
        if (i == this.getBalloonType().getNodeCount() - 1) {
            next = new ModelNode(current, (float) ((float) this.getBalloonType().getDistanceBetweenNodes() + this.getBalloonType().getHeadNodeOffset()),
                    i, getBalloonType(), this.getBalloonOwner(), this.getBalloonType().getMaxNodeJointAngle(), this.getBalloonType().getYAxisInterpolation(),
                    this.getBalloonType().getTurningSplineInterpolation());

        } else {
            next = new ModelNode(current, (float) ((float) this.getBalloonType().getDistanceBetweenNodes() + this.getBalloonType().getBodyNodeOffset()),
                    i, getBalloonType(), this.getBalloonOwner(), this.getBalloonType().getMaxNodeJointAngle(), this.getBalloonType().getYAxisInterpolation(),
                    this.getBalloonType().getTurningSplineInterpolation());
        }
        return next;
    }

    /**
     * Runs the balloons' functionality that needs to loop infinitely
     */
    public void run() {
        // Ensure the previous runnable is canceled before creating a new one
        if (this.getRunnable() != null) {
            this.getRunnable().cancel();
        }

        long timeInTicks = 1;
        double speed = 0.05; // Adjust the speed of the sine wave
        double amplitude = 0.5; // Adjust the amplitude of the sine wave
        double noseAmplitude = 0.5; // Adjust how much the nose of the first node goes up and down

        this.setRunnable(new BukkitRunnable() {
            double yOffset = 0; // Initial offset for the sine curve

            @Override
            public void run() {
                Location ownerLocation = getBalloonOwner().getLocation();
                // Calculate the Y offset using a sine function with adjusted amplitude
                double sinValue = Math.sin(yOffset);
                double newY = ownerLocation.getY() + 2 + amplitude * sinValue; // Adjust the amplitude and offset as needed

                // Adjust the nose amplitude based on the sine value
                double noseOffset = noseAmplitude * sinValue;

                // Calculate the midpoint between Point A and Point B
                double midpointX = (getTentacle().getPointA().x + getTentacle().getPointB().x) / 2.0;
                double midpointZ = (getTentacle().getPointA().z + getTentacle().getPointA().z) / 2.0;

                // Teleport the chicken holding the leash constantly
                getBalloonChicken().teleport(new Location(getBalloonOwner().getWorld(), midpointX, getTentacle().getPointA().y + 1.5, midpointZ));
                getBalloonChicken().setLeashHolder(getBalloonOwner());

                // Constantly teleport the balloons
                getTentacle().follow((float) ownerLocation.getX(), (float) (newY + noseOffset), (float) ownerLocation.getZ());
                getTentacle().show();

                // Make the other segments follow
                ModelNode next = getTentacle().parent;
                while (next != null) {
                    next.follow();
                    next.show();

                    next = next.parent;
                }

                // Increment the yOffset based on the speed for the next iteration
                yOffset += speed; // Adjust the speed of the sine wave as needed
            }
        });

        this.getRunnable().runTaskTimer(Bloons.getInstance(), timeInTicks, timeInTicks);
    }

    /**
     * Destroys the balloons visual appearance and functionality
     * This should be initiated with the removal of the player from the balloons array
     */
    public void destroy() {
        if (this.getRunnable() != null) {
            this.getRunnable().cancel();

            this.setRunnable(null);
        }

        for (ModelNode modelNode : this.getModelNodes()) {
            modelNode.destroy();
        }

        this.getBalloonChicken().remove();

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
