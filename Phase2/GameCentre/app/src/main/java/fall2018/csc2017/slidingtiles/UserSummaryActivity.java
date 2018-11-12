package fall2018.csc2017.slidingtiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * activity that summarize the best score of each games that the user played
 */
public class UserSummaryActivity extends AppCompatActivity {
    /**
     * get the information from the "content" variable below and display them to the app
     */
    private TextView summaryTextMessage ;
    /**
     * used to store the scores from the file
     */
    private ArrayList<Score> scoreList = new ArrayList<>(15);
    /**
     * to check who is the current user, so that the score can be displayed correctly
     */
    private String lastUser ;
    /**
     * to indicate whether the score file is available
     */
    private Boolean scoreFileExist = false;
    /**
     * content used to store all the information about all the different games and respective best score for each game
     */
    private StringBuilder content = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_summary);

        //determine the current user and his/her score, then summarize and display the info correctly
        summaryTextMessage =  findViewById(R.id.textView7);
        loadLastUserFromFile(GameActivity.LAST_USER_FILE);
        String path = lastUser + "_score_save_file.ser";
        loadScoreFromFile(path);

        if(!scoreFileExist){
            content.append("No record found for Sliding Puzzle");
        }else{
            content.append("Best Score for Sliding Puzzle is ");
            content.append(showBestScore());
        }
        summaryTextMessage.setText(content);
    }

    /**
     * Load the score into the scoreListArray
     *
     * @param fileName file name where all the previous scores are stored
     */
    public void loadScoreFromFile(String fileName){
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                this.scoreList = (ArrayList<Score>)input.readObject();
                inputStream.close();
                scoreFileExist = true;
            }
        } catch (FileNotFoundException e) {
            Log.e("Score activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Score activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("Score activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * get the current user using the app
     *
     * @param fileName file name of the file where the last user/ current user is stored
     */
    public void loadLastUserFromFile(String fileName){
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                this.lastUser = (String)input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("Score activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Score activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("Score activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * show the best score for the game
     *
     * @return the best score among all the scores in the scoreList
     */
    public int showBestScore(){
        int bestScore;

        ArrayList<Score> compareArray = new ArrayList<>(15);
        for(Score element : scoreList){
            if(element.getScore() != 0){
                compareArray.add(element);
            }
        }
        Collections.sort(compareArray);
        bestScore = compareArray.get(0).getScore();
        return bestScore;
    }
}
