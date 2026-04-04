/**
 * Small helper methods used by the geometry classes.
 *
 * @author Avraham Tsaban
 */
public class Helper {

    private static final double THRESHOLD = 1e-6;

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
