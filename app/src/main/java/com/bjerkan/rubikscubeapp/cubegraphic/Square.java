package com.bjerkan.rubikscubeapp.cubegraphic;

import com.bjerkan.rubikscubeapp.rubikscube.Colour;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A class for a graphical square.
 */
class Square {
    /**
     * Creates a square with the given vertices.
     *
     * @param topLeftVertex the position of the top-left vertex
     * @param topRightVertex the position of the top-right vertex
     * @param bottomRightVertex the position of the bottom-right vertex
     * @param bottomLeftVertex the position of the bottom-left vertex
     */
    Square(Vertex topLeftVertex, Vertex topRightVertex,
           Vertex bottomRightVertex, Vertex bottomLeftVertex) {
        float[] vertexArray = {
                topLeftVertex.x(), topLeftVertex.y(), topLeftVertex.z(),
                topRightVertex.x(), topRightVertex.y(), topRightVertex.z(),
                bottomRightVertex.x(), bottomRightVertex.y(), bottomRightVertex.z(),
                bottomLeftVertex.x(), bottomLeftVertex.y(), bottomLeftVertex.z()
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

    /**
     * Renders the square to the given OpenGL context.
     *
     * @param gl the OpenGL context to render to
     */
    void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColor4f(colour[0], colour[1], colour[2], 1f);

        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    /**
     * Sets the colour of the square.
     *
     * @param colour a Rubik's Cube colour to use for the square
     */
    void setColour(Colour colour) {
        this.colour[0] = (float) colour.rgb.val[0] / 255f;
        this.colour[1] = (float) colour.rgb.val[1] / 255f;
        this.colour[2] = (float) colour.rgb.val[2] / 255f;
    }

    private FloatBuffer mVertexBuffer;
    private ByteBuffer mIndexBuffer;

    private float[] colour = new float[3];
}
