import biuoop.DrawSurface;

/**
 * Represents a ball with a center point, radius, and color.
 * Provides methods to access the ball's properties and to draw the ball on a given surface.
 */
public class Ball {
    private Point point;
    private final int radius;
    private final java.awt.Color color;
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
     * Move the ball one step according to its current velocity.
     * If the ball hits boundaries, appropriate velocity component will be reversed.
     * Bouncing velocity is calculated by math.abs to avoid bouncing loops if the ball starts on a boundary.
     */
    public void moveOneStep() {
        double nextX = point.getX() + velocity.getDx();
        if (nextX < radius) {
            velocity.reassign(Math.abs(velocity.getDx()), velocity.getDy());
        } else if (nextX > Helper.WIDTH - radius) {
            velocity.reassign(-Math.abs(velocity.getDx()), velocity.getDy());
        }
        double y = point.getY() + velocity.getDy();
        if (y < radius) {
            velocity.reassign(velocity.getDx(), Math.abs(velocity.getDy()));
        } else if (y > Helper.HEIGHT - radius) {
            velocity.reassign(velocity.getDx(), -Math.abs(velocity.getDy()));
        }
        this.point = this.velocity.applyToPoint(this.point);
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
     * Draw the ball on the given DrawSurface.
     * @param surface the surface on which to draw the ball
     */
    public void drawOn(DrawSurface surface) {
        surface.fillCircle((int) this.point.getX(), (int) this.point.getY(), this.radius);
    }
}