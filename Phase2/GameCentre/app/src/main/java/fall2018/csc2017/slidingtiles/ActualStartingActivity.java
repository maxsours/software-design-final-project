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

public class ActualStartingActivity extends AppCompatActivity {

    /**
     * A file to store users.
     */
    public static final String USER_FILENAME = "user_file.ser";

    /**
     * List of users.
     */
    private ArrayList<User> users = new ArrayList<>(0);

    /**
     * Username of active user. Not the user itself because it can't get passed through an intent.
     */
    private User activeUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_starting);

        loadUsers();
        addLoginButtonListener();
        addLogoutButtonListener();
        addCreateAccountListener();
        addSlidingTilesButtonListener();
        addDraughtsButtonListener();
        add2048sButtonListener();
        addHighScoreButtonListener();
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
     * Activate sliding tiles button.
     */
    private void addSlidingTilesButtonListener(){
        Button slidingTilesButton = findViewById(R.id.slidingTiles);
        slidingTilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSlidingTiles();
                if(activeUser == null){
                    saveLastUser("Guest");
                }
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
     * Save the username to a file when the login button is pressed
     *
     * @param username username of the user
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
     * activate the High Score button
     */
    private void addHighScoreButtonListener(){
        Button highScoreButton = findViewById(R.id.HighScoreButton);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ActualStartingActivity.this, ScoreActivity.class);
                ActualStartingActivity.this.startActivity(myIntent);
                if(activeUser == null){
                    saveLastUser("Guest");
                }
            }
        });
    }

    /**
     * Transition to sliding tiles game.
     */
    private void switchToSlidingTiles(){
        Intent intent = new Intent(this, StartingActivity.class);
        if (activeUser != null) {
            intent.putExtra("activeUser", activeUser.getUsername());
        }
        startActivity(intent);
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
     * Button for draughts game on Game Centre.
     */
    private Button draughtsbutton;

    /**
     * Activate draughts button.
     */
    private void addDraughtsButtonListener(){
        draughtsbutton = findViewById(R.id.draughtsbutton);

        draughtsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDraughts();
                if(activeUser == null){
                    saveLastUser("Guest");
                }
            }
        });
    }

    /**
     * Transition to draughts game.
     */
    public void openDraughts () {
        Intent intent = new Intent(this, fall2018.csc2017.slidingtiles.
                draughts.MyCheckersActivity.class);
        startActivity(intent);
    }

    private Button button2048;

    /**
     * Activate draughts button.
     */
    private void add2048sButtonListener(){
        button2048 = findViewById(R.id.button2048);

        button2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open2048();
                if(activeUser == null){
                    saveLastUser("Guest");
                }
            }
        });
    }

    /**
     * Transition to draughts game.
     */
    public void open2048 () {
        Intent intent = new Intent(this, fall2018.csc2017.slidingtiles.
                Game2048.MainActivity.class);
        startActivity(intent);
    }

    public User getActiveUser(){
        return this.activeUser;
    }

}

