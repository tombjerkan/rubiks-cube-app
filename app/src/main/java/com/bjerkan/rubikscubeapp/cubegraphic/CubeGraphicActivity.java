package com.bjerkan.rubikscubeapp.cubegraphic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjerkan.rubikscubeapp.CubeScanner;
import com.bjerkan.rubikscubeapp.R;

import java.util.List;
import java.util.stream.Collectors;

public class CubeGraphicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_graphic);

        rubiksCubeModel = new RubiksCubeModel(
                asColourList(getIntent().getStringArrayListExtra(FRONT_COLOURS_ARGUMENT)),
                asColourList(getIntent().getStringArrayListExtra(LEFT_COLOURS_ARGUMENT)),
                asColourList(getIntent().getStringArrayListExtra(BACK_COLOURS_ARGUMENT)),
                asColourList(getIntent().getStringArrayListExtra(RIGHT_COLOURS_ARGUMENT)),
                asColourList(getIntent().getStringArrayListExtra(TOP_COLOURS_ARGUMENT)),
                asColourList(getIntent().getStringArrayListExtra(BOTTOM_COLOURS_ARGUMENT))
        );

        CubeSurfaceView cubeSurfaceView = findViewById(R.id.cubeSurfaceView);
        cubeSurfaceView.setModel(rubiksCubeModel);
    }

    private List<CubeScanner.RubiksColour> asColourList(List<String> colourNames) {
        return colourNames.stream()
                .map(CubeScanner.RubiksColour::valueOf)
                .collect(Collectors.toList());
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

    public static final String FRONT_COLOURS_ARGUMENT = "com.bjerkan.FRONT_COLOURS_ARGUMENT";
    public static final String LEFT_COLOURS_ARGUMENT = "com.bjerkan.LEFT_COLOURS_ARGUMENT";
    public static final String BACK_COLOURS_ARGUMENT = "com.bjerkan.BACK_COLOURS_ARGUMENT";
    public static final String RIGHT_COLOURS_ARGUMENT = "com.bjerkan.RIGHT_COLOURS_ARGUMENT";
    public static final String TOP_COLOURS_ARGUMENT = "com.bjerkan.TOP_COLOURS_ARGUMENT";
    public static final String BOTTOM_COLOURS_ARGUMENT = "com.bjerkan.BOTTOM_COLOURS_ARGUMENT";

    private RubiksCubeModel rubiksCubeModel;
}
