package tdd;

import java.util.Scanner;

public class Main {
    private static final int ALL_AUTOMATIC_PLAYERS = 0;
    private static final int ALL_HUMAN_PLAYERS = 2;
    public static void main(String[] args) {
        System.out.println("How many players [0-2]?");
        Scanner sc = new Scanner(System.in);
        int numPlayers = sc.nextInt();
        sc.nextLine(); // Consume the newline character

        if (numPlayers < 0 || numPlayers > 2) {
            System.out.println("Invalid number of players. Please enter a number between 0 and 2.");
            return;
        }

        if (numPlayers == ALL_HUMAN_PLAYERS) {
            TicTacToe game = new TicTacToe();
            game.play();
            return;
        }

        Player player1 = null;
        Player player2 = new AutoPlayer("PLAYER-O", 2, 'O');
        if (numPlayers == ALL_AUTOMATIC_PLAYERS) {
            player1 = new AutoPlayer("PLAYER-X", 1, 'X');
        } else {
            player1 = new Player("PLAYER-X", 1, 'X');
        }

        TicTacToe game = new TicTacToe(player1, player2);
        game.play();
    }
}
