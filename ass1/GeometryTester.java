/**
 * Basic tests for Point and Line.
 */
public final class GeometryTester {

<<<<<<< HEAD
    private static final double COMPARISON_THRESHOLD = 0.00001;
=======
    static final double COMPARISON_THRESHOLD = 0.00001;
>>>>>>> 5439265 (stage 1)

    /**
     * Checks if two doubles are almost equal.
     *
<<<<<<< HEAD
     * @param a
     * @param b
<<<<<<< HEAD
     * @return true if a and b are equal up to the comparison threshold, false otherwise.
     */
    public static boolean doubleEquals(double a, double b) {
        return  Math.abs(a - b) < GeometryTester.COMPARISON_THRESHOLD;
=======
     * @return true if a and b are close enough to be considered equal, false otherwise.
=======
     * @param a first value
     * @param b second value
     * @return true if the difference is smaller than the threshold
>>>>>>> 95e5362 (removed colouredLine class)
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < GeometryTester.COMPARISON_THRESHOLD;
>>>>>>> 5439265 (stage 1)
    }

    /**
     * Runs Point tests.
     *
     * @return true if all point tests pass; otherwise false
     */
    public static boolean testPoint() {
        boolean mistake = false;
        Point p1 = new Point(12, 2);
        Point p2 = new Point(9, -2);

        if (!doubleEquals(p1.getX(), 12)) {
            System.out.println("Test p1.getX() failed.");
            mistake = true;
        }
        if (!doubleEquals(p1.getY(), 2)) {
            System.out.println("Test p1.getY() failed.");
            mistake = true;
        }
        if (!doubleEquals(p1.distance(p1), 0)) {
            System.out.println("Test distance to self failed.");
            mistake = true;
        }
        if (!doubleEquals(p1.distance(p2), p2.distance(p1))) {
            System.out.println("Test distance symmetry failed.");
            mistake = true;
        }
        if (!doubleEquals(p1.distance(p2), 5)) {
            System.out.println("Test distance failed.");
            mistake = true;
        }
        if (!p1.equals(p1)) {
            System.out.println("Equality to self failed.");
            mistake = true;
        }
        if (!p1.equals(new Point(12, 2))) {
            System.out.println("Equality failed.");
            mistake = true;
        }
        if (p1.equals(p2)) {
            System.out.println("Equality failed -- should not be equal.");
            mistake = true;
        }

        return !mistake;
    }

    /**
     * Runs Line tests.
     *
     * @return true if all line tests pass; otherwise false
     */
    public static boolean testLine() {
        boolean mistakes = false;
        Line l1 = new Line(12, 2, 9, -2);
        Line l2 = new Line(0, 0, 20, 0);
        Line l3 = new Line(9, 2, 12, -2);

        if (!l1.isIntersecting(l2)) {
            System.out.println("Test isIntersecting failed (1).");
            mistakes = true;
        }
        if (l1.isIntersecting(new Line(0, 0, 1, 1))) {
            System.out.println("Test isIntersecting failed (2).");
            mistakes = true;
        }
        Point intersectL1L2 = l1.intersectionWith(l2);
        if (!l1.middle().equals(intersectL1L2)) {
            System.out.println("Test intersectionWith middle failed.");
            mistakes = true;
        }

        return !mistakes;
    }

    /**
     * Runs all tests and prints a summary.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
<<<<<<< HEAD
        if (GeometryTester.testPoint() && GeometryTester.testLine()) {
=======
        GeometryTester tester = new GeometryTester();
        if (tester.testPoint() && tester.testLine()) {
>>>>>>> 5439265 (stage 1)
            System.out.println("Test Completed Successfully!");
        } else {
            System.out.println("Found failing tests.");
        }
    }
}
