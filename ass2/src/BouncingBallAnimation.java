import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;

/**
 * Displays an animation of a single bouncing ball on the screen.
 *
 * <p>The ball bounces within the window boundaries. Initial position and velocity
 * are provided as command-line arguments.</p>
 *
 * <p>This is a utility class with a private constructor to prevent instantiation.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public final class BouncingBallAnimation {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BouncingBallAnimation() { }

    /**
     * A helper method to create an animation of random balls being drawn on the screen.
     * @param start the starting point for the animation
     * @param dx the change in x for each ball
     * @param dy the change in y for each ball
     */
    private static void drawAnimation(Point start, double dx, double dy) {
        GUI gui = new GUI("title", Helper.WIDTH, Helper.HEIGHT);
        Sleeper sleeper = new biuoop.Sleeper();
        Ball ball = new Ball(start, Helper.DEFAULT_RADIUS, java.awt.Color.BLACK);
        ball.setVelocity(dx, dy);
        while (true) {
            ball.moveOneStep();
            DrawSurface d = gui.getDrawSurface();
            ball.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(Helper.SLEEP_TIME);  // wait for the animation frame time
       }
    }

    /**
     * The main entry point to run the animation.
     * Parses command-line arguments for initial ball position (first 2 arguments for x, y positions)
     * and velocity (last 2 arguments for dx, dy), then starts the animation.
     *
     * @param args command line arguments in the format: {@code x y dx dy}
     */
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Usage: java BouncingBallAnimation <x> <y> <dx> <dy>");
            return;
        }

        double x = (double) Integer.parseInt(args[0]);
        x = Math.min(x, Helper.WIDTH - Helper.DEFAULT_RADIUS);
        x = Math.max(x, Helper.DEFAULT_RADIUS);

        double y = (double) Integer.parseInt(args[1]);
        y = Math.min(y, Helper.HEIGHT - Helper.DEFAULT_RADIUS);
        y = Math.max(y, Helper.DEFAULT_RADIUS);

        double dx = (double) Integer.parseInt(args[2]);
        dx = Math.min(dx, Helper.MAX_SPEED);
        dx = Math.max(dx, -Helper.MAX_SPEED);

        double dy = (double) Integer.parseInt(args[3]);
        dy = Math.min(dy, Helper.MAX_SPEED);
        dy = Math.max(dy, -Helper.MAX_SPEED);

        BouncingBallAnimation.drawAnimation(new Point(x, y), dx, dy);
    }
}