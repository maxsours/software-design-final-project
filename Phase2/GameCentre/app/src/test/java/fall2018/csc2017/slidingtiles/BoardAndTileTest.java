package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardAndTileTest {

    /** The board manager for testing. */
    BoardManager boardManager;

    /**
     * Make a set of tiles that are in order.
     * @return a set of tiles that are in order
     */
    private List<Tile> makeTiles() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }

        return tiles;
    }

    /**
     * Make a solved 4x4 Board.
     */
    private void setUpCorrect() {
        List<Tile> tiles = makeTiles();
        Board board = new Board(tiles, 3);
        boardManager = new BoardManager(board);
    }

    /**
     * Shuffle a few tiles.
     */
    private void swapFirstTwoTiles() {
        boardManager.getBoard().swapTiles(0, 0, 0, 1);
    }

    /**
     * Test whether swapping two tiles makes a solved board unsolved.
     */
    @Test
    public void testIsSolved() {
        setUpCorrect();
        assertEquals(true, boardManager.puzzleSolved());
        swapFirstTwoTiles();
        assertEquals(false, boardManager.puzzleSolved());
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo() {
        setUpCorrect();
        assertEquals(1, boardManager.getBoard().getTile(0, 0).getId());
        assertEquals(2, boardManager.getBoard().getTile(0, 1).getId());
        boardManager.getBoard().swapTiles(0, 0, 0, 1);
        assertEquals(2, boardManager.getBoard().getTile(0, 0).getId());
        assertEquals(1, boardManager.getBoard().getTile(0, 1).getId());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo() {
        setUpCorrect();
        assertEquals(15, boardManager.getBoard().getTile(3, 2).getId());
        assertEquals(16, boardManager.getBoard().getTile(3, 3).getId());
        boardManager.getBoard().swapTiles(3, 3, 3, 2);
        assertEquals(16, boardManager.getBoard().getTile(3, 2).getId());
        assertEquals(15, boardManager.getBoard().getTile(3, 3).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap() {
        setUpCorrect();
        assertEquals(true, boardManager.isValidTap(11));
        assertEquals(false, boardManager.isValidTap(15));
        assertEquals(false, boardManager.isValidTap(10));
    }

    /**
     * Test whether isSolvable() works.
     */
    @Test
    public void testIsSolvable() {
        setUpCorrect();
        assertEquals(true, boardManager.isSolvable());
        boardManager.getBoard().swapTiles(3, 3, 2, 2);
        assertEquals(false, boardManager.isSolvable());
        boardManager.fixUnsolvableBoard();
        assertEquals(true, boardManager.isSolvable());
    }

    /**
     * Test whether a 5x5 board is created correctly
     */
    @Test
    public void test5x5(){
        Board.NUM_ROWS = 5;
        Board.NUM_COLS = 5;
        List<Tile> tiles = makeTiles();
        Board board = new Board(tiles, 3);
        boardManager = new BoardManager(board);
        assertEquals(25, boardManager.getTiles().size());
        Board.NUM_ROWS = 4;
        Board.NUM_COLS = 4;
    }

    /**
     * Test whether a 3x3 board is created correctly
     */
    @Test
    public void test3x3(){
        Board.NUM_ROWS = 3;
        Board.NUM_COLS = 3;
        List<Tile> tiles = makeTiles();
        Board board = new Board(tiles, 3);
        boardManager = new BoardManager(board);
        assertEquals(9, boardManager.getTiles().size());
        Board.NUM_ROWS = 4;
        Board.NUM_COLS = 4;
    }
}

