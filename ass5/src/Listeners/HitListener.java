package Listeners;

import sprites.Ball;
import sprites.Block;

/**
 * The HitListener interface defines the contract for objects that want to be notified when a block is hit by a ball.
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit by the hitter ball.
     * @param beingHit the Block that is being hit
     * @param hitter the Ball that is doing the hitting
     */
    void hitEvent(Block beingHit, Ball hitter);
}
