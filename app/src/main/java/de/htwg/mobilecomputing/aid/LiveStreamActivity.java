package de.htwg.mobilecomputing.aid;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LiveStreamActivity extends AppCompatActivity {
    private final static String portLiveStream = "8081";
    private WebView wvLiveStream;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ip = preferences.getString(getString(R.string.pref_key_pi_ip), "raspberrypi");

        wvLiveStream = findViewById(R.id.wv_live_stream);
        WebSettings webSettings = wvLiveStream.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvLiveStream.setWebViewClient(new WebViewClient());
        wvLiveStream.loadUrl("https://" + ip + ":" + portLiveStream);
    }
}
