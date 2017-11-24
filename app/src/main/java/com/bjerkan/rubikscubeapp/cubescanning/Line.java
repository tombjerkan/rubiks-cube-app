package com.bjerkan.rubikscubeapp.cubescanning;

import org.opencv.core.Point;

/**
 * A polar representation of a line: ρ = x.cosθ + y.sinθ
 */
class Line {
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

    double rho() {
        return rho;
    }

    double theta() {
        return theta;
    }

    boolean isHorizontal() {
        return (theta > (Math.PI / 2) - ORTHOGONAL_THRESHOLD) &&
                (theta < (Math.PI / 2) + ORTHOGONAL_THRESHOLD);
    }

    boolean isVertical() {
        return (theta > -ORTHOGONAL_THRESHOLD) && (theta < ORTHOGONAL_THRESHOLD);
    }

    boolean isOrthogonal() {
        return isHorizontal() || isVertical();
    }

    boolean isSimilar(Line otherLine) {
        return Math.abs(rho - otherLine.rho) < SIMILARITY_RHO_THRESHOLD &&
                Math.abs(theta - otherLine.theta) < SIMILARITY_THETA_THRESHOLD;
    }

    Point intersection(Line otherLine) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double otherCosTheta = Math.cos(otherLine.theta);
        double otherSinTheta = Math.sin(otherLine.theta);

        double det = cosTheta * otherSinTheta - sinTheta * otherCosTheta;

        if (det == 0) {
            return null;
        }

        double x = (otherSinTheta * rho - sinTheta * otherLine.rho) / det;
        double y = (cosTheta * otherLine.rho - otherCosTheta * rho) / det;

        return new Point(x, y);
    }

    private final double rho;
    private final double theta;

    private static final double ORTHOGONAL_THRESHOLD = Math.PI/36;
    private static final double SIMILARITY_RHO_THRESHOLD = 100;
    private static final double SIMILARITY_THETA_THRESHOLD = Math.PI / 18;
}