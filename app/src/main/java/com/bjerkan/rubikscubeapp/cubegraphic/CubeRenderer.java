package com.bjerkan.rubikscubeapp.cubegraphic;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * An OpenGL renderer to handle drawing graphical objects to a CubeSurfaceView.
 *
 * The public methods onSurfaceCreated, onDrawFrame and onSurfaceChanged are for use by the Android
 * SurfaceView and should not be called directly.
 */
class CubeRenderer implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
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

    /**
     * Sets the model to be drawn to the view.
     *
     * @param model the model that the renderer will draw
     */
    void setModel(RubiksCubeModel model) {
        this.model = model;
    }

    private RubiksCubeModel model;
}