import java.util.Random;

/**
 * A test class to fuzz test the collision detection of the Rectangle class.
 * It generates random balls and moves them around,
 * checking if they correctly detect collisions with the gray and yellow squares.
 * It counts the number of failures for both inside and outside cases, and prints out details of each failure.
 */
public class TestSquareFuzz {
    /**
     * The main method to run the fuzz test.
     * @param args
     */
    public static void main(String[] args) {
        Rectangle gray = new Rectangle(new Point(50, 50), 450, 450, java.awt.Color.GRAY);
        Rectangle yellow = new Rectangle(new Point(450, 450), 150, 150, java.awt.Color.YELLOW);
        Rectangle screen = new Rectangle(new Point(0, 0), 800, 600, java.awt.Color.WHITE);
        Random rand = new Random(0);

        int outsideFailures = 0;
        int insideFailures = 0;

        for (int t = 0; t < 300; t++) {
            int r = 5 + rand.nextInt(26);
            Ball b;
            do {
                b = Ball.generateMovingBallBySize(r, screen, rand);
            } while (!gray.isOutside(b) || !yellow.isOutside(b));

            for (int i = 0; i < 1500; i++) {
                b.moveOneStep(new Rectangle[0], new Rectangle[]{gray, yellow});
                if (!gray.isOutside(b) || !yellow.isOutside(b)) {
                    outsideFailures++;
                    System.out.println("OUTSIDE_FAIL t=" + t + " step=" + i
                        + " x=" + b.getX() + " y=" + b.getY() + " r=" + b.getSize());
                    break;
                }
            }
        }

        for (int t = 0; t < 300; t++) {
            int r = 5 + rand.nextInt(26);
            Ball b;
            do {
                b = Ball.generateMovingBallBySize(r, gray, rand);
            } while (!yellow.isOutside(b));

            for (int i = 0; i < 1500; i++) {
                b.moveOneStep(gray, yellow);
                if (!gray.isInside(b) || !yellow.isOutside(b)) {
                    insideFailures++;
                    System.out.println("INSIDE_FAIL t=" + t + " step=" + i
                        + " x=" + b.getX() + " y=" + b.getY() + " r=" + b.getSize()
                        + " inGray=" + gray.isInside(b) + " outYellow=" + yellow.isOutside(b));
                    break;
                }
            }
        }

        System.out.println("outsideFailures=" + outsideFailures);
        System.out.println("insideFailures=" + insideFailures);
    }
}
