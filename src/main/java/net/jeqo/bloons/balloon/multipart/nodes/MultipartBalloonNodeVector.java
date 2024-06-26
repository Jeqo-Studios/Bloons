package net.jeqo.bloons.balloon.multipart.nodes;

import java.io.Serializable;

/**
 * A custom vector to store the axis of a balloon node
 */
public class MultipartBalloonNodeVector implements Serializable {
    /**
     * Model node vector axis.
     */
    public float x;
    public float y;
    public float z;

    /**
     * Creates a blank model node vector.
     */
    public MultipartBalloonNodeVector() {
    }

    /**
     * Creates a segment vector holding a 3D space.
     * @param x X axis.
     * @param y Y axis.
     * @param z Z axis.
     */
    public MultipartBalloonNodeVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Sets a 3D's vectors axis.
     * @param x X axis float.
     * @param y Y axis float.
     * @param z Z axis float.
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Sets a 2D's vectors axis.
     * @param x X axis float.
     * @param z Z axis float.
     */
    public void set(float x, float z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Copies a segment vector.
     * @return The copy of the vector.
     */
    public MultipartBalloonNodeVector copy() {
        return new MultipartBalloonNodeVector(x, y, z);
    }

    /**
     * Creates a new segment vector derived from 2 segment vectors added together.
     * @param v1 Segment vector 1.
     * @param v2 Segment vector 2.
     * @return The combined segment vector.
     */
    static public MultipartBalloonNodeVector add(MultipartBalloonNodeVector v1, MultipartBalloonNodeVector v2) {
        return add(v1, v2, null);
    }

    /**
     * Adds two segment vectors together and sets it to target if provided.
     * @param v1 Segment vector 1.
     * @param v2 Segment vector 2.
     * @param target Target vector.
     * @return The combined vectors.
     */
    static public MultipartBalloonNodeVector add(MultipartBalloonNodeVector v1, MultipartBalloonNodeVector v2, MultipartBalloonNodeVector target) {
        if (target == null) {
            target = new MultipartBalloonNodeVector(v1.x + v2.x,v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }
        return target;
    }

    /**
     * Subtracts two segment vectors from each other.
     * @param v1 Segment vector 1.
     * @param v2 Segment vector 2.
     * @return The result of the two subtracted vectors.
     */
    static public MultipartBalloonNodeVector subtract(MultipartBalloonNodeVector v1, MultipartBalloonNodeVector v2) {
        return subtract(v1, v2, null);
    }

    /**
     * Subtracts two vectors from each other and sets it to a target is provided.
     * @param v1 Segment vector 1.
     * @param v2 Segment vector 2.
     * @param target Target vector.
     * @return The result of the subtraction.
     */
    static public MultipartBalloonNodeVector subtract(MultipartBalloonNodeVector v1, MultipartBalloonNodeVector v2, MultipartBalloonNodeVector target) {
        if (target == null) {
            target = new MultipartBalloonNodeVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }
        return target;
    }

    /**
     * Gets the heading of a segment.
     * @return The heading of the segment.
     */
    public float heading() {
        return (float) Math.atan2(z, x);
    }
}
