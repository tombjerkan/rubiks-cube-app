package com.bjerkan.rubikscubeapp.rubikscube;

import org.opencv.core.Scalar;

/**
 * An enum for the different colours present on a Rubik's cube.
 */
public enum Colour {
    WHITE,
    GREEN,
    RED,
    BLUE,
    ORANGE,
    YELLOW;

    /**
     * The hue value to use when comparing similarity of colours when scanning.
     */
    public double hue;

    /**
     * The RGB value to use when rendering the cube squares.
     */
    public Scalar rgb;

    static {
        WHITE.hue = 0.;
        GREEN.hue = 74.;
        RED.hue = 0.;
        BLUE.hue = 108.;
        ORANGE.hue = 11.;
        YELLOW.hue = 25.;

        WHITE.rgb = new Scalar(255., 255., 255.);
        GREEN.rgb = new Scalar(0., 155., 72.);
        RED.rgb = new Scalar(183., 18., 52.);
        BLUE.rgb = new Scalar(0., 70., 173.);
        ORANGE.rgb = new Scalar(255., 88., 0.);
        YELLOW.rgb = new Scalar(255., 213., 0.);
    }
}