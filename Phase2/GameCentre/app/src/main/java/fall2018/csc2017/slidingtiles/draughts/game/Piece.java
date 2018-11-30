package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;

/**
 * The data for a single checker piece.
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class Piece implements Serializable {
    /**
     * The checker piece's colour.
     */
    private int _color;

    /**
     * Whether the checker piece is a king.
     */
    private boolean _isKing;

    /**
     * Creates a piece.
     *
     * @param color the checker piece's colour.
     * @param king whether the checker piece is a king.
     */
    Piece(int color, boolean king) {
        _color = color;
        _isKing = king;
    }

    /**
     * Returns the checker piece's colour.
     *
     * @return the piece's colour.
     */
    public int getColor() {
        return _color;
    }

    /**
     * Returns whether the checker piece is a king.
     *
     * @return whether the piece is a king.
     */
    public boolean isKing() {
        return _isKing;
    }

    /**
     * Makes the checker piece a king.
     */
    public void makeKing() {
        _isKing = true;
    }
}
