package com.bjerkan.rubikscubeapp;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import java.util.Collections;

public class CubeGraphicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_graphic);

        rubiksCubeModel = new RubiksCubeModel(
                Collections.nCopies(9, CubeScanner.RubiksColour.WHITE),
                Collections.nCopies(9, CubeScanner.RubiksColour.RED),
                Collections.nCopies(9, CubeScanner.RubiksColour.YELLOW),
                Collections.nCopies(9, CubeScanner.RubiksColour.ORANGE),
                Collections.nCopies(9, CubeScanner.RubiksColour.GREEN),
                Collections.nCopies(9, CubeScanner.RubiksColour.BLUE)
        );

        CubeSurfaceView cubeSurfaceView = findViewById(R.id.cubeSurfaceView);
        cubeSurfaceView.setModel(rubiksCubeModel);
    }

    private RubiksCubeModel rubiksCubeModel;
}
