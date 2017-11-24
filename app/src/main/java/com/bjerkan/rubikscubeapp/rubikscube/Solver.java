package com.bjerkan.rubikscubeapp.rubikscube;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class for solving a given Rubik's Cube.
 *
 * The package-private methods solveBottomEdges, solveBottomCorners, solveSecondRow,
 * solveTopEdges and solveTopCorners are only package-private so that they can be accessed from
 * tests. This allows the tests to test each step individually to diagnose where an error occurs.
 * They should not be used directly outside of the tests.
 */
public class Solver {
    /**
     * Solves the given cube and returns the actions needed to solve it.
     *
     * @param cube the cube to solve
     * @return a list of actions performed while solving the cube
     */
    public static List<RubiksCube.Action> solve(RubiksCube cube) {
        solveBottomEdges(cube);
        solveBottomCorners(cube);
        solveSecondRow(cube);
        solveTopEdges(cube);
        solveTopCorners(cube);
        return cube.history();
    }

    static void solveBottomEdges(RubiksCube cube) {
        while (!bottomEdgesOnTop(cube)) {
            if (cube.bottomFace().topMiddle() == cube.bottomFace().middle()) {
                while (cube.topFace().bottomMiddle() == cube.bottomFace().middle()) {
                    cube.top();
                }

                cube.front().front();
            } else if (cube.leftFace().middleRight() == cube.bottomFace().middle()) {
                while (cube.topFace().bottomMiddle() == cube.bottomFace().middle()) {
                    cube.top();
                }

                cube.front();
            } else if (cube.frontFace().middleLeft() == cube.bottomFace().middle()) {
                while (cube.topFace().middleLeft() == cube.bottomFace().middle()) {
                    cube.top();
                }

                cube.leftInv();
            } else if (cube.frontFace().topMiddle() == cube.bottomFace().middle()) {
                cube.front().topInv().right();
            } else if (cube.frontFace().bottomMiddle() == cube.bottomFace().middle()) {
                while (cube.topFace().bottomMiddle() == cube.bottomFace().middle()) {
                    cube.top();
                }

                cube.front().topInv().right();
            } else {
                cube.rotate();
            }
        }

        for (int face = 0; face < 4; face++) {
            while (!frontTopEdgeAbovePosition(cube)) {
                cube.top();
            }

            cube.front().front().rotate();
        }
    }

    private static boolean bottomEdgesOnTop(RubiksCube cube) {
        return allAreColour(
                cube.bottomFace().middle(),
                Arrays.asList(
                        cube.topFace().topMiddle(), cube.topFace().middleLeft(),
                        cube.topFace().middleRight(), cube.topFace().bottomMiddle()));
    }

    private static boolean frontTopEdgeAbovePosition(RubiksCube cube) {
        return cube.frontFace().topMiddle() == cube.frontFace().middle() &&
                cube.topFace().bottomMiddle() == cube.bottomFace().middle();
    }

    static void solveBottomCorners(RubiksCube cube) {
        while (!bottomFaceComplete(cube)) {
            handleBottomCorner(cube);
            handleTopCorner(cube);
            cube.rotateInv();
        }
    }

    private static boolean bottomFaceComplete(RubiksCube cube) {
        RubiksCubeFace face = cube.bottomFace();
        boolean bottomFaceComplete = allAreColour(cube.bottomFace().middle(),
                Arrays.asList(
                        face.topLeft(), face.topMiddle(), face.topRight(), face.middleLeft(),
                        face.middleRight(), face.bottomLeft(), face.bottomMiddle(),
                        face.bottomRight()));

        boolean frontCorrect = bottomRowCorrect(cube.frontFace());
        boolean leftCorrect = bottomRowCorrect(cube.leftFace());
        boolean backCorrect = bottomRowCorrect(cube.backFace());
        boolean rightCorrect = bottomRowCorrect(cube.rightFace());

        return bottomFaceComplete && frontCorrect && leftCorrect && backCorrect && rightCorrect;
    }

