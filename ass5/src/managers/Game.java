package managers;
import geometry.Point;
import geometry.Rectangle;
import sprites.Block;
import sprites.MiniBlock;
import sprites.Paddle;
import sprites.Ball;
import sprites.BackGround;
import sprites.ScoreIndicator;
import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;
import biuoop.KeyboardSensor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import Listeners.HitListener;
import Listeners.BlockRemover;
import Listeners.BallRemover;
import Listeners.ScoreTrackingListener;
import Listeners.Counter;

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
 * @version 1.5
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
    /** The width of the game area excluding the blocks. */
    public static final int WIDTH_WITHOUT_BLOCKS = Game.WIDTH - 2 * Game.BLOCK_WIDTH;
    /** Width of the paddle. */
    private static final int PADDLE_WIDTH = 150;
    /** Height of the paddle. */
    private static final int PADDLE_HEIGHT = 20;
    /** Frames per second for the animation. */
    public static final int FPS = 60;
    /** Milliseconds per frame for the animation. */
    public static final int MS_PER_FRAME = 1000 / FPS;
    /** Number of balls to create at the start of the game. */
    private static final int BALL_NUMBER = 3;
    /** An array of colors for the blocks. */
    private static final Color[] COLOR_PALLETE = new Color[]{
        Color.GRAY, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK, Color.GREEN};
    /** The color of the background of the game area. */
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private final SpriteCollection sprites;
    private final GameEnvironment environment;
    private final GUI gui;
    private final Sleeper sleeper;
    private final Random rand;
    private final Counter remainingBlocks;
    private final Counter remainingBalls;
    private final Counter score;

    /**
     * Create a new Game with an empty SpriteCollection and an empty GameEnvironment.
     */
    public Game() {
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        gui = new GUI("double PingPong", WIDTH, HEIGHT);
        sleeper = new biuoop.Sleeper();
        rand = new Random();
        remainingBlocks = new Counter();
        remainingBalls = new Counter();
        score = new Counter();
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
     * remove the given collidable from the game environment.
     * @param c the collidable to remove from the game environment
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }

    /**
     * Remove the given sprite from the game.
     * @param s the sprite to remove from the game
     */
    public void removeSprite(Sprite s) {
        sprites.removeSprite(s);
    }

    /**
     * Initialize the game by creating the blocks and ball and adding them to the game.
     */
    public void initialize() {
        // note for maintainer: adds order determines draw order.
        addBackground();
        addBoundaries();
        addDeathRegion();
        addMiniBlocks();
        addScoreIndicator();
        addPaddle();
        for (int i = 0; i < BALL_NUMBER; i++) {
            addBall();
        }
    }


    /**
     * Run the game. The animation will continue indefinitely until the program is terminated.
     * The method uses a game loop that updates the game state and renders the sprites at a fixed frame rate.
     */
    public void run() {
        while (remainingBlocks.getValue() > 0 && remainingBalls.getValue() > 0) {
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
        ArrayList<String> messages = new ArrayList<>();
        if (remainingBalls.getValue() <= 0) {
            messages.add("Game Over");
            messages.add("Your score is: " + score.getValue());
        } else {
            score.increase(100);
            messages.add("You win!");
            messages.add("Your score is: " + score.getValue());
        }
        try {
            celebrate(messages);
        } catch (IllegalArgumentException e) {
            System.err.println("Error during celebration animation: \n" + e.getMessage());
        }
        gui.close();
    }

    /** A helper method to add the background to the game. */
    private void addBackground() {
        BackGround background = new BackGround(BACKGROUND_COLOR);
        background.addToGame(this);
    }

    /**
     * Create the blocks that form the boundaries of the game area, and  add them to collidables and sprites lists.
     */
    private void addBoundaries() {
        Point tmp = new Point(0, 0);
        Block top = new Block(tmp, WIDTH, BLOCK_WIDTH);
        tmp = new Point(0, BLOCK_WIDTH);
        Block left = new Block(tmp, BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH);
        tmp = new Point(WIDTH - BLOCK_WIDTH, BLOCK_WIDTH);
        Block right = new Block(tmp, BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH);

        Block[] boundaries = new Block[]{top, left, right};
        for (Block b : boundaries) {
            b.addToGame(this);
        }
    }

    /**
     * A helper method to add a "death region" block at the bottom of the game area,
     * which will remove balls that hit it and decrease the remaining balls counter.
     */
    private void addDeathRegion() {
        HitListener ballKiller = new BallRemover(this, remainingBalls);
        Point tmp = new Point(BLOCK_WIDTH, HEIGHT - BLOCK_WIDTH);
        Block deathRegion = new Block(tmp, WIDTH - 2 * BLOCK_WIDTH, BLOCK_WIDTH);
        deathRegion.addHitListener(ballKiller);
        deathRegion.addToGame(this);
    }

    /**
     * A helper method to add the mini blocks in a grid pattern on the right side of the game area.
     */
    private void addMiniBlocks() {
        HitListener blockKiller = new BlockRemover(this, remainingBlocks);
        HitListener scoreTracker = new ScoreTrackingListener(score);
        final int gridColumns = 12;
        final int gridRows = 5;
        final int anchorX = WIDTH - BLOCK_WIDTH;
        final int anchorY = 3 * BLOCK_WIDTH;

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
                MiniBlock toAdd = new MiniBlock(upperLeft, COLOR_PALLETE[a]);
                remainingBlocks.increase(1);
                toAdd.addHitListener(blockKiller);
                toAdd.addHitListener(scoreTracker);
                toAdd.addToGame(this);
            }
        }
    }

    /**
     * A helper method to add the score indicator to the game.
     */
    private void addScoreIndicator() {
        ScoreIndicator scoreIndicator = new ScoreIndicator(score);
        scoreIndicator.addToGame(this);
    }

    /**
     * A helper method to create a ball with a random color and position.
     *
     * <p><strong>Implementation note:</strong> should be called after all collidables are added to the game,
     * to ensure the ball is not created inside any of them.</p>
     */
    private void addBall() {
        Point tmp = new Point(BLOCK_WIDTH, BLOCK_WIDTH);
        Rectangle inside = new Rectangle(tmp, WIDTH - 2 * BLOCK_WIDTH, HEIGHT - 5 * BLOCK_WIDTH);
        Ball ball = null;
        do {
            ball = Ball.generateMovingBallBySize(Ball.DEFAULT_RADIUS, inside, rand, environment);
        } while (environment.isInsideCollidable(ball.getCenter(), Ball.DEFAULT_RADIUS));
        ball.addToGame(this);
        remainingBalls.increase(1);
    }

    /**
     * A helper method to create a paddle.
     */
    private void addPaddle() {
        Point tmp = new Point(WIDTH / 2.0 - PADDLE_WIDTH / 2.0, HEIGHT - BLOCK_WIDTH - PADDLE_HEIGHT);
        KeyboardSensor sensor = gui.getKeyboardSensor();
        Paddle paddle = new Paddle(sensor, tmp, PADDLE_WIDTH, PADDLE_HEIGHT, Color.ORANGE);
        paddle.addToGame(this);
    }

    /**
     * A helper method to display a celebration animation with the given messages.
     * Used to "celebrate" the end of the game, whether it's a win or a loss.
     * @param messages the messages to display during the celebration animation
     * @throws IllegalArgumentException if any of the messages are too long to be displayed properly
     */
    private void celebrate(final ArrayList<String> messages) throws IllegalArgumentException {
        final int charSize = 25;
        final int animationDuration = 2500;
        final int widthCenter = WIDTH / 2;
        int maxTextWidth = 0;
        for (String message : messages) {
            maxTextWidth = Math.max(maxTextWidth, message.length() * charSize / 2); // Approximate width of the text
            if (maxTextWidth > widthCenter) {
                throw new IllegalArgumentException("Message is too long to be displayed properly: \"" + message + "\"");
            }
        }
        final int xStart = widthCenter - maxTextWidth / 2;
        final int yStart = (HEIGHT - messages.size() * charSize) / 2;

        for (Color co : COLOR_PALLETE) {
            DrawSurface d = gui.getDrawSurface();
            this.sprites.drawAllOn(d);
            d.setColor(co);
            for (int i = 0; i < messages.size(); i++) {
                d.drawText(xStart, yStart + i * charSize, messages.get(i), charSize);
            }
            gui.show(d);
            sleeper.sleepFor(animationDuration / COLOR_PALLETE.length);
        }
    }
}