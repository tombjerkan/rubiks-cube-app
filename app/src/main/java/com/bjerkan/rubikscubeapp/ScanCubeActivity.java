package com.bjerkan.rubikscubeapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        transaction.addToBackStack(null);
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
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (currentFace.nextFace != null) {
            currentFace = currentFace.nextFace;

            CaptureImageFragment captureImageFragment = new CaptureImageFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, captureImageFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
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
