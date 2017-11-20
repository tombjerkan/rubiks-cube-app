package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour;

import java.util.Arrays;
import java.util.List;

public class Solver {
    public static void solve(RubiksCube cube) {
        solveBottomEdges(cube);
        solveBottomCorners(cube);
        solveSecondRow(cube);
        solveTopEdges(cube);
        solveTopCorners(cube);
    }

    public static void solveBottomEdges(RubiksCube cube) {
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

    public static void solveBottomCorners(RubiksCube cube) {

    }

    public static void solveSecondRow(RubiksCube cube) {

    }

    public static void solveTopEdges(RubiksCube cube) {

    }

    public static void solveTopCorners(RubiksCube cube) {

    }

    private static boolean allAreColour(RubiksColour matchColour, List<RubiksColour> colours) {
        return colours.stream().allMatch(colour -> colour == matchColour);
    }
}
