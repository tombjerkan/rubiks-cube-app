package com.bjerkan.rubikscubeapp.cubescanning;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.bjerkan.rubikscubeapp.R;
import com.bjerkan.rubikscubeapp.cubegraphic.CubeGraphicActivity;
import com.bjerkan.rubikscubeapp.rubikscube.Colour;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScanCubeActivity extends FragmentActivity
        implements CaptureImageFragment.OnImageCapturedListener,
                   DisplayResultFragment.NextStepRequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_cube);

        currentFace = CubeFace.startFace;

        if (savedInstanceState == null) {
            setFragment(captureImageFragment);
        }
    }

    @Override
    public void onImageCaptured(Mat image) {
        cubeScanner = new CubeScanner(image);

        if (!cubeScanner.wasSuccessful()) {
            Toast.makeText(this, "Scan Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        faceColours.put(currentFace, cubeScanner.squareColours());

        currentStep = CubeScanner.Step.EDGES;
        showResultFragment();
    }

    @Override
    public void nextStep() {
        if (currentStep.nextStep() != null) {
            currentStep = currentStep.nextStep();
            showResultFragment();
        } else if (currentFace.nextFace != null) {
            currentFace = currentFace.nextFace;
            setFragment(captureImageFragment);
        } else {
            showCubeGraphic();
        }
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void showResultFragment() {
        Bundle arguments = new Bundle();
        arguments.putParcelable(DisplayResultFragment.IMAGE_ARGUMENT,
                matToBitmap(cubeScanner.stepImage(currentStep)));

        DisplayResultFragment resultFragment = new DisplayResultFragment();
        resultFragment.setArguments(arguments);
        setFragment(resultFragment);
    }

    private void showCubeGraphic() {
        Intent cubeGraphicIntent = new Intent(this, CubeGraphicActivity.class);

        ArrayList<String> frontColours = asIntentArgument(faceColours.get(CubeFace.FRONT));
        ArrayList<String> leftColours = asIntentArgument(faceColours.get(CubeFace.LEFT));
        ArrayList<String> backColours = asIntentArgument(faceColours.get(CubeFace.BACK));
        ArrayList<String> rightColours = asIntentArgument(faceColours.get(CubeFace.RIGHT));
        ArrayList<String> topColours = asIntentArgument(faceColours.get(CubeFace.TOP));
        ArrayList<String> bottomColours = asIntentArgument(faceColours.get(CubeFace.BOTTOM));

        cubeGraphicIntent.putStringArrayListExtra(
                CubeGraphicActivity.FRONT_COLOURS_ARGUMENT, frontColours);
        cubeGraphicIntent.putStringArrayListExtra(
                CubeGraphicActivity.LEFT_COLOURS_ARGUMENT, leftColours);
        cubeGraphicIntent.putStringArrayListExtra(
                CubeGraphicActivity.BACK_COLOURS_ARGUMENT, backColours);
        cubeGraphicIntent.putStringArrayListExtra(
                CubeGraphicActivity.RIGHT_COLOURS_ARGUMENT, rightColours);
        cubeGraphicIntent.putStringArrayListExtra(
                CubeGraphicActivity.TOP_COLOURS_ARGUMENT, topColours);
        cubeGraphicIntent.putStringArrayListExtra(
                CubeGraphicActivity.BOTTOM_COLOURS_ARGUMENT, bottomColours);

        startActivity(cubeGraphicIntent);
    }

    // Convert list of enums to ArrayList of Strings so that it can be passed in an intent
    private ArrayList<String> asIntentArgument(List<Colour> colours) {
        return colours.stream().map(Enum::name).collect(Collectors.toCollection(ArrayList::new));
    }

    private Bitmap matToBitmap(Mat image) {
        Bitmap bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, bitmap);
        return bitmap;
    }

    private CubeScanner cubeScanner;
    private CubeFace currentFace;
    private CubeScanner.Step currentStep;

    private enum CubeFace {
        FRONT,
        BACK,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM;

        private static final CubeFace startFace = FRONT;

        private CubeFace nextFace;
        static {
            FRONT.nextFace = LEFT;
            LEFT.nextFace = BACK;
            BACK.nextFace = RIGHT;
            RIGHT.nextFace = TOP;
            TOP.nextFace = BOTTOM;
        }
    }

    private CaptureImageFragment captureImageFragment = new CaptureImageFragment();

    private Map<CubeFace, List<Colour>> faceColours = new HashMap<>();
}
