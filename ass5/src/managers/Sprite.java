package managers;

import biuoop.DrawSurface;

/**
 * Sprite interface represents a graphical object that can be drawn on the screen and can respond to time passing.
 * It provides methods to draw the sprite and to notify it that time has passed.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public interface Sprite {
    /**
     * Draw the sprite to the screen.
     * @param d the DrawSurface on which to draw the sprite
     */
    void drawOn(DrawSurface d);

    /** Notify the sprite that time has passed. */
    void timePassed();

    /** Add this sprite to the given game.
     * Sprites are responsible for determining wether they shluold add themselves to the game as
     * a Collidable, or only as a Sprite.
     * @param g the game to which to add this sprite
     */
    void addToGame(Game g);
}