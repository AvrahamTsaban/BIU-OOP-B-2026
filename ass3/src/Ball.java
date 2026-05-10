import java.awt.Color;
import java.util.Random;

import biuoop.DrawSurface;

/**
 * Represents a ball with a center point, radius, and color.
 *
 * <p>Provides methods to access and modify the ball's properties,
 * simulate movement with collision detection, and render the ball on a DrawSurface.
 * The ball can move within boundaries and bounce off rectangles (from inside or outside).
 * Movement is determined by a velocity vector.</p>
 *
 * <p><strong>Precision Note:</strong> {@link #getX()} and {@link #getY()} cast to int, which loses
 * precision. For precise position, use {@link #getCenter()}.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public class Ball implements Sprite {
    /**
     * Base speed for generating moving balls, used in relation to ball size.
     * relates to the sleep time of the animation to ensure consistency across different frame rates.
     */
    private static final double BASE_SPEED = Game.MS_PER_FRAME * 0.3;
    /** used to avoid division by zero and make speed scaling natural. */
    private static final double LOG_SHIFT = 2.0;
    /** maximum radius for which to apply speed scaling. */
    private static final int MAX_RADIUS_FOR_SPEED = 50;
    /** Default radius for balls in animations. */
    public static final int DEFAULT_RADIUS = 7;
    /** the full step size for movement. */
    private static final double FULL_STEP = 1.0;

    private Point point;
    private final int radius;
    private final Color color;
    private final Velocity velocity;
    private final GameEnvironment gameEnvironment;

    /**
     * Initialize a new ball with the given center, radius, and color.
     * Sets the initial velocity of the ball to 0, 0 (no movement).
     * @param point the center point of the ball
     * @param radius the radius of the ball
     * @param color the color of the ball
     * @param gameEnvironment the game environment in which the ball exists
     */
    public Ball(Point point, int radius, java.awt.Color color, GameEnvironment gameEnvironment) {
        this.point = point;
        this.radius = radius;
        this.color = color;
        this.velocity = new Velocity(0, 0);  // default velocity is (0, 0)
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Set the velocity of the ball.
     * @param v the new velocity to set for the ball
     */
    public void setVelocity(Velocity v) {
        this.velocity.reassign(v.getDx(), v.getDy());
    }

    /**
     * Set the velocity of the ball using dx and dy values.
     * @param dx the change in x (horizontal velocity)
     * @param dy the change in y (vertical velocity)
     */
    public void setVelocity(double dx, double dy) {
        this.velocity.reassign(dx, dy);
    }

    /**
     * Get the current velocity of the ball.
     * @return a new Velocity object representing the current velocity of the ball
     */
    public Velocity getVelocity() {
        return new Velocity(velocity.getDx(), velocity.getDy());
    }

    /**
     * Get the x-coordinate of the ball's center.
     * @return the x-coordinate of the ball's center
     */
    public int getX() {
        return (int) this.point.getX();
    }

    /**
     * Get the y-coordinate of the ball's center.
     * @return the y-coordinate of the ball's center
     */
    public int getY() {
        return (int) this.point.getY();
    }

    /**
     * Get the size (radius) of the ball.
     * @return the radius of the ball
     */
    public int getSize() {
        return this.radius;
    }

    /**
     * Get the color of the ball.
     * @return the color of the ball
     */
    public java.awt.Color getColor() {
        return this.color;
    }

    /**
     * Get the center point of the ball.
     * @return a new Point object representing the center of the ball
     */
    public Point getCenter() {
        return new Point(this.point.getX(), this.point.getY());
    }

    /**
     * Move the ball one step according to its current velocity.
     * If the ball is predicted to hit the boundaries of the window,
     * its position and velocity will be adjusted to simulate a bounce, as many times as needed.
     */
    private void moveOneStep() {
        double fullStepLength = velocity.getSpeed();
        if (fullStepLength == 0) {
            return; // no movement if velocity is zero
        }
        /* Ensure the ball is outside of any collidable object to prevent getting stuck.
        If is inside one, its position is adjusted, and its Velocity is auto updated by keepOutside */
        Point keepOutsidePoint = gameEnvironment.keepOutside(this);
        if (keepOutsidePoint != null) {
            this.point = keepOutsidePoint;
            this.point = velocity.applyToPoint(this.point, Helper.DELTA);
            fullStepLength = velocity.getSpeed();
            if (fullStepLength == 0) {
                // safety measure (velocity should not be zero after keepOutside, but we haven't learnd exceptions yet
                return;
            }
        }

        /* Extend the collision probe by a radius scaled (~1.58x) to approximate diagonal distances for common angles,
        mostly 30°/60° (for 1.58 being the averahe of 1/sin of both angles) */
        final double adjustedRadius = (double) radius * 1.58;

        double remainingStep = FULL_STEP;
        double maxStepLength = fullStepLength;
        Point endOfStep = velocity.applyToPoint(this.point, remainingStep);
        Line trajectory = new Line(this.point, endOfStep);
        Line extendedTrajectory = trajectory.resize(maxStepLength + adjustedRadius);
        CollisionInfo collisionInfo = gameEnvironment.getClosestCollision(extendedTrajectory);
        if (collisionInfo == null) {
            this.point = endOfStep; // no collision, move freely
            return;
        }

        while (collisionInfo != null) {
            Point collisionPoint = collisionInfo.collisionPoint();
            Collidable collisionObject = collisionInfo.collisionObject();

            double wayToCollision = this.point.distance(collisionPoint) - adjustedRadius;
            double stepFraction = wayToCollision / fullStepLength;
            stepFraction = Math.max(0, Math.min(stepFraction, remainingStep));
            this.point = velocity.applyToPoint(this.point, stepFraction);

            Velocity newVelocity = collisionObject.hit(collisionPoint, velocity);
            velocity.reassign(newVelocity);

            this.point = velocity.applyToPoint(this.point, Helper.DELTA);
            stepFraction += Helper.DELTA;
            remainingStep -= stepFraction;
            if (remainingStep <= Helper.THRESHOLD) {
                // <= epsilon moves left
                return;
            }

            endOfStep = velocity.applyToPoint(this.point, remainingStep);
            trajectory = new Line(this.point, endOfStep);

            maxStepLength = fullStepLength * remainingStep;
            extendedTrajectory = trajectory.resize(maxStepLength + adjustedRadius);
            collisionInfo = gameEnvironment.getClosestCollision(extendedTrajectory);
        }
        this.point = endOfStep;
        return;
    }

    /**
     * Notify the ball that time has passed.
     * Moves the ball one step according to its current velocity.
     */
    public void timePassed() {
        moveOneStep();
    }

    /**
     * Draw the ball on the given DrawSurface.
     * @param surface the surface on which to draw the ball
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(getX(), getY(), this.radius);
    }

    /**
     * A helper method to create a ball with a random color and position, given a radius and a Random object.
     * @param radius the radius of the ball to create
     * @param inside the rectangle representing the area in which the ball should be created
     * @param rand the Random object to use for generating random values
     * @param ge the game environment to which the ball belongs
     * @return a new Ball object with the specified radius and a random color and position
     */
    public static Ball createBall(int radius, Rectangle inside, Random rand, GameEnvironment ge) {
        radius = validateRadius(radius, inside);
        float hue = rand.nextFloat(); // 0.0 to 1.0 - full spectrum of colors
        float saturation = 0.5f + rand.nextFloat() * 0.5f; // 0.5 to 1.0 - vibrant colors
        float brightness = 0.3f + rand.nextFloat() * 0.7f; // 0.3 to 1.0 - visible colors
        Color color = Color.getHSBColor(hue, saturation, brightness);
        double x = rand.nextDouble() * (inside.getWidth() - 2 * radius) + radius + inside.getLeft();
        double y = rand.nextDouble() * (inside.getHeight() - 2 * radius) + radius + inside.getTop();
        Point start = new Point(x, y);
        return new Ball(start, radius, color, ge);
    }

    /**
     * A helper method to validate and adjust the radius of a ball,
     * based on the maximum allowed radius and the size of the inside rectangle.
     * @param radius the radius to validate
     * @param inside the rectangle representing the area in which the ball should be created
     * @return the validated radius
     */
    private static int validateRadius(int radius, Rectangle inside) {
        int maxRadius = (int) Math.min(inside.getWidth(), inside.getHeight()) / 2;
        if (radius >= maxRadius) {
            return maxRadius;
        }
        if (radius <= 0) {
            return DEFAULT_RADIUS;
        }
        return radius;
    }

    /**
     * A helper method to create a moving ball with a random color and position, given a size and a Random object.
     * Ball's speed is determined by its size, with larger balls moving slower,
     * while balls larger than {@link #MAX_RADIUS_FOR_SPEED} get the same speed.
     * @param size the size of the ball to create
     * @param inside the rectangle representing the area in which the ball should be created
     * @param rand the Random object to use for generating random values
     * @param ge the game environment to which the ball belongs
     * @return a new Ball object with the specified size and a random color and position
     */
    public static Ball generateMovingBallBySize(int size, Rectangle inside, Random rand, GameEnvironment ge) {
        int adjustedSize = Math.min(size, MAX_RADIUS_FOR_SPEED);
        adjustedSize = Math.max(adjustedSize, 1); // prevent zero size for speed calculation
        // log(adjustedSize + LOG_SHIFT) to prevent division by zero and enforce speeds < BASE_SPEED for tiny balls
        double speed = BASE_SPEED / Math.log(adjustedSize + LOG_SHIFT);
        Velocity velocity = Velocity.semiRandVelocity(rand, speed);
        Ball ball = Ball.createBall(size, inside, rand, ge);
        ball.setVelocity(velocity);
        return ball;
    }

    /**
     * Add this ball to the given game as a sprite.
     * @param g the game to which to add this ball as a sprite
     */
    public void addToGame(Game g) {
        g.addSprite(this);
    }
}