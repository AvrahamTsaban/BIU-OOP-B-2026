import biuoop.GUI;
import biuoop.DrawSurface;

import java.util.Arrays;
import java.util.Random;
import java.awt.Color;

/**
 * Draws random lines in a GUI window.
 *
 * @author Avraham Tsaban
 */
public class AbstractArtDrawing {

    private Random rand;
    private GUI gui;
    private DrawSurface d;
    private LineWrapper[] lines;
    private static final int widthPX = 1280;
    private static final int heightPX = 720;
    public final static int NUM_LINES = 10;

    /**
     * Creates the random generator, window, and draw surface.
     */
    public AbstractArtDrawing() {
        this.rand = new Random();
        this.gui = new GUI("Random Lines Example", AbstractArtDrawing.widthPX, AbstractArtDrawing.heightPX);
        this.d = gui.getDrawSurface();
    }

    /**
     * Creates 10 random lines inside the window bounds.
     */
    public void createLines(int numLines) {
        this.lines = new LineWrapper[numLines];
        for (int i = 0; i < numLines; ++i) {
            Line line = null;
            boolean duplicate;
            do {
                line = generateLine(AbstractArtDrawing.widthPX, AbstractArtDrawing.heightPX);
                duplicate = false;
                for (int j = 0; j < i; ++j) {
                    if (line.equals(this.lines[j].getLine())) {
                        duplicate = true;
                        break;
                    }
                }
            } while (line == null || duplicate);
            this.lines[i] = new LineWrapper(line);
        }
        for (LineWrapper wrapper : this.lines) {
            wrapper.calculateGreenLines(Arrays.copyOf(this.lines, this.lines.length));
        }
    }

    /**
     * Generates a random line within the window bounds.
     * Ensures that the line is not a point.
     *
     * @param width the width of the window
     * @param height the height of the window
     * @return a random line
     */
    private Line generateLine(int width, int height) {
        int x1 = rand.nextInt(width) + 1; // get integer in range 1-${width}
        int y1 = rand.nextInt(height) + 1; // get integer in range 1-${height}
        int x2 = rand.nextInt(width) + 1;
        int y2 = rand.nextInt(height) + 1;
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        while (p1.equals(p2)) { // ensure the line is not a point
            x2 = rand.nextInt(width) + 1;
            y2 = rand.nextInt(height) + 1;
            p2 = new Point(x2, y2);
        }
        return new Line(p1, p2);
    }

    /**
     * Draws the lines on the draw surface:
     * For each line, draws the green segments (segments that are part of a triangle) in green,
     * and the remaining segments in black.
     */
    public void drawLines() {
        for (LineWrapper wrapper : this.lines) {
            Line[] greenLines = wrapper.getGreenLines();
            if (greenLines != null) {
                d.setColor(Color.green);
                for (Line ln : greenLines) {
                    Point start = ln.start();
                    Point end = ln.end();
                    int x1 = (int) start.getX();
                    int y1 = (int) start.getY();
                    int x2 = (int) end.getX();
                    int y2 = (int) end.getY();
                    d.drawLine(x1, y1, x2, y2);
                }
            }

            Line[] blackLines = wrapper.getBlackLines();
            if (blackLines != null) {
                d.setColor(Color.black);
                for (Line ln : blackLines) {
                    Point start = ln.start();
                    Point end = ln.end();
                    int x1 = (int) start.getX();
                    int y1 = (int) start.getY();
                    int x2 = (int) end.getX();
                    int y2 = (int) end.getY();
                    d.drawLine(x1, y1, x2, y2);
                }
            }

            d.setColor(Color.blue);
            Line line = wrapper.getLine();
            int x = (int) line.middle().getX();
            int y = (int) line.middle().getY();
            d.fillCircle(x, y, 3);

            d.setColor(Color.red);
            Point[] intersections = wrapper.getAllIntersections(Arrays.copyOf(this.lines, this.lines.length));
            for (Point pt : intersections) {
                if (pt != null) {
                    int ptX = (int) pt.getX();
                    int ptY = (int) pt.getY();
                    d.fillCircle(ptX, ptY, 3);
                }
            }
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
        canvas.createLines(NUM_LINES);
        canvas.drawLines();
        canvas.showLines();
    }
}