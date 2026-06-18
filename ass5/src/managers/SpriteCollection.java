package managers;

import java.util.ArrayList;
import biuoop.DrawSurface;

/**
 * SpriteCollection class represents a collection of sprites in the game.
 * It provides methods to add sprites to the collection,
 * notify all sprites that time has passed, and draw all sprites on a given DrawSurface.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public class SpriteCollection {
    private final ArrayList<Sprite> sprites;

    /**
     * Initialize a new SpriteCollection with an empty list of sprites.
     */
    public SpriteCollection() {
        sprites = new ArrayList<Sprite>();
    }

    /**
     * Add the given sprite to the collection.
     * @param s the sprite to add to the collection
     */
    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    /**
     * Remove the given sprite from the collection.
     * @param s the sprite to remove from the collection
     */
    public void removeSprite(Sprite s) {
        sprites.remove(s);
    }

    /**
     * Notify all sprites in the collection that time has passed.
     */
    public void notifyAllTimePassed() {
        ArrayList<Sprite> copy = new ArrayList<Sprite>(this.sprites);
        for (Sprite s : copy) {
            s.timePassed();
        }
    }

    /**
     * Draw all sprites in the collection on the given DrawSurface.
     * @param d the surface on which to draw the sprites
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite s : this.sprites) {
            s.drawOn(d);
        }
    }
}