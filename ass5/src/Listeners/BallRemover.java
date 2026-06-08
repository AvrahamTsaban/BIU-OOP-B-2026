package Listeners;

import managers.Game;
import sprites.Ball;
import sprites.Block;

/**
 * Responsible for removing balls from the game when they hit a specific block (e.g., the death region).
 * It keeps track of the number of remaining balls using a Counter object.
 */
public class BallRemover implements HitListener {
    private final Game game;
    private final Counter remainingBalls;

    /**
     * Constructor for BallRemover.
     * @param game the game from which balls will be removed
     * @param remainingBalls the counter that keeps track of the number of remaining balls
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    /**
     * {@inheritDoc}
     * Removes the ball from the game and decreases the count of remaining balls when a hit event occurs.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(game);
        remainingBalls.decrease(1);
    }
}
