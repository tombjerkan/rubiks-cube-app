package com.bjerkan.rubikscubeapp;

import android.os.SystemClock;

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

    public void animate() {
        if (!animating) {
            return;
        }

        long timeElapsed = SystemClock.uptimeMillis() - startTime;

        if (timeElapsed >= ANIMATION_TIME) {
            animating = false;
            commitRotation();
        } else {
            tempRotation(timeElapsed);
        }
    }

    private void commitRotation() {
        switch (animationType) {
            case FRONT_INV: {
                faces[0].commitFaceRotation(Axis.Z, Direction.ANTICLOCKWISE);
                faces[1].commitRightColumnRotation(Axis.Z, Direction.ANTICLOCKWISE);
                faces[3].commitLeftColumnRotation(Axis.Z, Direction.ANTICLOCKWISE);
                faces[4].commitBottomRowRotation(Axis.Z, Direction.ANTICLOCKWISE);
                faces[5].commitTopRowRotation(Axis.Z, Direction.ANTICLOCKWISE);
            }
        }
    }

    private void tempRotation(long timeElapsed) {
        float angleToRotate = ((float) timeElapsed / ANIMATION_TIME) * 90f;

        switch (animationType) {
            case FRONT_INV: {
                faces[0].tempFaceRotation(Axis.Z, Direction.ANTICLOCKWISE, angleToRotate);
                faces[1].tempRightColumnRotation(Axis.Z, Direction.ANTICLOCKWISE, angleToRotate);
                faces[3].tempLeftColumnRotation(Axis.Z, Direction.ANTICLOCKWISE, angleToRotate);
                faces[4].tempBottomRowRotation(Axis.Z, Direction.ANTICLOCKWISE, angleToRotate);
                faces[5].tempTopRowRotation(Axis.Z, Direction.ANTICLOCKWISE, angleToRotate);
            }
        }
    }

    public void front_inv() {
        if (!animating) {
            animating = true;
            startTime = SystemClock.uptimeMillis();
            animationType = Animation.FRONT_INV;
        }
    }

    private enum Animation {
        FRONT_INV
    }

    public enum Axis {
        X, Y, Z
    }

    public enum Direction {
        CLOCKWISE,
        ANTICLOCKWISE
    }

    private RubiksFace[] faces = new RubiksFace[6];

    private boolean animating = false;
    private long startTime;
    private Animation animationType;

    private static final int ANIMATION_TIME = 1500;
}
