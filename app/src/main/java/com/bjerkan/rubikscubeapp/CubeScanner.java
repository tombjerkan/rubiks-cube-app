package com.bjerkan.rubikscubeapp;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CubeScanner {
    public CubeScanner(Mat cubeImage) {
        mOriginalImage = cubeImage;

        findEdges();
    }

    public Mat originalImage() {
        return mOriginalImage;
    }

    public Mat edgeImage() {
        return mEdgeImage;
    }

    private void findEdges() {
        mEdgeImage = new Mat(mOriginalImage.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(mOriginalImage, mEdgeImage, Imgproc.COLOR_BGR2GRAY, 4);
        Imgproc.GaussianBlur(mEdgeImage, mEdgeImage, new Size(5, 5), 0);
        Imgproc.Canny(mEdgeImage, mEdgeImage, 0, 50);
    }

    private Mat mOriginalImage;
    private Mat mEdgeImage;

    private static final int CANNY_THRESHOLD_1 = 0;
    private static final int CANNY_THRESHOLD_2 = 50;
}
