import java.util.Random;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * Displays an animation of multiple bouncing balls within rectangular boundaries.
 *
 * <p>Balls are separated into two groups:
 * those bouncing inside a gray square (bouncing off a yellow overlapped square),
 * and those bouncing outside both squares. Each ball's size is specified
 * by command-line arguments. If balls overlap, those specified later in arguments are drawn on top.</p>
 *
 * <p>This is a utility class with a private constructor to prevent instantiation.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public final class MultipleFramesBouncingBallsAnimation {
    /** An array containing all the predefined squares for easy access. */
    private static final Rectangle[] ALL_SQUARES = new Rectangle[] {Helper.GRAY_SQUARE, Helper.YELLOW_SQUARE};
    /** The maximum number of attempts to generate a valid ball position. */
    private static final int MAX_ATTEMPTS = 20;
    /** Private constructor to prevent instantiation of this utility class. */
    private MultipleFramesBouncingBallsAnimation() { }

    /**
     * A helper method to create an animation of given balls being drawn on the screen.
     * @param allBalls a 2D array of Ball objects, where the first dimension separates inside and outside balls
     */
    private static void drawAnimation(Ball[][] allBalls) {
        final int sleepTime = Helper.SLEEP_TIME;
        GUI gui = new GUI("title", Helper.WIDTH, Helper.HEIGHT);
        Sleeper sleeper = new biuoop.Sleeper();
        Ball[] insideBalls = allBalls[0];
        Ball[] outsideBalls = allBalls[1];
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            Helper.GRAY_SQUARE.drawOn(d);
            for (Ball ball : insideBalls) {
                ball.moveOneStep(Helper.GRAY_SQUARE, Helper.YELLOW_SQUARE);
                ball.drawOn(d);
            }
            for (Ball ball : outsideBalls) {
                ball.moveOneStep(new Rectangle[0], ALL_SQUARES);
                ball.drawOn(d);
            }
            Helper.YELLOW_SQUARE.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(sleepTime);
        }
    }

    /**
     * A helper method to create an array of balls separated into inside and outside the gray square.
     * The first half (or one more if odd) of the sizes will be used for inside balls, and the rest for outside balls.
     * @param sizes an array of sizes for the balls to be created
     * @return a 2D array of Ball objects, where the first dimension separates inside and outside balls
     */
    private static Ball[][] generateBalls(int[] sizes) {
        Random rand = new Random();
        int numBalls = sizes.length;
        int numInsideBalls = numBalls / 2;
        if (numBalls % 2 != 0) {
            numInsideBalls++;
        }
        Ball[] insideBalls = new Ball[numInsideBalls];
        Ball[] outsideBalls = new Ball[numBalls - numInsideBalls];
        int arrayIndex = 0;
        for (int i = 0; i < numInsideBalls; i++, arrayIndex++) {
            int attempts = 0;
            do {
                insideBalls[i] = Ball.generateMovingBallBySize(sizes[arrayIndex], Helper.GRAY_SQUARE, rand);
                attempts++;
                if (attempts > MAX_ATTEMPTS) {
                    sizes[arrayIndex] = Helper.DEFAULT_RADIUS; // Fallback to default size if too many attempts
                }
            } while (!Helper.YELLOW_SQUARE.isOutside(insideBalls[i]));
        }
        for (int i = 0; i < numBalls - numInsideBalls; i++, arrayIndex++) {
            int attempts = 0;
            do {
                outsideBalls[i] = Ball.generateMovingBallBySize(sizes[arrayIndex], Helper.SCREEN, rand);
                attempts++;
                if (attempts > MAX_ATTEMPTS) {
                    sizes[arrayIndex] = Helper.DEFAULT_RADIUS; // Fallback to default size if too many attempts
                }
            } while (!Helper.GRAY_SQUARE.isOutside(outsideBalls[i])
                || !Helper.YELLOW_SQUARE.isOutside(outsideBalls[i]));
        }
        return new Ball[][] {insideBalls, outsideBalls};
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
            int size = Math.abs(Integer.parseInt(args[i]));
            sizes[i] = Math.min(size, Helper.MAX_RADIUS);
        }
        Ball[][] balls = generateBalls(sizes);
        MultipleFramesBouncingBallsAnimation.drawAnimation(balls);
    }
}