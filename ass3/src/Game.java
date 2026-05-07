import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;
import biuoop.KeyboardSensor;
import java.awt.Color;
import java.util.Random;

/**
 * The Game class manages the game state, including the sprites and collidables,
 * and handles the game loop for animation.
 * It initializes the game by creating the boundaries and a ball,
 * and runs the animation until the program is terminated.
 *
 * <p>The game area is defined by a GUI window with specified width and height.
 * The boundaries of the game area are formed by blocks,
 * and a ball is created with a random position and velocity within these boundaries.
 * The ball bounces off the boundaries and any other collidable objects in the game environment.</p>
 *
 * @author Avraham Tsaban
 * @version 1.0
 * @since 2024-06-05
 */
public class Game {
    /** Width of GUI windows for geometric calculations. */
    public static final int WIDTH = 800;
    /** Height of GUI windows for geometric calculations. */
    public static final int HEIGHT = 600;
    /** Width of the blocks that form the boundaries of the game area. */
    private static final int BLOCK_WIDTH = 25;
    /** Width of the paddle. */
    private static final int PADDLE_WIDTH = 100;
    /** Height of the paddle. */
    private static final int PADDLE_HEIGHT = 20;
    /** Frames per second for the animation. */
    public static final int FPS = 60;
    /** Milliseconds per frame for the animation. */
    public static final int MS_PER_FRAME = 1000 / FPS;

    private final SpriteCollection sprites;
    private final GameEnvironment environment;
    private final GUI gui;
    private final Sleeper sleeper;
    private final Random rand;

    /**
     * Create a new Game with an empty SpriteCollection and an empty GameEnvironment.
     */
    public Game() {
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        gui = new GUI("title", WIDTH, HEIGHT);
        sleeper = new biuoop.Sleeper();
        rand = new Random();
    }

    /**
     * Add the given collidable to the game environment.
     * @param c the collidable to add to the game environment
     */
    public void addCollidable(Collidable c) {
        environment.addCollidable(c);
    }

    /**
     * Add the given sprite to the game.
     * @param s the sprite to add to the game
     */
    public void addSprite(Sprite s) {
        sprites.addSprite(s);
    }

    /**
     * Initialize the game by creating the blocks and ball and adding them to the game.
     */
    public void initialize() {
        Block[] boundaries = makeBoundaries();
        for (Block block : boundaries) {
            block.addToGame(this);
        }
        Ball ball = makeBall();
        ball.addToGame(this);
        Paddle paddle = makePaddle();
        paddle.addToGame(this);
    }


    /**
     * Run the game. The animation will continue indefinitely until the program is terminated.
     * The method uses a game loop that updates the game state and renders the sprites at a fixed frame rate.
     */
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis(); // timing

            DrawSurface d = gui.getDrawSurface();
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            // timing
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = MS_PER_FRAME - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }

    /**
     * Create the blocks that form the boundaries of the game area, and  add them to collidables and sprites lists.
     * @return an array of the blocks that form the boundaries of the game area
     */
    private Block[] makeBoundaries() {
        Point tmp = new Point(0, 0);
        Block block1 = new Block(tmp, WIDTH, BLOCK_WIDTH, Color.GRAY);
        tmp = new Point(0, BLOCK_WIDTH);
        Block block2 = new Block(tmp, BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH, Color.GRAY);
        tmp = new Point(WIDTH - BLOCK_WIDTH, BLOCK_WIDTH);
        Block block3 = new Block(tmp, BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH, Color.GRAY);
        tmp = new Point(BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH);
        Block block4 = new Block(tmp, WIDTH - 2 * BLOCK_WIDTH, BLOCK_WIDTH, Color.GRAY);

        return new Block[]{block1, block2, block3, block4};
    }

    /**
     * A helper method to create a ball with a random color and position.
     * @return the ball that was created
     */
    private Ball makeBall() {
        Point tmp = new Point(BLOCK_WIDTH, BLOCK_WIDTH);
        Rectangle inside = new Rectangle(tmp, WIDTH - 2 * BLOCK_WIDTH, HEIGHT - 5 * BLOCK_WIDTH);
        Ball ball = Ball.generateMovingBallBySize(Ball.DEFAULT_RADIUS, inside, rand, environment);
        return ball;
    }

    /**
     * A helper method to create a paddle.
     * @return the paddle that was created
     */
    private Paddle makePaddle() {
        Point tmp = new Point(WIDTH / 2.0 - PADDLE_WIDTH / 2.0, HEIGHT - BLOCK_WIDTH - PADDLE_HEIGHT);
        KeyboardSensor sensor = gui.getKeyboardSensor();
        Paddle paddle = new Paddle(sensor, tmp, PADDLE_WIDTH, PADDLE_HEIGHT, Color.ORANGE);
        return paddle;
    }

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
