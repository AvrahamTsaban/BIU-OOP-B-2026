package Listeners;

import sprites.Block;
import managers.Game;
import sprites.Ball;

/**
 * The BlockRemover class is responsible for removing blocks from the game when they are hit by a ball.
 * It keeps track of the number of remaining blocks using a Counter object.
 */
public class BlockRemover implements HitListener {
    private final Game game;
    private final Counter remainingBlocks;

    /**
     * Constructor for BlockRemover.
     * @param game the game from which blocks will be removed
     * @param remainingBlocks the counter that keeps track of the number of remaining blocks
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    /**
     * {@inheritDoc}
     * Removes the block from the game and decreases the count of remaining blocks when a hit event occurs.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeHitListener(this);
        beingHit.removeFromGame(game);
        remainingBlocks.decrease(1);
    }
}