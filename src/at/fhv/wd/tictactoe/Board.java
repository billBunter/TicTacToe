package at.fhv.wd.tictactoe;

import java.util.HashSet;

public class Board {

    static final int BOARD_WIDTH = 3;
    static final int BOARD_HIGHT = 3;


    public enum State {Blank, X, O}
    private State[][] _board;
    private State _playersTurn;
    private State _winner;
    private HashSet<Integer> _movesAvailable;

    private int moveCount;
    private boolean gameOver;

    /**
     * Construct the Tic Tac Toe _board.
     */
    Board() {
        _board = new State[BOARD_WIDTH][BOARD_HIGHT];
        _movesAvailable = new HashSet<>();
        reset();
    }

    /**
     * Set the cells to be blank and load the available moves (all the moves are
     * available at the start of the game).
     */
    private void initialize () {
        for (int row = 0; row < BOARD_HIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                _board[row][col] = State.Blank;
            }
        }

        _movesAvailable.clear();

        for (int i = 0; i < BOARD_WIDTH*BOARD_HIGHT; i++) {
            _movesAvailable.add(i);
        }
    }

    /**
     * Restart the game with a new blank _board.
     */
    void reset () {
        moveCount = 0;
        gameOver = false;
        _playersTurn = State.X;
        _winner = State.Blank;
        initialize();
    }

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index     the position on the _board (example: index 4 is location (0, 1))
     * @return          true if the move has not already been played
     */
    public boolean move (int index) {
        return move(index% BOARD_WIDTH, index/ BOARD_WIDTH);
    }

    /**
     * Places an X or an O on the specified location depending on who turn it is.
     * @param x         the x coordinate of the location
     * @param y         the y coordinate of the location
     * @return          true if the move has not already been played
     */
    private boolean move (int x, int y) {

        if (gameOver) {
            throw new IllegalStateException("TicTacToe is over. No moves can be played.");
        }

        if (_board[y][x] == State.Blank) {
            _board[y][x] = _playersTurn;
        } else {
            return false;
        }

        moveCount++;
        _movesAvailable.remove(y * BOARD_WIDTH + x);

        // The game is a draw.
        if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
            _winner = State.Blank;
            gameOver = true;
        }

        // Check for a _winner.
        checkRow(y);
        checkColumn(x);
        checkDiagonalFromTopLeft(x, y);
        checkDiagonalFromTopRight(x, y);

        _playersTurn = (_playersTurn == State.X) ? State.O : State.X;
        return true;
    }

    /**
     * Check to see if the game is over (if there is a _winner or a draw).
     * @return          true if the game is over
     */
    public boolean isGameOver () {
        return gameOver;
    }

    /**
     * Get a copy of the array that represents the _board.
     * @return          the _board array
     */
    State[][] toArray () {
        return _board.clone();
    }

    /**
     * Check to see who's turn it is.
     * @return          the player who's turn it is
     */
    public State getTurn () {
        return _playersTurn;
    }

    /**
     * Check to see who won.
     * @return          the player who won (or Blank if the game is a draw)
     */
    public State getWinner () {
        if (!gameOver) {
            throw new IllegalStateException("TicTacToe is not over yet.");
        }
        return _winner;
    }

    /**
     * Get the indexes of all the positions on the _board that are empty.
     * @return          the empty cells
     */
    public HashSet<Integer> getAvailableMoves () {
        return _movesAvailable;
    }

    /**
     * Checks the specified row to see if there is a _winner.
     * @param row       the row to check
     */
    private void checkRow (int row) {
        for (int i = 1; i < BOARD_WIDTH; i++) {
            if (_board[row][i] != _board[row][i-1]) {
                break;
            }
            if (i == BOARD_WIDTH -1) {
                _winner = _playersTurn;
                gameOver = true;
            }
        }
    }

    /**
     * Checks the specified column to see if there is a _winner.
     * @param column    the column to check
     */
    private void checkColumn (int column) {
        for (int i = 1; i < BOARD_WIDTH; i++) {
            if (_board[i][column] != _board[i-1][column]) {
                break;
            }
            if (i == BOARD_WIDTH -1) {
                _winner = _playersTurn;
                gameOver = true;
            }
        }
    }

    /**
     * Check the left diagonal to see if there is a _winner.
     * @param x         the x coordinate of the most recently played move
     * @param y         the y coordinate of the most recently played move
     */
    private void checkDiagonalFromTopLeft (int x, int y) {
        if (x == y) {
            for (int i = 1; i < BOARD_WIDTH; i++) {
                if (_board[i][i] != _board[i-1][i-1]) {
                    break;
                }
                if (i == BOARD_WIDTH -1) {
                    _winner = _playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    /**
     * Check the right diagonal to see if there is a _winner.
     * @param x     the x coordinate of the most recently played move
     * @param y     the y coordinate of the most recently played move
     */
    private void checkDiagonalFromTopRight (int x, int y) {
        if (BOARD_WIDTH -1-x == y) {
            for (int i = 1; i < BOARD_WIDTH; i++) {
                if (_board[BOARD_WIDTH -1-i][i] != _board[BOARD_WIDTH -i][i-1]) {
                    break;
                }
                if (i == BOARD_WIDTH -1) {
                    _winner = _playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    /**
     * Get a deep copy of the Tic Tac Toe _board.
     * @return      an identical copy of the _board
     */
    public Board getDeepCopy () {
        Board board             = new Board();

        for (int i = 0; i < board._board.length; i++) {
            board._board[i] = this._board[i].clone();
        }

        board._playersTurn = this._playersTurn;
        board._winner = this._winner;
        board._movesAvailable = new HashSet<>();
        board._movesAvailable.addAll(this._movesAvailable);
        board.moveCount         = this.moveCount;
        board.gameOver          = this.gameOver;
        return board;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < BOARD_WIDTH; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {

                if (_board[y][x] == State.Blank) {
                    sb.append("-");
                } else {
                    sb.append(_board[y][x].name());
                }
                sb.append(" ");

            }
            if (y != BOARD_WIDTH -1) {
                sb.append("\n");
            }
        }

        return new String(sb);
    }

}
