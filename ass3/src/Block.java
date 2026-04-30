/**
 * Block class represents a rectangular block that can be collided with.
 * It extends the Rectangle class and implements the Collidable interface,
 * providing methods to get its collision shape and to handle collisions with a ball.
 */
public class Block extends Rectangle implements Collidable {
    /**
     * Create a new block with the specified upper left corner, width, and height.
     * @param upperLeft the upper left corner of the block
     * @param width the length of the horizontal side of the block
     * @param height the length of the vertical side of the block
     */
    public Block(Point upperLeft, double width, double height) {
        super(upperLeft, width, height);
    }

    /**
     * Get the "collision shape" of the object,
     * which is the rectangle that defines its boundaries for collision detection.
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
        double newDx;
        double newDy;
        double x = collisionPoint.getX();
        double y = collisionPoint.getY();

        if (Helper.doubleEq(x, this.getLeft())) {
            newDx = -Math.abs(oldDx);
        } else if (Helper.doubleEq(x, this.getRight())) {
            newDx = Math.abs(oldDx);
        } else {
            newDx = oldDx;
        }
        if (Helper.doubleEq(y, this.getTop())) {
            newDy = -Math.abs(oldDy);
        } else if (Helper.doubleEq(y, this.getBottom())) {
            newDy = Math.abs(oldDy);
        } else {
            newDy = oldDy;
        }
        return new Velocity(newDx, newDy);
    }
}
