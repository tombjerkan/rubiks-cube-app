package com.bjerkan.rubikscubeapp.cubegraphic;

import com.bjerkan.rubikscubeapp.CubeScanner;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

class Square {
    Square(float[] topLeftVertex, float[] topRightVertex,
                  float[] bottomRightVertex, float[] bottomLeftVertex) {
        float[] vertexArray = {
                topLeftVertex[0], topLeftVertex[1], topLeftVertex[2],
                topRightVertex[0], topRightVertex[1], topRightVertex[2],
                bottomRightVertex[0], bottomRightVertex[1], bottomRightVertex[2],
                bottomLeftVertex[0], bottomLeftVertex[1], bottomLeftVertex[2]
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

        mIndexBuffer = ByteBuffer.allocateDirect(indexArray.length);
        mIndexBuffer.put(indexArray);
        mIndexBuffer.position(0);

        colour[0] = 0f;
        colour[1] = 0f;
        colour[2] = 0f;
    }

    void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColor4f(colour[0], colour[1], colour[2], 1f);

        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    void setColour(CubeScanner.RubiksColour colour) {
        this.colour[0] = (float) colour.rgb.val[0] / 255f;
        this.colour[1] = (float) colour.rgb.val[1] / 255f;
        this.colour[2] = (float) colour.rgb.val[2] / 255f;
    }

    private FloatBuffer mVertexBuffer;
    private ByteBuffer mIndexBuffer;

    private float[] colour = new float[3];
}
