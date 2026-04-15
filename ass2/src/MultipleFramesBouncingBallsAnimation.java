import java.util.Random;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
/**
 * Creates an animation of multiple bouncing balls on the screen.
 * Each ball's size is determined by the corresponding element in the sizes array.
 * The balls will bounce around the screen, and if they overlap,
 * the one whose command line argument is later in the list will be drawn on top of the earlier ones.
 */
public final class MultipleFramesBouncingBallsAnimation {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MultipleFramesBouncingBallsAnimation() { }

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
        int numBalls = sizes.length;
        int numInsideBalls = numBalls / 2;
        if (numBalls % 2 != 0) {
            numInsideBalls++;
        }
        Ball[] insideBalls = new Ball[numInsideBalls];
        Ball[] outsideBalls = new Ball[numBalls - numInsideBalls];
        int arrayIndex = 0;
        for (int i = 0; i < numInsideBalls; i++, arrayIndex++) {
            do {
                insideBalls[i] = Ball.generateMovingBallBySize(sizes[arrayIndex], Helper.GRAY_SQUARE, rand);
            } while (!Helper.YELLOW_SQUARE.isOutside(insideBalls[i]));
        }
        for (int i = 0; i < numBalls - numInsideBalls; i++, arrayIndex++) {
            do {
                outsideBalls[i] = Ball.generateMovingBallBySize(sizes[arrayIndex], Helper.SCREEN, rand);
            } while (!Helper.GRAY_SQUARE.isOutside(outsideBalls[i])
                || !Helper.YELLOW_SQUARE.isOutside(outsideBalls[i]));
        }
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            Helper.GRAY_SQUARE.drawOn(d);
            for (Ball ball : balls) {
                boolean validStep = ball.remainsIn(Helper.SCREEN);
                if (validStep) {
                    ball.moveOneStep();
                } else {
                    ball.complexMove();
                }
                ball.drawOn(d);
            }
            Helper.YELLOW_SQUARE.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(sleepTime);  // wait for 50 milliseconds.
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
        MultipleFramesBouncingBallsAnimation.drawAnimation(sizes);
    }
}