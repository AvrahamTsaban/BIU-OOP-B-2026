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
     * Initialize a new rectangle with the given upper left corner, edge length, and color.
     * @param upperLeft the upper left corner of the rectangle
     * @param horizontalEdge the length of the horizontal side of the rectangle
     * @param verticalEdge the length of the vertical side of the rectangle
     * @param color the color of the rectangle
     */
    public Rectangle(Point upperLeft, double horizontalEdge, double verticalEdge, Color color) {
        this.upperLeft = upperLeft;
        this.horizontalEdge = horizontalEdge;
        this.verticalEdge = verticalEdge;
        this.color = color;
    }

    /**
     * Get the x-coordinate of the right edge of the rectangle.
     * @return the x-coordinate of the right edge
     */
    public double rightX() {
        return upperLeft.getX() + horizontalEdge;
    }

    /**
     * Get the y-coordinate of the bottom edge of the rectangle.
     * @return the y-coordinate of the bottom edge
     */
    public double bottomY() {
        return upperLeft.getY() + verticalEdge;
    }

    /**
     * Get the x-coordinate of the left edge of the rectangle.
     * @return the x-coordinate of the left edge
     */
    public double leftX() {
        return upperLeft.getX();
    }

    /**
     * Get the y-coordinate of the top edge of the rectangle.
     * @return the y-coordinate of the top edge
     */
    public double topY() {
        return upperLeft.getY();
    }

    /**
     * Get the width of the rectangle.
     * @return the width of the rectangle
     */
    public double width() {
        return horizontalEdge;
    }

    /**
     * Get the height of the rectangle.
     * @return the height of the rectangle
     */
    public double height() {
        return verticalEdge;
    }

    /**
     * Get a Line representing the left edge of the rectangle.
     * @return the line representing the left edge
     */
    public Line leftLine() {
        return new Line(new Point(leftX(), topY()), new Point(leftX(), bottomY()));
    }

    /**
     * Get a Line representing the right edge of the rectangle.
     * @return the line representing the right edge
     */
    public Line rightLine() {
        return new Line(new Point(rightX(), topY()), new Point(rightX(), bottomY()));
    }

    /**
     * Get a Line representing the top edge of the rectangle.
     * @return the line representing the top edge
     */
    public Line topLine() {
        return new Line(new Point(leftX(), topY()), new Point(rightX(), topY()));
    }

    /**
     * Get a Line representing the bottom edge of the rectangle.
     * @return the line representing the bottom edge
     */
    public Line bottomLine() {
        return new Line(new Point(leftX(), bottomY()), new Point(rightX(), bottomY()));
    }

    /**
     * Get the center point of the rectangle.
     * @return the center point of the rectangle
     */
    public Point getCenter() {
        return new Point(upperLeft.getX() + horizontalEdge / 2, upperLeft.getY() + verticalEdge / 2);
    }

    /**
     * Draw the rectangle on the given surface.
     * @param surface the surface to draw the rectangle on
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillRectangle((int) upperLeft.getX(), (int) upperLeft.getY(), (int) horizontalEdge, (int) verticalEdge);
    }

    /**
     * Get the maximum radius of a ball that can fit inside the rectangle without overlapping the edges.
     * @return the maximum radius of a ball that can fit inside the rectangle
     */
    public int getMaxRadius() {
        return (int) Math.min(horizontalEdge, verticalEdge) / 2;
    }

    /**
     * Check if the given ball is inside the rectangle.
     * @param b the ball to check
     * @return true if the ball is inside the rectangle, false otherwise
     */
    public boolean isInside(Ball b) {
        Point p = b.getCenter();
        double x = p.getX();
        double y = p.getY();
        double r = b.getSize();
        // Expand acceptance slightly to avoid treating a legal boundary position as outside due to FP drift.
        if (x - r < leftX() - Helper.THRESHOLD || x + r > rightX() + Helper.THRESHOLD) {
            return false;
        }
        if (y - r < topY() - Helper.THRESHOLD || y + r > bottomY() + Helper.THRESHOLD) {
            return false;
        }
        return true;
    }

    /**
     * Check if the given ball is outside the rectangle.
     * @param b the ball to check (should be the predicted position of the ball)
     * @return true if the ball is outside the rectangle, false otherwise
     */
    public boolean isOutside(Ball b) {
        Point c = b.getCenter();
        // get the nearest point on the rectangle to the center of the ball
        double nearestX = Math.max(leftX(), Math.min(c.getX(), rightX()));
        double nearestY = Math.max(topY(), Math.min(c.getY(), bottomY()));
        // check if the nearest point is inside the ball
        double dx = c.getX() - nearestX;
        double dy = c.getY() - nearestY;
        double adjustedR = b.getSize() + Helper.THRESHOLD;
        return dx * dx + dy * dy > adjustedR * adjustedR;
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
        return new CollisionCase(y > bottomY(), y < topY(), x < leftX(), x > rightX(), false);
    }

    /**
     * Check if the given ball is colliding with the rectangle from the inside, and return the type of collision.
     * @param b the ball to check (before the move)
     * @param remainingStep the fraction of the step to check for collision
     * @return the type of collision that is between the ball and the rectangle, or NONE if there is no collision
     */
    public PartialStep collisionFromInside(Ball b, double remainingStep) {
        if (!isInside(b) || remainingStep <= Helper.THRESHOLD) {
            return PartialStep.emptyStep(b);
        }

        Ball predictedBall = b.predictMove(remainingStep);
        Point p = predictedBall.getCenter();
        Point topLeft = new Point(p.getX() - predictedBall.getSize(), p.getY() - predictedBall.getSize());
        Point bottomRight = new Point(p.getX() + predictedBall.getSize(), p.getY() + predictedBall.getSize());
        CollisionCase collisionsTL = deviationFromInside(topLeft);
        CollisionCase collisionsBR = deviationFromInside(bottomRight);
        CollisionCase collisions = new CollisionCase(collisionsTL, collisionsBR);
        return new PartialStep(this, b, collisions, remainingStep);
    }

    /**
     * Check if the given ball is colliding with the rectangle from the outside, and return the type of collision.
     *
     * <p>If collision is with the corner of the rectangle, the returned collision is wrapping a ball,
     * containing the new position after step and reflected velocity.
     * If no collision occurs, returns a collision with type NONE.</p>
     *
     * @param b the ball to check (before the move)
     * @param remainingStep the fraction of the step to check for collision
     * @return collision between the ball and the rectangle (may be ball wrapperor none if needed)
     */
    public PartialStep collisionFromOutside(Ball b, double remainingStep) {
        if (!isOutside(b) || remainingStep <= Helper.THRESHOLD) {
            return PartialStep.emptyStep(b);
        }

        Velocity v = b.getVelocity();
        double remainingDist = v.getSpeed() * remainingStep;
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

        int collisionCount = 0;
        /* Collisions amount shall be checked to determine if there is a corner collision.
        Hence, only collisions of appropriate direction are checked, based on the velocity of the ball. */
        if (v.getDx() < 0) {
            Line vector = new Line(leftmost, v);
            Point intersection = this.rightLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(leftmost) <= remainingDist + Helper.THRESHOLD) {
                collisionPtFromR = intersection;
                collisionCount++;
            }
        } else if (v.getDx() > 0) {
            Line vector = new Line(rightmost, v);
            Point intersection = this.leftLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(rightmost) <= remainingDist + Helper.THRESHOLD) {
                collisionPtFromL = intersection;
                collisionCount++;
            }
        }
        if (v.getDy() < 0) {
            Line vector = new Line(uppermost, v);
            Point intersection = this.bottomLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(uppermost) <= remainingDist + Helper.THRESHOLD) {
                collisionPtFromB = intersection;
                collisionCount++;
            }
        } else if (v.getDy() > 0) {
            Line vector = new Line(lowermost, v);
            Point intersection = this.topLine().intersectionWith(vector);
            if (intersection != null && intersection.distance(lowermost) <= remainingDist + Helper.THRESHOLD) {
                collisionPtFromT = intersection;
                collisionCount++;
            }
        }

        if (collisionCount != 1) {
            // no collision with edges, check for corner collision
            Ball colliding = CornerCalc.calc(this, b, remainingStep);
            if (colliding != null) {
                return new PartialStep(b, colliding, remainingStep);
            }
        }
        Point[] roots = new Point[] {leftmost, rightmost, lowermost, uppermost};
        Point[] targets = new Point[] {collisionPtFromR, collisionPtFromL, collisionPtFromT, collisionPtFromB};
        Point collision = nearestCollision(roots, targets);
        if (collision == null) {
            // dummy point to avoid null pointer errors, will not be equal to any valid collision point
            collision = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        } else if (collision.equals(collisionPtFromR)) {
            CollisionCase caseR = new CollisionCase(false, false, true, false, true);
            return new PartialStep(this, b, caseR, remainingStep);
        } else if (collision.equals(collisionPtFromL)) {
            CollisionCase caseL = new CollisionCase(false, false, false, true, true);
            return new PartialStep(this, b, caseL, remainingStep);
        } else if (collision.equals(collisionPtFromT)) {
            CollisionCase caseT = new CollisionCase(true, false, false, false, true);
            return new PartialStep(this, b, caseT, remainingStep);
        } else if (collision.equals(collisionPtFromB)) {
            CollisionCase caseB = new CollisionCase(false, true, false, false, true);
            return new PartialStep(this, b, caseB, remainingStep);
        }

        Ball colliding = CornerCalc.calc(this, b, remainingStep);
        return colliding == null ? PartialStep.maxStep(b, remainingStep) : new PartialStep(b, colliding, remainingStep);
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