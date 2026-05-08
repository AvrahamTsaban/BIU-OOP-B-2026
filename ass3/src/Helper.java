/**
 * Static utility methods and constants used by geometry and animation classes.
 *
 * <p>Contains threshold values for floating-point comparisons, window dimensions,
 * default animation parameters, and pre-configured rectangles.</p>
 *
 * <p>This is a utility class with a private constructor to prevent instantiation.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public final class Helper {
    /** Threshold (epsilon) for comparing doubles. */
    public static final double THRESHOLD = 1e-6;
    /** Small delta value for delibreately differentiating doubles. */
    public static final double DELTA = 1e-4;

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
