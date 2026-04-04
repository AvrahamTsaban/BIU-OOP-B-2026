/**
 * Represents a point in 2D space.
 *
 * @author Avraham Tsaban
 */
public class Point {
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
}