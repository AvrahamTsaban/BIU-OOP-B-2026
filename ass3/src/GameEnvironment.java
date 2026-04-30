import java.util.ArrayList;

/**
 * GameEnvironment class represents all collidable objects in the environment in which the game takes place.
 * It provides methods to add collidable objects,
 * and to determine the closest collision that will occur along a given trajectory.
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
        if (trajectory == null) {
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
}