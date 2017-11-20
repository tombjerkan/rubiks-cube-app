package com.bjerkan.rubikscubeapp.cubegraphic;

import com.bjerkan.rubikscubeapp.CubeScanner;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

class Cube {
    public Cube(float centreX, float centreY, float centreZ, float sideLength) {
        float leftX = centreX - (sideLength / 2f);
        float rightX = centreX + (sideLength / 2f);
        float bottomY = centreY - (sideLength / 2f);
        float topY = centreY + (sideLength / 2f);
        float backZ = centreZ - (sideLength / 2f);
        float frontZ = centreZ + (sideLength / 2f);

        float[] frontTopLeft = {leftX, topY, frontZ};
        float[] frontTopRight = {rightX, topY, frontZ};
        float[] frontBottomLeft = {leftX, bottomY, frontZ};
        float[] frontBottomRight = {rightX, bottomY, frontZ};
        float[] backTopLeft = {rightX, topY, backZ};
        float[] backTopRight = {leftX, topY, backZ};
        float[] backBottomLeft = {rightX, bottomY, backZ};
        float[] backBottomRight = {leftX, bottomY, backZ};

        frontSquare = new Square(frontTopLeft, frontTopRight, frontBottomRight, frontBottomLeft);
        leftSquare = new Square(backTopRight, frontTopLeft, frontBottomLeft, backBottomRight);
        backSquare = new Square(backTopLeft, backTopRight, backBottomRight, backBottomLeft);
        rightSquare = new Square(frontTopRight, backTopLeft, backBottomLeft, frontBottomRight);
        topSquare = new Square(backTopRight, backTopLeft, frontTopRight, frontTopLeft);
        bottomSquare = new Square(frontBottomLeft, frontBottomRight, backBottomLeft,
                backBottomRight);
    }

    public void draw(GL10 gl, long drawStartTime) {
        gl.glPushMatrix();

        if (animating) {
            long timeElapsed = drawStartTime - animationStartTime;

            if (timeElapsed > ANIMATION_TIME) {
                animating = false;
                animationHistory.add(new Animation(animationAxis, animationDirection));
            } else {
                float angleToRotate = ((float) timeElapsed / ANIMATION_TIME) * 90f;
                rotate(gl, animationAxis, animationDirection, angleToRotate);
            }
        }

        // Transformation matrices must be applied in reverse to give desired order
        for (Animation animation : Lists.reverse(animationHistory)) {
            rotate(gl, animation.axis, animation.direction, 90f);
        }

        squares().forEach(square -> square.draw(gl));
        gl.glPopMatrix();
    }

    public void startAnimation(long animationStartTime, RubiksCubeModel.Axis axis,
                               RubiksCubeModel.Direction direction) {
        animating = true;
        this.animationStartTime = animationStartTime;
        animationAxis = axis;
        animationDirection = direction;
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setFrontColour(CubeScanner.RubiksColour colour) {
        frontSquare.setColour(colour);
    }

    public void setLeftColour(CubeScanner.RubiksColour colour) {
        leftSquare.setColour(colour);
    }

    public void setBackColour(CubeScanner.RubiksColour colour) {
        backSquare.setColour(colour);
    }

    public void setRightColour(CubeScanner.RubiksColour colour) {
        rightSquare.setColour(colour);
    }

    public void setTopColour(CubeScanner.RubiksColour colour) {
        topSquare.setColour(colour);
    }

    public void setBottomColour(CubeScanner.RubiksColour colour) {
        bottomSquare.setColour(colour);
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
    private long animationStartTime;
    private RubiksCubeModel.Axis animationAxis;
    private RubiksCubeModel.Direction animationDirection;

    private static final int ANIMATION_TIME = 1000;

    private List<Animation> animationHistory = new LinkedList<>();

    private class Animation {
        public Animation(RubiksCubeModel.Axis axis, RubiksCubeModel.Direction direction) {
            this.axis = axis;
            this.direction = direction;
        }

        public RubiksCubeModel.Axis axis;
        public RubiksCubeModel.Direction direction;
    }
}