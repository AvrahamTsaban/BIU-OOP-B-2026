package Listeners;
import sprites.Block;
import sprites.Ball;

/**
 * The ScoreTrackingListener class is responsible for tracking the score when blocks are hit by balls.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * Create a new ScoreTrackingListener with the given Counter to track the score.
     * @param scoreCounter the Counter to track the score
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * {@inheritDoc}
     * Increases the score when a block is hit by a ball.
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        currentScore.increase(5);
    }

}
