package fall2018.csc2017.slidingtiles.draughts.game;

import android.os.ParcelFileDescriptor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A test for checkers
 */
public class CheckersGameTest {

    /**
     * CheckersGame for testing purposes
     */
    CheckersGame game;

    /**
     * Create new game with starting positions
     */
    private void createNewGame(){
        game = new CheckersGame(true);
    }

    /**
     * Create game with positions specified by board
     * @param board 2-D array of positions
     */
    private void createGameWithBoard(Integer[][] board){
        game = new CheckersGame(new CheckerBoard(board), CheckersGame.BLACK, false, CheckersGame.NONE, true);
    }

    /**
     * Generate x by y board full of 0s
     * @param x number of rows
     * @param y number of cols
     * @return an x by y board full of 0s
     */
    private Integer[][] zeros(int x, int y){
        Integer[][] board = new Integer[x][y];
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                board[i][j] = 0;
            }
        }
        return board;
    }

    /**
     * Generate a board with a double capture
     * @return a board with a double capture
     */
    private Integer[][] boardWithDoubleCapture(){
        Integer[][] board = zeros(8, 8);
        board[7][4] = CheckersGame.BLACK;
        board[6][3] = CheckersGame.RED;
        board[4][3] = CheckersGame.RED;
        return board;

    }

    @Test
    public void restart() {
        createGameWithBoard(boardWithDoubleCapture());
        assertEquals(CheckersGame.RED, game.getBoard().getPiece(6, 3).getColor());
        game.restart();
        assertNull(game.getBoard().getPiece(6, 3));
    }

    /**
     * Test whether getter and setter methods for game.allowAnyMove work
     */
    @Test
    public void testSetAnyMove() {
        createNewGame();
        assertTrue(game.getAllowAnyMove());
        game.setAnyMove(false);
        assertFalse(game.getAllowAnyMove());
    }

    /**
     * Test whoseTurn method
     */
    @Test
    public void testWhoseTurn() {
        createNewGame();
        assertEquals(CheckersGame.BLACK, game.whoseTurn());
    }

    @Test
    public void testGetLongestMove() {
        createGameWithBoard(boardWithDoubleCapture());
        Move move = game.getLongestMove(new Position(7, 4), new Position(5, 2));
        assertEquals(new Position(7, 4), move.start());
        assertEquals(new Position(5, 2), move.end());
        game.setAnyMove(false);
        move = game.getLongestMove(new Position(7, 4), new Position(5, 2));
        assertEquals(new Position(7, 4), move.start());
        assertEquals(new Position(5, 2), move.end());
    }

    @Test
    public void getMoves() {
    }

    @Test
    public void makeMove() {
    }
}