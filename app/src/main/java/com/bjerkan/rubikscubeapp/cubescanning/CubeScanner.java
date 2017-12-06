package com.bjerkan.rubikscubeapp.cubescanning;

import com.bjerkan.rubikscubeapp.rubikscube.Colour;
import com.bjerkan.rubikscubeapp.rubikscube.RubiksCubeFace;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scans an image of a cube face to find the colours of the individual squares.
 */
public class CubeScanner {
    /**
     * Creates an object representing a scan of the cube face image given.
     *
     * @param cubeImage an image of the cube face to be scanned
     */
    public CubeScanner(Mat cubeImage) {
        Mat edgeImage = findEdges(cubeImage);
        List<Line> allHoughLines = findLines(edgeImage);
        List<Line> orthogonalLines = findOrthogonalLines(allHoughLines);
        List<Line> combinedLines = combineLines(orthogonalLines);
        List<Line> centreLines = findCentreLines(combinedLines);

        if (centreLines == null) {
            return;
        }

        List<Point> centrePoints = findCentrePoints(centreLines);
        findColours(cubeImage, centrePoints);
    }

    /**
     * Returns the scanned RubiksCubeFace.
     *
     * @return an Optional containing the scanned face, or empty if scan was not successful
     */
    Optional<RubiksCubeFace> scannedFace() {
        return Optional.ofNullable(scannedFace);
    }

    /**
     * Returns the Mat image showing the scanned face colours.
     *
     * @return an Optional containing a Mat image representing the scanned cube face, or empty if
     * scan was not successful
     */
    Optional<Mat> faceImage() {
        return Optional.ofNullable(faceImage);
    }

