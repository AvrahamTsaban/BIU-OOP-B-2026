<<<<<<< HEAD
import java.util.Arrays;

/**
 * Wraps a line and keeps extra segments used for coloring.
 *
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-06-05
 */
public class LineWrapper {
    private final Line line;
    private Line[] greenLines;

    /**
     * Creates a wrapper for a line.
     *
     * @param line line to wrap
     */
    public LineWrapper(Line line) {
        this.line = new Line(line.start(), line.end());
        this.greenLines = null;
    }

    /**
     * Finds intersection-based segments on this line.
     * Updates the greenLines field with the segments that are between two intersections with other lines.
     * Uses private methods for defragging, removing nulls and sorting to keep the data usable.
     *
     * <p>Should be called for each line wrapper after all lines are created,
     * and before any of the segments are accessed.
     * Trying to access the segments before calling this method will return null.</p>
     *
     * <p>Best practice: call this method with a copy of LineWrapper array, to avoid accidental modification.</p>
     *
     * <p>Note: recalling this method with null LineWrapper array keeps previous greenLines calculation.</p>
     *
     * @param allLines all wrapped lines in the drawing
     */
    public void calculateGreenLines(LineWrapper[] allLines) {
        if (allLines == null) {
            return;
        }
        this.greenLines = new Line[0];

        for (int i = 0; i < allLines.length; ++i) {
            Line other1 = allLines[i].getLine();
            if (allLines[i] == this || !this.line.isIntersecting(other1)) {
                continue;
            }
            for (int j = i + 1; j < allLines.length; ++j) {
                if (allLines[j] == this || allLines[j] == allLines[i]) {
=======

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
>>>>>>> fee5559 (happy passover)
                    continue;
                }
                Line other2 = allLines[j].getLine();

                if (!other1.isIntersecting(other2, this.line)) {
                    continue;
                }

                Point intersection1 = this.line.intersectionWith(other1);
                Point intersection2 = this.line.intersectionWith(other2);
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 95e5362 (removed colouredLine class)
                if (intersection1 == null || intersection2 == null) {
                    continue;
                }

                Line greenLn = new Line(intersection1, intersection2);
<<<<<<< HEAD
                this.greenLines = Arrays.copyOf(this.greenLines, this.greenLines.length + 1);
                this.greenLines[this.greenLines.length - 1] = greenLn;
            }
        }
        this.defragGreenLines();
        this.greenLines = removeNulls(greenLines);
        Arrays.sort(greenLines);
    }

