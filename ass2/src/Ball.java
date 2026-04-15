import java.util.Random;

import org.w3c.dom.css.Rect;

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
     * Predict the next position of the ball based on its current velocity, without actually moving it.
     * @return a new Ball object representing the predicted position
     */
    public Ball predictMove() {
        Point nextPoint = this.getVelocity().applyToPoint(this.point);
        return new Ball(nextPoint, this.radius, this.color);
    }

    /**
     * Makes precise movement of the ball when it hits the boundaries,
     * by calculating the exact point of collision and adjusting the position and velocity accordingly.
     * Absolute value of velocity components is used instead of flipping the sign,
     * to avoid bouncing loops if the ball starts on a boundary.
     * No need to call velocity.applyToPoint() afterwards;
     * the new position is directly set during the collision handling.
     * @param r the rectangle representing the boundaries to check against
     */
    public void complexMove(Rectangle r) {
        double nextX = point.getX() + velocity.getDx();
        if (nextX - radius < r.leftX()) {
            bounceOnLeft(r.leftX());
        } else if (nextX + radius > r.rightX()) {
            bounceOnRight(r.rightX());
        } else {
            point = new Point(nextX, point.getY());
        }

        double nextY = point.getY() + velocity.getDy();
        if (nextY - radius < r.topY()) {
            bounceOnTop(r.topY());
        } else if (nextY + radius > r.bottomY()) {
            bounceOnBottom(r.bottomY());
        } else {
            point = new Point(point.getX(), nextY);
        }
    }

    /**
     * Helper method to handle bouncing on the left boundary.
     * @param leftX the x-coordinate of the left boundary
     */
    private void bounceOnLeft(double leftX) {
        double distanceFromBoundary = (point.getX() - radius) - leftX;
        double absDX = Math.abs(velocity.getDx());
        double newX = leftX + Math.max(absDX - distanceFromBoundary, radius);
        point = new Point(newX, point.getY());
        velocity.reassign(Math.abs(velocity.getDx()), velocity.getDy());
    }

    /**
     * Helper method to handle bouncing on the right boundary.
     * @param rightX the x-coordinate of the right boundary
     */
    private void bounceOnRight(double rightX) {
        double distanceFromBoundary = rightX - (point.getX() + radius);
        double absDX = Math.abs(velocity.getDx());
        double newX = rightX - Math.max((absDX - distanceFromBoundary), radius);
        point = new Point(newX, point.getY());
        velocity.reassign(-absDX, velocity.getDy());
    }

    /**
     * Helper method to handle bouncing on the top boundary.
     * @param topY the y-coordinate of the top boundary
     */
    private void bounceOnTop(double topY) {
        double distanceFromBoundary = (point.getY() - radius) - topY;
        double absDY = Math.abs(velocity.getDy());
        double newY = topY + Math.max(absDY - distanceFromBoundary, radius);
        point = new Point(point.getX(), newY);
        velocity.reassign(velocity.getDx(), absDY);
    }

    /**
     * Helper method to handle bouncing on the bottom boundary.
     * @param bottomY the y-coordinate of the bottom boundary
     */
    private void bounceOnBottom(double bottomY) {
        double distanceFromBoundary = bottomY - (point.getY() + radius);
        double absDY = Math.abs(velocity.getDy());
        double newY = bottomY - Math.max(absDY - distanceFromBoundary, radius);
        point = new Point(point.getX(), newY);
        velocity.reassign(velocity.getDx(), -absDY);
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
     * @param inside the rectangle representing the area in which the ball should be created
     * @param rand the Random object to use for generating random values
     * @return a new Ball object with the specified radius and a random color and position
     */
    public static Ball createBall(int radius, Rectangle inside, Random rand) {
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
     * A helper method to create a moving ball with a random color and position, given a size and a Random object.
     * Ball's speed is determined by its size, with larger balls moving slower,
     * while balls larger than 50 get the same speed.
     * @param size the size of the ball to create
     * @param inside the rectangle representing the area in which the ball should be created
     * @param rand the Random object to use for generating random values
     * @return a new Ball object with the specified size and a random color and position
     */
    public static Ball generateMovingBallBySize(int size, Rectangle inside, Random rand) {
        double adjustedSize = Math.min(size, 50);
        // using log(adjustedSize + 2) to avoid division by zero and to create a more natural speed scaling
        double speed = BASE_SPEED / Math.log(adjustedSize + 2);
        Velocity velocity = Velocity.semiRandVelocity(rand, speed);
        Ball ball = Ball.createBall(size, inside, rand);
        ball.setVelocity(velocity);
        return ball;
    }
}