package tdd;


import java.util.Random;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

public class AutoPlayerTest {
    private final Random rand = new Random();


    @Test
    public void AutoPlayerMoveTest() {
        int size = rand.nextInt(7) + 3;
        Board board = new Board(size);
        AutoPlayer autoPlayer = new AutoPlayer("TestMe", 1, 'X');
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

    @Test
    public void AutoPlayerGettersTest() {
        AutoPlayer autoPlayer = new AutoPlayer("TestMe", 2, 'O');
        Assert.assertEquals("TestMe", autoPlayer.getName());
        Assert.assertEquals(2, autoPlayer.getId());
        Assert.assertEquals('O', autoPlayer.getMarker());
    }

    @Test
    public void AutoPlayerScannerTest() {
        AutoPlayer autoPlayer = new AutoPlayer("TestMe", 3, 'X');
        Scanner sc = autoPlayer.getSC();
        Assert.assertNotNull(sc);
        autoPlayer.closeSC();
        try {
            sc.nextLine();
            Assert.fail("Expected an IllegalStateException to be thrown after closing the Scanner.");
        } catch (IllegalStateException e) {
            // Expected exception, test passes
        }
    }

    @Test
    public void AutoPlayerWinsTest() {
        AutoPlayer autoPlayer = new AutoPlayer("TestMe", 44, 'O');
        Assert.assertEquals(0, autoPlayer.getNumberOfWins());
        for (int i = 0; i < 5; ++i) {
            autoPlayer.incrementNumberOfWins();
        }
        Assert.assertEquals(5, autoPlayer.getNumberOfWins());
        autoPlayer.resetNumberOfWins();
        Assert.assertEquals(0, autoPlayer.getNumberOfWins());
    }
}
