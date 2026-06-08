package sprites;
import geometry.Helper;
import geometry.Point;
import geometry.Rectangle;
import managers.Sprite;
import managers.Collidable;
import managers.HitNotifier;
import managers.Game;
import biuoop.DrawSurface;
import java.awt.Color;
import java.util.List;

import Listeners.HitListener;

import java.util.ArrayList;

/**
 * Block class represents a rectangular block that can be collided with.
 * It extends the Rectangle class and implements the Collidable interface,
 * providing methods to get its collision shape and to handle collisions with a ball.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public class Block extends Rectangle implements Collidable, Sprite, HitNotifier {
    private final Color color;
    private final List<HitListener> hitListeners;

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
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
     * Create a new block with the specified upper left corner, width, and height, using a default color.
     * @param upperLeft the upper left corner of the block
     * @param width the length of the horizontal side of the block
     * @param height the length of the vertical side of the block
     */
    public Block(Point upperLeft, double width, double height) {
        this(upperLeft, width, height, Color.GRAY);
    }

    /**
     * Get the "collision shape" of the object,
     * which is the rectangle that defines its boundaries for collision detection.
     * @return the collision shape of the object
     */
    public Rectangle getCollisionRectangle() {
        return (Rectangle) this;
    }

    /**
     * Notify the object that we collided with it at collisionPoint with a given velocity.
     * Returns the new velocity expected after the hit (based on the force the object inflicted on us),
     * after notifying all hit listeners about the hit event if hitter colour not equals this block colour.
     * According to the specifications, we reduce corner collisions to edge collisions (or allow pass-throughs).
     * @param hitter the ball that hit this block
     * @param collisionPoint the point of collision
     * @param currentVelocity the current velocity
     * @return the new velocity expected after the hit
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
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

        this.tryNotifyHit(hitter);
        return new Velocity(newDx, newDy);
    }

    /**
     * Draw this block on the given DrawSurface, filling it with its color and drawing a black border around it.
     * @param d the DrawSurface on which to draw the block
     */
    public void drawOn(DrawSurface d) {
        if (d == null || this.color == null) {
            return;
        }
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

    /**
     * Keep the ball outside of any collidable object that may get on it while moving,
     * or after collision if the ball did not get out of the object.
     * @param ballCenter the center point of the ball that may be inside the collision shape of the object
     * @param ballRadius the radius of the ball
     * @return a point just outside the collision shape of the object
     */
    public CollisionInfo keepOutside(Point ballCenter, double ballRadius) {
        boolean isInside = isInside(ballCenter, ballRadius);
        if (!isInside) {
            return null;
        }

        double buffer = ballRadius + Helper.DELTA;
        double currentX = ballCenter.getX();
        double currentY = ballCenter.getY();
        double rightMinX = this.getRight() + buffer;
        double leftMaxX = this.getLeft() - buffer;
        double bottomMinY = this.getBottom() + buffer;
        double topMaxY = this.getTop() - buffer;

        double newX = currentX;
        double newY = currentY;
        double collisionX = currentX;
        double collisionY = currentY;

        double dxLeft = Math.abs(currentX - leftMaxX);
        double dxRight = Math.abs(currentX - rightMinX);
        double dyTop = Math.abs(currentY - topMaxY);
        double dyBottom = Math.abs(currentY - bottomMinY);

        double minDx = Math.min(dxLeft, dxRight);
        double minDy = Math.min(dyTop, dyBottom);
        if (minDx < minDy) {
            if (dxLeft < dxRight) {
                newX = leftMaxX;
                collisionX = this.getLeft();
            } else {
                newX = rightMinX;
                collisionX = this.getRight();
            }
        } else {
            if (dyTop < dyBottom) {
                newY = topMaxY;
                collisionY = this.getTop();
            } else {
                newY = bottomMinY;
                collisionY = this.getBottom();
            }
        }
        Point collisionPoint = new Point(collisionX, collisionY);
        Point newPoint = new Point(newX, newY);
        return new CollisionInfo(collisionPoint, this, newPoint);
    }

    /**
     * Check if a given point is contained within the block (including edges and safety margin).
     * @param p the point to check
     * @param radius the radius of the ball (used to determine the safety margin for containment)
     * @return true if the point is inside the block (or within the safety margin), false otherwise
     */
    public boolean isInside(Point p, double radius) {
        return super.isInside(p, radius);
    }

    /**
     * Check if the color of the given ball matches the color of this block.
     * @param ball the ball to check
     * @return true if the color of the ball matches the color of this block, false otherwise
     */
    public boolean ballColorMatch(Ball ball) {
        return this.color.equals(ball.getColor());
    }

    /**
     * Remove this block from the given game, removing it as both a Collidable and a Sprite.
     * @param g the game from which to remove this block
     */
    public void removeFromGame(Game g) {
        g.removeCollidable(this);
        g.removeSprite(this);
    }

    /**
     * {@inheritDoc}
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * {@inheritDoc}
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * Notify all hit listeners about a hit event if the ball's color does not match this block's color.
     * Child classes may want to notify hit listeners without checking for a color match,
     * so this method may be overridden by child classes to change this behavior.
     * @param hitter the ball that hit this block
     */
    protected void tryNotifyHit(Ball hitter) {
        this.notifyHit(hitter);
    }

    /**
     * Notify all hit listeners about a hit event.
     * @param hitter the ball that hit this block
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * {@inheritDoc}
     * If block is a mini-block, it changes the ball's color to match its own color, so we return this block's color.
     * Otherwise, it does not change the ball's color, so we return null.
     */
    public Color getNewColor() {
        return null;
    }

    /**
     * Get the color of this block.
     * @return the color of this block
     */
    protected Color getColor() {
        return this.color;
    }
}
