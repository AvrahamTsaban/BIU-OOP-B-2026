/**
 * Collidable interface represents an object that can be collided with.
 * It provides methods to get the collision shape and to handle collisions.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public interface Collidable {
    /**
     * Get the "collision shape" of the object,
     * which is the rectangle that defines its boundaries for collision detection.
     * @return the collision shape of the object
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     * @param collisionPoint the point of collision
     * @param currentVelocity the current velocity
     * @return the new velocity expected after the hit
     */
    Velocity hit(Point collisionPoint, Velocity currentVelocity);

    /**
     * Get a Ball that is suspected of being inside the collision shape of the object,
     * and ensure that it is outside of it.
     * If the ball is inside the collision shape, returns a point just outside the collision shape of the object.
     * Every collidable object may define its own way of choosing the point to return, as long as it is outside of it.
     * Otherwise, returns the current center of the ball.
     * Used to prevent moving objects from getting on the ball.
     * @param ballCenter the center point of the ball that may be inside the collision shape of the object
     * @return a point just outside the collision shape of the object
     */
    //TODO add to UML
    Point keepOutside(Point ballCenter);
}