package fall2018.csc2017.slidingtiles.draughts;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import fall2018.csc2017.slidingtiles.CheckersScore;
import fall2018.csc2017.slidingtiles.GameActivity;
import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.Score;
import fall2018.csc2017.slidingtiles.User;
import fall2018.csc2017.slidingtiles.draughts.game.CheckerBoard;
import fall2018.csc2017.slidingtiles.draughts.game.CheckersGame;
import fall2018.csc2017.slidingtiles.draughts.game.Move;
import fall2018.csc2017.slidingtiles.draughts.game.Piece;
import fall2018.csc2017.slidingtiles.draughts.game.Position;

/**
 * The checkers game's activity.
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class MyCheckersActivity extends AppCompatActivity {
    /**
     * The Checkers Game being played.
     */
    private CheckersGame gamelogic;

    /**
     * The Layout of the checkers game.
     */
    private CheckersLayout checkersView;

    /**
     * The current status of the checkers game being played.
     */
    public TextView statusText;

    /**
     * The difficulty of the checkers game being played.
     */
    private String prefDifficulty;

    /**
     * Whether any move is permitted in the game currently being played.
     */
    private boolean prefAllowAnyMove;

    /**
     * The list of checkers game scores.
     */
    private ArrayList <Score> scoreList = new ArrayList<>(20);

    /**
     * A checker game's score.
     */
    private Score score;

    /**
     * The current user; the file containing the User's score.
     */
    private String currentUser, checkersScoreFile;

    /**
     * Represents whether the user has made an initial move.
     */
    private int stepTaken = 0;

    /**
     * The starting time for a checkers game.
     */
    private double startTime;

    /**
     * Whether the currently played checkers game has been won.
     */
    private boolean winStatus = false;

    /**
     * A string representation of the current game difficulty.
     */
    private static final String DIFFICULTY = "pref_difficulty";

    /**
     * A string representation of whether any move is permitted in the currently played game.
     */
    private static final String ANY_MOVE = "pref_any_move";

    /**
     * The checkers game score save file.
     */
    private final String SCORE_FILENAME_TEMPLATE = "_checkers_score_save_file.ser";

    /**
     * The checkers game save file.
     */
    private final String CHECKERS_SAVE_FILE = "_checkers_save_file.ser";

    @Override
    protected void onCreate(Bundle saved)
    {
        super.onCreate(saved);
        loadLastUser();

        createGameBoard();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferencesChangeListener);
        prefDifficulty = sharedPreferences.getString(DIFFICULTY, "Easy");
        prefAllowAnyMove = sharedPreferences.getBoolean(ANY_MOVE, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepTurn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_menu_icon:
                Intent preferencesIntent = new Intent(this, SettingsActivity.class);
                startActivity(preferencesIntent);
                break;
            case R.id.new_menu_icon:
                Toast.makeText(this,
                        "New Game",
                        Toast.LENGTH_SHORT).show();
                restart();
                break;
            case R.id.about_menu_icon:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("CHECKERS")
                        .setCancelable(false)
                        .setPositiveButton("Okay", null)
                        .create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a new checkers board.
     */
    private void createGameBoard()
    {
        loadFromFile(currentUser + CHECKERS_SAVE_FILE);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        TextView topText = new TextView(this);
        topText.setText("Play Checkers");

        statusText = new TextView(this);
        statusText.setText("status");

        checkersView = new CheckersLayout(gamelogic, this);
        checkersView.refresh();

        rootLayout.addView(topText);
        rootLayout.addView(checkersView);
        rootLayout.addView(statusText);

        setContentView(rootLayout);
    }

    /**
     * Stops the automated opponent algorithm from continuing handling tasks.
     */
    public void clearComputerTask() {
        if (computerTask != null) {
            if (computerTask.getStatus() != AsyncTask.Status.FINISHED)
            {
                computerTask.cancel(true);
            }
            computerTask = null;
        }
    }

    /**
     * Restarts the checkers game.
     */
    private void restart() {
        File file = getFileStreamPath(currentUser+ CHECKERS_SAVE_FILE);
        file.delete();
        clearComputerTask();
        gamelogic.restart();
        checkersView.refresh();
        prepTurn();
    }

    /**
     * The currently selected piece in the game.
     */
    Piece selectedPiece;

    /**
     * The selected position to be played upon in the game.
     */
    Position selectedPosition;

    /**
     * Pieces that can currently be selected to be played within the checkers game.
     */
    Piece selectablePieces[];

    /**
     * List of possible moves to be played within the checkers game.
     */
    Position moveOptions[];

    /**
     * Whether it is the computers turn to play.
     */
    ComputerTurn computerTask = null;

    /**
     * Prepares a human or computer turn.
     */
    public void prepTurn() {
        CheckerBoard board = gamelogic.getBoard();

        selectedPiece = null;
        selectedPosition = null;
        selectablePieces = null;
        moveOptions = null;

        clearComputerTask();

        int turn = gamelogic.whoseTurn();

        if (turn == CheckersGame.RED) {
            statusText.setText("Red's (computer's) turn. Difficulty: "+prefDifficulty);

            computerTask = new ComputerTurn(this, gamelogic, prefDifficulty, prefAllowAnyMove);
            computerTask.execute();

        } else if (turn == CheckersGame.BLACK) {
            statusText.setText("Black's (player's) turn.");
            ArrayList<Piece> selectablePieces = new ArrayList<>();
            Move moves[] = gamelogic.getMoves();

            for (Move move : moves) {
                Piece newPiece = board.getPiece(move.start());
                if (!selectablePieces.contains(newPiece)) {
                    selectablePieces.add(newPiece);
                }
            }

            this.selectablePieces = selectablePieces.toArray(
                    new Piece[selectablePieces.size()]
            );

            if (selectablePieces.size() == 0) {
                statusText.setText("You lost!");
            }

            stepTaken++;
            if(stepTaken == 1){
                startTime = System.currentTimeMillis();
            }
        }

        checkersView.refresh();
    }

    /**
     * Whether a piece has been selected
     *
     * @param piece a piece that potentially was selected
     * @return whether a piece has been selected
     */
    public boolean isSelected(Piece piece) {
        return (piece != null && piece == selectedPiece);
    }

    /**
     * Returns whether the selected piece to move is at the edge of the board.
     *
     * @param checkPosition checks whether the selected piece to move is at the edge of the board.
     *
     * @return whether the selected piece to move is at the edge of the board.
     */
    public boolean isOption(Position checkPosition) {
        if (moveOptions == null) {
            return false;
        }
        for (Position position : moveOptions) {
            if (position.equals(checkPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines all possilble moves for the selected piece
     *
     * @param piece the selected piece
     * @param location the location of the selected checker piece
     */
    public void selectPiece(Piece piece, Position location)
    {
        selectedPiece = null;
        selectedPosition = null;
        moveOptions = null;

        if (piece != null && selectablePieces != null
                && piece.getColor() == gamelogic.whoseTurn())
        {
            boolean isSelectable = false;
            for (Piece selectablePiece : selectablePieces) {
                if (selectablePiece == piece) {
                    isSelectable = true;
                }
            }

            if (isSelectable) {
                selectedPiece = piece;
                selectedPosition = location;

                ArrayList<Position> moveOptionsArr = new ArrayList<>();

                Move allMoves[] = gamelogic.getMoves();

                for (Move checkMove : allMoves) {
                    Position start = checkMove.start();
                    Position end = checkMove.end();

                    if (start.equals(location)) {
                        if (!moveOptionsArr.contains(end)) {
                            moveOptionsArr.add(end);
                        }
                    }
                }

                moveOptions = moveOptionsArr.toArray(new Position[moveOptionsArr.size()]);
            }
        }

        checkersView.refresh();
    }

    /**
     * Makes the move the User has chosen.
     *
     * @param destination the position the piece is intended to go to.
     */
    public void makeMove(Position destination)
    {

        Move move = gamelogic.getLongestMove(selectedPosition, destination);
        if (move != null) {
            gamelogic.makeMove(move);
            prepTurn();
            saveToFile(currentUser + CHECKERS_SAVE_FILE);
        }
    }

    /**
     * Responds to when a player clicks on an area on the board.
     *
     * @param x the row position of the clicked area
     * @param y the column position of the clicked area
     */
    public void onClick(int x, int y) {
        if (gamelogic.whoseTurn() != CheckersGame.BLACK) {
            return;
        }

        Position location = new Position(x, y);
        Piece targetPiece = gamelogic.getBoard().getPiece(x, y);
        if (selectedPiece != null && selectedPosition != null && targetPiece == null) {
            makeMove(location);
        }
        else
        {
            selectPiece(targetPiece, location);
        }
    }

    /**
     * Updates the checker game's chosen preferences.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    prefDifficulty = sharedPreferences.getString(DIFFICULTY, "Easy");
                    prefAllowAnyMove = sharedPreferences.getBoolean(ANY_MOVE, true);

                    gamelogic.setAnyMove(prefAllowAnyMove);

                    prepTurn();
                }
            };

    /**
     * Saves the checker game's final score into the save file.
     *
     * @param finalScore the calculated final score after the player wins.
     * @param difficulty the difficulty of AI the player decided to play with.
     */
    public void saveScoreToFile( int finalScore, String difficulty){
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String dateToday = formatter.format(date);
            checkersScoreFile = currentUser + SCORE_FILENAME_TEMPLATE;
            score = new CheckersScore(currentUser, finalScore, difficulty, dateToday);
            setDefaultValueForArray();
            loadScoreFromFile(checkersScoreFile);
            compareScore(score, difficulty, currentUser);
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(checkersScoreFile, MODE_PRIVATE));
            outputStream.writeObject(scoreList);
            outputStream.close();
            saveLastUser(currentUser);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Opens the list of checkers scores to be modified or read in within a file.
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
     * Calculates the final score of the checkers game
     * (Note to Reader: It produces a score by taking the ceiling function of the following formula:
     * finalScore = (int) Math.ceil((100000 * difficultyScore)/timeTaken);)
     *
     *
     * @param difficulty of the game played
     * @param endTime when does the player win the game
     * @param startTime when does the player made the first move
     * @return final score obtained by the player
     */
    private int calculateFinalScore(String difficulty, double endTime, double startTime){
        int finalScore, difficultyScore;
        double timeTaken;

        if(difficulty.equals("Easy")){
            difficultyScore = 1;
        }else if (difficulty.equals("Medium")){
            difficultyScore = 2;
        }else if (difficulty.equals("Hard")){
            difficultyScore = 3;
        }else{
            difficultyScore = 4;
        }

        timeTaken = ( endTime - startTime)/600000;
        finalScore = (int) Math.ceil((100000 * difficultyScore)/timeTaken);

        return finalScore;
    }

    /**
     * Saves the currently active user.
     *
     * @param username the username of the user.
     */
    public void saveLastUser(String username){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(GameActivity.LAST_USER_FILE, MODE_PRIVATE));
            outputStream.writeObject(username);
            outputStream.close();
            System.out.println("last User saved");
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Sorts all the checkers game scores according to their difficulty and score.
     *
     * @param other the score to add.
     */
    private void compareScore(Score other, String difficulty, String username){
        ArrayList<Score> temp = new ArrayList<>();
        int tempCounter;
        int pointer;
        switch(difficulty){
            case "Easy":
                for(int  counter = 0; counter < 5; counter ++){
                    temp.add(scoreList.get(counter));
                }
                temp.add(other);
                Collections.sort(temp);
                tempCounter = searchNonZeroIndex(temp);

                pointer = temp.size() -1 ;
                for(int counter = 0; counter < 5; counter ++){
                    if(tempCounter <= pointer){
                        scoreList.set(counter, temp.get(pointer));
                        pointer--;
                    }else{
                        scoreList.set(counter, new CheckersScore(username,0,"Easy", ""));
                    }
                }
                break;

            case "Medium":
                for(int  counter = 5; counter < 10; counter ++){
                    temp.add(scoreList.get(counter));
                }
                temp.add(other);
                Collections.sort(temp);
                tempCounter = searchNonZeroIndex(temp);
                pointer = temp.size() -1 ;
                for(int counter = 0; counter < 5; counter ++){
                    if(tempCounter <= pointer){
                        scoreList.set(counter, temp.get(pointer));
                        pointer--;
                    }else{
                        scoreList.set(counter, new CheckersScore(username,0,"Medium", ""));
                    }
                }
                break;

            case "Hard":
                for(int  counter = 10; counter < 15; counter ++){
                    temp.add(scoreList.get(counter));
                }
                temp.add(other);
                Collections.sort(temp);
                tempCounter = searchNonZeroIndex(temp);
                pointer = temp.size() -1 ;
                for(int counter = 0; counter < 5; counter ++){
                    if(tempCounter <= pointer){
                        scoreList.set(counter, temp.get(pointer));
                        pointer--;
                    }else{
                        scoreList.set(counter, new CheckersScore(username,0,"Hard", ""));
                    }
                }
                break;

            case "Very Hard":
                for(int  counter = 15; counter < 20; counter ++){
                    temp.add(scoreList.get(counter));
                }
                temp.add(other);
                Collections.sort(temp);
                tempCounter = searchNonZeroIndex(temp);
                pointer = temp.size() -1 ;
                for(int counter = 0; counter < 5; counter ++){
                    if(tempCounter <= pointer){
                        scoreList.set(counter, temp.get(pointer));
                        pointer--;
                    }else{
                        scoreList.set(counter, new CheckersScore(username,0,"Very Hard", ""));
                    }
                }
                break;
        }
    }

    /**
     * Determines the index in the scoreList where the score is != 0
     *
     * @param temp the list of scores.
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
        return index;
    }

    /**
     * Initializes the scoreList, if the user is new to the game.
     *
     * from index 0 to 4, it stores the scores of "easy"
     * from index 5 to 9, it stores the scores of "medium"
     * from index 10 to 14, it stores the scores of "hard"
     * from index 15 to 19, it stores the scores of "very hard"
     *
     */
    public void setDefaultValueForArray(){

        String difficulty;
        for (int counter = 0; counter < 20; counter ++){
            if(counter < 5){
                difficulty = "Easy";
            }else if(counter >= 5 && counter < 10){
                difficulty = "Medium";
            }else if (counter >=10 && counter < 15){
                difficulty = "Hard";
            }else{
                difficulty = "Very Hard";
            }
            Score score = new CheckersScore (currentUser,0, difficulty, "");
            scoreList.add(score);
        }
    }


    /**
     * After the game is won by the plays, this method saves the user, score and the
     * game progress.
     *
     * @param status whether the player wins the game
     * @param difficulty the difficulty chosen by the player
     */
    public void setWinStatus(boolean status, String difficulty){
        winStatus = status;
        if(winStatus == true){
            System.out.println("Inside the method");
            double endTime = System.currentTimeMillis();
            int finalScore = calculateFinalScore(difficulty, endTime, startTime);

            saveLastUser(currentUser);
            saveScoreToFile(finalScore,difficulty );
            gamelogic = new CheckersGame(prefAllowAnyMove);
            saveToFile(currentUser + CHECKERS_SAVE_FILE);

            File file = getFileStreamPath(currentUser+ CHECKERS_SAVE_FILE);
            file.delete();
        }
    }

    /**
     * Save the CheckersGame object to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(gamelogic);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Loads the CheckersGame object from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                gamelogic = (CheckersGame) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            gamelogic = new CheckersGame(prefAllowAnyMove);
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * load the current active user
     */
    private void loadLastUser(){
        try {
            InputStream inputStream = this.openFileInput(GameActivity.LAST_USER_FILE);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                 currentUser = (String) input.readObject();
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

}
