import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import java.awt.Color;
/**
 * A class representing a paddle in the game.
 * The paddle can move left and right in response to keyboard input,
 * and can collide with other objects in the game environment.
 * It implements the Sprite interface to be drawn on the screen,
 * and the Collidable interface to interact with colliding objects.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public class Paddle implements Sprite, Collidable {
    /** The speed of the paddle. */
    private static final double SPEED = Game.MS_PER_FRAME * 0.4;
    /** The number of partitions the paddle is divided into for collision response. */
    private static final int PART_NUM = 5;

    private Point upperLeft;
    private final double width;
    private final double height;
    private final Color color;
    private biuoop.KeyboardSensor keyboard;

    /**
     * Create a new paddle with the given upper-left corner, width, height, and color.
     * @param keyboard the keyboard sensor for controlling the paddle
     * @param upperLeft the upper-left corner of the paddle
     * @param width the width of the paddle
     * @param height the height of the paddle
     * @param color the color of the paddle
     */
    public Paddle(KeyboardSensor keyboard, Point upperLeft, double width, double height, Color color) {
        this.keyboard = keyboard;
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    /**
     * Move the paddle to the left by decreasing the x-coordinate of its upper-left corner by the defined speed.
     */
    public void moveLeft() {
        double newX = this.upperLeft.getX() - SPEED;
        if (newX < 0 + Game.BLOCK_WIDTH) {
            newX = Game.WIDTH - width; // wrap around to the right edge
        }
        this.upperLeft = new Point(newX, upperLeft.getY());
    }

    /**
     * Move the paddle to the right by increasing the x-coordinate of its upper-left corner by the defined speed.
     */
    public void moveRight() {
        double newX = this.upperLeft.getX() + SPEED;
        if (newX + width > Game.WIDTH - Game.BLOCK_WIDTH) {
            newX = 0; // wrap around to the left edge
        }
        this.upperLeft = new Point(newX, upperLeft.getY());
    }

    /**
     * Notify the paddle that time has passed. This method checks for left and right key presses
     * and moves the paddle accordingly.
     * If both keys are pressed, the paddle will not move.
     */
    public void timePassed() {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY) && keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            return;
        }
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        }
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    /**
     * Draw the paddle on the given DrawSurface. The paddle is drawn as a filled rectangle with its specified color.
     * @param d the DrawSurface on which to draw the paddle
     */
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle((int) upperLeft.getX(), (int) upperLeft.getY(), (int) width, (int) height);
    }

    /**
     * Return the collision rectangle of the paddle, which is used for collision detection in the game.
     * @return the collision rectangle of the paddle
     */
    public Rectangle getCollisionRectangle() {
        return new Rectangle(this.upperLeft, this.width, this.height);
    }

    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     * According to the specifications, we reduce corner collisions to edge collisions (or allow pass-throughs).
     * @param collisionPoint the point of collision
     * @param currentVelocity the current velocity
     * @return the new velocity expected after the hit
     */
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
        double partitionSize = this.width / (double) PART_NUM;
        double relativeX = collisionPoint.getX() - this.upperLeft.getX();
        int partition = (int) Math.ceil(relativeX / partitionSize);
        partition = Math.max(1, Math.min(partition, PART_NUM));
        switch (partition) {
            case 1:
                return Velocity.fromAngleAndSpeed(300, currentVelocity.getSpeed());
            case 2:
                return Velocity.fromAngleAndSpeed(330, currentVelocity.getSpeed());
            case 3:
                return new Velocity(currentVelocity.getDx(), -Math.abs(currentVelocity.getDy()));
            case 4:
                return Velocity.fromAngleAndSpeed(30, currentVelocity.getSpeed());
            case 5:
                return Velocity.fromAngleAndSpeed(60, currentVelocity.getSpeed());
            default:
                return new Velocity(currentVelocity.getDx(), -Math.abs(currentVelocity.getDy()));
        }
    }


    /**
     * Add the paddle to the given game as both a Collidable and a Sprite.
     * @param g the game to which to add this paddle
     */
    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    /**
     * If the ball is inside the Paddle, gives a new position for the ball, just above the paddle.
     * @param ballCenter the center point of the ball that may be inside the paddle
     * @param ballRadius the radius of the ball
     * @return a point just outside the collision shape of the paddle
     */
    //TODO add to UML
    public Point keepOutside(Point ballCenter, double ballRadius) {
        Rectangle rect = this.getCollisionRectangle();
        boolean isInside = rect.isInside(ballCenter, ballRadius);
        if (!isInside) {
            return null;
        }
        // lift the ball just above the paddle to keep it outside
        return new Point(ballCenter.getX(), upperLeft.getY() - ballRadius - Helper.DELTA);
    }

    /**
     * Check if a point is inside the collision shape of the paddle, with a given margin (e.g. ball radius).
     * @param p the point to check
     * @param radius the margin to consider (e.g. the radius of the ball)
     * @return true if the point is inside the collision shape of the paddle with the given margin, false otherwise
     */
    public boolean isInside(Point p, double radius) {
        Rectangle rect = this.getCollisionRectangle();
        return rect.isInside(p, radius);
    }
}