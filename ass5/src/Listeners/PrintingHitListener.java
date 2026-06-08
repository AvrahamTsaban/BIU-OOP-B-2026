package Listeners;

import sprites.Ball;
import sprites.Block;

/**
 * A simple implementation of the HitListener interface, prints a message to the console whenever a hit event occurs.
 */
public class PrintingHitListener implements HitListener {
    /**
     * {@inheritDoc}
     * Prints a message to the console whenever a block is hit by a ball.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
       System.out.println("A Block was hit.");
    }
}
