package com.bjerkan.rubikscubeapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class ScanCubeActivity extends FragmentActivity
        implements CaptureImageFragment.OnImageCapturedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_cube);

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
        mOriginalImage = image;

        DisplayResultFragment resultFragment = new DisplayResultFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(DisplayResultFragment.IMAGE_ARGUMENT, matToBitmap(image));
        resultFragment.setArguments(arguments);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, resultFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Bitmap matToBitmap(Mat image) {
        Bitmap bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, bitmap);
        return bitmap;
    }

    private Mat mOriginalImage;
}
