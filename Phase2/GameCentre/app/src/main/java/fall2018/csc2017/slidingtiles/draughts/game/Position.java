package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;

/**
 * A position for a checkers game move.
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class Position implements Serializable {

    /**
     * The position's row position.
     */
    public final int x;

    /**
     * The position's column position.
     */
    public final int y;

    /**
     * Creates a checkers game position.
     *
     * @param x the position's row position.
     * @param y the position's column position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds to's position to the current position.
     *
     * @param to the position to be added to the current position.
     * @return the summed position of to and the current position.
     */
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
