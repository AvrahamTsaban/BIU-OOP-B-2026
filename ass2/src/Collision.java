/**
 * A class representing a collision event of a ball with a rectangle.
 * Holds collision coordinates by side.
 * A side with no collision is stored as NaN.
 */
public class Collision {
    /** Enum representing the type of collision that can occur. */
    static public enum CollisionType {
        NONE, TOP, BOTTOM, LEFT, RIGHT, TL, TR, BL, BR, DEEP
    }

    private final Double rightX;
    private final Double leftX;
    private final Double topY;
    private final Double bottomY;

    /**
     * Creates a collision from point and type.
     * Relevant sides get the point coordinate.
     * Other sides get NaN.
     * @param pt collision point
     * @param type collision type
     */
    public Collision(Rectangle r, boolean hasTop, boolean hasBottom, boolean hasLeft, boolean hasRight) {
        if (hasTop) {
            this.topY = r.topY();
        } else {
            this.topY = Double.NEGATIVE_INFINITY;
        }
        if (hasBottom) {
            this.bottomY = r.bottomY();
        } else {
            this.bottomY = Double.POSITIVE_INFINITY;
        }
        if (hasLeft) {
            this.leftX = r.leftX();
        } else {
            this.leftX = Double.NEGATIVE_INFINITY;
        }
        if (hasRight) {
            this.rightX = r.rightX();
        } else {
            this.rightX = Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Creates a collision from a rectangle and a collision case.
     * @param r the rectangle involved in the collision
     * @param cc the collision case representing which sides are colliding
     */
    public Collision (Rectangle r, CollisionCase cc) {
        boolean hasTop = cc.isTop();
        boolean hasBottom = cc.isBottom();
        boolean hasLeft = cc.isLeft();
        boolean hasRight = cc.isRight();
        this(r, hasTop, hasBottom, hasLeft, hasRight);
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
     * Merges multiple collisions into one collision.
     * The new collision has the most demanding coordinates of all the collisions for each side.
     * @param collisions the collisions to merge
     * @return a new collision representing the merged result of all the given collisions
     */
    public Collision mergeMultipleCollisions(Collision[] collisions) {
        Collision mergedCollision = new Collision(this, collisions[0]);
        for (Collision currentCollision : collisions) {
            mergedCollision = new Collision(mergedCollision, currentCollision);
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

    public boolean isEmpty() {
        return !isRight() && !isLeft() && !isTop() && !isBottom();
    }
}
