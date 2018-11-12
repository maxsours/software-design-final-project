package fall2018.csc2017.slidingtiles;

import android.content.res.Resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class BoardManager implements Serializable {

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * Manage a board that has been pre-populated.
     * @param board the board
     */
    BoardManager(Board board) {

        this.board = board;
    }

    /**
     * Return the current board.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     *
     * @param res the Resources that need to get passed to the Tile class
     * @param numUndos the number of undos allowed for this game instance.
     */
    BoardManager(Resources res, int numUndos) {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum, res));
        }
        Collections.shuffle(tiles);
        this.board = new Board(tiles, numUndos);
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        boolean solved = true;
        Iterator<Tile> tileIterator = board.iterator();
        Tile prevTile = tileIterator.next();
        while (tileIterator.hasNext()){
            Tile currTile = tileIterator.next();
            if (prevTile.getId() >= currTile.getId()){
                solved = false;
                break;
            }
            prevTile = currTile;
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {
        int row = position / Board.NUM_COLS;
        int col = position % Board.NUM_COLS;
        int blankId = board.numTiles();
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == Board.NUM_ROWS - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == Board.NUM_COLS - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {
        int row = position / Board.NUM_ROWS;
        int col = position % Board.NUM_COLS;
        int blankId = board.numTiles();
        Tile[] adjacentTiles = getAdjacentTiles(row, col);
        int[][] posChange = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < adjacentTiles.length; i++){
            if (isBlankTile(adjacentTiles[i], blankId)){
                board.swapTiles(row, col, row + posChange[i][0], col + posChange[i][1]);
            }
        }
    }

    /**
     * Return true iff the tile is blank
     *
     * @param tile the tile being checked
     * @param blankId the id that signifies the blank tile
     * @return true iff tile is blank
     */
    private boolean isBlankTile(Tile tile, int blankId){
        return tile != null && tile.getId() == blankId;
    }

    /**
     * Return an array of the adjacent tiles
     *
     * @param row the row position of the center tile
     * @param col the col position of the center tile
     * @return an array of the adjacent tiles
     */
    private Tile[] getAdjacentTiles(int row, int col){
        Tile[] result = new Tile[4];
        result[0] = row == 0 ? null : board.getTile(row - 1, col);
        result[1] = row == Board.NUM_ROWS - 1 ? null : board.getTile(row + 1, col);
        result[2] = col == 0 ? null : board.getTile(row, col - 1);
        result[3] = col == Board.NUM_COLS - 1 ? null : board.getTile(row, col + 1);
        return result;
    }

}
