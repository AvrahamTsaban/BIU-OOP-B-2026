import java.util.Random;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
/**
 * A test class for the Ball class. It creates a GUI and draws multiple balls on it.
 */
public final class MultipleBouncingBallsAnimation {
    private static final double BASE_SPEED = 9.0;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MultipleBouncingBallsAnimation() { }

    /**
     * A helper method to create an animation of random balls being drawn on the screen.
     * @param sizes an array of sizes for the balls to be drawn
     */
    private static void drawAnimation(double[] sizes) {
        final int sleepTime = Helper.SLEEP_TIME;
        Random rand = new Random();
        GUI gui = new GUI("title", Helper.WIDTH, Helper.HEIGHT);
        Sleeper sleeper = new biuoop.Sleeper();
        Ball[] balls = new Balls[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            double speed = BASE_SPEED / Math.log(sizes[i]);
            Velocity velocity = Velocity.semiRandVelocity(rand, speed);
            Point start = new Point(rand.nextDouble() * Helper.WIDTH, rand.nextDouble() * Helper.HEIGHT);
            java.awt.Color color = new java.awt.Color.class()
            Balls[i] = new Ball(null, i, null)
        }
        Ball ball = new Ball(start, Helper.DEFAULT_RADIUS, java.awt.Color.BLACK);
        ball.setVelocity(dx, dy);
        while (true) {
            ball.moveOneStep();
            DrawSurface d = gui.getDrawSurface();
            ball.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(sleepTime);  // wait for 50 milliseconds.
       }
    }

    /**
     * The main method to run the animation.
     * @param args command line arguments (not used)
    */
    public static void main(String[] args) {
        double[] sizes = new double[args.length];
        for (int i = 0; i < args.length; i++) {
            sizes[i] = Double.parseDouble(args[i]);
        }
        MultipleBouncingBallsAnimation.drawAnimation(sizes);
    }
}