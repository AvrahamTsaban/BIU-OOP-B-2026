classDiagram
    class Main {
        -int ALL_AUTOMATIC_PLAYERS = 0$
        -int ALL_HUMAN_PLAYERS = 2$
        +main(String[] args)$
    }

    class TicTacToe {
        -int MIN_BOARD_SIZE = 3$
        -int MAX_BOARD_SIZE = 10$
        -Scanner sc$
        -Player player1
        -Player player2
        -Board board
        +TicTacToe()
        +TicTacToe(Player player1, Player player2)
        +play()
        -gameOver()
        ~handleWinner(Player winner)
        -welcome()
        -getBoardSize() int
        ~verifyBoardSize(String boardSize) boolean
        -playAgain() boolean
        +printResults()
    }

    class Board {
        ~int boardSize
        ~char[][] board
        -Board()
        +Board(int boardSize)
        +setBoardSize(int boardSize)
        +getBoardSize() int
        -setBoard()
        +print()
        +placeTheMove(char checkMark, int movePosition)
        +checkWin(char player) boolean
        +isValidPosition(String position) boolean
        +isFull() boolean
    }

    class Player {
        -int id
        -String name
        -char marker
        -Scanner sc
        -int numberOfWins
        +Player(String name, int id, char marker)
        +getId() int
        +getName() String
        +getMarker() char
        +getSC() Scanner
        +closeSC()
        +getNumberOfWins() int
        +incrementNumberOfWins()
        +resetNumberOfWins()
        +move(Board board)
    }

    class AutoPlayer {
        +AutoPlayer(String name, int id, char marker)
        +move(Board board)
    }

    Player <|-- AutoPlayer
    TicTacToe --o Player
    TicTacToe --o Board
    Main .. TicTacToe
    Main .. Player
    Player .. Board