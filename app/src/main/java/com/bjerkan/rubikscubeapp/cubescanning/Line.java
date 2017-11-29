package com.bjerkan.rubikscubeapp.cubescanning;

import org.opencv.core.Point;

import java.util.Optional;

/**
 * A polar representation of a line (ρ = x cosθ + y sinθ).
 */
class Line {
    /**
     * Creates a Line object with given ρ and θ parameters.
     *
     * @param rho The ρ value for the line's polar equation
     * @param theta The θ value for the line's polar equation
     */
    Line(double rho, double theta) {
        // Ensure similar lines have numerically close values
        if (rho >= 0) {
            this.rho = rho;
            this.theta = theta;
        } else {
            this.rho = -rho;
            this.theta = theta - Math.PI;
        }
    }

    /**
     * Returns the ρ value for the line's polar equation
     *
     * @return the ρ value for the line's polar equation
     */
    double rho() {
        return rho;
    }

    /**
     * Returns the θ value for the line's polar equation
     *
     * @return the θ value for the line's polar equation
     */
    double theta() {
        return theta;
    }

    /**
     * Returns whether the line is, or is close to, horizontal. A line is close to horizontal if its
     * angle is within a certain threshold of being horizontal.
     *
     * @return true if the line is, or is close to, horizontal and false otherwise
     */
    boolean isHorizontal() {
        return (theta > (Math.PI / 2) - ORTHOGONAL_THRESHOLD) &&
                (theta < (Math.PI / 2) + ORTHOGONAL_THRESHOLD);
    }

    /**
     * Returns whether the line is, or is close to, vertical. A line is close to horizontal if its
     * angle is within a certain threshold of being vertical.
     *
     * @return true if the line is, or is close to, vertical and false otherwise
     */
    boolean isVertical() {
        return (theta > -ORTHOGONAL_THRESHOLD) && (theta < ORTHOGONAL_THRESHOLD);
    }

    /**
     * Returns whether the line is, or is close to being, either horizontal or vertical.
     *
     * @return true if the line is, or is close to being, horizontal or vertical and false otherwise
     * @see #isHorizontal()
     * @see #isVertical()
     */
    boolean isOrthogonal() {
        return isHorizontal() || isVertical();
    }

    /**
     * Returns whether this line is similar to the given line. Two lines are similar if the
     * difference between their ρ and θ values are within some thresholds.
     *
     * @param otherLine the line to compare for similarity with
     * @return true if the two lines are similar enough, false otherwise
     */
    boolean isSimilar(Line otherLine) {
        return Math.abs(rho - otherLine.rho) < SIMILARITY_RHO_THRESHOLD &&
                Math.abs(theta - otherLine.theta) < SIMILARITY_THETA_THRESHOLD;
    }

    /**
     * Returns the point whether this line intersects with the given line. If the lines are parallel
     * and do not intersect, an empty Optional is returned.
     *
     * @param otherLine the line to find the intersection with
     * @return an Optional with the point of intersection, or empty if the lines do not intersect
     */
    Optional<Point> intersection(Line otherLine) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double otherCosTheta = Math.cos(otherLine.theta);
        double otherSinTheta = Math.sin(otherLine.theta);

        double det = cosTheta * otherSinTheta - sinTheta * otherCosTheta;

        if (det == 0) {
            return Optional.empty();
        }

        double x = (otherSinTheta * rho - sinTheta * otherLine.rho) / det;
        double y = (cosTheta * otherLine.rho - otherCosTheta * rho) / det;

        return Optional.of(new Point(x, y));
    }

    private final double rho;
    private final double theta;

    private static final double ORTHOGONAL_THRESHOLD = Math.PI/36;
    private static final double SIMILARITY_RHO_THRESHOLD = 100;
    private static final double SIMILARITY_THETA_THRESHOLD = Math.PI / 18;
}