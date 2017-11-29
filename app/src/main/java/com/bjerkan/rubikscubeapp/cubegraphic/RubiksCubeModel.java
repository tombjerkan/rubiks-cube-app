package com.bjerkan.rubikscubeapp.cubegraphic;

import android.os.SystemClock;

import com.bjerkan.rubikscubeapp.rubikscube.Colour;
import com.bjerkan.rubikscubeapp.rubikscube.RubiksCubeFace;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * A class representing the graphical model of a Rubik's Cube.
 */
class RubiksCubeModel {

    public RubiksCubeModel(
            RubiksCubeFace frontFace,
            RubiksCubeFace leftFace,
            RubiksCubeFace backFace,
            RubiksCubeFace rightFace,
            RubiksCubeFace topFace,
            RubiksCubeFace bottomFace) {
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
            frontSubCubes().get(colourIndex).setFrontColour(frontFace.colours().get(colourIndex));
            leftSubCubes().get(colourIndex).setLeftColour(leftFace.colours().get(colourIndex));
            backSubCubes().get(colourIndex).setBackColour(backFace.colours().get(colourIndex));
            rightSubCubes().get(colourIndex).setRightColour(rightFace.colours().get(colourIndex));
            topSubCubes().get(colourIndex).setTopColour(topFace.colours().get(colourIndex));
            bottomSubCubes().get(colourIndex).setBottomColour(
                    bottomFace.colours().get(colourIndex));
        }
    }

    /**
     * Creates a graphical Rubik's Cube model with the given square colours. All colour lists are in
     * the order top-left, top-middle, top-right, middle-left, middle, middle-right, bottom-left,
     * bottom-middle, bottom-right.
     *
     * @param frontColours the colours for the front face
     * @param leftColours the colours for the left face
     * @param backColours the colours for the back face
     * @param rightColours the colours for the right face
     * @param topColours the colours for the top face
     * @param bottomColours the colours for the bottom face
     */
    RubiksCubeModel(
            List<Colour> frontColours,
            List<Colour> leftColours,
            List<Colour> backColours,
            List<Colour> rightColours,
            List<Colour> topColours,
            List<Colour> bottomColours) {
    }

    /**
     * Draws the Rubik's Cube graphic to the OpenGL context.
     *
     * @param gl the OpenGL context to draw to
     */
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

    /**
     * Animates the turning of the front face of the cube graphic clockwise.
     */
    void front() {
        startAnimation(frontSubCubes(), Axis.Z, Direction.CLOCKWISE);
        frontCubeSwap();
    }

    /**
     * Animates the turning of the front face of the cube graphic anti-clockwise.
     */
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

    /**
     * Animates the turning of the left face of the cube graphic clockwise.
     */
    void left() {
        startAnimation(leftSubCubes(), Axis.X, Direction.ANTICLOCKWISE);
        leftCubeSwap();
    }

    /**
     * Animates the turning of the front face of the cube graphic anti-clockwise.
     */
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

    /**
     * Animates the turning of the right face of the cube graphic clockwise.
     */
    void right() {
        startAnimation(rightSubCubes(), Axis.X, Direction.CLOCKWISE);
        rightCubeSwap();
    }

    /**
     * Animates the turning of the left face of the cube graphic anti-clockwise.
     */
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

    /**
     * Animates the turning of the top face of the cube graphic clockwise.
     */
    void top() {
        startAnimation(topSubCubes(), Axis.Y, Direction.CLOCKWISE);
        topCubeSwap();
    }

    /**
     * Animates the turning of the top face of the cube graphic anti-clockwise.
     */
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

    /**
     * Animates the turning of the bottom face of the cube graphic clockwise.
     */
    void bottom() {
        startAnimation(bottomSubCubes(), Axis.Y, Direction.ANTICLOCKWISE);
        bottomCubeSwap();
    }

    /**
     * Animates the turning of the bottom face of the cube graphic anti-clockwise.
     */
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

    /**
     * Animates the rotating of the cube graphic clockwise.
     */
    void rotate() {
        startAnimation(allSubCubes(), Axis.Y, Direction.CLOCKWISE);
        rotateCubeSwap();
    }

    /**
     * Animates the rotating of the cube graphic anti-clockwise.
     */
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

    /**
     * Sets a listener to be informed when an animation is finished.
     *
     * @param animationListener the object to set as listener
     */
    public void setAnimationFinishedListener(AnimationFinishedListener animationListener) {
        this.animationListener = animationListener;
    }

    /**
     * Interface to be implemented by objects that want to listen for when an animation is finished.
     */
    public interface AnimationFinishedListener {
        /**
         * Called by RubiksCubeModel when an animation is finished.
         */
        void animationFinished();
    }

    /**
     * An enum representing the different axes to rotate around during animations.
     */
    public enum Axis {
        X, Y, Z
    }

    /**
     * An enum representing the directions to rotate around the axes during animations.
     */
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