    private static boolean bottomRowCorrect(RubiksCubeFace face) {
        return allAreColour(face.middle(),
                Arrays.asList(face.bottomLeft(), face.bottomMiddle(), face.bottomRight()));
    }

    // Moves the bottom, left, front corner to the top row if it is a bottom corner in the wrong
    // position so that it can then be moved from the top to the correct bottom row position.
    private static void handleBottomCorner(RubiksCube cube) {
        boolean bottomCornerCorrect = cube.bottomFace().topLeft() == cube.bottomFace().middle() &&
                cube.frontFace().bottomLeft() == cube.frontFace().middle() &&
                cube.leftFace().bottomRight() == cube.leftFace().middle();

        boolean bottomCornerHasBottomColour = bottomCornerColours(cube)
                .contains(cube.bottomFace().middle());

        if (bottomCornerHasBottomColour && !bottomCornerCorrect) {
            cube.leftInv().top().left();
        }
    }

    // Returns the 3 colours of the bottom, left, front corner as a set
    private static Set<Colour> bottomCornerColours(RubiksCube cube) {
        return Stream.of(
                cube.frontFace().bottomLeft(),
                cube.leftFace().bottomRight(),
                cube.bottomFace().topLeft())
                .collect(Collectors.toSet());
    }

    private static void handleTopCorner(RubiksCube cube) {
        if (topCornerColours(cube).contains(cube.bottomFace().middle())) {
            while (!topCornerColours(cube).equals(frontLeftBottomFaceColours(cube))) {
                cube.top().rotateInv();
            }

            if (cube.frontFace().topLeft() == cube.bottomFace().middle()) {
                cube.front().top().frontInv();
            } else if (cube.leftFace().topRight() == cube.bottomFace().middle()) {
                cube.top().front().topInv().frontInv();
            } else if (cube.topFace().bottomLeft() == cube.bottomFace().middle()) {
                for (int repeat = 0; repeat < 3; repeat++) {
                    cube.top().front().topInv().frontInv();
                }
            }
        }
    }

    // Returns the 3 colours of the top, left, front corner as a set
    private static Set<Colour> topCornerColours(RubiksCube cube) {
        return Stream.of(
                cube.frontFace().topLeft(),
                cube.leftFace().topRight(),
                cube.topFace().bottomLeft())
                .collect(Collectors.toSet());
    }

    private static Set<Colour> frontLeftBottomFaceColours(RubiksCube cube) {
        return Stream.of(
                cube.frontFace().middle(),
                cube.leftFace().middle(),
                cube.bottomFace().middle()).collect(Collectors.toSet());
    }

    static void solveSecondRow(RubiksCube cube) {
        for (int face = 0; face < 4; face++) {
            if (secondRowBelongsOnRow(cube)) {
                while (topEdgeBelongsInSecondRow(cube)) {
                    cube.top();
                }
                cube.topInv().leftInv().top().left().top().front().topInv().frontInv();
            }

            cube.rotate();
        }

        for (int face = 0; face < 4; face++) {
            while (!topEdgeInPosition(cube)) {
                cube.top();
            }

            if (cube.frontFace().topMiddle() == cube.frontFace().middle()) {
                cube.topInv().leftInv().top().left().top().front().topInv().frontInv();
            } else {
                cube.top().top().front().topInv().frontInv().topInv().leftInv().top().left();
            }

            cube.rotate();
        }
    }

    // Returns true if the left, front edge belongs somewhere on the second row (rather than the top
    // row)
    private static boolean secondRowBelongsOnRow(RubiksCube cube) {
        return cube.frontFace().middleLeft() != cube.topFace().middle() &&
                cube.leftFace().middleRight() != cube.topFace().middle();
    }

    // Returns true if the top, front edge belongs on the top
    private static boolean topEdgeBelongsInSecondRow(RubiksCube cube) {
        return cube.frontFace().topMiddle() != cube.topFace().middle() &&
                cube.topFace().bottomMiddle() != cube.topFace().middle();
    }

