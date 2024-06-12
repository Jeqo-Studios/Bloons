package net.jeqo.bloons.balloon.multipart;

import lombok.Getter;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.nodes.ModelNode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MultipartBalloon {

    @Getter
    private MultipartBalloonType balloonType;
    @Getter
    private Player balloonOwner;
    private ModelNode tentacle;

    private final List<ModelNode> modelNodes = new ArrayList<>();

    private BukkitRunnable runnable;

    /**
     * Initializes the balloons functionality
     */
    public void initialize() {
        Location balloonOwnerLocation = getBalloonOwner().getLocation();

        // Things that only need to be set up once and not looped over
        ModelNode current = new ModelNode((float) balloonOwnerLocation.getX(), (float) balloonOwnerLocation.getY() + 2, (float) balloonOwnerLocation.getZ(), (float) ((float) balloonType.getDistanceBetweenNodes() + balloonType.getTailNodeOffset()), 0, getBalloonType(), balloonOwner, balloonType.getMaxNodeJointAngle(), balloonType.getYAxisInterpolation(), balloonType.getTurningSplineInterpolation());

        modelNodes.add(current);

        // Create a new node for each node in the balloon type
        for (int i = 1; i < balloonType.getNodeCount(); i++) {
            ModelNode next;
            if (i == balloonType.getNodeCount() - 1) {
                next = new ModelNode(current, (float) ((float) balloonType.getDistanceBetweenNodes() + balloonType.getHeadNodeOffset()), i, getBalloonType(), balloonOwner, balloonType.getMaxNodeJointAngle(), balloonType.getYAxisInterpolation(), balloonType.getTurningSplineInterpolation());
            } else {
                next = new ModelNode(current, (float) ((float) balloonType.getDistanceBetweenNodes() + balloonType.getBodyNodeOffset()), i, getBalloonType(), balloonOwner, balloonType.getMaxNodeJointAngle(), balloonType.getYAxisInterpolation(), balloonType.getTurningSplineInterpolation());
            }
            modelNodes.add(next);
            current.child = next;
            current = next;
        }
        tentacle = current;
    }

    /**
     * Runs the balloons' functionality that needs to loop infinitely
     */
    public void run() {
        // Ensure the previous runnable is canceled before creating a new one
        if (runnable != null) {
            runnable.cancel();
        }

        long timeInTicks = 1;
        double speed = 0.05; // Adjust the speed of the sine wave
        double amplitude = 0.5; // Adjust the amplitude of the sine wave
        double noseAmplitude = 0.5; // Adjust how much the nose of the first node goes up and down

        runnable = new BukkitRunnable() {
            double yOffset = 0; // Initial offset for the sine curve

            @Override
            public void run() {
                Location balloonOwnerLocation = balloonOwner.getLocation();

                // Calculate the Y offset using a sine function with adjusted amplitude
                double sinValue = Math.sin(yOffset);
                double newY = balloonOwnerLocation.getY() + 2 + amplitude * sinValue; // Adjust the amplitude and offset as needed

                // Adjust the nose amplitude based on the sine value
                double noseOffset = noseAmplitude * sinValue;

                // Constantly teleport the balloons
                tentacle.follow((float) balloonOwnerLocation.getX(), (float) (newY + noseOffset), (float) balloonOwnerLocation.getZ());
                tentacle.show();

                // Make the other segments follow
                ModelNode next = tentacle.parent;
                while (next != null) {
                    next.follow();
                    next.show();

                    next = next.parent;
                }

                // Increment the yOffset based on the speed for the next iteration
                yOffset += speed; // Adjust the speed of the sine wave as needed
            }
        };

        runnable.runTaskTimer(Bloons.getInstance(), timeInTicks, timeInTicks);
    }




    public void destroy() {
        if (runnable != null) {
            runnable.cancel();

            runnable = null;
        }

        for (ModelNode modelNode : modelNodes) {
            modelNode.destroy();
        }

        modelNodes.clear();
    }

    MultipartBalloon(MultipartBalloonBuilder builder) {
        this.balloonType = builder.balloonType;
        this.balloonOwner = builder.balloonOwner;
    }
}
