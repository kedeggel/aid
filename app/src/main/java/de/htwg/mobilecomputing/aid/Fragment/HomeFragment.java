package de.htwg.mobilecomputing.aid.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import de.htwg.mobilecomputing.aid.Activity.SettingsActivity;
import de.htwg.mobilecomputing.aid.R;
import de.htwg.mobilecomputing.aid.Rest.HttpUtils;
import de.htwg.mobilecomputing.aid.Rest.RestCalls;

public class HomeFragment extends Fragment {
    private SwipeRefreshLayout swipe;
    private TextView subtitle1;
    private TextView subtitle2;
    private boolean loaded;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.toolbar_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_home));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        HttpUtils.setIp(sp.getString("ipKey", null));

        subtitle1 = view.findViewById(R.id.subtitle1);
        subtitle2 = view.findViewById(R.id.subtitle2);
        progressBar = view.findViewById(R.id.progress_bar);

        subtitle1.setVisibility(View.GONE);
        subtitle2.setVisibility(View.GONE);
        loaded = false;

        swipe = view.findViewById(R.id.swipeRefreshLayout);
        swipe.setOnRefreshListener(onRefreshListener);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //fill first subtitle
        RestCalls.getAllDocs(docsResponseHandler);

        //fill second subtitle
        RestCalls.getSelector(getContext(), 1, selectorResponseHandler);
    }

    private AsyncHttpResponseHandler docsResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(getActivity() != null) { //in case user navigated away
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(new String(responseBody), JsonObject.class);
                int count = jsonObject.getAsJsonArray("rows").
                        get(0).getAsJsonObject().get("value").getAsInt();
                if (count == 1)
                    subtitle1.setText(String.format(Locale.getDefault(), "%d %s", count, getString(R.string.annotation_single_intrusion_detected)));
                else
                    subtitle1.setText(String.format(Locale.getDefault(), "%d %s", count, getString(R.string.annotation_intrusions_detected)));
                subtitle1.setVisibility(View.VISIBLE);
                setProgressBarVisibility();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            if(getActivity() != null) {
                Toast.makeText(getActivity(), getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
                setProgressBarVisibility();
            }
        }
    };

    private AsyncHttpResponseHandler selectorResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(getActivity() != null) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(new String(responseBody), JsonObject.class);
                if(jsonObject.has("docs")) {
                    JsonObject doc = jsonObject.get("docs").getAsJsonArray().get(0).getAsJsonObject();
                    if(doc.has("timestamp")) {
                        long timestamp = doc.get("timestamp").getAsLong();
                        subtitle2.setText(String.format("%s: %s", getString(R.string.annotation_last_intrusion), DateUtils.getRelativeTimeSpanString(timestamp)));
                        subtitle2.setVisibility(View.VISIBLE);
                    }
                }
                setProgressBarVisibility();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            if(getActivity() != null) {
                Toast.makeText(getActivity(), getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
                setProgressBarVisibility();
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            RestCalls.getAllDocs(docsResponseHandler);
            RestCalls.getSelector(getContext(), 1, selectorResponseHandler);
        }
    };

    //every other time this method is called, the progress bars are turned off
    private void setProgressBarVisibility() {
        if(loaded) {
            progressBar.setVisibility(View.GONE);
            swipe.setRefreshing(false);
        }
        loaded = !loaded;
    }
}
