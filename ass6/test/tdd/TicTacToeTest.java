package tdd;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public final class TicTacToeTest {
    private final Random rand = new Random();

    @Test
    public void checkWin() {
        Board board = new Board(3);
        board.board = new char[][] {
            {'X', ' ', ' '},
            {' ', 'X', ' '},
            {' ', ' ', 'X'}
        };
        assertWinCondition('X', board);
        board.board = new char[][] {
            {' ', ' ', 'O'},
            {' ', 'O', ' '},
            {'O', ' ', ' '}
        };
        assertWinCondition('O', board);
        board.board = new char[][] {
            {'X', ' ', ' '},
            {'X', ' ', ' '},
            {'X', ' ', ' '}
        };
        assertWinCondition('X', board);
        // make one test where no one wins
        board.board = new char[][] {
            {'X', 'O', 'X'},
            {'X', 'O', 'O'},
            {'O', 'X', 'X'}
        };
        // assert that no one has won
        Assert.assertFalse("Incorrectly detected a win condition for player: X", board.checkWin('X'));
        Assert.assertFalse("Incorrectly detected a win condition for player: O", board.checkWin('O'));

        board = new Board(rand.nextInt(7) + 3);
        for (int i = 0; i < board.getBoardSize(); ++i) {
            board.board[i][0] = 'O';
        }
        assertWinCondition('O', board);
    }

    private void assertWinCondition(char player, Board board) {
        Assert.assertTrue("Failed to detect a win condition for player: " + player, board.checkWin(player));
        char opponent = player == 'X' ? 'O' : 'X';
        Assert.assertFalse("Incorrectly detected a win condition for the wrong player: " + opponent, board.checkWin(opponent));
    }

    @Test
    public void handleWinnerTest() {
        Player player = new Player("Alice", 1, 'X');
        TicTacToe game = new TicTacToe();

        game.handleWinner(player);
        Assert.assertEquals("Failed to increment the number of wins for the player.", 1, player.getNumberOfWins());
        player.incrementNumberOfWins();
        game.handleWinner(player);
        Assert.assertEquals("Failed to correctly increment the number of wins for the player.", 3, player.getNumberOfWins());
    }

    @Test
    public void isValidPositionTest() {
        int boardSize = rand.nextInt(7) + 3;
        Board board = new Board(boardSize);
        for (int i = 1; i <= boardSize * boardSize; ++i) {
            Assert.assertTrue("Failed to recognize a valid position: " + i, board.isValidPosition(String.valueOf(i)));
        }
        Board secondBoard = new Board(3);
        secondBoard.board = (new char[][] {
            {'X', ' ', ' '},
            {' ', 'O', ' '},
            {' ', ' ', 'X'}
        });
        for (int i = 1; i <= 9; ++i) {
            boolean isOccupied = (i == 1 || i == 5 || i == 9);
            boolean success;
            if (secondBoard.isValidPosition(String.valueOf(i))) {
                success = !isOccupied;
            } else {
                success = isOccupied;
            }
            Assert.assertTrue("Failed to correctly identify position " + i + " as " + (isOccupied ? "occupied" : "unoccupied") + ".", success);
        }
    }

    @Test
    public void isFullTest() {
        int boardSize = rand.nextInt(7) + 3;
        Board board = new Board(boardSize);
        Assert.assertFalse("Failed to recognize that a new board is not full.", board.isFull());

        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board.board[i][j] = rand.nextBoolean() ? 'X' : 'O';
            }
        }
        Assert.assertTrue("Failed to recognize that a fully occupied board is full.", board.isFull());
        int emptyCell = rand.nextInt(boardSize * boardSize);
        board.board[emptyCell / boardSize][emptyCell % boardSize] = ' ';
        Assert.assertFalse("Failed to recognize that a board with " + emptyCell + " empty cell is not full.", board.isFull());
    }

    @Test
    public void verifyBoardSizeTest() {
        TicTacToe game = new TicTacToe();
        for (int i = 3; i <= 10; ++i) {
            Assert.assertTrue("Failed to recognize a valid board size: " + i, game.verifyBoardSize(String.valueOf(i)));
        }
        for (int i = 0; i < 3; ++i) {
            Assert.assertFalse("Incorrectly recognized an invalid board size as valid: " + i, game.verifyBoardSize(String.valueOf(i)));
        }
        for (int i = 11; i < 20; ++i) {
            Assert.assertFalse("Incorrectly recognized an invalid board size as valid: " + i, game.verifyBoardSize(String.valueOf(i)));
        }

        Assert.assertFalse("Incorrectly recognized a non-numeric board size as valid: 'abc'", game.verifyBoardSize("abc"));
        Assert.assertFalse("Incorrectly recognized an empty string as a valid board size.", game.verifyBoardSize(""));
    }
}
