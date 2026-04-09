/**
 * Represents a point in 2D space.
 *
 * <p>Implementation warning: Point.equals and Point.compareTo are altered in a way that is not consistent with the
 * general contract, without keeping transitivity and overriding hashCode.
 * This is acceptable for this assignment, but should be used with caution.</p>
 *
 * @author Avraham Tsaban
 * @since 2024-06-05
 */
public class Point implements Comparable<Point> {
    private final double x;
    private final double y;

    /**
     * Creates a point from x and y values.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the distance to another point.
     *
     * @param other point to measure distance to
     * @return distance between this point and other
     */
    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Returns x.
    *
     * @return x coordinate
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns y.
     *
     * @return y coordinate
     */
    public double getY() {
        return this.y;
    }

    /**
     * Checks if this point equals another point.
     *
     * @param other point to compare with
     * @return true if both coordinates are equal (up to {@link Helper#doubleEq(double, double)})
     */
    public boolean equals(Point other) {
        if (other == null) {
            return false;
        }
        return (Helper.doubleEq(this.x, other.getX()) && Helper.doubleEq(this.y, other.getY()));
    }

    /**
     * Compares this point to another point for ordering.
     * First compares x coordinates, then y coordinates if x's are equal.
     *
     * @param other point to compare with
     * @return negative if this < other, positive if this > other, 0 if equal
     */
    public int compareTo(Point other) {
        if (other == null) {
            return -1;
        }
        if (!Helper.doubleEq(this.x, other.getX())) {
            return Double.compare(this.x, other.getX());
        } else if (Helper.doubleEq(this.y, other.getY())) {
            return 0;
        } else {
            return Double.compare(this.y, other.getY());
        }
    }
}