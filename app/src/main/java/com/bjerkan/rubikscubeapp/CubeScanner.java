package com.bjerkan.rubikscubeapp;

import org.opencv.core.Mat;

public class CubeScanner {
    public CubeScanner(Mat cubeImage) {
        mOriginalImage = cubeImage;
    }

    public Mat originalImage() {
        return mOriginalImage;
    }

    private Mat mOriginalImage;
}
