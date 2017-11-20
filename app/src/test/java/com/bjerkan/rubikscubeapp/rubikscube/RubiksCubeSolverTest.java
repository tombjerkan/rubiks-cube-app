package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class RubiksCubeSolverTest {
    @Test
    public void test_solve() throws Exception {
        randomCubeSet().forEach(cube -> {
            Solver.solve(cube);

            assertTrue(faceSolved(cube.frontFace()));
            assertTrue(faceSolved(cube.leftFace()));
            assertTrue(faceSolved(cube.backFace()));
            assertTrue(faceSolved(cube.rightFace()));
            assertTrue(faceSolved(cube.topFace()));
            assertTrue(faceSolved(cube.bottomFace()));
        });
    }

    @Test
    public void test_solveBottomEdges() throws Exception {
        randomCubeSet().forEach(cube -> {
            Solver.solveBottomEdges(cube);

            assertTrue(allSame(Arrays.asList(
                    cube.bottomFace().middle(), cube.bottomFace().topMiddle(),
                    cube.bottomFace().middleLeft(), cube.bottomFace().middleRight(),
                    cube.bottomFace().bottomMiddle())));

            assertTrue(cube.frontFace().bottomMiddle() == cube.frontFace().middle());
            assertTrue(cube.leftFace().bottomMiddle() == cube.leftFace().middle());
            assertTrue(cube.backFace().bottomMiddle() == cube.backFace().middle());
            assertTrue(cube.rightFace().bottomMiddle() == cube.rightFace().middle());
        });
    }

    @Test
    public void test_solveBottomCorners() throws Exception {
        randomCubeSet().forEach(cube -> {
            Solver.solveBottomEdges(cube);
            Solver.solveBottomCorners(cube);

            assertTrue(allSame(Arrays.asList(
                    cube.bottomFace().middle(), cube.bottomFace().topMiddle(),
                    cube.bottomFace().middleLeft(), cube.bottomFace().middleRight(),
                    cube.bottomFace().bottomMiddle())));

            assertTrue(allSame(Arrays.asList(
                    cube.frontFace().middle(), cube.frontFace().bottomMiddle(),
                    cube.frontFace().bottomLeft(), cube.frontFace().bottomRight())));

            assertTrue(allSame(Arrays.asList(
                    cube.leftFace().middle(), cube.leftFace().bottomMiddle(),
                    cube.leftFace().bottomLeft(), cube.leftFace().bottomRight())));

            assertTrue(allSame(Arrays.asList(
                    cube.backFace().middle(), cube.backFace().bottomMiddle(),
                    cube.backFace().bottomLeft(), cube.backFace().bottomRight())));

            assertTrue(allSame(Arrays.asList(
                    cube.rightFace().middle(), cube.rightFace().bottomMiddle(),
                    cube.rightFace().bottomLeft(), cube.rightFace().bottomRight())));
        });
    }

    @Test
    public void test_solveSecondRow() throws Exception {
        randomCubeSet().forEach(cube -> {
            Solver.solveBottomEdges(cube);
            Solver.solveBottomCorners(cube);
            Solver.solveSecondRow(cube);

            assertTrue(allSame(Arrays.asList(
                    cube.bottomFace().middle(), cube.bottomFace().topMiddle(),
                    cube.bottomFace().middleLeft(), cube.bottomFace().middleRight(),
                    cube.bottomFace().bottomMiddle())));

            assertTrue(allSame(Arrays.asList(
                    cube.frontFace().middle(), cube.frontFace().middleLeft(),
                    cube.frontFace().middleRight(), cube.frontFace().bottomMiddle(),
                    cube.frontFace().bottomLeft(), cube.frontFace().bottomRight())));

            assertTrue(allSame(Arrays.asList(
                    cube.leftFace().middle(), cube.leftFace().middleLeft(),
                    cube.leftFace().middleRight(), cube.leftFace().bottomMiddle(),
                    cube.leftFace().bottomLeft(), cube.leftFace().bottomRight())));

            assertTrue(allSame(Arrays.asList(
                    cube.backFace().middle(), cube.backFace().middleLeft(),
                    cube.backFace().middleRight(), cube.backFace().bottomMiddle(),
                    cube.backFace().bottomLeft(), cube.backFace().bottomRight())));

            assertTrue(allSame(Arrays.asList(
                    cube.rightFace().middle(), cube.rightFace().middleLeft(),
                    cube.rightFace().middleRight(), cube.rightFace().bottomMiddle(),
                    cube.rightFace().bottomLeft(), cube.rightFace().bottomRight())));
        });
    }

    @Test
    public void test_solveTopEdges() throws Exception {
        randomCubeSet().forEach(cube -> {
            Solver.solveBottomEdges(cube);
            Solver.solveBottomCorners(cube);
            Solver.solveSecondRow(cube);
            Solver.solveTopEdges(cube);

            assertTrue(allSame(Arrays.asList(
                    cube.bottomFace().middle(), cube.bottomFace().topMiddle(),
                    cube.bottomFace().middleLeft(), cube.bottomFace().middleRight(),
                    cube.bottomFace().bottomMiddle())));

            assertTrue(allSame(Arrays.asList(
                    cube.frontFace().middle(), cube.frontFace().middleLeft(),
                    cube.frontFace().middleRight(), cube.frontFace().bottomMiddle(),
                    cube.frontFace().bottomLeft(), cube.frontFace().bottomRight(),
                    cube.frontFace().topMiddle())));

            assertTrue(allSame(Arrays.asList(
                    cube.leftFace().middle(), cube.leftFace().middleLeft(),
                    cube.leftFace().middleRight(), cube.leftFace().bottomMiddle(),
                    cube.leftFace().bottomLeft(), cube.leftFace().bottomRight(),
                    cube.leftFace().topMiddle())));

            assertTrue(allSame(Arrays.asList(
                    cube.backFace().middle(), cube.backFace().middleLeft(),
                    cube.backFace().middleRight(), cube.backFace().bottomMiddle(),
                    cube.backFace().bottomLeft(), cube.backFace().bottomRight(),
                    cube.backFace().topMiddle())));

            assertTrue(allSame(Arrays.asList(
                    cube.rightFace().middle(), cube.rightFace().middleLeft(),
                    cube.rightFace().middleRight(), cube.rightFace().bottomMiddle(),
                    cube.rightFace().bottomLeft(), cube.rightFace().bottomRight(),
                    cube.rightFace().topMiddle())));
        });
    }

    private boolean allSame(List<RubiksColour> colours) {
        return colours.stream().distinct().count() == 1;
    }

    private boolean faceSolved(RubiksCubeFace face) {
        return allSame(Arrays.asList(
                face.topLeft(), face.topMiddle(), face.topRight(),
                face.middleLeft(), face.middle(), face.middleRight(),
                face.bottomLeft(), face.bottomMiddle(), face.bottomRight()));
    }

    private List<RubiksCube> randomCubeSet() {
        List<RubiksCube> randomCubes = new ArrayList<>(100);
        for (int repeat = 0; repeat < 100; repeat++) {
            randomCubes.add(randomCube());
        }
        return randomCubes;
    }

    private RubiksCube randomCube() {
        RubiksCube cube = solvedCube();
        shuffleCube(cube);
        return cube;
    }

    private RubiksCube solvedCube() {
        return new RubiksCube(
                Collections.nCopies(9, RubiksColour.WHITE),
                Collections.nCopies(9, RubiksColour.RED),
                Collections.nCopies(9, RubiksColour.YELLOW),
                Collections.nCopies(9, RubiksColour.ORANGE),
                Collections.nCopies(9, RubiksColour.GREEN),
                Collections.nCopies(9, RubiksColour.BLUE));
    }

    private void shuffleCube(RubiksCube cube) {
        new Random().ints(100, 0, 12)
            .forEach(randomActionIndex -> {
                if (randomActionIndex == 0) {
                    cube.rotate();
                } else if (randomActionIndex == 1) {
                    cube.rotateInv();
                } else if (randomActionIndex == 2) {
                    cube.front();
                } else if (randomActionIndex == 3) {
                    cube.frontInv();
                } else if (randomActionIndex == 4) {
                    cube.left();
                } else if (randomActionIndex == 5) {
                    cube.leftInv();
                } else if (randomActionIndex == 6) {
                    cube.right();
                } else if (randomActionIndex == 7) {
                    cube.rightInv();
                } else if (randomActionIndex == 8) {
                    cube.top();
                } else if (randomActionIndex == 9) {
                    cube.topInv();
                } else if (randomActionIndex == 10) {
                    cube.bottom();
                } else if (randomActionIndex == 11) {
                    cube.bottomInv();
                }
            });
    }
}