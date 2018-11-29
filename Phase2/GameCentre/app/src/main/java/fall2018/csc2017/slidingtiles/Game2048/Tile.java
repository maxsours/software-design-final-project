package fall2018.csc2017.slidingtiles.Game2048;

/**
 * The tile in a 2048 game
 */
public class Tile extends Cell {
    private final int value; // the value of the tile
    private Tile[] mergedFrom = null; // the tiles this tile merged from, if any

    /**
     * Create a new tile object
     * @param x the x-coordinate of this tile
     * @param y the y-coordinate of this tile
     * @param value the value of this tile
     */
    public Tile(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    /**
     * Create a new tile object
     * @param cell the cell this tile is being created in
     * @param value the value of this tile
     */
    public Tile(Cell cell, int value) {
        super(cell.getX(), cell.getY());
        this.value = value;
    }

    /**
     * Update this position to be the corresponding position of the cell
     * @param cell the cell at the position this tile is going to be at
     */
    public void updatePosition(Cell cell) {
        this.setX(cell.getX());
        this.setY(cell.getY());
    }

    /**
     * Return the value of this tile
     * @return the value of this tile
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Get the tiles this tile merged from
     * @return the tiles this tile merged from
     */
    public Tile[] getMergedFrom() {
        return mergedFrom;
    }

    /**
     * Set the tiles this tile merged from
     * @param tile the tiles this tile merged from
     */
    public void setMergedFrom(Tile[] tile) {
        mergedFrom = tile;
    }
}
