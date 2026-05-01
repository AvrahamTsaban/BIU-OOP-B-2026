import biuoop.DrawSurface;
import java.awt.Color;

/**
 * Block class represents a rectangular block that can be collided with.
 * It extends the Rectangle class and implements the Collidable interface,
 * providing methods to get its collision shape and to handle collisions with a ball.
 */
public class Block extends Rectangle implements Collidable, Sprite {
    private final Color color;

    /**
     * Create a new block with the specified upper left corner, width, and height.
     * @param upperLeft the upper left corner of the block
     * @param width the length of the horizontal side of the block
     * @param height the length of the vertical side of the block
     * @param color the color of the block
     */
    public Block(Point upperLeft, double width, double height, Color color) {
        super(upperLeft, width, height);
        this.color = color;
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

    /**
     * Draw this block on the given DrawSurface, filling it with its color and drawing a black border around it.
     * @param d the DrawSurface on which to draw the block
     */
    public void drawOn(DrawSurface d) {
        Point upperLeft = this.getUpperLeft();
        d.setColor(this.color);
        d.fillRectangle((int) upperLeft.getX(), (int) upperLeft.getY(), (int) this.getWidth(), (int) this.getHeight());
        d.setColor(java.awt.Color.BLACK);
        d.drawRectangle((int) upperLeft.getX(), (int) upperLeft.getY(), (int) this.getWidth(), (int) this.getHeight());
    }

    /**
     * Notify the block that time has passed.
     */
    public void timePassed() {
        // do nothing, blocks are static for now
    }

    /**
     * Add this block to the given game as both a Collidable and a Sprite.
     * @param g the game to which to add this block
     */
    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }
}
