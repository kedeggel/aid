package de.htwg.mobilecomputing.aid.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.htwg.mobilecomputing.aid.Camera.CameraWebView;
import de.htwg.mobilecomputing.aid.Camera.CapturePhotoUtils;
import de.htwg.mobilecomputing.aid.R;

public class CameraFragment extends Fragment {
    private SharedPreferences sp;
    private WebView cameraView;
    private Button startCamera;
    private Button takePicture;
    private ProgressBar progressBar;

    private static boolean cameraOn = false;

    //private OnFragmentInteractionListener mListener;

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.camera));

        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        cameraView = view.findViewById(R.id.camera_view);

        //set height of camera view to correct aspect ratio
        FrameLayout cameraFrame = view.findViewById(R.id.camera_frame);
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

        progressBar = view.findViewById(R.id.progress_bar);

        setCamera();

        return view;
    }

    //todo: Remove if unnecessary
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)  {
        super.onViewCreated(view, savedInstanceState);

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
            startCamera.setText(getString(R.string.stop_camera));
            cameraView.setVisibility(View.VISIBLE);
            cameraView.setWebViewClient(webViewClient);
            cameraView.getSettings().setLoadWithOverviewMode(true);
            cameraView.getSettings().setUseWideViewPort(true);
            String url = "http://" + sp.getString("ipKey", "127.0.0.1") + ":" + sp.getString("portKey", "22");
            cameraView.loadUrl(url);
        } else {
            takePicture.setEnabled(false);
            startCamera.setText(getString(R.string.start_camera));
            cameraView.setVisibility(View.GONE);
            cameraView.loadUrl("about:blank");
            //progressBar.setVisibility(View.GONE);
        }
    }

    private void setCamera(boolean on) {
        cameraOn = on;
        setCamera();
    }

    private final WebViewClient webViewClient = new WebViewClient() {
        //todo: Show spinner while loading: https://stackoverflow.com/questions/11241513/android-progessbar-while-loading-webview
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
            Toast.makeText(getActivity(), error.getErrorCode() + ": " + error.getDescription(), Toast.LENGTH_LONG).show();
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
            //todo: take still image with camera and save in library: https://stackoverflow.com/questions/9745988/how-can-i-programmatically-take-a-screenshot-of-a-webview-capturing-the-full-pa
            Picture picture = cameraView.capturePicture();
            Bitmap b = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            picture.draw(c);
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String result = CapturePhotoUtils.insertImage(getActivity().getContentResolver(), b, "AID_" + df.format(new Date()), getString(R.string.default_description));
            if(result != null) {
                Toast.makeText(getActivity(), getString(R.string.success_image_saved), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_general), Toast.LENGTH_LONG).show();
            }
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }*/
}
