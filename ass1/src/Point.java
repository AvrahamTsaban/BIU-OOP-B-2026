/**
 * Point class represents a point in 2D space with x and y coordinates.
 * It provides methods to:
 * - calculate the distance to another point,
 * - check for equality with another point,
 * - and get the x and y values of the point.
 *
 * @author Avraham Tsaban
 */
public class Point {
    private double x;
    private double y;

    /**
     * Constructor for Point class.
     *
     * @param x - the x value of the point
     * @param y - the y value of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate the distance between this point and another point.
     *
     * @param other - the other point to calculate the distance to
     *
     * @return the distance between the two points
     */
    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Check if this point is equal to another point.
     *
     * @param other - the other point to compare to
     *
     * @return true if the points are equal, false otherwise
     */
    public boolean equals(Point other) {
        return (Helper.doubleEq(this.x, other.getX()) && Helper.doubleEq(this.y, other.getY()));
    }

    /**
     * Return the x value of this point.
     *
     * @return the x value
     */
    public double getX() {
        return this.x;
    }

    /**
     * Return the y value of this point.
     *
     * @return the y value
     */
    public double getY() {
        return this.y;
    }
}