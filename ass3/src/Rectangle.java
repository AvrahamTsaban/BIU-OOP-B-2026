/**
 * Represents a rectangle (or generalized rectangular frame) in 2D space.
 *
 * <p>Defined by its upper-left corner, horizontal and vertical edge lengths, and color.
 * Provides methods to query its boundaries, generate boundary lines, check ball containment,
 * detect collisions with balls from inside or outside, and render itself on a DrawSurface.</p>
 *
 * <p>Collision detection assumes the rectangle represents either an inner obstacle (collisions from outside)
 * or an outer boundary (collisions from inside).</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public class Rectangle {
    private final Point upperLeft;
    private final double width;
    private final double height;

    /**
     * Initialize a new rectangle with the given upper left corner, edge length, and color.
     * @param upperLeft the upper left corner of the rectangle
     * @param width the length of the horizontal side of the rectangle
     * @param height the length of the vertical side of the rectangle
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }

    /**
     * Get the upper left corner of the rectangle.
     * @return the upper left corner of the rectangle
     */
    public Point getUpperLeft() {
        return upperLeft;
    }

    /**
     * Get the width of the rectangle.
     * @return the width of the rectangle
     */
    public double getWidth() {
        return width;
    }

    /**
     * Get the height of the rectangle.
     * @return the height of the rectangle
     */
    public double getHeight() {
        return height;
    }
    
    /**
     * Get the right boundary of the rectangle (x-coordinate of the right edge).
     * @return the x-coordinate of the right edge of the rectangle
     */
    public double getRight() {
        return upperLeft.getX() + width;
    }

    /**
     * Get the left boundary of the rectangle (x-coordinate of the left edge).
     * @return the x-coordinate of the left edge of the rectangle
     */
    public double getLeft() {
        return upperLeft.getX();
    }

    /**
     * Get the top boundary of the rectangle (y-coordinate of the top edge).
     * @return the y-coordinate of the top edge of the rectangle
     */
    public double getTop() {
        return upperLeft.getY();
    }

    /**
     * Get the bottom boundary of the rectangle (y-coordinate of the bottom edge).
     * @return the y-coordinate of the bottom edge of the rectangle
     */
    public double getBottom() {
        return upperLeft.getY() + height;
    }

    /**
     * Calculate the intersection points of a given line with the edges of the rectangle.
     * @param line the line to check for intersections
     * @return a list of intersection points
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        java.util.List<Point> intersectionPoints = new java.util.ArrayList<>();
        Point upperRight = new Point(upperLeft.getX() + width, upperLeft.getY());
        Point lowerLeft = new Point(upperLeft.getX(), upperLeft.getY() + height);
        Point lowerRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);

        // Check intersection with each edge of the rectangle
        Point topIntersection = line.intersectionWith(new Line(upperLeft, upperRight));
        Point rightIntersection = line.intersectionWith(new Line(upperRight, lowerRight));
        Point bottomIntersection = line.intersectionWith(new Line(lowerLeft, lowerRight));
        Point leftIntersection = line.intersectionWith(new Line(upperLeft, lowerLeft));

        if (topIntersection != null) {
            intersectionPoints.add(topIntersection);
        }
        if (rightIntersection != null && !intersectionPoints.contains(rightIntersection)) {
            intersectionPoints.add(rightIntersection);
        }
        if (bottomIntersection != null && !intersectionPoints.contains(bottomIntersection)) {
            intersectionPoints.add(bottomIntersection);
        }
        if (leftIntersection != null && !intersectionPoints.contains(leftIntersection)) {
            intersectionPoints.add(leftIntersection);
        }

        return intersectionPoints;
    }

}