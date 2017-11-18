package com.bjerkan.rubikscubeapp;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {
    public Square(float[] topLeftVertex, float[] topRightVertex,
            float[] bottomRightVertex, float[] bottomLeftVertex, float[] colour) {
        this.topLeftVertex = topLeftVertex;
        this.topRightVertex = topRightVertex;
        this.bottomRightVertex = bottomRightVertex;
        this.bottomLeftVertex = bottomLeftVertex;

        float[] vertexArray = {
                topLeftVertex[0], topLeftVertex[1], topLeftVertex[2],
                topRightVertex[0], topRightVertex[1], topRightVertex[2],
                bottomRightVertex[0], bottomRightVertex[1], bottomRightVertex[2],
                bottomLeftVertex[0], bottomLeftVertex[1], bottomLeftVertex[2]
        };

        float[] colourArray = {
                colour[0], colour[1], colour[2], 1f,
                colour[0], colour[1], colour[2], 1f,
                colour[0], colour[1], colour[2], 1f,
                colour[0], colour[1], colour[2], 1f
        };

        byte[] indexArray = {
                0, 1, 3,
                1, 2, 3
        };

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertexArray.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(colourArray.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuf.asFloatBuffer();
        mColorBuffer.put(colourArray);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indexArray.length);
        mIndexBuffer.put(indexArray);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glPushMatrix();

        gl.glRotatef(commitedXRotations * 90f, 1f, 0f, 0f);
        gl.glRotatef(commitedYRotations * 90f, 0f, 1f, 0f);
        gl.glRotatef(commitedZRotations * 90f, 0f, 0f, 1f);

        if (tempRotation) {
            switch (tempRotationAxis) {
                case X: {
                    if (tempRotationDirection == RubiksCubeModel.Direction.ANTICLOCKWISE) {
                        gl.glRotatef(tempRotationAngle, 1f, 0f, 0f);
                    } else {
                        gl.glRotatef(-tempRotationAngle, 1f, 0f, 0f);
                    }
                }

                case Y: {
                    if (tempRotationDirection == RubiksCubeModel.Direction.ANTICLOCKWISE) {
                        gl.glRotatef(tempRotationAngle, 0f, 1f, 0f);
                    } else {
                        gl.glRotatef(-tempRotationAngle, 0f, 1f, 0f);
                    }
                }

                case Z: {
                    if (tempRotationDirection == RubiksCubeModel.Direction.ANTICLOCKWISE) {
                        gl.glRotatef(tempRotationAngle, 0f, 0f, 1f);
                    } else {
                        gl.glRotatef(-tempRotationAngle, 0f, 0f, 1f);
                    }
                }
            }
        }

        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    public void commitRotation(RubiksCubeModel.Axis axis, RubiksCubeModel.Direction direction) {
        switch (axis) {
            case X: {
                if (direction == RubiksCubeModel.Direction.ANTICLOCKWISE) {
                    commitedXRotations += 1;
                } else {
                    commitedXRotations -= 1;
                }
            }

            case Y: {
                if (direction == RubiksCubeModel.Direction.ANTICLOCKWISE) {
                    commitedYRotations += 1;
                } else {
                    commitedYRotations -= 1;
                }
            }

            case Z: {
                if (direction == RubiksCubeModel.Direction.ANTICLOCKWISE) {
                    commitedZRotations += 1;
                } else {
                    commitedZRotations -= 1;
                }
            }
        }

        tempRotation = false;
    }

    public void tempRotation(RubiksCubeModel.Axis axis, RubiksCubeModel.Direction direction,
                             float angle) {
        tempRotation = true;
        tempRotationAngle = angle;
        tempRotationAxis = axis;
        tempRotationDirection = direction;
    }

    float[] topLeftVertex;
    float[] topRightVertex;
    float[] bottomRightVertex;
    float[] bottomLeftVertex;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;

    int commitedXRotations = 0;
    int commitedYRotations = 0;
    int commitedZRotations = 0;

    boolean tempRotation = false;
    float tempRotationAngle;
    RubiksCubeModel.Axis tempRotationAxis;
    RubiksCubeModel.Direction tempRotationDirection;
}
