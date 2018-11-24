package fall2018.csc2017.slidingtiles;

import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.Observable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The sliding tiles board.
 *
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {

    /**
     * keep track of the total moves while user is playing the puzzle
     */
    private int totalMove=0;

    /**
     * The number of rows.
     */
    static int NUM_ROWS = 4;

    /**
     * The number of columns.
     */
    static int NUM_COLS = 4;

    /**
     * The game mode.
     */
    static int NUM_MODE = 0;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles = new Tile[NUM_ROWS][NUM_COLS];
    /**
     * The previous moves executed.
     */
    private List<int[]> moves = new ArrayList<>();


    /**
     * Number of undos available.
     */
     private int numUndos;
    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     * @param numUndos the number of undos allowed for this board.
     */
    Board(List<Tile> tiles, int numUndos) {
        Iterator<Tile> iter = tiles.iterator();

        for (int row = 0; row != Board.NUM_ROWS; row++) {
            for (int col = 0; col != Board.NUM_COLS; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
        this.numUndos = numUndos;
    }

    /**
     * Return the number of tiles on the board.
     * @return the number of tiles on the board
     */
    int numTiles() {
        return Board.NUM_ROWS * Board.NUM_COLS;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Get the number of undos left.
     * @return number of undos.
     */
    int getNumUndos(){
        return this.numUndos;
    }

    /**
     * Swaps the tiles at (ro1, col1) and (row2, col2) and does NOT add the move to move history.
     * @param row1 the first tiles row
     * @param col1 the first tiles column
     * @param row2 the second tiles row
     * @param col2 the second tiles column
     */
    void swapTilesNoHistory(int row1, int col1, int row2, int col2){
        Tile temp = getTile(row1, col1);
        tiles[row1][col1] = getTile(row2, col2);
        tiles[row2][col2] = temp;

        setChanged();
        notifyObservers();
    }


    /**
     * Swap the tiles at (row1, col1) and (row2, col2) and adds the move to move history.
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        Tile temp = getTile(row1, col1);
        int[] move = {row1,col1,row2,col2};
        addMove(move);
        totalMove++;
        tiles[row1][col1] = getTile(row2, col2);
        tiles[row2][col2] = temp;

        setChanged();
        notifyObservers();
    }

    /**
     * Adds a move to the current move list while also limiting its size.
     * @param move the move with the two swapped tiles coordinates.
     */
    private void addMove(int[] move){
        if (this.numUndos <= 0)
        {
            //do nothing
        }
         else if (this.moves.size() >= 10){
            this.moves.remove(0);
            this.moves.add(move);
        }
        else {
            this.moves.add(move);
        }
    }

    /**
     * Pops a past move from move history and executes the swap.
     */
    void popUndoMove(){
        if ((this.moves.size() != 0) && (this.numUndos > 0)) {
            int[] move = this.moves.remove(moves.size() - 1);
            swapTilesNoHistory(move[0], move[1], move[2], move[3]);

            this.numUndos -= 1;
        }
    }

    /**
     * Get total moves.
     * @return the total number of moves
     */
    int getTotalMove(){
        return this.totalMove;
    }

    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        List<Tile> tileList = new ArrayList<>();
        for (int r = 0; r < Board.NUM_ROWS; r++){
            for (int c = 0; c < Board.NUM_COLS; c++){
                tileList.add(tiles[r][c]);
            }
        }
        return tileList.iterator();
    }
}
