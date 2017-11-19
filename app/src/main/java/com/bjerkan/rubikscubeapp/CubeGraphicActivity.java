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

    public void front(View view) {
        rubiksCubeModel.front();
    }

    public void frontInv(View view) {
        rubiksCubeModel.frontInv();
    }

    public void left(View view) {
        rubiksCubeModel.left();
    }

    public void leftInv(View view) {
        rubiksCubeModel.leftInv();
    }

    public void right(View view) {
        rubiksCubeModel.right();
    }

    public void rightInv(View view) {
        rubiksCubeModel.rightInv();
    }

    public void top(View view) {
        rubiksCubeModel.top();
    }

    public void topInv(View view) {
        rubiksCubeModel.topInv();
    }

    public void bottom(View view) {
        rubiksCubeModel.bottom();
    }

    public void bottomInv(View view) {
        rubiksCubeModel.bottomInv();
    }

    public void rotate(View view) {
        rubiksCubeModel.rotate();
    }

    public void rotateInv(View view) {
        rubiksCubeModel.rotateInv();
    }

    private RubiksCubeModel rubiksCubeModel;
}