    private Mat findEdges(Mat originalImage) {
        Mat edgeImage = new Mat(originalImage.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(originalImage, edgeImage, Imgproc.COLOR_BGR2GRAY, 4);
        Imgproc.GaussianBlur(edgeImage, edgeImage, new Size(5, 5), 0);
        Imgproc.Canny(edgeImage, edgeImage, CANNY_THRESHOLD_1, CANNY_THRESHOLD_2);
        return edgeImage;
    }

    private List<Line> findLines(Mat edgeImage) {
        Mat linesMatrix = new Mat();
        Imgproc.HoughLines(edgeImage, linesMatrix, 1, Math.PI/180, HOUGH_THRESHOLD);

        List<Line> allHoughLines = new LinkedList<>();
        for (int lineIndex = 0; lineIndex < linesMatrix.rows(); lineIndex++) {
            double[] matrixLine = linesMatrix.get(lineIndex, 0);
            allHoughLines.add(new Line(matrixLine[0], matrixLine[1]));
        }

        return allHoughLines;
    }

    private List<Line> findOrthogonalLines(List<Line> allHoughLines) {
        return allHoughLines.stream()
                .filter(Line::isOrthogonal)
                .collect(Collectors.toList());
    }

    private List<Line> combineLines(List<Line> orthogonalLines) {
        boolean[] lineHandled = new boolean[orthogonalLines.size()];
        for (int i = 0; i < lineHandled.length; ++i) {
            lineHandled[i] = false;
        }

        List<Line> combinedLines = new LinkedList<>();
        for (int lineIndex = 0; lineIndex < orthogonalLines.size(); lineIndex++) {
            if (!lineHandled[lineIndex]) {
                List<Line> similarLines = new LinkedList<>();

                similarLines.add(orthogonalLines.get(lineIndex));
                lineHandled[lineIndex] = true;

                for (int otherLineIndex = 0; otherLineIndex < orthogonalLines.size();
                     otherLineIndex++) {
                    if (orthogonalLines.get(lineIndex).isSimilar(
                            orthogonalLines.get(otherLineIndex))) {
                        similarLines.add(orthogonalLines.get(otherLineIndex));
                        lineHandled[otherLineIndex] = true;
                    }
                }

                Line averageLine = averageLines(similarLines);
                combinedLines.add(averageLine);
            }
        }

        return combinedLines;
    }

    private List<Line> findCentreLines(List<Line> combinedLines) {
        List<Line> horizontalLines = combinedLines.stream()
                .filter(Line::isHorizontal)
                .collect(Collectors.toList());

        List<Line> verticalLines = combinedLines.stream()
                .filter(Line::isVertical)
                .collect(Collectors.toList());

        if (horizontalLines.size() != 4 || verticalLines.size() != 4) {
            return null;
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

        return new ArrayList<>(Arrays.asList(
                topCentreHorizontal, middleCentreHorizontal, bottomCentreHorizontal,
                leftCentreVertical, middleCentreVertical, rightCentreVertical));
    }

    private Line averageLines(List<Line> lines) {
        return new Line(
                lines.stream().mapToDouble(Line::rho).sum() / lines.size(),
                lines.stream().mapToDouble(Line::theta).sum() / lines.size());
    }

    private Line averageLines(Line line1, Line line2) {
        return averageLines(Arrays.asList(line1, line2));
    }

    private List<Point> findCentrePoints(List<Line> centreLines) {
        // Do not need to check that Optional not empty as centre lines always 3 horizontal and
        // 3 vertical lines. A horizontal and a vertical line always have an intersection.
        Point topLeftCentre = centreLines.get(0).intersection(centreLines.get(3)).get();
        Point topMiddleCentre = centreLines.get(0).intersection(centreLines.get(4)).get();
        Point topRightCentre = centreLines.get(0).intersection(centreLines.get(5)).get();

        Point middleLeftCentre = centreLines.get(1).intersection(centreLines.get(3)).get();
        Point middleMiddleCentre = centreLines.get(1).intersection(centreLines.get(4)).get();
        Point middleRightCentre = centreLines.get(1).intersection(centreLines.get(5)).get();

        Point bottomLeftCentre = centreLines.get(2).intersection(centreLines.get(3)).get();
        Point bottomMiddleCentre = centreLines.get(2).intersection(centreLines.get(4)).get();
        Point bottomRightCentre = centreLines.get(2).intersection(centreLines.get(5)).get();

        return new ArrayList<>(Arrays.asList(
                topLeftCentre, topMiddleCentre, topRightCentre,
                middleLeftCentre, middleMiddleCentre, middleRightCentre,
                bottomLeftCentre, bottomMiddleCentre, bottomRightCentre));
    }

    private void findColours(Mat originalImage, List<Point> centrePoints) {
        final Mat hsvImage = new Mat(originalImage.size(), CvType.CV_8UC3);
        Imgproc.cvtColor(originalImage, hsvImage, Imgproc.COLOR_RGB2HSV, 3);

        List<Colour> squareColours = centrePoints.stream().map(centre -> {
            List<Point> votingPoints = votingPoints(centre);

            Map<Colour, Integer> colourVotes = new HashMap<>();
            Stream.of(Colour.values()).forEach(colour -> colourVotes.put(colour, 0));

            votingPoints.stream()
                    .map(point -> mostSimilarColour(hsvImage, point))
                    .forEach(colour -> colourVotes.put(colour, colourVotes.get(colour) + 1));

            return colourVotes.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get().getKey();
        }).collect(Collectors.toList());

        scannedFace = new RubiksCubeFace(squareColours);

        drawFaceImage();
    }

    private List<Point> votingPoints(Point centre) {
        int leftX = (int) centre.x - COLOUR_VOTE_OFFSET;
        int rightX = (int) centre.x + COLOUR_VOTE_OFFSET;
        int topY = (int) centre.y - COLOUR_VOTE_OFFSET;
        int bottomY = (int) centre.y + COLOUR_VOTE_OFFSET;

        List<Point> votingPoints = new LinkedList<>();
        for (int x = leftX; x <= rightX; x++) {
            for (int y = topY; y <= bottomY; y++) {
                votingPoints.add(new Point(x, y));
            }
        }

        return votingPoints;
    }

    private Colour mostSimilarColour(Mat hsvImage, Point point) {
        double[] hsvColour = hsvImage.get((int) point.y, (int) point.x);

        // Mark colours with low saturation as white.
        if (hsvColour[1] < 80.) {
            return Colour.WHITE;
        }

        Comparator<Colour> similarityComparator = (colour1, colour2) -> Double.compare(
                hueSimilarity(colour1.hue, hsvColour[0]),
                hueSimilarity(colour2.hue, hsvColour[0]));

        return Arrays.stream(Colour.values())
                .filter(colour -> colour != Colour.WHITE)
                .max(similarityComparator)
                .get();
    }

    private void drawFaceImage() {
        faceImage = new Mat(300, 300, CvType.CV_8UC4);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Point start = new Point(x * 100, y * 100);
                Point end = new Point((x + 1) * 100 - 1, (y + 1) * 100 - 1);
                Scalar colour = scannedFace.colours().get((y * 3) + x).rgb;

                Imgproc.rectangle(faceImage, start, end, colour, -1);
            }
        }
    }

    private double hueSimilarity(double hue1, double hue2) {
        // Hue wraps around from 180. to 0. so must take this into account
        return 90. - Math.min(Math.abs(hue1 - hue2), 180. - Math.abs(hue1 - hue2));
    }

    private Mat faceImage;

    private RubiksCubeFace scannedFace;

    private static final int CANNY_THRESHOLD_1 = 10;
    private static final int CANNY_THRESHOLD_2 = 25;
    private static final int HOUGH_THRESHOLD = 100;
    private static final int COLOUR_VOTE_OFFSET = 20;
}
