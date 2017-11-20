package com.bjerkan.rubikscubeapp.cubegraphic;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

class CubeSurfaceView extends GLSurfaceView {

    private final CubeRenderer cubeRenderer;

    public CubeSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(1);

        cubeRenderer = new CubeRenderer();
        setRenderer(cubeRenderer);
    }

    // Needed to use from layout file
    public CubeSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(1);

        cubeRenderer = new CubeRenderer();
        setRenderer(cubeRenderer);
    }

    public void setModel(RubiksCubeModel model) {
        cubeRenderer.setModel(model);
    }
}
