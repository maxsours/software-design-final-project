package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {

    User user;

    public User generateUser(){
        return new User("alpha", "alpha");
    }

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
     * Make a solved Board.
     */
    private BoardManager createBoardManager() {
        List<Tile> tiles = makeTiles();
        Board board = new Board(tiles, 3);
        return new BoardManager(board);
    }

    @Test
    public void testGetUsername() {
        user = generateUser();
        assertEquals("alpha", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        user = generateUser();
        assertEquals("alpha", user.getPassword());
    }

    @Test
    public void testGetBoardManager() {
        user = generateUser();
        assertNull(user.getBoardManager());
        BoardManager boardManager = createBoardManager();
        user.setBoardManager(boardManager);
        assertEquals(boardManager, user.getBoardManager());
    }

    @Test
    public void testEquals() {
        User anotherUser;
        user = generateUser();
        anotherUser = generateUser();
        assertEquals(user, anotherUser);
        anotherUser = new User("beta", "beta");
        assertNotEquals(user, anotherUser);
    }

    @Test
    public void testToString() {
        user = generateUser();
        assertEquals("alpha", user.toString());
    }
}