package com.bjerkan.rubikscubeapp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

class Cube {
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;

    public Cube(float centreX, float centreY, float centreZ, float sideLength,
                float[] frontColour, float[] leftColour, float[] backColour, float[] rightColour,
                float[] topColour, float[] bottomColour) {
        float offset = sideLength / 2f;
        float vertices[] = {
                centreX - offset, centreY - offset, centreZ - offset,
                centreX - offset, centreY - offset, centreZ + offset,
                centreX + offset, centreY - offset, centreZ + offset,
                centreX + offset, centreY - offset, centreZ - offset,

                centreX + offset, centreY - offset, centreZ - offset,
                centreX + offset, centreY - offset, centreZ + offset,
                centreX + offset, centreY + offset, centreZ + offset,
                centreX + offset, centreY + offset, centreZ - offset,

                centreX + offset, centreY + offset, centreZ - offset,
                centreX + offset, centreY + offset, centreZ + offset,
                centreX - offset, centreY + offset, centreZ + offset,
                centreX - offset, centreY + offset, centreZ - offset,

                centreX - offset, centreY + offset, centreZ - offset,
                centreX - offset, centreY + offset, centreZ + offset,
                centreX - offset, centreY - offset, centreZ + offset,
                centreX - offset, centreY - offset, centreZ - offset,

                centreX - offset, centreY - offset, centreZ + offset,
                centreX - offset, centreY + offset, centreZ + offset,
                centreX + offset, centreY + offset, centreZ + offset,
                centreX + offset, centreY - offset, centreZ + offset,

                centreX - offset, centreY + offset, centreZ - offset,
                centreX - offset, centreY - offset, centreZ - offset,
                centreX + offset, centreY - offset, centreZ - offset,
                centreX + offset, centreY + offset, centreZ - offset,
        };

        byte[] indices = {
                0, 1, 2, 0, 2, 3,
                4, 5, 6, 4, 6, 7,
                8, 9, 10, 8, 10, 11,
                12, 13, 14, 12, 14, 15,
                16, 17, 18, 16, 18, 19,
                20, 21, 22, 20, 22, 23
        };

        float[] black = {0f, 0f, 0f};
        if (frontColour == null) {
            frontColour = black;
        }
        if (leftColour == null) {
            leftColour = black;
        }
        if (backColour == null) {
            backColour = black;
        }
        if (rightColour == null) {
            rightColour = black;
        }
        if (topColour == null) {
            topColour = black;
        }
        if (bottomColour == null) {
            bottomColour = black;
        }

        float coloursArray[] = {
                bottomColour[0], bottomColour[1], bottomColour[2], 1f,
                bottomColour[0], bottomColour[1], bottomColour[2], 1f,
                bottomColour[0], bottomColour[1], bottomColour[2], 1f,
                bottomColour[0], bottomColour[1], bottomColour[2], 1f,

                rightColour[0], rightColour[1], rightColour[2], 1f,
                rightColour[0], rightColour[1], rightColour[2], 1f,
                rightColour[0], rightColour[1], rightColour[2], 1f,
                rightColour[0], rightColour[1], rightColour[2], 1f,

                topColour[0], topColour[1], topColour[2], 1f,
                topColour[0], topColour[1], topColour[2], 1f,
                topColour[0], topColour[1], topColour[2], 1f,
                topColour[0], topColour[1], topColour[2], 1f,

                leftColour[0], leftColour[1], leftColour[2], 1f,
                leftColour[0], leftColour[1], leftColour[2], 1f,
                leftColour[0], leftColour[1], leftColour[2], 1f,
                leftColour[0], leftColour[1], leftColour[2], 1f,

                frontColour[0], frontColour[1], frontColour[2], 1f,
                frontColour[0], frontColour[1], frontColour[2], 1f,
                frontColour[0], frontColour[1], frontColour[2], 1f,
                frontColour[0], frontColour[1], frontColour[2], 1f,

                backColour[0], backColour[1], backColour[2], 1f,
                backColour[0], backColour[1], backColour[2], 1f,
                backColour[0], backColour[1], backColour[2], 1f,
                backColour[0], backColour[1], backColour[2], 1f
        };

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(coloursArray.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuf.asFloatBuffer();
        mColorBuffer.put(coloursArray);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE,
                mIndexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}