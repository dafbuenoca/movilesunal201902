package co.edu.unal.androidtictactoe2;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import java.util.prefs.Preferences;

import static co.edu.unal.androidtictactoe2.R.xml.preferences;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(preferences);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final ListPreference difficultyLevelPrefs = (ListPreference) findPreference("difficulty_level");

        String difficulty = prefs.getString("difficulty_level", getResources().getString(R.string.difficulty_expert));

        difficultyLevelPrefs.setSummary((CharSequence) difficulty);

        difficultyLevelPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                difficultyLevelPrefs.setSummary((CharSequence) newValue);

                //since we are handling the pref, we must save it
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("difficulty_level", newValue.toString());
                ed.commit();
                return true;
            }
        });

        final EditTextPreference victoryMessagePref = (EditTextPreference) findPreference("victory_message");
        String victoryMessage = prefs.getString("victory_message", getResources().getString(R.string.result_human_wins));
        victoryMessagePref.setSummary((CharSequence) victoryMessage);

        victoryMessagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                victoryMessagePref.setSummary((CharSequence) newValue);

                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("victory_message", newValue.toString());
                ed.commit();
                return true;
            }
        });

    }


}
