import java.util.Random;

/**
 * A class representing a velocity in 2D space, defined by changes in x and y coordinates.
 */
public class Velocity {
        private double dx;
        private double dy;

    /**
     * Initialize a new Velocity with the given changes in x and y.
     * @param dx the change in x
     * @param dy the change in y
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Alternative constructor to create a Velocity from an angle and speed.
     * @param angle the angle of the velocity in degrees (0 degrees is up, 90 degrees is right)
     * @param speed the speed of the velocity
     * @return a new Velocity instance with the calculated dx and dy values
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double correctedAngle = 90 - angle;
        double radians = Math.toRadians(correctedAngle);
        double dx = Math.cos(radians) * speed;
        double dy = Math.sin(radians) * speed;
        return new Velocity(dx, dy);
    }

    /**
     * Get the angle of this velocity in degrees.
     * @return the angle of this velocity in degrees (0 degrees is up, 90 degrees is right)
     */
    public double getAngle() {
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        return (90 - angle + 360) % 360; // Convert to the desired angle format
    }

    /**
    * Get the speed of this velocity.
    * @return the speed of this velocity, calculated as the magnitude of the dx and dy components
    */
    public double getSpeed() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Apply this velocity to the given point, returning a new point with the updated position.
     * @param p the point to apply the velocity to
     * @return a new Point with the updated position after applying the velocity
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }

    /**
     * Get new dx, dy values.
     * @param dx the change in x
     * @param dy the change in y
     */
    public void reassign(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get the change in y (dy) of this velocity.
     * @return the change in y (dy) of this velocity
     */
    public double getDx() {
        return this.dx;
    }

    /**
     * Get the change in y (dy) of this velocity.
     * @return the change in y (dy) of this velocity
     */
    public double getDy() {
        return this.dy;
     }

     /**
     * A helper method to generate a random velocity for the ball.
     * @param rand a Random object to generate random numbers
     * @return a Velocity object with random dx and dy values
     */
    public static Velocity randVelocity(Random rand) {
        double speed = (rand.nextDouble() - 0.5) * 10; // random speed between -30 and 30
        double angle = rand.nextDouble() * 360; // random angle between 0 and 360 degrees
        return Velocity.fromAngleAndSpeed(angle, speed);
    }

     /**
     * A helper method to generate a random velocity for the ball.
     * @param rand a Random object to generate random numbers
     * @param speed the speed of the velocity
     * @return a Velocity object with random dx and dy values
     */
    public static Velocity semiRandVelocity(Random rand, double speed) {
        double angle = 0;
        while (Helper.doubleEq(angle, 0) || Helper.doubleEq(angle, 90)
                || Helper.doubleEq(angle, 180) || Helper.doubleEq(angle, 270)) {
            angle = rand.nextDouble() * 360; // random angle between 0 and 360 degrees
        }
        return Velocity.fromAngleAndSpeed(angle, speed);
    }
}