    /**
     * Defrags the green lines array by merging intersecting segments.
     */
    private void defragGreenLines() {
        for (int i = 0; i < greenLines.length; ++i) {
            if (greenLines[i] == null) {
                continue;
            }
            for (int j = i + 1; j < greenLines.length; ++j) {
                if (greenLines[j] == null) {
                    continue;
                }
                if (greenLines[i].isIntersecting(greenLines[j])) {
                    Point newStart = greenLines[i].start();
                    if (newStart.distance(this.start()) > greenLines[j].start().distance(this.start())) {
                        newStart = greenLines[j].start();
                    }
                    Point newEnd = greenLines[i].end();
                    if (newEnd.distance(this.start()) < greenLines[j].end().distance(this.start())) {
                        newEnd = greenLines[j].end();
                    }
                    greenLines[j] = new Line(newStart, newEnd);
                    greenLines[i] = null;
                    break;
                }
=======
                trianglePoints = Arrays.copyOf(trianglePoints, trianglePoints.length + 1);
                trianglePoints[trianglePoints.length - 1] = new Point[]{intersection1, intersection2};
>>>>>>> fee5559 (happy passover)
=======
                this.triangleLines = Arrays.copyOf(this.triangleLines, this.triangleLines.length + 1);
                this.triangleLines[this.triangleLines.length - 1] = greenLn;
>>>>>>> 95e5362 (removed colouredLine class)
            }
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * Gets an array of Line[] type and returns a copy of it without nulls.
     *
     * @param arr Line[] array to remove nulls from
     * @return new array without nulls
     */
    private Line[] removeNulls(Line[] arr) {
        int newIndex = 0;
        Line[] newArr = new Line[0];
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] == null) {
                continue;
            }
            newArr = Arrays.copyOf(newArr, newArr.length + 1);
            newArr[newIndex] = arr[i];
            ++newIndex;
        }
        return newArr;
    }


    /**
     * Returns a copy of the green lines.
     *
     * @return copied green lines (null if green lines were not calculated yet)
     */
    public Line[] getGreenLines() {
        if (this.greenLines == null) {
            return null;
        }

        return Arrays.copyOf(this.greenLines, this.greenLines.length);
    }

    /**
    * Returns the black lines of this wrapper, which are the segments before, between and after the green lines.
    *
    * @return black-line segments (null if green lines were not calculated yet)
    */
    public Line[] getBlackLines() {
        if (this.greenLines == null) {
            return null;
        }

        Line[] blackLines = new Line[this.greenLines.length + 1];
        int lineIndex = 0;
        Point currentStart = this.start();
        for (Line greenLn : this.greenLines) {
            Line currentLine = new Line(currentStart, greenLn.start());
            if (currentLine.length() > 0) {
                blackLines[lineIndex] = currentLine;
                ++lineIndex;
            }
            currentStart = greenLn.end();
        }
        Line currentLine = new Line(currentStart, this.end());
        if (currentLine.length() > 0) {
            blackLines[lineIndex] = currentLine;
            ++lineIndex;
        }
        blackLines = Arrays.copyOf(blackLines, lineIndex);
        return blackLines;
    }

    /**
     * Gets the intersection points of this line with lines in the provided array.
     *
     * <p>Should be called for each line wrapper after all lines are created.</p>
     *
     * <p>Best practice: only call this method with a copy of LineWrapper array.</p>
     *
     * @param allLines all wrapped lines in the drawing
     * @return array of intersection points
     */
    public Point[] getAllIntersections(LineWrapper[] allLines) {
        Point[] intersections = new Point[allLines.length * 2];
        int index = 0;
        for (LineWrapper wrapper : allLines) {
            Point pt = this.line.intersectionWith(wrapper.getLine());
            if (pt != null) {
                intersections[index] = pt;
                ++index;
            }
        }
        return Arrays.copyOf(intersections, index);
    }

    /**
     * Returns a copy of the wrapped line.
     *
=======
    //TODO: anything. recycle trianglepoints as line array
=======
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
>>>>>>> 95e5362 (removed colouredLine class)

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
<<<<<<< HEAD
     * Return a copy of the line wrapped by this class.
>>>>>>> fee5559 (happy passover)
=======
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
>>>>>>> 95e5362 (removed colouredLine class)
     * @return a copy of the line
     */
    public Line getLine() {
        return new Line(this.line.start(), this.line.end());
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Returns the wrapped line start point.
=======
     * Return the start point of the wrapped line.
>>>>>>> fee5559 (happy passover)
=======
     * Returns the wrapped line start point.
>>>>>>> 95e5362 (removed colouredLine class)
     *
     * @return the start point
     */
    public Point start() {
        return this.line.start();
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Returns the wrapped line end point.
=======
     * Return the end point of the wrapped line.
>>>>>>> fee5559 (happy passover)
=======
     * Returns the wrapped line end point.
>>>>>>> 95e5362 (removed colouredLine class)
     *
     * @return the end point
     */
    public Point end() {
        return this.line.end();
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Returns the wrapped line middle point.
=======
     * Return the middle point of the wrapped line.
>>>>>>> fee5559 (happy passover)
=======
     * Returns the wrapped line middle point.
>>>>>>> 95e5362 (removed colouredLine class)
     *
     * @return the middle point
     */
    public Point middle() {
        return this.line.middle();
    }
}
