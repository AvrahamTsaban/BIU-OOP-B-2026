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
    /** Width of GUI windows for geometric calculations. */
    public static final int WIDTH = 800;
    /** Height of GUI windows for geometric calculations. */
    public static final int HEIGHT = 600;
    /** Default radius for balls in animations. */
    public static final int DEFAULT_RADIUS = 30;
    /** Sleep time in milliseconds for animations. */
    public static final int SLEEP_TIME = 18;

    /** Upper-left corner of the gray square frame. */
    private static final Point GRAY_SQUARE_UL = new Point(50, 50);
    /** Edge length of the gray square frame. */
    private static final double GRAY_SQUARE_EDGE = 450;
    /** Gray square frame used as the main outer border. */
    public static final Rectangle GRAY_SQUARE =
        new Rectangle(GRAY_SQUARE_UL, GRAY_SQUARE_EDGE, GRAY_SQUARE_EDGE, java.awt.Color.GRAY);
    /** Upper-left corner of the yellow square frame. */
    private static final Point YELLOW_SQUARE_UL = new Point(450, 450);
    /** Edge length of the yellow square frame. */
    private static final double YELLOW_SQUARE_EDGE = 150;
    /** Yellow square frame used as an inner region. */
    public static final Rectangle YELLOW_SQUARE =
        new Rectangle(YELLOW_SQUARE_UL, YELLOW_SQUARE_EDGE, YELLOW_SQUARE_EDGE, java.awt.Color.YELLOW);
    /** Full screen rectangle representing the drawable area. */
    public static final Rectangle SCREEN =
        new Rectangle(new Point(0, 0), Helper.WIDTH, Helper.HEIGHT, java.awt.Color.WHITE);

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
