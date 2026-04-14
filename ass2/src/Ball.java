import java.util.Random;
import java.awt.Color;

import biuoop.DrawSurface;

/**
 * Represents a ball with a center point, radius, and color.
 * Provides methods to access the ball's properties and to draw the ball on a given surface.
 */
public class Ball {
    private Point point;
    private final int radius;
    private final Color color;
    private final Velocity velocity;
    private static final double BASE_SPEED = 20.0;

    /*todo: implement helper equals*/

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
     * Move the ball one step according to its current velocity.
     * If the ball is predicted to hit the boundaries of the window,
     * its position and velocity will be adjusted to simulate a bounce.
     */
    public void moveOneStep() {
        this.point = this.getVelocity().applyToPoint(this.point);
    }

    /**
     * Check if the predicted step of the ball is within the boundaries of a rectangle.
     * @param r the rectangle representing the boundaries to check against
     * @return true if the next step is within the boundaries, false otherwise
     */
    public boolean remainsIn(Rectangle r) {
        double nextX = point.getX() + velocity.getDx();
        double nextY = point.getY() + velocity.getDy();
        return nextX - radius >= r.leftX() && nextX + radius <= r.rightX()
            && nextY - radius >= r.topY() && nextY + radius <= r.bottomY();
    }

    /**
     * Makes pecise movement of the ball when it hits the boundaries,
     * by calculating the exact point of collision and adjusting the position and velocity accordingly.
     * Absolute value of velocity components is used instead of flipping the sign,
     * to avoid bouncing loops if the ball starts on a boundary.
     * No need to call velocity.applyToPoint() afterwards;
     * the new position is directly set during the collision handling.
     */
    public void complexMove() {
        double absDX = Math.abs(velocity.getDx());
        double nextX = point.getX() + velocity.getDx();
        //Bouncing velocity is calculated by math.abs to avoid bouncing loops if the ball starts on a boundary.
        if (nextX < radius) {
            double distanceFromBoundary = point.getX() - radius;
            double newX = Math.max(absDX - distanceFromBoundary, radius);
            point = new Point(newX, point.getY());
            velocity.reassign(Math.abs(velocity.getDx()), velocity.getDy());
        } else if (nextX + radius > Helper.WIDTH) {
            double distanceFromBoundary = Helper.WIDTH - radius - point.getX();
            double newX = Math.min(Helper.WIDTH - (absDX - distanceFromBoundary), Helper.WIDTH - radius);
            point = new Point(newX, point.getY());
            velocity.reassign(-absDX, velocity.getDy());
        } else {
            point = new Point(nextX, point.getY());
        }

        double absDY = Math.abs(velocity.getDy());
        double nextY = point.getY() + velocity.getDy();
        if (nextY < radius) {
            double distanceFromBoundary = point.getY() - radius;
            double newY = Math.max(absDY - distanceFromBoundary, radius);
            point = new Point(point.getX(), newY);
            velocity.reassign(velocity.getDx(), absDY);
        } else if (nextY + radius > Helper.HEIGHT) {
            double distanceFromBoundary = Helper.HEIGHT - radius - point.getY();
            double newY = Math.min(Helper.HEIGHT - (absDY - distanceFromBoundary), Helper.HEIGHT - radius);
            point = new Point(point.getX(), newY);
            velocity.reassign(velocity.getDx(), -absDY);
        } else {
            point = new Point(point.getX(), nextY);
        }
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
        surface.fillCircle((int) this.point.getX(), (int) this.point.getY(), this.radius);
    }

    /**
     * A helper method to create a ball with a random color and position, given a radius and a Random object.
     * @param radius the radius of the ball to create
     * @param rand the Random object to use for generating random values
     * @return a new Ball object with the specified radius and a random color and position
     */
    public static Ball createBall(int radius, Random rand) {
        float hue = rand.nextFloat(); // 0.0 to 1.0 - full spectrum of colors
        float saturation = 0.5f + rand.nextFloat() * 0.5f; // 0.5 to 1.0 - vibrant colors
        float brightness = 0.3f + rand.nextFloat() * 0.7f; // 0.3 to 1.0 - visible colors
        Color color = Color.getHSBColor(hue, saturation, brightness);
        double x = rand.nextDouble() * (Helper.WIDTH - 2 * radius) + radius;
        double y = rand.nextDouble() * (Helper.HEIGHT - 2 * radius) + radius;
        Point start = new Point(x, y);
        return new Ball(start, radius, color);
    }

    /**
     * A helper method to create a moving ball with a random color and position, given a size and a Random object.
     * Ball's speed is determined by its size, with larger balls moving slower,
     * while balls larger than 50 get the same speed.
     * @param size the size of the ball to create
     * @param rand the Random object to use for generating random values
     * @return a new Ball object with the specified size and a random color and position
     */
    public static Ball generateMovingBallBySize(int size, Random rand) {
        double adjustedSize = Math.min(size, 50);
        // using log(adjustedSize + 2) to avoid division by zero and to create a more natural speed scaling
        double speed = BASE_SPEED / Math.log(adjustedSize + 2);
        Velocity velocity = Velocity.semiRandVelocity(rand, speed);
        Ball ball = Ball.createBall(size, rand);
        ball.setVelocity(velocity);
        return ball;
    }
}