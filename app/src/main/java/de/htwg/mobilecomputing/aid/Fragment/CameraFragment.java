package de.htwg.mobilecomputing.aid.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.htwg.mobilecomputing.aid.Activity.SettingsActivity;
import de.htwg.mobilecomputing.aid.Camera.CapturePhotoUtils;
import de.htwg.mobilecomputing.aid.R;

public class CameraFragment extends Fragment {
    private SharedPreferences sp;
    private WebView cameraView;
    private Button startCamera;
    private Button takePicture;

    private static boolean cameraOn = false;

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
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_camera));

        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        cameraView = view.findViewById(R.id.camera_view);

        //set height of camera view to correct aspect ratio
        final FrameLayout cameraFrame = view.findViewById(R.id.camera_frame);
        cameraFrame.post(new Runnable() {
            @Override
            public void run() {
                String aspectRatio = sp.getString("aspectRatio", "4:3");
                double a = Double.parseDouble(aspectRatio.substring(0, aspectRatio.indexOf(':')));
                int b = Integer.parseInt(aspectRatio.substring(aspectRatio.indexOf(':')+1, aspectRatio.length()));
                int height = (int)(Math.ceil((cameraFrame.getWidth() / a) * b));
                ViewGroup.LayoutParams params = cameraFrame.getLayoutParams();
                params.height = height;
                cameraFrame.setLayoutParams(params);
            }
        });

        startCamera = view.findViewById(R.id.button_start_camera);
        startCamera.setOnClickListener(startCameraOnClickListener);

        takePicture = view.findViewById(R.id.button_take_picture);
        takePicture.setOnClickListener(takePictureOnClickListener);

        setCamera();

        return view;
    }

    private final Button.OnClickListener startCameraOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isNetworkAvailable()) {
                cameraOn = !cameraOn;
                setCamera();
            } else
                Toast.makeText(getActivity(), getString(R.string.error_internet_connection), Toast.LENGTH_LONG).show();
        }
    };

    private void setCamera() {
        if(cameraOn) {
            takePicture.setEnabled(true);
            startCamera.setText(getString(R.string.button_stop_camera));
            cameraView.setVisibility(View.VISIBLE);
            cameraView.setWebViewClient(webViewClient);
            cameraView.getSettings().setLoadWithOverviewMode(true);
            cameraView.getSettings().setUseWideViewPort(true);
            String url = "http://" + sp.getString("ipKey", "127.0.0.1") + ":" + sp.getString("portKey", "22");
            cameraView.loadUrl(url);
        } else {
            takePicture.setEnabled(false);
            startCamera.setText(getString(R.string.button_start_camera));
            cameraView.setVisibility(View.GONE);
            cameraView.loadUrl("about:blank");
        }
    }

    private void setCamera(boolean on) {
        cameraOn = on;
        setCamera();
    }

    private final WebViewClient webViewClient = new WebViewClient() {
        /*@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }*/
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            switch(errorResponse.getStatusCode()) {
                case 401:
                    Toast.makeText(getActivity(), getString(R.string.error_login_credentials), Toast.LENGTH_LONG).show();
                    break;
                case 408:
                    Toast.makeText(getActivity(), getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "HTTP Error " + errorResponse.getStatusCode(), Toast.LENGTH_LONG).show();
            }
            setCamera(false);
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)  {
            switch(error.getErrorCode()) {
                case -1: //too many requests
                case -6: //connection refused
                    Toast.makeText(getActivity(), getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(), error.getErrorCode() + ": " + error.getDescription(), Toast.LENGTH_LONG).show();
            }
            setCamera(false);
        }
        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            handler.proceed(sp.getString("usernameKey", ""), sp.getString("passwordKey", ""));
        }
        /*@Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }*/
    };

    private final Button.OnClickListener takePictureOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Check for writing permission
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            else {
                //Capture bitmap
                Bitmap b = Bitmap.createBitmap(cameraView.getWidth(), cameraView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                cameraView.draw(c);

                //Save bitmap to phone gallery
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
                String result = CapturePhotoUtils.insertImage(getActivity().getContentResolver(), b, "AID_" + df.format(new Date()), getString(R.string.image_default_description));
                if (result != null) {
                    Toast.makeText(getActivity(), getString(R.string.success_image_saved), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_general), Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
