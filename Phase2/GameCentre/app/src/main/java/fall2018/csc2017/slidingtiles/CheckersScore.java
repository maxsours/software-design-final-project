package fall2018.csc2017.slidingtiles;

public class CheckersScore extends Score {

    String difficulty;

    public CheckersScore(String user, int score, String difficulty, String completionDate){
        super(user, score, completionDate);
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
