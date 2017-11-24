package com.bjerkan.rubikscubeapp.rubikscube;

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;

/**
 * A class representing a Rubik's Cube.
 */
public class RubiksCube {

    /**
     * Create a Rubik's Cube with the given face colours. All colour lists are in the order
     * top-left, top-middle, top-right, middle-left, middle, middle-right, bottom-left,
     * bottom-middle, bottom-right.
     *
     * @param frontColours the colours for the front face
     * @param leftColours the colours for the left face
     * @param backColours the colours for the back face
     * @param rightColours the colours for the right face
     * @param topColours the colours of the top face
     * @param bottomColours the colours of the bottom face
     */
    public RubiksCube(
            List<Colour> frontColours,
            List<Colour> leftColours,
            List<Colour> backColours,
            List<Colour> rightColours,
            List<Colour> topColours,
            List<Colour> bottomColours) {
        front = new RubiksCubeFace(frontColours);
        left = new RubiksCubeFace(leftColours);
        back = new RubiksCubeFace(backColours);
        right = new RubiksCubeFace(rightColours);
        top = new RubiksCubeFace(topColours);
        bottom = new RubiksCubeFace(bottomColours);
    }

    /**
     * Rotates the cube clockwise to change which face is facing forwards.
     *
     * @return this cube after the rotation has been applied
     */
    RubiksCube rotate() {
        rotateAction();
        history.add(Action.ROTATE);
        return this;
    }

    /**
     * Rotates the cube anti-clockwise to change which face is facing forwards.
     *
     * @return this cube after the rotation has been applied
     */
    RubiksCube rotateInv() {
        rotateAction();
        rotateAction();
        rotateAction();
        history.add(Action.ROTATE_INV);
        return this;
    }

    void rotateAction() {
        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = right;
        right = back;
        back = left;
        left = previousFront;

        top = top.rotated();
        bottom = bottom.rotatedInv();
    }

    /**
     * Turns the front face of the cube clockwise.
     *
     * @return this cube after the front face has been turned
     */
    RubiksCube front() {
        frontAction();
        history.add(Action.FRONT);
        return this;
    }

    /**
     * Turns the front face of the cube anti-clockwise.
     *
     * @return this cube after the front face has been turned
     */
    RubiksCube frontInv() {
        frontAction();
        frontAction();
        frontAction();
        history.add(Action.FRONT_INV);
        return this;
    }

    private void frontAction() {
        front = front.rotated();

        RubiksCubeFace previousLeft = new RubiksCubeFace(left);
        left = left.replaceRightColumn(bottom.topRow());
        bottom = bottom.replaceTopRow(Lists.reverse(right.leftColumn()));
        right = right.replaceLeftColumn(top.bottomRow());
        top = top.replaceBottomRow(Lists.reverse(previousLeft.rightColumn()));
    }

    /**
     * Turns the left face of the cube clockwise.
     *
     * @return this cube after the left face has been turned
     */
    RubiksCube left() {
        leftAction();
        history.add(Action.LEFT);
        return this;
    }

    /**
     * Turns the left face of the cube anti-clockwise.
     *
     * @return this cube after the left face has been turned
     */
    RubiksCube leftInv() {
        leftAction();
        leftAction();
        leftAction();
        history.add(Action.LEFT_INV);
        return this;
    }

    private void leftAction() {
        left = left.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceLeftColumn(top.leftColumn());
        top = top.replaceLeftColumn(Lists.reverse(back.rightColumn()));
        back = back.replaceRightColumn(Lists.reverse(bottom.leftColumn()));
        bottom = bottom.replaceLeftColumn(previousFront.leftColumn());
    }

    /**
     * Turns the right face of the cube clockwise.
     *
     * @return this cube after the right face has been turned
     */
    RubiksCube right() {
        rightAction();
        history.add(Action.RIGHT);
        return this;
    }

    /**
     * Turns the right face of the cube anti-clockwise.
     *
     * @return this cube after the right face has been turned
     */
    RubiksCube rightInv() {
        rightAction();
        rightAction();
        rightAction();
        history.add(Action.RIGHT_INV);
        return this;
    }

    private void rightAction() {
        right = right.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceRightColumn(bottom.rightColumn());
        bottom = bottom.replaceRightColumn(Lists.reverse(back.leftColumn()));
        back = back.replaceLeftColumn(Lists.reverse(top.rightColumn()));
        top = top.replaceRightColumn(previousFront.rightColumn());
    }

    /**
     * Turns the top face of this cube clockwise.
     *
     * @return this face after the top face has been turned
     */
    RubiksCube top() {
        topAction();
        history.add(Action.TOP);
        return this;
    }

    /**
     * Turns the top face of this cube anti-clockwise.
     *
     * @return this face after the top face has been turned
     */
    RubiksCube topInv() {
        topAction();
        topAction();
        topAction();
        history.add(Action.TOP_INV);
        return this;
    }

    private void topAction() {
        top = top.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceTopRow(right.topRow());
        right = right.replaceTopRow(back.topRow());
        back = back.replaceTopRow(left.topRow());
        left = left.replaceTopRow(previousFront.topRow());
    }

    /**
     * Turns the bottom face of this cube clockwise.
     *
     * @return this face after the bottom face has been turned
     */
    RubiksCube bottom() {
        bottomAction();
        history.add(Action.BOTTOM);
        return this;
    }

    /**
     * Turns the bottom face of this cube anti-clockwise
     *
     * @return this face after the bottom face has been turned
     */
    RubiksCube bottomInv() {
        bottomAction();
        bottomAction();
        bottomAction();
        history.add(Action.BOTTOM_INV);
        return this;
    }

    private void bottomAction() {
        bottom = bottom.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceBottomRow(left.bottomRow());
        left = left.replaceBottomRow(back.bottomRow());
        back = back.replaceBottomRow(right.bottomRow());
        right = right.replaceBottomRow(previousFront.bottomRow());
    }

    RubiksCubeFace frontFace() {
        return front;
    }

    RubiksCubeFace leftFace() {
        return left;
    }

    RubiksCubeFace backFace() {
        return back;
    }

    RubiksCubeFace rightFace() {
        return right;
    }

    RubiksCubeFace topFace() {
        return top;
    }

    RubiksCubeFace bottomFace() {
        return bottom;
    }

    List<Action> history() {
        return history;
    }

    public enum Action {
        ROTATE, ROTATE_INV, FRONT, FRONT_INV, LEFT, LEFT_INV, RIGHT, RIGHT_INV, TOP, TOP_INV,
        BOTTOM, BOTTOM_INV
    }

    private RubiksCubeFace front;
    private RubiksCubeFace left;
    private RubiksCubeFace back;
    private RubiksCubeFace right;
    private RubiksCubeFace top;
    private RubiksCubeFace bottom;

    private List<Action> history = new LinkedList<>();
}
