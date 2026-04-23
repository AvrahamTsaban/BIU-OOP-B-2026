/**
 * Represents the result of evaluating one movement step against collision constraints.
 *
 * <p>The object stores a derived ball state ({@link #getCollidingBall()}) that already reflects
 * the computed position and velocity after handling the relevant collision case(s).
 * It also stores the unconsumed fraction of the original movement step
 * ({@link #getRemainingStep()}). This allows higher-level code to continue resolving
 * additional collisions recursively within the same frame.</p>
 *
 * <p>An empty collision is represented by a {@code null} colliding ball and a negative remaining step,
 * and can be created via {@link #emptyStep(Ball)}.
 * A collision may also represent a non-collision scenario where no actual collision occurs,
 * giving a full step and remaining step value of 0.</p>
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public class PartialStep {
    /** The full step value. */
    public static final double FULL_STEP = 1.0;
    /** A sentinel value representing no remaining step. */
    public static final double NO_REMAINING_STEP = 0.0;
    /** A sentinel value representing an error or empty collision. */
    public static final double ERROR_STEP = -1.0;

    /** Tiny distance used to move away from the wall after a bounce and avoid t=0 re-collision. */
    private static final double DEPENETRATION_DISTANCE = 10.0 * Helper.THRESHOLD;

    private final Ball collidingBall;
    private final double remainingStep;

    /**
     * Builds a collision result from a rectangle, a ball, and explicit collision direction flags.
     *
     * <p>The constructor computes a new ball state by applying bounce logic on the relevant axes,
     * or linear movement on axes without collision. The resulting ball and the remaining fraction
     * of the step are stored in this object.</p>
     *
     * @param r the rectangle involved in collision resolution
     * @param b the original moving ball before this collision resolution step
     * @param c a CollisionCase object containing the collision direction flags
     * @param stepToMove the fraction of the original step that remains after previous collision (if any)
     */
    public PartialStep(Rectangle r, Ball b, CollisionCase c, double stepToMove) {
        boolean fromTop = c.isFromTop();
        boolean fromBottom = c.isFromBottom();
        boolean fromRight = c.isFromRight();
        boolean fromLeft = c.isFromLeft();
        boolean outside = c.isFromOutside();
        if (b == null || r == null) {
            this.remainingStep = ERROR_STEP;
            this.collidingBall = null;
            return;
        }
        if (!fromBottom && !fromTop && !fromRight && !fromLeft) {
            this.remainingStep = NO_REMAINING_STEP;
            this.collidingBall = b.movePartialStep(stepToMove);
            return;
        }

        Velocity v = b.getVelocity();
        Ball ballY;
        if (fromBottom) {
            double fromBottomY = outside ? r.bottomY() : r.topY();
            ballY = bounceBelow(b, fromBottomY);
        } else if (fromTop) {
            double fromTopY = outside ? r.topY() : r.bottomY();
            ballY = bounceAbove(b, fromTopY);
        } else {
            ballY = linearlyMoveY(b);
        }
        double consumedStepY;
        if (v.getDy() == 0) {
            consumedStepY = stepToMove;
        } else {
            consumedStepY = b.getCenter().distance(ballY.getCenter()) / Math.abs(v.getDy());
        }
        double remainingStepY = stepToMove - consumedStepY;

        Ball ballX;
        if (fromRight) {
            double fromRightX = outside ? r.rightX() : r.leftX();
            ballX = bounceFromRight(b, fromRightX);
        } else if (fromLeft) {
            double fromLeftX = outside ? r.leftX() : r.rightX();
            ballX = bounceFromLeft(b, fromLeftX);
        } else {
            ballX = linearlyMoveX(b);
        }
        double consumedStepX;
        if (v.getDx() == 0) {
            consumedStepX = stepToMove;
        } else {
            consumedStepX = b.getCenter().distance(ballX.getCenter()) / Math.abs(v.getDx());
        }
        double remainingStepX = stepToMove - consumedStepX;

        Velocity newV;
        if (!isValidStep(remainingStepX) && !isValidStep(remainingStepY)) {
            this.remainingStep = NO_REMAINING_STEP;
            this.collidingBall = b.movePartialStep(stepToMove);
            return;
        } else if (!isValidStep(remainingStepX)) {
            this.remainingStep = remainingStepY;
            newV = ballY.getVelocity();
        } else if (!isValidStep(remainingStepY)) {
            this.remainingStep = remainingStepX;
            newV = ballX.getVelocity();
        } else if (Helper.doubleEq(remainingStepX, remainingStepY)) {
            this.remainingStep = remainingStepX;
            newV = new Velocity(ballX.getVelocity().getDx(), ballY.getVelocity().getDy());
        } else if (remainingStepX > remainingStepY) {
            this.remainingStep = remainingStepX;
            newV = ballX.getVelocity();
        } else {
            this.remainingStep = remainingStepY;
            newV = ballY.getVelocity();
        }
        Ball newBall = b.movePartialStep(stepToMove - remainingStep);
        newBall.setVelocity(newV);
        this.collidingBall = pushOutAfterBounce(r, newBall, c);
    }

    /**
     * Advances the post-bounce ball by a tiny distance along reflected velocity to avoid sticking.
     *
     * @param b the ball state immediately after bounce resolution
     * @param rect the rectangle involved in the bounce, used to determine push direction
     * @param c the collision case containing the collision direction flags
     * @return the same ball or a slightly advanced clone if speed is positive
     */
    private Ball pushOutAfterBounce(Rectangle rect, Ball b, CollisionCase c) {
        double x = b.getCenter().getX();
        double y = b.getCenter().getY();
        double margin = b.getSize() + DEPENETRATION_DISTANCE;
        if (c.isFromOutside()) {
            double topY = rect.topY() - margin;
            double bottomY = rect.bottomY() + margin;
            double rightX = rect.rightX() + margin;
            double leftX = rect.leftX() - margin;
            if (c.isFromLeft()) {
                x = Math.min(leftX, x);
            } else if (c.isFromRight()) {
                x = Math.max(rightX, x);
            }
            if (c.isFromTop()) {
                y = Math.min(topY, y);
            } else if (c.isFromBottom()) {
                y = Math.max(bottomY, y);
            }
            Ball pushed = new Ball(new Point(x, y), b.getSize(), b.getColor());
            pushed.setVelocity(b.getVelocity());
            return pushed;
        } else {
            double topY = rect.topY() + margin;
            double bottomY = rect.bottomY() - margin;
            double rightX = rect.rightX() - margin;
            double leftX = rect.leftX() + margin;
            if (c.isFromLeft()) {
                x = Math.min(rightX, x);
            } else if (c.isFromRight()) {
                x = Math.max(leftX, x);
            }
            if (c.isFromTop()) {
                y = Math.min(bottomY, y);
            } else if (c.isFromBottom()) {
                y = Math.max(topY, y);
            }
            Ball pushed = new Ball(new Point(x, y), b.getSize(), b.getColor());
            pushed.setVelocity(b.getVelocity());
            return pushed;
        }
    }

    /**
     * Creates a collision directly from an original ball and a precomputed post-collision ball.
     *
     * <p>Useful for corner/outside cases where the new state is calculated externally.
     * The remaining step is derived from the traveled distance relative to the original speed.</p>
     *
     * @param origin the original moving ball before collision handling
     * @param colliding the ball just after collision (position is near collision point, velocity already altered)
     * @param stepToMove the total step left to move (including the current collision step)
     */
    public PartialStep(Ball origin, Ball colliding, double stepToMove) {
        if (origin == null || colliding == null) {
            this.remainingStep = ERROR_STEP;
            this.collidingBall = null;
            return;
        }
        this.collidingBall = colliding;
        if (stepToMove < NO_REMAINING_STEP + Helper.THRESHOLD) {
            this.remainingStep = NO_REMAINING_STEP;
            return;
        }
        Point oldCenter = origin.getCenter();
        Point newCenter = colliding.getCenter();
        double stepLength = origin.getVelocity().getSpeed();
        double traveledDist = oldCenter.distance(newCenter);
        if (stepLength <= Helper.THRESHOLD) {
            this.remainingStep = stepToMove;
            return;
        }
        this.remainingStep = stepToMove - traveledDist / stepLength;
    }

    /**
     * Merges two collision candidates and keeps the one that should be applied first.
     *
     * <p>The selected collision is the one whose resulting center is closer to the origin center,
     * i.e., the earliest effective contact along the step.</p>
     *
     * @param origin the original moving ball used as a distance reference
     * @param first the first collision
     * @param second the second collision
     */
    public PartialStep(Ball origin, PartialStep first, PartialStep second) {
        if (origin == null || (first == null && second == null)) {
            this.remainingStep = ERROR_STEP;
            this.collidingBall = null;
            return;
        }
        if (first == null || first.isEmpty()) {
            this.remainingStep = second.getRemainingStep();
            this.collidingBall = second.getCollidingBall();
            return;
        }
        if (second == null || second.isEmpty()) {
            this.remainingStep = first.getRemainingStep();
            this.collidingBall = first.getCollidingBall();
            return;
        }

        if (first.getRemainingStep() >= second.getRemainingStep()) {
            this.remainingStep = first.getRemainingStep();
            this.collidingBall = first.getCollidingBall();
        } else {
            this.remainingStep = second.getRemainingStep();
            this.collidingBall = second.getCollidingBall();
        }
    }

    /**
     * Merges multiple collision candidates into a single effective collision.
     *
     * <p>The merge is performed iteratively using {@link #PartialStep(Ball, PartialStep, PartialStep)}
     * so that the earliest applicable collision is preserved.</p>
     *
     * @param origin the original moving ball used as a distance reference
     * @param collisions the collisions to merge
     */
    public PartialStep(Ball origin, PartialStep[] collisions) {
        if (origin == null || collisions == null || collisions.length == 0) {
            this.remainingStep = ERROR_STEP;
            this.collidingBall = null;
            return;
        }
        if (collisions.length == 1) {
            this.remainingStep = collisions[0].getRemainingStep();
            this.collidingBall = collisions[0].getCollidingBall();
            return;
        }
        PartialStep mergedCollision = new PartialStep(origin, collisions[0], collisions[1]);
        for (int i = 2; i < collisions.length; i++) {
            mergedCollision = new PartialStep(origin, mergedCollision, collisions[i]);
        }
        this.remainingStep = mergedCollision.getRemainingStep();
        this.collidingBall = mergedCollision.getCollidingBall();
    }

    /**
     * Creates a collision representing a full step with no collision.
     * @param b the original moving ball before this collision resolution step
     * @param remainingStep the fraction of the step to move (should be 1.0 for a full step)
     * @return a Collision object representing a full step with no collision
     */
    public static PartialStep maxStep(Ball b, double remainingStep) {
        Ball newBall = b.movePartialStep(remainingStep);
        return new PartialStep(b, newBall, remainingStep);
    }

    /**
     * Creates a collision representing no movement and no collision.
     * @param b the original moving ball before this collision resolution step
     * @return a Collision object duplicating the original ball and indicating an empty collision
     */
    public static PartialStep emptyStep(Ball b) {
        Ball newBall = new Ball(b.getCenter(), b.getSize(), b.getColor());
        newBall.setVelocity(b.getVelocity());
        return new PartialStep(b, newBall, ERROR_STEP);
    }

    /**
     * Advances the ball linearly on the Y axis without applying a vertical bounce.
     *
     * @param b the source ball
     * @return a new ball translated by {@code dy} on Y while preserving velocity
     */
    private Ball linearlyMoveY(Ball b) {
        Point point = b.getCenter();
        Velocity velocity = b.getVelocity();
        double newY = point.getY() + velocity.getDy();
        point = new Point(point.getX(), newY);
        Ball newBall = new Ball(point, b.getSize(), b.getColor());
        newBall.setVelocity(velocity);
        return newBall;
    }

    /**
     * Advances the ball linearly on the X axis without applying a horizontal bounce.
     *
     * @param b the source ball
     * @return a new ball translated by {@code dx} on X while preserving velocity
     */
    private Ball linearlyMoveX(Ball b) {
        Point point = b.getCenter();
        Velocity velocity = b.getVelocity();
        double newX = point.getX() + velocity.getDx();
        point = new Point(newX, point.getY());
        Ball newBall = new Ball(point, b.getSize(), b.getColor());
        newBall.setVelocity(velocity);
        return newBall;
    }

    /**
     * Applies horizontal bounce against a right-side boundary approached from the left.
     *
     * <p>The reflected state flips {@code dx} to negative while preserving {@code dy}.
     * Position is pushed just outside the boundary using radius and threshold safeguards.</p>
     *
     * @param b the source ball before horizontal bounce handling
     * @param x the x-coordinate of the right boundary
     * @return a new ball with updated center and reflected horizontal velocity
     */
    private Ball bounceFromLeft(Ball b, double x) {
        Point point = b.getCenter();
        Velocity velocity = b.getVelocity();
        int radius = b.getSize();
        double newX = x - (radius + Helper.THRESHOLD);
        point = new Point(newX, point.getY());
        Ball newBall = new Ball(point, radius, b.getColor());
        newBall.setVelocity(-Math.abs(velocity.getDx()), velocity.getDy());
        return newBall;
    }

    /**
     * Applies horizontal bounce against a left-side boundary approached from the right.
     *
     * <p>The reflected state sets {@code dx} to positive while preserving {@code dy}.
     * Position is pushed just outside the boundary using radius and threshold safeguards.</p>
     *
     * @param b the source ball before horizontal bounce handling
     * @param x the x-coordinate of the left boundary
     * @return a new ball with updated center and reflected horizontal velocity
     */
    private Ball bounceFromRight(Ball b, double x) {
        Point point = b.getCenter();
        Velocity velocity = b.getVelocity();
        int radius = b.getSize();
        double newX = x + (radius + Helper.THRESHOLD);
        point = new Point(newX, point.getY());
        Ball newBall = new Ball(point, radius, b.getColor());
        newBall.setVelocity(Math.abs(velocity.getDx()), velocity.getDy());
        return newBall;
    }

    /**
     * Applies vertical bounce against a top boundary approached from below.
     *
     * <p>The reflected state sets {@code dy} to positive while preserving {@code dx}.
     * Position is pushed just outside the boundary using radius and threshold safeguards.</p>
     *
     * @param b the source ball before vertical bounce handling
     * @param y the y-coordinate of the top boundary
     * @return a new ball with updated center and reflected vertical velocity
     */
    private Ball bounceBelow(Ball b, double y) {
        Point point = b.getCenter();
        Velocity velocity = b.getVelocity();
        int radius = b.getSize();
        double newY = y + (radius + Helper.THRESHOLD);
        point = new Point(point.getX(), newY);
        Ball newBall = new Ball(point, radius, b.getColor());
        newBall.setVelocity(velocity.getDx(), Math.abs(velocity.getDy()));
        return newBall;
    }

    /**
     * Applies vertical bounce against a bottom boundary approached from above.
     *
     * <p>The reflected state flips {@code dy} to negative while preserving {@code dx}.
     * Position is pushed just outside the boundary using radius and threshold safeguards.</p>
     *
     * @param b the source ball before vertical bounce handling
     * @param y the y-coordinate of the bottom boundary
     * @return a new ball with updated center and reflected vertical velocity
     */
    private Ball bounceAbove(Ball b, double y) {
        Point point = b.getCenter();
        Velocity velocity = b.getVelocity();
        int radius = b.getSize();
        double newY = y - (radius + Helper.THRESHOLD);
        point = new Point(point.getX(), newY);
        Ball newBall = new Ball(point, radius, b.getColor());
        newBall.setVelocity(velocity.getDx(), -Math.abs(velocity.getDy()));
        return newBall;
    }

    /**
     * Checks if a given step value is within the valid range for remaining steps.
     * @param step the step value to check
     * @return true if the step is valid, false otherwise
     */
    private boolean isValidStep(double step) {
        return step >= NO_REMAINING_STEP - Helper.THRESHOLD && step <= FULL_STEP + Helper.THRESHOLD;
    }

    /**
     * Checks whether this object represents an empty/no remaining step result.
     *
     * @return true if no colliding ball is available or the remaining step is negative
     */
    public boolean isEmpty() {
        return (this.collidingBall == null) || (this.remainingStep <= ERROR_STEP + Helper.THRESHOLD);
    }

    /**
     * Checks whether this collision represents a full step with no collision.
     *
     * @return true if the remaining step is effectively zero (within threshold)
     */
    public boolean isMaxStep() {
        return this.remainingStep <= NO_REMAINING_STEP + Helper.THRESHOLD;
    }

    /**
     * Returns the computed ball state after applying this collision resolution.
     *
     * <p><strong>Implementation note:</strong> when {@link #isEmpty()} is true,
     * this method returns {@code null}.</p>
     *
     * @return the post-collision ball state, or {@code null} for an empty collision
     */
    public Ball getCollidingBall() {
        return this.collidingBall;
    }

    /**
     * Returns the fraction of the original movement step that is still unconsumed.
     *
     * @return remaining step in range {@code [0, 1]} for non-empty collisions,
     * or a negative value for empty collisions
     */
    public double getRemainingStep() {
        return this.remainingStep;
    }
}
