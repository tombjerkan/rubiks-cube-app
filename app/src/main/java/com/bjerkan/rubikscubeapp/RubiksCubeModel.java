package com.bjerkan.rubikscubeapp;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class RubiksCubeModel {

    public RubiksCubeModel(
            List<CubeScanner.RubiksColour> frontColoursList,
            List<CubeScanner.RubiksColour> leftColoursList,
            List<CubeScanner.RubiksColour> backColoursList,
            List<CubeScanner.RubiksColour> rightColoursList,
            List<CubeScanner.RubiksColour> topColoursList,
            List<CubeScanner.RubiksColour> bottomColoursList) {
        float[] frontTopLeft = {-3f, 3f, 3f};
        float[] frontTopRight = {3f, 3f, 3f};
        float[] frontBottomRight = {3f, -3f, 3f};
        float[] frontBottomLeft = {-3f, -3f, 3f};
        float[] backTopLeft = {3f, 3f, -3f};
        float[] backTopRight = {-3f, 3f, -3f};
        float[] backBottomRight = {-3f, -3f, -3f};
        float[] backBottomLeft = {3f, -3f, -3f};

        faces[0] = new RubiksFace(frontColoursList,
                frontTopLeft, frontTopRight, frontBottomRight, frontBottomLeft);

        faces[1] = new RubiksFace(leftColoursList,
                backTopRight, frontTopLeft, frontBottomLeft, backBottomRight);

        faces[2] = new RubiksFace(backColoursList,
                backTopLeft, backTopRight, backBottomRight, backBottomLeft);

        faces[3] = new RubiksFace(rightColoursList,
                frontTopRight, backTopLeft, backBottomLeft, frontBottomRight);

        faces[4] = new RubiksFace(topColoursList,
                backTopRight, backTopLeft, frontTopRight, frontTopLeft);

        faces[5] = new RubiksFace(bottomColoursList,
                frontBottomLeft, frontBottomRight, backBottomLeft, backBottomRight);
    }

    public void draw(GL10 gl) {
        for (RubiksFace face : faces) {
            face.draw(gl);
        }
    }

    RubiksFace[] faces = new RubiksFace[6];
}
