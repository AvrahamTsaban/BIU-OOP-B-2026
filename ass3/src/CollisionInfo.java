/**
 * CollisionInfo class represents information about a collision that is about to occur.
 * It contains the point at which the collision occurs and the collidable object involved in the collision.
 */
public class CollisionInfo {
    private final Point collisionPoint;
    private final Collidable collisionObject;

    /**
     * Initialize a new CollisionInfo with the given collision point and collidable object.
     * @param collisionPoint the point at which the collision occurs
     * @param collisionObject the collidable object involved in the collision
     */
    public CollisionInfo(Point collisionPoint, Collidable collisionObject) {
        this.collisionPoint = collisionPoint;
        this.collisionObject = collisionObject;
    }

    /**
     * Get the point at which the collision occurs.
     * @return the collision point
     */
    public Point collisionPoint() {
        return collisionPoint;
    }

    /**
     * Get the collidable object involved in the collision.
     * @return the collidable object
     */
    public Collidable collisionObject() {
        return collisionObject;
    }
}