package com.bjerkan.rubikscubeapp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {
    public Square(float[] topLeftVertex, float[] topRightVertex,
            float[] bottomRightVertex, float[] bottomLeftVertex, float[] colour) {
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

        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
}
