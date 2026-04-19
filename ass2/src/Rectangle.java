import java.awt.Color;
import biuoop.DrawSurface;

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
     * Get a Line representing the left edge of the square.
     * @return the line representing the left edge
     */
    public Line leftLine() {
        return new Line(new Point(leftX(), topY()), new Point(leftX(), bottomY()));
    }

    /**
     * Get a Line representing the right edge of the square.
     * @return the line representing the right edge
     */
    public Line rightLine() {
        return new Line(new Point(rightX(), topY()), new Point(rightX(), bottomY()));
    }

    /**
     * Get a Line representing the top edge of the square.
     * @return the line representing the top edge
     */
    public Line topLine() {
        return new Line(new Point(leftX(), topY()), new Point(rightX(), topY()));
    }

    /**
     * Get a Line representing the bottom edge of the square.
     * @return the line representing the bottom edge
     */
    public Line bottomLine() {
        return new Line(new Point(leftX(), bottomY()), new Point(rightX(), bottomY()));
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
     * Determines the collision direction(s) when a point deviates from inside the rectangle.
     *
     * <p>The collision direction indicates which side of the rectangle the point crosses from.
     * For example, if a point is to the left of the rectangle (p.getX() is smaller than leftX()),
     * it collides from the left side (fromLeft = true).</p>
     *
     * <p>Threshold values are not applied in this method, as it is used for determining collision directions,
     * so it should be consistent with the Ball.bounce() method,
     * which avoids using threshold values by itself (see there)</p>
     *
     * @param p the point to check
     * @return a CollisionCase describing the collision directions
     */
    private CollisionCase deviationFromInside(Point p) {
        double x = p.getX();
        double y = p.getY();
        return new CollisionCase(y > bottomY(), y < topY(), x > rightX(), x < leftX(), false);
    }

    /**
     * Check if the given ball is colliding with the square from the inside, and return the type of collision.
     * @param b the ball to check (before the move)
     * @return the type of collision that is occurring between the ball and the square, or NONE if there is no collision
     */
    public Collision collisionFromInside(Ball b) {
        b = b.predictMove();
        Point p = b.getCenter();
        Point topLeft = new Point(p.getX() - b.getSize(), p.getY() - b.getSize());
        Point bottomRight = new Point(p.getX() + b.getSize(), p.getY() + b.getSize());
        CollisionCase collisionsTL = deviationFromInside(topLeft);
        CollisionCase collisionsBR = deviationFromInside(bottomRight);
        CollisionCase collisions = new CollisionCase(collisionsTL, collisionsBR);
        return new Collision(this, collisions);
    }

    /**
     * Checks if a point is within rectangle bounds.
     *
     * @param p the point to check
     * @return true if the point is inside the rectangle, false otherwise
     */
    private boolean isInside(Point p) {
        return isInXRange(p) && isInYRange(p);
    }

    /**
     * Checks if a point's x-coordinate is within the rectangle's x-range (with threshold tolerance).
     *
     * @param p the point to check
     * @return true if the point's x is within bounds, false otherwise
     */
    private boolean isInXRange(Point p) {
        double x = p.getX();
        return x > leftX() - Helper.THRESHOLD && x < rightX() + Helper.THRESHOLD;
    }

    /**
     * Checks if a point's y-coordinate is within the rectangle's y-range (with threshold tolerance).
     *
     * @param p the point to check
     * @return true if the point's y is within bounds, false otherwise
     */
    private boolean isInYRange(Point p) {
        double y = p.getY();
        return y > topY() - Helper.THRESHOLD && y < bottomY() + Helper.THRESHOLD;
    }

    /**
     * Check if the given ball is inside the square.
     * @param b the ball to check
     * @return true if the ball is inside the square, false otherwise
     */
    public boolean isInside(Ball b) {
        Point p = b.getCenter();
        Point topLeft = new Point(p.getX() - b.getSize(), p.getY() - b.getSize());
        Point bottomRight = new Point(p.getX() + b.getSize(), p.getY() + b.getSize());
        return isInside(topLeft) && isInside(bottomRight);
    }

    /**
     * Check if the given ball is outside the square.
     * @param b the ball to check (should be the predicted position of the ball)
     * @return true if the ball is outside the square, false otherwise
     */
    public boolean isOutside(Ball b) {
        Point p = b.getCenter();
        Point topLeft = new Point(p.getX() - b.getSize(), p.getY() - b.getSize());
        Point bottomRight = new Point(p.getX() + b.getSize(), p.getY() + b.getSize());
        if (isInside(topLeft) || isInside(bottomRight)) {
            return false;
        } else {
            return !cornerTouch(b);
        }
    }

    /**
     * Checks if a ball is touching (within threshold) any corner of the rectangle.
     *
     * @param b the ball to check
     * @return true if the ball is touching a corner, false otherwise
     */
    private boolean cornerTouch(Ball b) {
        Point c = b.getCenter();
        double adjustedR = b.getSize() + Helper.THRESHOLD;
        if (c.distance(upperLeft) < adjustedR) {
            return true;
        }
        if (c.distance(new Point(rightX(), topY())) < adjustedR) {
            return true;
        }
        if (c.distance(new Point(leftX(), bottomY())) < adjustedR) {
            return true;
        }
        if (c.distance(new Point(rightX(), bottomY())) < adjustedR) {
            return true;
        }
        return false;
    }

    /**
     * Check if the given ball is colliding with the square from the outside, and return the type of collision.
     *
     * <p>If collision is with the corner of the rectangle, the returned collision is wrapping a ball,
     * containing the new position after step and reflected velocity.
     * If no collision occurs, returns a collision with type NONE.</p>
     *
     * @param b the ball to check (before the move)
     * @return collision between the ball and the square (may be ball wrapperor none if needed)
     */
    public Collision collisionFromOutside(Ball b) {
        Velocity v = b.getVelocity();
        Point p = b.getCenter();
        double r = b.getSize();
        Point collisionPtFromR = null;
        Point collisionPtFromL = null;
        Point collisionPtFromT = null;
        Point collisionPtFromB = null;
        Point leftmost = new Point(p.getX() - r, p.getY());
        Point rightmost = new Point(p.getX() + r, p.getY());
        Point uppermost = new Point(p.getX(), p.getY() - r);
        Point lowermost = new Point(p.getX(), p.getY() + r);
        if (v.getDx() < 0) {
            Line vector = new Line(leftmost, v);
            Point intersection = this.rightLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(leftmost) <= v.getSpeed() + Helper.THRESHOLD) {
                collisionPtFromR = intersection;
            }
        } else if (v.getDx() > 0) {
            Line vector = new Line(rightmost, v);
            Point intersection = this.leftLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(rightmost) <= v.getSpeed() + Helper.THRESHOLD) {
                collisionPtFromL = intersection;
            }
        }
        if (v.getDy() < 0) {
            Line vector = new Line(uppermost, v);
            Point intersection = this.bottomLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(uppermost) <= v.getSpeed() + Helper.THRESHOLD) {
                collisionPtFromB = intersection;
            }
        } else if (v.getDy() > 0) {
            Line vector = new Line(lowermost, v);
            Point intersection = this.topLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(lowermost) <= v.getSpeed() + Helper.THRESHOLD) {
                collisionPtFromT = intersection;
            }
        }

        Point[] roots = new Point[] {leftmost, rightmost, lowermost, uppermost};
        Point[] targets = new Point[] {collisionPtFromR, collisionPtFromL, collisionPtFromT, collisionPtFromB};
        Point collision = nearestCollision(roots, targets);
        if (collision == null) {
            Ball colliding = CornerCalc.calc(this, b);
            if (colliding == null) {
                return Collision.none();
            } else {
                return new Collision(colliding);
            }
        }

        if (collision.equals(collisionPtFromR)) {
            return new Collision(this, false, false, true, false, true);
        } else if (collision.equals(collisionPtFromL)) {
            return new Collision(this, false, false, false, true, true);
        } else if (collision.equals(collisionPtFromT)) {
            return new Collision(this, false, true, false, false, true);
        } else if (collision.equals(collisionPtFromB)) {
            return new Collision(this, true, false, false, false, true);
        }
        Ball colliding = CornerCalc.calc(this, b);
        if (colliding == null) {
            return Collision.none();
        } else {
            return new Collision(colliding);
        }
    }


    /**
     * Helper method to find the nearest collision point from an array of potential collision points.
     * @param roots the original points representing the edges of the ball in the direction of movement
     * @param targets the potential collision points, ordered corresponding to each root
     * @return the nearest collision point from the targets array, or null if no valid collision points are found
     */
    private Point nearestCollision(Point[] roots, Point[] targets) {
        double minDistance = Double.POSITIVE_INFINITY;
        Point closestTarget = null;
        if (roots == null || targets == null || roots.length != targets.length) {
            return null;
        }
        for (int i = 0; i < roots.length; i++) {
            if (roots[i] == null || targets[i] == null) {
                continue;
            }
            double distance = roots[i].distance(targets[i]);
            if (distance < minDistance) {
                minDistance = distance;
                closestTarget = targets[i];
            }
        }
        return closestTarget;
    }
}