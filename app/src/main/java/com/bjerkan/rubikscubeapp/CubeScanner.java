package com.bjerkan.rubikscubeapp;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CubeScanner {
    public CubeScanner(Mat cubeImage) {
        mOriginalImage = cubeImage;

        findEdges();
        findLines();
        findOrthogonalLines();
        combineLines();
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

    public Mat orthogonalLineImage() {
        return mOrthogonalLineImage;
    }

    public Mat combinedLineImage() {
        return mCombinedLineImage;
    }

    public Mat stepImage(Step step) {
        switch(step) {
            case EDGES:
                return edgeImage();
            case LINES:
                return lineImage();
            case ORTHOGONAL_LINES:
                return orthogonalLineImage();
            case COMBINED_LINES:
                return combinedLineImage();
            default:
                return originalImage();
        }
    }

    public enum Step {
        EDGES,
        LINES,
        ORTHOGONAL_LINES,
        COMBINED_LINES;

        private Step nextStep;

        static {
            EDGES.nextStep = LINES;
            LINES.nextStep = ORTHOGONAL_LINES;
            ORTHOGONAL_LINES.nextStep = COMBINED_LINES;
            COMBINED_LINES.nextStep = null;
        }

        public Step nextStep() {
            return nextStep;
        }
    }

    private void findEdges() {
        mEdgeImage = new Mat(mOriginalImage.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(mOriginalImage, mEdgeImage, Imgproc.COLOR_BGR2GRAY, 4);
        Imgproc.GaussianBlur(mEdgeImage, mEdgeImage, new Size(5, 5), 0);
        Imgproc.Canny(mEdgeImage, mEdgeImage, CANNY_THRESHOLD_1, CANNY_THRESHOLD_2);
    }

    private void findLines() {
        Mat linesMatrix = new Mat();
        Imgproc.HoughLines(mEdgeImage, linesMatrix, 1, Math.PI/180, HOUGH_THRESHOLD);

        mLines = new LinkedList<>();
        for (int lineIndex = 0; lineIndex < linesMatrix.rows(); lineIndex++) {
            double[] matrixLine = linesMatrix.get(lineIndex, 0);
            mLines.add(new Line(matrixLine[0], matrixLine[1]));
        }

        mLineImage = drawLines(mLines);
    }

    private void findOrthogonalLines() {
        mOrthogonalLines = mLines.stream()
                .filter(line -> line.isOrthogonal())
                .collect(Collectors.toList());

        mOrthogonalLineImage = drawLines(mOrthogonalLines);
    }

    private void combineLines() {
        boolean[] lineHandled = new boolean[mOrthogonalLines.size()];
        for (int i = 0; i < lineHandled.length; ++i) {
            lineHandled[i] = false;
        }

        mCombinedLines = new LinkedList<>();
        for (int lineIndex = 0; lineIndex < mOrthogonalLines.size(); lineIndex++) {
            if (!lineHandled[lineIndex]) {
                List<Line> similarLines = new LinkedList<>();

                similarLines.add(mOrthogonalLines.get(lineIndex));
                lineHandled[lineIndex] = true;

                for (int otherLineIndex = 0; otherLineIndex < mOrthogonalLines.size();
                     otherLineIndex++) {
                    if (mOrthogonalLines.get(lineIndex).isSimilar(
                            mOrthogonalLines.get(otherLineIndex))) {
                        similarLines.add(mOrthogonalLines.get(otherLineIndex));
                        lineHandled[otherLineIndex] = true;
                    }
                }

                Line averageLine = averageLines(similarLines);
                mCombinedLines.add(averageLine);
            }
        }

        mCombinedLineImage = drawLines(mCombinedLines);
    }

    private Line averageLines(List<Line> lines) {
        return new Line(
                lines.stream().mapToDouble(line -> line.rho()).sum() / lines.size(),
                lines.stream().mapToDouble(line ->line.theta()).sum() / lines.size());
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

        public boolean isHorizontal() {
            return (mTheta > (Math.PI / 2) - ORTHOGONAL_THRESHOLD) &&
                    (mTheta < (Math.PI / 2) + ORTHOGONAL_THRESHOLD);
        }

        public boolean isVertical() {
            return (mTheta > -ORTHOGONAL_THRESHOLD) && (mTheta < ORTHOGONAL_THRESHOLD);
        }

        public boolean isOrthogonal() {
            return isHorizontal() || isVertical();
        }

        public boolean isSimilar(Line otherLine) {
            return Math.abs(mRho - otherLine.mRho) < SIMILARITY_RHO_THRESHOLD &&
                    Math.abs(mTheta - otherLine.mTheta) < SIMILARITY_THETA_THRESHOLD;
        }

        private final double mRho;
        private final double mTheta;
    }

    private Mat mOriginalImage;
    private Mat mEdgeImage;
    private Mat mLineImage;
    private Mat mOrthogonalLineImage;
    private Mat mCombinedLineImage;

    List<Line> mLines;
    List<Line> mOrthogonalLines;
    List<Line> mCombinedLines;

    private static final int CANNY_THRESHOLD_1 = 0;
    private static final int CANNY_THRESHOLD_2 = 50;
    private static final int HOUGH_THRESHOLD = 100;
    private static final double ORTHOGONAL_THRESHOLD = Math.PI/36;
    private static final double SIMILARITY_RHO_THRESHOLD = 100;
    private static final double SIMILARITY_THETA_THRESHOLD = Math.PI / 18;
}
