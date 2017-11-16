package com.bjerkan.rubikscubeapp;

import android.app.Activity;
import android.os.Bundle;

import org.opencv.core.Mat;

public class ScanCubeActivity extends Activity
        implements CaptureImageFragment.OnImageCapturedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_cube);
    }

    @Override
    public void onImageCaptured(Mat image) {
        mOriginalImage = image;
    }

    private Mat mOriginalImage;
}
