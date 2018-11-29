package fall2018.csc2017.slidingtiles.draughts;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import fall2018.csc2017.slidingtiles.R;
/**
 * Adapted on 2018/11/15 from an openly available applet by Greg Tour:
 * https://github.com/gregtour/CheckersAndroid
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
