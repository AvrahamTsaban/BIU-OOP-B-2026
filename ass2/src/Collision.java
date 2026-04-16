/**
 * A class representing a collision event of a ball with a rectangle.
 * Holds collision coordinates by side.
 * A side with no collision is stored as NaN.
 */
public class Collision {
    private final Double rightX;
    private final Double leftX;
    private final Double topY;
    private final Double bottomY;

    /**
     * Creates a collision from point and type.
     * Relevant sides get the point coordinate.
     * Other sides get infinite values adjusted for comparison (see Collision(Collision, Collision) constructor).
     * @param r the rectangle involved in the collision
     * @param fromBottom true if there is a collision with the top edge, false otherwise
     * @param fromTop true if there is a collision with the bottom edge, false otherwise
     * @param fromRight true if there is a collision with the left edge, false otherwise
     * @param fromLeft true if there is a collision with the right edge, false otherwise
     * @param outside true if the collision is from the outside, false otherwise
     */
    public Collision(Rectangle r,
            boolean fromBottom, boolean fromTop, boolean fromRight, boolean fromLeft, boolean outside) {
        if (fromBottom) {
            this.topY = outside ? r.topY() : r.bottomY();
        } else {
            this.topY = Double.NEGATIVE_INFINITY;
        }
        if (fromTop) {
            this.bottomY = outside ? r.bottomY() : r.topY();
        } else {
            this.bottomY = Double.POSITIVE_INFINITY;
        }
        if (fromRight) {
            this.leftX = outside ? r.rightX() : r.leftX();
        } else {
            this.leftX = Double.NEGATIVE_INFINITY;
        }
        if (fromLeft) {
            this.rightX = outside ? r.leftX() : r.rightX();
        } else {
            this.rightX = Double.POSITIVE_INFINITY;
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
        this.rightX = Math.min(first.rightX, second.rightX);
        this.leftX = Math.max(first.leftX, second.leftX);
        this.topY = Math.max(first.topY, second.topY);
        this.bottomY = Math.min(first.bottomY, second.bottomY);
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
        return rightX;
    }

    /**
     * Returns left side x value.
     * @return left x or NaN
     */
    public Double getLeft() {
        return leftX;
    }

    /**
     * Returns top side y value.
     * @return top y or NaN
     */
    public Double getTop() {
        return topY;
    }

    /**
     * Returns bottom side y value.
     * @return bottom y or NaN
     */
    public Double getBottom() {
        return bottomY;
    }

    /**
     * Checks if right side is missing.
     * @return true when right value is not finite
     */
    public boolean isRight() {
        return Double.isFinite(rightX);
    }

    /**
     * Checks if left side is missing.
     * @return true when left value is not finite
     */
    public boolean isLeft() {
        return Double.isFinite(leftX);
    }

    /**
     * Checks if top side is missing.
     * @return true when top value is not finite
     */
    public boolean isTop() {
        return Double.isFinite(topY);
    }

    /**
     * Checks if bottom side is missing.
     * @return true when bottom value is not finite
     */
    public boolean isBottom() {
        return Double.isFinite(bottomY);
    }

    /**
     * Checks if there are no sides.
     * @return true when all sides are missing
     */
    public boolean isEmpty() {
        return !isRight() && !isLeft() && !isTop() && !isBottom();
    }
}
