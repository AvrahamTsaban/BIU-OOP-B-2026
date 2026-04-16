/**
 * A temporary class to hold the collision edges of a ball with a rectangle.
 */
public class CollisionCase {
    private final boolean hasTop;
    private final boolean hasBottom;
    private final boolean hasLeft;
    private final boolean hasRight;
    private final boolean outside;

    /**
     * Creates a collision case with the given edges.
     * @param hasTop true if there is a collision with the top edge, false otherwise
     * @param hasBottom true if there is a collision with the bottom edge, false otherwise
     * @param hasLeft true if there is a collision with the left edge, false otherwise
     * @param hasRight true if there is a collision with the right edge, false otherwise
     * @param outside true if the collision is from the outside, false otherwise
     */
    public CollisionCase(boolean hasTop, boolean hasBottom, boolean hasLeft, boolean hasRight, boolean outside) {
        this.hasTop = hasTop;
        this.hasBottom = hasBottom;
        this.hasLeft = hasLeft;
        this.hasRight = hasRight;
        this.outside = outside;
    }

    /**
     * Creates a collision case from two collision cases.
     * The new collision case has the union of the edges of the two collision cases.
     * If one of the collision cases is from the outside and the other is not,
     * the new collision case is set to have no edges and not be from the outside.
     * @param first the first collision case
     * @param second the second collision case
     */
    public CollisionCase(CollisionCase first, CollisionCase second) {
        if (first.isOutside() != second.isOutside()) {
            this.hasTop = false;
            this.hasBottom = false;
            this.hasLeft = false;
            this.hasRight = false;
            this.outside = false;
            return;
        }
        this.hasTop = first.hasTop || second.hasTop;
        this.hasBottom = first.hasBottom || second.hasBottom;
        this.hasLeft = first.hasLeft || second.hasLeft;
        this.hasRight = first.hasRight || second.hasRight;
        this.outside = first.outside;
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

    /**
     * Checks if the collision is from the outside.
     * @return true when outside is true
     */
    public boolean isOutside() {
        return outside;
    }
}