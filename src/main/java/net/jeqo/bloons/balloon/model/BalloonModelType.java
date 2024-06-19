package net.jeqo.bloons.balloon.model;

/**
 * The type of segment that the model accommodates
 */
public enum BalloonModelType {
    HEAD, // This is the head of the balloon, indexed as the last index in the multipart balloon
    BODY, // Body, this accommodates the middle segments of the balloon
    TAIL // This is the tail of the balloon, indexed as the first index in the multipart balloon (0)
}
