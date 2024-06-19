package net.jeqo.bloons.balloon.multipart;

import lombok.Getter;
import lombok.Setter;

/**
 * An object to store the data of a balloon created in the config.yml file
 */
@Getter @Setter
public class MultipartBalloonType {
    private String id;
    private String permission;
    private String name;
    private String[] lore;
    private int nodeCount;
    private double distanceBetweenNodes;
    private double headNodeOffset = 0.0;
    private double bodyNodeOffset = 0.0;
    private double tailNodeOffset = 0.0;
    private double maxNodeJointAngle = 35.0;
    private double yAxisInterpolation = 0.35;
    private double turningSplineInterpolation = 0.35;
    private double passiveSineWaveSpeed = 0.05;
    private double passiveSineWaveAmplitude = 0.5;
    private double passiveNoseSineWaveAmplitude = 0.5;
    private MultipartBalloonModel headModel;
    private MultipartBalloonModel bodyModel;
    private MultipartBalloonModel tailModel;

    /**
     *                                      Creates a new multipart balloon type which contains
     *                                      the data in the configuration for the balloon
     * @param id                            The id of the balloon, type java.lang.String
     * @param permission                    The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                          The name of the balloon, type java.lang.String
     * @param lore                          The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                     The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes          The distance between each node in the balloon measured as blocks, type double
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
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, double yAxisInterpolation, double turningSplineInterpolation, double passiveSineWaveSpeed, double passiveSineWaveAmplitude, double passiveNoseSineWaveAmplitude, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadNodeOffset(headNodeOffset);
        this.setBodyNodeOffset(bodyNodeOffset);
        this.setTailNodeOffset(tailNodeOffset);
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setYAxisInterpolation(yAxisInterpolation);
        this.setTurningSplineInterpolation(turningSplineInterpolation);
        this.setPassiveSineWaveSpeed(passiveSineWaveSpeed);
        this.setPassiveSineWaveAmplitude(passiveSineWaveAmplitude);
        this.setPassiveNoseSineWaveAmplitude(passiveNoseSineWaveAmplitude);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }

    /**
     *                                     Creates a new multipart balloon type which contains
     *                                     the data in the configuration for the balloon
     * @param id                           The id of the balloon, type java.lang.String
     * @param permission                   The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                         The name of the balloon, type java.lang.String
     * @param lore                         The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                    The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes         The distance between each node in the balloon measured as blocks, type double
     * @param headNodeOffset               The offset of the head node from its 0 position measured as blocks, type double
     * @param bodyNodeOffset               The offset of the body node from its 0 position measured as blocks, type double
     * @param tailNodeOffset               The offset of the tail node from its 0 position measured as blocks, type double
     * @param maxNodeJointAngle            The maximum angle a node can rotate in degrees, type double
     * @param yAxisInterpolation           The interpolation of the Y-axis, type double
     * @param turningSplineInterpolation   The interpolation of the turning spline to prevent overturning, type double
     * @param passiveSineWaveSpeed         The speed of the passive sine wave animation, type double
     * @param passiveSineWaveAmplitude     The amplitude of the passive sine wave animation, type double
     * @param headModel                    The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel                    The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel                    The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, double yAxisInterpolation, double turningSplineInterpolation, double passiveSineWaveSpeed, double passiveSineWaveAmplitude, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadNodeOffset(headNodeOffset);
        this.setBodyNodeOffset(bodyNodeOffset);
        this.setTailNodeOffset(tailNodeOffset);
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setYAxisInterpolation(yAxisInterpolation);
        this.setTurningSplineInterpolation(turningSplineInterpolation);
        this.setPassiveSineWaveSpeed(passiveSineWaveSpeed);
        this.setPassiveSineWaveAmplitude(passiveSineWaveAmplitude);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }

    /**
     *                                    Creates a new multipart balloon type which contains
     *                                    the data in the configuration for the balloon
     * @param id                          The id of the balloon, type java.lang.String
     * @param permission                  The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                        The name of the balloon, type java.lang.String
     * @param lore                        The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                   The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes        The distance between each node in the balloon measured as blocks, type double
     * @param headNodeOffset              The offset of the head node from its 0 position measured as blocks, type double
     * @param bodyNodeOffset              The offset of the body node from its 0 position measured as blocks, type double
     * @param tailNodeOffset              The offset of the tail node from its 0 position measured as blocks, type double
     * @param maxNodeJointAngle           The maximum angle a node can rotate in degrees, type double
     * @param yAxisInterpolation          The interpolation of the Y-axis, type double
     * @param turningSplineInterpolation  The interpolation of the turning spline to prevent overturning, type double
     * @param passiveSineWaveSpeed        The speed of the passive sine wave animation, type double
     * @param headModel                   The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel                   The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel                   The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, double yAxisInterpolation, double turningSplineInterpolation, double passiveSineWaveSpeed, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadNodeOffset(headNodeOffset);
        this.setBodyNodeOffset(bodyNodeOffset);
        this.setTailNodeOffset(tailNodeOffset);
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setYAxisInterpolation(yAxisInterpolation);
        this.setTurningSplineInterpolation(turningSplineInterpolation);
        this.setPassiveSineWaveSpeed(passiveSineWaveSpeed);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }

    /**
     *                                   Creates a new multipart balloon type which contains
     *                                   the data in the configuration for the balloon
     * @param id                         The id of the balloon, type java.lang.String
     * @param permission                 The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                       The name of the balloon, type java.lang.String
     * @param lore                       The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                  The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes       The distance between each node in the balloon measured as blocks, type double
     * @param headNodeOffset             The offset of the head node from its 0 position measured as blocks, type double
     * @param bodyNodeOffset             The offset of the body node from its 0 position measured as blocks, type double
     * @param tailNodeOffset             The offset of the tail node from its 0 position measured as blocks, type double
     * @param maxNodeJointAngle          The maximum angle a node can rotate in degrees, type double
     * @param yAxisInterpolation         The interpolation of the Y-axis, type double
     * @param turningSplineInterpolation The interpolation of the turning spline to prevent overturning, type double
     * @param headModel                  The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel                  The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel                  The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, double yAxisInterpolation, double turningSplineInterpolation, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadNodeOffset(headNodeOffset);
        this.setBodyNodeOffset(bodyNodeOffset);
        this.setTailNodeOffset(tailNodeOffset);
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setYAxisInterpolation(yAxisInterpolation);
        this.setTurningSplineInterpolation(turningSplineInterpolation);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }

    /**
     *                                    Creates a new multipart balloon type which contains
     *                                    the data in the configuration for the balloon
     * @param id                          The id of the balloon, type java.lang.String
     * @param permission                  The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                        The name of the balloon, type java.lang.String
     * @param lore                        The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                   The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes        The distance between each node in the balloon measured as blocks, type double
     * @param headModel                   The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel                   The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel                   The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }

    /**
     *                               Creates a new multipart balloon type which contains
     *                               the data in the configuration for the balloon
     * @param id                     The id of the balloon, type java.lang.String
     * @param permission             The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                   The name of the balloon, type java.lang.String
     * @param lore                   The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount              The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes   The distance between each node in the balloon measured as blocks, type double
     * @param headNodeOffset         The offset of the head node from its 0 position measured as blocks, type double
     * @param bodyNodeOffset         The offset of the body node from its 0 position measured as blocks, type double
     * @param tailNodeOffset         The offset of the tail node from its 0 position measured as blocks, type double
     * @param headModel              The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel              The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel              The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadNodeOffset(headNodeOffset);
        this.setBodyNodeOffset(bodyNodeOffset);
        this.setTailNodeOffset(tailNodeOffset);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }

    /**
     *                                 Creates a new multipart balloon type which contains
     *                                 the data in the configuration for the balloon
     * @param id                       The id of the balloon, type java.lang.String
     * @param permission               The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                     The name of the balloon, type java.lang.String
     * @param lore                     The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes     The distance between each node in the balloon measured as blocks, type double
     * @param headNodeOffset           The offset of the head node from its 0 position measured as blocks, type double
     * @param bodyNodeOffset           The offset of the body node from its 0 position measured as blocks, type double
     * @param tailNodeOffset           The offset of the tail node from its 0 position measured as blocks, type double
     * @param maxNodeJointAngle        The maximum angle a node can rotate in degrees, type double
     * @param headModel                The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel                The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel                The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadNodeOffset(headNodeOffset);
        this.setBodyNodeOffset(bodyNodeOffset);
        this.setTailNodeOffset(tailNodeOffset);
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }

    /**
     *                                   Creates a new multipart balloon type which contains
     *                                   the data in the configuration for the balloon
     * @param id                         The id of the balloon, type java.lang.String
     * @param permission                 The permission required to use the balloon (i.e. blue.jeqo), type java.lang.String
     * @param name                       The name of the balloon, type java.lang.String
     * @param lore                       The lore lines of the balloon in the balloon GUI, type java.lang.String[]
     * @param nodeCount                  The number of nodes, or model, in the balloon, type int
     * @param distanceBetweenNodes       The distance between each node in the balloon measured as blocks, type double
     * @param headNodeOffset             The offset of the head node from its 0 position measured as blocks, type double
     * @param bodyNodeOffset             The offset of the body node from its 0 position measured as blocks, type double
     * @param tailNodeOffset             The offset of the tail node from its 0 position measured as blocks, type double
     * @param maxNodeJointAngle          The maximum angle a node can rotate in degrees, type double
     * @param yAxisInterpolation         The interpolation of the Y-axis, type double
     * @param headModel                  The model used for the head node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param bodyModel                  The model used for the body node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     * @param tailModel                  The model used for the tail node, type net.jeqo.bloons.balloon.multipart.MultipartBalloonModel
     */
    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, double yAxisInterpolation, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.setId(id);
        this.setPermission(permission);
        this.setName(name);
        this.setLore(lore);
        this.setNodeCount(nodeCount);
        this.setDistanceBetweenNodes(distanceBetweenNodes);
        this.setHeadNodeOffset(headNodeOffset);
        this.setBodyNodeOffset(bodyNodeOffset);
        this.setTailNodeOffset(tailNodeOffset);
        this.setMaxNodeJointAngle(maxNodeJointAngle);
        this.setYAxisInterpolation(yAxisInterpolation);
        this.setHeadModel(headModel);
        this.setBodyModel(bodyModel);
        this.setTailModel(tailModel);
    }
}
