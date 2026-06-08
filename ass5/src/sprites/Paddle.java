package sprites;
import geometry.Helper;
import geometry.Point;
import geometry.Line;
import geometry.Rectangle;
import managers.Collidable;
import managers.Game;
import managers.Sprite;
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
public class Paddle extends Rectangle implements Sprite, Collidable {
    /** The speed of the paddle. */
    private static final double SPEED = Game.MS_PER_FRAME * 0.4;
    /** The number of partitions the paddle is divided into for collision response. */
    private static final int PART_NUM = 5;

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
        super(upperLeft, width < Game.WIDTH_WITHOUT_BLOCKS / 2 ? width : Game.WIDTH_WITHOUT_BLOCKS / 2, height);
        this.keyboard = keyboard;
        this.color = color;
    }

    /**
     * Move the paddle to the left by decreasing the x-coordinate of its upper-left corner by the defined speed.
     */
    public void moveLeft() {
        double newX = super.getUpperLeft().getX() - SPEED;
        if (newX < 0 + Game.BLOCK_WIDTH) {
            // wrap around to the right edge
            newX += (double) Game.WIDTH_WITHOUT_BLOCKS;
        }
        super.move(new Point(newX, topY()));
    }

    /**
     * Move the paddle to the right by increasing the x-coordinate of its upper-left corner by the defined speed.
     */
    public void moveRight() {
        double newX = super.getUpperLeft().getX() + SPEED;
        if (newX > Game.WIDTH - Game.BLOCK_WIDTH) {
            // wrap around to the left edge
            newX -= (double) Game.WIDTH_WITHOUT_BLOCKS;
        }
        super.move(new Point(newX, topY()));
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
        if (isSplit()) {
            drawSplit(d);
            return;
        }
        Point upperLeft = super.getUpperLeft();
        double width = super.getWidth();
        double height = super.getHeight();
        d.fillRectangle((int) upperLeft.getX(), (int) upperLeft.getY(), (int) width, (int) height);
    }

    /**
     * Draw the split paddle on the given DrawSurface.
     * This method is called when the paddle is split across the game area,
     * and it draws the two parts of the paddle separately.
     * @param d the DrawSurface on which to draw the split paddle
     */
    private void drawSplit(DrawSurface d) {
        double firstPartWidth = Game.WIDTH - Game.BLOCK_WIDTH - leftEdge();
        double secondPartWidth = super.getWidth() - firstPartWidth;
        double height = super.getHeight();
        d.fillRectangle((int) leftEdge(), (int) topY(), (int) firstPartWidth, (int) height);
        d.fillRectangle(Game.BLOCK_WIDTH, (int) topY(), (int) secondPartWidth, (int) height);
    }

    /**
     * Return the collision rectangle of the paddle, which is used for collision detection in the game.
     * @return the collision rectangle of the paddle
     */
    public Rectangle getCollisionRectangle() {
        return (Rectangle) this;
    }

    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     * According to the specifications, we reduce corner collisions to edge collisions (or allow pass-throughs).
     * @param hitter the ball that hit this object
     * @param collisionPoint the point of collision
     * @param currentVelocity the current velocity
     * @return the new velocity expected after the hit
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double partitionSize = super.getWidth() / (double) PART_NUM;
        double relativeX = collisionPoint.getX() - super.getUpperLeft().getX();
        if (relativeX < 0) {
            relativeX += Game.WIDTH_WITHOUT_BLOCKS;
        }
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
    public CollisionInfo keepOutside(Point ballCenter, double ballRadius) {
        boolean isInside;
        if (isSplit()) {
            Rectangle[] parts = getParts();
            isInside = false;
            for (Rectangle part : parts) {
                if (part.isInside(ballCenter, ballRadius)) {
                    isInside = true;
                    break;
                }
            }
        } else {
            Rectangle rect = new Rectangle(super.getUpperLeft(), super.getWidth(), super.getHeight());
            isInside = rect.isInside(ballCenter, ballRadius);
        }
        if (!isInside) {
            return null;
        }
        Point collisionPoint = new Point(ballCenter.getX(), topY());
        // lift the ball to keep it outside
        Point recommendedBallCenter = new Point(ballCenter.getX(), topY() - ballRadius - Helper.DELTA);
        return new CollisionInfo(collisionPoint, this, recommendedBallCenter);
    }

    /**
     * Check if a point is inside the collision shape of the paddle, with a given margin (e.g. ball radius).
     * @param p the point to check
     * @param radius the margin to consider (e.g. the radius of the ball)
     * @return true if the point is inside the collision shape of the paddle with the given margin, false otherwise
     */
    public boolean isInside(Point p, double radius) {
        if (isSplit()) {
            Rectangle[] parts = getParts();
            for (Rectangle part : parts) {
                if (part.isInside(p, radius)) {
                    return true;
                }
            }
            return false;
        }
        Rectangle rect = new Rectangle(super.getUpperLeft(), super.getWidth(), super.getHeight());
        return rect.isInside(p, radius);
    }

    /**
     * Calculate the left edge of the paddle, taking into account wrapping around the game area.
     * @return the x-coordinate of the left edge of the paddle
     */
    private double leftEdge() {
        return super.getUpperLeft().getX();
    }

    /**
     * Calculate the y-coordinate of the top edge of the paddle.
     * @return the y-coordinate of the top edge of the paddle
     */
    private double topY() {
        return super.getUpperLeft().getY();
    }

    /**
     * Check if the paddle is split across the game area.
     * @return true if the paddle is split, false otherwise
     */
    private boolean isSplit() {
        return leftEdge() > Game.WIDTH - Game.BLOCK_WIDTH - super.getWidth();
    }

    /**
     * Calculate the intersection points of a given line with the edges of the paddle,
     * taking into account wrapping around the game area.
     * @param line the line to check for intersections
     * @return a list of intersection points
     */
    @Override
    public java.util.List<Point> intersectionPoints(Line line) {
        if (isSplit()) {
            Rectangle[] parts = getParts();
            java.util.List<Point> intersectionPoints = new java.util.ArrayList<>();
            for (Rectangle part : parts) {
                intersectionPoints.addAll(part.intersectionPoints(line));
            }
            return intersectionPoints;
        } else {
            return super.intersectionPoints(line);
        }
    }

    /**
     * Get the parts of the paddle as separate rectangles when it is split across the game area.
     * @return an array of two rectangles representing the split parts of the paddle
     */
    private Rectangle[] getParts() {
        double firstPartWidth = Game.WIDTH - Game.BLOCK_WIDTH - leftEdge();
        double secondPartWidth = super.getWidth() - firstPartWidth;
        double height = super.getHeight();
        Rectangle firstPart = new Rectangle(super.getUpperLeft(), firstPartWidth, height);
        Rectangle secondPart = new Rectangle(new Point(Game.BLOCK_WIDTH, topY()), secondPartWidth, height);
        return new Rectangle[]{firstPart, secondPart};
    }

    /**
     * Get a potential new color for the ball when it hits the paddle.
     * For the paddle, we do not change the ball's color.
     * @return null, indicating that no change to the ball's color is needed
     */
    public Color getNewColor() {
        return null;
    }
}