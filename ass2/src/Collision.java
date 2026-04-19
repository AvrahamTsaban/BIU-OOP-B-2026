/**
 * Represents a collision event of a ball with a rectangle.
 *
 * <p>Holds collision boundary coordinates organized by approach direction (left, right, top, bottom).
 * A direction with no collision is stored as a sentinel value (non-finite double: positive/negative infinity).
 * May also hold a reference to a ball representing a corner collision from outside,
 * which should contain the new position after bounce and reflected velocity.</p>
 *
 * <p><strong>Best practice:</strong> If possible, avoid merging collisions that contain ball references
 * with collisions that do not (see {@link #Collision(Collision, Collision)}) on dense canvas.
 * Code is generally safe, but methods are not completely adjusted and tested for handling many small objects,
 * which may lead to inconsistent behavior.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public class Collision {
    private final Double fromRightX;
    private final Double fromLeftX;
    private final Double fromTopY;
    private final Double fromBottomY;
    private final Ball collidingBall;

    /**
     * Creates a collision from point and type.
     * Relevant directions get the matching boundary coordinate.
     * Other directions get infinite values adjusted for comparison (see Collision(Collision, Collision) constructor).
     * @param r the rectangle involved in the collision
     * @param fromBottom true if there is a collision from the bottom, false otherwise
     * @param fromTop true if there is a collision from the top, false otherwise
     * @param fromRight true if there is a collision from the right, false otherwise
     * @param fromLeft true if there is a collision from the left, false otherwise
     * @param outside true if the collision is from the outside, false otherwise
     */
    public Collision(Rectangle r,
            boolean fromBottom, boolean fromTop, boolean fromRight, boolean fromLeft, boolean outside) {
        if (fromBottom) {
            this.fromBottomY = outside ? r.bottomY() : r.topY();
        } else {
            this.fromBottomY = Double.NEGATIVE_INFINITY;
        }
        if (fromTop) {
            this.fromTopY = outside ? r.topY() : r.bottomY();
        } else {
            this.fromTopY = Double.POSITIVE_INFINITY;
        }
        if (fromRight) {
            this.fromRightX = outside ? r.rightX() : r.leftX();
        } else {
            this.fromRightX = Double.NEGATIVE_INFINITY;
        }
        if (fromLeft) {
            this.fromLeftX = outside ? r.leftX() : r.rightX();
        } else {
            this.fromLeftX = Double.POSITIVE_INFINITY;
        }
        this.collidingBall = null;
    }

    /**
     * Creates a collision from a rectangle and a collision case.
     * @param r the rectangle involved in the collision
     * @param c the collision case representing which directions are colliding
     */
    public Collision(Rectangle r, CollisionCase c) {
        this(r, c.isFromBottom(), c.isFromTop(), c.isFromRight(), c.isFromLeft(), c.isFromOutside());
    }

    /**
     * Creates a collision representing a collision with the corner of the rectangle from outside.
     * The ball should contain the new position after step and reflected velocity.
     * @param collidingBall the ball representing the collision with the corner of the rectangle from outside
     */
    public Collision(Ball collidingBall) {
        this.fromRightX = Double.NEGATIVE_INFINITY;
        this.fromLeftX = Double.POSITIVE_INFINITY;
        this.fromTopY = Double.POSITIVE_INFINITY;
        this.fromBottomY = Double.NEGATIVE_INFINITY;
        this.collidingBall = collidingBall;
    }

    /**
     * Creates a collision from two collisions.
     * The new collision has the most demanding coordinates of the two collisions for each direction.
     * @param first the first collision
     * @param second the second collision
     */
    public Collision(Collision first, Collision second) {
        if (first == null) {
            this.fromRightX = second.fromRightX;
            this.fromLeftX = second.fromLeftX;
            this.fromTopY = second.fromTopY;
            this.fromBottomY = second.fromBottomY;
            this.collidingBall = second.collidingBall;
            return;
        }
        if (second == null) {
            this.fromRightX = first.fromRightX;
            this.fromLeftX = first.fromLeftX;
            this.fromTopY = first.fromTopY;
            this.fromBottomY = first.fromBottomY;
            this.collidingBall = first.collidingBall;
            return;
        }

        Ball outCorner = null;
        Collision regular = null;
        if (first.isOutCorner()) {
            outCorner = first.collidingBall;
            regular = second;
        } else if (second.isOutCorner()) {
            outCorner = second.collidingBall;
            regular = first;
        }
        if (outCorner != null) {
            this.collidingBall = mergeBallWithRegular(outCorner, regular);
            this.fromRightX = Double.NEGATIVE_INFINITY;
            this.fromLeftX = Double.POSITIVE_INFINITY;
            this.fromTopY = Double.POSITIVE_INFINITY;
            this.fromBottomY = Double.NEGATIVE_INFINITY;
            return;
        }
        this.fromRightX = Math.max(first.fromRightX, second.fromRightX);
        this.fromLeftX = Math.min(first.fromLeftX, second.fromLeftX);
        this.fromTopY = Math.min(first.fromTopY, second.fromTopY);
        this.fromBottomY = Math.max(first.fromBottomY, second.fromBottomY);
        this.collidingBall = null;
    }

    private Ball mergeBallWithRegular(Ball outCorner, Collision regular) {
        int size = outCorner.getSize();
        Point center = outCorner.getCenter();
        double newX = Math.min(center.getX(), regular.fromLeftX - size);
        newX = Math.max(newX, regular.fromRightX + size);
        double newY = Math.min(center.getY(), regular.fromTopY - size);
        newY = Math.max(newY, regular.fromBottomY + size);
        Point newCenter = new Point(newX, newY);
        Ball toGet = new Ball(newCenter, size, outCorner.getColor());
        toGet.setVelocity(outCorner.getVelocity());
        return toGet;
    }

    /**
     * Creates an empty collision with no collision directions.
     * @return a new collision representing no collision
     */
    public static Collision none() {
        return new Collision(Helper.SCREEN, false, false, false, false, false);
    }

    /**
     * Merges multiple collisions into one collision.
     * The new collision has the most demanding coordinates of all the collisions for each direction.
     * @param collisions the collisions to merge
     * @return a new collision representing the merged result of all the given collisions
     */
    public static Collision mergeMultipleCollisions(Collision[] collisions) {
        if (collisions == null || collisions.length == 0) {
            return Collision.none();
        }
        if (collisions.length == 1) {
            return collisions[0];
        }
        Collision mergedCollision = new Collision(collisions[0], collisions[1]);
        for (int i = 2; i < collisions.length; i++) {
            mergedCollision = new Collision(mergedCollision, collisions[i]);
        }
        return mergedCollision;
    }

    /**
     * Returns the x boundary used for collisions from the right.
     * @return right x or NaN
     */
    public Double getRight() {
        return fromRightX;
    }

    /**
     * Returns the x boundary used for collisions from the left.
     * @return left x or NaN
     */
    public Double getLeft() {
        return fromLeftX;
    }

    /**
     * Returns the y boundary used for collisions from the top.
     * @return top y or NaN
     */
    public Double getTop() {
        return fromTopY;
    }

    /**
     * Returns the y boundary used for collisions from the bottom.
     * @return bottom y or NaN
     */
    public Double getBottom() {
        return fromBottomY;
    }

    /**
     * Checks if a collision from the right exists.
     * @return true when right value is not finite
     */
    public boolean isFromRight() {
        return Double.isFinite(fromRightX);
    }

    /**
     * Checks if a collision from the left exists.
     * @return true when left value is not finite
     */
    public boolean isFromLeft() {
        return Double.isFinite(fromLeftX);
    }

    /**
     * Checks if a collision from the top exists.
     * @return true when top value is not finite
     */
    public boolean isFromTop() {
        return Double.isFinite(fromTopY);
    }

    /**
     * Checks if a collision from the bottom exists.
     * @return true when bottom value is not finite
     */
    public boolean isFromBottom() {
        return Double.isFinite(fromBottomY);
    }

    /**
     * Checks if there are no collision directions (actually represents a move with no collisions).
     * @return true when all directions are missing
     */
    public boolean isEmpty() {
        return !isFromRight() && !isFromLeft() && !isFromTop() && !isFromBottom() && !isOutCorner();
    }

    /**
     * Checks if there is a is a ball reference,
     * which represents a collision with the corner of the rectangle from outside.
     * If exists, the ball should contain the new position after step and reflected velocity.
     * @return true when there are collisions from both horizontal and vertical directions
     */
    public boolean isOutCorner() {
        return this.collidingBall != null;
    }

    /**
     * Gets the ball reference representing a collision with the corner of the rectangle from outside.
     * <strong>Implementation Note:</strong> Should only be used if isOutCorner() is true.
     * @return the ball representing the collision with the corner of the rectangle from outside
     */
    public Ball getCollidingBall() {
        return this.collidingBall;
    }
}
