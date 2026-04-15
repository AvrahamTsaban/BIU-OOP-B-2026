import java.awt.Color;
import java.util.Arrays;

import biuoop.DrawSurface;

/****
 * A class representing a square shape that can be drawn on a DrawSurface.
 * It has an upper left corner, an edge length, and a color.
 * It also provides methods to check if a ball is inside or outside the square.
 */
public class Rectangle {
    private static final int MAX_COLLISIONS = 4;

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
     * Get the width of the square.
     * @return the width of the square
     */
    public double width() {
        return horizontalEdge;
    }

    /**
     * Get the height of the square.
     * @return the height of the square
     */
    public double height() {
        return verticalEdge;
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
      * Check if the given point is inside the square. Otherwise, returns the corresponding type of collision.
      * @param p the point to check
      * @return an array of CollisionType representing the corresponding collisions
      */
    private CollisionCase deviation(Point p) {
        boolean hasTop = false;
        boolean hasBottom = false;
        boolean hasLeft = false;
        boolean hasRight = false;
        if (p.getX() < leftX()) {
            hasLeft = true;
        }
        if (p.getX() > rightX()) {
            hasRight = true;
        }
        if (p.getY() < topY()) {
            hasTop = true;
        }
        if (p.getY() > bottomY()) {
            hasBottom = true;
        }

        return new CollisionCase(hasTop, hasBottom, hasLeft, hasRight);
    }

    /**
     * Check if the given ball is colliding with the square from the inside, and return the type of collision.
     * @param b the ball to check
     * @return the type of collision that is occurring between the ball and the square, or NONE if there is no collision
     */
    public Collision collisionFromInside(Ball b) {
        Point topLeft = new Point(b.getX() - b.getSize(), b.getY() - b.getSize());
        Point bottomRight = new Point(b.getX() + b.getSize(), b.getY() + b.getSize());
        CollisionCase collisionsTL = deviation(topLeft);
        CollisionCase collisionsBR = deviation(bottomRight);
        CollisionCase collisions = new CollisionCase(
            collisionsTL.isTop() || collisionsBR.isTop(),
            collisionsTL.isBottom() || collisionsBR.isBottom(),
            collisionsTL.isLeft() || collisionsBR.isLeft(),
            collisionsTL.isRight() || collisionsBR.isRight()
        );
        return new Collision(this, collisions);
    }

    /**
     * Check if the given point is inside the square.
     * @param p the point to check
     * @return true if the point is inside the square, false otherwise
     */
    public boolean isInside(Point p) {
        return p.getX() > leftX() && p.getX() < rightX() && p.getY() > topY() && p.getY() < bottomY();
    }

    /**
     * Check if the given ball is inside the square.
     * @param b the ball to check
     * @return true if the ball is inside the square, false otherwise
     */
    public boolean isInside(Ball b) {
        Point topLeft = new Point(b.getX() - b.getSize(), b.getY() - b.getSize());
        Point bottomRight = new Point(b.getX() + b.getSize(), b.getY() + b.getSize());
        return isInside(topLeft) && isInside(bottomRight);
    }

    /**
     * Check if the given ball is outside the square.
     * @param b the ball to check
     * @return true if the ball is outside the square, false otherwise
     */
    public boolean isOutside(Ball b) {
        return collisionFromOutside(b).isEmpty();
    }

    /**
     * Check if the given ball is colliding with the square from the outside, and return the type of collision.
     * Note that this method assumes the ball was previously outside the square.
     * @param b the ball to check
     * @return the type of collision that is occurring between the ball and the square, NONE if there is no collision
     */
    public Collision collisionFromOutside(Ball b) {
        Point c = b.getCenter();
        double r = b.getSize();
        boolean collidesTop = false;
        boolean collidesBottom = false;
        boolean collidesLeft = false;
        boolean collidesRight = false;
        boolean done = false;
        if (c.distance(upperLeft) < r) {
            collidesLeft = true;
            collidesTop = true;
            done = true;
        } else if (c.distance(new Point(rightX(), topY())) < r) {
            collidesRight = true;
            collidesTop = true;
            done = true;
        } else if (c.distance(new Point(leftX(), bottomY())) < r) {
            collidesLeft = true;
            collidesBottom = true;
            done = true;
        } else if (c.distance(new Point(rightX(), bottomY())) < r) {
            collidesRight = true;
            collidesBottom = true;
            done = true;
        }

        if (done) {
            return new Collision(this, collidesTop, collidesBottom, collidesLeft, collidesRight);
        }

        double x = c.getX();
        double y = c.getY();
        boolean rightPtInside = isInside(new Point(x + r, y));
        boolean leftPtInside = isInside(new Point(x - r, y));
        boolean topPtInside = isInside(new Point(x, y - r));
        boolean bottomPtInside = isInside(new Point(x, y + r));

        // asserts no corner collision, as they are handled above
        if (topPtInside && !bottomPtInside) {
            collidesTop = true;
        }
        if (bottomPtInside && !topPtInside) {
            collidesBottom = true;
        }
        if (leftPtInside && !rightPtInside) {
            collidesLeft = true;
        }
        if (rightPtInside && !leftPtInside) {
            collidesRight = true;
        }
        if (topPtInside && bottomPtInside) {
            collidesTop = true;
            collidesBottom = true;
            collidesRight = true;
            collidesLeft = true;
        }
        return new Collision(this, collidesTop, collidesBottom, collidesLeft, collidesRight);
    }
}