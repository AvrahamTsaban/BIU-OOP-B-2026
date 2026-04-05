/**
<<<<<<< HEAD
<<<<<<< HEAD
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
     * Calculates and stores the slope.
     * Vertical lines get slope Double.POSITIVE_INFINITY.
     * A line that is a point gets slope 0.
     *
     * @return slope value (see method description)
     */
    private double calcSlope() {
        double dx = this.end.getX() - this.start.getX();
        double dy = this.end.getY() - this.start.getY();

        if (Helper.doubleEq(dx, 0) && Helper.doubleEq(dy, 0)) {
            // line is a point, we mey define arbitrary, easy to handle slope value
            return 0;
        } else if (Helper.doubleEq(dx, 0)) {
            return Double.POSITIVE_INFINITY;
        } else {
            return (dy / dx);
=======
 * Line class represents a line segment in 2D space defined by two points:
 * start and end.
 * Constructor ensures that the start point x value <= end point x value, for simplicity.
 * It provides methods to:
 * - calculate the length of the line,
 * - find the middle point,
 * - check for intersection with another line,
 * - and check for equality with another line.
=======
 * Represents a line segment between two points.
<<<<<<< HEAD
 * Constructors keep the point with the smaller x value as start.
>>>>>>> 95e5362 (removed colouredLine class)
=======
 * Constructors normalize endpoints: smaller x first;
 * when x is equal (up to threshold), smaller y first.
>>>>>>> 3108e30 (clean, javadoc and make random doubles (not ints))
 *
 * <p>Implementation warning: Line.equals and Line.compareTo are altered in a way that is not consistent with the
 * general contract and without overriding hashCode.
 * Both are also not fully transitive (up to the comparison threshold).
 * This is acceptable for this assignment, but should be used with caution.</p>
 *
 * <p>Generally, avoid creating lines that are points (endpoints are the same).
 * Such case is treated for safety, but not recommended.</p>
 *
 * @author Avraham Tsaban
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
     * Calculates and stores the slope.
     * Vertical lines get slope Double.POSITIVE_INFINITY.
     * A line that is a point gets slope 0.
     *
     * @return slope value (see method description)
     */
    private double calcSlope() {
        double dx = this.end.getX() - this.start.getX();
        double dy = this.end.getY() - this.start.getY();

        if (Helper.doubleEq(dx, 0) && Helper.doubleEq(dy, 0)) {
            // line is a point, we mey define arbitrary, easy to handle slope value
            return 0;
        } else if (Helper.doubleEq(dx, 0)) {
            return Double.POSITIVE_INFINITY;
        } else {
<<<<<<< HEAD
            this.slope = (dy / dx);
>>>>>>> 5439265 (stage 1)
=======
            return (dy / dx);
>>>>>>> 3108e30 (clean, javadoc and make random doubles (not ints))
        }
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Returns the line length.
     *
     * @return line length.
=======
     * Get the length of the line.
     *
     * @return line length
>>>>>>> 5439265 (stage 1)
=======
     * Returns the line length.
     *
     * @return line length.
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public double length() {
        return this.start.distance(this.end);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Returns the middle point.
     *
     * @return middle point of the line
=======
     * Get the middle point of the line.
     *
     * @return the middle point of the line
>>>>>>> 5439265 (stage 1)
=======
     * Returns the middle point.
     *
     * @return middle point of the line
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public Point middle() {
        double midx = (this.start.getX() + this.end.getX()) / 2;
        double midy = (this.start.getY() + this.end.getY()) / 2;
<<<<<<< HEAD
<<<<<<< HEAD
        return new Point(midx, midy);
    }

    /**
     * Returns a copy of the start point.
<<<<<<< HEAD
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
=======
        Point mid = new Point(midx, midy);
        return mid;
=======
        return new Point(midx, midy);
>>>>>>> fee5559 (happy passover)
    }

    /**
     * Get the start point of the line.
=======
>>>>>>> 95e5362 (removed colouredLine class)
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
<<<<<<< HEAD
     * @return the slope of the line
>>>>>>> 5439265 (stage 1)
=======
     * @return line slope
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public double getSlope() {
        return this.slope;
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
     * Returns true if the lines intersect, false otherwise.
     * Intersection algorithm:
     * 1. If the x-ranges of the two segments do not overlap,
     *    intersection is impossible.
     * 2. If both lines are vertical, check if their y-ranges overlap.
     * 3. If only one line is vertical, calculate the intersection point using designated method.
     * 4. Compare slopes using their difference. If slopes are equal,
     *    check if the y-intercept is also equal.
     * 5. Otherwise, solve to find intersection x and verify it lies
     *    within both segments' x-ranges.
=======
     * Checks if this line intersects another line.
>>>>>>> 95e5362 (removed colouredLine class)
     *
     * @param other line to check for intersection
     * @return true if the lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other) {

        if (!isXWithinBounds(other) || !isYWithinBounds(other)) {
            return false;
        }

        if (Double.isInfinite(this.getSlope()) && Double.isInfinite(other.getSlope())) {
<<<<<<< HEAD
            return isYWithinBounds(other);
>>>>>>> 5439265 (stage 1)
=======
            return true;
        }
        if (Double.isInfinite(this.getSlope()) || Double.isInfinite(other.getSlope())) {
            return verticalIntersection(other) != null;
>>>>>>> 95e5362 (removed colouredLine class)
        }

        // assume "this" is y = a1 * x + b1 and "other" is y = a2 * x + b2
        double slopeDiff = this.getSlope() - other.getSlope(); // a1 - a2
<<<<<<< HEAD
<<<<<<< HEAD
        double yInterceptDiff = getYIntercept(other) - getYIntercept(this); // b2 - b1
        if (Helper.doubleEq(slopeDiff, 0)) {
            return Helper.doubleEq(yInterceptDiff, 0);
        }

        // (a1 - a2)x + (b1 - b2) = 0 => x = (b2 - b1) / (a1 - a2)
        double x = yInterceptDiff / slopeDiff;
        return (this.isXWithinBounds(x) && other.isXWithinBounds(x));
    }

    /**
     * Returns a line y-intercept.
<<<<<<< HEAD
     *
     * @param line line whose intercept is calculated
     * @return y-intercept value
     */
=======
        double YInterceptDiff = getYIntercept(other) - getYIntercept(this); // b2 - b1
=======
        double yInterceptDiff = getYIntercept(other) - getYIntercept(this); // b2 - b1
>>>>>>> fee5559 (happy passover)
        if (Helper.doubleEq(slopeDiff, 0)) {
            return Helper.doubleEq(yInterceptDiff, 0);
        }

        // (a1 - a2)x + (b1 - b2) = 0 => x = (b2 - b1) / (a1 - a2)
        double x = yInterceptDiff / slopeDiff;
        return (this.isXWithinBounds(x) && other.isXWithinBounds(x));
    }

<<<<<<< HEAD
>>>>>>> 5439265 (stage 1)
=======
    /**
     * Calculate the y-intercept of the line, which is the y value when x is 0.
=======
>>>>>>> 95e5362 (removed colouredLine class)
     *
     * @param line line whose intercept is calculated
     * @return y-intercept value
     */
>>>>>>> fee5559 (happy passover)
    private double getYIntercept(Line line) {
        return line.start.getY() - (line.start.getX() * line.getSlope());
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Checks if the x ranges of two lines overlap.
     *
     * @param other other line
     * @return true if the x projections overlap; otherwise false
=======
     * Checks if the x-ranges of the two lines overlap.
     *
     * @param other - the other line
     * @return true if the x-ranges of the two lines overlap, false otherwise.
>>>>>>> 5439265 (stage 1)
=======
     * Checks if the x ranges of two lines overlap.
     *
     * @param other other line
     * @return true if the x projections overlap; otherwise false
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public boolean isXWithinBounds(Line other) {
        double a1 = this.start.getX();
        double a2 = this.end.getX();
        double b1 = other.start.getX();
        double b2 = other.end.getX();
        return isWithinBounds(a1, a2, b1, b2);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Checks if the y ranges of two lines overlap.
     *
     * @param other other segment
     * @return true if the y projections overlap; otherwise false
=======
     * Checks if the y-ranges of the two lines overlap.
     *
     * @param other - the other line
     * @return true if the y-ranges of the two lines overlap, false otherwise.
>>>>>>> 5439265 (stage 1)
=======
     * Checks if the y ranges of two lines overlap.
     *
     * @param other other segment
     * @return true if the y projections overlap; otherwise false
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public boolean isYWithinBounds(Line other) {
        double a1 = this.start.getY();
        double a2 = this.end.getY();
        double b1 = other.start.getY();
        double b2 = other.end.getY();
        return isWithinBounds(a1, a2, b1, b2);
    }

<<<<<<< HEAD
<<<<<<< HEAD
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
<<<<<<< HEAD
=======
=======
    /**
     * Checks if two ranges overlap.
     *
     * @param a1 first endpoint of the first range
     * @param a2 second endpoint of the first range
     * @param b1 first endpoint of the second range
     * @param b2 second endpoint of the second range
     * @return true if the closed ranges overlap; otherwise false
     */
>>>>>>> fee5559 (happy passover)
    private boolean isWithinBounds(double a1, double a2, double b1, double b2) {
        double maxA = Math.max(a1, a2);
        double minB = Math.min(b1, b2);
        if (maxA < minB) {
>>>>>>> 5439265 (stage 1)
=======
>>>>>>> 69e61f8 (Seems mature)
            return false;
        }
        double minA = Math.min(a1, a2);
        double maxB = Math.max(b1, b2);
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 69e61f8 (Seems mature)
        return (maxB >= minA || Helper.doubleEq(maxB, minA));
    }

    /**
     * Checks if an x value is inside this line x range.
     *
     * @param x value to test
     * @return true if x is between the line endpoint x values
=======
        return maxB >= minA;
    }

    /**
     * Checks if an x value is inside this line x range.
     *
<<<<<<< HEAD
     * @param x - the x value to check
     * @return true if the x value is within the x-range, false otherwise
>>>>>>> 5439265 (stage 1)
=======
     * @param x value to test
     * @return true if x is between the line endpoint x values
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public boolean isXWithinBounds(double x) {
        double a1 = this.start.getX();
        double a2 = this.end.getX();
        return isWithinBounds(a1, a2, x);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Checks if a y value is inside this line y range.
     *
     * @param y value to test
     * @return true if y is between the line endpoint y values
=======
     * Checks if a given y value is within the y-range of this line.
     *
     * @param y - the y value to check
     * @return true if the y value is within the y-range, false otherwise
>>>>>>> 5439265 (stage 1)
=======
     * Checks if a y value is inside this line y range.
     *
     * @param y value to test
     * @return true if y is between the line endpoint y values
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public boolean isYWithinBounds(double y) {
        double a1 = this.start.getY();
        double a2 = this.end.getY();
        return isWithinBounds(a1, a2, y);
    }

<<<<<<< HEAD
<<<<<<< HEAD
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
<<<<<<< HEAD
<<<<<<< HEAD
        return (((pt <= max) && (pt >= min)) || Helper.doubleEq(pt, max) || Helper.doubleEq(pt, min));
=======
        return (pt - Helper.THRESHOLD <= max) && (pt + Helper.THRESHOLD >= min);
>>>>>>> 69e61f8 (Seems mature)
=======
        return (((pt <= max) && (pt >= min)) || Helper.doubleEq(pt, max) || Helper.doubleEq(pt, min));
>>>>>>> 3108e30 (clean, javadoc and make random doubles (not ints))
    }

    /**
     * Checks if this line intersects both given lines.
     *
     * @param other1 first segment
     * @param other2 second segment
     * @return true if both intersections exist
=======
=======
    /**
     * Checks if a value is inside a range.
     *
     * @param a1 first endpoint of the range
     * @param a2 second endpoint of the range
     * @param pt value to test
     * @return true if the value is in the closed range
     */
>>>>>>> fee5559 (happy passover)
    private boolean isWithinBounds(double a1, double a2, double pt) {
        double max = Math.max(a1, a2);
        double min = Math.min(a1, a2);
        return (pt <= max) && (pt >= min);
    }

    /**
     * Checks if this line intersects both given lines.
     *
<<<<<<< HEAD
     * @param other1 - the first other line to check for intersection
     * @param other2 - the second other line to check for intersection
     * @return true if the lines intersect, false otherwise
>>>>>>> 5439265 (stage 1)
=======
     * @param other1 first segment
     * @param other2 second segment
     * @return true if both intersections exist
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public boolean isIntersecting(Line other1, Line other2) {
        return this.isIntersecting(other1) && this.isIntersecting(other2);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
     * Returns the intersection point if the lines intersect,
     * and null otherwise.
     * Algorithm is the same as isIntersecting, but if the lines do intersect,
     * we calculate intersection point using "this" line's linear equation.
     * If lines are parallel, we arbitrarily return null, even if they are collinear,
     * to keep the method predictable, since in that case there are many intersection points
     * (except if they have single intersection point).
=======
     * Returns the intersection point with another line.
>>>>>>> 95e5362 (removed colouredLine class)
     *
     * Returns null if there is no intersection or if the intersection is not unique.
     *
     * @param other other segment
     * @return intersection point or null
     */
    public Point intersectionWith(Line other) {
        if (!isXWithinBounds(other) || !isYWithinBounds(other)) {
            return null;
        }

        if (Helper.doubleEq(this.getSlope(), INFINITY) || Helper.doubleEq(other.getSlope(), INFINITY)) {
>>>>>>> 5439265 (stage 1)
            return verticalIntersection(other);
        }

        double slopeDiff = this.getSlope() - other.getSlope();
        double interceptDiff = getYIntercept(other) - getYIntercept(this);
        if (Helper.doubleEq(slopeDiff, 0)) {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> fee5559 (happy passover)
            if (Helper.doubleEq(interceptDiff, 0)) {
                 return collinearLinesIntersection(other);
            } else {
                return null;
            }
<<<<<<< HEAD
=======
            return null;
>>>>>>> 5439265 (stage 1)
=======
>>>>>>> fee5559 (happy passover)
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

<<<<<<< HEAD
<<<<<<< HEAD
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
=======
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
<<<<<<< HEAD
            } else if (other.end.equals(other.start)) {
                return other.start;
>>>>>>> fee5559 (happy passover)
=======
            } else if (other.end().equals(other.start())) {
                return other.start();
>>>>>>> 69e61f8 (Seems mature)
            } else {
                return null;
            }
        }

<<<<<<< HEAD
<<<<<<< HEAD
        if (this.start.equals(other.start()) || this.start.equals(other.end())) {
            return this.start;
        } else if (this.end.equals(other.start()) || this.end.equals(other.end())) {
=======
        if (this.start.equals(other.start) || this.start.equals(other.end)) {
            return this.start;
        } else if (this.end.equals(other.start) || this.end.equals(other.end)) {
>>>>>>> fee5559 (happy passover)
=======
        if (this.start.equals(other.start()) || this.start.equals(other.end())) {
            return this.start;
        } else if (this.end.equals(other.start()) || this.end.equals(other.end())) {
>>>>>>> 69e61f8 (Seems mature)
            return this.end;
        } else {
            return null;
        }
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Handles intersection when exactly one line is vertical.
     *
     * @param other other segment
     * @return intersection point or null
     */
    private Point verticalIntersection(Line other) {

        Line nonVertical;
        Line vertical;
        if (Double.isInfinite(this.getSlope())) {
=======
    /* this method asserts isXWithinBounds(other) is true, and
     * this.getSlope() *xor* other.getSlope() is infinite. */
=======
     * Returns the intersection point if one of the lines is vertical and they intersect,
     * and null otherwise.
     * this method asserts isXWithinBounds(other) is true, and
     * this.getSlope() *xor* other.getSlope() is infinite.
     *
     * @param other - the other line to check for intersection
     * @return the intersection point if the lines intersect, null otherwise
     * */
>>>>>>> fee5559 (happy passover)
=======
     * Handles intersection when exactly one line is vertical.
     *
     * @param other other segment
     * @return intersection point or null
     */
>>>>>>> 95e5362 (removed colouredLine class)
    private Point verticalIntersection(Line other) {

        Line nonVertical;
        Line vertical;
        if (Helper.doubleEq(this.getSlope(), INFINITY)) {
>>>>>>> 5439265 (stage 1)
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
<<<<<<< HEAD
<<<<<<< HEAD
     * Checks if two lines are equal by endpoints.
     *
     * @param other segment to compare with
     * @return true if both endpoints match (up to {@link Point#equals(Point)}), false otherwise
<<<<<<< HEAD
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
=======
     * Returns true if the lines are equal, false otherwise.
     * Two lines are considered equal if their start and end points are equal,
     * regardless of their order.
=======
     * Checks if two lines are equal by endpoints.
>>>>>>> 95e5362 (removed colouredLine class)
     *
     * @param other segment to compare with
     * @return true if both endpoints match (in any order)
=======
>>>>>>> 69e61f8 (Seems mature)
     */
    public boolean equals(Line other) {
        if (other == null) {
            return false;
        }
        Point a1 = this.start;
        Point a2 = this.end;
        Point b1 = other.start();
        Point b2 = other.end();
        return (a1.equals(b1) && a2.equals(b2));
    }

<<<<<<< HEAD
>>>>>>> 5439265 (stage 1)
=======
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
>>>>>>> 95e5362 (removed colouredLine class)
}