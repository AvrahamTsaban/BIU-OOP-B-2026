public interface Collidable {
    /**
     * Get the "collision shape" of the object, which is the rectangle that defines its boundaries for collision detection.
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
}