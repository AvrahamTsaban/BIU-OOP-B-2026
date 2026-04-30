import biuoop.DrawSurface;

/**
 * Sprite interface represents a graphical object that can be drawn on the screen and can respond to time passing.
 * It provides methods to draw the sprite and to notify it that time has passed.
 */
public interface Sprite {
    /** Draw the sprite to the screen.
     * @param d the DrawSurface on which to draw the sprite
     */
    void drawOn(DrawSurface d);

    /** Notify the sprite that time has passed. */
    void timePassed();
}