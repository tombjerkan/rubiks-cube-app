package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;

public class RubiksCube {

    public RubiksCube(
            List<CubeScanner.RubiksColour> frontColours,
            List<CubeScanner.RubiksColour> leftColours,
            List<CubeScanner.RubiksColour> backColours,
            List<CubeScanner.RubiksColour> rightColours,
            List<CubeScanner.RubiksColour> topColours,
            List<CubeScanner.RubiksColour> bottomColours) {
        front = new RubiksCubeFace(frontColours);
        left = new RubiksCubeFace(leftColours);
        back = new RubiksCubeFace(backColours);
        right = new RubiksCubeFace(rightColours);
        top = new RubiksCubeFace(topColours);
        bottom = new RubiksCubeFace(bottomColours);
    }

    RubiksCube rotate() {
        rotateAction();
        history.add(Action.ROTATE);
        return this;
    }

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

    RubiksCube front() {
        frontAction();
        history.add(Action.FRONT);
        return this;
    }

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

    RubiksCube left() {
        leftAction();
        history.add(Action.LEFT);
        return this;
    }

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

    RubiksCube right() {
        rightAction();
        history.add(Action.RIGHT);
        return this;
    }

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

    RubiksCube top() {
        topAction();
        history.add(Action.TOP);
        return this;
    }

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

    RubiksCube bottom() {
        bottomAction();
        history.add(Action.BOTTOM);
        return this;
    }

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
