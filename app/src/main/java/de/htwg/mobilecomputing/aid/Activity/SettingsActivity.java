package de.htwg.mobilecomputing.aid.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.htwg.mobilecomputing.aid.Fragment.SettingsFragment;
import de.htwg.mobilecomputing.aid.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
