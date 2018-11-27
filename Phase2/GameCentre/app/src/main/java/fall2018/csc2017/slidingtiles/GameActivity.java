package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import static fall2018.csc2017.slidingtiles.StartingActivity.SAVE_FILENAME;
import static fall2018.csc2017.slidingtiles.StartingActivity.USER_FILENAME;

/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    private Score score;
    /**
     * every score save file will titled "(username)_slidingtiles_score_save_file.ser"
     */
    private final String SCORE_FILENAME_TEMPLATE = "_slidingtiles_score_save_file.ser";

    /**
     * The name of the file score is saved in.
     */
    public static String SCORE_FILE;

    /**
     * stores the list of score for the user
     *
     * from index 0 to 4, it stores the scores of complexity of 3 x 3
     * from index 5 to 9, it stores the scores of complexity of 4 x 4
     * from index 10 to 14, it stores the scores of complexity of 5 x 5
     */
    private ArrayList<Score> scoreList = new ArrayList<>(15);

    /**
     * keep track of who is the currently active user
     */
    public static final String LAST_USER_FILE = "last_user_file.ser";
    String username;

    /**
     * Active user, null if there is no active user.
     */
    private User activeUser = null;
    private ArrayList<User> users;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromFile(StartingActivity.TEMP_SAVE_FILENAME);
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        addUndoButtonListener();
        loadUsers();
        activeUser = getUserFromUsername(getIntent().getStringExtra("activeUser"));
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board.NUM_COLS);
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / Board.NUM_COLS;
                        columnHeight = displayHeight / Board.NUM_ROWS;

                        display();
                    }
                });
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
    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != Board.NUM_ROWS; row++) {
            for (int col = 0; col != Board.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board.NUM_ROWS;
            int col = nextPos % Board.NUM_COLS;
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
        //auto save the game for every 3 moves
        if (board.getTotalMove() != 0 && board.getTotalMove() % 3 == 0) {
            saveToFile(SAVE_FILENAME);
            saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        }
        // save the score into the file
        if(boardManager.puzzleSolved()){
            if(activeUser == null){
                SCORE_FILE = "Guest" + SCORE_FILENAME_TEMPLATE;
            }else{
                SCORE_FILE = activeUser + SCORE_FILENAME_TEMPLATE;
            }
            setDefaultValueForArray();
            saveScoreToFile(SCORE_FILE);
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        if (fileName.equals(SAVE_FILENAME) && activeUser != null &&
                activeUser.getBoardManager() != null){
            boardManager = activeUser.getBoardManager();
        } else {
            loadFromFile1(fileName);
        }
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile1(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManager) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Gets the list of users from a file.
     */
    private void loadUsers(){
        try {
            InputStream inputStream = this.openFileInput(USER_FILENAME);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                users = (ArrayList<User>)input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the users to the user file.
     */
    private void saveUsers(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(USER_FILENAME, MODE_PRIVATE));
            outputStream.writeObject(users);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName){
        if (fileName.equals(SAVE_FILENAME) && activeUser != null){
            activeUser.setBoardManager(boardManager);
            saveUsers();
        } else {
            saveToFile1(fileName);
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile1(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * save the score into fileName
     *
     * @param fileName the name of the file
     */
    public void saveScoreToFile(String fileName){
        try {
            int puzzleSize =(int) Math.sqrt(boardManager.getBoard().numTiles());
            String username;
            if(activeUser == null){
                username = "Guest";
            }else{
                username = activeUser.getUsername();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String dateToday = formatter.format(date);

            score = new Score(username, boardManager.getBoard().getTotalMove(), puzzleSize, dateToday);
            loadScoreFromFile(fileName);
            compareScore(score,score.getPuzzleSize());
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(scoreList);
            outputStream.close();
            saveLastUser(username);
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

    /**
     * sort all the scores according to its puzzle size and score
     *
     * @param other the other score to add.
     * @param puzzleSize size of the puzzle.
     */
    private void compareScore(Score other, int puzzleSize){
        ArrayList<Score> temp = new ArrayList<>();
        int tempCounter = 0;
        switch(puzzleSize){
            case 3:
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
                        scoreList.set(counter, new Score(username,0,3, ""));
                    }
                }
                break;
            case 4:
                for(int  counter = 5; counter < 10; counter ++){
                    temp.add(scoreList.get(counter));
                }
                temp.add(other);
                Collections.sort(temp);
                tempCounter = searchNonZeroIndex(temp);
                for(int counter = 5; counter < 10; counter ++){
                    if(tempCounter < temp.size()){
                        scoreList.set(counter, temp.get(tempCounter));
                        tempCounter++;
                    }else{
                        scoreList.set(counter, new Score(username,0,4, ""));
                    }
                }
                break;
            case 5:
                for(int  counter = 10; counter < 15; counter ++){
                    temp.add(scoreList.get(counter));
                }
                temp.add(other);
                Collections.sort(temp);
                tempCounter = searchNonZeroIndex(temp);
                for(int counter = 10; counter < 15; counter ++){
                    if(tempCounter < temp.size()){
                        scoreList.set(counter, temp.get(tempCounter));
                        tempCounter++;
                    }else{
                        scoreList.set(counter, new Score(username,0,5, ""));
                    }
                }
                break;
        }
    }

    /**
     * initialize the scoreList if the user is new to the game
     *
     * from index 0 to 4, it stores the scores of complexity of 3 x 3
     * from index 5 to 9, it stores the scores of complexity of 4 x 4
     * from index 10 to 14, it stores the scores of complexity of 5 x 5
     *
     */
    public void setDefaultValueForArray(){
        if(activeUser == null){
            username = "Guest";
        }else{
            username = activeUser.getUsername();
        }
        int defaultPuzzleSize = 3;
        for (int counter = 0; counter < 15; counter ++){
            if(counter < 5){
                defaultPuzzleSize = 3;
            }else if(counter >= 5 && counter < 10){
                defaultPuzzleSize = 4;
            }else if (counter >=10 && counter < 15){
                defaultPuzzleSize = 5;
            }
            Score score = new Score (username,0,defaultPuzzleSize, "");
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
                    this.openFileOutput(LAST_USER_FILE, MODE_PRIVATE));
            outputStream.writeObject(username);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Sets up the undo button.
     */
    private void addUndoButtonListener(){
        Button UndoButton = findViewById(R.id.UndoButton);
        UndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager.getBoard().popUndoMove();
                if (boardManager.getBoard().getNumUndos() != 0)
                {
                    Toast.makeText(GameActivity.this,  "You have " +  boardManager.getBoard().getNumUndos() + " undo(s) remaining.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameActivity.this,  "Undos depleted.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * determine the index in the scoreList where the score is != 0
     * @param temp the scorelist.
     * @return the index where score is 0.
     */
    public int searchNonZeroIndex(ArrayList<Score> temp){
        int index = 0;
        while(true){
            if(temp.get(index).getScore() == 0){
                index++;
            }else{
                break;
            }
        }
        return index;
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}
