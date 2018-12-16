package de.htwg.mobilecomputing.aid.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.view.View;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.util.Map;

import de.htwg.mobilecomputing.aid.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.settings));
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getPreferenceManager().getSharedPreferences();

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        for(Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            updatePreferenceSummary(findPreference(entry.getKey()));
        }

        //hide navigation bar
        getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreferenceSummary(findPreference(key));
    }

    private void updatePreferenceSummary(Preference preference) {
        if(preference != null && preference instanceof EditTextPreference) {
            String newValue = ((EditTextPreference) preference).getText();
            if (preference.getKey().toLowerCase().contains("password"))
                newValue = newValue.replaceAll(".", "*");
            preference.setSummary(newValue);
        }
    }
}