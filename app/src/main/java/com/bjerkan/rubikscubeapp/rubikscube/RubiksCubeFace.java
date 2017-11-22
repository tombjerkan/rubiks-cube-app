package com.bjerkan.rubikscubeapp.rubikscube;

import java.util.Arrays;
import java.util.List;

class RubiksCubeFace {
    RubiksCubeFace(
            Colour topLeft,
            Colour topMiddle,
            Colour topRight,
            Colour middleLeft,
            Colour middle,
            Colour middleRight,
            Colour bottomLeft,
            Colour bottomMiddle,
            Colour bottomRight) {
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

    RubiksCubeFace(List<Colour> colours) {
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
            Colour newTopLeft, Colour newTopMiddle, Colour newTopRight) {
        return new RubiksCubeFace(
                newTopLeft, newTopMiddle, newTopRight,
                middleLeft, middle, middleRight,
                bottomLeft, bottomMiddle, bottomRight);
    }

    RubiksCubeFace replaceTopRow(List<Colour> colours) {
        return replaceTopRow(colours.get(0), colours.get(1), colours.get(2));
    }

    RubiksCubeFace replaceBottomRow(
            Colour newBottomLeft, Colour newBottomMiddle, Colour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, topRight,
                middleLeft, middle, middleRight,
                newBottomLeft, newBottomMiddle, newBottomRight);
    }

    RubiksCubeFace replaceBottomRow(List<Colour> colours) {
        return replaceBottomRow(colours.get(0), colours.get(1), colours.get(2));
    }

    RubiksCubeFace replaceLeftColumn(
            Colour newTopLeft, Colour newMiddleLeft, Colour newBottomLeft) {
        return new RubiksCubeFace(
                newTopLeft, topMiddle, topRight,
                newMiddleLeft, middle, middleRight,
                newBottomLeft, bottomMiddle, bottomRight);
    }

    RubiksCubeFace replaceLeftColumn(List<Colour> colours) {
        return replaceLeftColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    RubiksCubeFace replaceRightColumn(
            Colour newTopRight, Colour newMiddleRight, Colour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, newTopRight,
                middleLeft, middle, newMiddleRight,
                bottomLeft, bottomMiddle, newBottomRight);
    }

    RubiksCubeFace replaceRightColumn(List<Colour> colours) {
        return replaceRightColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    List<Colour> topRow() {
        return Arrays.asList(topLeft, topMiddle, topRight);
    }

    List<Colour> bottomRow() {
        return Arrays.asList(bottomLeft, bottomMiddle, bottomRight);
    }

    List<Colour> leftColumn() {
        return Arrays.asList(topLeft, middleLeft, bottomLeft);
    }

    List<Colour> rightColumn() {
        return Arrays.asList(topRight, middleRight, bottomRight);
    }

    Colour topLeft() {
        return topLeft;
    }

    Colour topMiddle() {
        return topMiddle;
    }

    Colour topRight() {
        return topRight;
    }

    Colour middleLeft() {
        return middleLeft;
    }

    Colour middle() {
        return middle;
    }

    Colour middleRight() {
        return middleRight;
    }

    Colour bottomLeft() {
        return bottomLeft;
    }

    Colour bottomMiddle() {
        return bottomMiddle;
    }

    Colour bottomRight() {
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

    private final Colour topLeft;
    private final Colour topMiddle;
    private final Colour topRight;
    private final Colour middleLeft;
    private final Colour middle;
    private final Colour middleRight;
    private final Colour bottomLeft;
    private final Colour bottomMiddle;
    private final Colour bottomRight;
}
