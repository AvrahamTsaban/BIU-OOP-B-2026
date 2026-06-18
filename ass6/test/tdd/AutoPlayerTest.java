package tdd;


import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class AutoPlayerTest {
    private final Random rand = new Random();


    @Test
    public void AutoPlayerMoveTest() {
        int size = rand.nextInt(7) + 3;
        Board board = new Board(size);
        AutoPlayer autoPlayer = new AutoPlayer("Auto", 1, 'X');
        for (int i = 0; i < size * size;) {
            autoPlayer.move(board);
            boolean validMoveMade = countCells(board, 'X', ++i);
            Assert.assertTrue("AutoPlayer made an invalid move or did not make a move at all.", validMoveMade);
        }
    }

    private boolean countCells(Board board, char marker, int expectedCount) {
        int count = 0;
        for (char[] row : board.board) {
            for (char cell : row) {
                if (cell == marker) {
                    ++count;
                }
            }
        }
        return count == expectedCount;
    }
}
