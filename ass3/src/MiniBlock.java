import java.awt.Color;
/**
 * A class representing a mini block in the game, which is a small (and fixed size) version of a regular block.
 * It inherits all functionality from the Block class and implements the Collidable and Sprite interfaces.
 */
public class MiniBlock extends Block {
    /**
     * Create a new mini block with the specified upper left corner and color, using default width and height.
     * @param upperLeft the upper left corner of the mini block
     * @param color the color of the mini block
     */
    public MiniBlock(Point upperLeft, Color color) {
        super(upperLeft, Game.MINIBLOCK_LENGTH, Game.BLOCK_WIDTH, color);
    }
}