    // Returns true if the top front edge belongs in the front, left edge position
    private static boolean topEdgeInPosition(RubiksCube cube) {
        Set<Colour> topEdgeColours = Stream.of(
                cube.frontFace().topMiddle(), cube.topFace().bottomMiddle())
                .collect(Collectors.toSet());

        Set<Colour> frontLeftFaceColours = Stream.of(
                cube.frontFace().middle(), cube.leftFace().middle()).collect(Collectors.toSet());

        return topEdgeColours.equals(frontLeftFaceColours);
    }

    static void solveTopEdges(RubiksCube cube) {
        getTopCross(cube);
        alignTopEdges(cube);
    }

    private static void getTopCross(RubiksCube cube) {
        if (isTopDot(cube)) {
            cube.front().top().right().topInv().rightInv().frontInv();
        }

        while (!hasTopCross(cube)) {
            if (hasTopReverseL(cube)) {
                cube.front().top().right().topInv().rightInv().frontInv();
            } else if (hasTopLine(cube)) {
                cube.front().right().top().rightInv().topInv().frontInv();
            } else {
                cube.rotate();
            }
        }
    }

    // Returns true if the top is the single dot scenario (no reverse-L or line or already solved
    // for any rotation)
    private static boolean isTopDot(RubiksCube cube) {
        Stream<Colour> topColours = Stream.of(
                cube.topFace().topMiddle(), cube.topFace().middleLeft(),
                cube.topFace().middleRight(), cube.topFace().bottomMiddle());

        return topColours.filter(colour -> colour == cube.topFace().middle()).count() < 2;
    }

    private static boolean hasTopCross(RubiksCube cube) {
        return allAreColour(cube.topFace().middle(), Arrays.asList(
                cube.topFace().topMiddle(), cube.topFace().middleLeft(),
                cube.topFace().middleRight(), cube.topFace().bottomMiddle()));
    }

    private static boolean hasTopReverseL(RubiksCube cube) {
        return allAreColour(cube.topFace().middle(), Arrays.asList(
                cube.topFace().topMiddle(), cube.topFace().middleLeft()));
    }

    private static boolean hasTopLine(RubiksCube cube) {
        return allAreColour(cube.topFace().middle(), Arrays.asList(
                cube.topFace().middleLeft(), cube.topFace().middleRight()));
    }


    private static void alignTopEdges(RubiksCube cube) {
        // The colour that must be in its position on the top edge of the back of the cube
        Colour backColour = null;

        // The order of face colours clockwise on the cube
        Map<Colour, Colour> nextColour = new HashMap<>();
        nextColour.put(cube.frontFace().middle(), cube.leftFace().middle());
        nextColour.put(cube.leftFace().middle(), cube.backFace().middle());
        nextColour.put(cube.backFace().middle(), cube.rightFace().middle());
        nextColour.put(cube.rightFace().middle(), cube.frontFace().middle());

        // Finds two adjacent edges which are both correct relative to each other
        while (backColour == null) {
            if (cube.leftFace().topMiddle() == nextColour.get(cube.frontFace().topMiddle())) {
                backColour = cube.frontFace().topMiddle();
            } else if (cube.backFace().topMiddle() == nextColour.get(cube.leftFace().topMiddle())) {
                backColour = cube.leftFace().topMiddle();
            } else if (cube.rightFace().topMiddle() ==
                    nextColour.get(cube.backFace().topMiddle())) {
                backColour = cube.backFace().topMiddle();
            } else if (cube.frontFace().topMiddle() ==
                    nextColour.get(cube.rightFace().topMiddle())) {
                backColour = cube.rightFace().topMiddle();
            } else {
                // No appropriate faces so apply algorithm and try again
                cube.right().top().rightInv().top().right().top().top().rightInv().top();
            }
        }

        while (cube.backFace().middle() != backColour) {
            cube.rotate();
        }

        while (cube.backFace().topMiddle() != backColour) {
            cube.top();
        }

        if (!topEdgesAligned(cube)) {
            cube.right().top().rightInv().top().right().top().top().rightInv().top();
        }
    }

    private static boolean topEdgesAligned(RubiksCube cube) {
        return cube.frontFace().topMiddle() == cube.frontFace().middle() &&
                cube.leftFace().topMiddle() == cube.leftFace().middle() &&
                cube.backFace().topMiddle() == cube.backFace().middle() &&
                cube.rightFace().topMiddle() == cube.rightFace().middle();
    }

