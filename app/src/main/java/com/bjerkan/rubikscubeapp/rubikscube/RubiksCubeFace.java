package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour;

import java.util.Arrays;
import java.util.List;

class RubiksCubeFace {
    public RubiksCubeFace(
            RubiksColour topLeft,
            RubiksColour topMiddle,
            RubiksColour topRight,
            RubiksColour middleLeft,
            RubiksColour middle,
            RubiksColour middleRight,
            RubiksColour bottomLeft,
            RubiksColour bottomMiddle,
            RubiksColour bottomRight) {
        this.topLeft = topLeft;
        this.topMiddle = topMiddle;
        this.topRight = topRight;
        this.middleLeft = middleLeft;
        this.middle = middle;
        this.middleRight = middleRight;
        this.bottomLeft = bottomLeft;
        this.bottomMiddle = bottomMiddle;
        this.bottomRight = bottomRight;
    }

    public RubiksCubeFace(List<RubiksColour> colours) {
        this(colours.get(0), colours.get(1), colours.get(2), colours.get(3), colours.get(4),
                colours.get(5), colours.get(6), colours.get(7), colours.get(8));
    }

    public RubiksCubeFace(RubiksCubeFace face) {
        this(face.topLeft, face.topMiddle, face.topRight, face.middleLeft, face.middle,
                face.middleRight, face.bottomLeft, face.bottomMiddle, face.bottomRight);
    }

    public RubiksCubeFace rotated() {
        return new RubiksCubeFace(
                bottomLeft(), middleLeft(), topLeft(),
                bottomMiddle(), middle(), topMiddle(),
                bottomRight(), middleRight(), topRight());
    }

    public RubiksCubeFace rotatedInv() {
        return rotated().rotated().rotated();
    }

    public RubiksCubeFace replaceTopRow(
            RubiksColour newTopLeft, RubiksColour newTopMiddle, RubiksColour newTopRight) {
        return new RubiksCubeFace(
                newTopLeft, newTopMiddle, newTopRight,
                middleLeft, middle, middleRight,
                bottomLeft, bottomMiddle, bottomRight);
    }

    public RubiksCubeFace replaceTopRow(List<RubiksColour> colours) {
        return replaceTopRow(colours.get(0), colours.get(1), colours.get(2));
    }

    public RubiksCubeFace replaceBottomRow(
            RubiksColour newBottomLeft, RubiksColour newBottomMiddle, RubiksColour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, topRight,
                middleLeft, middle, middleRight,
                newBottomLeft, newBottomMiddle, newBottomRight);
    }

    public RubiksCubeFace replaceBottomRow(List<RubiksColour> colours) {
        return replaceBottomRow(colours.get(0), colours.get(1), colours.get(2));
    }

    public RubiksCubeFace replaceLeftColumn(
            RubiksColour newTopLeft, RubiksColour newMiddleLeft, RubiksColour newBottomLeft) {
        return new RubiksCubeFace(
                newTopLeft, topMiddle, topRight,
                newMiddleLeft, middle, middleRight,
                newBottomLeft, bottomMiddle, bottomRight);
    }

    public RubiksCubeFace replaceLeftColumn(List<RubiksColour> colours) {
        return replaceLeftColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    public RubiksCubeFace replaceRightColumn(
            RubiksColour newTopRight, RubiksColour newMiddleRight, RubiksColour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, newTopRight,
                middleLeft, middle, newMiddleRight,
                bottomLeft, bottomMiddle, newBottomRight);
    }

    public RubiksCubeFace replaceRightColumn(List<RubiksColour> colours) {
        return replaceRightColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    public List<RubiksColour> topRow() {
        return Arrays.asList(topLeft, topMiddle, topRight);
    }

    public List<RubiksColour> bottomRow() {
        return Arrays.asList(bottomLeft, bottomMiddle, bottomRight);
    }

    public List<RubiksColour> leftColumn() {
        return Arrays.asList(topLeft, middleLeft, bottomLeft);
    }

    public List<RubiksColour> rightColumn() {
        return Arrays.asList(topRight, middleRight, bottomRight);
    }

    public RubiksColour topLeft() {
        return topLeft;
    }

    public RubiksColour topMiddle() {
        return topMiddle;
    }

    public RubiksColour topRight() {
        return topRight;
    }

    public RubiksColour middleLeft() {
        return middleLeft;
    }

    public RubiksColour middle() {
        return middle;
    }

    public RubiksColour middleRight() {
        return middleRight;
    }

    public RubiksColour bottomLeft() {
        return bottomLeft;
    }

    public RubiksColour bottomMiddle() {
        return bottomMiddle;
    }

    public RubiksColour bottomRight() {
        return bottomRight;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof RubiksCubeFace)) {
            return false;
        }

        RubiksCubeFace otherFace = (RubiksCubeFace) other;
        return topLeft == otherFace.topLeft &&
                topMiddle == otherFace.topMiddle &&
                topRight == otherFace.topRight &&
                middleLeft == otherFace.middleLeft &&
                middle == otherFace.middle &&
                middleRight == otherFace.middleRight &&
                bottomLeft == otherFace.bottomLeft &&
                bottomMiddle == otherFace.bottomMiddle &&
                bottomRight == otherFace.bottomRight;
    }

    private final RubiksColour topLeft;
    private final RubiksColour topMiddle;
    private final RubiksColour topRight;
    private final RubiksColour middleLeft;
    private final RubiksColour middle;
    private final RubiksColour middleRight;
    private final RubiksColour bottomLeft;
    private final RubiksColour bottomMiddle;
    private final RubiksColour bottomRight;
}
