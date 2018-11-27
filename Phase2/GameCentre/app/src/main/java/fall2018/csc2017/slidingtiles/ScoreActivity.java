package fall2018.csc2017.slidingtiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * the high score activity for each complexity of the puzzle
 */
public class ScoreActivity extends AppCompatActivity {

    /**
     * the detailed title header when there are scores to be display depending which complexity score you want to see
     */
    private TextView mTextMessage;
    /**
     * used to display the scores
     */
    private TextView scoreTextMessage ;
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
    private Boolean scoreFileExist;

    /**
     * display the correct score accordingly when user pressed on the BottomNavigationView icon
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            loadLastUserFromFile(GameActivity.LAST_USER_FILE);
            String path;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    path = lastUser + "_slidingtiles_score_save_file.ser";
                    scoreFileExist = false;
                    loadScoreFromFile(path);
                    mTextMessage.setTextSize(15);
                    if(!scoreFileExist){
                        scoreTextMessage.setText("No record found for Sliding Tile");
                    }else{
                        StringBuilder text = new StringBuilder();

                        text.append(getResources().getString(R.string.size_3_title));
                        text.append("\n");
                        text.append(appendScoreDetails(0,5));
                        text.append("\n");

                        text.append(getResources().getString(R.string.size_4_title));
                        text.append("\n");
                        text.append(appendScoreDetails(5,10));
                        text.append("\n");

                        text.append(getResources().getString(R.string.size_5_title));
                        text.append("\n");
                        text.append(appendScoreDetails(10,15));
                        text.append("\n");

                        scoreTextMessage.setText(text);
                    }
                    return true;

                case R.id.navigation_dashboard:
                    path = lastUser + "_checkers_score_save_file.ser";
                    scoreFileExist = false;
                    loadScoreFromFile(path);
                    mTextMessage.setTextSize(15);
                    if(!scoreFileExist){
                        scoreTextMessage.setText("No record found for Checkers");
                    }else{
                        StringBuilder text = new StringBuilder();
                        text.append(appendCheckerScore(0,5));
                        text.append("\n");
                        text.append(appendCheckerScore(5,10));
                        text.append("\n");
                        text.append(appendCheckerScore(10,15));
                        text.append("\n");
                        text.append(appendCheckerScore(15,20));

                        scoreTextMessage.setText(text);
                    }
                    return true;

                case R.id.navigation_notifications:
                    path = lastUser + "_2048_score_save_file.ser";
                    scoreFileExist = false;
                    loadScoreFromFile(path);
                    mTextMessage.setTextSize(15);
                    if(!scoreFileExist){
                        scoreTextMessage.setText("No record found for 2048");
                    }else{
                        StringBuilder text = append2048Score();
                        scoreTextMessage.setText(text);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        mTextMessage = findViewById(R.id.message);
        scoreTextMessage = findViewById(R.id.textView);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
     * Append the scores in proper format before displaying them on the activity
     *
     * @param start the beginning of the score section we want to append score to.
     * @param end the end of the score section we want to append score to.
     * @return the scores that need to be displayed on the app
     */
    public StringBuilder appendScoreDetails(int start, int end){
        StringBuilder scoreDetails = new StringBuilder();
        for(int counter = start; counter < end; counter ++){
            if(scoreList.get(counter).getScore() != 0){
                scoreDetails.append(scoreList.get(counter).getUser());
                scoreDetails.append("\t\t\t\t\t\t");
                scoreDetails.append(Integer.toString(scoreList.get(counter).getScore()));
                scoreDetails.append(" moves");
                scoreDetails.append("\t\t\t\t\t\t");
                scoreDetails.append(scoreList.get(counter).getDate());
                scoreDetails.append("\n");
            }
        }
        return scoreDetails;
    }

    public StringBuilder appendCheckerScore(int start, int end){
        StringBuilder scoreDetails = new StringBuilder();
        for(int counter = start; counter < end; counter ++){
            if(scoreList.get(counter).getScore() != 0){
                scoreDetails.append(scoreList.get(counter).getUser());
                scoreDetails.append("\t\t\t\t");
                scoreDetails.append(Integer.toString(scoreList.get(counter).getScore()));
                scoreDetails.append("\t\t\t\t");
                scoreDetails.append((scoreList.get(counter).getDifficulty()));
                scoreDetails.append("\t\t\t\t");
                scoreDetails.append(scoreList.get(counter).getDate());
                scoreDetails.append("\n");
            }
        }
        System.out.println(scoreDetails);
        return scoreDetails;
    }

    public StringBuilder append2048Score(){
        StringBuilder scoreDetails = new StringBuilder();
        for(Score score : scoreList){
            if(score.getScore() != 0){
                scoreDetails.append(score.getUser());
                scoreDetails.append("\t\t\t\t");

                scoreDetails.append(Integer.toString(score.getScore()));
                scoreDetails.append("\t\t\t\t");

                scoreDetails.append(score.getDate());
                scoreDetails.append("\n");
            }
        }
        return scoreDetails;
    }

}