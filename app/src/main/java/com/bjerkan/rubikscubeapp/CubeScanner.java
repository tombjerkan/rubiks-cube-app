package com.bjerkan.rubikscubeapp;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

public class CubeScanner {
    public CubeScanner(Mat cubeImage) {
        mOriginalImage = cubeImage;

        findEdges();
        findLines();
    }

    public Mat originalImage() {
        return mOriginalImage;
    }

    public Mat edgeImage() {
        return mEdgeImage;
    }

    public Mat lineImage() {
        return mLineImage;
    }

    private void findEdges() {
        mEdgeImage = new Mat(mOriginalImage.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(mOriginalImage, mEdgeImage, Imgproc.COLOR_BGR2GRAY, 4);
        Imgproc.GaussianBlur(mEdgeImage, mEdgeImage, new Size(5, 5), 0);
        Imgproc.Canny(mEdgeImage, mEdgeImage, 0, 50);
    }

    private void findLines() {
        Mat linesMatrix = new Mat();
        Imgproc.HoughLines(mEdgeImage, linesMatrix, 1, Math.PI/180, HOUGH_THRESHOLD);

        List<Line> lines = new LinkedList<>();
        for (int lineIndex = 0; lineIndex < linesMatrix.rows(); lineIndex++) {
            double[] matrixLine = linesMatrix.get(lineIndex, 0);
            lines.add(new Line(matrixLine[0], matrixLine[1]));
        }

        mLineImage = drawLines(lines);
    }

    private Mat drawLines(List<Line> lines) {
        Mat image = mOriginalImage.clone();

        for (Line line : lines) {
            double a = Math.cos(line.theta());
            double b = Math.sin(line.theta());
            double x0 = a * line.rho();
            double y0 = b * line.rho();
            Point start = new Point(x0 + (3000 * -b), y0 + (3000 * a));
            Point end = new Point(x0 - (3000 * -b), y0 - (3000 * a));

            Imgproc.line(image, start, end, new Scalar(255, 0, 0), 3);
        }

        return image;
    }

    private class Line {
        public Line(double rho, double theta) {
            // Ensure similar lines have numerically close values
            if (rho >= 0) {
                mRho = rho;
                mTheta = theta;
            } else {
                mRho = -rho;
                mTheta = theta - Math.PI;
            }
        }

        public double rho() {
            return mRho;
        }

        public double theta() {
            return mTheta;
        }

        private final double mRho;
        private final double mTheta;
    }

    private Mat mOriginalImage;
    private Mat mEdgeImage;
    private Mat mLineImage;

    private static final int CANNY_THRESHOLD_1 = 0;
    private static final int CANNY_THRESHOLD_2 = 50;
    private static final int HOUGH_THRESHOLD = 100;
}
