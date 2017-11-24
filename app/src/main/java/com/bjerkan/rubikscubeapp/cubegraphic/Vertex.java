package com.bjerkan.rubikscubeapp.cubegraphic;

/**
 * Class representing a vertex in 3D space for rendering.
 */
public class Vertex {

    /**
     * Creates a Vertex object with given coordinates.
     *
     * @param x the x coordinate of the vertex
     * @param y the y coordinate of the vertex
     * @param z the z coordinate of the vertex
     */
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x coordinate of the vertex.
     *
     * @return the x coordinate of the vertex
     */
    public float x() {
        return x;
    }

    /**
     * Returns the y coordinate of the vertex.
     *
     * @return the y coordinate of the vertex
     */
    public float y() {
        return y;
    }

    /**
     * Returns the z coordinate of the vertex.
     *
     * @return the z coordinate of the vertex
     */
    public float z() {
        return z;
    }

    private final float x;
    private final float y;
    private final float z;
}
