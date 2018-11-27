package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScoreTest {

    Score score;

    /**
     * Build a score for testing purposes
     * @return a score that can be used for testing
     */
    private Score buildScore(){
        return new Score("alpha", 42, 3, "March 14");
    }

    /**
     * Test whether compareTo works.
     */
    @Test
    public void testCompareTo() {
        score = buildScore();
        Score anotherScore = buildScore();
        assertEquals(0, score.compareTo(anotherScore));
        anotherScore = new Score(score.getUser(), score.getScore() - 1, score.getPuzzleSize(), score.getDate());
        assertEquals(1, score.compareTo(anotherScore));
        anotherScore = new Score(score.getUser(), score.getScore() + 1, score.getPuzzleSize(), score.getDate());
        assertEquals(-1, score.compareTo(anotherScore));
    }

    /**
     * Test whether toString works.
     */
    @Test
    public void testToString() {
        score = buildScore();
        assertEquals("3\talpha\t42", score.toString());
    }
}