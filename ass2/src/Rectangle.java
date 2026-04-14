import java.awt.Color;
import biuoop.DrawSurface;

/****
 * A class representing a square shape that can be drawn on a DrawSurface.
 * It has an upper left corner, an edge length, and a color.
 * It also provides methods to check if a ball is inside or outside the square.
 */
public class Rectangle {
    private final Point upperLeft;
    private final double horizontalEdge;
    private final double verticalEdge;
    private final Color color;

    /**
     * Initialize a new square with the given upper left corner, edge length, and color.
     * @param upperLeft the upper left corner of the square
     * @param horizontalEdge the length of the horizontal side of the square
     * @param verticalEdge the length of the vertical side of the square
     * @param color the color of the square
     */
    public Rectangle(Point upperLeft, double horizontalEdge, double verticalEdge, Color color) {
        this.upperLeft = upperLeft;
        this.horizontalEdge = horizontalEdge;
        this.verticalEdge = verticalEdge;
        this.color = color;
    }

    /**
     * Get the x-coordinate of the right edge of the square.
     * @return the x-coordinate of the right edge
     */
    public double rightX() {
        return upperLeft.getX() + horizontalEdge;
    }

    /**
     * Get the y-coordinate of the bottom edge of the square.
     * @return the y-coordinate of the bottom edge
     */
    public double bottomY() {
        return upperLeft.getY() + verticalEdge;
    }

    /**
     * Get the x-coordinate of the left edge of the square.
     * @return the x-coordinate of the left edge
     */
    public double leftX() {
        return upperLeft.getX();
    }

    /**
     * Get the y-coordinate of the top edge of the square.
     * @return the y-coordinate of the top edge
     */
    public double topY() {
        return upperLeft.getY();
    }

    /**
     * Draw the square on the given surface.
     * @param surface the surface to draw the square on
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillRectangle((int) upperLeft.getX(), (int) upperLeft.getY(), (int) horizontalEdge, (int) verticalEdge);
    }

    /**
      * Check if the given point is inside the square.
      * @param p the point to check
      * @return true if the point is inside the square, false otherwise
      */
    private boolean isInside(Point p) {
        return p.getX() >= leftX()
            && p.getX() <= rightX()
            && p.getY() >= topY()
            && p.getY() <= bottomY();
    }

    /**
     * Check if the given ball is completely inside the square.
     * @param b the ball to check
     * @return true if the ball is completely inside the square, false otherwise
     */
    public boolean isInside(Ball b) {
        return isInside(new Point(b.getX() - b.getSize(), b.getY() - b.getSize()))
            && isInside(new Point(b.getX() + b.getSize(), b.getY() + b.getSize()));
    }

    /**
     * Check if the given ball is completely outside the square.
     * @param b the ball to check
     * @return true if the ball is completely outside the square, false otherwise
     */
    public boolean isOutside(Ball b) {
        Point center = b.getCenter();
        double radius = b.getSize();
        if (center.distance(upperLeft) < radius
                || center.distance(new Point(rightX(), topY())) < radius
                || center.distance(new Point(leftX(), bottomY())) < radius
                || center.distance(new Point(rightX(), bottomY())) < radius) {
            return false;
        }

        double x = center.getX();
        double y = center.getY();
        return !isInside(new Point(x - radius, y - radius))
            && !isInside(new Point(x - radius, y + radius))
            && !isInside(new Point(x + radius, y - radius))
            && !isInside(new Point(x + radius, y + radius));
    }
}