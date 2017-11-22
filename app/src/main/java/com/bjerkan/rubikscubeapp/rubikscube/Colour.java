package com.bjerkan.rubikscubeapp.rubikscube;

import org.opencv.core.Scalar;

public enum Colour {
    WHITE,
    GREEN,
    RED,
    BLUE,
    ORANGE,
    YELLOW;

    public double hue;
    public Scalar rgb;

    static {
        WHITE.hue = 0.;
        GREEN.hue = 74.;
        RED.hue = 174.;
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