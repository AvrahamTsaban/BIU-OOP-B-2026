/**
 * Line class represents a line segment in 2D space defined by two points:
 * start and end.
 * It provides methods to:
 * - calculate the length of the line,
 * - find the middle point,
 * - check for intersection with another line,
 * - and check for equality with another line.
 *
 * @author Avraham Tsaban
 */
public class Line {
    private final Point start;
    private final Point end;
    private double slope;

    /**
     * Constructor which takes two Point objects as parameters.
     *
     * @param start - the starting point of the line
     * @param end - the ending point of the line
     */
    public Line(Point start, Point end) {
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
        calcSlope();
    }

    /**
     * Constructor which takes four double values as parameters, representing
     * x and y coordinates of the start and end points of the line.
     *
     * @param x1 - x-coordinate of the start point
     * @param y1 - y-coordinate of the start point
     * @param x2 - x-coordinate of the end point
     * @param y2 - y-coordinate of the end point
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
        calcSlope();
    }

    private void calcSlope() {
        double dx = this.end.getX() - this.start.getX();
        double dy = this.end.getY() - this.start.getY();
        
        if (Helper.doubleEq(dx, 0)) {
            this.slope = Double.POSITIVE_INFINITY;
        } else {
            this.slope = (dy / dx);
        }
    }

    /**
     * Get the length of the line.
     *
     * @return line length
     */
    public double length() {
        return this.start.distance(this.end);
    }

    /**
     * Get the middle point of the line.
     *
     * @return the middle point of the line
     */
    public Point middle() {
        double midx = (this.start.getX() + this.end.getX()) / 2;
        double midy = (this.start.getY() + this.end.getY()) / 2;
        Point mid = new Point(midx, midy);
        return mid;
    }

    /**
     * Get the start point of the line.
     *
     * @return the start point of the line
     */
    public Point start() {
        Point startCpy = new Point(this.start.getX(), this.start.getY());
        return startCpy;
    }

    /**
     * Get the end point of the line.
     *
     * @return the end point of the line
     */
    public Point end() {
        Point endCpy = new Point(this.end.getX(), this.end.getY());
        return endCpy;
    }

    /**
     * Get the slope of the line.
     *
     * @return the slope of the line
     */
    public double getSlope() {
        return this.slope;
    }

    /**
     * Returns true if the lines intersect, false otherwise.
     * Intersection algorithm:
     * 1. If the x-ranges of the two segments do not overlap,
     *    intersection is impossible.
     * 2. Compare slopes using their difference. If slopes are equal,
     *    check if the y-intercept is also equal.
     * 4. Otherwise, solve to find intersection x and verify it lies
     *    within both segments' x-ranges.
     *
     * @param other - the other line to check for intersection
     * @return true if the lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other) {
        
        if (!isXWithinBounds(other)) {
            return false;
        }
        
        if (Double.isInfinite(this.getSlope()) && Double.isInfinite(other.getSlope())) {
            return isYWithinBounds(other);
        }
        if (Double.isInfinite(this.getSlope()) || Double.isInfinite(other.getSlope())) {
            return verticalIntersection(other) != null;
        }

        // assume "this" is y = a1 * x + b1 and "other" is y = a2 * x + b2
        double slopeDiff = this.getSlope() - other.getSlope(); // a1 - a2
        double YInterceptDiff = getYIntercept(other) - getYIntercept(this); // b2 - b1
        if (Helper.doubleEq(slopeDiff, 0)) {
            return Helper.doubleEq(YInterceptDiff, 0);
        }

        // (a1 - a2)x + (b1 - b2) = 0 => x = (b2 - b1) / (a1 - a2)
        double x = YInterceptDiff / slopeDiff; 
        return (this.isXWithinBounds(x) && other.isXWithinBounds(x));
    }

    private double getYIntercept(Line line) {
        return line.start.getY() - (line.start.getX() * line.getSlope());
    }

    /**
     * Checks if the x-ranges of the two lines overlap.
     * 
     * @param other - the other line
     * @return true if the x-ranges of the two lines overlap, false otherwise.
     */
    public boolean isXWithinBounds(Line other) {
        double a1 = this.start.getX();
        double a2 = this.end.getX();
        double b1 = other.start.getX();
        double b2 = other.end.getX();
        return isWithinBounds(a1, a2, b1, b2);
    }

