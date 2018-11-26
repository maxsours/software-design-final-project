package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * A user's score
 */
public class Score implements Serializable, Comparable<Score> {
    private String user;
    private int score;
    private int puzzleSize;
    private String date;
    private String difficulty;

    public Score(String user, int score, int puzzleSize, String completionDate) {
        this.user = user;
        this.score = score;
        this.puzzleSize = puzzleSize;
        this.date = completionDate;
    }

    public Score(String user, int score, String difficulty, String completionDate) {
        this.user = user;
        this.score = score;
        this.difficulty = difficulty;
        this.date = completionDate;
    }

    public int getScore() {
        return score;
    }

    public String getUser() {
        return user;
    }

    public int getPuzzleSize() {
        return puzzleSize;
    }

    public String getDate() {
        return this.date;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public int compareTo(Score other) {
        if (this.score < other.getScore()) {
            return -1;
        } else if (this.score > other.getScore()) {
            return 1;
        } else {
            return 0;
        }
    }
}
