sequenceDiagram
    autonumber
    participant Main
    actor User as (output/sc)
    participant TicTacToe
    participant Board
    participant Player

    activate Main
    Main->>TicTacToe: new TicTacToe()
    Main->>TicTacToe: play()
    deactivate Main
    
    activate TicTacToe
    TicTacToe->>Player:new Player()
    TicTacToe->>Player:new Player()
    TicTacToe->>User: playAgain()
    User-->>TicTacToe: 
    
    alt if answer is not Y/y
        TicTacToe->>User: printResults()
        TicTacToe->>Main: gameOver()
    end
        TicTacToe->>User: getBoardSize()
        User-->>TicTacToe:
        TicTacToe->>Board: new Board(boardSize)

    loop while true
        TicTacToe->>Player: move(board)
        deactivate TicTacToe
        activate Player
        
        loop 
            Player->>Board: board.print()
            Board-->>User: 
            Player->>User: get move
            User-->>Player:
            Player->>Board: isValidPosition(move)
            Board-->>Player:
        end
        
        Player->>Board: placeTheMove(marker, movePosition)
        deactivate Player

        activate TicTacToe
        TicTacToe->>Board: checkWin(marker)
        Board-->>TicTacToe:
        
        alt victory
            TicTacToe->>User: declaration
            TicTacToe->>Player:incrementNumberOfWins()
            TicTacToe->>User: playAgain()
            User-->>TicTacToe: 
        end
            TicTacToe->>Board: isFull()
            Board-->>TicTacToe: 
        alt tie
            TicTacToe->>User:declaration
            TicTacToe->>User: playAgain()
            User-->>TicTacToe: 
        end
        
    end
    deactivate TicTacToe