    /**
     * Checks if the y-ranges of the two lines overlap.
     *
     * @param other - the other line
     * @return true if the y-ranges of the two lines overlap, false otherwise.
     */
    public boolean isYWithinBounds(Line other) {
        double a1 = this.start.getY();
        double a2 = this.end.getY();
        double b1 = other.start.getY();
        double b2 = other.end.getY();
        return isWithinBounds(a1, a2, b1, b2);
    }

    private boolean isWithinBounds(double a1, double a2, double b1, double b2) {
        double maxA = Math.max(a1, a2);
        double minB = Math.min(b1, b2);
        if (maxA < minB) {
            return false;
        }
        double minA = Math.min(a1, a2);
        double maxB = Math.max(b1, b2);
        return maxB >= minA;
    }

    /**
     * Checks if a given x value is within the x-range of this line.
     *
     * @param x - the x value to check
     * @return true if the x value is within the x-range, false otherwise
     */
    public boolean isXWithinBounds(double x) {
        double a1 = this.start.getX();
        double a2 = this.end.getX();
        return isWithinBounds(a1, a2, x);
    }

    /**
     * Checks if a given y value is within the y-range of this line.
     *
     * @param y - the y value to check
     * @return true if the y value is within the y-range, false otherwise
     */
    public boolean isYWithinBounds(double y) {
        double a1 = this.start.getY();
        double a2 = this.end.getY();
        return isWithinBounds(a1, a2, y);
    }

    private boolean isWithinBounds(double a1, double a2, double pt) {
        double max = Math.max(a1, a2);
        double min = Math.min(a1, a2);
        return (pt <= max) && (pt >= min);
    }

    /**
     * Returns true if this line intersects with the other two lines, false otherwise.
     *
     * @param other1 - the first other line to check for intersection
     * @param other2 - the second other line to check for intersection
     * @return true if the lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other1, Line other2) {
        return this.isIntersecting(other1) && this.isIntersecting(other2);
    }

    /**
     * Returns the intersection point if the lines intersect,
     * and null otherwise.
     * Algorithm is the same as isIntersecting, but if the lines do intersect,
     * we calculate intersection point using "this" line's linear equation.
     * If lines are parallel, we arbitrarily return null, 
     * even if they are collinear, to keep the method predictable,
     * since in that case there are many intersection points.
     *
     * @param other - the other line to check for intersection
     * @return the intersection point if the lines intersect, null otherwise
     */
    public Point intersectionWith(Line other) {
        if (!isXWithinBounds(other)) {
            return null;
        }

        if (Double.isInfinite(this.getSlope()) && Double.isInfinite(other.getSlope())) {
            return null;
        }
        if (Double.isInfinite(this.getSlope()) || Double.isInfinite(other.getSlope())) {
            return verticalIntersection(other);
        }

        double slopeDiff = this.getSlope() - other.getSlope();
        double interceptDiff = getYIntercept(other) - getYIntercept(this);
        if (Helper.doubleEq(slopeDiff, 0)) {
            return null;
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

    /* this method asserts isXWithinBounds(other) is true, and
     * this.getSlope() *xor* other.getSlope() is infinite. */
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
     * Returns true if the lines are equal, false otherwise.
     * Two lines are considered equal if their start and end points are equal,
     * regardless of their order.
     *
     * @param other - the other line to compare with
     * @return true if the lines are equal, false otherwise
     */
    public boolean equals(Line other) {
        Point a1 = this.start;
        Point a2 = this.end;
        Point b1 = other.start;
        Point b2 = other.end;
        return (a1.equals(b1) && a2.equals(b2)) || (a1.equals(b2) && a2.equals(b1));
    }

}