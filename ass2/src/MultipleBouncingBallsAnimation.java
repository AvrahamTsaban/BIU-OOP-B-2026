import java.util.Random;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
/**
 * Displays an animation of multiple bouncing balls on the screen.
 *
 * <p>Each ball is created with a size specified by command-line arguments.
 * Ball sizes determine both appearance and movement speed (larger balls move slower).
 * Balls bounce within the window boundaries. If balls overlap, those specified later
 * in arguments are drawn on top of earlier ones.</p>
 *
 * <p>This is a utility class with a private constructor to prevent instantiation.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public final class MultipleBouncingBallsAnimation {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MultipleBouncingBallsAnimation() { }

    /**
     * A helper method to create an animation of random balls being drawn on the screen.
     * Each ball's size is determined by the corresponding element in the sizes array.
     * @param sizes an array of sizes for the balls to be drawn
     */
    private static void drawAnimation(int[] sizes) {
        final int sleepTime = Helper.SLEEP_TIME;
        Random rand = new Random();
        GUI gui = new GUI("title", Helper.WIDTH, Helper.HEIGHT);
        Sleeper sleeper = new biuoop.Sleeper();
        Ball[] balls = new Ball[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            balls[i] = Ball.generateMovingBallBySize(sizes[i], Helper.SCREEN, rand);
        }
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            for (Ball ball : balls) {
                ball.moveOneStep();
                ball.drawOn(d);
            }
            gui.show(d);
            sleeper.sleepFor(sleepTime);  // wait for the animation frame time
       }
    }

    /**
     * The main method to run the animation.
     * Gets the sizes of the balls to be drawn from the command line arguments,
     * and starts the animation with those sizes.
     * If balls overlap, the ball whose cmd arguments is later in the list will be drawn on top.
     * @param args command line arguments representing the sizes of the balls to be drawn
    */
    public static void main(String[] args) {
        int[] sizes = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            sizes[i] = Integer.parseUnsignedInt(args[i]);
        }
        MultipleBouncingBallsAnimation.drawAnimation(sizes);
    }
}