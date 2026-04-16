import java.awt.Color;
import biuoop.DrawSurface;

/****
 * A class representing a square shape that can be drawn on a DrawSurface.
 * It has an upper left corner, an edge length, and a color.
 * It also provides methods to check if a ball is inside or outside the square.
 */
public class Rectangle {
    private final Point upperLeft;
    private final double horizontalEdge;
    private final double verticalEdge;
    private final Color color;

    /**
     * Initialize a new square with the given upper left corner, edge length, and color.
     * @param upperLeft the upper left corner of the square
     * @param horizontalEdge the length of the horizontal side of the square
     * @param verticalEdge the length of the vertical side of the square
     * @param color the color of the square
     */
    public Rectangle(Point upperLeft, double horizontalEdge, double verticalEdge, Color color) {
        this.upperLeft = upperLeft;
        this.horizontalEdge = horizontalEdge;
        this.verticalEdge = verticalEdge;
        this.color = color;
    }

    /**
     * Get the x-coordinate of the right edge of the square.
     * @return the x-coordinate of the right edge
     */
    public double rightX() {
        return upperLeft.getX() + horizontalEdge;
    }

    /**
     * Get the y-coordinate of the bottom edge of the square.
     * @return the y-coordinate of the bottom edge
     */
    public double bottomY() {
        return upperLeft.getY() + verticalEdge;
    }

    /**
     * Get the x-coordinate of the left edge of the square.
     * @return the x-coordinate of the left edge
     */
    public double leftX() {
        return upperLeft.getX();
    }

    /**
     * Get the y-coordinate of the top edge of the square.
     * @return the y-coordinate of the top edge
     */
    public double topY() {
        return upperLeft.getY();
    }

    /**
     * Get the width of the square.
     * @return the width of the square
     */
    public double width() {
        return horizontalEdge;
    }

    /**
     * Get the height of the square.
     * @return the height of the square
     */
    public double height() {
        return verticalEdge;
    }

    /**
     * Get a Line representing the left edge of the square.
     * @return the line representing the left edge
     */
    public Line LeftLine() {
        return new Line(new Point(leftX(), topY()), new Point(leftX(), bottomY()));
    }

    /**
     * Get a Line representing the right edge of the square.
     * @return the line representing the right edge
     */
    public Line RightLine() {
        return new Line(new Point(rightX(), topY()), new Point(rightX(), bottomY()));
    }

    /**
     * Get a Line representing the top edge of the square.
     * @return the line representing the top edge
     */
    public Line TopLine() {
        return new Line(new Point(leftX(), topY()), new Point(rightX(), topY()));
    }

    /**
     * Get a Line representing the bottom edge of the square.
     * @return the line representing the bottom edge
     */
    public Line BottomLine() {
        return new Line(new Point(leftX(), bottomY()), new Point(rightX(), bottomY()));
    }

    /**
     * Draw the square on the given surface.
     * @param surface the surface to draw the square on
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillRectangle((int) upperLeft.getX(), (int) upperLeft.getY(), (int) horizontalEdge, (int) verticalEdge);
    }

    /**
      * Check if the given point is inside the square. Otherwise, returns the corresponding type of collision.
      * @param p the point to check
      * @return an array of CollisionCase representing the corresponding collisions
      */
    private CollisionCase deviationFromInside(Point p) {
        boolean hasTop = false;
        boolean hasBottom = false;
        boolean hasLeft = false;
        boolean hasRight = false;
        if (p.getX() < leftX()) {
            hasLeft = true;
        }
        if (p.getX() > rightX()) {
            hasRight = true;
        }
        if (p.getY() < topY()) {
            hasTop = true;
        }
        if (p.getY() > bottomY()) {
            hasBottom = true;
        }

        return new CollisionCase(hasTop, hasBottom, hasLeft, hasRight, false);
    }

    /**
     * Check if the given ball is colliding with the square from the inside, and return the type of collision.
     * @param b the ball to check
     * @return the type of collision that is occurring between the ball and the square, or NONE if there is no collision
     */
    public Collision collisionFromInside(Ball b) {
        Point topLeft = new Point(b.getX() - b.getSize(), b.getY() - b.getSize());
        Point bottomRight = new Point(b.getX() + b.getSize(), b.getY() + b.getSize());
        CollisionCase collisionsTL = deviationFromInside(topLeft);
        CollisionCase collisionsBR = deviationFromInside(bottomRight);
        CollisionCase collisions = new CollisionCase(collisionsTL, collisionsBR);
        return new Collision(this, collisions);
    }

    /**
     * Check if the given point is inside the square.
     * @param p the point to check
     * @return true if the point is inside the square, false otherwise
     */
    private boolean isInside(Point p) {
        return isInXRange(p) && isInYRange(p);
    }

