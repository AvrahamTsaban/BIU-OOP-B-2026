/**
 * Utility class for calculating ball-corner collisions with rectangles.
 *
 * <p>This class handles the complex calculations for determining the collision point of the ball with corners.
 * While calculating the time to collision, the reflected velocity, and the final position after the bounce.
 * </p>
 *
 * <p>This is a utility class with a private constructor to prevent instantiation.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public final class CornerCalc {
    /** Private constructor to prevent instantiation. */
    private CornerCalc() { }

    /**
     * Calculates the new ball after colliding with the corner of the rectangle.
     * finds the time to collision,
     * calculates the collision point and the velocity after reflection,
     * then calculates the remaining time after collision and the final position of the ball after the move.
     * If collision occurs,
     * returns a new Ball representing the state of the ball after colliding with the corner.
     * The ball represents the new position after step and reflected velocity,
     * so no further ball moves are needed for current step.
     * Else returns null.
     *
     * @param r the rectangle involved in the collision
     * @param ball the ball involved in the collision
     * @return a new Ball representing the state of the ball (position and velocity) after collision
     */
    public static Ball calc(Rectangle r, Ball ball) {
        Velocity v = ball.getVelocity();
        double t = findTimeToCollision(ball, r);
        if (Double.isInfinite(t)) {
            // collision does not occur during the move
            return null;
        }
        double newX = ball.getCenter().getX() + t * v.getDx();
        double newY = ball.getCenter().getY() + t * v.getDy();
        Point simCenter = new Point(newX, newY);
        Ball colliding = new Ball(simCenter, ball.getSize(), ball.getColor());

        Point rLT = new Point(r.leftX(), r.topY());
        // default value to avoid null pointer errors, overridden if there is a closer corner
        Point collisionPt = rLT;
        double minDistance = simCenter.distance(rLT);

        Point rRT = new Point(r.rightX(), r.topY());
        double distRT = simCenter.distance(rRT);
        if (distRT < minDistance) {
            minDistance = distRT;
            collisionPt = rRT;
        }

        Point rLB = new Point(r.leftX(), r.bottomY());
        double dist = simCenter.distance(rLB);
        if (dist < minDistance) {
            minDistance = dist;
            collisionPt = rLB;
        }

        Point rRB = new Point(r.rightX(), r.bottomY());
        dist = simCenter.distance(rRB);
        if (dist < minDistance) {
            minDistance = dist;
            collisionPt = rRB;
        }

        Velocity newV = reflectVelocity(v, collisionPt, colliding);
        colliding.setVelocity(newV);
        double remainingT = Math.max(1 - t, 0); // ensure non-negative remaining time
        double finalX = simCenter.getX() + remainingT * newV.getDx();
        double finalY = simCenter.getY() + remainingT * newV.getDy();
        Point finalCenter = new Point(finalX, finalY);
        Ball finalB = new Ball(finalCenter, ball.getSize(), ball.getColor());
        finalB.setVelocity(newV);
        return finalB;
    }

    /**
     * Calculates the reflected velocity of the ball after colliding with the corner.
     * Uses the normal vector from the collision point to the ball center to calculate the reflection.
     * @param v the initial velocity of the ball
     * @param collisionPt the point of collision
     * @param colliding the ball involved in the collision
     * @return the reflected velocity of the ball after colliding with the corner
     */
    private static Velocity reflectVelocity(Velocity v, Point collisionPt, Ball colliding) {
        // Calculate normal vector from collision point to ball center, normalized by radius
        double nx = (colliding.getCenter().getX() - collisionPt.getX()) / colliding.getSize();
        double ny = (colliding.getCenter().getY() - collisionPt.getY()) / colliding.getSize();
        // Reflect velocity: v' = v - 2(v·n)n
        double projection = v.getDx() * nx + v.getDy() * ny;
        if (projection > 0) { // using threshold is negligible
            // If projection is positive, the velocity is already moving away from the corner
            return v;
        }
        double rx = v.getDx() - 2 * projection * nx;
        double ry = v.getDy() - 2 * projection * ny;
        return new Velocity(rx, ry);
    }

    /**
     * Finds the time (in range (0, 1], 1 is full move) at which the ball collides with any corner of the rectangle.
     *
     * <p>Uses a quadratic formula to solve for collision time with each corner independently,
     * then returns the earliest collision time if one exists within the move duration.</p>
     *
     * @param b the ball to check for collision
     * @param r the rectangle whose corners to check
     * @return the time to collision (0 to 1], or Double.POSITIVE_INFINITY if no collision occurs during the move
     */
    private static double findTimeToCollision(Ball b, Rectangle r) {
        Point rLT = new Point(r.leftX(), r.topY());
        Point rRT = new Point(r.rightX(), r.topY());
        Point rLB = new Point(r.leftX(), r.bottomY());
        Point rRB = new Point(r.rightX(), r.bottomY());
        double t = Double.POSITIVE_INFINITY;
        t = Math.min(t, timeTo(rLT, b));
        t = Math.min(t, timeTo(rRT, b));
        t = Math.min(t, timeTo(rLB, b));
        t = Math.min(t, timeTo(rRB, b));
        return t;
    }

    /**
     * Calculates the collision point based on the time to collision and the ball's velocity.
     *
     * <p>Calculation:
     * x0^2 + y0^2 - radius^2 == distance^2.
     * On time t ∈ (0,1] (representing part of move), it becomes
     * f(t) == (x0 + t*dx)^2 + (y0 + t*dy)^2 - radius^2.
     * f(t) == 0 is the time of collision.
     * (x0 + t*dx)^2 + (y0 + t*dy)^2 - radius^2 == 0
     * It expands to
     * t^2(dx^2 + dy^2) + 2*t*(x0*dx + y0*dy) + x0^2 + y0^2 - radius^2 == 0
     * solving for t using the quadratic formula gives
     * a = dx^2 + dy^2
     * b = 2*(x0*dx + y0*dy)
     * c = x0^2 + y0^2 - radius^2
     * Afterwards, we may neglect the higher root because it represents a collision of the far side of the ball,
     * meaning a collision have already occurred.</p>
     *
     * @param simPt the point to calculate the time to
     * @param ball the ball to check for collision
     * @return the time to the simulation point, or Double.POSITIVE_INFINITY if there is no collision
     */
    private static double timeTo(Point simPt, Ball ball) {
        double x0 = ball.getCenter().getX() - simPt.getX();
        double y0 = ball.getCenter().getY() - simPt.getY();
        double dx = ball.getVelocity().getDx();
        double dy = ball.getVelocity().getDy();
        double radius = ball.getSize();

        double a = dx * dx + dy * dy;
        double b = 2 * (x0 * dx + y0 * dy);
        double c = x0 * x0 + y0 * y0 - radius * radius;
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return Double.POSITIVE_INFINITY; // no collision
        }

        double sqrtDisc = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDisc) / (2 * a);
        if (t1 > 0 - Helper.THRESHOLD && t1 <= 1 + Helper.THRESHOLD) {
            return t1; // collision occurs during the move
        }
        return Double.POSITIVE_INFINITY; // collision does not occur during the move
        /* If t1 < 0, collision occurs before the move starts, so t2 is irrelevant
        If t1 > 1, collision occurs after the move ends, t2 > t1 > 1 and is also irrelevant. */
    }
}