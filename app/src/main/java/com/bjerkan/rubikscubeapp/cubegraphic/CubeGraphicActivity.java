package com.bjerkan.rubikscubeapp.cubegraphic;

import android.app.Activity;
import android.os.Bundle;

import com.bjerkan.rubikscubeapp.R;
import com.bjerkan.rubikscubeapp.rubikscube.Colour;
import com.bjerkan.rubikscubeapp.rubikscube.RubiksCube;
import com.bjerkan.rubikscubeapp.rubikscube.Solver;

import java.util.List;
import java.util.stream.Collectors;

import static com.bjerkan.rubikscubeapp.cubegraphic.RubiksCubeModel.AnimationFinishedListener;

/**
 * An activity for showing a Rubik's Cube graphic and animating the moves needed to solve it. Must
 * be started using an intent with the *_COLOURS_ARGUMENT arguments to give the starting square
 * colours for the cube. These colours must represent a valid Rubik's Cube.
 */
public class CubeGraphicActivity extends Activity implements AnimationFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_graphic);

        List<Colour> frontColours = asColourList(
                getIntent().getStringArrayListExtra(FRONT_COLOURS_ARGUMENT));
        List<Colour> leftColours = asColourList(
                getIntent().getStringArrayListExtra(LEFT_COLOURS_ARGUMENT));
        List<Colour> backColours = asColourList(
                getIntent().getStringArrayListExtra(BACK_COLOURS_ARGUMENT));
        List<Colour> rightColours = asColourList(
                getIntent().getStringArrayListExtra(RIGHT_COLOURS_ARGUMENT));
        List<Colour> topColours = asColourList(
                getIntent().getStringArrayListExtra(TOP_COLOURS_ARGUMENT));
        List<Colour> bottomColours = asColourList(
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

    private List<Colour> asColourList(List<String> colourNames) {
        return colourNames.stream()
                .map(Colour::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Called when an animation has finished and the next animation should be initiated.
     */
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

    /**
     * The name of the intent argument with the colours of the front face. The argument should be
     * an ArrayList of the String names for the enum colours.
     */
    public static final String FRONT_COLOURS_ARGUMENT = "com.bjerkan.FRONT_COLOURS_ARGUMENT";

    /**
     * The name of the intent argument with the colours of the left face. The argument should be
     * an ArrayList of the String names for the enum colours.
     */
    public static final String LEFT_COLOURS_ARGUMENT = "com.bjerkan.LEFT_COLOURS_ARGUMENT";

    /**
     * The name of the intent argument with the colours of the back face. The argument should be
     * an ArrayList of the String names for the enum colours.
     */
    public static final String BACK_COLOURS_ARGUMENT = "com.bjerkan.BACK_COLOURS_ARGUMENT";

    /**
     * The name of the intent argument with the colours of the right face. The argument should be
     * an ArrayList of the String names for the enum colours.
     */
    public static final String RIGHT_COLOURS_ARGUMENT = "com.bjerkan.RIGHT_COLOURS_ARGUMENT";

    /**
     * The name of the intent argument with the colours of the top face. The argument should be
     * an ArrayList of the String names for the enum colours.
     */
    public static final String TOP_COLOURS_ARGUMENT = "com.bjerkan.TOP_COLOURS_ARGUMENT";

    /**
     * The name of the intent argument with the colours of the bottom face. The argument should be
     * an ArrayList of the String names for the enum colours.
     */
    public static final String BOTTOM_COLOURS_ARGUMENT = "com.bjerkan.BOTTOM_COLOURS_ARGUMENT";

    private RubiksCubeModel rubiksCubeModel;

    private List<RubiksCube.Action> solvingActions;
    private int nextActionIndex;
}
