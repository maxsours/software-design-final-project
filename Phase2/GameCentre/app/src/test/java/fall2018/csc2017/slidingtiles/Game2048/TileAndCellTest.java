package fall2018.csc2017.slidingtiles.Game2048;


import org.junit.Test;

import java.lang.reflect.Array;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertTrue;

public class TileAndCellTest {

    /** The 2048 grid for testing */
    Grid grid;
    /** Equivalent cell grid for testing */
    Cell[][] cells = new Cell[4][4];

    /**
     * Create a grid with no tiles
     */
    private void makeClearGrid(){
        grid = new Grid(4, 4);
        createCells();
    }

    /**
     * Create the cells needed for a 4x4 grid
     */
    private void createCells(){
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * Make a grid full of tiles with value 2
     */
    private void makeFullGrid(){
        Tile[][] tiles = new Tile[4][4];
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                tiles[x][y] = new Tile(x, y, 2);
            }
        }
        grid = new Grid(tiles);
        createCells();
    }

    /**
     * Make a grid full of tiles with value 2, except position (3,3) is empty
     */
    private void makeAlmostFullGrid(){
        Tile[][] tiles = new Tile[4][4];
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                tiles[x][y] = new Tile(x, y, 2);
            }
        }
        tiles[3][3] = null;
        grid = new Grid(tiles);
        createCells();
    }

    /**
     * Test the randomAvailableCell method
     */
    @Test
    public void testRandomAvailableCell(){
        makeClearGrid();
        Cell randomCell = grid.randomAvailableCell();
        assertTrue(0 <= randomCell.getX() && randomCell.getX() < 4);
        assertTrue(0 <= randomCell.getY() && randomCell.getY() < 4);
        makeFullGrid();
        assertNull(grid.randomAvailableCell());
        makeAlmostFullGrid();
        assertEquals(3, grid.randomAvailableCell().getX());
        assertEquals(3, grid.randomAvailableCell().getY());
    }

    /**
     * Test the isCellsAvailable method
     */
    @Test
    public void testIsCellsAvailable(){
        makeClearGrid();
        assertTrue(grid.isCellsAvailable());
        makeFullGrid();
        assertFalse(grid.isCellsAvailable());
    }

    /**
     * Test the isCellAvailable method
     */
    @Test
    public void testIsCellAvailable(){
        makeAlmostFullGrid();
        assertTrue(grid.isCellAvailable(cells[3][3]));
        assertFalse(grid.isCellAvailable(cells[1][2]));
    }

    /**
     * Test both versions of getCellContent
     */
    @Test
    public void testGetCellContent(){
        makeFullGrid();
        assertNull(grid.getCellContent(new Cell(4, 4)));
        assertEquals(2, grid.getCellContent(2,2).getX());
        assertEquals(2, grid.getCellContent(2, 2).getY());
        assertEquals(2, grid.getCellContent(2, 2).getValue());
        assertNull(grid.getCellContent(42, 42));
    }

    /**
     * Test insertTile method
     */
    @Test
    public void testInsertTile(){
        makeAlmostFullGrid();
        assertNull(grid.getCellContent(3, 3));
        grid.insertTile(new Tile(3, 3, 4));
        assertEquals(4, grid.getCellContent(3, 3).getValue());
    }

    /**
     * Test removeTile method
     */
    @Test
    public void testRemoveTile(){
        makeFullGrid();
        grid.removeTile(grid.getCellContent(2, 2));
        assertNull(grid.getCellContent(2,2));
    }

    /**
     * Test the methods that save the tiles
     */
    @Test
    public void testSaveTiles(){
        makeAlmostFullGrid();
        grid.prepareSaveTiles();
        grid.saveTiles();
        Tile tile = grid.getCellContent(3, 2);
        grid.removeTile(tile);
        tile.updatePosition(cells[3][3]);
        grid.insertTile(tile);
        assertNull(grid.getCellContent(3, 2));
        assertEquals(2, grid.getCellContent(3, 3).getValue());
        grid.revertTiles();
        assertEquals(2, grid.getCellContent(3, 2).getValue());
        assertNull(grid.getCellContent(3, 3));
    }

    /**
     * Simulate merging two tiles
     */
    @Test
    public void testMergeTiles(){
        makeFullGrid();
        Tile[] removedTiles = new Tile[2];
        removedTiles[0] = grid.getCellContent(3, 3);
        removedTiles[1] = grid.getCellContent(3, 2);
        grid.removeTile(removedTiles[0]);
        grid.removeTile(removedTiles[1]);
        Tile newTile = new Tile(3, 2, removedTiles[0].getValue() + removedTiles[1].getValue());
        newTile.setMergedFrom(removedTiles);
        grid.insertTile(newTile);
        assertEquals(removedTiles, grid.getCellContent(3, 2).getMergedFrom());
    }

    /**
     * Test tile merging and construction
     */
    @Test
    public void testTileConstruction(){
        makeClearGrid();
        grid.insertTile(new Tile(cells[0][0], 2));
        assertNull(grid.getCellContent(0, 0).getMergedFrom());
    }

}
