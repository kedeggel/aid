package de.htwg.mobilecomputing.aid.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import de.htwg.mobilecomputing.aid.R;
import de.htwg.mobilecomputing.aid.Rest.HttpUtils;
import de.htwg.mobilecomputing.aid.Rest.RestCalls;

public class HomeFragment extends Fragment {
    private TextView subtitle;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_home));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        HttpUtils.setIp(sp.getString("ipKey", null));

        subtitle = view.findViewById(R.id.subtitle);
        progressBar = view.findViewById(R.id.progress_bar);

        subtitle.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RestCalls.getAllDocs(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(new String(responseBody), JsonObject.class);
                int count = jsonObject.get("total_rows").getAsInt(); //todo: restrict to 24 hours
                progressBar.setVisibility(View.GONE);
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText(count + " " + getString(R.string.annotation_intrusions_detected));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
