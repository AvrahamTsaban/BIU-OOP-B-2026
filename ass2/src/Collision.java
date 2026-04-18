/**
 * A class representing a collision event of a ball with a rectangle.
 * Holds collision coordinates by side.
 * A side with no collision is stored as NaN.
 */
public class Collision {
    private final Double fromRightX;
    private final Double fromLeftX;
    private final Double fromTopY;
    private final Double fromBottomY;

    /**
     * Creates a collision from point and type.
     * Relevant sides get the point coordinate.
     * Other sides get infinite values adjusted for comparison (see Collision(Collision, Collision) constructor).
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
    }

    /**
     * Creates a collision from a rectangle and a collision case.
     * @param r the rectangle involved in the collision
     * @param c the collision case representing which sides are colliding
     */
    public Collision(Rectangle r, CollisionCase c) {
        this(r, c.isTop(), c.isBottom(), c.isLeft(), c.isRight(), c.isOutside());
    }

    /**
     * Creates a collision from two collisions.
     * The new collision has the most demanding coordinates of the two collisions for each side.
     * @param first the first collision
     * @param second the second collision
     */
    public Collision(Collision first, Collision second) {
        this.fromRightX = Math.max(first.fromRightX, second.fromRightX);
        this.fromLeftX = Math.min(first.fromLeftX, second.fromLeftX);
        this.fromTopY = Math.min(first.fromTopY, second.fromTopY);
        this.fromBottomY = Math.max(first.fromBottomY, second.fromBottomY);
    }

    /**
     * Creates an empty collision with no sides.
     * @return a new collision representing no collision
     */
    public static Collision none() {
        return new Collision(Helper.SCREEN, false, false, false, false, false);
    }

    /**
     * Merges multiple collisions into one collision.
     * The new collision has the most demanding coordinates of all the collisions for each side.
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
     * Returns right side x value.
     * @return right x or NaN
     */
    public Double getRight() {
        return fromRightX;
    }

    /**
     * Returns left side x value.
     * @return left x or NaN
     */
    public Double getLeft() {
        return fromLeftX;
    }

    /**
     * Returns top side y value.
     * @return top y or NaN
     */
    public Double getTop() {
        return fromTopY;
    }

    /**
     * Returns bottom side y value.
     * @return bottom y or NaN
     */
    public Double getBottom() {
        return fromBottomY;
    }

    /**
     * Checks if right side is missing.
     * @return true when right value is not finite
     */
    public boolean isFromRight() {
        return Double.isFinite(fromRightX);
    }

    /**
     * Checks if left side is missing.
     * @return true when left value is not finite
     */
    public boolean isFromLeft() {
        return Double.isFinite(fromLeftX);
    }

    /**
     * Checks if top side is missing.
     * @return true when top value is not finite
     */
    public boolean isFromTop() {
        return Double.isFinite(fromTopY);
    }

    /**
     * Checks if bottom side is missing.
     * @return true when bottom value is not finite
     */
    public boolean isFromBottom() {
        return Double.isFinite(fromBottomY);
    }

    /**
     * Checks if there are no sides.
     * @return true when all sides are missing
     */
    public boolean isEmpty() {
        return !isFromRight() && !isFromLeft() && !isFromTop() && !isFromBottom();
    }
}
