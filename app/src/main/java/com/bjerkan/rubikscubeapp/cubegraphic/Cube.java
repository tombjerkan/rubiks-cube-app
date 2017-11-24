package com.bjerkan.rubikscubeapp.cubegraphic;

import com.bjerkan.rubikscubeapp.rubikscube.Colour;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * A class for a cube graphic.
 */
class Cube {
    /**
     * Creates a cube graphic with the given properties.
     *
     * @param centre the position of the cube's centre
     * @param sideLength the length of the cube's sides
     */
    Cube(Vertex centre, float sideLength) {
        float leftX = centre.x() - (sideLength / 2f);
        float rightX = centre.x() + (sideLength / 2f);
        float bottomY = centre.y() - (sideLength / 2f);
        float topY = centre.y() + (sideLength / 2f);
        float backZ = centre.z() - (sideLength / 2f);
        float frontZ = centre.z() + (sideLength / 2f);

        Vertex frontTopLeft = new Vertex(leftX, topY, frontZ);
        Vertex frontTopRight = new Vertex(rightX, topY, frontZ);
        Vertex frontBottomLeft = new Vertex(leftX, bottomY, frontZ);
        Vertex frontBottomRight = new Vertex(rightX, bottomY, frontZ);
        Vertex backTopLeft = new Vertex(rightX, topY, backZ);
        Vertex backTopRight = new Vertex(leftX, topY, backZ);
        Vertex backBottomLeft = new Vertex(rightX, bottomY, backZ);
        Vertex backBottomRight = new Vertex(leftX, bottomY, backZ);

        frontSquare = new Square(frontTopLeft, frontTopRight, frontBottomRight, frontBottomLeft);
        leftSquare = new Square(backTopRight, frontTopLeft, frontBottomLeft, backBottomRight);
        backSquare = new Square(backTopLeft, backTopRight, backBottomRight, backBottomLeft);
        rightSquare = new Square(frontTopRight, backTopLeft, backBottomLeft, frontBottomRight);
        topSquare = new Square(backTopRight, backTopLeft, frontTopRight, frontTopLeft);
        bottomSquare = new Square(frontBottomLeft, frontBottomRight, backBottomLeft,
                backBottomRight);
    }

    /**
     * Draws the cube to the given OpenGL context.
     *
     * @param gl the OpenGL context to draw to
     * @param timeElapsed the time elapsed since the last draw for use with animation
     */
    void draw(GL10 gl, long timeElapsed) {
        gl.glPushMatrix();

        if (animating) {
            float angleToRotate = ((float) timeElapsed / animationTime) * 90f;
            rotate(gl, currentAnimation.axis, currentAnimation.direction, angleToRotate);
        }

        // Transformation matrices must be applied in reverse to give desired order
        for (Animation animation : Lists.reverse(animationHistory)) {
            rotate(gl, animation.axis, animation.direction, 90f);
        }

        squares().forEach(square -> square.draw(gl));
        gl.glPopMatrix();
    }

    /**
     * Starts animating the cube by rotating it in the given direction around the given axis. Should
     * not start an animation if an animation is already being performed.
     *
     * @param axis the axis to rotate the cube around
     * @param direction the direction around the axis to rotate the cube
     */
    void startAnimation(RubiksCubeModel.Axis axis, RubiksCubeModel.Direction direction) {
        animating = true;
        currentAnimation = new Animation(axis, direction);
    }

    /**
     * Finish the animation currently being performed.
     */
    void finishAnimation() {
        if (animating) {
            animating = false;
            animationHistory.add(currentAnimation);
        }
    }

    void setFrontColour(Colour colour) {
        frontSquare.setColour(colour);
    }

    void setLeftColour(Colour colour) {
        leftSquare.setColour(colour);
    }

    void setBackColour(Colour colour) {
        backSquare.setColour(colour);
    }

    void setRightColour(Colour colour) {
        rightSquare.setColour(colour);
    }

    void setTopColour(Colour colour) {
        topSquare.setColour(colour);
    }

    void setBottomColour(Colour colour) {
        bottomSquare.setColour(colour);
    }

    void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    private void rotate(GL10 gl, RubiksCubeModel.Axis axis, RubiksCubeModel.Direction direction,
                        float angle) {
        if (direction == RubiksCubeModel.Direction.CLOCKWISE) {
            angle = -angle;
        }

        if (axis == RubiksCubeModel.Axis.X) {
            gl.glRotatef(angle, 1f, 0f, 0f);
        } else if (axis == RubiksCubeModel.Axis.Y) {
            gl.glRotatef(angle, 0f, 1f, 0f);
        } else {
            gl.glRotatef(angle, 0f, 0f, 1f);
        }
    }

    private List<Square> squares() {
        return Arrays.asList(
                frontSquare, leftSquare, backSquare, rightSquare, topSquare, bottomSquare);
    }

    private Square frontSquare;
    private Square leftSquare;
    private Square backSquare;
    private Square rightSquare;
    private Square topSquare;
    private Square bottomSquare;

    private boolean animating = false;
    private Animation currentAnimation;

    private int animationTime = DEFAULT_ANIMATION_TIME;
    private static final int DEFAULT_ANIMATION_TIME = 1000;

    private List<Animation> animationHistory = new LinkedList<>();

    private class Animation {
        Animation(RubiksCubeModel.Axis axis, RubiksCubeModel.Direction direction) {
            this.axis = axis;
            this.direction = direction;
        }

        RubiksCubeModel.Axis axis;
        RubiksCubeModel.Direction direction;
    }
}