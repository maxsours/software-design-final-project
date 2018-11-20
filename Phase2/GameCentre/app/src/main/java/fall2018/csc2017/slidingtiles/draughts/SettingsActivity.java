package fall2018.csc2017.slidingtiles.draughts;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import fall2018.csc2017.slidingtiles.R;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
