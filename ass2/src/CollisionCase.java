/**
 * A temporary class to hold the collision edges of a ball with a rectangle.
 */
public class CollisionCase {
    private final boolean fromTop;
    private final boolean fromBottom;
    private final boolean fromLeft;
    private final boolean fromRight;
    private final boolean outside;

    /**
     * Creates a collision case with the given edges.
     * @param fromTop true if there is a collision with the top edge, false otherwise
     * @param fromBottom true if there is a collision with the bottom edge, false otherwise
     * @param fromLeft true if there is a collision with the left edge, false otherwise
     * @param fromRight true if there is a collision with the right edge, false otherwise
     * @param outside true if the collision is from the outside, false otherwise
     */
    public CollisionCase(boolean fromTop, boolean fromBottom, boolean fromLeft, boolean fromRight, boolean outside) {
        this.fromTop = fromTop;
        this.fromBottom = fromBottom;
        this.fromLeft = fromLeft;
        this.fromRight = fromRight;
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
            this.fromTop = false;
            this.fromBottom = false;
            this.fromLeft = false;
            this.fromRight = false;
            this.outside = false;
            return;
        }
        this.fromTop = first.fromTop || second.fromTop;
        this.fromBottom = first.fromBottom || second.fromBottom;
        this.fromLeft = first.fromLeft || second.fromLeft;
        this.fromRight = first.fromRight || second.fromRight;
        this.outside = first.outside;
    }

    /**
     * Checks if right side exists.
     * @return true when fromRight is true
     */
    public boolean isRight() {
        return fromRight;
    }

    /**
     * Checks if left side exists.
     * @return true when fromLeft is true
     */
    public boolean isLeft() {
        return fromLeft;
    }

    /**
     * Checks if top side exists.
     * @return true when fromTop is true
     */
    public boolean isTop() {
        return fromTop;
    }

    /**
     * Checks if bottom side exists.
     * @return true when fromBottom is true
     */
    public boolean isBottom() {
        return fromBottom;
    }

    /**
     * Checks if the collision is from the outside.
     * @return true when outside is true
     */
    public boolean isOutside() {
        return outside;
    }
}