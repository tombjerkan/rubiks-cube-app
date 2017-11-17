package com.bjerkan.rubikscubeapp;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        float centreX = 0f;
        float centreY = 0f;
        float centreZ = 0f;

        float sideLength = 2f;

        float[] frontColour = {1f, 1f, 1f};
        float[] leftColour = {0f, 1f, 0f};
        float[] backColour = {1f, 1f, 0f};
        float[] rightColour = {0f, 0f, 1f};
        float[] topColour = {1f, 0.66f, 0f};
        float[] bottomColour = {1f, 0f, 0f};

        cube = new Cube(centreX, centreY, centreZ , sideLength,
                        frontColour, leftColour, backColour, rightColour, topColour, bottomColour);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        cube.draw(gl);

        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height,
                0.1f, 100.0f);

        GLU.gluLookAt(gl, 0f, 0f, 10f,
                0f, 0f, 0f,
                0f, 1f, 0f);

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private Cube cube;
}
