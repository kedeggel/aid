package de.htwg.mobilecomputing.aid.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.htwg.mobilecomputing.aid.Fragment.CameraFragment;
import de.htwg.mobilecomputing.aid.Fragment.HomeFragment;
import de.htwg.mobilecomputing.aid.Fragment.LibraryFragment;
import de.htwg.mobilecomputing.aid.Fragment.SettingsFragment;
import de.htwg.mobilecomputing.aid.R;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final Fragment home = HomeFragment.newInstance();
    private final Fragment camera = CameraFragment.newInstance();
    private final Fragment library = LibraryFragment.newInstance();
    private final Fragment settings = new SettingsFragment();

    private FragmentTransaction fragmentTransaction;
    private Menu menu;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = findViewById(R.id.navigation);
        if(savedInstanceState == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, home).commit();
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment, settings)
                        .commit();
                item.setVisible(false);
                return true;

            case R.id.action_share:
                //todo: Share image
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Show UI elements hidden in ImageFragment Landscape Orientation and Settings Menu
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getSupportActionBar().show();
        menu.findItem(R.id.action_settings).setVisible(true);
        navigation.setVisibility(View.VISIBLE);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment, home)
                            .commit();
                    return true;
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
            }
            return false;
        }
    };
}
