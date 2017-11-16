package com.bjerkan.rubikscubeapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
    }

    private Mat mOriginalImage;
}
