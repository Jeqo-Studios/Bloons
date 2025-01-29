package net.jeqo.bloons.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.colors.ColorCodeConverter;

/**
 * An object to store the data of a balloon created in the config.yml file
 */
@Getter @Setter
public class MultipartBalloonType {
    /**
     * The unique identifier for the balloon type
     */
    private String id;
    /**
     * The permission required to use the balloon
     */
    private String permission;
    /**
     * The name of the balloon that is displayed both in chat and in the Bloons menu
     */
    private String name;
    /**
     * The lore of the item that is displayed in the GUI
     */
    private String[] lore;
    /**
     * The number of nodes, or models, in the balloon
     */
    private int nodeCount = 5; // optional
    /**
     * The distance between each node in the balloon measured in blocks
     */
    private double distanceBetweenNodes = 2.0D; // optional
    /**
     * The height of the leash attached to a player relative from the head of the player in blocks
     */
    private double leashHeight = 1.2D; // optional
    /**
     * The offset of the head node from its 0 position measured in blocks
     */
    private double headNodeOffset = 0.0D; // optional
    /**
     * The offset of the body node from its 0 position measured in blocks
     */
    private double bodyNodeOffset = 0.0D; // optional
    /**
     * The offset of the tail node from its 0 position measured in blocks
     */
    private double tailNodeOffset = 0.0D; // optional
    /**
     * The max angle that a segment/node can rotate to in degrees in both directions
     * This number *2 is equal to the total range of motion for each segment
     */
    private double maxNodeJointAngle = 35.0D; // optional
    /**
     * The interpolation of the Y-axis motion of every segment
     */
    private double yAxisInterpolation = 0.35D; // optional
    /**
     * The interpolation of the turning spline to prevent overturning and
     * to enhance smoother turning
     */
    private double turningSplineInterpolation = 0.35D; // optional
    /**
     * The speed of the passive sine wave animation. This is the amount of blocks it
     * will move every tick due to the runnable running every tick.
     */
    private double passiveSineWaveSpeed = 0.05D; // optional
    /**
     * The amplitude of the passive sine wave animation. This is the maximum amount of
     * blocks the balloon will move in the positive and negative direction.
     */
    private double passiveSineWaveAmplitude = 0.5D; // optional
    /**
     * The amplitude of the passive sine wave animation starting at the nose
     */
    private double passiveNoseSineWaveAmplitude = 0.5D; // optional
    /**
     * The model used for the head node
     */
    private MultipartBalloonModel headModel;
    /**
     * The model used for the body node
     */
    private MultipartBalloonModel bodyModel;
    /**
     * The model used for the tail node
     */
    private MultipartBalloonModel tailModel;

    /**
     *                                      Creates a new multipart balloon type which contains
     *                                      the data in the configuration for the balloon
     * @param id                            The ID of the balloon, type java.lang.String
     * @param permission                    The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                          The name of the balloon, type java.lang.String
     * @param lore                          The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                     The number of nodes, or models, in the balloon, type int
     * @param distanceBetweenNodes          The distance between each node in the balloon measured as blocks, type double
     * @param leashHeight                   The height of the leash from the player to the lead node, type double
     * @param headNodeOffset                The offset of the head node from its 0 position measured as blocks, type double
     * @param bodyNodeOffset                The offset of the body node from its 0 position measured as blocks, type double
     * @param tailNodeOffset                The offset of the tail node from its 0 position measured as blocks, type double
     * @param maxNodeJointAngle             The maximum angle a node can rotate in degrees, type double
     * @param yAxisInterpolation            The interpolation of the Y-axis, type double
     * @param turningSplineInterpolation    The interpolation of the turning spline to prevent overturning, type double
     * @param passiveSineWaveSpeed          The speed of the passive sine wave animation, type double
     * @param passiveSineWaveAmplitude      The amplitude of the passive sine wave animation, type double
     * @param passiveNoseSineWaveAmplitude  The amplitude of the passive sine wave animation starting at the nose, type double
     * @param headModel                     The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel                     The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel                     The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double leashHeight, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, double yAxisInterpolation, double turningSplineInterpolation, double passiveSineWaveSpeed, double passiveSineWaveAmplitude, double passiveNoseSineWaveAmplitude, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id); // required
        this.setPermission(permission); // required
        this.setName(name); // required
        this.setLore(lore); // required
        if (nodeCount > 0) this.setNodeCount(nodeCount); // 5 by default, optional
        if (distanceBetweenNodes > 0.0D) this.setDistanceBetweenNodes(distanceBetweenNodes); // 2.0 by default, optional
        if (leashHeight > 0.0D) this.setLeashHeight(leashHeight); // 1.2 by default, optional
        this.setHeadNodeOffset(headNodeOffset); // 0 by default, optional
        this.setBodyNodeOffset(bodyNodeOffset); // 0 by default, optional
        this.setTailNodeOffset(tailNodeOffset); // 0 by default, optional
        if (maxNodeJointAngle > 0.0D) this.setMaxNodeJointAngle(maxNodeJointAngle); // 35.0 by default, optional
        if (yAxisInterpolation > 0.0D) this.setYAxisInterpolation(yAxisInterpolation); // 0.35 by default, optional
        if (turningSplineInterpolation > 0.0D) this.setTurningSplineInterpolation(turningSplineInterpolation); // 0.35 by default, optional
        if (passiveSineWaveSpeed > 0.0D) this.setPassiveSineWaveSpeed(passiveSineWaveSpeed); // 0.05 by default, optional
        if (passiveSineWaveAmplitude > 0.0D) this.setPassiveSineWaveAmplitude(passiveSineWaveAmplitude); // 0.5 by default, optional
        if (passiveNoseSineWaveAmplitude > 0.0D) this.setPassiveNoseSineWaveAmplitude(passiveNoseSineWaveAmplitude); // 0.5 by default, optional
        this.setHeadModel(headModel); // required
        this.setBodyModel(bodyModel); // required
        this.setTailModel(tailModel); // required
    }

    public String getName() {
        return ColorCodeConverter.adventureToColorCode(this.name);
    }
}
