import java.util.Arrays;

/**
 * Represents a collection of line segments, where each line has an associated color.
 *
 * @author Avraham Tsaban
 */
public class colouredLine {
    private Line line[];
    private int colours[];

    /**
     * Creates a new colored-line collection with a single line segment.
     *
     * @param start start point of the line
     * @param end end point of the line
     * @param colour color of the line
     */
    public colouredLine() {
        this.line = new Line[0];
        this.colours = new int[0];
    }

    /**
     * Adds a new line segment and its color to the collection.
     *
     * @param start start point of the new line
     * @param end end point of the new line
     * @param colour color of the new line
     */
    public void addLine(Point start, Point end, int colour) {
        this.line = Arrays.copyOf(this.line, this.line.length + 1);
        this.colours = Arrays.copyOf(this.colours, this.colours.length + 1);
        this.line[this.colours.length - 1] = new Line(start, end);
        this.colours[this.colours.length - 1] = colour;
    }

    /**
     * Returns the line at the given index.
     *
     * @param index position of the requested line
     * @return the line stored at {@code index}
     */
    public Line getLine(int index) {
        return new Line(this.line[index].start(), this.line[index].end());
    }

    /**
     * Returns the color at the given index.
     *
     * @param index position of the requested color
     * @return the color stored at {@code index}
     */
    public int getColour(int index) {
        return this.colours[index];
    }
}
