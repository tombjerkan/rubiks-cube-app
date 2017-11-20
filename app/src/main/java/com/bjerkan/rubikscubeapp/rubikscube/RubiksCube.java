package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner;
import com.google.common.collect.Lists;

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

    public void rotate() {
        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = right;
        right = back;
        back = left;
        left = previousFront;

        top = top.rotated();
        bottom = bottom.rotatedInv();
    }

    public void rotateInv() {
        rotate();
        rotate();
        rotate();
    }

    public void front() {
        front = front.rotated();

        RubiksCubeFace previousLeft = new RubiksCubeFace(left);
        left = left.replaceRightColumn(bottom.topRow());
        bottom.replaceTopRow(Lists.reverse(right.leftColumn()));
        right.replaceLeftColumn(top.bottomRow());
        top.replaceBottomRow(Lists.reverse(previousLeft.rightColumn()));
    }

    public void frontInv() {
        front();
        front();
        front();
    }

    public void left() {
        left = left.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front.replaceLeftColumn(top.leftColumn());
        top.replaceLeftColumn(Lists.reverse(back.rightColumn()));
        back.replaceRightColumn(Lists.reverse(bottom.leftColumn()));
        bottom.replaceLeftColumn(previousFront.leftColumn());
    }

    public void leftInv() {
        left();
        left();
        left();
    }

    public void right() {
        right = right.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front.replaceRightColumn(bottom.rightColumn());
        bottom.replaceRightColumn(Lists.reverse(back.leftColumn()));
        back.replaceLeftColumn(Lists.reverse(top.rightColumn()));
        top.replaceRightColumn(previousFront.rightColumn());
    }

    public void rightInv() {
        right();
        right();
        right();
    }

    public void top() {
        top = top.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front.replaceTopRow(right.topRow());
        right.replaceTopRow(back.topRow());
        back.replaceTopRow(left.topRow());
        left.replaceTopRow(previousFront.topRow());
    }

    public void topInv() {
        top();
        top();
        top();
    }

    public void bottom() {
        bottom = bottom.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front.replaceBottomRow(left.bottomRow());
        left.replaceBottomRow(back.bottomRow());
        back.replaceBottomRow(right.bottomRow());
        right.replaceBottomRow(previousFront.bottomRow());
    }

    public void bottomInv() {
        bottom();
        bottom();
        bottom();
    }

    public RubiksCubeFace frontFace() {
        return front;
    }

    public RubiksCubeFace leftFace() {
        return left;
    }

    public RubiksCubeFace backFace() {
        return back;
    }

    public RubiksCubeFace rightFace() {
        return right;
    }

    public RubiksCubeFace topFace() {
        return top;
    }

    public RubiksCubeFace bottomFace() {
        return bottom;
    }

    private RubiksCubeFace front;
    private RubiksCubeFace left;
    private RubiksCubeFace back;
    private RubiksCubeFace right;
    private RubiksCubeFace top;
    private RubiksCubeFace bottom;
}
