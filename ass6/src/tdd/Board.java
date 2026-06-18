package tdd;

public class Board {
    int boardSize;
    char[][] board;

    private Board() {}

    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.setBoard();
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    private void setBoard() {
        this.board = new char[this.boardSize][this.boardSize];

        for (int i = 0; i < this.boardSize; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                board[i][j] = ' ';
            }
        }
    }

    public void print() {
        StringBuilder topBottomBoundary = new StringBuilder();

        topBottomBoundary.append("+---".repeat(Math.max(0, this.boardSize)));
        topBottomBoundary.append("+");

        for (char[] row : this.board) {
            System.out.println(topBottomBoundary);

            for (char cell : row) {
                System.out.print("| " + cell + " ");
            }
            System.out.println("|");
        }
        System.out.println(topBottomBoundary);
        System.out.println();
    }

    public void placeTheMove(char checkMark, int movePosition) {
        int i = (movePosition - 1) / this.board.length;
        int j = (movePosition - 1) % this.board.length;
        this.board[i][j] = checkMark;
    }

    public boolean checkWin(char player) {
        for (int i = 0; i < this.boardSize; ++i) {
            boolean rowWin = true;
            for (int j = 0; j < this.boardSize; ++j) {
                if (this.board[i][j] != player) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
            boolean colWin = true;
            for (int j = 0; j < this.boardSize; ++j) {
                if (this.board[j][i] != player) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                return true;
            }
        }
        boolean diag1Win = true;
        boolean diag2Win = true;
        for (int i = 0; i < this.boardSize; ++i) {
            if (this.board[i][i] != player) {
                diag1Win = false;
            }
            if (this.board[i][this.boardSize - 1 - i] != player) {
                diag2Win = false;
            }
        }
        if (diag1Win || diag2Win) {
            return true;
        }
        return false;
    }

    public boolean isValidPosition(String position) {
        int pos;
        try {
            pos = Integer.parseInt(position);
        } catch (NumberFormatException e) {
            return false;
        }
        if (pos < 1 || pos > boardSize * boardSize) {
            return false;
        }
        // 
        --pos;
        return board[pos /  boardSize][pos % boardSize] == ' ';
    }

    public boolean isFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
