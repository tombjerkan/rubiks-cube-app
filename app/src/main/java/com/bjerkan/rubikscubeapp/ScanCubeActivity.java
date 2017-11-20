package com.bjerkan.rubikscubeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.bjerkan.rubikscubeapp.cubegraphic.CubeGraphicActivity;

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
            CaptureImageFragment fragment = new CaptureImageFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
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

        DisplayResultFragment resultFragment = new DisplayResultFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(DisplayResultFragment.IMAGE_ARGUMENT,
                matToBitmap(cubeScanner.edgeImage()));
        resultFragment.setArguments(arguments);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, resultFragment);
        transaction.commit();
    }

    @Override
    public void nextStep() {
        if (currentStep.nextStep() != null) {
            currentStep = currentStep.nextStep();

            DisplayResultFragment resultFragment = new DisplayResultFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(DisplayResultFragment.IMAGE_ARGUMENT,
                    matToBitmap(cubeScanner.stepImage(currentStep)));
            resultFragment.setArguments(arguments);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, resultFragment);
            transaction.commit();
        } else if (currentFace.nextFace != null) {
            currentFace = currentFace.nextFace;

            CaptureImageFragment captureImageFragment = new CaptureImageFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, captureImageFragment);
            transaction.commit();
        } else {
            showCubeGraphic();
        }
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
    private ArrayList<String> asIntentArgument(List<CubeScanner.RubiksColour> colours) {
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

    private Map<CubeFace, List<CubeScanner.RubiksColour>> faceColours = new HashMap<>();
}
