public class Block extends Rectangle implements Collidable {

    public Block(Point upperLeft, double width, double height) {
        super(upperLeft, width, height);
    }

    /**
     * Get the "collision shape" of the object, which is the rectangle that defines its boundaries for collision detection.
     * @return the collision shape of the object
     */
    public Rectangle getCollisionRectangle() {
        return this;
    }

    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     * According to the specifications, we reduce corner collisions to edge collisions (or allow pass-throughs).
     * @param collisionPoint the point of collision
     * @param currentVelocity the current velocity
     * @return the new velocity expected after the hit
     */
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
        double oldDx = currentVelocity.getDx();
        double oldDy = currentVelocity.getDy();
        double x = collisionPoint.getX();
        double y = collisionPoint.getY();
        if (Helper.doubleEq(x, this.getLeft()) || Helper.doubleEq(x, this.getRight())) {
            return new Velocity(-oldDx, oldDy);
        }
        if (Helper.doubleEq(y, this.getTop()) || Helper.doubleEq(y, this.getBottom())) {
            return new Velocity(oldDx, -oldDy);
        }
        return currentVelocity;
    }
}
