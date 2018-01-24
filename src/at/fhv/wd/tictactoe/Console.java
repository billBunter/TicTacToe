package at.fhv.wd.tictactoe;

import at.fhv.wd.logic.Algorithms;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.Scanner;

public class Console {

    private Board _board;
    private Scanner _sc = new Scanner(System.in);

    private enum Mode{AI, Player};
    private Mode _mode;
    private enum Algo{Random, MinMax, AlphaBeta}
    private Algo _algo;

    /**
     * Construct Console.
     */
    private Console(Mode mode, Algo algo) {
        _board = new Board();
        _algo = algo;
        _mode = mode;
    }

    /**
     * Begin the game.
     */
    private void play () {

        System.out.println("Starting a new game.");

        while (true) {
            printGameStatus();
            playMove();

            if (_board.isGameOver()) {
                printWinner();

                if (!tryAgain()) {
                    break;
                }
            }
        }
    }

    /**
     * Handle the move to be played, either by the player or the AI.
     */
    private void playMove () {
        if (_mode == Mode.AI){
            if (_board.getTurn() == Board.State.X) {
                getPlayerMove();
            } else {
                if (_algo == Algo.AlphaBeta) {
                    Algorithms.alphaBetaAdvanced(_board);
                } else if (_algo == Algo.Random) {
                    Algorithms.random(_board);
                } else if (_algo == Algo.MinMax) {
                    Algorithms.minMax(_board);
                }
            }
        } else if (_mode == Mode.Player){
            getPlayerMove();
        }

    }

    /**
     * Print out the _board and the player who's turn it is.
     */
    private void printGameStatus () {
        System.out.println("\n" + _board + "\n");
        System.out.println(_board.getTurn().name() + "'s turn.");
    }

    /**
     * For reading in and interpreting the move that the user types into the console.
     */
    private void getPlayerMove () {
        System.out.print("Index of move: ");

        int move = _sc.nextInt();

        if (move < 0 || move >= Board.BOARD_WIDTH* Board.BOARD_WIDTH) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (Board.BOARD_WIDTH * Board.BOARD_WIDTH - 1) + ", inclusive.");
        } else if (!_board.move(move)) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
    }

    /**
     * Print out the winner of the game.
     */
    private void printWinner () {
        Board.State winner = _board.getWinner();

        System.out.println("\n" + _board + "\n");

        if (winner == Board.State.Blank) {
            System.out.println("The TicTacToe is a Draw.");
        } else {
            System.out.println("Player " + winner.toString() + " wins!");
        }
    }

    /**
     * Reset the game if the player wants to play again.
     * @return      true if the player wants to play again
     */
    private boolean tryAgain () {
        if (promptTryAgain()) {
            _board.reset();
            System.out.println("Started new game.");
            System.out.println("X's turn.");
            return true;
        }

        return false;
    }

    /**
     * Ask the player if they want to play again.
     * @return      true if the player wants to play again
     */
    private boolean promptTryAgain () {
        while (true) {
            System.out.print("Would you like to start a new game? (Y/N): ");
            String response = _sc.next();
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Invalid input.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Console ticTacToe = null;
        System.out.println("Do you want to play a frind (f) or the comuputer(c)?");
        String mode = scanner.next();
        if (mode.equals("f")){
            ticTacToe = new Console(Mode.Player, Algo.Random);
        } else if (mode.equals("c")){
            System.out.println("What algorythem should be used?\nAlphaBeta (a), Random (r), MinMax (m)");
            String algo = scanner.next();
            if (algo.equals("a")){
                ticTacToe = new Console(Mode.AI, Algo.AlphaBeta);
            }
            if (algo.equals("r")){
                ticTacToe = new Console(Mode.AI, Algo.Random);
            }
            if (algo.equals("m")){
                ticTacToe = new Console(Mode.AI, Algo.MinMax);
            }
        }
        if (ticTacToe != null) {
            ticTacToe.play();
        }
    }
}
