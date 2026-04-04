import biuoop.GUI;
import biuoop.DrawSurface;

import java.util.Random;
import java.awt.Color;

/**
 * Draws random lines in a GUI window.
 *
 * @author Avraham Tsaban
 */
public class AbstractArtDrawing {

        private Random rand; // create a random-number generator
        // Create a window with the title "Random Lines Example"
        // which is 800 pixels wide and 600 pixels high.
        private GUI gui;
        private DrawSurface d;
        private LineWrapper[] lines;

        /**
         * Creates the random generator, window, and draw surface.
         */
        public AbstractArtDrawing() {
            this.rand = new Random();
            this.gui = new GUI("Random Lines Example", 800, 600);
            this.d = gui.getDrawSurface();
        }

        /**
         * Creates 10 random lines inside the window bounds.
         */
        public void createLines() {
            this.lines = new LineWrapper[10];
            for (int i = 0; i < 10; ++i) {
                int x1 = rand.nextInt(800) + 1; // get integer in range 1-800
                int y1 = rand.nextInt(600) + 1; // get integer in range 1-600
                int x2 = rand.nextInt(800) + 1;
                int y2 = rand.nextInt(600) + 1;
                while (x1 == x2 && y1 == y2) { // esure the line is not a point
                    x2 = rand.nextInt(800) + 1;
                    y2 = rand.nextInt(600) + 1;
                }
                this.lines[i] = new LineWrapper(new Line(x1, y1, x2, y2), i);
            }
        }

        /**
         * Shows the current drawing on the window.
         */
        public void showLines() {
            this.gui.show(d);
        }

    /**
     * Runs the random-line drawing demo.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        AbstractArtDrawing canvas = new AbstractArtDrawing();
        canvas.createLines();
        canvas.showLines();
    }
}