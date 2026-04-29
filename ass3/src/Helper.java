/**
 * Static utility methods and constants used by geometry and animation classes.
 *
 * <p>Contains threshold values for floating-point comparisons, window dimensions,
 * default animation parameters, and pre-configured rectangles.</p>
 *
 * <p>This is a utility class with a private constructor to prevent instantiation.</p>
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
    /** Full screen rectangle representing the drawable area. */
    public static final Rectangle SCREEN =
    new Rectangle(new Point(0, 0), WIDTH, HEIGHT);

    /** Maximum possible distance between any two points in the window, used for collision detection. */
    public static final double MAX_LINE_LENGTH = Math.sqrt(WIDTH * WIDTH + HEIGHT * HEIGHT);
    /** Maximum radius for balls in animations. */
    public static final int MAX_RADIUS = Math.min(WIDTH, HEIGHT) / 4;
    /** Default radius for balls in animations. */
    public static final int DEFAULT_RADIUS = 8;
    /** Maximum speed for balls in animations. */
    public static final int MAX_SPEED = Math.min(WIDTH, HEIGHT) / 10;
    /** Sleep time in milliseconds for animations. */
    public static final int SLEEP_TIME = 50;

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
