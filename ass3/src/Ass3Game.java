/**
 * The ass3Game class is the entry point for the game. It contains the main method that initializes and runs the game.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.3
 * @since 2024-06-05
 */
public final class Ass3Game {

    /**
     * Private constructor to prevent instantiation of the ass3Game class.
     */
    private Ass3Game() { }

    /**
     * The main entry point to run the animation.
     * @param args command line arguments (neglected)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
