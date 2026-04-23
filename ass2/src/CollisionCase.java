/**
 * A temporary data class to hold collision direction flags between a ball and a rectangle.
 *
 * <p>The stored flags describe the direction(s) from which the ball approaches a boundary,
 * providing a simple way to represent collision geometry before being converted to a {@link PartialStep} object.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public class CollisionCase {
    private final boolean fromTop;
    private final boolean fromBottom;
    private final boolean fromLeft;
    private final boolean fromRight;
    private final boolean outside;

    /**
     * Creates a collision case with the given direction flags.
     * @param fromTop true if there is a collision from the top, false otherwise
     * @param fromBottom true if there is a collision from the bottom, false otherwise
     * @param fromLeft true if there is a collision from the left, false otherwise
     * @param fromRight true if there is a collision from the right, false otherwise
     * @param outside true if the collision is from the outside, false otherwise
     */
    public CollisionCase(boolean fromTop, boolean fromBottom, boolean fromRight, boolean fromLeft, boolean outside) {
        this.fromTop = fromTop;
        this.fromBottom = fromBottom;
        this.fromRight = fromRight;
        this.fromLeft = fromLeft;
        this.outside = outside;
    }

    /**
     * Creates a collision case from two collision cases.
     * The new collision case has the union of the direction flags of the two collision cases.
     * If one of the collision cases is from the outside and the other is not,
     * the new collision case is set to have no directions and not be from the outside.
     * @param first the first collision case
     * @param second the second collision case
     */
    public CollisionCase(CollisionCase first, CollisionCase second) {
        if (first.isFromOutside() != second.isFromOutside()) {
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
     * Checks if there is a collision from the right.
     * @return true when fromRight is true
     */
    public boolean isFromRight() {
        return fromRight;
    }

    /**
     * Checks if there is a collision from the left.
     * @return true when fromLeft is true
     */
    public boolean isFromLeft() {
        return fromLeft;
    }

    /**
     * Checks if there is a collision from the top.
     * @return true when fromTop is true
     */
    public boolean isFromTop() {
        return fromTop;
    }

    /**
     * Checks if there is a collision from the bottom.
     * @return true when fromBottom is true
     */
    public boolean isFromBottom() {
        return fromBottom;
    }

    /**
     * Checks if the collision is from the outside.
     * @return true when outside is true
     */
    public boolean isFromOutside() {
        return outside;
    }
}