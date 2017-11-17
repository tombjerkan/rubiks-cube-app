package com.bjerkan.rubikscubeapp;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class CubeGraphicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glView = new CubeSurfaceView(this);
        setContentView(glView);
    }

    private class CubeSurfaceView extends GLSurfaceView {

        private final CubeRenderer cubeRenderer;

        public CubeSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(1);

            cubeRenderer = new CubeRenderer();
            setRenderer(cubeRenderer);
        }
    }

    private GLSurfaceView glView;
}
