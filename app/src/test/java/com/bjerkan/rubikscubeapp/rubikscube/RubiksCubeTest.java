package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour;

import org.junit.Test;

import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.BLUE;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.GREEN;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.ORANGE;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.RED;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.WHITE;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.YELLOW;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.BOTTOM;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.BOTTOM_INV;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.FRONT;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.FRONT_INV;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.LEFT;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.LEFT_INV;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.RIGHT;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.RIGHT_INV;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.ROTATE;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.ROTATE_INV;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.TOP;
import static com.bjerkan.rubikscubeapp.rubikscube.RubiksCube.Action.TOP_INV;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class RubiksCubeTest {
    @Test
    public void test_rotate() {
        RubiksCube cube = createCube();
        cube.rotate();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                YELLOW, RED, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                WHITE, BLUE, ORANGE,
                GREEN, YELLOW, GREEN)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                BLUE, GREEN, RED,
                ORANGE, YELLOW, ORANGE,
                ORANGE, GREEN, ORANGE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                WHITE, RED, RED,
                GREEN, WHITE, BLUE,
                RED, BLUE, YELLOW)));
    }

    @Test
    public void test_rotateInv() {
        RubiksCube cube = createCube();
        cube.rotateInv();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                YELLOW, RED, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                WHITE, BLUE, ORANGE,
                GREEN, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                ORANGE, GREEN, ORANGE,
                ORANGE, YELLOW, ORANGE,
                RED, GREEN, BLUE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                YELLOW, BLUE, RED,
                BLUE, WHITE, GREEN,
                RED, RED, WHITE)));
    }

    @Test
    public void test_front() {
        RubiksCube cube = createCube();
        cube.front();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                GREEN, WHITE, YELLOW,
                YELLOW, BLUE, GREEN,
                GREEN, ORANGE, WHITE)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                RED, ORANGE, GREEN,
                ORANGE, YELLOW, WHITE)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                ORANGE, RED, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                RED, ORANGE, ORANGE,
                GREEN, YELLOW, GREEN,
                YELLOW, ORANGE, RED)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                ORANGE, YELLOW, BLUE,
                BLUE, WHITE, RED,
                YELLOW, BLUE, RED)));
    }

    @Test
    public void test_frontInv() {
        RubiksCube cube = createCube();
        cube.frontInv();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                WHITE, ORANGE, GREEN,
                GREEN, BLUE, YELLOW,
                YELLOW, WHITE, GREEN)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                WHITE, WHITE, ORANGE,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, BLUE)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                WHITE, RED, YELLOW,
                GREEN, RED, WHITE,
                RED, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                RED, ORANGE, ORANGE,
                GREEN, YELLOW, GREEN,
                BLUE, YELLOW, ORANGE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                RED, ORANGE, YELLOW,
                BLUE, WHITE, RED,
                YELLOW, BLUE, RED)));
    }

    @Test
    public void test_left() {
        RubiksCube cube = createCube();
        cube.left();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                RED, GREEN, WHITE,
                GREEN, BLUE, ORANGE,
                BLUE, YELLOW, GREEN)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                ORANGE, RED, WHITE,
                YELLOW, ORANGE, WHITE,
                YELLOW, ORANGE, RED)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                GREEN, BLUE, YELLOW,
                BLUE, GREEN, BLUE,
                WHITE, RED, RED)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                YELLOW, RED, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                BLUE, ORANGE, ORANGE,
                WHITE, YELLOW, GREEN,
                BLUE, ORANGE, ORANGE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                WHITE, WHITE, RED,
                GREEN, BLUE, RED)));
    }

    @Test
    public void test_leftInv() {
        RubiksCube cube = createCube();
        cube.leftInv();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                RED, GREEN, WHITE,
                BLUE, BLUE, ORANGE,
                YELLOW, YELLOW, GREEN)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                RED, ORANGE, YELLOW,
                WHITE, ORANGE, YELLOW,
                WHITE, RED, ORANGE)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, GREEN,
                WHITE, RED, RED)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                YELLOW, RED, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                YELLOW, ORANGE, ORANGE,
                WHITE, YELLOW, GREEN,
                GREEN, ORANGE, ORANGE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                BLUE, GREEN, WHITE,
                WHITE, WHITE, RED,
                BLUE, BLUE, RED)));
    }

    @Test
    public void test_right() {
        RubiksCube cube = createCube();
        cube.right();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                WHITE, BLUE, RED,
                GREEN, YELLOW, RED)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                ORANGE, BLUE, BLUE,
                GREEN, GREEN, WHITE,
                ORANGE, RED, BLUE)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                ORANGE, YELLOW, BLUE,
                YELLOW, RED, RED,
                GREEN, WHITE, YELLOW)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                RED, ORANGE, WHITE,
                GREEN, YELLOW, ORANGE,
                BLUE, ORANGE, GREEN)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                RED, GREEN, WHITE,
                BLUE, WHITE, BLUE,
                YELLOW, BLUE, GREEN)));
    }

    @Test
    public void test_rightInv() {
        RubiksCube cube = createCube();
        cube.rightInv();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, ORANGE,
                WHITE, BLUE, GREEN,
                GREEN, YELLOW, ORANGE)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                RED, BLUE, BLUE,
                RED, GREEN, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                YELLOW, WHITE, GREEN,
                RED, RED, YELLOW,
                BLUE, YELLOW, ORANGE)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                RED, ORANGE, WHITE,
                GREEN, YELLOW, BLUE,
                BLUE, ORANGE, GREEN)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                RED, GREEN, WHITE,
                BLUE, WHITE, ORANGE,
                YELLOW, BLUE, GREEN)));
    }

    @Test
    public void test_top() {
        RubiksCube cube = createCube();
        cube.top();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                WHITE, BLUE, ORANGE,
                GREEN, YELLOW, GREEN)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                BLUE, GREEN, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                YELLOW, RED, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                BLUE, GREEN, RED,
                ORANGE, YELLOW, ORANGE,
                ORANGE, GREEN, ORANGE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                RED, GREEN, WHITE,
                BLUE, WHITE, RED,
                YELLOW, BLUE, RED)));
    }

    @Test
    public void test_topInv() {
        RubiksCube cube = createCube();
        cube.topInv();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                WHITE, BLUE, ORANGE,
                GREEN, YELLOW, GREEN)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                BLUE, GREEN, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                YELLOW, RED, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                ORANGE, GREEN, ORANGE,
                ORANGE, YELLOW, ORANGE,
                RED, GREEN, BLUE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                RED, GREEN, WHITE,
                BLUE, WHITE, RED,
                YELLOW, BLUE, RED)));
    }

    @Test
    public void test_bottom() {
        RubiksCube cube = createCube();
        cube.bottom();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                WHITE, BLUE, ORANGE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                RED, ORANGE, ORANGE,
                WHITE, RED, BLUE)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, WHITE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                YELLOW, RED, WHITE,
                GREEN, YELLOW, GREEN)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                RED, ORANGE, ORANGE,
                GREEN, YELLOW, GREEN,
                BLUE, ORANGE, ORANGE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                YELLOW, BLUE, RED,
                BLUE, WHITE, GREEN,
                RED, RED, WHITE)));
    }

    @Test
    public void test_bottomInv() {
        RubiksCube cube = createCube();
        cube.bottomInv();

        assertTrue(cube.frontFace().equals(new RubiksCubeFace(
                YELLOW, GREEN, WHITE,
                WHITE, BLUE, ORANGE,
                ORANGE, YELLOW, GREEN)));

        assertTrue(cube.leftFace().equals(new RubiksCubeFace(
                WHITE, WHITE, RED,
                RED, ORANGE, ORANGE,
                GREEN, YELLOW, GREEN)));

        assertTrue(cube.backFace().equals(new RubiksCubeFace(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, WHITE,
                ORANGE, YELLOW, YELLOW)));

        assertTrue(cube.rightFace().equals(new RubiksCubeFace(
                BLUE, RED, YELLOW,
                YELLOW, RED, WHITE,
                WHITE, RED, BLUE)));

        assertTrue(cube.topFace().equals(new RubiksCubeFace(
                RED, ORANGE, ORANGE,
                GREEN, YELLOW, GREEN,
                BLUE, ORANGE, ORANGE)));

        assertTrue(cube.bottomFace().equals(new RubiksCubeFace(
                WHITE, RED, RED,
                GREEN, WHITE, BLUE,
                RED, BLUE, YELLOW)));
    }

    @Test
    public void test_historyIsStoredCorrectly() {
        RubiksCube cube = createCube();
        cube.front().topInv().rotate().bottom().left().rightInv().frontInv().top().bottomInv()
                .right().rotateInv().leftInv();

        assertTrue(cube.history().equals(Arrays.asList(
                FRONT, TOP_INV, ROTATE, BOTTOM, LEFT, RIGHT_INV, FRONT_INV, TOP, BOTTOM_INV, RIGHT,
                ROTATE_INV, LEFT_INV)));
    }

    private RubiksCube createCube() {
        List<RubiksColour> frontColours = Arrays.asList(
                YELLOW, GREEN, WHITE,
                WHITE, BLUE, ORANGE,
                GREEN, YELLOW, GREEN);

        List<RubiksColour> leftColours = Arrays.asList(
                WHITE, WHITE, RED,
                RED, ORANGE, ORANGE,
                ORANGE, YELLOW, YELLOW);

        List<RubiksColour> backColours = Arrays.asList(
                GREEN, BLUE, BLUE,
                BLUE, GREEN, WHITE,
                WHITE, RED, BLUE);

        List<RubiksColour> rightColours = Arrays.asList(
                BLUE, RED, YELLOW,
                YELLOW, RED, WHITE,
                ORANGE, YELLOW, GREEN);

        List<RubiksColour> topColours = Arrays.asList(
                RED, ORANGE, ORANGE,
                GREEN, YELLOW, GREEN,
                BLUE, ORANGE, ORANGE);

        List<RubiksColour> bottomColours = Arrays.asList(
                RED, GREEN, WHITE,
                BLUE, WHITE, RED,
                YELLOW, BLUE, RED);

        return new RubiksCube(
                frontColours, leftColours, backColours,
                rightColours, topColours, bottomColours);
    }
}
