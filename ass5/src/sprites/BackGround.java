package sprites;

import biuoop.DrawSurface;
import geometry.Point;
import managers.Game;
import managers.Sprite;
import java.awt.Color;

/**
 * The BackGround class represents the background of the game area.
 * It implements the Sprite interface, allowing it to be drawn on the game screen and updated as needed.
 */
public class BackGround implements Sprite {
    /** A point representing the upper left corner of the background of the game area. */
    private static final Point UPPER_LEFT = new Point(0, 0);
    /** The color of the background of the game area. */
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    /** Draw the background on the given DrawSurface.
     * @param d the DrawSurface on which to draw the background
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(BACKGROUND_COLOR);
        d.fillRectangle((int) UPPER_LEFT.getX(), (int) UPPER_LEFT.getY(), (int) Game.WIDTH, (int) Game.HEIGHT);
    }

    /** Notify the background that time has passed.
     * Since the background does not change on its own over time, this method is left empty. */
    @Override
    public void timePassed() {
        // The background does not change on its own over time, so this method can be left empty.
    }

    /** {@inheritDoc} */
    @Override
    public void addToGame(Game g) {
        g.addSprite(this);
    }
}
