/**
 * Helper class based on the GeometryTester class from the assignment description.
 *
 * @author Avraham Tsaban
 */
public class Helper {

    private static final double THRESHOLD = 1e-6;

    /**
     * Check whether two double values are approximately equal.
     *
     * @param a the first value
     * @param b the second value
     * @return true if a and b are close enough to be considered equal, false otherwise.
     */
    public static boolean doubleEq(double a, double b) {
        return Math.abs(a - b) < Helper.THRESHOLD;
    }
}
