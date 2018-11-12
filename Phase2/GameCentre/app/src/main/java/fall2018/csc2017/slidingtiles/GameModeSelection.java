package fall2018.csc2017.slidingtiles;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;
/**
 * The pop up for selecting the game mode.
 * Adapted on 2018/10/31 from a YouTube Tutorial by Filip Vujovic:
 * https://www.youtube.com/watch?v=fn5OlqQuOCk
 */
public class GameModeSelection extends StartingActivity {
    /**
     * The number of undos allowed.
     */
    public int number_of_undos = 3;
    /**
     * The current number of columns/rows the user selected.
     */
    int cur_board = 4;
    /**
     * The game mode user has selected. 0 = number tiles, 1 is for picture tiles.
     */
    int cur_mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_mode_selection_window);
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);
        int width = d.widthPixels;
        int height = d.heightPixels;
        getWindow().setLayout((int) (width * 0.65),(int) (height *.60));

        configureSpinners();

        addStartButtonListener();
        addUndoSpinner();
    }
    /**
     * Creates and sets up the spinners pertaining to picture selection and game complexity.
     * Adapted on 2018/10/30 from the Android Studio developer guide:
     * https://developer.android.com/guide/topics/ui/controls/spinner
     */
    private void configureSpinners(){
        Spinner[] spinners = {findViewById(R.id.spinner), findViewById(R.id.spinnerPicture)};
        int[] arrays = {R.array.size_array, R.array.number_or_picture_array};
        for (int i = 0; i < spinners.length; i++) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    arrays[i], android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners[i].setAdapter(adapter);
        }
        spinners[0].setSelection(1);
        spinners[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cur_board = i + 3;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinners[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cur_mode = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Creates and sets up the spinner to select the undo limit of the incoming new game..
     */
    private void addUndoSpinner(){
        Spinner undoDropdown = findViewById(R.id.spinnerSetUndo);

        List<Integer> options = new ArrayList<>();
        for (int i= 0; i < 8; i++){
            options.add(i);
        }

        ArrayAdapter<Integer> adapt = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,options);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        undoDropdown.setAdapter(adapt);
        undoDropdown.setSelection(3);
        undoDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                number_of_undos = (Integer) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.NewStartGame);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Board.NUM_ROWS = cur_board;
                Board.NUM_COLS = cur_board;
                Board.NUM_MODE = cur_mode;
                boardManager = new BoardManager(getResources(), number_of_undos);
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                switchToGame();
            }
        });
    }
}
