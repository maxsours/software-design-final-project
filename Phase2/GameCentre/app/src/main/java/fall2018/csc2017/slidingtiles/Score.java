package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * A user's score
 */
public class Score implements Serializable, Comparable<Score> {
    private String user;
    private int score;
    private String date;

    //constructor used by 2048
    public Score(String user, int score, String completionDate){
        this.user = user;
        this.score = score;
        this.date = completionDate;
    }

    public int getScore() {
        return score;
    }

    public String getUser(){
        return user;
    }

    public String getDate(){
        return this.date;
    }


    public int compareTo(Score other) {
        if (this.score < other.getScore()) {
            return -1;
        }else if(this.score > other.getScore()){
            return 1;
        }else{
            return 0;
        }
    }

}
