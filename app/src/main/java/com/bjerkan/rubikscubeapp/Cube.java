package com.bjerkan.rubikscubeapp;

import java.util.Arrays;
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

    public void draw(GL10 gl) {
        squares().forEach(square -> square.draw(gl));
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
}