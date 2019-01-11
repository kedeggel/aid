package de.htwg.mobilecomputing.aid.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import de.htwg.mobilecomputing.aid.Fragment.CameraFragment;
import de.htwg.mobilecomputing.aid.Fragment.HomeFragment;
import de.htwg.mobilecomputing.aid.Fragment.LibraryFragment;
import de.htwg.mobilecomputing.aid.R;
import de.htwg.mobilecomputing.aid.Rest.HttpUtils;
import de.htwg.mobilecomputing.aid.Rest.RestCalls;
import de.htwg.mobilecomputing.aid.Support.ParameterManager;
import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static int active = R.id.navigation_home;

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final Fragment home = new HomeFragment();
    private final Fragment camera = new CameraFragment();
    private final Fragment library = new LibraryFragment();

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!ParameterManager.isRegistered(this) && ParameterManager.getToken(this) != null)
            sendToken(ParameterManager.getToken(this));

        //Setup Pushy
        if(!Pushy.isRegistered(this)) {
            new RegisterForPushNotificationsAsync().execute();
        }
        Pushy.listen(this);

        navigation = findViewById(R.id.navigation);
        if(savedInstanceState == null) {
            navigation.setSelectedItemId(active);
            navigate();
        }
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                active = menuItem.getItemId();
                return navigate();
            }
        });
    }

    private boolean navigate() {
        switch(active) {
            case R.id.navigation_home:
                fragmentManager.beginTransaction().replace(R.id.fragment, home).commit();
                return true;
            case R.id.navigation_camera:
                fragmentManager.beginTransaction().replace(R.id.fragment, camera).commit();
                return true;
            case R.id.navigation_library:
                fragmentManager.beginTransaction().replace(R.id.fragment, library).commit();
                return true;
        }
        return false;
    }

    private void sendToken(String token) {
        // Send the token to your backend server via an HTTP POST request
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        HttpUtils.setIp(sp.getString("ipKey", null));
        RestCalls.sendDeviceToken(getApplicationContext(), token, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "Pushy token successfully sent", Toast.LENGTH_LONG).show();
                ParameterManager.setRegistered(getApplicationContext(), true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Pushy token could not be sent", Toast.LENGTH_LONG).show();
                ParameterManager.setRegistered(getApplicationContext(), false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Show UI elements hidden in ImageFragment Landscape Orientation and Settings Menu
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getSupportActionBar().show();
        navigation.setVisibility(View.VISIBLE);
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        protected Exception doInBackground(Void... params) {
            try {
                // Assign a unique token to this device
                String deviceToken = Pushy.register(getApplicationContext());

                // Persist token in Shared Parameters
                ParameterManager.setToken(getApplicationContext(), deviceToken);

                // Send token to server
                sendToken(deviceToken);
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
