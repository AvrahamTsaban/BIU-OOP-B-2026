import java.util.ArrayList;

/**
 * GameEnvironment class represents all collidable objects in the environment in which the game takes place.
 * It provides methods to add collidable objects,
 * and to determine the closest collision that will occur along a given trajectory.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public class GameEnvironment {
    private final ArrayList<Collidable> collidables;

    /**
     * Create a new game environment, ready to hold collidable objects.
     */
    public GameEnvironment() {
        collidables = new ArrayList<Collidable>();
    }

    /**
     * Add the given collidable to the environment.
     * @param c the collidable to add
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    /**
     * Gets a line representing the trajectory of an object moving from line.start() to line.end().
     * If this object will not collide with any of the collidables in this collection,
     * returns null. Else, returns the information about the closest collision that is going to occur.
     * @param trajectory the line representing the trajectory of the moving object
     * @return the information about the closest collision that is to occur, or null if no collision will occur
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        if (trajectory == null || Helper.doubleEq(trajectory.length(), 0) || trajectory.length() < 0) {
            return null;
        }
        Point start = trajectory.start();
        Point collisionPoint = null;
        Collidable collisionObject = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < collidables.size(); i++) {
            Collidable currCollidable = collidables.get(i);
            Rectangle rect = currCollidable.getCollisionRectangle();
            Point intersection = trajectory.closestIntersectionToStartOfLine(rect);
            if (intersection == null) {
                continue;
            }
            double distance = start.distance(intersection);
            if (distance < minDistance) {
                collisionObject = currCollidable;
                collisionPoint = intersection;
                minDistance = distance;
            }
        }
        if (collisionObject == null || collisionPoint == null) {
            return null;
        }

        return new CollisionInfo(collisionPoint, collisionObject);
    }

    /**
     * Keep the ball outside of any collidable object that may get on it while moving,
     * by adjusting its velocity and returning a recommended new center if necessary.
     * If the ball is inside any collidable object, adjusts its velocity and returns a recommended new center.
     * Static objects will not be checked.
     * @param ball the ball to check and adjust if necessary
     * @return a CollisionInfo object containing updated ball center and collision details,
     * or null if no adjustment was needed
     */
    public CollisionInfo keepOutside(Ball ball) {
        if (ball == null) {
            return null;
        }
        double r = (double) ball.getSize();
        Point ballCenter = ball.getCenter();
        Point newCenter = ballCenter;
        CollisionInfo finalCollisionInfo = null;

        for (Collidable collidable : collidables) {
            CollisionInfo collisionInfo = collidable.keepOutside(newCenter, r);
            if (collisionInfo == null) {
                continue;
            }
            finalCollisionInfo = collisionInfo;
            newCenter = collisionInfo.recommendedBallCenter();
        }
        return newCenter.equals(ballCenter) ? null : finalCollisionInfo;
    }

    /**
     * Check if a point is inside any of the collidable objects in this environment,
     * with a given margin (e.g. ball radius).
     * @param p the point to check
     * @param radius the margin to consider (e.g. the radius of the ball)
     * @return true if the point is inside any collidable object with the given margin, false otherwise
     */
    public boolean isInsideCollidable(Point p, double radius) {
        for (Collidable collidable : collidables) {
            if (collidable.isInside(p, radius)) {
                return true;
            }
        }
        return false;
    }
}