package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScoreTest {

    Score score;

    private Score buildScore(){
        return new Score("gamma", 3, "April 1");
    }
    /**
     * Build a sliding tiles score for testing purposes
     * @return a score that can be used for testing
     */
    private Score buildSlidingTilesScore(){
        return new SlidingTilesScore("alpha", 42, 3, "March 14");
    }

    private CheckersScore buildCheckersScore(){
        return new CheckersScore("beta", 43, "Easy", "October 22");
    }
    /**
     * Test whether compareTo works.
     */
    @Test
    public void testCompareTo() {
        score = buildScore();
        Score anotherScore = buildScore();
        assertEquals(0, score.compareTo(anotherScore));
        anotherScore = new Score(score.getUser(), score.getScore() - 1, score.getDate());
        assertEquals(1, score.compareTo(anotherScore));
        anotherScore = new Score(score.getUser(), score.getScore() + 1, score.getDate());
        assertEquals(-1, score.compareTo(anotherScore));
    }

    /**
     * Test the functionality unique to CheckersScore
     */
    @Test
    public void testCheckersScore(){
        score = buildCheckersScore();
        assertEquals("Easy", ((CheckersScore)score).getDifficulty());
    }

    /**
     * Test the functionality unique to SlidingTilesScore
     */
    @Test
    public void testSlidingTilesScore(){
        score = buildSlidingTilesScore();
        assertEquals(3, ((SlidingTilesScore)score).getPuzzleSize());
    }
}