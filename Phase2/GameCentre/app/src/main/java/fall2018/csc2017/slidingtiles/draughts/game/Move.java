package fall2018.csc2017.slidingtiles.draughts.game;

import java.util.ArrayList;

/**
 * Created by Greg on 8/6/2017.
 */ // move as a sequence of positions and list of capture positions
public class Move {
    public ArrayList<Position> positions;
    public ArrayList<Position> captures;
    public boolean kings;

    public Move(Position pos) {
        init(pos.x, pos.y);
    }

    public Move(int x, int y) {
        init(x, y);
    }

    private void init(int x, int y) {
        Position first = new Position(x, y);
        positions = new ArrayList();
        positions.add(first);
        kings = false;
        captures = new ArrayList();
    }

    // copy constructor
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
