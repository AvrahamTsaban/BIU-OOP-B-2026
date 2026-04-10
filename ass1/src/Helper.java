/**
 * Static methods used by the geometry classes.
 * Currently, only contains a method for comparing doubles with a threshold.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public final class Helper {
    /** Threshold for comparing doubles. */
    public static final double THRESHOLD = 1e-6;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Helper() { }

    /**
     * Checks if two doubles are almost equal.
     *
     * @param a the first value
     * @param b the second value
     * @return true if the absolute difference is smaller than the threshold
     */
    public static boolean doubleEq(double a, double b) {
        return Math.abs(a - b) < Helper.THRESHOLD;
    }
}
