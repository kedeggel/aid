package de.htwg.mobilecomputing.aid.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import de.htwg.mobilecomputing.aid.Fragment.CameraFragment;
import de.htwg.mobilecomputing.aid.Fragment.LibraryFragment;
import de.htwg.mobilecomputing.aid.Fragment.SettingsFragment;
import de.htwg.mobilecomputing.aid.R;

public class MainActivity extends AppCompatActivity {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final Fragment camera = CameraFragment.newInstance();
    private final Fragment library = LibraryFragment.newInstance();
    private final Fragment settings = new SettingsFragment();

    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        if(savedInstanceState == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, camera).commit();
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportActionBar().show();
        navigation.setVisibility(View.VISIBLE);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment, camera)
                            .commit();
                    return true;
                case R.id.navigation_library:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment, library)
                            .commit();
                    return true;
                case R.id.navigation_settings:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment, settings)
                            .commit();
                    return true;
            }
            return false;
        }
    };
}
