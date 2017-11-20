package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour;

import java.util.Arrays;
import java.util.List;

class RubiksCubeFace {
    RubiksCubeFace(
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

    RubiksCubeFace(List<RubiksColour> colours) {
        this(colours.get(0), colours.get(1), colours.get(2), colours.get(3), colours.get(4),
                colours.get(5), colours.get(6), colours.get(7), colours.get(8));
    }

    RubiksCubeFace(RubiksCubeFace face) {
        this(face.topLeft, face.topMiddle, face.topRight, face.middleLeft, face.middle,
                face.middleRight, face.bottomLeft, face.bottomMiddle, face.bottomRight);
    }

    RubiksCubeFace rotated() {
        return new RubiksCubeFace(
                bottomLeft(), middleLeft(), topLeft(),
                bottomMiddle(), middle(), topMiddle(),
                bottomRight(), middleRight(), topRight());
    }

    RubiksCubeFace rotatedInv() {
        return rotated().rotated().rotated();
    }

    RubiksCubeFace replaceTopRow(
            RubiksColour newTopLeft, RubiksColour newTopMiddle, RubiksColour newTopRight) {
        return new RubiksCubeFace(
                newTopLeft, newTopMiddle, newTopRight,
                middleLeft, middle, middleRight,
                bottomLeft, bottomMiddle, bottomRight);
    }

    RubiksCubeFace replaceTopRow(List<RubiksColour> colours) {
        return replaceTopRow(colours.get(0), colours.get(1), colours.get(2));
    }

    RubiksCubeFace replaceBottomRow(
            RubiksColour newBottomLeft, RubiksColour newBottomMiddle, RubiksColour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, topRight,
                middleLeft, middle, middleRight,
                newBottomLeft, newBottomMiddle, newBottomRight);
    }

    RubiksCubeFace replaceBottomRow(List<RubiksColour> colours) {
        return replaceBottomRow(colours.get(0), colours.get(1), colours.get(2));
    }

    RubiksCubeFace replaceLeftColumn(
            RubiksColour newTopLeft, RubiksColour newMiddleLeft, RubiksColour newBottomLeft) {
        return new RubiksCubeFace(
                newTopLeft, topMiddle, topRight,
                newMiddleLeft, middle, middleRight,
                newBottomLeft, bottomMiddle, bottomRight);
    }

    RubiksCubeFace replaceLeftColumn(List<RubiksColour> colours) {
        return replaceLeftColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    RubiksCubeFace replaceRightColumn(
            RubiksColour newTopRight, RubiksColour newMiddleRight, RubiksColour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, newTopRight,
                middleLeft, middle, newMiddleRight,
                bottomLeft, bottomMiddle, newBottomRight);
    }

    RubiksCubeFace replaceRightColumn(List<RubiksColour> colours) {
        return replaceRightColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    List<RubiksColour> topRow() {
        return Arrays.asList(topLeft, topMiddle, topRight);
    }

    List<RubiksColour> bottomRow() {
        return Arrays.asList(bottomLeft, bottomMiddle, bottomRight);
    }

    List<RubiksColour> leftColumn() {
        return Arrays.asList(topLeft, middleLeft, bottomLeft);
    }

    List<RubiksColour> rightColumn() {
        return Arrays.asList(topRight, middleRight, bottomRight);
    }

    RubiksColour topLeft() {
        return topLeft;
    }

    RubiksColour topMiddle() {
        return topMiddle;
    }

    RubiksColour topRight() {
        return topRight;
    }

    RubiksColour middleLeft() {
        return middleLeft;
    }

    RubiksColour middle() {
        return middle;
    }

    RubiksColour middleRight() {
        return middleRight;
    }

    RubiksColour bottomLeft() {
        return bottomLeft;
    }

    RubiksColour bottomMiddle() {
        return bottomMiddle;
    }

    RubiksColour bottomRight() {
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
