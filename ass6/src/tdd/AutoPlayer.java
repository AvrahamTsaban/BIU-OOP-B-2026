package tdd;

import java.util.ArrayList;
import java.util.Random;

public class AutoPlayer extends Player {
    public AutoPlayer(String name, int id, char marker) {
        super(name, id, marker);
    }

    @Override
    public void move(Board board) {
        Random rand = new Random();
        System.out.println("Player " + super.getName() + ", please enter your move. (enter a value from 1 - " + board.getBoardSize() * board.getBoardSize() + ")");
        board.print();

        // Generate a list of valid moves to avoid too long loops in case of a nearly full board
        ArrayList<Integer> validMoves = new ArrayList<>();
        for (int i = 1; i <= board.getBoardSize() * board.getBoardSize(); i++) {
            if (board.isValidPosition(Integer.toString(i))) {
                validMoves.add(i);
            }
        }
        if (validMoves.isEmpty()) {
            return; // No valid moves available
        }

        int movePosition = validMoves.get(rand.nextInt(validMoves.size()));
        board.placeTheMove(super.getMarker(), movePosition);
    }
}
