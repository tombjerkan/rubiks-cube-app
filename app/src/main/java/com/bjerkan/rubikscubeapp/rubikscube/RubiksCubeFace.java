package com.bjerkan.rubikscubeapp.rubikscube;

import java.util.Arrays;
import java.util.List;

/**
 * A class representing a single face of a Rubik's Cube.
 */
class RubiksCubeFace {
    /**
     * Create a RubiksCubeFace with the square colours given.
     *
     * @param topLeft the colour of the top-left square
     * @param topMiddle the colour of the top-middle square
     * @param topRight the colour of the top-right square
     * @param middleLeft the colour of the middle-left square
     * @param middle the colour of the middle square
     * @param middleRight the colour of the middle-right square
     * @param bottomLeft the colour of the bottom-left square
     * @param bottomMiddle the colour of the bottom-middle square
     * @param bottomRight the colour of the bottom-right square
     */
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

    /**
     * Create a RubiksCubeFace with the square colours in the list. The list colours are in the
     * order top-left, top-middle, top-right, middle-left, middle, middle-right, bottom-left,
     * bottom-middle, bottom-right.
     *
     * @param colours a list of colours to give the face's squares
     */
    RubiksCubeFace(List<Colour> colours) {
        this(colours.get(0), colours.get(1), colours.get(2), colours.get(3), colours.get(4),
                colours.get(5), colours.get(6), colours.get(7), colours.get(8));
    }

    /**
     * Create a copy of the RubiksCubeFace given.
     *
     * @param face the face for which this new face will be a copy of
     */
    RubiksCubeFace(RubiksCubeFace face) {
        this(face.topLeft, face.topMiddle, face.topRight, face.middleLeft, face.middle,
                face.middleRight, face.bottomLeft, face.bottomMiddle, face.bottomRight);
    }

    /**
     * Returns a new RubiksCubeFace with this face's colours rotated clockwise.
     *
     * @return a new RubiksCubeFace for the current face rotated clockwise
     */
    RubiksCubeFace rotated() {
        return new RubiksCubeFace(
                bottomLeft(), middleLeft(), topLeft(),
                bottomMiddle(), middle(), topMiddle(),
                bottomRight(), middleRight(), topRight());
    }

    /**
     * Returns a new RubiksCubeFace with this face's colours rotated anti-clockwise.
     *
     * @return a new RubiksCubeFace for the current face rotated anti-clockwise
     */
    RubiksCubeFace rotatedInv() {
        return rotated().rotated().rotated();
    }

    /**
     * Returns a new RubiksCubeFace of this face with the top row colours replaced.
     *
     * @param newTopLeft new colour for top-left square
     * @param newTopMiddle new colour for top-middle square
     * @param newTopRight new colour for top-right square
     * @return a new RubiksCubeFace of this face with the top row colours replaced
     */
    RubiksCubeFace replaceTopRow(
            Colour newTopLeft, Colour newTopMiddle, Colour newTopRight) {
        return new RubiksCubeFace(
                newTopLeft, newTopMiddle, newTopRight,
                middleLeft, middle, middleRight,
                bottomLeft, bottomMiddle, bottomRight);
    }

    /**
     * Returns a new RubiksCubeFace of this face with the top row colours replaced.
     *
     * @param colours list of new colours for the top row in the order left, middle, right
     * @return a new RubiksCubeFace of this face with the top row colours replaced
     */
    RubiksCubeFace replaceTopRow(List<Colour> colours) {
        return replaceTopRow(colours.get(0), colours.get(1), colours.get(2));
    }

    /**
     * Returns a new RubiksCubeFace of this face with the bottom row colours replaced.
     *
     * @param newBottomLeft new colour for bottom-left square
     * @param newBottomMiddle new colour for bottom-middle square
     * @param newBottomRight new colour for bottom-right square
     * @return a new RubiksCubeFace of this face with the bottom row colours replaced
     */
    RubiksCubeFace replaceBottomRow(
            Colour newBottomLeft, Colour newBottomMiddle, Colour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, topRight,
                middleLeft, middle, middleRight,
                newBottomLeft, newBottomMiddle, newBottomRight);
    }

    /**
     * Returns a new RubiksCubeFace of this face with the bottom row colours replaced.
     *
     * @param colours list of new colours for the bottom row in the order left, middle, right
     * @return a new RubiksCubeFace of this face with the bottom row colours replaced
     */
    RubiksCubeFace replaceBottomRow(List<Colour> colours) {
        return replaceBottomRow(colours.get(0), colours.get(1), colours.get(2));
    }

    /**
     * Returns a new RubiksCubeFace of this face with the left column colours replaced.
     *
     * @param newTopLeft new colour for top-left square
     * @param newMiddleLeft new colour for middle-left square
     * @param newBottomLeft new colour for bottom-left square
     * @return a new RubiksCubeFace of this face with the left column colours replaced
     */
    RubiksCubeFace replaceLeftColumn(
            Colour newTopLeft, Colour newMiddleLeft, Colour newBottomLeft) {
        return new RubiksCubeFace(
                newTopLeft, topMiddle, topRight,
                newMiddleLeft, middle, middleRight,
                newBottomLeft, bottomMiddle, bottomRight);
    }

    /**
     * Returns a new RubiksCubeFace of this face with the left column colours replaced.
     *
     * @param colours list of new colours for the left column in the order top, middle, bottom
     * @return a new RubiksCubeFace of this face with the left column colours replaced
     */
    RubiksCubeFace replaceLeftColumn(List<Colour> colours) {
        return replaceLeftColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    /**
     * Returns a new RubiksCubeFace of this face with the right column colours replaced.
     *
     * @param newTopRight new colour for top-right square
     * @param newMiddleRight new colour for middle-right square
     * @param newBottomRight new colour for bottom-right square
     * @return a new RubiksCubeFace of this face with the right column colours replaced
     */
    RubiksCubeFace replaceRightColumn(
            Colour newTopRight, Colour newMiddleRight, Colour newBottomRight) {
        return new RubiksCubeFace(
                topLeft, topMiddle, newTopRight,
                middleLeft, middle, newMiddleRight,
                bottomLeft, bottomMiddle, newBottomRight);
    }

    /**
     * Returns a new RubiksCubeFace of this face with the right column colours replaced.
     *
     * @param colours list of new colours for the right column in the order top, middle, bottom
     * @return a new RubiksCubeFace of this face with the right column colours replaced
     */
    RubiksCubeFace replaceRightColumn(List<Colour> colours) {
        return replaceRightColumn(colours.get(0), colours.get(1), colours.get(2));
    }

    /**
     * Returns the colours of the top row of this face.
     *
     * @return a list of colours in the order top-left, top-middle, top-right
     */
    List<Colour> topRow() {
        return Arrays.asList(topLeft, topMiddle, topRight);
    }

    /**
     * Returns the colours of the bottom row of this face.
     *
     * @return a list of colours in the order bottom-left, bottom-middle, bottom-right
     */
    List<Colour> bottomRow() {
        return Arrays.asList(bottomLeft, bottomMiddle, bottomRight);
    }

    /**
     * Returns the colours of the left column of this face.
     *
     * @return a list of colours in the order top-left, middle-left, bottom-left
     */
    List<Colour> leftColumn() {
        return Arrays.asList(topLeft, middleLeft, bottomLeft);
    }

    /**
     * Returns the colours of the right column of this face.
     *
     * @return a list of colours in the order top-right, middle-right, bottom-right
     */
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
