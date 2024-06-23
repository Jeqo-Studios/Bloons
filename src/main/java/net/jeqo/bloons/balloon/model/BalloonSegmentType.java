package net.jeqo.bloons.balloon.model;

/**
 * The type of segment that the model accommodates
 */
public enum BalloonSegmentType {
    /**
     * This is the head of the balloon, indexed as the last index in the multipart balloon
     */
    HEAD,
    /**
     * Accommodates the middle segments of the balloon
     */
    BODY,
    /**
     * The tail of the balloon, indexed as the first index in the multipart balloon (0)
     */
    TAIL
}
