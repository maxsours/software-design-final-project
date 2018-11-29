package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A move for the checkers game as a sequence of positions and a list of capture positions.
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class Move implements Serializable {
    /**
     * Positions to be utilized for a move.
     */
    public ArrayList<Position> positions;

    /**
     * Capture positions to be utilized for a move.
     */
    public ArrayList<Position> captures;

    /**
     * Whether the piece to be moved is a king.
     */
    public boolean kings;

    /**
     * Creates a Move.
     * @param pos a position for the move.
     */
    public Move(Position pos) {
        init(pos.x, pos.y);
    }

    /**
     * Initialize the move with a given position.
     *
     * @param x a move's row position.
     * @param y a move's column position.
     */
    private void init(int x, int y) {
        Position first = new Position(x, y);
        positions = new ArrayList();
        positions.add(first);
        kings = false;
        captures = new ArrayList();
    }

    /**
     * Creates a copy of the constructor.
     *
     * @param clone the constructor to be cloned.
     */
    public Move(Move clone) {
        kings = clone.kings;
        positions = new ArrayList<>();
        for (Position position : clone.positions) {
            positions.add(position);
        }
        captures = new ArrayList<>();
        for (Position capture : clone.captures) {
            captures.add(capture);
        }
    }

    /**
     *
     *
     * @param pos a move's position
     */
    public void add(Position pos) {
        add(pos.x, pos.y);
    }

    public void add(int x, int y) {
        Position prev = positions.get(positions.size() - 1);
        Position next = new Position(x, y);
        positions.add(next);
        // check if move is a capture
        int dist = Math.abs(prev.x - x) + Math.abs(prev.y - y);
        if (dist > 2) {
            int cx = (prev.x + x) / 2;
            int cy = (prev.y + y) / 2;
            captures.add(new Position(cx, cy));
        }
        // check if move is a king
        if (y == 0 || y == 7) {
            kings = true;
        }
    }

    public Position start() {
        return positions.get(0);
    }

    public Position end() {
        return positions.get(positions.size() - 1);
    }
}
