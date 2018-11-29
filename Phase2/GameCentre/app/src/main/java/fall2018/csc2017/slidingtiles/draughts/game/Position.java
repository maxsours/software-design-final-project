package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;

/**
 * Created by Greg on 8/6/2017.
 */ // x y coordinate
public class Position implements Serializable {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position plus(Position to) {
        return new Position(x + to.x, y + to.y);
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof Position) {
            Position otherPosition = (Position)other;
            return (x == otherPosition.x && y == otherPosition.y);
        } else {
            return false;
        }
    }
}
