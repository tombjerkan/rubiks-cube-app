package com.bjerkan.rubikscubeapp;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

public class CubeGraphicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_graphic);

        cubeSurfaceView = findViewById(R.id.cubeSurfaceView);
    }

    public void turn(View view) {
        cubeSurfaceView.model().front_inv();
    }

    private CubeSurfaceView cubeSurfaceView;
}
