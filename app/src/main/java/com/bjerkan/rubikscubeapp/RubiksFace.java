package com.bjerkan.rubikscubeapp;

import android.util.Log;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class RubiksFace {
    public RubiksFace(List<CubeScanner.RubiksColour> squareColours,
                      float[] topLeftCorner, float[] topRightCorner,
                      float[] bottomRightCorner, float[] bottomLeftCorner) {
        float[] topEdge = {
                topRightCorner[0] - topLeftCorner[0],
                topRightCorner[1] - topLeftCorner[1],
                topRightCorner[2] - topLeftCorner[2]
        };

        float[] leftEdge = {
                bottomLeftCorner[0] - topLeftCorner[0],
                bottomLeftCorner[1] - topLeftCorner[1],
                bottomLeftCorner[2] - topLeftCorner[2]
        };

        float[][][] squareVertices = new float[4][4][];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                squareVertices[x][y] = new float[3];

                squareVertices[x][y][0] = topLeftCorner[0]
                        + (x / 3f * topEdge[0]) + (y / 3f * leftEdge[0]);

                squareVertices[x][y][1] = topLeftCorner[1]
                        + (x / 3f * topEdge[1]) + (y / 3f * leftEdge[1]);

                squareVertices[x][y][2] = topLeftCorner[2]
                        + (x / 3f * topEdge[2]) + (y / 3f * leftEdge[2]);

                Log.d("RubiksFace", x + " " + y);
                Log.d("RubiksFace", Float.toString(squareVertices[x][y][0]));
                Log.d("RubiksFace", Float.toString(squareVertices[x][y][1]));
                Log.d("RubiksFace", Float.toString(squareVertices[x][y][2]));
            }
        }

        for (int squareX = 0; squareX < 3; squareX++) {
            for (int squareY = 0; squareY < 3; squareY++) {
                squares[squareX][squareY] = new Square(
                        squareVertices[squareX][squareY],
                        squareVertices[squareX + 1][squareY],
                        squareVertices[squareX + 1][squareY + 1],
                        squareVertices[squareX][squareY + 1],
                        rubiksColourToFloatArray(squareColours.get(3 * squareY + squareX))
                );
            }
        }
    }

    public void draw(GL10 gl) {
        for (Square[] squareColumn : squares) {
            for (Square square : squareColumn) {
                square.draw(gl);
            }
        }
    }

    private float[] rubiksColourToFloatArray(CubeScanner.RubiksColour colour) {
        return new float[] {
                (float) colour.rgb.val[0] / 255f,
                (float) colour.rgb.val[1] / 255f,
                (float) colour.rgb.val[2] / 255f
        };
    }

    private Square[][] squares = new Square[3][3];
}
