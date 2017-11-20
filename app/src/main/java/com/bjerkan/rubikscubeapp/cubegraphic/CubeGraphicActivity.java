package com.bjerkan.rubikscubeapp.cubegraphic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjerkan.rubikscubeapp.CubeScanner;
import com.bjerkan.rubikscubeapp.R;
import com.bjerkan.rubikscubeapp.rubikscube.RubiksCube;
import com.bjerkan.rubikscubeapp.rubikscube.Solver;

import java.util.List;
import java.util.stream.Collectors;

import static com.bjerkan.rubikscubeapp.cubegraphic.RubiksCubeModel.AnimationFinishedListener;

public class CubeGraphicActivity extends Activity implements AnimationFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_graphic);

        List<CubeScanner.RubiksColour> frontColours = asColourList(
                getIntent().getStringArrayListExtra(FRONT_COLOURS_ARGUMENT));
        List<CubeScanner.RubiksColour> leftColours = asColourList(
                getIntent().getStringArrayListExtra(LEFT_COLOURS_ARGUMENT));
        List<CubeScanner.RubiksColour> backColours = asColourList(
                getIntent().getStringArrayListExtra(BACK_COLOURS_ARGUMENT));
        List<CubeScanner.RubiksColour> rightColours = asColourList(
                getIntent().getStringArrayListExtra(RIGHT_COLOURS_ARGUMENT));
        List<CubeScanner.RubiksColour> topColours = asColourList(
                getIntent().getStringArrayListExtra(TOP_COLOURS_ARGUMENT));
        List<CubeScanner.RubiksColour> bottomColours = asColourList(
                getIntent().getStringArrayListExtra(BOTTOM_COLOURS_ARGUMENT));

        rubiksCubeModel = new RubiksCubeModel(frontColours, leftColours, backColours, rightColours,
                topColours, bottomColours);
        rubiksCubeModel.setAnimationFinishedListener(this);

        RubiksCube cube = new RubiksCube(frontColours, leftColours, backColours, rightColours,
                topColours, bottomColours);

        solvingActions = Solver.solve(cube);
        nextActionIndex = 0;
        // Make animations begin as logic to start new animation is in method for animation ending
        animationFinished();

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

    @Override
    public void animationFinished() {
        if (nextActionIndex >= solvingActions.size()) {
            return;
        }

        RubiksCube.Action nextAction = solvingActions.get(nextActionIndex);
        nextActionIndex++;

        switch (nextAction) {
            case FRONT:
                rubiksCubeModel.front();
                break;
            case FRONT_INV:
                rubiksCubeModel.frontInv();
                break;
            case LEFT:
                rubiksCubeModel.left();
                break;
            case LEFT_INV:
                rubiksCubeModel.leftInv();
                break;
            case RIGHT:
                rubiksCubeModel.right();
                break;
            case RIGHT_INV:
                rubiksCubeModel.rightInv();
                break;
            case TOP:
                rubiksCubeModel.top();
                break;
            case TOP_INV:
                rubiksCubeModel.topInv();
                break;
            case BOTTOM:
                rubiksCubeModel.bottom();
                break;
            case BOTTOM_INV:
                rubiksCubeModel.bottomInv();
                break;
            case ROTATE:
                rubiksCubeModel.rotate();
                break;
            case ROTATE_INV:
                rubiksCubeModel.rotateInv();
                break;
        }
    }

    public static final String FRONT_COLOURS_ARGUMENT = "com.bjerkan.FRONT_COLOURS_ARGUMENT";
    public static final String LEFT_COLOURS_ARGUMENT = "com.bjerkan.LEFT_COLOURS_ARGUMENT";
    public static final String BACK_COLOURS_ARGUMENT = "com.bjerkan.BACK_COLOURS_ARGUMENT";
    public static final String RIGHT_COLOURS_ARGUMENT = "com.bjerkan.RIGHT_COLOURS_ARGUMENT";
    public static final String TOP_COLOURS_ARGUMENT = "com.bjerkan.TOP_COLOURS_ARGUMENT";
    public static final String BOTTOM_COLOURS_ARGUMENT = "com.bjerkan.BOTTOM_COLOURS_ARGUMENT";

    private RubiksCubeModel rubiksCubeModel;

    private List<RubiksCube.Action> solvingActions;
    private int nextActionIndex;
}
