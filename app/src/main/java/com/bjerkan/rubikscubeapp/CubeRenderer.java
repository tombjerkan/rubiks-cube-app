package com.bjerkan.rubikscubeapp;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import java.util.Collections;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        model = new RubiksCubeModel(
                Collections.nCopies(9, CubeScanner.RubiksColour.WHITE),
                Collections.nCopies(9, CubeScanner.RubiksColour.RED),
                Collections.nCopies(9, CubeScanner.RubiksColour.YELLOW),
                Collections.nCopies(9, CubeScanner.RubiksColour.ORANGE),
                Collections.nCopies(9, CubeScanner.RubiksColour.GREEN),
                Collections.nCopies(9, CubeScanner.RubiksColour.BLUE)
        );
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        model.draw(gl);

        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height,
                0.1f, 100.0f);

        GLU.gluLookAt(gl, -7.5f, 10f, 15f,
                0f, 0f, 0f,
                0f, 1f, 0f);

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private RubiksCubeModel model;
}