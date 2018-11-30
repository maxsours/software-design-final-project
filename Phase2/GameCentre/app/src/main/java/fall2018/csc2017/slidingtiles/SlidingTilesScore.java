package fall2018.csc2017.slidingtiles;

/**
 * A score for slidingTiles
 */
public class SlidingTilesScore extends Score {

    private int puzzleSize;

    public SlidingTilesScore(String user, int score, int puzzleSize, String completionDate){
        super(user, score, completionDate);
        this.puzzleSize = puzzleSize;
    }

    public int getPuzzleSize() {
        return puzzleSize;
    }
}