    static void solveTopCorners(RubiksCube cube) {
        positionCorners(cube);
        orientCorners(cube);
        correctRows(cube);
    }

    private static void positionCorners(RubiksCube cube) {
        ensureCornerInPosition(cube);

        while (!cornersInPosition(cube)) {
            cube.top().right().topInv().leftInv().top().rightInv().topInv().left();
        }
    }

    // Ensures the front, right, top corner is in position
    private static void ensureCornerInPosition(RubiksCube cube) {
        if (frontLeftCornerInPosition(cube)) {
            cube.rotateInv();
        } else if (backLeftCornerInPosition(cube)) {
            cube.rotate().rotate();
        } else if (backRightCornerInPosition(cube)) {
            cube.rotate();
        } else if (!frontRightCornerInPosition(cube)) {
            cube.top().right().topInv().leftInv().top().rightInv().topInv().left();
            ensureCornerInPosition(cube);
        }
    }

    private static boolean cornersInPosition(RubiksCube cube) {
        return frontLeftCornerInPosition(cube) &&
                frontRightCornerInPosition(cube) &&
                backLeftCornerInPosition(cube) &&
                backRightCornerInPosition(cube);
    }

    private static boolean frontLeftCornerInPosition(RubiksCube cube) {
        Set<Colour> frontLeftCornerColours = Stream.of(
                cube.frontFace().topLeft(),
                cube.leftFace().topRight(),
                cube.topFace().bottomLeft()).collect(Collectors.toSet());

        return frontLeftCornerColours.contains(cube.frontFace().middle()) &&
                frontLeftCornerColours.contains(cube.leftFace().middle());
    }

    private static boolean frontRightCornerInPosition(RubiksCube cube) {
        Set<Colour> frontRightCornerColours = Stream.of(
                cube.frontFace().topRight(),
                cube.rightFace().topLeft(),
                cube.topFace().bottomRight()).collect(Collectors.toSet());

        return frontRightCornerColours.contains(cube.frontFace().middle()) &&
                frontRightCornerColours.contains(cube.rightFace().middle());
    }

    private static boolean backLeftCornerInPosition(RubiksCube cube) {
        Set<Colour> backLeftCornerColours = Stream.of(
                cube.backFace().topRight(),
                cube.leftFace().topLeft(),
                cube.topFace().topLeft()).collect(Collectors.toSet());

        return backLeftCornerColours.contains(cube.backFace().middle()) &&
                backLeftCornerColours.contains(cube.leftFace().middle());
    }

    private static boolean backRightCornerInPosition(RubiksCube cube) {
        Set<Colour> backRightCornerColours = Stream.of(
                cube.backFace().topLeft(),
                cube.rightFace().topRight(),
                cube.topFace().topRight()).collect(Collectors.toSet());

        return backRightCornerColours.contains(cube.backFace().middle()) &&
                backRightCornerColours.contains(cube.rightFace().middle());
    }

    private static void orientCorners(RubiksCube cube) {
        while (!topCornersComplete(cube)) {
            while (cube.topFace().bottomRight() != cube.topFace().middle()) {
                cube.rightInv().bottomInv().right().bottom();
            }

            cube.top();
        }
    }

    private static boolean topCornersComplete(RubiksCube cube) {
        return allAreColour(cube.topFace().middle(), Arrays.asList(
                cube.topFace().topLeft(), cube.topFace().topRight(), cube.topFace().bottomLeft(),
                cube.topFace().bottomRight()));
    }

    private static void correctRows(RubiksCube cube) {
        while (cube.frontFace().topMiddle() != cube.frontFace().middle()) {
            cube.top();
        }

        while (cube.frontFace().bottomMiddle() != cube.frontFace().middle()) {
            cube.bottom();
        }
    }

    private static boolean allAreColour(Colour matchColour, List<Colour> colours) {
        return colours.stream().allMatch(colour -> colour == matchColour);
    }
}
