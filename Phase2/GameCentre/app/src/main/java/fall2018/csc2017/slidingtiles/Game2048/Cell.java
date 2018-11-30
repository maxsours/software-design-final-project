package fall2018.csc2017.slidingtiles.Game2048;




/*
 * Adapted from a open source project from Jerry Jiang:
 * https://github.com/tpcstld/2048/blob/master/2048/2048/src/main/java/com/tpcstld/twozerogame/Cell.java
 */


/**
 * A cell in a 2048 game. May or may not be blank.
 */
public class Cell {
    private int x; // x-coordinate
    private int y; // y-coordinate

    /**
     * Create a new Cell object in the grid
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Return the x-coordinate
     * @return the x-coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Change the x-coordinate
     * @param x the new x-coordinate
     */
    void setX(int x) {
        this.x = x;
    }

    /**
     * Return the y-coordinate
     * @return the y-coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Change the y-coordinate
     * @param y the new y-coordinate
     */
    void setY(int y) {
        this.y = y;
    }
}
