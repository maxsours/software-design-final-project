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

        boolean solvable = isSolvable();
        if (!solvable) {
            fixUnsolvableBoard();
        }


    }

    /**
     * Fixes the unsolvable board to make it solvable.
     * Adds or takes away an inversion from the board.
     */
    void fixUnsolvableBoard() {
        int blankTilePos = getBlankPosition(board.numTiles());

        if ((blankTilePos != 1) && (blankTilePos != 2))
            board.swapTiles(0,0,0,1);
        else
            board.swapTiles(Board.NUM_ROWS -1, Board.NUM_COLS -1, Board.NUM_ROWS -1, Board.NUM_COLS -2);

    }

    /**
     * Get the tiles in the board manager
     * @return the list of tiles
     */
    List<Tile> getTiles(){
        List<Tile> tiles = new ArrayList<>();
        for(Tile tile : board){
            tiles.add(tile);
        }
        return tiles;
    }

    /**
     * Returns the numbers of inversions from a list row-major representation of the board.
     * @return the number of inversions
     */
    private int countInversions(List<Tile> tiles) {
        int count = 0;
        int size = tiles.size();

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if ((tiles.get(i).getId() > tiles.get(j).getId()) && ((tiles.get(i).getId() != size) && (tiles.get(j).getId() != size))){
                    count += 1;
                }
            }
        }
        return count;
    }

    /**
     * Determines if the current board is solvable.
     * formula adapted from www.cs.princeton.edu/courses/archive/spr18/cos226/assignments/8puzzle/index.html
     * @return whether the board is solvable.
     */
   boolean isSolvable(){
       int numInver = countInversions(getTiles());
        if ((Board.NUM_ROWS % 2) != 0)
            return (numInver % 2 == 0);
        else {
            //iff the number of inversions plus the row of the blank square is odd.
            int blankRow = getBlankTileRow();
            return ((numInver + blankRow) % 2 != 0);
        }
    }

    /**
     * @param size_board the size of the current game board.
     * @return the position of the blank tile from row-order.
     */
    private int getBlankPosition(int size_board){
        int count = 0;
        for (Tile tile: this.board){
            count += 1;
            if (tile.getId() == size_board)
                    break;
        }
        return count;
    }

    /**
     * get the row of the blank tile on the board, rows counting from the bottom
     * For example, bottom row is row 1
     * @return the row where the blank tile is on.
     */
    private int getBlankTileRow(){
        int size = this.board.numTiles();
        if (size == 9)
            return blankTileRow3x3(getBlankPosition(size));
        else if (size == 16)
            return blankTileRow4x4(getBlankPosition(size));
        else if (size == 25)
            return blankTileRow5x5(getBlankPosition(size));
        return 0;
    }
    /**
     * Determine the row in which the blank tile is placed in a 3x3 board.
     * @param position the position blank tile is on in row-major order.
     * @return the row which the blank tile is in.
     */
    private int blankTileRow3x3(int position){
        if (1 <= position && position <= 3)
            return 3;
        else if (4 <= position && position <= 6)
            return 2;
        else if (7 <= position && position <= 9)
            return 1;
        else
            return 3;
    }

    /**
     * Determine the row in which the blank tile is placed in a 4x4 board.
     * @param position the position the blank tile is on in row-major order.
     * @return the row which the blank tile is in.
     */
    private int blankTileRow4x4(int position){
        if (1 <= position && position <= 4)
            return 4;
        else if (5 <= position && position <= 8)
            return 3;
        else if (9 <= position && position <= 12)
            return 2;
        else if (13 <= position && position <= 16)
            return 1;
        else
            return 4;
    }
    /**
     * Determine the row in which the blank tile is placed in a 5x5 board.
     * @param  position the posiiton the blank tile is on in row-major order.
     * @return the row which the blank tile is in.
     */
    private int blankTileRow5x5(int position){
        if (1 <= position && position <= 5)
            return 5;
        else if (6 <= position && position <= 10)
            return 4;
        else if (11 <= position && position <= 15)
            return 3;
        else if (16 <= position && position <= 20)
            return 2;
        else if (21 <= position && position <= 25)
            return 1;
        else
            return 5;
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
