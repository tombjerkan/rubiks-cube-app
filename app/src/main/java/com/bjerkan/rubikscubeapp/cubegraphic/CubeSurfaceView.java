package com.bjerkan.rubikscubeapp.cubegraphic;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * A view to which a Rubik's Cube graphic can be drawn.
 */
class CubeSurfaceView extends GLSurfaceView {

    private final CubeRenderer cubeRenderer;

    /**
     * Standard view constructor. Displays nothing initially until a model is set using setModel.
     *
     * @param context the context to run the view in
     */
    public CubeSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(1);

        cubeRenderer = new CubeRenderer();
        setRenderer(cubeRenderer);
    }

    /**
     * Standard view constructor. Displays nothing initially until a model is set using setModel.
     *
     * @param context the context to run the view in
     * @param attrs the XML attributes given to the view
     */
    public CubeSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(1);

        cubeRenderer = new CubeRenderer();
        setRenderer(cubeRenderer);
    }

    /**
     * Sets the Rubik's Cube model to draw to the view.
     *
     * @param model the model to draw
     */
    public void setModel(RubiksCubeModel model) {
        cubeRenderer.setModel(model);
    }
}
