package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;

public class CheckersGame implements Serializable {
    public static final int NONE = 0;
    public static final int BLACK = 1;
    public static final int RED = 2;
    public static final int KINGED = 3;

    // checkers game state
    private CheckerBoard gameBoard;
    private int turn;
    private boolean over;
    private int winner;
    private boolean allowAnyMove;

    private double startTime;
    private int totalMoves = 0;

    // checkers game holds board state and current turn
    public CheckersGame(boolean anyMove) {
        gameBoard = new CheckerBoard(this);
        turn = CheckersGame.BLACK;
        over = false;
        winner = CheckersGame.NONE;
        allowAnyMove = anyMove;
    }

    public void restart() {
        gameBoard = new CheckerBoard(this);
        turn = CheckersGame.BLACK;
        over = false;
        winner = CheckersGame.NONE;
    }

    public void setAnyMove(boolean anyMove) {
        allowAnyMove = anyMove;
    }

    // check whose turn it is
    public int whoseTurn() {
        return turn;
    }

    // get the board data
    public CheckerBoard getBoard() {
        return this.gameBoard;
    }

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

    public Move[] getMoves() {
        return gameBoard.getMoves(turn, allowAnyMove);
    }

    // make a move
    public void makeMove(Move choice) {
        gameBoard.makeMove(choice);
        totalMoves++;
        if(totalMoves == 1){
            startTime = System.currentTimeMillis();
        }
        advanceTurn();
    }

    // switch turns
    private void advanceTurn() {
        if (turn == CheckersGame.RED) {
            turn = CheckersGame.BLACK;
        } else {
            turn = CheckersGame.RED;
        }
    }

    public double getStartTime(){
        return startTime;
    }

    public int getTotalMoves() {
        return totalMoves;
    }
}
