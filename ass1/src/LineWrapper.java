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
                if (intersection1 == null || intersection2 == null) {
                    continue;
                }

                Line greenLn = new Line(intersection1, intersection2);
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
            }
        }
    }

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
>>>>>>> fee5559 (happy passover)
     * @return a copy of the line
     */
    public Line getLine() {
        return new Line(this.line.start(), this.line.end());
    }

    /**
<<<<<<< HEAD
     * Returns the wrapped line start point.
=======
     * Return the start point of the wrapped line.
>>>>>>> fee5559 (happy passover)
     *
     * @return the start point
     */
    public Point start() {
        return this.line.start();
    }

    /**
<<<<<<< HEAD
     * Returns the wrapped line end point.
=======
     * Return the end point of the wrapped line.
>>>>>>> fee5559 (happy passover)
     *
     * @return the end point
     */
    public Point end() {
        return this.line.end();
    }

    /**
<<<<<<< HEAD
     * Returns the wrapped line middle point.
=======
     * Return the middle point of the wrapped line.
>>>>>>> fee5559 (happy passover)
     *
     * @return the middle point
     */
    public Point middle() {
        return this.line.middle();
    }
}
