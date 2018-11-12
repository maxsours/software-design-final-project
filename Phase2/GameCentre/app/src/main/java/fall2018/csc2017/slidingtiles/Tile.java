package fall2018.csc2017.slidingtiles;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class Tile implements Comparable<Tile>, Serializable {

    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * A Tile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    public Tile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId the id for the background
     * @param res the app Resources
     */
    public Tile(int backgroundId, Resources res) {
        id = backgroundId + 1;
        if ((Board.NUM_COLS * Board.NUM_COLS) == id) {
            background = R.drawable.tile_blank;
        } else if (id >= 1 && id <= 24 && Board.NUM_MODE == 0) {
            background = res.getIdentifier("default_" + id, "drawable",
                    "fall2018.csc2017.slidingtiles");
        } else {
            background = res.getIdentifier("upload_" + Board.NUM_ROWS + "_" + id,
                    "drawable", "fall2018.csc2017.slidingtiles");
        }
    }
    @Override
    public int compareTo(@NonNull Tile o) {
        return o.id - this.id;
    }
}
