
import java.util.Arrays;

/**
 * Wraps a line and keeps extra segments used for coloring.
 *
 * @author Avraham Tsaban
 */
public class LineWrapper {
    private final Line line;
    private Line[] triangleLines;
    private final int index;
    private final ColouredLine colouredLine;

    /**
     * Creates a wrapper for a line.
     *
     * @param line line to wrap
     * @param index index of this line in the original array
     */
    public LineWrapper(Line line, int index) {
        this.line = new Line(line.start(), line.end());
        this.index = index;
        this.triangleLines = new Line[0];
        colouredLine = new ColouredLine(this.line.start());
    }

    /**
     * Finds intersection-based segments on this line.
     *
     * @param allLines all wrapped lines in the drawing
     */
    public void intersections(LineWrapper[] allLines) {
        for (int i = 0; i < allLines.length; ++i) {
            Line other1 = allLines[i].getLine();
            if (i == this.index || !this.line.isIntersecting(other1)) {
                continue;
            }
            for (int j = i + 1; j < allLines.length; ++j) {
                if (i == j || j == this.index) {
                    continue;
                }
                Line other2 = allLines[j].getLine();

                if (!other1.isIntersecting(other2, this.line)) {
                    continue;
                }

                Point intersection1 = this.line.intersectionWith(other1);
                Point intersection2 = this.line.intersectionWith(other2);
                if (intersection1 == null || intersection2 == null) {
                    continue;
                }

                Line greenLn = new Line(intersection1, intersection2);
                this.triangleLines = Arrays.copyOf(this.triangleLines, this.triangleLines.length + 1);
                this.triangleLines[this.triangleLines.length - 1] = greenLn;
            }
        }
    }

    private void defragGreenLines() {
        for (int i = 0; i < triangleLines.length; ++i) {
            if (triangleLines[i] == null) {
                continue;
            }
            for (int j = i + 1; j < triangleLines.length; ++j) {
                if (triangleLines[j] == null) {
                    continue;
                }
                if (triangleLines[i].isIntersecting(triangleLines[j])) {
                    Point newStart = triangleLines[i].start();
                    if (triangleLines[i].start().distance(this.start) > triangleLines[j].start().distance(this.start)) {
                        newStart = triangleLines[j].start();
                    }
                    Point newEnd = triangleLines[i].end();
                    if (triangleLines[i].end().distance(this.start) < triangleLines[j].end().distance(this.start)) {
                        newEnd = triangleLines[j].end();
                    }
                    triangleLines[j] = new Line(newStart, newEnd);
                    triangleLines[i] = null;
                }
            }
        }
        removeNulls();
        Arrays.sort(triangleLines);
    }

    private void removeNulls() {
        int newIndex = 0;
        Line[] newArr = new Line[0];
        for (int i = 0; i < triangleLines.length; ++i) {
            if (triangleLines[i] != null) {
                newArr = Arrays.copyOf(newArr, newArr.length + 1);
                newArr[newIndex] = triangleLines[i];
                ++newIndex;
            }
        }
        this.triangleLines = newArr;
    }



    /**
     * Splits the wrapped line into colored parts.
     * Triangle segments are saved with color 0, and uncovered parts with color 1.
     */
    public void mapToColor() {
        Line next = findNextLn(line.start().getX(), line.end().getX());
        if (next == null) {
            colouredLine.addLine(line.start(), line.end(), 1);
            return;
        }
        colouredLine.addLine(line.start(), next.start(), 1);
        Point after = next.start();
        while (next != null) {
            colouredLine.addLine(next.start(), next.end(), 0);
            after = next.end();
            next = findNextLn(after.getX(), line.end().getX());
        }
        if (!after.equals(line.end())) {
            colouredLine.addLine(after, line.end(), 1);
        }

    }

    /**
     * Finds the next triangle segment after a given x value.
     *
     * @param after lower x bound (exclusive)
     * @param max upper x bound used as current best candidate
     * @return segment with the smallest start x above after, or null if none was found
     */
    private Line findNextLn(double after, double max) {
        double temp = max;
        Line ln = null;
        for (Line current : triangleLines) {
            if (current.start().getX() > after && current.start().getX() < temp) {
                temp = current.start().getX();
                ln = current;
            }
        }
        return ln;
    }

    /**
     * Returns a copy of the colored lines.
     *
     * @return copied colored-line representation
     */
    public ColouredLine getColouredLine() {
        return this.colouredLine.getColouredLine();
    }

    /**
     * Returns a copy of the wrapped line.
     *
     * @return a copy of the line
     */
    public Line getLine() {
        return new Line(this.line.start(), this.line.end());
    }

    /**
     * Returns the wrapped line start point.
     *
     * @return the start point
     */
    public Point start() {
        return this.line.start();
    }

    /**
     * Returns the wrapped line end point.
     *
     * @return the end point
     */
    public Point end() {
        return this.line.end();
    }

    /**
     * Returns the wrapped line middle point.
     *
     * @return the middle point
     */
    public Point middle() {
        return this.line.middle();
    }
}
