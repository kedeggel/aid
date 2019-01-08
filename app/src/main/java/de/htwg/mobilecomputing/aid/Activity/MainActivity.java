package de.htwg.mobilecomputing.aid.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.URL;

import cz.msebera.android.httpclient.Header;
import de.htwg.mobilecomputing.aid.Fragment.CameraFragment;
import de.htwg.mobilecomputing.aid.Fragment.HomeFragment;
import de.htwg.mobilecomputing.aid.Fragment.LibraryFragment;
import de.htwg.mobilecomputing.aid.Fragment.SettingsFragment;
import de.htwg.mobilecomputing.aid.R;
import de.htwg.mobilecomputing.aid.Rest.HttpUtils;
import de.htwg.mobilecomputing.aid.Rest.RestCalls;
import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final Fragment home = new HomeFragment();
    private final Fragment camera = new CameraFragment();
    private final Fragment library = new LibraryFragment();

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Pushy
        if(!Pushy.isRegistered(getApplicationContext())) {
            new RegisterForPushNotificationsAsync().execute();
        }
        Pushy.listen(this);

        navigation = findViewById(R.id.navigation);
        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, home).commit();
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Show UI elements hidden in ImageFragment Landscape Orientation and Settings Menu
        //todo: Check
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getSupportActionBar().show();
        //menu.findItem(R.id.action_settings).setVisible(true);
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

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        protected Exception doInBackground(Void... params) {
            try {
                // Assign a unique token to this device
                String deviceToken = Pushy.register(getApplicationContext());

                // Send the token to your backend server via an HTTP GET request
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                HttpUtils.setIp(sp.getString("ipKey", null));
                RestCalls.sendDeviceToken(getApplicationContext(), deviceToken, tokenResponseHandler);
            }
            catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }
            return null;
        }

        private AsyncHttpResponseHandler tokenResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "Token successfully sent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Token could not be sent", Toast.LENGTH_LONG).show();
                Pushy.unregister(getApplicationContext());
            }
        };

        @Override
        protected void onPostExecute(Exception exc) {
            // Failed?
            if (exc != null)
                Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_LONG).show();
            // Succeeded, optionally do something to alert the user
        }
    }
}
