package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;

/**
 * The data for a checker board game.
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class CheckersGame implements Serializable {

    /**
     * A value representing an non-existent piece.
     */
    public static final int NONE = 0;

    /**
     * A value representing a black piece.
     */
    public static final int BLACK = 1;

    /**
     * A value representing a red piece.
     */
    public static final int RED = 2;

    /**
     * A value representing an king piece.
     */
    public static final int KINGED = 3;

    /**
     * A checker game's board.
     */
    private CheckerBoard gameBoard;

    /**
     * A checker game's number of turns.
     */
    private int turn;

    /**
     * Whether a checker game is over or not.
     */
    private boolean over;

    /**
     * A checker game's winner.
     */
    private int winner;

    /**
     * Whether a checker game allows for any move to occur.
     */
    private boolean allowAnyMove;

    /**
     * The start time for a checkers game.
     */
    private double startTime;

    /**
     * The total number of moves played by the user in the checkers game.
     */
    private int totalMoves = 0;

    /**
     * Creates a checkers game with a board state and current turn.
     *
     * @param anyMove whether any move is permitted for this checkers game.
     */
    public CheckersGame(boolean anyMove) {
        gameBoard = new CheckerBoard(this);
        turn = CheckersGame.BLACK;
        over = false;
        winner = CheckersGame.NONE;
        allowAnyMove = anyMove;
    }

    /**
     * Restarts the checker game being played.
     */
    public void restart() {
        gameBoard = new CheckerBoard(this);
        turn = CheckersGame.BLACK;
        over = false;
        winner = CheckersGame.NONE;
    }

    public void setAnyMove(boolean anyMove) {
        allowAnyMove = anyMove;
    }

    /**
     * Returns whose turn it is in the checkers game.
     *
     * @return whose turn it is in the checkers game.
     */
    public int whoseTurn() {
        return turn;
    }

    /**
     * Returns the checker board's game data
     *
     * @return the checker board's game data
     */
    public CheckerBoard getBoard() {
        return this.gameBoard;
    }

    /**
     * Returns the longest move possible for a piece.
     *
     * @param start the piece's start position.
     * @param end the piece's end position.
     * @return the longest move possible for a piece.
     */
    public Move getLongestMove(Position start, Position end) {
        Move longest = null;
        Move moveset[] = getMoves();
        for (Move move : moveset) {
            if (move.start().equals(start) && move.end().equals(end)) {
                if (longest == null ||
                        longest.captures.size() < move.captures.size())
                    longest = move;
            }
        }
        return longest;
    }

    /**
     * Returns the moves played in a checker's game.
     *
     * @return the moves played in a checker's game.
     */
    public Move[] getMoves() {
        return gameBoard.getMoves(turn, allowAnyMove);
    }

    /**
     * Makes a move on the checker's game.
     *
     * @param choice the chosen move to be played.
     */
    public void makeMove(Move choice) {
        gameBoard.makeMove(choice);
        totalMoves++;
        if(totalMoves == 1){
            startTime = System.currentTimeMillis();
        }
        advanceTurn();
    }

    /**
     * Switches turns when the current player's turn has finished.
     */
    private void advanceTurn() {
        if (turn == CheckersGame.RED) {
            turn = CheckersGame.BLACK;
        } else {
            turn = CheckersGame.RED;
        }
    }

}
