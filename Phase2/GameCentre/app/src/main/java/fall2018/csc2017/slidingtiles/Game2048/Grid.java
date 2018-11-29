package fall2018.csc2017.slidingtiles.Game2048;

import java.util.ArrayList;

/**
 * A grid for a 2048 game
 */
public class Grid {

    public final Tile[][] field; //field of game tiles
    public final Tile[][] undoField; //save of game tiles for undo feature
    private final Tile[][] bufferField; //intermediary tileset before saving undoField

    /**
     * Create new Grid object
     * @param sizeX num rows
     * @param sizeY num cols
     */
    public Grid(int sizeX, int sizeY) {
        field = new Tile[sizeX][sizeY];
        undoField = new Tile[sizeX][sizeY];
        bufferField = new Tile[sizeX][sizeY];
        clearGrid();
        clearUndoGrid();
    }

    /**
     * Create a new Grid based on a 2-D array of tiles
     * @param tiles 2-D array of tiles
     */
    public Grid(Tile[][] tiles){
        field = tiles;
        int sizeX = field.length;
        int sizeY = field[0].length;
        undoField = new Tile[sizeX][sizeY];
        bufferField = new Tile[sizeX][sizeY];
    }

    /**
     * Return an available cell at random
     * @return an available cell chosen at random
     */
    public Cell randomAvailableCell() {
        ArrayList<Cell> availableCells = getAvailableCells();
        if (availableCells.size() >= 1) {
            return availableCells.get((int) Math.floor(Math.random() * availableCells.size()));
        }
        return null;
    }

    /**
     * Return a list of cells with no active tiles in them
     * @return a list of cells with no active tiles in them
     */
    private ArrayList<Cell> getAvailableCells() {
        ArrayList<Cell> availableCells = new ArrayList<>();
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    availableCells.add(new Cell(xx, yy));
                }
            }
        }
        return availableCells;
    }

    /**
     * Return true iff there is at least one available cell
     * @return true iff there is at least one available cell
     */
    public boolean isCellsAvailable() {
        return (getAvailableCells().size() >= 1);
    }

    /**
     * Return true iff this specific cell is available
     * @param cell the cell to be checked
     * @return true iff cell is available
     */
    public boolean isCellAvailable(Cell cell) {
        return !isCellOccupied(cell);
    }

    /**
     * Return true iff the cell is occupied
     * @param cell the cell to be checked
     * @return true iff the cell is occupied
     */
    public boolean isCellOccupied(Cell cell) {
        return (getCellContent(cell) != null);
    }

    /**
     * Get the content of the cell
     * @param cell the cell
     * @return the content of the cell
     */
    public Tile getCellContent(Cell cell) {
        if (cell != null && isCellWithinBounds(cell)) {
            return field[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    }

    /**
     * Get the content of a cell at (x,y)
     * @param x the x-position
     * @param y the y-position
     * @return the content at the cell specified by (x,y)
     */
    public Tile getCellContent(int x, int y) {
        if (isCellWithinBounds(x, y)) {
            return field[x][y];
        } else {
            return null;
        }
    }

    /**
     * Return true iff the cell is within the bounds of field
     * @param cell the cell
     * @return true iff cell is within bounds
     */
    public boolean isCellWithinBounds(Cell cell) {
        return 0 <= cell.getX() && cell.getX() < field.length
                && 0 <= cell.getY() && cell.getY() < field[0].length;
    }

    /**
     * Return true iff cell at (x,y) is within bounds of field
     * @param x x-position
     * @param y y-position
     * @return true iff cell at (x,y) is within the bounds
     */
    private boolean isCellWithinBounds(int x, int y) {
        return 0 <= x && x < field.length
                && 0 <= y && y < field[0].length;
    }

    /**
     * Insert a tile into the field
     * @param tile the tile to be inserted
     */
    public void insertTile(Tile tile) {
        field[tile.getX()][tile.getY()] = tile;
    }

    /**
     * Remove tile from field
     * @param tile tile to be removed
     */
    public void removeTile(Tile tile) {
        field[tile.getX()][tile.getY()] = null;
    }

    /**
     * Save the tiles in bufferField to undoField
     */
    public void saveTiles() {
        for (int xx = 0; xx < bufferField.length; xx++) {
            for (int yy = 0; yy < bufferField[0].length; yy++) {
                if (bufferField[xx][yy] == null) {
                    undoField[xx][yy] = null;
                } else {
                    undoField[xx][yy] = new Tile(xx, yy, bufferField[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * Prepare the tiles to be saved by copying field to bufferField
     */
    public void prepareSaveTiles() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    bufferField[xx][yy] = null;
                } else {
                    bufferField[xx][yy] = new Tile(xx, yy, field[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * Revert the tiles to the undoField
     */
    public void revertTiles() {
        for (int xx = 0; xx < undoField.length; xx++) {
            for (int yy = 0; yy < undoField[0].length; yy++) {
                if (undoField[xx][yy] == null) {
                    field[xx][yy] = null;
                } else {
                    field[xx][yy] = new Tile(xx, yy, undoField[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * Clear all tiles from the grid
     */
    public void clearGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                field[xx][yy] = null;
            }
        }
    }

    /**
     * Clear all tiles from undo grid
     */
    private void clearUndoGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                undoField[xx][yy] = null;
            }
        }
    }
}
