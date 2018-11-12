package fall2018.csc2017.slidingtiles;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "save_file.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";

    /**
     * A file to store users.
     */
    public static final String USER_FILENAME = "user_file.ser";

    /**
     * The board manager.
     */
    public BoardManager boardManager;

    /**
     * List of users.
     */
    private ArrayList<User> users = new ArrayList<>(0);

    /**
     * Username of active user. Not the user itself because it can't get passed through an intent.
     */
    private User activeUser = null;
    /**
     * The number of undos allowed.
     */
    public int number_of_undos = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardManager = new BoardManager(this.getResources(), number_of_undos);
        saveToFile(TEMP_SAVE_FILENAME);

        setContentView(R.layout.activity_starting_);
        addLoadButtonListener();
        addSaveButtonListener();
        loadUsers();
        addLoginButtonListener();
        addLogoutButtonListener();
        addCreateAccountListener();
        addHighScoreButtonListener();
        addNewGameButton();
        addUserSummaryButtonListener();
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
     * Activate the login button
     */
    private void addLoginButtonListener() {
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText)findViewById(R.id.usernameText)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordText)).getText().toString();
                User user = null;
                for (User u : users){
                    if (u.getUsername().equals(username)){
                        user = u;
                        saveLastUser(user.getUsername());
                    }
                }
                if (user == null){
                    makeToastWrongUsernameText();
                } else if (user.getPassword().equals(password)) {
                    login(user);
                } else {
                    makeToastWrongPasswordText();
                }
            }
        });
    }

    /**
     * activate the High Score button
     */
    private void addHighScoreButtonListener(){
        Button highScoreButton = findViewById(R.id.HighScoreButton);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StartingActivity.this, ScoreActivity.class);
                StartingActivity.this.startActivity(myIntent);
            }
        });
    }

    /**
     * activate the User Summary button
     */
    private void addUserSummaryButtonListener(){
        Button userSummaryButton = findViewById(R.id.SummaryButton);
        userSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StartingActivity.this, UserSummaryActivity.class);
                StartingActivity.this.startActivity(myIntent);
            }
        });
    }

    /**
     * Logs the user in.
     *
     * @param user the user being logged in
     */
    private void login(User user){
        TextView userGreeting = findViewById(R.id.userGreeting);
        userGreeting.setText("Welcome, " + user + "!");
        activeUser = user;
        findViewById(R.id.usernameText).setVisibility(View.INVISIBLE);
        findViewById(R.id.passwordText).setVisibility(View.INVISIBLE);
        findViewById(R.id.createAccountButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
    }

    /**
     * Logs the user out.
     */
    private void logOut(){
        TextView userGreeting = findViewById(R.id.userGreeting);
        userGreeting.setText("");
        activeUser = null;
        findViewById(R.id.usernameText).setVisibility(View.VISIBLE);
        findViewById(R.id.passwordText).setVisibility(View.VISIBLE);
        findViewById(R.id.createAccountButton).setVisibility(View.VISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
        findViewById(R.id.logoutButton).setVisibility(View.INVISIBLE);
    }

    /**
     * Activates the logout button
     */
    private void addLogoutButtonListener(){
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
                saveLastUser("Guest");
            }
        });
    }

    /**
     * Activate the create account button
     */
    private void addCreateAccountListener(){
        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText)findViewById(R.id.usernameText)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordText)).getText().toString();
                User user = new User(username, password);
                boolean userCanBeAdded = true;
                for (User u : users){
                    if (user.equals(u)){
                        makeToastUserAlreadyExistsText(u);
                        userCanBeAdded = false;
                        break;
                    }
                }
                if (userCanBeAdded) {
                    users.add(user);
                    makeToastUserAddedText();
                    saveUsers();
                }
            }
        });
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
     * Display that the new user was created correctly.
     */
    private void makeToastUserAddedText() {
        Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the new user already exists
     *
     * @param user the user that already exists
     */
    private void makeToastUserAlreadyExistsText(User user) {
        Toast.makeText(this, user + " Already Exists", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the username was not entered correctly.
     */
    private void makeToastWrongUsernameText() {
        Toast.makeText(this, "Incorrect Username", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the password was not entered correctly.
     */
    private void makeToastWrongPasswordText() {
        Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
    }


    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastLoadedText();
                switchToGame();
            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
            }
            });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    public void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
        if (activeUser != null) {
            tmp.putExtra("activeUser", activeUser.getUsername());
        }
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        if (fileName.equals(SAVE_FILENAME) && activeUser != null && activeUser.getBoardManager() != null){
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
            int previousPuzzleSize = (int)Math.sqrt(boardManager.getBoard().numTiles());
            System.out.println("saved previousPuzzleSize is " + previousPuzzleSize);
            Board.NUM_COLS = previousPuzzleSize;
            Board.NUM_ROWS = previousPuzzleSize;
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    /**
     * Activates NewGameButton
     */
    private void addNewGameButton(){
        Button NewGameButton = findViewById(R.id.NewGameButton);
        NewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(StartingActivity.this, GameModeSelection.class);
                StartingActivity.this.startActivity(Intent);
            }
        });
        if(activeUser == null){
            saveLastUser("Guest");
        }
    }

    /**
     * save the user when the login button
     *
     * @param username
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

}
