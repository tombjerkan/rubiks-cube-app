package com.bjerkan.rubikscubeapp.cubegraphic;

import android.os.SystemClock;

import com.bjerkan.rubikscubeapp.rubikscube.Colour;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * A class representing the graphical model of a Rubik's Cube.
 */
class RubiksCubeModel {

    RubiksCubeModel(
            List<Colour> frontColours,
            List<Colour> leftColours,
            List<Colour> backColours,
            List<Colour> rightColours,
            List<Colour> topColours,
            List<Colour> bottomColours) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    float centreX = (x - 1) * SUBCUBE_SIZE;
                    float centreY = (1 - y) * SUBCUBE_SIZE;
                    float centreZ = (1 - z) * SUBCUBE_SIZE;
                    subCubes[x][y][z] = new Cube(new Vertex(centreX, centreY, centreZ),
                            SUBCUBE_SIZE);
                    subCubes[x][y][z].setAnimationTime(ANIMATION_TIME);
                }
            }
        }

        for (int colourIndex = 0; colourIndex < 3 * 3; colourIndex++) {
            frontSubCubes().get(colourIndex).setFrontColour(frontColours.get(colourIndex));
            leftSubCubes().get(colourIndex).setLeftColour(leftColours.get(colourIndex));
            backSubCubes().get(colourIndex).setBackColour(backColours.get(colourIndex));
            rightSubCubes().get(colourIndex).setRightColour(rightColours.get(colourIndex));
            topSubCubes().get(colourIndex).setTopColour(topColours.get(colourIndex));
            bottomSubCubes().get(colourIndex).setBottomColour(bottomColours.get(colourIndex));
        }
    }

    void draw(GL10 gl) {
        long timeElapsed = SystemClock.uptimeMillis() - startTime;

        boolean animationFinished = animating && timeElapsed > ANIMATION_TIME;

        if (animationFinished) {
            animating = false;
            allSubCubes().forEach(Cube::finishAnimation);
        }

        allSubCubes().forEach(cube -> cube.draw(gl, timeElapsed));

        // Must be called after cubes are drawn so doesn't start drawing new animation before last
        if (animationFinished && animationListener != null) {
            animationListener.animationFinished();
        }
    }

    void front() {
        startAnimation(frontSubCubes(), Axis.Z, Direction.CLOCKWISE);
        frontCubeSwap();
    }

    void frontInv() {
        startAnimation(frontSubCubes(), Axis.Z, Direction.ANTICLOCKWISE);
        frontCubeSwap();
        frontCubeSwap();
        frontCubeSwap();
    }

    private void frontCubeSwap() {
        Cube temp = subCubes[0][0][0];
        subCubes[0][0][0] = subCubes[0][2][0];
        subCubes[0][2][0] = subCubes[2][2][0];
        subCubes[2][2][0] = subCubes[2][0][0];
        subCubes[2][0][0] = temp;

        temp = subCubes[1][0][0];
        subCubes[1][0][0] = subCubes[0][1][0];
        subCubes[0][1][0] = subCubes[1][2][0];
        subCubes[1][2][0] = subCubes[2][1][0];
        subCubes[2][1][0] = temp;
    }

    void left() {
        startAnimation(leftSubCubes(), Axis.X, Direction.ANTICLOCKWISE);
        leftCubeSwap();
    }

    void leftInv() {
        startAnimation(leftSubCubes(), Axis.X, Direction.CLOCKWISE);
        leftCubeSwap();
        leftCubeSwap();
        leftCubeSwap();
    }

    private void leftCubeSwap() {
        Cube temp = subCubes[0][0][2];
        subCubes[0][0][2] = subCubes[0][2][2];
        subCubes[0][2][2] = subCubes[0][2][0];
        subCubes[0][2][0] = subCubes[0][0][0];
        subCubes[0][0][0] = temp;

        temp = subCubes[0][0][1];
        subCubes[0][0][1] = subCubes[0][1][2];
        subCubes[0][1][2] = subCubes[0][2][1];
        subCubes[0][2][1] = subCubes[0][1][0];
        subCubes[0][1][0] = temp;
    }

    void right() {
        startAnimation(rightSubCubes(), Axis.X, Direction.CLOCKWISE);
        rightCubeSwap();
    }

    void rightInv() {
        startAnimation(rightSubCubes(), Axis.X, Direction.ANTICLOCKWISE);
        rightCubeSwap();
        rightCubeSwap();
        rightCubeSwap();
    }

    private void rightCubeSwap() {
        Cube temp = subCubes[2][0][0];
        subCubes[2][0][0] = subCubes[2][2][0];
        subCubes[2][2][0] = subCubes[2][2][2];
        subCubes[2][2][2] = subCubes[2][0][2];
        subCubes[2][0][2] = temp;

        temp = subCubes[2][0][1];
        subCubes[2][0][1] = subCubes[2][1][0];
        subCubes[2][1][0] = subCubes[2][2][1];
        subCubes[2][2][1] = subCubes[2][1][2];
        subCubes[2][1][2] = temp;
    }

    void top() {
        startAnimation(topSubCubes(), Axis.Y, Direction.CLOCKWISE);
        topCubeSwap();
    }

    void topInv() {
        startAnimation(topSubCubes(), Axis.Y, Direction.ANTICLOCKWISE);
        topCubeSwap();
        topCubeSwap();
        topCubeSwap();
    }

    private void topCubeSwap() {
        Cube temp = subCubes[0][0][2];
        subCubes[0][0][2] = subCubes[0][0][0];
        subCubes[0][0][0] = subCubes[2][0][0];
        subCubes[2][0][0] = subCubes[2][0][2];
        subCubes[2][0][2] = temp;

        temp = subCubes[1][0][2];
        subCubes[1][0][2] = subCubes[0][0][1];
        subCubes[0][0][1] = subCubes[1][0][0];
        subCubes[1][0][0] = subCubes[2][0][1];
        subCubes[2][0][1] = temp;
    }

    void bottom() {
        startAnimation(bottomSubCubes(), Axis.Y, Direction.ANTICLOCKWISE);
        bottomCubeSwap();
    }

    void bottomInv() {
        startAnimation(bottomSubCubes(), Axis.Y, Direction.CLOCKWISE);
        bottomCubeSwap();
        bottomCubeSwap();
        bottomCubeSwap();
    }

    private void bottomCubeSwap() {
        Cube temp = subCubes[0][2][0];
        subCubes[0][2][0] = subCubes[0][2][2];
        subCubes[0][2][2] = subCubes[2][2][2];
        subCubes[2][2][2] = subCubes[2][2][0];
        subCubes[2][2][0] = temp;

        temp = subCubes[1][2][0];
        subCubes[1][2][0] = subCubes[0][2][1];
        subCubes[0][2][1] = subCubes[1][2][2];
        subCubes[1][2][2] = subCubes[2][2][1];
        subCubes[2][2][1] = temp;
    }

    void rotate() {
        startAnimation(allSubCubes(), Axis.Y, Direction.CLOCKWISE);
        rotateCubeSwap();
    }

    void rotateInv() {
        startAnimation(allSubCubes(), Axis.Y, Direction.ANTICLOCKWISE);
        rotateCubeSwap();
        rotateCubeSwap();
        rotateCubeSwap();
    }

    private void rotateCubeSwap() {
        topCubeSwap();
        bottomCubeSwap();
        bottomCubeSwap();
        bottomCubeSwap();

        // Middle cube swap
        Cube temp = subCubes[0][1][2];
        subCubes[0][1][2] = subCubes[0][1][0];
        subCubes[0][1][0] = subCubes[2][1][0];
        subCubes[2][1][0] = subCubes[2][1][2];
        subCubes[2][1][2] = temp;

        temp = subCubes[1][1][2];
        subCubes[1][1][2] = subCubes[0][1][1];
        subCubes[0][1][1] = subCubes[1][1][0];
        subCubes[1][1][0] = subCubes[2][1][1];
        subCubes[2][1][1] = temp;
    }

    public void setAnimationFinishedListener(AnimationFinishedListener animationListener) {
        this.animationListener = animationListener;
    }

    public interface AnimationFinishedListener {
        void animationFinished();
    }

    public enum Axis {
        X, Y, Z
    }

    public enum Direction {
        CLOCKWISE,
        ANTICLOCKWISE
    }

    private void startAnimation(List<Cube> subCubes, Axis axis, Direction direction) {
        animating = true;
        startTime = SystemClock.uptimeMillis();
        subCubes.forEach(subCube -> subCube.startAnimation(axis, direction));
    }

    private List<Cube> allSubCubes() {
        List<Cube> allSubCubes = new ArrayList<>(3 * 3 * 3);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    allSubCubes.add(subCubes[x][y][z]);
                }
            }
        }

        return allSubCubes;
    }

    private List<Cube> frontSubCubes() {
        List<Cube> frontSubCubes = new ArrayList<>(3 * 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                frontSubCubes.add(subCubes[x][y][0]);
            }
        }
        return frontSubCubes;
    }

    private List<Cube> leftSubCubes() {
        List<Cube> leftSubCubes = new ArrayList<>(3 * 3);
        for (int y = 0; y < 3; y++) {
            for (int z = 2; z >= 0; z--) {
                leftSubCubes.add(subCubes[0][y][z]);
            }
        }
        return leftSubCubes;
    }

    private List<Cube> backSubCubes() {
        List<Cube> backSubCubes = new ArrayList<>(3 * 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 2; x >= 0; x--) {
                backSubCubes.add(subCubes[x][y][2]);
            }
        }
        return backSubCubes;
    }

    private List<Cube> rightSubCubes() {
        List<Cube> rightSubCubes = new ArrayList<>(3 * 3);
        for (int y = 0; y < 3; y++) {
            for (int z = 0; z < 3; z++) {
                rightSubCubes.add(subCubes[2][y][z]);
            }
        }
        return rightSubCubes;
    }

    private List<Cube> topSubCubes() {
        List<Cube> topSubCubes = new ArrayList<>(3 * 3);
        for (int z = 2; z >= 0; z--) {
            for (int x = 0; x < 3; x++) {
                topSubCubes.add(subCubes[x][0][z]);
            }
        }
        return topSubCubes;
    }

    private List<Cube> bottomSubCubes() {
        List<Cube> bottomSubCubes = new ArrayList<>(3 * 3);
        for (int z = 0; z < 3; z++) {
            for (int x = 0; x < 3; x++) {
                bottomSubCubes.add(subCubes[x][2][z]);
            }
        }
        return bottomSubCubes;
    }

    // (x, y, z) z = 0 is front, x = 0 is left, y = 0 is top
    private Cube[][][] subCubes = new Cube[3][3][3];

    private static final float CUBE_SIZE = 6f;
    private static final float SUBCUBE_SIZE = CUBE_SIZE / 3f;

    private static final int ANIMATION_TIME = 1000;
    private boolean animating = false;
    private long startTime;
    private AnimationFinishedListener animationListener;
}
