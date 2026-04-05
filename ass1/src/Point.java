/**
<<<<<<< HEAD
<<<<<<< HEAD
 * Represents a point in 2D space.
 *
 * <p>Implementation warning: Point.equals and Point.compareTo are altered in a way that is not consistent with the
 * general contract, without keeping transitivity and overriding hashCode.
 * This is acceptable for this assignment, but should be used with caution.</p>
 *
<<<<<<< HEAD
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
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
=======
 * Point class represents a point in 2D space with x and y coordinates.
 * It provides methods to:
 * - calculate the distance to another point,
 * - check for equality with another point,
 * - and get the x and y values of the point.
=======
 * Represents a point in 2D space.
>>>>>>> 95e5362 (removed colouredLine class)
 *
=======
>>>>>>> 69e61f8 (Seems mature)
 * @author Avraham Tsaban
 */
public class Point implements Comparable<Point> {
    private final double x;
    private final double y;

    /**
     * Creates a point from x and y values.
     *
<<<<<<< HEAD
     * @param x - the x value of the point
     * @param y - the y value of the point
>>>>>>> 5439265 (stage 1)
=======
     * @param x x coordinate
     * @param y y coordinate
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Calculates the distance to another point.
     *
     * @param other point to measure distance to
     * @return distance between this point and other
=======
     * Calculate the distance between this point and another point.
     *
     * @param other - the other point to calculate the distance to
     *
     * @return the distance between the two points
>>>>>>> 5439265 (stage 1)
=======
     * Calculates the distance to another point.
     *
     * @param other point to measure distance to
     * @return distance between this point and other
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
     * Returns x.
    *
     * @return x coordinate
=======
     * Check if this point is equal to another point.
=======
     * Checks if this point equals another point.
>>>>>>> 95e5362 (removed colouredLine class)
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
<<<<<<< HEAD
     * @return the x value
>>>>>>> 5439265 (stage 1)
=======
     * @return x coordinate
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public double getX() {
        return this.x;
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Returns y.
     *
     * @return y coordinate
=======
     * Return the y value of this point.
     *
     * @return the y value
>>>>>>> 5439265 (stage 1)
=======
     * Returns y.
     *
     * @return y coordinate
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public double getY() {
        return this.y;
    }
<<<<<<< HEAD

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
=======
     * Returns x.
    *
     * @return x coordinate
>>>>>>> 69e61f8 (Seems mature)
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
<<<<<<< HEAD
=======
>>>>>>> 5439265 (stage 1)
=======

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
>>>>>>> 69e61f8 (Seems mature)
}