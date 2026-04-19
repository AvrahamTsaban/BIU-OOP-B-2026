/**
 * Represents a line segment between two points.
 * Constructors normalize endpoints: smaller x first;
 * when x is equal (up to threshold), smaller y first.
 *
 * <p>Implementation warning: Line.equals and Line.compareTo are altered in a way that is not consistent with the
 * general contract and without overriding hashCode.
 * Both are also not fully transitive (up to the comparison threshold).
 * This is acceptable for this assignment, but should be used with caution.</p>
 *
 * <p>Generally, avoid creating lines that are points (endpoints are the same).
 * Such case is treated for safety, but not recommended.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public class Line implements Comparable<Line> {
    private final Point start;
    private final Point end;
    private final double slope;

    /**
     * Creates a line from two points and calculates slope.
    * Endpoints are swapped when needed to keep normalized order.
     *
     * @param start start point candidate
     * @param end end point candidate
     */
    public Line(Point start, Point end) {
        boolean replace = replaceEndpoints(start.getX(), start.getY(), end.getX(), end.getY());
        if (replace) {
            Point tmp = start;
            start = end;
            end = tmp;
        }
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
        this.slope = calcSlope();
    }

    /**
     * Creates a line from endpoint coordinates and calculates slope.
     * Endpoints are swapped when needed to keep normalized order.
     *
     * @param x1 x coordinate of the first endpoint candidate
     * @param y1 y coordinate of the first endpoint candidate
     * @param x2 x coordinate of the second endpoint candidate
     * @param y2 y coordinate of the second endpoint candidate
     */
    public Line(double x1, double y1, double x2, double y2) {
        boolean replace = replaceEndpoints(x1, y1, x2, y2);
        if (replace) {
            double xTmp = x1;
            double yTmp = y1;
            x1 = x2;
            y1 = y2;
            x2 = xTmp;
            y2 = yTmp;
        }
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
        this.slope = calcSlope();
    }

    /**
     * Creates a line from a start point and a velocity.
     * The end point is calculated by applying the velocity to the start point with a large multiplier.
     * Useful for checking line intersections in the direction of the velocity.
     *
     * @param p the start point
     * @param v the velocity to apply to the start point to get the end point
     */
    public Line(Point p, Velocity v) {
        double Angle = v.getAngle();
        Velocity tmp = Velocity.fromAngleAndSpeed(Angle, Helper.MAX_LINE_LENGTH);
        Point end = tmp.applyToPoint(p);
        this(p, end);
    }

    /**
     * Determines if the endpoints should be swapped.
     * Lines are ordered by their start point x coordinate, and then by y coordinate.
     *
     * @param x1 x coordinate of the first endpoint candidate
     * @param y1 y coordinate of the first endpoint candidate
     * @param x2 x coordinate of the second endpoint candidate
     * @param y2 y coordinate of the second endpoint candidate
     * @return wether the endpoints should be swapped
     */
    private boolean replaceEndpoints(double x1, double y1, double x2, double y2) {
        if (Helper.doubleEq(x1, x2)) {
            return (y1 > y2 && !Helper.doubleEq(y1, y2));
        } else {
            return x1 > x2;
        }
    }

    /**
     * Calculates and stores the slope of this line.
     * Vertical lines get slope Double.POSITIVE_INFINITY.
     * A line that is a point (start equals end) gets slope 0.
     *
     * @return slope value (see method description)
     */
    private double calcSlope() {
        double dx = this.end.getX() - this.start.getX();
        double dy = this.end.getY() - this.start.getY();

        if (Helper.doubleEq(dx, 0) && Helper.doubleEq(dy, 0)) {
            // line is a point, we may define arbitrary, easy to handle slope value
            return 0;
        } else if (Helper.doubleEq(dx, 0)) {
            return Double.POSITIVE_INFINITY;
        } else {
            return (dy / dx);
        }
    }

    /**
     * Returns the line length.
     *
     * @return line length.
     */
    public double length() {
        return this.start.distance(this.end);
    }

    /**
     * Returns the middle point.
     *
     * @return middle point of the line
     */
    public Point middle() {
        double midx = (this.start.getX() + this.end.getX()) / 2;
        double midy = (this.start.getY() + this.end.getY()) / 2;
        return new Point(midx, midy);
    }

    /**
     * Returns a copy of the start point.
     *
     * @return start point copy
     */
    public Point start() {
        return new Point(this.start.getX(), this.start.getY());
    }

    /**
     * Returns a copy of the end point.
     *
     * @return end point copy
     */
    public Point end() {
        return new Point(this.end.getX(), this.end.getY());
    }

    /**
     * Returns the slope.
     *
     * @return line slope
     */
    public double getSlope() {
        return this.slope;
    }

    /**
     * Checks if this line intersects another line.
     *
     * @param other line to check for intersection
     * @return true if the lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other) {

        if (!isXWithinBounds(other) || !isYWithinBounds(other)) {
            return false;
        }

        if (Double.isInfinite(this.getSlope()) && Double.isInfinite(other.getSlope())) {
            return true;
        }
        if (Double.isInfinite(this.getSlope()) || Double.isInfinite(other.getSlope())) {
            return verticalIntersection(other) != null;
        }

        // assume "this" is y = a1 * x + b1 and "other" is y = a2 * x + b2
        double slopeDiff = this.getSlope() - other.getSlope(); // a1 - a2
        double yInterceptDiff = getYIntercept(other) - getYIntercept(this); // b2 - b1
        if (Helper.doubleEq(slopeDiff, 0)) {
            return Helper.doubleEq(yInterceptDiff, 0);
        }

        // (a1 - a2)x + (b1 - b2) = 0 => x = (b2 - b1) / (a1 - a2)
        double x = yInterceptDiff / slopeDiff;
        return (this.isXWithinBounds(x) && other.isXWithinBounds(x));
    }

    /**
     * Returns the y-intercept of a line (where it crosses the y-axis).
     *
     * @param line the line whose intercept is calculated
     * @return y-intercept value
     */
    private double getYIntercept(Line line) {
        return line.start.getY() - (line.start.getX() * line.getSlope());
    }

    /**
     * Checks if the x ranges of two lines overlap.
     *
     * @param other other line
     * @return true if the x projections overlap; otherwise false
     */
    public boolean isXWithinBounds(Line other) {
        double a1 = this.start.getX();
        double a2 = this.end.getX();
        double b1 = other.start.getX();
        double b2 = other.end.getX();
        return isWithinBounds(a1, a2, b1, b2);
    }

    /**
     * Checks if the y ranges of two lines overlap.
     *
     * @param other other segment
     * @return true if the y projections overlap; otherwise false
     */
    public boolean isYWithinBounds(Line other) {
        double a1 = this.start.getY();
        double a2 = this.end.getY();
        double b1 = other.start.getY();
        double b2 = other.end.getY();
        return isWithinBounds(a1, a2, b1, b2);
    }

    /**
     * Checks if two ranges overlap.
     *
     * @param a1 first endpoint of the first range
     * @param a2 second endpoint of the first range
     * @param b1 first endpoint of the second range
     * @param b2 second endpoint of the second range
     * @return true if the closed ranges overlap; otherwise false
     */
    private boolean isWithinBounds(double a1, double a2, double b1, double b2) {
        double maxA = Math.max(a1, a2);
        double minB = Math.min(b1, b2);
        if (maxA < minB && !Helper.doubleEq(maxA, minB)) {
            return false;
        }
        double minA = Math.min(a1, a2);
        double maxB = Math.max(b1, b2);
        return (maxB >= minA || Helper.doubleEq(maxB, minA));
    }

    /**
     * Checks if an x value is inside this line x range.
     *
     * @param x value to test
     * @return true if x is between the line endpoint x values
     */
    public boolean isXWithinBounds(double x) {
        double a1 = this.start.getX();
        double a2 = this.end.getX();
        return isWithinBounds(a1, a2, x);
    }

    /**
     * Checks if a y value is inside this line y range.
     *
     * @param y value to test
     * @return true if y is between the line endpoint y values
     */
    public boolean isYWithinBounds(double y) {
        double a1 = this.start.getY();
        double a2 = this.end.getY();
        return isWithinBounds(a1, a2, y);
    }

    /**
     * Checks if a value is inside a range.
     *
     * @param a1 first endpoint of the range
     * @param a2 second endpoint of the range
     * @param pt value to test
     * @return true if the value is in the closed range
     */
    private boolean isWithinBounds(double a1, double a2, double pt) {
        double max = Math.max(a1, a2);
        double min = Math.min(a1, a2);
        return (((pt <= max) && (pt >= min)) || Helper.doubleEq(pt, max) || Helper.doubleEq(pt, min));
    }


    /**
     * Returns the intersection point with another line.
     * Returns null if there is no intersection or if the intersection is not unique.
     *
     * @param other other segment
     * @return intersection point or null
     */
    public Point intersectionWith(Line other) {
        if (!isXWithinBounds(other) || !isYWithinBounds(other)) {
            return null;
        }

        if (Double.isInfinite(this.getSlope()) && Double.isInfinite(other.getSlope())) {
            return collinearLinesIntersection(other);
        }
        if (Double.isInfinite(this.getSlope()) || Double.isInfinite(other.getSlope())) {
            return verticalIntersection(other);
        }

        double slopeDiff = this.getSlope() - other.getSlope();
        double interceptDiff = getYIntercept(other) - getYIntercept(this);
        if (Helper.doubleEq(slopeDiff, 0)) {
            if (Helper.doubleEq(interceptDiff, 0)) {
                 return collinearLinesIntersection(other);
            } else {
                return null;
            }
        }
        double x = interceptDiff / slopeDiff;
        if (this.isXWithinBounds(x) && other.isXWithinBounds(x)) {
            double dx = x - this.start.getX();
            double dy = dx * this.getSlope();
            return new Point(x, dy + this.start.getY());
        } else {
            return null;
        }
    }

    /**
     * Handles the collinear case for intersectionWith.
     *
     * @param other other collinear segment
     * @return single shared point, or null if there is no single point
     */
    private Point collinearLinesIntersection(Line other) {
        boolean middleOverlap;
        if (Double.isInfinite(this.getSlope())) {
            middleOverlap = this.isYWithinBounds(other.middle().getY()) || other.isYWithinBounds(this.middle().getY());
        } else {
            middleOverlap = this.isXWithinBounds(other.middle().getX()) || other.isXWithinBounds(this.middle().getX());
        }
        if (middleOverlap) {
            if (this.end.equals(this.start)) {
                return this.start;
            } else if (other.end().equals(other.start())) {
                return other.start();
            } else {
                return null;
            }
        }

        if (this.start.equals(other.start()) || this.start.equals(other.end())) {
            return this.start;
        } else if (this.end.equals(other.start()) || this.end.equals(other.end())) {
            return this.end;
        } else {
            return null;
        }
    }

    /**
     * Handles intersection when exactly one line is vertical.
     *
     * @param other other segment
     * @return intersection point or null
     */
    private Point verticalIntersection(Line other) {

        Line nonVertical;
        Line vertical;
        if (Double.isInfinite(this.getSlope())) {
            vertical = this;
            nonVertical = other;
        } else {
            vertical = other;
            nonVertical = this;
        }
        double ptX = vertical.start.getX();

        double x0 = nonVertical.start.getX();
        double y0 = nonVertical.start.getY();
        double ptY = y0 + (ptX - x0) * nonVertical.getSlope();
        if (vertical.isYWithinBounds(ptY)) {
            return new Point(ptX, ptY);
        } else {
            return null;
        }
    }

    /**
     * Checks if two lines are equal by endpoints.
     *
     * @param other segment to compare with
     * @return true if both endpoints match (up to {@link Point#equals(Point)}), false otherwise
     */
    public boolean equals(Line other) {
        if (other == null) {
            return false;
        }
        Point a1 = this.start;
        Point a2 = this.end;
        Point b1 = other.start();
        Point b2 = other.end();
        return (a1.equals(b1) && a2.equals(b2) || a1.equals(b2) && a2.equals(b1));
    }

    /**
     * Compares this line to another line.
     *
     * @param other line to compare with
     * @return negative if this line comes before the other, positive if after, zero if equal
     */
    public int compareTo(Line other) {
        if (other == null) {
            return -1;
        }

        int startComparison = this.start.compareTo(other.start());
        if (startComparison != 0) {
            return startComparison;
        } else {
            return this.end.compareTo(other.end());
        }
    }
}