package fall2018.csc2017.slidingtiles.draughts.game;

import java.io.Serializable;

/**
 * Created by Greg on 8/6/2017.
 */ // data for a single piece
public class Piece implements Serializable {
    private int _color;
    private boolean _isKing;

    Piece(int color, boolean king) {
        _color = color;
        _isKing = king;
    }

    public int getColor() {
        return _color;
    }

    public boolean isKing() {
        return _isKing;
    }

    public void makeKing() {
        _isKing = true;
    }
}
