package net.jeqo.bloons.balloon.multipart;

import lombok.Getter;
import lombok.Setter;

/**
 * An object to store the data of a balloon created in the config.yml file
 */
@Getter @Setter
public class MultipartBalloonType {

    private final String id;
    private final String permission;
    private final String name;
    private final String[] lore;
    private final int nodeCount;
    private final double distanceBetweenNodes;
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
        this.id = id;
        this.permission = permission;
        this.name = name;
        this.lore = lore;
        this.nodeCount = nodeCount;
        this.distanceBetweenNodes = distanceBetweenNodes;
        this.headNodeOffset = headNodeOffset;
        this.bodyNodeOffset = bodyNodeOffset;
        this.tailNodeOffset = tailNodeOffset;
        this.maxNodeJointAngle = maxNodeJointAngle;
        this.yAxisInterpolation = yAxisInterpolation;
        this.turningSplineInterpolation = turningSplineInterpolation;
        this.headModel = headModel;
        this.bodyModel = bodyModel;
        this.tailModel = tailModel;
    }

    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.id = id;
        this.permission = permission;
        this.name = name;
        this.lore = lore;
        this.nodeCount = nodeCount;
        this.distanceBetweenNodes = distanceBetweenNodes;
        this.headModel = headModel;
        this.bodyModel = bodyModel;
        this.tailModel = tailModel;
    }

    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.id = id;
        this.permission = permission;
        this.name = name;
        this.lore = lore;
        this.nodeCount = nodeCount;
        this.distanceBetweenNodes = distanceBetweenNodes;
        this.headNodeOffset = headNodeOffset;
        this.bodyNodeOffset = bodyNodeOffset;
        this.tailNodeOffset = tailNodeOffset;
        this.headModel = headModel;
        this.bodyModel = bodyModel;
        this.tailModel = tailModel;
    }

    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.id = id;
        this.permission = permission;
        this.name = name;
        this.lore = lore;
        this.nodeCount = nodeCount;
        this.distanceBetweenNodes = distanceBetweenNodes;
        this.headNodeOffset = headNodeOffset;
        this.bodyNodeOffset = bodyNodeOffset;
        this.tailNodeOffset = tailNodeOffset;
        this.maxNodeJointAngle = maxNodeJointAngle;
        this.headModel = headModel;
        this.bodyModel = bodyModel;
        this.tailModel = tailModel;
    }

    public MultipartBalloonType(String id, String permission, String name, String[] lore, int nodeCount, double distanceBetweenNodes, double headNodeOffset, double bodyNodeOffset, double tailNodeOffset, double maxNodeJointAngle, double yAxisInterpolation, MultipartBalloonModel headModel, MultipartBalloonModel bodyModel, MultipartBalloonModel tailModel) {
        this.id = id;
        this.permission = permission;
        this.name = name;
        this.lore = lore;
        this.nodeCount = nodeCount;
        this.distanceBetweenNodes = distanceBetweenNodes;
        this.headNodeOffset = headNodeOffset;
        this.bodyNodeOffset = bodyNodeOffset;
        this.tailNodeOffset = tailNodeOffset;
        this.maxNodeJointAngle = maxNodeJointAngle;
        this.yAxisInterpolation = yAxisInterpolation;
        this.headModel = headModel;
        this.bodyModel = bodyModel;
        this.tailModel = tailModel;
    }
}
