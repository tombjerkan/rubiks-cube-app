package com.bjerkan.rubikscubeapp.rubikscube;

import com.bjerkan.rubikscubeapp.CubeScanner;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.YELLOW;
import static org.junit.Assert.*;

import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.BLUE;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.GREEN;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.ORANGE;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.RED;
import static com.bjerkan.rubikscubeapp.CubeScanner.RubiksColour.WHITE;

public class RubiksCubeFaceTest {
    @Test
    public void test_copyConstructor() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);
    }
    @Test
    public void test_rotated() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        RubiksCubeFace rotatedFace = face.rotated();

        assertTrue(rotatedFace.equals(new RubiksCubeFace(
                WHITE, RED, WHITE,
                BLUE, ORANGE, GREEN,
                GREEN, BLUE, BLUE)));
    }

    @Test
    public void test_rotatedInv() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        RubiksCubeFace rotatedFace = face.rotatedInv();

        assertTrue(rotatedFace.equals(new RubiksCubeFace(
                BLUE, BLUE, GREEN,
                GREEN, ORANGE, BLUE,
                WHITE, RED, WHITE)));
    }

    @Test
    public void test_replaceTopRow() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        RubiksCubeFace replacedTop = face.replaceTopRow(YELLOW, RED, GREEN);

        assertTrue(replacedTop.equals(new RubiksCubeFace(
                YELLOW, RED, GREEN,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN)));
    }

    @Test
    public void test_replaceBottomRow() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN
        );

        RubiksCubeFace replacedBottom = face.replaceBottomRow(YELLOW, ORANGE, BLUE);

        assertTrue(replacedBottom.equals(new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                YELLOW, ORANGE, BLUE)));
    }

    @Test
    public void test_replaceLeftColumn() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        RubiksCubeFace replacedLeft = face.replaceLeftColumn(RED, YELLOW, ORANGE);

        assertTrue(replacedLeft.equals(new RubiksCubeFace(
                RED, GREEN, BLUE,
                YELLOW, ORANGE, BLUE,
                ORANGE, BLUE, GREEN)));
    }

    @Test
    public void test_replaceRightColumn() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        RubiksCubeFace replacedRight = face.replaceRightColumn(RED, BLUE, ORANGE);

        assertTrue(replacedRight.equals(new RubiksCubeFace(
                WHITE, GREEN, RED,
                RED, ORANGE, BLUE,
                WHITE, BLUE, ORANGE)));
    }

    @Test
    public void test_topRow() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        List<CubeScanner.RubiksColour> topRow = face.topRow();

        assertTrue(topRow.equals(Arrays.asList(WHITE, GREEN, BLUE)));
    }

    @Test
    public void test_bottomRow() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        List<CubeScanner.RubiksColour> bottomRow = face.bottomRow();

        assertTrue(bottomRow.equals(Arrays.asList(WHITE, BLUE, GREEN)));
    }

    @Test
    public void test_leftColumn() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        List<CubeScanner.RubiksColour> leftColumn = face.leftColumn();

        assertTrue(leftColumn.equals(Arrays.asList(WHITE, RED, WHITE)));
    }

    @Test
    public void test_rightColumn() {
        RubiksCubeFace face = new RubiksCubeFace(
                WHITE, GREEN, BLUE,
                RED, ORANGE, BLUE,
                WHITE, BLUE, GREEN);

        List<CubeScanner.RubiksColour> rightColumn = face.rightColumn();

        assertTrue(rightColumn.equals(Arrays.asList(BLUE, BLUE, GREEN)));
    }
}