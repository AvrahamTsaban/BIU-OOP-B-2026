import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;

/**
 * A test class for the Ball class. It creates a GUI and draws multiple balls on it.
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
            sleeper.sleepFor(50);  // wait for 50 milliseconds.
       }
    }

    /**
     * The main method to run the animation.
     * @param args command line arguments (not used)
    */
    public static void main(String[] args) {
        //final int RADIUS = Helper.DEFAULT_RADIUS;
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double dx = Double.parseDouble(args[2]);
        double dy = Double.parseDouble(args[3]);
        /*if (x - RADIUS < 0 || x + RADIUS > Helper.WIDTH || y - RADIUS < 0 || y + RADIUS > Helper.HEIGHT) {
            System.out.println("Starting point must be within the bounds of the window.");
            return;
        }*/
        BouncingBallAnimation.drawAnimation(new Point(x, y), dx, dy);
    }
}