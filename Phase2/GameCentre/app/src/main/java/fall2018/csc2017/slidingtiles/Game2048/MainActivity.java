package fall2018.csc2017.slidingtiles.Game2048;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import fall2018.csc2017.slidingtiles.GameActivity;
import fall2018.csc2017.slidingtiles.Score;
import fall2018.csc2017.slidingtiles.User;

public class MainActivity extends AppCompatActivity {

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String SCORE = "score";
    private static final String HIGH_SCORE = "high score temp";
    private static final String UNDO_SCORE = "undo score";
    private static final String CAN_UNDO = "can undo";
    private static final String UNDO_GRID = "undo";
    private static final String GAME_STATE = "game state";
    private static final String UNDO_GAME_STATE = "undo game state";
    private final String SCORE_FILENAME_TEMPLATE = "_2048_score_save_file.ser";
    private String game2048SaveFile;
    private Score newHighScore;

    private MainView view;

    private ArrayList<User> users = new ArrayList<>(0);
    private ArrayList<Score> scoreList = new ArrayList<>(5);
    private String currentUser;
    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MainView(this);

        activeUser = getUserFromUsername(getIntent().getStringExtra("activeUser"));
        if (activeUser == null){
            currentUser = "Guest";
        }else{
            currentUser = activeUser.getUsername();
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        view.hasSaveState = settings.getBoolean("save_state", false);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("hasState")) {
                load();
            }
        }
        setContentView(view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //Do nothing
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            view.game.move(2);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            view.game.move(0);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            view.game.move(3);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            view.game.move(1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("hasState", true);
        save();
    }

    protected void onPause() {
        super.onPause();
        save();
    }

    private void save() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        Tile[][] field = view.game.grid.field;
        Tile[][] undoField = view.game.grid.undoField;
        editor.putInt(WIDTH, field.length);
        editor.putInt(HEIGHT, field.length);
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    editor.putInt(xx + " " + yy, field[xx][yy].getValue());
                } else {
                    editor.putInt(xx + " " + yy, 0);
                }

                if (undoField[xx][yy] != null) {
                    editor.putInt(UNDO_GRID + xx + " " + yy, undoField[xx][yy].getValue());
                } else {
                    editor.putInt(UNDO_GRID + xx + " " + yy, 0);
                }
            }
        }
        editor.putLong(SCORE, view.game.score);
        editor.putLong(HIGH_SCORE, view.game.highScore);
        editor.putLong(UNDO_SCORE, view.game.lastScore);
        editor.putBoolean(CAN_UNDO, view.game.canUndo);
        editor.putInt(GAME_STATE, view.game.gameState);
        editor.putInt(UNDO_GAME_STATE, view.game.lastGameState);
        editor.commit();

        if(view.getHasLost() == true){
            saveScoreToFile();
        }
    }

    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        //Stopping all animations
        view.game.aGrid.cancelAnimations();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        for (int xx = 0; xx < view.game.grid.field.length; xx++) {
            for (int yy = 0; yy < view.game.grid.field[0].length; yy++) {
                int value = settings.getInt(xx + " " + yy, -1);
                if (value > 0) {
                    view.game.grid.field[xx][yy] = new Tile(xx, yy, value);
                } else if (value == 0) {
                    view.game.grid.field[xx][yy] = null;
                }

                int undoValue = settings.getInt(UNDO_GRID + xx + " " + yy, -1);
                if (undoValue > 0) {
                    view.game.grid.undoField[xx][yy] = new Tile(xx, yy, undoValue);
                } else if (value == 0) {
                    view.game.grid.undoField[xx][yy] = null;
                }
            }
        }

        view.game.score = settings.getLong(SCORE, view.game.score);
        view.game.highScore = settings.getLong(HIGH_SCORE, view.game.highScore);
        view.game.lastScore = settings.getLong(UNDO_SCORE, view.game.lastScore);
        view.game.canUndo = settings.getBoolean(CAN_UNDO, view.game.canUndo);
        view.game.gameState = settings.getInt(GAME_STATE, view.game.gameState);
        view.game.lastGameState = settings.getInt(UNDO_GAME_STATE, view.game.lastGameState);
    }

    /**
     * Given a username, return the user
     * @param username the username
     * @return the user with the input username
     */
    public User getUserFromUsername(String username){
        if (username != null) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    public void saveScoreToFile(){
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String dateToday = formatter.format(date);
            game2048SaveFile = currentUser + SCORE_FILENAME_TEMPLATE;

            newHighScore = new Score(currentUser, (int) view.game.highScore, dateToday);
            setDefaultValueForArray();
            loadScoreFromFile(game2048SaveFile);
            compareScore(newHighScore);
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(game2048SaveFile, MODE_PRIVATE));
            outputStream.writeObject(scoreList);
            outputStream.close();
            saveLastUser(currentUser);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * get the list of score
     *
     * @param fileName name of the file
     */
    public void loadScoreFromFile(String fileName){
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                scoreList = (ArrayList<Score>)input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("loadScoreFromFile", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("loadScoreFromFile", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("loadScoreFromFile", "File contained unexpected data type: " + e.toString());
        }
    }

    public void setDefaultValueForArray(){
        for (int counter = 0; counter < 5; counter ++){
            Score score = new Score (currentUser,0, "");
            scoreList.add(score);
        }
    }

    /**
     * Save the currently active user.
     *
     * @param username the username of the user.
     */
    public void saveLastUser(String username){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(GameActivity.LAST_USER_FILE, MODE_PRIVATE));
            outputStream.writeObject(username);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * sort all the scores according to its puzzle size and score
     *
     * @param other the other score to add.
     */
    private void compareScore(Score other){
        ArrayList<Score> temp = new ArrayList<>();
        int tempCounter;

        for(int  counter = 0; counter < 5; counter ++){
            temp.add(scoreList.get(counter));
        }
        temp.add(other);
        Collections.sort(temp);
        tempCounter = searchNonZeroIndex(temp);

        for(int counter = 0; counter < 5; counter ++){
            if(tempCounter < temp.size()){
                scoreList.set(counter, temp.get(tempCounter));
                    tempCounter++;
            }else{
                scoreList.set(counter, new Score(currentUser,0, ""));
            }
        }
    }

    /**
     * determine the index in the scoreList where the score is != 0
     * @param temp the scorelist.
     * @return the index where score is 0.
     */
    public int searchNonZeroIndex(ArrayList<Score> temp){
        int index = 0;

        while(index < temp.size()){
            if(temp.get(index).getScore() != 0){
                break;
            }
            index ++;
        }
        System.out.println("index is " + index);
        return index;
    }
}
