
import java.util.Arrays;

/** A wrapper for a line in the abstract art drawing.
 * @author Avraham Tsaban
 */
public class LineWrapper {
    private final Line line;
    private Line[] triangleLines;
    private final int index;
    private colouredLine colouredLine;

    /**
     * Constructor for LineWrapper class.
     *
     * @param line the line to wrap
     * @param index the index of the line in the drawing
     */
    public LineWrapper(Line line, int index) {
        this.line = new Line(line.start(), line.end());
        this.index = index;
        this.triangleLines = new Line[];
        colouredLine = new colouredLine();
    }

    /**
     * Calculate the intersection points of this line with all the other lines in the drawing,
     * and add the corresponding triangles to this line.
     *
     * @param allLines all line wrappers in the drawing
     */
    public void intersections(LineWrapper[] allLines) {
        for (int i = 0; i < allLines.length; ++i) {
            Line other1 = allLines[i].getLine();
            if (i == this.index || !this.line.isIntersecting(other1)) {
                continue;
            }
            for (int j = 0; j < allLines.length; ++j) {
                if (i == j || j == this.index) {
                    continue;
                }
                Line other2 = allLines[j].getLine();

                if (!other1.isIntersecting(other2, this.line)) {
                    continue;
                }

                Point intersection1 = this.line.intersectionWith(other1);
                Point intersection2 = this.line.intersectionWith(other2);
                trianglePoints = Arrays.copyOf(trianglePoints, trianglePoints.length + 1);
                trianglePoints[trianglePoints.length - 1] = new Point[]{intersection1, intersection2};
            }
        }
    }

    //TODO: anything. recycle trianglepoints as line array

    public void mapToColor() {
        int index = 1;
        Point[] next = findNextLn(line.start().getX());
        colouredLine.addLine(start, end, index);
        while (true) {
            ++index;
        }
    }

    private Point[] findNextLn(double x) {
        Point[] temp = trianglePoints[0];
        for (Point[] current : trianglePoints) {
            if (current[0].getX() > x && current[0].getX() < temp[0].getX()) {
                temp = current;
            }
        }
        return temp;
    }

    /**
     * Return a copy of the line wrapped by this class.
     * @return a copy of the line
     */
    public Line getLine() {
        return new Line(this.line.start(), this.line.end());
    }

    /**
     * Return the start point of the wrapped line.
     *
     * @return the start point
     */
    public Point start() {
        return this.line.start();
    }

    /**
     * Return the end point of the wrapped line.
     *
     * @return the end point
     */
    public Point end() {
        return this.line.end();
    }

    /**
     * Return the middle point of the wrapped line.
     *
     * @return the middle point
     */
    public Point middle() {
        return this.line.middle();
    }
}
