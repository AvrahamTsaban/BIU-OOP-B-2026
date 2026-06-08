package sprites;

import Listeners.Counter;
import biuoop.DrawSurface;
import managers.Game;
import managers.Sprite;
import java.awt.Color;

/****
 * The ScoreIndicator class is responsible for displaying the current score on the screen during the game.
 * It implements the Sprite interface, allowing it to be drawn on the game screen and updated as needed.
 */
public class ScoreIndicator implements Sprite {
    /** The length of the score text to be displayed. */
    private static final int TEXT_LEN = 80;
    /** The size of the score text to be displayed. */
    private static final int TEXT_SIZE = 21;
    /** The x-coordinate where the score text will be displayed. */
    private static final int TEXT_X = (Game.WIDTH - TEXT_LEN) / 2;
    /** The y-coordinate where the score text will be displayed. */
    private static final int TEXT_Y = TEXT_SIZE;

    private final Counter score;

    /**
     * Create a new ScoreIndicator with the given Counter to track the score.
     * @param scoreCounter the Counter to track the score
     */
    public ScoreIndicator(Counter scoreCounter) {
        this.score = scoreCounter;
    }

    /** Draw the score indicator on the given DrawSurface.
     * @param d the DrawSurface on which to draw the score indicator
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.BLACK);
        d.drawText(TEXT_X, TEXT_Y, "Score: " + score.getValue(), TEXT_SIZE);
    }

    /** Notify the score indicator that time has passed.
     * Since the score does not change on its own over time, this method is left empty. */
    @Override
    public void timePassed() {
        // The score does not change on its own over time, so this method can be left empty.
    }

    /** {@inheritDoc} */
    @Override
    public void addToGame(Game g) {
        g.addSprite(this);
    }
}
