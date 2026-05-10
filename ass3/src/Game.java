import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;
import biuoop.KeyboardSensor;
import java.awt.Color;
import java.util.ArrayList;
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
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public class Game {
    /** Width of GUI windows for geometric calculations. */
    public static final int WIDTH = 800;
    /** Height of GUI windows for geometric calculations. */
    public static final int HEIGHT = 600;
    /** Width of all blocks. */
    public static final int BLOCK_WIDTH = 25;
    /** Length of small blocks. */
    public static final int MINIBLOCK_LENGTH = 50;
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
        gui = new GUI("double PingPong", WIDTH, HEIGHT);
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
        addBoundaries();
        addMiniBlocks();
        addPaddle();
        addBall();
        addBall();
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
     */
    public void addBoundaries() {
        Point tmp = new Point(0, 0);
        Block block1 = new Block(tmp, WIDTH, BLOCK_WIDTH, Color.GRAY);
        block1.addToGame(this);
        tmp = new Point(0, BLOCK_WIDTH);
        Block block2 = new Block(tmp, BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH, Color.GRAY);
        block2.addToGame(this);
        tmp = new Point(WIDTH - BLOCK_WIDTH, BLOCK_WIDTH);
        Block block3 = new Block(tmp, BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH, Color.GRAY);
        block3.addToGame(this);
        tmp = new Point(BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH);
        Block block4 = new Block(tmp, WIDTH - 2 * BLOCK_WIDTH, BLOCK_WIDTH, Color.GRAY);
        block4.addToGame(this);
    }

    /**
     * A helper method to add the mini blocks in a grid pattern on the right side of the game area.
     */
    public void addMiniBlocks() {
        final int gridColumns = 12;
        final int gridRows = 5;
        final int anchorX = WIDTH - BLOCK_WIDTH;
        final int anchorY = 3 * BLOCK_WIDTH;
        final Color[] colors = new Color[]{Color.GRAY, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK, Color.GREEN};

        ArrayList<Integer> xValues = new ArrayList<Integer>();
        for (int n = 1; n <= gridColumns; n++) {
            xValues.add(anchorX - n * MINIBLOCK_LENGTH);
        }
        ArrayList<Integer> yValues = new ArrayList<Integer>();
        for (int n = 0; n < gridRows; n++) {
            yValues.add(anchorY + n * BLOCK_WIDTH);
        }

        for (int a = 0; a < gridRows; a++) {
            for (int b = 0; b < gridColumns - a; b++) {
                Point upperLeft = new Point(xValues.get(b), yValues.get(a));
                MiniBlock toAdd = new MiniBlock(upperLeft, colors[a]);
                toAdd.addToGame(this);
            }
        }
    }

    /**
     * A helper method to create a ball with a random color and position.
     *
     * <p><strong>Implementation note:</strong> should be called after all collidables are added to the game,
     * to ensure the ball is not created inside any of them.</p>
     */
    public void addBall() {
        Point tmp = new Point(BLOCK_WIDTH, BLOCK_WIDTH);
        Rectangle inside = new Rectangle(tmp, WIDTH - 2 * BLOCK_WIDTH, HEIGHT - 5 * BLOCK_WIDTH);
        Ball ball = null;
        do {
            ball = Ball.generateMovingBallBySize(Ball.DEFAULT_RADIUS, inside, rand, environment);
        } while (environment.isInsideCollidable(ball.getCenter(), Ball.DEFAULT_RADIUS));
        ball.addToGame(this);
    }

    /**
     * A helper method to create a paddle.
     */
    public void addPaddle() {
        Point tmp = new Point(WIDTH / 2.0 - PADDLE_WIDTH / 2.0, HEIGHT - BLOCK_WIDTH - PADDLE_HEIGHT);
        KeyboardSensor sensor = gui.getKeyboardSensor();
        Paddle paddle = new Paddle(sensor, tmp, PADDLE_WIDTH, PADDLE_HEIGHT, Color.ORANGE);
        paddle.addToGame(this);
    }
}
