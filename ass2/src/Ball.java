import java.util.Random;
import java.awt.Color;
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
 * @version 1.0
 * @since 2024-06-05
 */
public class Ball {
    /**
     * Base speed for generating moving balls, used in relation to ball size.
     * relates to the sleep time of the animation to ensure consistency across different frame rates.
     */
    private static final double BASE_SPEED = Helper.SLEEP_TIME * 0.4;
    /** used to avoid division by zero and make speed scaling natural. */
    private static final double LOG_SHIFT = 2.0;

    private Point point;
    private final int radius;
    private final Color color;
    private final Velocity velocity;

    /**
     * Initialize a new ball with the given center, radius, and color.
     * Sets the initial velocity of the ball to 0, 0 (no movement).
     * @param point the center point of the ball
     * @param radius the radius of the ball
     * @param color the color of the ball
     */
    public Ball(Point point, int radius, java.awt.Color color) {
        this.point = point;
        this.radius = radius;
        this.color = color;
        this.velocity = new Velocity(0, 0);  // default velocity is (0, 0)
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
     * Move the ball a partial step according to its current velocity.
     * @param partialStep the fraction of the step to move
     * @return a new Ball object representing the ball at its new position
     */
    public Ball movePartialStep(double partialStep) {
        Point newPoint = velocity.applyToPoint(this.point, partialStep);
        Ball afterStep = new Ball(newPoint, this.radius, this.color);
        afterStep.setVelocity(this.velocity);
        return afterStep;
    }

    /**
     * Move the ball one step according to its current velocity.
     * If the ball is predicted to hit the boundaries of the window,
     * its position and velocity will be adjusted to simulate a bounce, as many times as needed.
     */
    public void moveOneStep() {
        PartialStep collision = Helper.SCREEN.collisionFromInside(this, PartialStep.FULL_STEP);
        if (collision == null || collision.isEmpty() || collision.isMaxStep()) {
            this.point = velocity.applyToPoint(this.point);
            return; // no collision to handle
        }
        double remainingStep;
        do {
            Ball collidingBall = collision.getCollidingBall();
            this.point = collidingBall.getCenter();
            this.velocity.reassign(collidingBall.getVelocity());
            remainingStep = collision.getRemainingStep();
            collision = Helper.SCREEN.collisionFromInside(this, remainingStep);
        } while (collision != null && !collision.isEmpty() && remainingStep > Helper.THRESHOLD);
    }

    /**
     * Move the ball one step according to its current velocity,
     * while checking for collisions with both inside and outside rectangles.
     * If a collision is predicted with any of the rectangles, the ball will bounce accordingly.
     * This method allows for interactions with multiple rectangles in the environment.
     *
     * <p>If there are no collisions, the ball will move using moveOneStep() without parameters,
     * which will handle bouncing on the screen boundaries if needed.
     * Therefore, no need to check for collisions with the screen boundaries separately. </p>
     *
     * @param inside an array of rectangles representing the inside boundaries
     * @param outside an array of rectangles representing the outside boundaries
     */
    public void moveOneStep(Rectangle[] inside, Rectangle[] outside) {
        if ((inside == null || inside.length == 0) && (outside == null || outside.length == 0)) {
            this.moveOneStep(); // this method takes care of bouncing on screen boundaries
            return;
        }
        PartialStep collision = this.getSumCollision(inside, outside, PartialStep.FULL_STEP);
        double remainingStep;
        do {
            Ball collidingBall = collision.getCollidingBall();
            this.point = collidingBall.getCenter();
            this.velocity.reassign(collidingBall.getVelocity());
            remainingStep = collision.getRemainingStep();
            collision = this.getSumCollision(inside, outside, remainingStep);
        } while (collision != null && !collision.isEmpty() && remainingStep > Helper.THRESHOLD);
        return;
    }

    /**
     * Calculate the combined collision of the ball with multiple inside and outside rectangles for a given step.
     * @param inside an array of rectangles representing the boundaries to check for collisions from the inside
     * @param outside an array of rectangles representing the boundaries to check for collisions from the outside
     * @param step the fraction of the original step to consider
     * @return Partialstep of the earliest collision and reflected velocity (empty PartialStep if no collision)
     */
    private PartialStep getSumCollision(Rectangle[] inside, Rectangle[] outside, double step) {
        if (step < Helper.THRESHOLD) {
            return PartialStep.emptyStep(this);
        }
        int insideLen = inside == null ? 0 : inside.length;
        PartialStep[] insideCollision = new PartialStep[insideLen];
        for (int i = 0; i < insideLen; i++) {
            if (inside[i] == null) {
                continue; // skip null rectangles
            }
            insideCollision[i] = inside[i].collisionFromInside(this, step);
        }
        PartialStep fromInside = new PartialStep(this, insideCollision);

        int outsideLen = outside == null ? 0 : outside.length;
        PartialStep[] outsideCollision = new PartialStep[outsideLen];
        for (int i = 0; i < outsideLen; i++) {
            if (outside[i] == null) {
                continue; // skip null rectangles
            }
            outsideCollision[i] = outside[i].collisionFromOutside(this, step);
        }
        PartialStep fromOutside = new PartialStep(this, outsideCollision);

        PartialStep sumCollision = new PartialStep(this, fromInside, fromOutside);
        PartialStep screenCollision = Helper.SCREEN.collisionFromInside(this, step);
        sumCollision = new PartialStep(this, sumCollision, screenCollision);
        return sumCollision;
    }

    /**
     * A wrapper method for moveOneStep for passing a single inside and outside rectangle instead of arrays.
     * @param inside the rectangle representing the inside boundary, or null if there is no inside boundary
     * @param outside the rectangle representing the outside boundary, or null if there is no outside boundary
     */
    public void moveOneStep(Rectangle inside, Rectangle outside) {
        Rectangle[] insideArr = inside == null ? new Rectangle[0] : new Rectangle[]{inside};
        Rectangle[] outsideArr = outside == null ? new Rectangle[0] : new Rectangle[]{outside};
        this.moveOneStep(insideArr, outsideArr);
    }

    /**
     * A wrapper method for moveOneStep for passing a single inside rectangle instead of arrays.
     * @param inside the rectangle representing the inside boundary, or null if there is no inside boundary
     * @param outside the rectangle representing the outside boundary, or null if there is no outside boundary
     */
    public void moveOneStep(Rectangle inside, Rectangle[] outside) {
        Rectangle[] insideArr = inside == null ? new Rectangle[0] : new Rectangle[]{inside};
        this.moveOneStep(insideArr, outside);
    }


    /**
     * Predict the next position of the ball based on its current velocity, without actually moving it.
     * @return a new Ball object representing the predicted position
     */
    public Ball predictMove() {
        Point nextPoint = this.velocity.applyToPoint(this.point);
        return new Ball(nextPoint, this.radius, this.color);
    }


    /**
     * Predict the next position of the ball based on its current velocity, without actually moving it.
     * @param partialStep the fraction of the step to predict for
     * @return a new Ball object representing the predicted position
     */
    public Ball predictMove(double partialStep) {
        Point nextPoint = this.velocity.applyToPoint(this.point, partialStep);
        return new Ball(nextPoint, this.radius, this.color);
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
     * @return a new Ball object with the specified radius and a random color and position
     */
    public static Ball createBall(int radius, Rectangle inside, Random rand) {
        radius = validateRadius(radius, inside.getMaxRadius());
        float hue = rand.nextFloat(); // 0.0 to 1.0 - full spectrum of colors
        float saturation = 0.5f + rand.nextFloat() * 0.5f; // 0.5 to 1.0 - vibrant colors
        float brightness = 0.3f + rand.nextFloat() * 0.7f; // 0.3 to 1.0 - visible colors
        Color color = Color.getHSBColor(hue, saturation, brightness);
        double x = rand.nextDouble() * (inside.width() - 2 * radius) + radius + inside.leftX();
        double y = rand.nextDouble() * (inside.height() - 2 * radius) + radius + inside.topY();
        Point start = new Point(x, y);
        return new Ball(start, radius, color);
    }

    /**
     * A helper method to validate and adjust the radius of a ball,
     * based on the maximum allowed radius and the size of the inside rectangle.
     * @param radius the radius to validate
     * @param maxRadius the maximum radius allowed based on the inside rectangle's dimensions
     * @return the validated radius
     */
    private static int validateRadius(int radius, int maxRadius) {
        if (radius >= maxRadius) {
            return maxRadius;
        }
        if (radius <= 0) {
            return Helper.DEFAULT_RADIUS;
        }
        return radius;
    }

    /**
     * A helper method to create a moving ball with a random color and position, given a size and a Random object.
     * Ball's speed is determined by its size, with larger balls moving slower,
     * while balls larger than 50 get the same speed.
     * @param size the size of the ball to create
     * @param inside the rectangle representing the area in which the ball should be created
     * @param rand the Random object to use for generating random values
     * @return a new Ball object with the specified size and a random color and position
     */
    public static Ball generateMovingBallBySize(int size, Rectangle inside, Random rand) {
        int adjustedSize = Math.min(size, 50);
        adjustedSize = Math.max(adjustedSize, 1); // prevent zero size for speed calculation
        // log(adjustedSize + LOG_SHIFT) to prevent division by zero and enforce speeds < BASE_SPEED for tiny balls
        double speed = BASE_SPEED / Math.log(adjustedSize + LOG_SHIFT);
        Velocity velocity = Velocity.semiRandVelocity(rand, speed);
        Ball ball = Ball.createBall(size, inside, rand);
        ball.setVelocity(velocity);
        return ball;
    }
}