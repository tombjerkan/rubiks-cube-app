package com.bjerkan.rubikscubeapp;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        findCentreLines();

        if (mCentreLines == null) {
            mSuccessful = false;
            return;
        }

        findCentrePoints();
        findColours();

        mSuccessful = true;
    }

    public boolean wasSuccessful() {
        return mSuccessful;
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

    public Mat centreLineImage() {
        return mCentreLineImage;
    }

    public Mat centrePointImage() {
        return mCentrePointImage;
    }

    public List<RubiksColour> squareColours() {
        return mSquareColours;
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
            case CENTRE_LINES:
                return centreLineImage();
            case CENTRE_POINTS:
                return centrePointImage();
            default:
                return originalImage();
        }
    }

    public enum Step {
        EDGES,
        LINES,
        ORTHOGONAL_LINES,
        COMBINED_LINES,
        CENTRE_LINES,
        CENTRE_POINTS;

        private Step nextStep;

        static {
            EDGES.nextStep = LINES;
            LINES.nextStep = ORTHOGONAL_LINES;
            ORTHOGONAL_LINES.nextStep = COMBINED_LINES;
            COMBINED_LINES.nextStep = CENTRE_LINES;
            CENTRE_LINES.nextStep = CENTRE_POINTS;
            CENTRE_POINTS.nextStep = null;
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

    private void findCentreLines() {
        List<Line> horizontalLines = mCombinedLines.stream()
                .filter(line -> line.isHorizontal())
                .collect(Collectors.toList());

        List<Line> verticalLines = mCombinedLines.stream()
                .filter(line -> line.isVertical())
                .collect(Collectors.toList());

        if (horizontalLines.size() != 4 || verticalLines.size() != 4) {
            return;
        }

        // Sorts lines by distance away from origin to get left-to-right and top-to-bottom order
        horizontalLines.sort((line1, line2) -> (int) (line1.rho() - line2.rho()));
        verticalLines.sort((line1, line2) -> (int) (line1.rho() - line2.rho()));

        Line topCentreHorizontal = averageLines(horizontalLines.get(0), horizontalLines.get(1));
        Line middleCentreHorizontal = averageLines(horizontalLines.get(1), horizontalLines.get(2));
        Line bottomCentreHorizontal = averageLines(horizontalLines.get(2), horizontalLines.get(3));

        Line leftCentreVertical = averageLines(verticalLines.get(0), verticalLines.get(1));
        Line middleCentreVertical = averageLines(verticalLines.get(1), verticalLines.get(2));
        Line rightCentreVertical = averageLines(verticalLines.get(2), verticalLines.get(3));

        mCentreLines = new ArrayList<>(Arrays.asList(
                topCentreHorizontal, middleCentreHorizontal, bottomCentreHorizontal,
                leftCentreVertical, middleCentreVertical, rightCentreVertical));

        mCentreLineImage = drawLines(mCentreLines);
    }

    private Line averageLines(List<Line> lines) {
        return new Line(
                lines.stream().mapToDouble(line -> line.rho()).sum() / lines.size(),
                lines.stream().mapToDouble(line ->line.theta()).sum() / lines.size());
    }

    private Line averageLines(Line line1, Line line2) {
        return averageLines(Arrays.asList(line1, line2));
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

    private void findCentrePoints() {
        Point topLeftCentre = mCentreLines.get(0).intersection(mCentreLines.get(3));
        Point topMiddleCentre = mCentreLines.get(0).intersection(mCentreLines.get(4));
        Point topRightCentre = mCentreLines.get(0).intersection(mCentreLines.get(5));

        Point middleLeftCentre = mCentreLines.get(1).intersection(mCentreLines.get(3));
        Point middleMiddleCentre = mCentreLines.get(1).intersection(mCentreLines.get(4));
        Point middleRightCentre = mCentreLines.get(1).intersection(mCentreLines.get(5));

        Point bottomLeftCentre = mCentreLines.get(2).intersection(mCentreLines.get(3));
        Point bottomMiddleCentre = mCentreLines.get(2).intersection(mCentreLines.get(4));
        Point bottomRightCentre = mCentreLines.get(2).intersection(mCentreLines.get(5));

        mCentrePoints = new ArrayList<>(Arrays.asList(
                topLeftCentre, topMiddleCentre, topRightCentre,
                middleLeftCentre, middleMiddleCentre, middleRightCentre,
                bottomLeftCentre, bottomMiddleCentre, bottomRightCentre));

        mCentrePointImage = drawPoints(mCentrePoints);
    }

    private Mat drawPoints(List<Point> points) {
        Mat image = mOriginalImage.clone();
        for (Point point : points) {
            Imgproc.circle(image, point, 5, new Scalar(255, 0, 255));
        }

        return image;
    }

    private void findColours() {
        final Mat hsvImage = new Mat(mOriginalImage.size(), CvType.CV_8UC3);
        Imgproc.cvtColor(mOriginalImage, hsvImage, Imgproc.COLOR_RGB2HSV, 3);

        List<Double> centrePointColourHues = mCentrePoints.stream()
                .map(point -> hsvImage.get((int) point.y, (int) point.x)[0])
                .collect(Collectors.toList());

        List<RubiksColour> squareColours = centrePointColourHues.stream().map(hue ->
                Arrays.asList(RubiksColour.values())
                        .stream()
                        .map(rubiksColour -> new ColourSimilarity(rubiksColour,
                                hueSimilarity(hue, rubiksColour.hue)))
                        .max(Comparator.comparing(ColourSimilarity::similarity))
                        .get()
                        .colour()
        ).collect(Collectors.toList());
    }

    private class ColourSimilarity {
        public ColourSimilarity(RubiksColour colour, double similarity) {
            mColour = colour;
            mSimilarity = similarity;
        }

        public RubiksColour colour() {
            return mColour;
        }

        public double similarity() {
            return mSimilarity;
        }

        private final RubiksColour mColour;
        private final double mSimilarity;
    }

    private double hueSimilarity(double hue1, double hue2) {
        // Hue wraps around from 180. to 0. so must take this into account
        return 90. - Math.min(Math.abs(hue1 - hue2), 180. - Math.abs(hue1 - hue2));
    }

    private enum RubiksColour {
        WHITE,
        GREEN,
        RED,
        BLUE,
        ORANGE,
        YELLOW;

        private double hue;

        static {
            WHITE.hue = 0.;
            GREEN.hue = 74.;
            RED.hue = 174.;
            BLUE.hue = 108.;
            ORANGE.hue = 11.;
            YELLOW.hue = 25.;
        }
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

        public Point intersection(Line otherLine) {
            double cosTheta = Math.cos(mTheta);
            double sinTheta = Math.sin(mTheta);
            double otherCosTheta = Math.cos(otherLine.mTheta);
            double otherSinTheta = Math.sin(otherLine.mTheta);

            double det = cosTheta * otherSinTheta - sinTheta * otherCosTheta;

            if (det == 0) {
                return null;
            }

            double x = (otherSinTheta * mRho - sinTheta * otherLine.mRho) / det;
            double y = (cosTheta * otherLine.mRho - otherCosTheta * mRho) / det;

            return new Point(x, y);
        }

        private final double mRho;
        private final double mTheta;
    }

    private boolean mSuccessful;

    private Mat mOriginalImage;
    private Mat mEdgeImage;
    private Mat mLineImage;
    private Mat mOrthogonalLineImage;
    private Mat mCombinedLineImage;
    private Mat mCentreLineImage;
    private Mat mCentrePointImage;

    private List<Line> mLines;
    private List<Line> mOrthogonalLines;
    private List<Line> mCombinedLines;
    private List<Line> mCentreLines;
    private List<Point> mCentrePoints;
    private List<RubiksColour> mSquareColours;

    private static final int CANNY_THRESHOLD_1 = 10;
    private static final int CANNY_THRESHOLD_2 = 25;
    private static final int HOUGH_THRESHOLD = 100;
    private static final double ORTHOGONAL_THRESHOLD = Math.PI/36;
    private static final double SIMILARITY_RHO_THRESHOLD = 100;
    private static final double SIMILARITY_THETA_THRESHOLD = Math.PI / 18;
}
