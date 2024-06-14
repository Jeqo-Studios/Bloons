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
    private MultipartBalloonModel headModel;
    private MultipartBalloonModel bodyModel;
    private MultipartBalloonModel tailModel;

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
