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

    public RubiksCube rotate() {
        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = right;
        right = back;
        back = left;
        left = previousFront;

        top = top.rotated();
        bottom = bottom.rotatedInv();

        return this;
    }

    public RubiksCube rotateInv() {
        rotate();
        rotate();
        rotate();

        return this;
    }

    public RubiksCube front() {
        front = front.rotated();

        RubiksCubeFace previousLeft = new RubiksCubeFace(left);
        left = left.replaceRightColumn(bottom.topRow());
        bottom = bottom.replaceTopRow(Lists.reverse(right.leftColumn()));
        right = right.replaceLeftColumn(top.bottomRow());
        top = top.replaceBottomRow(Lists.reverse(previousLeft.rightColumn()));

        return this;
    }

    public RubiksCube frontInv() {
        front();
        front();
        front();

        return this;
    }

    public RubiksCube left() {
        left = left.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceLeftColumn(top.leftColumn());
        top = top.replaceLeftColumn(Lists.reverse(back.rightColumn()));
        back = back.replaceRightColumn(Lists.reverse(bottom.leftColumn()));
        bottom = bottom.replaceLeftColumn(previousFront.leftColumn());

        return this;
    }

    public RubiksCube leftInv() {
        left();
        left();
        left();

        return this;
    }

    public RubiksCube right() {
        right = right.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceRightColumn(bottom.rightColumn());
        bottom = bottom.replaceRightColumn(Lists.reverse(back.leftColumn()));
        back = back.replaceLeftColumn(Lists.reverse(top.rightColumn()));
        top = top.replaceRightColumn(previousFront.rightColumn());

        return this;
    }

    public RubiksCube rightInv() {
        right();
        right();
        right();

        return this;
    }

    public RubiksCube top() {
        top = top.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceTopRow(right.topRow());
        right = right.replaceTopRow(back.topRow());
        back = back.replaceTopRow(left.topRow());
        left = left.replaceTopRow(previousFront.topRow());

        return this;
    }

    public RubiksCube topInv() {
        top();
        top();
        top();

        return this;
    }

    public RubiksCube bottom() {
        bottom = bottom.rotated();

        RubiksCubeFace previousFront = new RubiksCubeFace(front);
        front = front.replaceBottomRow(left.bottomRow());
        left = left.replaceBottomRow(back.bottomRow());
        back = back.replaceBottomRow(right.bottomRow());
        right = right.replaceBottomRow(previousFront.bottomRow());

        return this;
    }

    public RubiksCube bottomInv() {
        bottom();
        bottom();
        bottom();

        return this;
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