    /**
     * Check if the given point is outside the square.
     * @param p the point to check
     * @return true if the point is outside the square, false otherwise
     */
    private boolean isInXRange(Point p) {
        return p.getX() > leftX() && p.getX() < rightX();
    }

    /**
     * Check if the given point is outside the square.
     * @param p the point to check
     * @return true if the point is outside the square, false otherwise
     */
    private boolean isInYRange(Point p) {
        return p.getY() > topY() && p.getY() < bottomY();
    }

    /**
     * Check if the given ball is inside the square.
     * @param b the ball to check
     * @return true if the ball is inside the square, false otherwise
     */
    public boolean isInside(Ball b) {
        Point topLeft = new Point(b.getX() - b.getSize(), b.getY() - b.getSize());
        Point bottomRight = new Point(b.getX() + b.getSize(), b.getY() + b.getSize());
        return isInside(topLeft) && isInside(bottomRight);
    }

    /**
     * Check if the given ball is outside the square.
     * @param b the ball to check (should be the predicted position of the ball)
     * @return true if the ball is outside the square, false otherwise
     */
    public boolean isOutside(Ball b) {
        Point topLeft = new Point(b.getX() - b.getSize(), b.getY() - b.getSize());
        Point bottomRight = new Point(b.getX() + b.getSize(), b.getY() + b.getSize());
        if (isInside(topLeft) || isInside(bottomRight)) {
            return false;
        }
        else return !cornerTouch(b);
    }

    /**
     * Check if the given ball is touching the square at a corner.
     * @param b the ball to check
     * @return true if the ball is touching the square at a corner, false otherwise
     */
    private boolean cornerTouch(Ball b) {
        Point c = b.getCenter();
        double r = b.getSize();
        if (c.distance(upperLeft) < r) {
            return true;
        } else if (c.distance(new Point(rightX(), topY())) < r) {
            return true;
        } else if (c.distance(new Point(leftX(), bottomY())) < r) {
            return true;
        } else if (c.distance(new Point(rightX(), bottomY())) < r) {
            return true;
        }
        else return false;
    }

    /**
     * Check for collisions from the outside.
     * @param b the ball to check
     * @return the type of collision that is occurring between the ball and the square,
     * or empty Collision if there is no collision
     */
    public Collision collisionFromOutside(Ball b) {
        Velocity v = b.getVelocity();
        Point p = b.getCenter();
        double r = b.getSize();
        Point collisionPtR = null;
        Point collisionPtL = null;
        Point collisionPtT = null;
        Point collisionPtB = null;
        if (v.getDx() < 0) {
            Point leftmost = new Point(p.getX() - r, p.getY());
            Line vector = new Line(leftmost, v);
            if (vector.length() <= v.getSpeed()) {
                collisionPtR = this.RightLine().intersectionWith(vector);
            }
        } else if (v.getDx() > 0) {
            Point rightmost = new Point(p.getX() + r, p.getY());
            Line vector = new Line(rightmost, v);
            if (vector.length() <= v.getSpeed()) {
                collisionPtL = this.LeftLine().intersectionWith(vector);
            }
        }
        if (v.getDy() < 0) {
            Point uppermost = new Point(p.getX(), p.getY() - r);
            Line vector = new Line(uppermost, v);
            if (vector.length() <= v.getSpeed()) {
                collisionPtB = this.BottomLine().intersectionWith(vector);
            }
        } else if (v.getDy() > 0) {
            Point lowermost = new Point(p.getX(), p.getY() + r);
            Line vector = new Line(lowermost, v);
            if (vector.length() <= v.getSpeed()) {
                collisionPtT = this.TopLine().intersectionWith(vector);
            }
        }

        Point collision = nearbyCollision(p, collisionPtT, collisionPtB);
        collision = nearbyCollision(p, collision, collisionPtL);
        collision = nearbyCollision(p, collision, collisionPtR);
        if (collision == null || collision.distance(p) > r) {
            return Collision.none();
        }
        
        if (collision.equals(collisionPtR)) {
            return new Collision(this, false, false, true, false, true);
        } else if (collision.equals(collisionPtL)) {
            return new Collision(this, false, false, false, true, true);
        } else if (collision.equals(collisionPtT)) {
            return new Collision(this, true, false, false, false, true);
        } else { // collision.equals(collisionPtB)
            return new Collision(this, false, true, false, false, true);
        }
    }

    private Point nearbyCollision(Point root, Point first, Point second) {
        if (first == null){
            return second;
        } else if (second == null) {
            return first;
        } else {
            return root.distance(first) < root.distance(second) ? first : second;
        }
    }
}