/**
 * Helper class based on the GeometryTester class from the assignment description.
 *
 * @author Avraham Tsaban
 */
public class Helper {

    private static final double THRESHOLD = 0.00001;

    /**
     * @param a
     * @param b
     * @return true if a and b are close enough to be considered equal, false otherwise.
     */
    public static boolean doubleEq(double a, double b) {
        return Math.abs(a - b) < Helper.THRESHOLD;
    }
}
