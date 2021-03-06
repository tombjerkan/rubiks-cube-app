package com.bjerkan.rubikscubeapp.cubegraphic;

import android.app.Activity;
import android.os.Bundle;

import com.bjerkan.rubikscubeapp.R;
import com.bjerkan.rubikscubeapp.rubikscube.RubiksCube;
import com.bjerkan.rubikscubeapp.rubikscube.RubiksCubeFace;
import com.bjerkan.rubikscubeapp.rubikscube.Solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

        RubiksCubeFace frontFace = (RubiksCubeFace) getIntent().getSerializableExtra(
                FRONT_COLOURS_ARGUMENT);
        RubiksCubeFace leftFace = (RubiksCubeFace) getIntent().getSerializableExtra(
                LEFT_COLOURS_ARGUMENT);
        RubiksCubeFace backFace = (RubiksCubeFace) getIntent().getSerializableExtra(
                BACK_COLOURS_ARGUMENT);
        RubiksCubeFace rightFace = (RubiksCubeFace) getIntent().getSerializableExtra(
                RIGHT_COLOURS_ARGUMENT);
        RubiksCubeFace topFace = (RubiksCubeFace) getIntent().getSerializableExtra(
                TOP_COLOURS_ARGUMENT);
        RubiksCubeFace bottomFace = (RubiksCubeFace) getIntent().getSerializableExtra(
                BOTTOM_COLOURS_ARGUMENT);

        rubiksCubeModel = new RubiksCubeModel(
                frontFace, leftFace, backFace, rightFace, topFace, bottomFace);
        rubiksCubeModel.setAnimationFinishedListener(this);

        RubiksCube cube = new RubiksCube(
                frontFace, leftFace, backFace, rightFace, topFace, bottomFace);

        List<RubiksCube.Action> solvingActions = Solver.solve(cube);
        prepareCubeModelActions(solvingActions);
        // Make animations begin as logic to start new animation is in method for animation ending
        animationFinished();

        CubeSurfaceView cubeSurfaceView = findViewById(R.id.cubeSurfaceView);
        cubeSurfaceView.setModel(rubiksCubeModel);
    }

    /**
     * Called when an animation has finished and the next animation should be initiated.
     */
    @Override
    public void animationFinished() {
        if (!modelSolvingActions.isEmpty()) {
            modelSolvingActions.remove().run();
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

    private void prepareCubeModelActions(List<RubiksCube.Action> cubeActions) {
        Map<RubiksCube.Action, Runnable> actionToModelMethod = new HashMap<>();
        actionToModelMethod.put(RubiksCube.Action.FRONT, rubiksCubeModel::front);
        actionToModelMethod.put(RubiksCube.Action.FRONT_INV, rubiksCubeModel::frontInv);
        actionToModelMethod.put(RubiksCube.Action.LEFT, rubiksCubeModel::left);
        actionToModelMethod.put(RubiksCube.Action.LEFT_INV, rubiksCubeModel::leftInv);
        actionToModelMethod.put(RubiksCube.Action.RIGHT, rubiksCubeModel::right);
        actionToModelMethod.put(RubiksCube.Action.RIGHT_INV, rubiksCubeModel::rightInv);
        actionToModelMethod.put(RubiksCube.Action.TOP, rubiksCubeModel::top);
        actionToModelMethod.put(RubiksCube.Action.TOP_INV, rubiksCubeModel::topInv);
        actionToModelMethod.put(RubiksCube.Action.BOTTOM, rubiksCubeModel::bottom);
        actionToModelMethod.put(RubiksCube.Action.BOTTOM_INV, rubiksCubeModel::bottomInv);
        actionToModelMethod.put(RubiksCube.Action.ROTATE, rubiksCubeModel::rotate);
        actionToModelMethod.put(RubiksCube.Action.ROTATE_INV, rubiksCubeModel::rotateInv);

        cubeActions.forEach(action -> modelSolvingActions.add(actionToModelMethod.get(action)));
    }

    private RubiksCubeModel rubiksCubeModel;

    private Queue<Runnable> modelSolvingActions = new LinkedList<>();
}
