import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;
import java.awt.Color;
import java.util.ArrayList;

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
public final class Main {
    private static final int BLOCK_SIZE = 25;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Main() { }

    /**
     * A helper method to create an animation of a random ball being drawn on the screen.
     * The ball moves according to its velocity and bounces off the provided blocks.
     * The animation runs indefinitely until the program is terminated.
     * @param ball the Ball object to animate
     * @param blocks an array of Block objects representing the blocks that the ball can collide with.
     */
    private static void drawAnimation(Ball ball, Block[] blocks) {
        GUI gui = new GUI("title", Helper.WIDTH, Helper.HEIGHT);
        Sleeper sleeper = new biuoop.Sleeper();
        ArrayList<Sprite> sprites = new ArrayList<>();
        sprites.add(ball);
        for (Block block : blocks) {
            sprites.add(block);
        }
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            for (Sprite sprite : sprites) {
                sprite.drawOn(d);
                sprite.timePassed();
            }
            gui.show(d);
            sleeper.sleepFor(Helper.SLEEP_TIME);  // wait for the animation frame time
       }
    }

    /**
     * The main entry point to run the animation.
     * @param args command line arguments (neglected)
     */
    public static void main(String[] args) {
        Point tmp = new Point(0, 0);
        Block block1 = new Block(tmp, Helper.WIDTH, BLOCK_SIZE, Color.GRAY);
        tmp = new Point(0, BLOCK_SIZE);
        Block block2 = new Block(tmp, BLOCK_SIZE, Helper.HEIGHT - BLOCK_SIZE, Color.GRAY);
        tmp = new Point(Helper.WIDTH - BLOCK_SIZE, BLOCK_SIZE);
        Block block3 = new Block(tmp, BLOCK_SIZE, Helper.HEIGHT - BLOCK_SIZE, Color.GRAY);
        tmp = new Point(BLOCK_SIZE, Helper.HEIGHT - BLOCK_SIZE);
        Block block4 = new Block(tmp, Helper.WIDTH - 2 * BLOCK_SIZE, BLOCK_SIZE, Color.GRAY);
        Block[] blocks = {block1, block2, block3, block4};
        GameEnvironment ge = new GameEnvironment();
        for (Collidable block : blocks) {
            ge.addCollidable((Collidable) block);
        }
        tmp = new Point(BLOCK_SIZE, BLOCK_SIZE);
        Rectangle inside = new Rectangle(tmp, Helper.WIDTH - 2 * BLOCK_SIZE, Helper.HEIGHT - 2 * BLOCK_SIZE);
        Ball ball = Ball.generateMovingBallBySize(Helper.DEFAULT_RADIUS, inside, new java.util.Random(), ge);
        Main.drawAnimation(ball, blocks);
    }
}