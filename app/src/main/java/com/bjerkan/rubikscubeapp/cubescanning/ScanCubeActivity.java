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
import com.bjerkan.rubikscubeapp.rubikscube.RubiksCubeFace;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An activity for scanning each face of the Rubik's Cube in term.
 *
 * Uses the phone's camera to take an image of a Rubik's Cube face which is subsequently scanned
 * to find the colours of the individual squares.
 */
public class ScanCubeActivity extends FragmentActivity
        implements CaptureImageFragment.OnImageCapturedListener,
                   DisplayResultFragment.NextStepRequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_cube);

        currentFace = CubeFace.startFace;

        if (savedInstanceState == null) {
            showCaptureImageFragment();
        }
    }

    /**
     * Receives a captured image and processes it to find the colours for the face being scanned.
     *
     * @param image the image captured of the face being scanned
     */
    @Override
    public void onImageCaptured(Mat image) {
        cubeScanner = new CubeScanner(image);

        if (cubeScanner.scannedFace().isPresent()) {
            scannedFaces.put(currentFace, cubeScanner.scannedFace().get());
            showResultFragment();
        } else {
            Toast.makeText(this, "Scan Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Moves on to scanning the next face, or shows cube graphic activity if all faces scanned.
     */
    @Override
    public void nextStep() {
        if (currentFace.nextFace != null) {
            currentFace = currentFace.nextFace;
            showCaptureImageFragment();
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

    private void showCaptureImageFragment() {
        String title = currentFace.name().substring(0, 1).toUpperCase() +
                currentFace.name().substring(1).toLowerCase();
        captureImageFragment.setTitle(title);
        setFragment(captureImageFragment);
    }

    private void showResultFragment() {
        cubeScanner.faceImage().ifPresent(faceImage -> {
            resultFragment.setResultImage(matToBitmap(faceImage));
            setFragment(resultFragment);
        });
    }

    private void showCubeGraphic() {
        Intent cubeGraphicIntent = new Intent(this, CubeGraphicActivity.class);

        cubeGraphicIntent.putExtra(
                CubeGraphicActivity.FRONT_COLOURS_ARGUMENT, scannedFaces.get(CubeFace.FRONT));
        cubeGraphicIntent.putExtra(
                CubeGraphicActivity.LEFT_COLOURS_ARGUMENT, scannedFaces.get(CubeFace.LEFT));
        cubeGraphicIntent.putExtra(
                CubeGraphicActivity.BACK_COLOURS_ARGUMENT, scannedFaces.get(CubeFace.BACK));
        cubeGraphicIntent.putExtra(
                CubeGraphicActivity.RIGHT_COLOURS_ARGUMENT, scannedFaces.get(CubeFace.RIGHT));
        cubeGraphicIntent.putExtra(
                CubeGraphicActivity.TOP_COLOURS_ARGUMENT, scannedFaces.get(CubeFace.TOP));
        cubeGraphicIntent.putExtra(
                CubeGraphicActivity.BOTTOM_COLOURS_ARGUMENT, scannedFaces.get(CubeFace.BOTTOM));

        startActivity(cubeGraphicIntent);
    }

    private Bitmap matToBitmap(Mat image) {
        Bitmap bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, bitmap);
        return bitmap;
    }

    private CubeScanner cubeScanner;
    private CubeFace currentFace;

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
    private DisplayResultFragment resultFragment = new DisplayResultFragment();

    private Map<CubeFace, RubiksCubeFace> scannedFaces = new HashMap<>();
}
