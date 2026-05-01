
/**
 * Displays an animation of a single bouncing ball on the screen.
 *
 * <p>The ball bounces on various objects, all of whom managed by {@link Game} class.</p>
 *
 * <p>This is a utility class with a private constructor to prevent instantiation.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public final class Main {
    /** Private constructor to prevent instantiation of this utility class. */
    private Main() { }

    /**
     * The main entry point to run the animation.
     * @param args command line arguments (neglected)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}