/**
 * A temporary class to hold the collision edges of a ball with a rectangle.
 */
public class CollisionCase {
    private final boolean hasTop;
    private final boolean hasBottom;
    private final boolean hasLeft;
    private final boolean hasRight;

    /**
     * Creates a collision case with the given edges.
     * @param hasTop true if there is a collision with the top edge, false otherwise
     * @param hasBottom true if there is a collision with the bottom edge, false otherwise
     * @param hasLeft true if there is a collision with the left edge, false otherwise
     * @param hasRight true if there is a collision with the right edge, false otherwise
     */
    public CollisionCase(boolean hasTop, boolean hasBottom, boolean hasLeft, boolean hasRight) {
        this.hasTop = hasTop;
        this.hasBottom = hasBottom;
        this.hasLeft = hasLeft;
        this.hasRight = hasRight;
    }

    /**
     * Checks if right side exists.
     * @return true when hasRight is true
     */
    public boolean isRight() {
        return hasRight;
    }

    /**
     * Checks if left side exists.
     * @return true when hasLeft is true
     */
    public boolean isLeft() {
        return hasLeft;
    }

    /**
     * Checks if top side exists.
     * @return true when hasTop is true
     */
    public boolean isTop() {
        return hasTop;
    }

    /**
     * Checks if bottom side exists.
     * @return true when hasBottom is true
     */
    public boolean isBottom() {
        return hasBottom;
    }
}
